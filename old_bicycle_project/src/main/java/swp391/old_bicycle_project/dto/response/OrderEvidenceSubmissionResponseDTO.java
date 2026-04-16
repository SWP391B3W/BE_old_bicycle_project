package swp391.old_bicycle_project.dto.response;

import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.OrderEvidenceType;
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
public class OrderEvidenceSubmissionResponseDTO {
    private UUID id;
    private OrderEvidenceType evidenceType;
    private UUID submittedByUserId;
    private String submittedByName;
    private AppRole submittedByRole;
    private String note;
    private LocalDateTime createdAt;
    private List<OrderEvidenceFileResponseDTO> files;

    // Manual Builder
    public static OrderEvidenceSubmissionResponseDTOBuilder builder() { return new OrderEvidenceSubmissionResponseDTOBuilder(); }
    public static class OrderEvidenceSubmissionResponseDTOBuilder {
        private OrderEvidenceSubmissionResponseDTO r = new OrderEvidenceSubmissionResponseDTO();
        public OrderEvidenceSubmissionResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public OrderEvidenceSubmissionResponseDTOBuilder evidenceType(OrderEvidenceType type) { r.evidenceType = type; return this; }
        public OrderEvidenceSubmissionResponseDTOBuilder submittedByUserId(UUID userId) { r.submittedByUserId = userId; return this; }
        public OrderEvidenceSubmissionResponseDTOBuilder submittedByName(String name) { r.submittedByName = name; return this; }
        public OrderEvidenceSubmissionResponseDTOBuilder submittedByRole(AppRole role) { r.submittedByRole = role; return this; }
        public OrderEvidenceSubmissionResponseDTOBuilder note(String note) { r.note = note; return this; }
        public OrderEvidenceSubmissionResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public OrderEvidenceSubmissionResponseDTOBuilder files(List<OrderEvidenceFileResponseDTO> files) { r.files = files; return this; }
        public OrderEvidenceSubmissionResponseDTO build() { return r; }
    }
}
