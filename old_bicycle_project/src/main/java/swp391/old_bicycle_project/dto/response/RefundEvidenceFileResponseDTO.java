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
public class RefundEvidenceFileResponseDTO {
    private UUID id;
    private String fileUrl;
    private String fileName;
    private String contentType;
    private Integer sortOrder;

    // Manual Builder
    public static RefundEvidenceFileResponseDTOBuilder builder() { return new RefundEvidenceFileResponseDTOBuilder(); }
    public static class RefundEvidenceFileResponseDTOBuilder {
        private RefundEvidenceFileResponseDTO r = new RefundEvidenceFileResponseDTO();
        public RefundEvidenceFileResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public RefundEvidenceFileResponseDTOBuilder fileUrl(String fileUrl) { r.fileUrl = fileUrl; return this; }
        public RefundEvidenceFileResponseDTOBuilder fileName(String fileName) { r.fileName = fileName; return this; }
        public RefundEvidenceFileResponseDTOBuilder contentType(String contentType) { r.contentType = contentType; return this; }
        public RefundEvidenceFileResponseDTOBuilder sortOrder(Integer sortOrder) { r.sortOrder = sortOrder; return this; }
        public RefundEvidenceFileResponseDTO build() { return r; }
    }
}
