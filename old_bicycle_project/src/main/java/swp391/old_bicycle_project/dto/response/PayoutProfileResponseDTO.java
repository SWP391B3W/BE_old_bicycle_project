package swp391.old_bicycle_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayoutProfileResponseDTO {
    private UUID id;
    private UUID userId;
    private String bankCode;
    private String bankBin;
    private String accountNumber;
    private String accountName;
    private LocalDateTime updatedAt;

    // Manual Builder
    public static PayoutProfileResponseDTOBuilder builder() { return new PayoutProfileResponseDTOBuilder(); }
    public static class PayoutProfileResponseDTOBuilder {
        private PayoutProfileResponseDTO r = new PayoutProfileResponseDTO();
        public PayoutProfileResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public PayoutProfileResponseDTOBuilder userId(UUID userId) { r.userId = userId; return this; }
        public PayoutProfileResponseDTOBuilder bankCode(String bankCode) { r.bankCode = bankCode; return this; }
        public PayoutProfileResponseDTOBuilder bankBin(String bankBin) { r.bankBin = bankBin; return this; }
        public PayoutProfileResponseDTOBuilder accountNumber(String accountNumber) { r.accountNumber = accountNumber; return this; }
        public PayoutProfileResponseDTOBuilder accountName(String accountName) { r.accountName = accountName; return this; }
        public PayoutProfileResponseDTOBuilder updatedAt(LocalDateTime updatedAt) { r.updatedAt = updatedAt; return this; }
        public PayoutProfileResponseDTO build() { return r; }
    }
}
