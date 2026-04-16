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
public class PayoutProfileUpsertRequestDTO {

    @NotBlank(message = "Bank code is required")
    private String bankCode;

    @NotBlank(message = "Bank BIN is required")
    private String bankBin;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Account name is required")
    private String accountName;

    // Manual Getter
    public String getBankCode() { return bankCode; }
    public String getBankBin() { return bankBin; }
    public String getAccountNumber() { return accountNumber; }
    public String getAccountName() { return accountName; }
}
