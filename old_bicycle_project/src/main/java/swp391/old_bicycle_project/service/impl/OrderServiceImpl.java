package swp391.old_bicycle_project.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swp391.old_bicycle_project.dto.request.OrderCreateRequestDTO;
import swp391.old_bicycle_project.dto.response.OrderEvidenceSubmissionResponseDTO;
import swp391.old_bicycle_project.dto.response.OrderResponseDTO;
import swp391.old_bicycle_project.entity.Order;
import swp391.old_bicycle_project.entity.Payout;
import swp391.old_bicycle_project.entity.Product;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.OrderCancelReason;
import swp391.old_bicycle_project.entity.enums.OrderEvidenceType;
import swp391.old_bicycle_project.entity.enums.OrderFundingStatus;
import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.PayoutStatus;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.entity.enums.PaymentOption;
import swp391.old_bicycle_project.entity.enums.PlatformFeeStatus;
import swp391.old_bicycle_project.entity.enums.ProductStatus;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.repository.InspectionRepository;
import swp391.old_bicycle_project.repository.OrderRepository;
import swp391.old_bicycle_project.repository.ProductRepository;
import swp391.old_bicycle_project.repository.ReviewRepository;
import swp391.old_bicycle_project.service.OrderEvidenceService;
import swp391.old_bicycle_project.service.OrderService;
import swp391.old_bicycle_project.service.PayoutService;
import swp391.old_bicycle_project.service.PlatformFeeService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PayoutService payoutService;
    private final OrderEvidenceService orderEvidenceService;
    private final PlatformFeeService platformFeeService;
    private final OrderTransitionSupport orderTransitionSupport;
    private final OrderViewSupport orderViewSupport;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            ReviewRepository reviewRepository,
            ApplicationEventPublisher eventPublisher,
            PayoutService payoutService,
            OrderEvidenceService orderEvidenceService,
            PlatformFeeService platformFeeService,
            InspectionRepository inspectionRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.payoutService = payoutService;
        this.orderEvidenceService = orderEvidenceService;
        this.platformFeeService = platformFeeService;
        this.orderTransitionSupport = new OrderTransitionSupport(orderRepository, productRepository, eventPublisher);
        this.orderViewSupport = new OrderViewSupport(reviewRepository, orderEvidenceService, platformFeeService, inspectionRepository);
    }

    @Override
    @Transactional
    // createOrder gồm lock product, validate quyền/trạng thái, tính upfront + platform fee,
    // tạo order pending và gửi notification cho seller.
    public OrderResponseDTO createOrder(User currentUser, OrderCreateRequestDTO requestDTO) {
        // 1. Lock product để tránh race condition khi nhiều người cùng đặt
        Product product = orderTransitionSupport.lockProduct(requestDTO.getProductId());

        // 2. Chặn seller tự mua sản phẩm của mình
        if (product.getSeller().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        // 3. Chỉ cho tạo đơn khi sản phẩm đang khả dụng
        if (product.getStatus() != ProductStatus.active && product.getStatus() != ProductStatus.inspected_passed) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        // 4. Nếu đã có exclusive lock thì không cho tạo thêm
        if (orderRepository.existsExclusiveOrderLockByProductId(product.getId())) {
            throw new AppException(ErrorCode.RECORD_ALREADY_EXISTS);
        }

        // 5. Chặn buyer tạo trùng đơn đang mở cho cùng sản phẩm
        if (orderRepository.existsByBuyerIdAndProductIdAndStatusIn(
                currentUser.getId(),
                product.getId(),
                List.of(OrderStatus.pending, OrderStatus.deposited, OrderStatus.awaiting_buyer_confirmation))) {
            throw new AppException(ErrorCode.ORDER_ALREADY_EXISTS);
        }

        PaymentOption paymentOption = requestDTO.getPaymentOption() != null
                ? requestDTO.getPaymentOption()
                : PaymentOption.partial;
        BigDecimal requiredUpfrontAmount = orderTransitionSupport.resolveRequiredUpfrontAmount(
                requestDTO,
                product.getPrice(),
                paymentOption);
            // 6. Tính phí nền tảng theo payment method
        PlatformFeeService.PlatformFeeQuote platformFeeQuote = platformFeeService.calculate(
                product.getPrice(),
                requiredUpfrontAmount,
                requestDTO.getPaymentMethod());

            // 7. Với partial payment: đảm bảo upfront đủ cover seller fee
        if (paymentOption == PaymentOption.partial
                && platformFeeQuote.sellerFeeAmount().compareTo(requiredUpfrontAmount) > 0) {
            throw new AppException(ErrorCode.UPFRONT_AMOUNT_TOO_LOW);
        }

            // 8. Tạo order ở trạng thái pending/unpaid
        Order order = orderRepository.save(Order.builder()
                .buyer(currentUser)
                .seller(product.getSeller())
                .product(product)
                .totalAmount(product.getPrice())
                .depositAmount(requiredUpfrontAmount)
                .requiredUpfrontAmount(requiredUpfrontAmount)
                .paidAmount(BigDecimal.ZERO)
                .remainingAmount(product.getPrice())
                .serviceFee(platformFeeQuote.platformFeeTotal())
                .feeBaseAmount(platformFeeQuote.feeBaseAmount())
                .platformFeeRate(platformFeeQuote.platformFeeRate())
                .platformFeeTotal(platformFeeQuote.platformFeeTotal())
                .buyerFeeAmount(platformFeeQuote.buyerFeeAmount())
                .sellerFeeAmount(platformFeeQuote.sellerFeeAmount())
                .buyerChargeAmount(platformFeeQuote.buyerChargeAmount())
                .sellerGrossPayoutAmount(platformFeeQuote.sellerGrossPayoutAmount())
                .sellerNetPayoutAmount(platformFeeQuote.sellerNetPayoutAmount())
                .platformFeeStatus(platformFeeQuote.platformFeeStatus())
                .paymentOption(paymentOption)
                .paymentMethod(requestDTO.getPaymentMethod())
                .fundingStatus(OrderFundingStatus.unpaid)
                .status(OrderStatus.pending)
                .build());

            // 9. Gửi notification cho seller có yêu cầu mua mới
        orderTransitionSupport.publishOrderNotification(
                order.getSeller().getId(),
                "Có yêu cầu mua mới",
                currentUser.getFullName() + " vừa tạo yêu cầu mua cho sản phẩm " + product.getTitle() + ".",
                "{\"orderId\":\"" + order.getId() + "\",\"productId\":\"" + product.getId() + "\"}");

        return orderViewSupport.mapToDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    // getMyOrders gồm lấy danh sách theo role (admin thấy tất cả, user thấy đơn liên quan)
    // và map sang DTO có enrich evidence/review.
    public List<OrderResponseDTO> getMyOrders(User currentUser) {
        List<Order> orders = currentUser.getRole() == AppRole.admin
                ? orderRepository.findAllByOrderByCreatedAtDesc()
                : orderRepository.findByBuyerIdOrSellerIdOrderByCreatedAtDesc(currentUser.getId(), currentUser.getId());
        return orderViewSupport.mapOrders(orders);
    }

    @Override
    @Transactional
    // acceptOrder gồm check quyền seller/admin, validate trạng thái, check payout profile,
    // set deadline thanh toán, reject các offer cạnh tranh và gửi notification cho buyer.
    public OrderResponseDTO acceptOrder(UUID orderId, User currentUser) {
        // 1. Lấy order và check quyền thao tác
        Order order = orderTransitionSupport.getOrder(orderId);
        orderTransitionSupport.validateSellerOrAdmin(order, currentUser);
        // 2. Lock product để đồng bộ trạng thái đơn liên quan
        orderTransitionSupport.lockProduct(order.getProduct().getId());

        // 3. Chỉ chấp nhận khi order đang pending + unpaid
        if (order.getStatus() != OrderStatus.pending || order.getFundingStatus() != OrderFundingStatus.unpaid) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
        if (orderRepository.existsExclusiveOrderLockByProductId(order.getProduct().getId())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
        // 4. Seller phải có payout profile trước khi nhận đơn
        if (!payoutService.hasCompleteProfile(order.getSeller())) {
            throw new AppException(ErrorCode.PAYOUT_PROFILE_REQUIRED);
        }

        // 5. Cập nhật mốc accept + deadline thanh toán 24h
        LocalDateTime acceptedAt = LocalDateTime.now();
        order.setAcceptedAt(acceptedAt);
        order.setPaymentDeadline(acceptedAt.plusHours(24));
        order.setFundingStatus(OrderFundingStatus.awaiting_payment);
        order.setCancelReason(null);
        order.setCancelledAt(null);
        order = orderRepository.save(order);
        // 6. Từ chối các pending offer cạnh tranh cùng sản phẩm
        orderTransitionSupport.rejectCompetingPendingOffers(order, acceptedAt);

        // 7. Thông báo cho buyer tiến hành thanh toán upfront
        orderTransitionSupport.publishOrderNotification(
                order.getBuyer().getId(),
                "Yêu cầu đặt cọc đã được chấp nhận",
                "Người bán đã chấp nhận đơn hàng và bạn có thể thanh toán khoản ứng trước qua SePay.",
                "{\"orderId\":\"" + order.getId() + "\"}"
        );

        return orderViewSupport.mapToDTO(order);
    }

    @Override
    @Transactional
    public OrderResponseDTO confirmDeposit(UUID orderId, User currentUser) {
        throw new AppException(ErrorCode.PAYMENT_METHOD_NOT_SUPPORTED);
    }

    @Override
    @Transactional
    // completeOrder gồm check quyền seller/admin, chuyển sang awaiting_buyer_confirmation,
    // tạo seller handover evidence và thông báo deadline 5 ngày cho buyer.
    public OrderResponseDTO completeOrder(UUID orderId, User currentUser, String note, List<MultipartFile> files) {
        // 1. Lấy order + check quyền
        Order order = orderTransitionSupport.getOrder(orderId);
        orderTransitionSupport.validateSellerOrAdmin(order, currentUser);

        // 2. Chỉ cho thao tác khi order đã deposited
        if (order.getStatus() != OrderStatus.deposited) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        // 3. Chuyển trạng thái chờ buyer xác nhận trong 5 ngày
        order.setStatus(OrderStatus.awaiting_buyer_confirmation);
        order.setBuyerConfirmationDeadline(LocalDateTime.now().plusDays(5));
        order = orderRepository.save(order);
        // 4. Tạo evidence phía seller (nếu có)
        OrderEvidenceSubmissionResponseDTO sellerEvidence = orderEvidenceService.createSellerHandoverEvidence(order,
                currentUser, note, files);

        // 5. Gửi notification cho buyer
        orderTransitionSupport.publishOrderNotification(
                order.getBuyer().getId(),
                "Người bán đã xác nhận gửi hàng",
                "Người bán đã tải bằng chứng gửi hàng. Bạn có 5 ngày để kiểm tra xe và xác nhận hoặc khiếu nại trước khi hệ thống tự hoàn tất đơn.",
                "{\"orderId\":\"" + order.getId() + "\",\"buyerConfirmationDeadline\":\"" + order.getBuyerConfirmationDeadline()
                        + "\"}"
        );

        return orderViewSupport.mapToDTO(
                order,
                Map.of(OrderEvidenceType.seller_handover, sellerEvidence));
    }

    @Override
    @Transactional
    // confirmReceived gồm check quyền buyer/admin, validate trạng thái,
    // rồi finalize giao dịch và release payout.
    public OrderResponseDTO confirmReceived(UUID orderId, User currentUser, String note, List<MultipartFile> files) {
        Order order = orderTransitionSupport.getOrder(orderId);
        orderTransitionSupport.validateBuyerOrAdmin(order, currentUser);

        if (order.getStatus() != OrderStatus.awaiting_buyer_confirmation) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        return finalizeOrderAfterBuyerConfirmation(order, currentUser, note, files, false);
    }

    @Override
    @Transactional
    // cancelOrder gồm check quyền hủy, validate trạng thái/funding cho phép,
    // cập nhật reason + trạng thái fee và gửi thông báo hủy đơn.
    public OrderResponseDTO cancelOrder(UUID orderId, User currentUser) {
        // 1. Lấy order và xác định ngữ cảnh seller reject request
        Order order = orderTransitionSupport.getOrder(orderId);
        boolean wasSellerReviewRequest = order.getStatus() == OrderStatus.pending
                && order.getFundingStatus() == OrderFundingStatus.unpaid
                && order.getAcceptedAt() == null;

        // 2. Check quyền buyer/seller/admin
        boolean canCancel = currentUser.getRole() == AppRole.admin
                || order.getBuyer().getId().equals(currentUser.getId())
                || order.getSeller().getId().equals(currentUser.getId());
        if (!canCancel) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        // 3. Chặn hủy với trạng thái đã hoàn tất/đã giữ tiền/đã hoàn tiền
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

        // 4. Cập nhật trạng thái hủy và funding tương ứng
        order.setStatus(OrderStatus.cancelled);
        if (order.getFundingStatus() == OrderFundingStatus.awaiting_payment) {
            order.setFundingStatus(OrderFundingStatus.unpaid);
        }
        // 5. Nếu chưa thu phí thì reset platform fee về not_applicable
        if (orderTransitionSupport.isUnpaidOrAwaitingPayment(order)
                && order.getPlatformFeeStatus() == PlatformFeeStatus.pending) {
            order.setPlatformFeeStatus(PlatformFeeStatus.not_applicable);
            order.setPlatformFeeRecognizedAt(null);
            order.setPlatformFeeReversedAt(null);
        }
        order.setCancelledAt(LocalDateTime.now());
        // 6. Set cancel reason theo actor thao tác
        if (currentUser.getRole() == AppRole.admin) {
            order.setCancelReason(OrderCancelReason.admin_cancelled);
        } else if (order.getSeller().getId().equals(currentUser.getId())) {
            order.setCancelReason(
                    wasSellerReviewRequest ? OrderCancelReason.seller_rejected : OrderCancelReason.seller_cancelled);
        } else {
            order.setCancelReason(OrderCancelReason.buyer_cancelled);
        }
        // 7. Lưu và publish notification
        Order savedOrder = orderRepository.save(order);
        orderTransitionSupport.publishCancellationNotifications(savedOrder, currentUser);
        return orderViewSupport.mapToDTO(savedOrder);
    }

    @Override
    @Transactional
    // autoCompleteOverdueBuyerConfirmations gồm quét đơn quá hạn buyer confirmation
    // và tự động finalize để giải phóng payout flow.
    public int autoCompleteOverdueBuyerConfirmations() {
        LocalDateTime now = LocalDateTime.now();
        List<Order> overdueOrders = orderRepository.findByStatusAndFundingStatusAndBuyerConfirmationDeadlineBefore(
                OrderStatus.awaiting_buyer_confirmation,
                OrderFundingStatus.held,
                now);

        overdueOrders.forEach(order -> finalizeOrderAfterBuyerConfirmation(order, null, null, Collections.emptyList(), true));
        return overdueOrders.size();
    }

    private OrderResponseDTO finalizeOrderAfterBuyerConfirmation(
            Order order,
            User currentUser,
            String note,
            List<MultipartFile> files,
            boolean autoCompleted) {
        // 1. Hoàn tất order + chuyển funding sang seller_payout_pending
        order.setStatus(OrderStatus.completed);
        order.setFundingStatus(OrderFundingStatus.seller_payout_pending);
        order.setBuyerConfirmationDeadline(null);
        order.getProduct().setStatus(ProductStatus.sold);
        productRepository.save(order.getProduct());
        order = orderRepository.save(order);

        // 2. Gom evidence hiện có của order
        Map<OrderEvidenceType, OrderEvidenceSubmissionResponseDTO> evidenceByType = new EnumMap<>(OrderEvidenceType.class);
        evidenceByType.putAll(orderEvidenceService.getEvidenceByOrderId(order.getId()));

        // 3. Nếu buyer xác nhận thủ công thì thêm buyer receipt evidence
        if (!autoCompleted && currentUser != null) {
            OrderEvidenceSubmissionResponseDTO buyerEvidence = orderEvidenceService.createBuyerReceiptEvidence(order,
                    currentUser, note, files);
            if (buyerEvidence != null) {
                evidenceByType.put(OrderEvidenceType.buyer_receipt, buyerEvidence);
            }
        }

        // 4. Tạo/cập nhật payout cho seller
        Payout payout = payoutService.ensureSellerReleasePayout(order);

        // 5. Gửi notification theo nhánh auto-complete hoặc manual-confirm
        if (autoCompleted) {
            orderTransitionSupport.publishOrderNotification(
                    order.getBuyer().getId(),
                    "Đơn hàng đã tự hoàn tất sau 5 ngày",
                    "Bạn không gửi khiếu nại trong vòng 5 ngày kể từ lúc người bán xác nhận gửi hàng, nên hệ thống đã tự hoàn tất đơn.",
                    "{\"orderId\":\"" + order.getId() + "\",\"payoutId\":\"" + payout.getId() + "\"}");
            orderTransitionSupport.publishOrderNotification(
                    order.getSeller().getId(),
                    "Đơn hàng đã tự hoàn tất",
                    payout.getStatus() == PayoutStatus.profile_required
                            ? "Đơn đã tự hoàn tất sau 5 ngày. Hãy cập nhật payout profile để nhận tiền bán xe sau khi trừ phí sàn."
                            : "Đơn đã tự hoàn tất sau 5 ngày. Tiền bán xe sau khi trừ phí sàn đã được đưa vào hàng chờ admin chuyển khoản.",
                    "{\"orderId\":\"" + order.getId() + "\",\"payoutId\":\"" + payout.getId() + "\"}");
        } else {
            orderTransitionSupport.publishOrderNotification(
                    order.getSeller().getId(),
                    "Người mua đã xác nhận nhận xe",
                    payout.getStatus() == PayoutStatus.profile_required
                            ? "Giao dịch đã hoàn tất. Hãy cập nhật payout profile để nhận tiền bán xe sau khi trừ phí sàn."
                            : "Giao dịch đã hoàn tất. Tiền bán xe sau khi trừ phí sàn đang chờ admin chuyển khoản cho bạn.",
                    "{\"orderId\":\"" + order.getId() + "\",\"payoutId\":\"" + payout.getId() + "\"}");
        }

        return orderViewSupport.mapToDTO(order, evidenceByType);
    }
}
