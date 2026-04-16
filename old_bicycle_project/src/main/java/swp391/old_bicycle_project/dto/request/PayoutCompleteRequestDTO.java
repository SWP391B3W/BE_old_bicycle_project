package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayoutCompleteRequestDTO {

    @NotBlank(message = "Bank reference is required")
    private String bankReference;

    private String adminNote;

    // Manual Getter
    public String getBankReference() { return bankReference; }
    public String getAdminNote() { return adminNote; }

    // Manual Builder
    public static PayoutCompleteRequestDTOBuilder builder() { return new PayoutCompleteRequestDTOBuilder(); }
    public static class PayoutCompleteRequestDTOBuilder {
        private PayoutCompleteRequestDTO r = new PayoutCompleteRequestDTO();
        public PayoutCompleteRequestDTOBuilder bankReference(String bankReference) { r.bankReference = bankReference; return this; }
        public PayoutCompleteRequestDTOBuilder adminNote(String adminNote) { r.adminNote = adminNote; return this; }
        public PayoutCompleteRequestDTO build() { return r; }
    }
}
