package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundCreateRequestDTO {

    @DecimalMin(value = "0.01", message = "Refund amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Refund reason is required")
    private String reason;

    private String evidenceNote;

    // Manual Getter
    public BigDecimal getAmount() { return amount; }
    public String getReason() { return reason; }
    public String getEvidenceNote() { return evidenceNote; }

    // Manual Builder
    public static RefundCreateRequestDTOBuilder builder() { return new RefundCreateRequestDTOBuilder(); }
    public static class RefundCreateRequestDTOBuilder {
        private RefundCreateRequestDTO r = new RefundCreateRequestDTO();
        public RefundCreateRequestDTOBuilder amount(BigDecimal amount) { r.amount = amount; return this; }
        public RefundCreateRequestDTOBuilder reason(String reason) { r.reason = reason; return this; }
        public RefundCreateRequestDTOBuilder evidenceNote(String evidenceNote) { r.evidenceNote = evidenceNote; return this; }
        public RefundCreateRequestDTO build() { return r; }
    }
}
