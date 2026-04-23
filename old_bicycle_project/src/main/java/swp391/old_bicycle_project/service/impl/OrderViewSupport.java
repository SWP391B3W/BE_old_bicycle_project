package swp391.old_bicycle_project.service.impl;

import swp391.old_bicycle_project.dto.response.OrderEvidenceSubmissionResponseDTO;
import swp391.old_bicycle_project.dto.response.OrderResponseDTO;
import swp391.old_bicycle_project.entity.Inspection;
import swp391.old_bicycle_project.entity.Order;
import swp391.old_bicycle_project.entity.enums.OrderEvidenceType;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.repository.InspectionRepository;
import swp391.old_bicycle_project.repository.ReviewRepository;
import swp391.old_bicycle_project.service.OrderEvidenceService;
import swp391.old_bicycle_project.service.PlatformFeeService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

final class OrderViewSupport {

    private final ReviewRepository reviewRepository;
    private final OrderEvidenceService orderEvidenceService;
    private final PlatformFeeService platformFeeService;
    private final InspectionRepository inspectionRepository;

    OrderViewSupport(
            ReviewRepository reviewRepository,
            OrderEvidenceService orderEvidenceService,
            PlatformFeeService platformFeeService,
            InspectionRepository inspectionRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.orderEvidenceService = orderEvidenceService;
        this.platformFeeService = platformFeeService;
        this.inspectionRepository = inspectionRepository;
    }

    List<OrderResponseDTO> mapOrders(List<Order> orders) {
        Set<UUID> reviewedOrderIds = orders.isEmpty()
                ? Collections.emptySet()
                : reviewRepository.findReviewedOrderIdsByOrderIds(orders.stream().map(Order::getId).toList());
        Map<UUID, Map<OrderEvidenceType, OrderEvidenceSubmissionResponseDTO>> evidenceByOrder =
                orderEvidenceService.getEvidenceByOrderIds(orders.stream().map(Order::getId).toList());

        return orders.stream()
                .map(order -> mapToDTO(
                        order,
                        reviewedOrderIds.contains(order.getId()),
                        evidenceByOrder.getOrDefault(order.getId(), Collections.emptyMap())
                ))
                .toList();
    }

    OrderResponseDTO mapToDTO(Order order) {
        return mapToDTO(
                order,
                reviewRepository.existsByOrderId(order.getId()),
                orderEvidenceService.getEvidenceByOrderId(order.getId())
        );
    }

    OrderResponseDTO mapToDTO(
            Order order,
            Map<OrderEvidenceType, OrderEvidenceSubmissionResponseDTO> evidenceByType
    ) {
        return mapToDTO(order, reviewRepository.existsByOrderId(order.getId()), evidenceByType);
    }

    OrderResponseDTO mapToDTO(
            Order order,
            boolean buyerReviewSubmitted,
            Map<OrderEvidenceType, OrderEvidenceSubmissionResponseDTO> evidenceByType
    ) {
        PlatformFeeService.PlatformFeeQuote feeQuote = shouldNormalizeFeeSnapshot(order)
                ? platformFeeService.calculate(
                        order.getTotalAmount(),
                        order.getRequiredUpfrontAmount(),
                        order.getPaymentMethod()
                )
                : null;

        return OrderResponseDTO.builder()
                .id(order.getId())
                .productId(order.getProduct().getId())
                .productTitle(order.getProduct().getTitle())
                .buyerId(order.getBuyer().getId())
                .buyerName(order.getBuyer().getFullName())
                .sellerId(order.getSeller().getId())
                .sellerName(order.getSeller().getFullName())
                .totalAmount(order.getTotalAmount())
                .depositAmount(order.getDepositAmount())
                .requiredUpfrontAmount(order.getRequiredUpfrontAmount())
                .paidAmount(order.getPaidAmount())
                .remainingAmount(order.getRemainingAmount())
                .serviceFee(feeQuote != null ? feeQuote.platformFeeTotal() : order.getServiceFee())
                .feeBaseAmount(feeQuote != null ? feeQuote.feeBaseAmount() : order.getFeeBaseAmount())
                .platformFeeRate(feeQuote != null ? feeQuote.platformFeeRate() : order.getPlatformFeeRate())
                .platformFeeTotal(feeQuote != null ? feeQuote.platformFeeTotal() : order.getPlatformFeeTotal())
                .buyerFeeAmount(feeQuote != null ? feeQuote.buyerFeeAmount() : order.getBuyerFeeAmount())
                .sellerFeeAmount(feeQuote != null ? feeQuote.sellerFeeAmount() : order.getSellerFeeAmount())
                .buyerChargeAmount(feeQuote != null ? feeQuote.buyerChargeAmount() : order.getBuyerChargeAmount())
                .sellerGrossPayoutAmount(feeQuote != null ? feeQuote.sellerGrossPayoutAmount() : order.getSellerGrossPayoutAmount())
                .sellerNetPayoutAmount(feeQuote != null ? feeQuote.sellerNetPayoutAmount() : order.getSellerNetPayoutAmount())
                .platformFeeStatus(order.getPlatformFeeStatus())
                .platformFeeRecognizedAt(order.getPlatformFeeRecognizedAt())
                .platformFeeReversedAt(order.getPlatformFeeReversedAt())
                .paymentOption(order.getPaymentOption())
                .status(order.getStatus())
                .fundingStatus(order.getFundingStatus())
                .paymentMethod(order.getPaymentMethod())
                .buyerReviewSubmitted(buyerReviewSubmitted)
                .sellerHandoverEvidence(evidenceByType.get(OrderEvidenceType.seller_handover))
                .buyerReceiptEvidence(evidenceByType.get(OrderEvidenceType.buyer_receipt))
                .acceptedAt(order.getAcceptedAt())
                .paymentDeadline(order.getPaymentDeadline())
                .buyerConfirmationDeadline(order.getBuyerConfirmationDeadline())
                .cancelReason(order.getCancelReason())
                .cancelledAt(order.getCancelledAt())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .productStatus(order.getProduct().getStatus().name())
                .productIsVerified(isProductVerified(order))
                .build();
    }

    private boolean isProductVerified(Order order) {
        Inspection inspection = inspectionRepository.findByProductId(order.getProduct().getId()).orElse(null);
        return inspection != null
                && Boolean.TRUE.equals(inspection.getPassed())
                && inspection.getValidUntil() != null
                && inspection.getValidUntil().isAfter(LocalDateTime.now())
                && order.getProduct().getDeletedAt() == null;
    }

        private boolean shouldNormalizeFeeSnapshot(Order order) {
                if (order.getPaymentMethod() == null || order.getPaymentMethod() == PaymentMethod.cash) {
                        return false;
                }
                return (order.getBuyerFeeAmount() != null && order.getBuyerFeeAmount().compareTo(java.math.BigDecimal.ZERO) > 0)
                                || (order.getBuyerChargeAmount() != null
                                && order.getRequiredUpfrontAmount() != null
                                && order.getBuyerChargeAmount().compareTo(order.getRequiredUpfrontAmount()) > 0);
        }
}
