package swp391.old_bicycle_project.dto.request;

import swp391.old_bicycle_project.entity.enums.ReportReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequestDTO {

    @NotNull(message = "Target ID is required")
    private UUID targetId;

    @NotBlank(message = "Target type is required (e.g., PRODUCT, USER)")
    private String targetType;

    @NotNull(message = "Reason is required")
    private ReportReason reason;

    private String description;

    // Manual Getter
    public UUID getTargetId() { return targetId; }
    public String getTargetType() { return targetType; }
    public ReportReason getReason() { return reason; }
    public String getDescription() { return description; }

    // Manual Setters
    public void setTargetId(UUID targetId) { this.targetId = targetId; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public void setReason(ReportReason reason) { this.reason = reason; }
    public void setDescription(String description) { this.description = description; }

    // Manual Builder
    public static ReportRequestDTOBuilder builder() { return new ReportRequestDTOBuilder(); }
    public static class ReportRequestDTOBuilder {
        private ReportRequestDTO r = new ReportRequestDTO();
        public ReportRequestDTOBuilder targetId(UUID targetId) { r.targetId = targetId; return this; }
        public ReportRequestDTOBuilder targetType(String targetType) { r.targetType = targetType; return this; }
        public ReportRequestDTOBuilder reason(ReportReason reason) { r.reason = reason; return this; }
        public ReportRequestDTOBuilder description(String description) { r.description = description; return this; }
        public ReportRequestDTO build() { return r; }
    }
}
