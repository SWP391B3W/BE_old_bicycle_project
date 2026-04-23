package swp391.old_bicycle_project.dto.response;

import swp391.old_bicycle_project.entity.enums.OrderFundingStatus;
import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.OrderCancelReason;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.entity.enums.PaymentOption;
import swp391.old_bicycle_project.entity.enums.PlatformFeeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private UUID id;
    private UUID productId;
    private String productTitle;
    private UUID buyerId;
    private String buyerName;
    private UUID sellerId;
    private String sellerName;
    private BigDecimal totalAmount;
    private BigDecimal depositAmount;
    private BigDecimal requiredUpfrontAmount;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private BigDecimal serviceFee;
    private BigDecimal feeBaseAmount;
    private BigDecimal platformFeeRate;
    private BigDecimal platformFeeTotal;
    private BigDecimal buyerFeeAmount;
    private BigDecimal sellerFeeAmount;
    private BigDecimal buyerChargeAmount;
    private BigDecimal sellerGrossPayoutAmount;
    private BigDecimal sellerNetPayoutAmount;
    private PlatformFeeStatus platformFeeStatus;
    private LocalDateTime platformFeeRecognizedAt;
    private LocalDateTime platformFeeReversedAt;
    private PaymentOption paymentOption;
    private OrderStatus status;
    private OrderFundingStatus fundingStatus;
    private PaymentMethod paymentMethod;
    private boolean buyerReviewSubmitted;
    private OrderEvidenceSubmissionResponseDTO sellerHandoverEvidence;
    private OrderEvidenceSubmissionResponseDTO buyerReceiptEvidence;
    private LocalDateTime acceptedAt;
    private LocalDateTime paymentDeadline;
    private LocalDateTime buyerConfirmationDeadline;
    private OrderCancelReason cancelReason;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String productStatus;
    private boolean productIsVerified;

    public static OrderResponseDTOBuilder builder() {
        return new OrderResponseDTOBuilder();
    }

    public static class OrderResponseDTOBuilder {
        private OrderResponseDTO k = new OrderResponseDTO();
        public OrderResponseDTOBuilder id(UUID id) { k.id = id; return this; }
        public OrderResponseDTOBuilder productId(UUID id) { k.productId = id; return this; }
        public OrderResponseDTOBuilder productTitle(String t) { k.productTitle = t; return this; }
        public OrderResponseDTOBuilder buyerId(UUID id) { k.buyerId = id; return this; }
        public OrderResponseDTOBuilder buyerName(String n) { k.buyerName = n; return this; }
        public OrderResponseDTOBuilder sellerId(UUID id) { k.sellerId = id; return this; }
        public OrderResponseDTOBuilder sellerName(String n) { k.sellerName = n; return this; }
        public OrderResponseDTOBuilder totalAmount(BigDecimal a) { k.totalAmount = a; return this; }
        public OrderResponseDTOBuilder depositAmount(BigDecimal a) { k.depositAmount = a; return this; }
        public OrderResponseDTOBuilder requiredUpfrontAmount(BigDecimal a) { k.requiredUpfrontAmount = a; return this; }
        public OrderResponseDTOBuilder paidAmount(BigDecimal a) { k.paidAmount = a; return this; }
        public OrderResponseDTOBuilder remainingAmount(BigDecimal a) { k.remainingAmount = a; return this; }
        public OrderResponseDTOBuilder serviceFee(BigDecimal a) { k.serviceFee = a; return this; }
        public OrderResponseDTOBuilder feeBaseAmount(BigDecimal a) { k.feeBaseAmount = a; return this; }
        public OrderResponseDTOBuilder platformFeeRate(BigDecimal a) { k.platformFeeRate = a; return this; }
        public OrderResponseDTOBuilder platformFeeTotal(BigDecimal a) { k.platformFeeTotal = a; return this; }
        public OrderResponseDTOBuilder buyerFeeAmount(BigDecimal a) { k.buyerFeeAmount = a; return this; }
        public OrderResponseDTOBuilder sellerFeeAmount(BigDecimal a) { k.sellerFeeAmount = a; return this; }
        public OrderResponseDTOBuilder buyerChargeAmount(BigDecimal a) { k.buyerChargeAmount = a; return this; }
        public OrderResponseDTOBuilder sellerGrossPayoutAmount(BigDecimal a) { k.sellerGrossPayoutAmount = a; return this; }
        public OrderResponseDTOBuilder sellerNetPayoutAmount(BigDecimal a) { k.sellerNetPayoutAmount = a; return this; }
        public OrderResponseDTOBuilder platformFeeStatus(PlatformFeeStatus s) { k.platformFeeStatus = s; return this; }
        public OrderResponseDTOBuilder platformFeeRecognizedAt(LocalDateTime d) { k.platformFeeRecognizedAt = d; return this; }
        public OrderResponseDTOBuilder platformFeeReversedAt(LocalDateTime d) { k.platformFeeReversedAt = d; return this; }
        public OrderResponseDTOBuilder paymentOption(PaymentOption o) { k.paymentOption = o; return this; }
        public OrderResponseDTOBuilder status(OrderStatus s) { k.status = s; return this; }
        public OrderResponseDTOBuilder fundingStatus(OrderFundingStatus s) { k.fundingStatus = s; return this; }
        public OrderResponseDTOBuilder paymentMethod(PaymentMethod m) { k.paymentMethod = m; return this; }
        public OrderResponseDTOBuilder buyerReviewSubmitted(boolean b) { k.buyerReviewSubmitted = b; return this; }
        public OrderResponseDTOBuilder sellerHandoverEvidence(OrderEvidenceSubmissionResponseDTO e) { k.sellerHandoverEvidence = e; return this; }
        public OrderResponseDTOBuilder buyerReceiptEvidence(OrderEvidenceSubmissionResponseDTO e) { k.buyerReceiptEvidence = e; return this; }
        public OrderResponseDTOBuilder acceptedAt(LocalDateTime d) { k.acceptedAt = d; return this; }
        public OrderResponseDTOBuilder paymentDeadline(LocalDateTime d) { k.paymentDeadline = d; return this; }
        public OrderResponseDTOBuilder buyerConfirmationDeadline(LocalDateTime d) { k.buyerConfirmationDeadline = d; return this; }
        public OrderResponseDTOBuilder cancelReason(OrderCancelReason r) { k.cancelReason = r; return this; }
        public OrderResponseDTOBuilder cancelledAt(LocalDateTime d) { k.cancelledAt = d; return this; }
        public OrderResponseDTOBuilder createdAt(LocalDateTime d) { k.createdAt = d; return this; }
        public OrderResponseDTOBuilder updatedAt(LocalDateTime d) { k.updatedAt = d; return this; }
        public OrderResponseDTOBuilder productStatus(String s) { k.productStatus = s; return this; }
        public OrderResponseDTOBuilder productIsVerified(boolean v) { k.productIsVerified = v; return this; }
        public OrderResponseDTO build() { return k; }
    }
}
