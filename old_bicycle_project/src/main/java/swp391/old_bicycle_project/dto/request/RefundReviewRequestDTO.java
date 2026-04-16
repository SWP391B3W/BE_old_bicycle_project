package swp391.old_bicycle_project.dto.request;

import swp391.old_bicycle_project.entity.enums.RefundStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundReviewRequestDTO {

    @NotNull(message = "Target refund status is required")
    private RefundStatus status;

    private String adminNote;

    private String refundReference;

    // Manual Getter
    public RefundStatus getStatus() { return status; }
    public String getAdminNote() { return adminNote; }
    public String getRefundReference() { return refundReference; }

    // Manual Builder
    public static RefundReviewRequestDTOBuilder builder() { return new RefundReviewRequestDTOBuilder(); }
    public static class RefundReviewRequestDTOBuilder {
        private RefundReviewRequestDTO r = new RefundReviewRequestDTO();
        public RefundReviewRequestDTOBuilder status(RefundStatus status) { r.status = status; return this; }
        public RefundReviewRequestDTOBuilder adminNote(String adminNote) { r.adminNote = adminNote; return this; }
        public RefundReviewRequestDTOBuilder refundReference(String refundReference) { r.refundReference = refundReference; return this; }
        public RefundReviewRequestDTO build() { return r; }
    }
}
