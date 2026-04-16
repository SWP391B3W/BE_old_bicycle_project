package swp391.old_bicycle_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEvidenceFileResponseDTO {
    private UUID id;
    private String fileUrl;
    private String fileName;
    private String contentType;
    private Integer sortOrder;

    // Manual Builder
    public static ReportEvidenceFileResponseDTOBuilder builder() { return new ReportEvidenceFileResponseDTOBuilder(); }
    public static class ReportEvidenceFileResponseDTOBuilder {
        private ReportEvidenceFileResponseDTO r = new ReportEvidenceFileResponseDTO();
        public ReportEvidenceFileResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public ReportEvidenceFileResponseDTOBuilder fileUrl(String fileUrl) { r.fileUrl = fileUrl; return this; }
        public ReportEvidenceFileResponseDTOBuilder fileName(String fileName) { r.fileName = fileName; return this; }
        public ReportEvidenceFileResponseDTOBuilder contentType(String contentType) { r.contentType = contentType; return this; }
        public ReportEvidenceFileResponseDTOBuilder sortOrder(Integer sortOrder) { r.sortOrder = sortOrder; return this; }
        public ReportEvidenceFileResponseDTO build() { return r; }
    }
}
