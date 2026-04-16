package swp391.old_bicycle_project.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SepayWebhookRequestDTO {
    private Long id;
    private String gateway;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;
    private String accountNumber;
    private String transferType;
    private BigDecimal transferAmount;
    private String accumulated;
    private String code;
    private String content;
    private String referenceCode;
    private String description;

    // Manual Getter
    public Long getId() { return id; }
    public String getGateway() { return gateway; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public String getAccountNumber() { return accountNumber; }
    public String getTransferType() { return transferType; }
    public BigDecimal getTransferAmount() { return transferAmount; }
    public String getAccumulated() { return accumulated; }
    public String getCode() { return code; }
    public String getContent() { return content; }
    public String getReferenceCode() { return referenceCode; }
    public String getDescription() { return description; }

    // Manual Builder
    public static SepayWebhookRequestDTOBuilder builder() { return new SepayWebhookRequestDTOBuilder(); }
    public static class SepayWebhookRequestDTOBuilder {
        private SepayWebhookRequestDTO r = new SepayWebhookRequestDTO();
        public SepayWebhookRequestDTOBuilder id(Long id) { r.id = id; return this; }
        public SepayWebhookRequestDTOBuilder gateway(String gateway) { r.gateway = gateway; return this; }
        public SepayWebhookRequestDTOBuilder transactionDate(LocalDateTime d) { r.transactionDate = d; return this; }
        public SepayWebhookRequestDTOBuilder accountNumber(String accountNumber) { r.accountNumber = accountNumber; return this; }
        public SepayWebhookRequestDTOBuilder transferType(String transferType) { r.transferType = transferType; return this; }
        public SepayWebhookRequestDTOBuilder transferAmount(BigDecimal transferAmount) { r.transferAmount = transferAmount; return this; }
        public SepayWebhookRequestDTOBuilder accumulated(String accumulated) { r.accumulated = accumulated; return this; }
        public SepayWebhookRequestDTOBuilder code(String code) { r.code = code; return this; }
        public SepayWebhookRequestDTOBuilder content(String content) { r.content = content; return this; }
        public SepayWebhookRequestDTOBuilder referenceCode(String referenceCode) { r.referenceCode = referenceCode; return this; }
        public SepayWebhookRequestDTOBuilder description(String description) { r.description = description; return this; }
        public SepayWebhookRequestDTO build() { return r; }
    }
}
