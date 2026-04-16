package swp391.old_bicycle_project.dto.response;

import swp391.old_bicycle_project.entity.enums.ReportReason;
import swp391.old_bicycle_project.entity.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponseDTO {
    private UUID id;
    private UUID reporterId;
    private String reporterName;
    private UUID targetId;
    private String targetType;
    private ReportReason reason;
    private String description;
    private List<ReportEvidenceFileResponseDTO> evidenceFiles;
    private ReportStatus status;
    private String adminNote;
    private UUID processedById;
    private String processedByName;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    // Manual Builder
    public static ReportResponseDTOBuilder builder() { return new ReportResponseDTOBuilder(); }
    public static class ReportResponseDTOBuilder {
        private ReportResponseDTO r = new ReportResponseDTO();
        public ReportResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public ReportResponseDTOBuilder reporterId(UUID reporterId) { r.reporterId = reporterId; return this; }
        public ReportResponseDTOBuilder reporterName(String reporterName) { r.reporterName = reporterName; return this; }
        public ReportResponseDTOBuilder targetId(UUID targetId) { r.targetId = targetId; return this; }
        public ReportResponseDTOBuilder targetType(String targetType) { r.targetType = targetType; return this; }
        public ReportResponseDTOBuilder reason(ReportReason reason) { r.reason = reason; return this; }
        public ReportResponseDTOBuilder description(String description) { r.description = description; return this; }
        public ReportResponseDTOBuilder evidenceFiles(List<ReportEvidenceFileResponseDTO> evidenceFiles) { r.evidenceFiles = evidenceFiles; return this; }
        public ReportResponseDTOBuilder status(ReportStatus status) { r.status = status; return this; }
        public ReportResponseDTOBuilder adminNote(String adminNote) { r.adminNote = adminNote; return this; }
        public ReportResponseDTOBuilder processedById(UUID processedById) { r.processedById = processedById; return this; }
        public ReportResponseDTOBuilder processedByName(String processedByName) { r.processedByName = processedByName; return this; }
        public ReportResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public ReportResponseDTOBuilder processedAt(LocalDateTime processedAt) { r.processedAt = processedAt; return this; }
        public ReportResponseDTO build() { return r; }
    }
}
