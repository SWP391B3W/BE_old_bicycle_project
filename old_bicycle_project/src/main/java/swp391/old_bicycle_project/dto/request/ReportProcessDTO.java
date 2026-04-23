package swp391.old_bicycle_project.dto.request;

import swp391.old_bicycle_project.entity.enums.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportProcessDTO {
    
    @NotNull(message = "Status is required")
    private ReportStatus status;
    
    private String adminNote;

    // Manual Getter
    public ReportStatus getStatus() { return status; }
    public String getAdminNote() { return adminNote; }

    // Manual Setters
    public void setStatus(ReportStatus status) { this.status = status; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }

    // Manual Builder
    public static ReportProcessDTOBuilder builder() { return new ReportProcessDTOBuilder(); }
    public static class ReportProcessDTOBuilder {
        private ReportProcessDTO r = new ReportProcessDTO();
        public ReportProcessDTOBuilder status(ReportStatus status) { r.status = status; return this; }
        public ReportProcessDTOBuilder adminNote(String adminNote) { r.adminNote = adminNote; return this; }
        public ReportProcessDTO build() { return r; }
    }
}
