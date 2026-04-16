package swp391.old_bicycle_project.dto.response;

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
public class RefundResponseDTO {
    private UUID id;
    private UUID orderId;
    private UUID paymentId;
    private UUID requesterId;
    private String requesterName;
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

    // Manual Builder
    public static RefundResponseDTOBuilder builder() { return new RefundResponseDTOBuilder(); }
    public static class RefundResponseDTOBuilder {
        private RefundResponseDTO r = new RefundResponseDTO();
        public RefundResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public RefundResponseDTOBuilder orderId(UUID orderId) { r.orderId = orderId; return this; }
        public RefundResponseDTOBuilder paymentId(UUID paymentId) { r.paymentId = paymentId; return this; }
        public RefundResponseDTOBuilder requesterId(UUID requesterId) { r.requesterId = requesterId; return this; }
        public RefundResponseDTOBuilder requesterName(String requesterName) { r.requesterName = requesterName; return this; }
        public RefundResponseDTOBuilder amount(BigDecimal amount) { r.amount = amount; return this; }
        public RefundResponseDTOBuilder reason(String reason) { r.reason = reason; return this; }
        public RefundResponseDTOBuilder evidenceNote(String evidenceNote) { r.evidenceNote = evidenceNote; return this; }
        public RefundResponseDTOBuilder evidenceFiles(List<RefundEvidenceFileResponseDTO> evidenceFiles) { r.evidenceFiles = evidenceFiles; return this; }
        public RefundResponseDTOBuilder status(RefundStatus status) { r.status = status; return this; }
        public RefundResponseDTOBuilder adminNote(String adminNote) { r.adminNote = adminNote; return this; }
        public RefundResponseDTOBuilder refundReference(String refundReference) { r.refundReference = refundReference; return this; }
        public RefundResponseDTOBuilder reviewedBy(UUID reviewedBy) { r.reviewedBy = reviewedBy; return this; }
        public RefundResponseDTOBuilder reviewedByName(String reviewedByName) { r.reviewedByName = reviewedByName; return this; }
        public RefundResponseDTOBuilder reviewedAt(LocalDateTime reviewedAt) { r.reviewedAt = reviewedAt; return this; }
        public RefundResponseDTOBuilder processedAt(LocalDateTime processedAt) { r.processedAt = processedAt; return this; }
        public RefundResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public RefundResponseDTO build() { return r; }
    }
}
