package swp391.old_bicycle_project.entity;

import swp391.old_bicycle_project.entity.enums.PaymentGateway;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.entity.enums.PaymentPhase;
import swp391.old_bicycle_project.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Builder.Default
    @Column(name = "protected_amount", nullable = false)
    private BigDecimal protectedAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "buyer_fee_amount", nullable = false)
    private BigDecimal buyerFeeAmount = BigDecimal.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "gateway")
    private PaymentGateway gateway = PaymentGateway.manual;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "method", nullable = false, columnDefinition = "payment_method")
    private PaymentMethod method;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "phase")
    private PaymentPhase phase = PaymentPhase.upfront;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "status", columnDefinition = "payment_status")
    private PaymentStatus status = PaymentStatus.pending;

    @Column(name = "gateway_order_code", unique = true)
    private String gatewayOrderCode;

    @Column(name = "checkout_url")
    private String checkoutUrl;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Column(name = "transaction_reference", unique = true)
    private String transactionReference;

    @Column(name = "gateway_response", columnDefinition = "jsonb")
    @ColumnTransformer(read = "gateway_response::text", write = "?::jsonb")
    private String gatewayResponse;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Manual Getter
    public UUID getId() { return id; }
    public Order getOrder() { return order; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getProtectedAmount() { return protectedAmount; }
    public BigDecimal getBuyerFeeAmount() { return buyerFeeAmount; }
    public PaymentGateway getGateway() { return gateway; }
    public PaymentMethod getMethod() { return method; }
    public PaymentPhase getPhase() { return phase; }
    public PaymentStatus getStatus() { return status; }
    public String getGatewayOrderCode() { return gatewayOrderCode; }
    public String getTransactionReference() { return transactionReference; }
    public String getCheckoutUrl() { return checkoutUrl; }
    public String getQrCodeUrl() { return qrCodeUrl; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Setter
    public void setBuyerFeeAmount(BigDecimal amount) { this.buyerFeeAmount = amount; }
    public void setProtectedAmount(BigDecimal amount) { this.protectedAmount = amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public void setGatewayOrderCode(String code) { this.gatewayOrderCode = code; }
    public void setCheckoutUrl(String url) { this.checkoutUrl = url; }
    public void setQrCodeUrl(String url) { this.qrCodeUrl = qrCodeUrl; }
    public void setTransactionReference(String ref) { this.transactionReference = ref; }
    public void setGatewayResponse(String resp) { this.gatewayResponse = resp; }
    public void setPaymentDate(LocalDateTime date) { this.paymentDate = date; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    // Manual Builder
    public static PaymentBuilder builder() { return new PaymentBuilder(); }
    public static class PaymentBuilder {
        private Payment r = new Payment();
        public PaymentBuilder id(UUID id) { r.id = id; return this; }
        public PaymentBuilder order(Order order) { r.order = order; return this; }
        public PaymentBuilder amount(BigDecimal amount) { r.amount = amount; return this; }
        public PaymentBuilder protectedAmount(BigDecimal protectedAmount) { r.protectedAmount = protectedAmount; return this; }
        public PaymentBuilder buyerFeeAmount(BigDecimal buyerFeeAmount) { r.buyerFeeAmount = buyerFeeAmount; return this; }
        public PaymentBuilder gateway(PaymentGateway gateway) { r.gateway = gateway; return this; }
        public PaymentBuilder method(PaymentMethod method) { r.method = method; return this; }
        public PaymentBuilder phase(PaymentPhase phase) { r.phase = phase; return this; }
        public PaymentBuilder status(PaymentStatus status) { r.status = status; return this; }
        public PaymentBuilder gatewayOrderCode(String code) { r.gatewayOrderCode = code; return this; }
        public PaymentBuilder checkoutUrl(String url) { r.checkoutUrl = url; return this; }
        public PaymentBuilder qrCodeUrl(String url) { r.qrCodeUrl = url; return this; }
        public PaymentBuilder expiresAt(LocalDateTime d) { r.expiresAt = d; return this; }
        public Payment build() { return r; }
    }
}
