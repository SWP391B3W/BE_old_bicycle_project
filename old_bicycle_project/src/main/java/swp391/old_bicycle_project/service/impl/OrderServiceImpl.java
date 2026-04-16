package swp391.old_bicycle_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swp391.old_bicycle_project.dto.order.OrderCreateRequest;
import swp391.old_bicycle_project.dto.order.OrderResponse;
import swp391.old_bicycle_project.entity.Order;
import swp391.old_bicycle_project.entity.OrderEvidenceSubmission;
import swp391.old_bicycle_project.entity.Product;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.OrderCancelReason;
import swp391.old_bicycle_project.entity.enums.OrderEvidenceType;
import swp391.old_bicycle_project.entity.enums.OrderFundingStatus;
import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.entity.enums.PaymentOption;
import swp391.old_bicycle_project.entity.enums.PlatformFeeStatus;
import swp391.old_bicycle_project.entity.enums.ProductStatus;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.repository.OrderRepository;
import swp391.old_bicycle_project.repository.ProductRepository;
import swp391.old_bicycle_project.service.OrderEvidenceService;
import swp391.old_bicycle_project.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderEvidenceService orderEvidenceService;

    @Override
    @Transactional
    public OrderResponse createOrder(User currentUser, OrderCreateRequest request) {
        UUID productId = request.getProductId();
        if (productId == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getSeller() == null) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
        if (product.getSeller().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        if (product.getStatus() != ProductStatus.active && product.getStatus() != ProductStatus.inspected_passed) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        if (orderRepository.existsExclusiveOrderLockByProductId(product.getId())) {
            throw new AppException(ErrorCode.RECORD_ALREADY_EXISTS);
        }

        PaymentOption paymentOption = request.getPaymentOption() != null ? request.getPaymentOption() : PaymentOption.partial;
        BigDecimal total = product.getPrice();
        BigDecimal requiredUpfront = resolveRequiredUpfrontAmount(total, paymentOption, request.getUpfrontAmount());

        Order order = Order.builder()
                .buyer(currentUser)
                .seller(product.getSeller())
                .product(product)
                .totalAmount(total)
                .depositAmount(requiredUpfront)
                .requiredUpfrontAmount(requiredUpfront)
                .paidAmount(BigDecimal.ZERO)
                .remainingAmount(total)
                .serviceFee(BigDecimal.ZERO)
                .platformFeeTotal(BigDecimal.ZERO)
                .platformFeeStatus(PlatformFeeStatus.not_applicable)
                .paymentOption(paymentOption)
                .paymentMethod(request.getPaymentMethod())
                .fundingStatus(OrderFundingStatus.unpaid)
                .status(OrderStatus.pending)
                .build();

        Order savedOrder = persistOrder(order);
        return map(savedOrder, Map.of());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(User currentUser) {
        List<Order> orders = currentUser.getRole() == AppRole.admin
                ? orderRepository.findAllByOrderByCreatedAtDesc()
                : orderRepository.findByBuyerIdOrSellerIdOrderByCreatedAtDesc(currentUser.getId(), currentUser.getId());

        Map<UUID, Map<OrderEvidenceType, OrderEvidenceSubmission>> evidenceByOrderId =
                orderEvidenceService.getEvidenceByOrderIds(orders.stream().map(Order::getId).toList());

        return orders.stream()
                .map(order -> map(order, evidenceByOrderId.getOrDefault(order.getId(), Map.of())))
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse acceptOrder(UUID orderId, User currentUser) {
        Order order = getOrder(orderId);
        validateSellerOrAdmin(order, currentUser);

        if (order.getStatus() != OrderStatus.pending || order.getFundingStatus() != OrderFundingStatus.unpaid) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        LocalDateTime now = LocalDateTime.now();
        order.setAcceptedAt(now);
        order.setPaymentDeadline(now.plusHours(24));
        order.setFundingStatus(OrderFundingStatus.awaiting_payment);
        order.setCancelReason(null);
        order.setCancelledAt(null);

        Order savedOrder = persistOrder(order);
        return map(savedOrder, orderEvidenceService.getEvidenceByOrderId(savedOrder.getId()));
    }

    @Override
    @Transactional
    public OrderResponse confirmDeposit(UUID orderId, User currentUser) {
        Order order = getOrder(orderId);
        validateSellerOrAdmin(order, currentUser);

        if (order.getStatus() != OrderStatus.pending) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
        if (order.getPaymentMethod() != PaymentMethod.cash) {
            throw new AppException(ErrorCode.PAYMENT_METHOD_NOT_SUPPORTED);
        }
        if (order.getFundingStatus() != OrderFundingStatus.awaiting_payment
                || order.getAcceptedAt() == null
                || order.getPaymentDeadline() == null) {
            throw new AppException(ErrorCode.PAYMENT_NOT_READY);
        }

        if (order.getPaymentDeadline().isBefore(LocalDateTime.now())) {
            order.setStatus(OrderStatus.cancelled);
            order.setCancelReason(OrderCancelReason.payment_expired);
            order.setCancelledAt(LocalDateTime.now());
            order.setFundingStatus(OrderFundingStatus.unpaid);
            persistOrder(order);
            throw new AppException(ErrorCode.PAYMENT_EXPIRED);
        }

        order.setStatus(OrderStatus.deposited);
        order.setPaidAmount(order.getRequiredUpfrontAmount());
        order.setRemainingAmount(order.getTotalAmount().subtract(order.getRequiredUpfrontAmount()));
        order.setFundingStatus(OrderFundingStatus.held);
        Order savedOrder = persistOrder(order);
        return map(savedOrder, orderEvidenceService.getEvidenceByOrderId(savedOrder.getId()));
    }

    @Override
    @Transactional
    public OrderResponse completeOrder(UUID orderId, User currentUser, String note, List<MultipartFile> files) {
        Order order = getOrder(orderId);
        validateSellerOrAdmin(order, currentUser);

        if (order.getStatus() != OrderStatus.deposited) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        if (files == null || files.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_EVIDENCE_REQUIRED);
        }
        orderEvidenceService.createSellerHandoverEvidence(order, currentUser, note, files);

        order.setStatus(OrderStatus.awaiting_buyer_confirmation);
        Order savedOrder = persistOrder(order);
        return map(savedOrder, orderEvidenceService.getEvidenceByOrderId(savedOrder.getId()));
    }

    @Override
    @Transactional
    public OrderResponse confirmReceived(UUID orderId, User currentUser, String note, List<MultipartFile> files) {
        Order order = getOrder(orderId);
        validateBuyerOrAdmin(order, currentUser);

        if (order.getStatus() != OrderStatus.awaiting_buyer_confirmation
                || order.getFundingStatus() != OrderFundingStatus.held) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
        orderEvidenceService.createBuyerReceiptEvidence(order, currentUser, note, files);

        order.setStatus(OrderStatus.completed);
        order.setFundingStatus(OrderFundingStatus.seller_payout_pending);

        Product product = order.getProduct();
        product.setStatus(ProductStatus.sold);
        productRepository.save(product);

        Order savedOrder = persistOrder(order);
        return map(savedOrder, orderEvidenceService.getEvidenceByOrderId(savedOrder.getId()));
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID orderId, User currentUser) {
        Order order = getOrder(orderId);

        boolean wasSellerReviewRequest = order.getStatus() == OrderStatus.pending
                && order.getFundingStatus() == OrderFundingStatus.unpaid
                && order.getAcceptedAt() == null;

        boolean canCancel = currentUser.getRole() == AppRole.admin
                || order.getBuyer().getId().equals(currentUser.getId())
                || order.getSeller().getId().equals(currentUser.getId());
        if (!canCancel) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        if (order.getStatus() == OrderStatus.completed
                || order.getStatus() == OrderStatus.cancelled
                || order.getStatus() == OrderStatus.deposited
                || order.getStatus() == OrderStatus.awaiting_buyer_confirmation
                || order.getFundingStatus() == OrderFundingStatus.held
                || order.getFundingStatus() == OrderFundingStatus.refund_pending
                || order.getFundingStatus() == OrderFundingStatus.refund_pending_transfer
                || order.getFundingStatus() == OrderFundingStatus.seller_payout_pending
                || order.getFundingStatus() == OrderFundingStatus.released
                || order.getFundingStatus() == OrderFundingStatus.refunded) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        order.setStatus(OrderStatus.cancelled);
        if (order.getFundingStatus() == OrderFundingStatus.awaiting_payment) {
            order.setFundingStatus(OrderFundingStatus.unpaid);
        }

        order.setCancelledAt(LocalDateTime.now());
        if (currentUser.getRole() == AppRole.admin) {
            order.setCancelReason(OrderCancelReason.admin_cancelled);
        } else if (order.getSeller().getId().equals(currentUser.getId())) {
            order.setCancelReason(wasSellerReviewRequest
                    ? OrderCancelReason.seller_rejected
                    : OrderCancelReason.seller_cancelled);
        } else {
            order.setCancelReason(OrderCancelReason.buyer_cancelled);
        }

        Order savedOrder = persistOrder(order);
        return map(savedOrder, orderEvidenceService.getEvidenceByOrderId(savedOrder.getId()));
    }

    private BigDecimal resolveRequiredUpfrontAmount(BigDecimal total, PaymentOption paymentOption, BigDecimal upfrontAmount) {
        if (paymentOption == PaymentOption.full) {
            return total;
        }

        if (upfrontAmount != null) {
            if (upfrontAmount.compareTo(BigDecimal.ZERO) <= 0 || upfrontAmount.compareTo(total) > 0) {
                throw new AppException(ErrorCode.PAYMENT_VALIDATION_FAILED);
            }
            return upfrontAmount;
        }

        return total.multiply(new BigDecimal("0.30")).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private Order getOrder(UUID orderId) {
        if (orderId == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST_BODY);
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTS));
    }

    private void validateSellerOrAdmin(Order order, User currentUser) {
        if (currentUser.getRole() == AppRole.admin) {
            return;
        }
        if (!order.getSeller().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
    }

    private void validateBuyerOrAdmin(Order order, User currentUser) {
        if (currentUser.getRole() == AppRole.admin) {
            return;
        }
        if (!order.getBuyer().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
    }

    private OrderResponse map(Order order, Map<OrderEvidenceType, OrderEvidenceSubmission> evidenceByType) {
        OrderEvidenceSubmission sellerHandoverEvidence = evidenceByType.get(OrderEvidenceType.seller_handover);
        OrderEvidenceSubmission buyerReceiptEvidence = evidenceByType.get(OrderEvidenceType.buyer_receipt);

        return OrderResponse.builder()
                .id(order.getId())
                .productId(order.getProduct() != null ? order.getProduct().getId() : null)
                .productTitle(order.getProduct() != null ? order.getProduct().getTitle() : null)
                .buyerId(order.getBuyer() != null ? order.getBuyer().getId() : null)
                .buyerName(order.getBuyer() != null ? order.getBuyer().getFullName() : null)
                .sellerId(order.getSeller() != null ? order.getSeller().getId() : null)
                .sellerName(order.getSeller() != null ? order.getSeller().getFullName() : null)
                .totalAmount(order.getTotalAmount())
                .depositAmount(order.getDepositAmount())
                .requiredUpfrontAmount(order.getRequiredUpfrontAmount())
                .paidAmount(order.getPaidAmount())
                .remainingAmount(order.getRemainingAmount())
                .serviceFee(order.getServiceFee())
                .platformFeeTotal(order.getPlatformFeeTotal())
                .platformFeeStatus(order.getPlatformFeeStatus())
                .paymentOption(order.getPaymentOption())
                .status(order.getStatus())
                .fundingStatus(order.getFundingStatus())
                .paymentMethod(order.getPaymentMethod())
                .sellerHandoverNote(sellerHandoverEvidence != null ? sellerHandoverEvidence.getNote() : null)
                .sellerHandoverImageUrls(extractFileUrls(sellerHandoverEvidence))
                .buyerReceiptNote(buyerReceiptEvidence != null ? buyerReceiptEvidence.getNote() : null)
                .buyerReceiptImageUrls(extractFileUrls(buyerReceiptEvidence))
                .acceptedAt(order.getAcceptedAt())
                .paymentDeadline(order.getPaymentDeadline())
                .cancelReason(order.getCancelReason())
                .cancelledAt(order.getCancelledAt())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private List<String> extractFileUrls(OrderEvidenceSubmission submission) {
        if (submission == null || submission.getFiles() == null || submission.getFiles().isEmpty()) {
            return List.of();
        }

        return submission.getFiles().stream()
                .map(file -> file.getFileUrl())
                .toList();
    }

    private Order persistOrder(Order order) {
        return orderRepository.save(Objects.requireNonNull(order));
    }
}
