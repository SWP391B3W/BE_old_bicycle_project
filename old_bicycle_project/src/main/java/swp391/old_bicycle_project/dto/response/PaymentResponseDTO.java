package swp391.old_bicycle_project.dto.response;

import swp391.old_bicycle_project.entity.enums.PaymentGateway;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
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
public class PaymentResponseDTO {
    private UUID id;
    private UUID orderId;
    private BigDecimal amount;
    private BigDecimal protectedAmount;
    private BigDecimal buyerFeeAmount;
    private PaymentGateway gateway;
    private PaymentMethod method;
    private PaymentPhase phase;
    private PaymentStatus status;
    private String gatewayOrderCode;
    private String transactionReference;
    private String checkoutUrl;
    private String qrCodeUrl;
    private LocalDateTime paymentDate;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    // Manual Builder
    public static PaymentResponseDTOBuilder builder() { return new PaymentResponseDTOBuilder(); }
    public static class PaymentResponseDTOBuilder {
        private PaymentResponseDTO r = new PaymentResponseDTO();
        public PaymentResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public PaymentResponseDTOBuilder orderId(UUID orderId) { r.orderId = orderId; return this; }
        public PaymentResponseDTOBuilder amount(BigDecimal amount) { r.amount = amount; return this; }
        public PaymentResponseDTOBuilder protectedAmount(BigDecimal protectedAmount) { r.protectedAmount = protectedAmount; return this; }
        public PaymentResponseDTOBuilder buyerFeeAmount(BigDecimal buyerFeeAmount) { r.buyerFeeAmount = buyerFeeAmount; return this; }
        public PaymentResponseDTOBuilder gateway(PaymentGateway gateway) { r.gateway = gateway; return this; }
        public PaymentResponseDTOBuilder method(PaymentMethod method) { r.method = method; return this; }
        public PaymentResponseDTOBuilder phase(PaymentPhase phase) { r.phase = phase; return this; }
        public PaymentResponseDTOBuilder status(PaymentStatus status) { r.status = status; return this; }
        public PaymentResponseDTOBuilder gatewayOrderCode(String gatewayOrderCode) { r.gatewayOrderCode = gatewayOrderCode; return this; }
        public PaymentResponseDTOBuilder transactionReference(String transactionReference) { r.transactionReference = transactionReference; return this; }
        public PaymentResponseDTOBuilder checkoutUrl(String checkoutUrl) { r.checkoutUrl = checkoutUrl; return this; }
        public PaymentResponseDTOBuilder qrCodeUrl(String qrCodeUrl) { r.qrCodeUrl = qrCodeUrl; return this; }
        public PaymentResponseDTOBuilder paymentDate(LocalDateTime paymentDate) { r.paymentDate = paymentDate; return this; }
        public PaymentResponseDTOBuilder expiresAt(LocalDateTime expiresAt) { r.expiresAt = expiresAt; return this; }
        public PaymentResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public PaymentResponseDTO build() { return r; }
    }
}
