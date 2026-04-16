package swp391.old_bicycle_project.dto.response;

import swp391.old_bicycle_project.entity.enums.OrderFundingStatus;
import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.entity.enums.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRefundResponseDTO {
    private UUID id;
    private UUID orderId;
    private UUID paymentId;
    private UUID requesterId;
    private String requesterName;
    private UUID buyerId;
    private String buyerName;
    private UUID sellerId;
    private String sellerName;
    private UUID productId;
    private String productTitle;
    private boolean hasInspection;
    private BigDecimal amount;
    private String reason;
    private String evidenceNote;
    private List<RefundEvidenceFileResponseDTO> evidenceFiles;
    private RefundStatus status;
    private String adminNote;
    private String refundReference;
    private UUID reviewedBy;
    private String reviewedByName;
    private LocalDateTime reviewedAt;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private OrderEvidenceSubmissionResponseDTO sellerHandoverEvidence;
    private OrderEvidenceSubmissionResponseDTO buyerReceiptEvidence;
    private OrderStatus orderStatus;
    private OrderFundingStatus fundingStatus;
    private PaymentMethod paymentMethod;

    // Manual Builder
    public static AdminRefundResponseDTOBuilder builder() { return new AdminRefundResponseDTOBuilder(); }
    public static class AdminRefundResponseDTOBuilder {
        private AdminRefundResponseDTO r = new AdminRefundResponseDTO();
        public AdminRefundResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public AdminRefundResponseDTOBuilder orderId(UUID orderId) { r.orderId = orderId; return this; }
        public AdminRefundResponseDTOBuilder paymentId(UUID paymentId) { r.paymentId = paymentId; return this; }
        public AdminRefundResponseDTOBuilder requesterId(UUID requesterId) { r.requesterId = requesterId; return this; }
        public AdminRefundResponseDTOBuilder requesterName(String requesterName) { r.requesterName = requesterName; return this; }
        public AdminRefundResponseDTOBuilder buyerId(UUID buyerId) { r.buyerId = buyerId; return this; }
        public AdminRefundResponseDTOBuilder buyerName(String buyerName) { r.buyerName = buyerName; return this; }
        public AdminRefundResponseDTOBuilder sellerId(UUID sellerId) { r.sellerId = sellerId; return this; }
        public AdminRefundResponseDTOBuilder sellerName(String sellerName) { r.sellerName = sellerName; return this; }
        public AdminRefundResponseDTOBuilder productId(UUID productId) { r.productId = productId; return this; }
        public AdminRefundResponseDTOBuilder productTitle(String productTitle) { r.productTitle = productTitle; return this; }
        public AdminRefundResponseDTOBuilder hasInspection(boolean hasInspection) { r.hasInspection = hasInspection; return this; }
        public AdminRefundResponseDTOBuilder amount(BigDecimal amount) { r.amount = amount; return this; }
        public AdminRefundResponseDTOBuilder reason(String reason) { r.reason = reason; return this; }
        public AdminRefundResponseDTOBuilder evidenceNote(String evidenceNote) { r.evidenceNote = evidenceNote; return this; }
        public AdminRefundResponseDTOBuilder evidenceFiles(List<RefundEvidenceFileResponseDTO> evidenceFiles) { r.evidenceFiles = evidenceFiles; return this; }
        public AdminRefundResponseDTOBuilder status(RefundStatus status) { r.status = status; return this; }
        public AdminRefundResponseDTOBuilder adminNote(String adminNote) { r.adminNote = adminNote; return this; }
        public AdminRefundResponseDTOBuilder refundReference(String refundReference) { r.refundReference = refundReference; return this; }
        public AdminRefundResponseDTOBuilder reviewedBy(UUID reviewedBy) { r.reviewedBy = reviewedBy; return this; }
        public AdminRefundResponseDTOBuilder reviewedByName(String reviewedByName) { r.reviewedByName = reviewedByName; return this; }
        public AdminRefundResponseDTOBuilder reviewedAt(LocalDateTime reviewedAt) { r.reviewedAt = reviewedAt; return this; }
        public AdminRefundResponseDTOBuilder processedAt(LocalDateTime processedAt) { r.processedAt = processedAt; return this; }
        public AdminRefundResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public AdminRefundResponseDTOBuilder sellerHandoverEvidence(OrderEvidenceSubmissionResponseDTO sellerHandoverEvidence) { r.sellerHandoverEvidence = sellerHandoverEvidence; return this; }
        public AdminRefundResponseDTOBuilder buyerReceiptEvidence(OrderEvidenceSubmissionResponseDTO buyerReceiptEvidence) { r.buyerReceiptEvidence = buyerReceiptEvidence; return this; }
        public AdminRefundResponseDTOBuilder orderStatus(OrderStatus orderStatus) { r.orderStatus = orderStatus; return this; }
        public AdminRefundResponseDTOBuilder fundingStatus(OrderFundingStatus fundingStatus) { r.fundingStatus = fundingStatus; return this; }
        public AdminRefundResponseDTOBuilder paymentMethod(PaymentMethod paymentMethod) { r.paymentMethod = paymentMethod; return this; }
        public AdminRefundResponseDTO build() { return r; }
    }
}
