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
public class OrderEvidenceFileResponseDTO {
    private UUID id;
    private String fileUrl;
    private String fileName;
    private String contentType;
    private Integer sortOrder;

    // Manual Builder
    public static OrderEvidenceFileResponseDTOBuilder builder() { return new OrderEvidenceFileResponseDTOBuilder(); }
    public static class OrderEvidenceFileResponseDTOBuilder {
        private OrderEvidenceFileResponseDTO r = new OrderEvidenceFileResponseDTO();
        public OrderEvidenceFileResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public OrderEvidenceFileResponseDTOBuilder fileUrl(String fileUrl) { r.fileUrl = fileUrl; return this; }
        public OrderEvidenceFileResponseDTOBuilder fileName(String fileName) { r.fileName = fileName; return this; }
        public OrderEvidenceFileResponseDTOBuilder contentType(String contentType) { r.contentType = contentType; return this; }
        public OrderEvidenceFileResponseDTOBuilder sortOrder(Integer sortOrder) { r.sortOrder = sortOrder; return this; }
        public OrderEvidenceFileResponseDTO build() { return r; }
    }
}
