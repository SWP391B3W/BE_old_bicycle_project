package swp391.old_bicycle_project.dto.response;

import swp391.old_bicycle_project.entity.enums.PaymentGateway;
import swp391.old_bicycle_project.entity.enums.PaymentPhase;
import swp391.old_bicycle_project.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestResponseDTO {
    private UUID paymentId;
    private UUID orderId;
    private PaymentGateway gateway;
    private PaymentPhase phase;
    private PaymentStatus status;
    private BigDecimal amount;
    private BigDecimal protectedAmount;
    private BigDecimal buyerFeeAmount;
    private String gatewayOrderCode;
    private String checkoutUrl;
    private String qrCodeUrl;
    private String transferContent;
    private String bankBin;
    private String bankAccountNumber;
    private String bankAccountName;
    private boolean mockMode;
    private String instructions;
    private LocalDateTime expiresAt;

    // Manual Builder
    public static PaymentRequestResponseDTOBuilder builder() { return new PaymentRequestResponseDTOBuilder(); }
    public static class PaymentRequestResponseDTOBuilder {
        private PaymentRequestResponseDTO r = new PaymentRequestResponseDTO();
        public PaymentRequestResponseDTOBuilder paymentId(UUID paymentId) { r.paymentId = paymentId; return this; }
        public PaymentRequestResponseDTOBuilder orderId(UUID orderId) { r.orderId = orderId; return this; }
        public PaymentRequestResponseDTOBuilder gateway(PaymentGateway gateway) { r.gateway = gateway; return this; }
        public PaymentRequestResponseDTOBuilder phase(PaymentPhase phase) { r.phase = phase; return this; }
        public PaymentRequestResponseDTOBuilder status(PaymentStatus status) { r.status = status; return this; }
        public PaymentRequestResponseDTOBuilder amount(BigDecimal amount) { r.amount = amount; return this; }
        public PaymentRequestResponseDTOBuilder protectedAmount(BigDecimal protectedAmount) { r.protectedAmount = protectedAmount; return this; }
        public PaymentRequestResponseDTOBuilder buyerFeeAmount(BigDecimal buyerFeeAmount) { r.buyerFeeAmount = buyerFeeAmount; return this; }
        public PaymentRequestResponseDTOBuilder gatewayOrderCode(String gatewayOrderCode) { r.gatewayOrderCode = gatewayOrderCode; return this; }
        public PaymentRequestResponseDTOBuilder checkoutUrl(String checkoutUrl) { r.checkoutUrl = checkoutUrl; return this; }
        public PaymentRequestResponseDTOBuilder qrCodeUrl(String qrCodeUrl) { r.qrCodeUrl = qrCodeUrl; return this; }
        public PaymentRequestResponseDTOBuilder transferContent(String transferContent) { r.transferContent = transferContent; return this; }
        public PaymentRequestResponseDTOBuilder bankBin(String bankBin) { r.bankBin = bankBin; return this; }
        public PaymentRequestResponseDTOBuilder bankAccountNumber(String bankAccountNumber) { r.bankAccountNumber = bankAccountNumber; return this; }
        public PaymentRequestResponseDTOBuilder bankAccountName(String bankAccountName) { r.bankAccountName = bankAccountName; return this; }
        public PaymentRequestResponseDTOBuilder mockMode(boolean mockMode) { r.mockMode = mockMode; return this; }
        public PaymentRequestResponseDTOBuilder instructions(String instructions) { r.instructions = instructions; return this; }
        public PaymentRequestResponseDTOBuilder expiresAt(LocalDateTime expiresAt) { r.expiresAt = expiresAt; return this; }
        public PaymentRequestResponseDTO build() { return r; }
    }
}
