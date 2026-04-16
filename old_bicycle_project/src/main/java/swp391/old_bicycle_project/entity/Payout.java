package swp391.old_bicycle_project.entity;

import swp391.old_bicycle_project.entity.enums.PayoutProvider;
import swp391.old_bicycle_project.entity.enums.PayoutStatus;
import swp391.old_bicycle_project.entity.enums.PayoutType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payouts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_request_id")
    private RefundRequest refundRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PayoutType type;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PayoutStatus status = PayoutStatus.profile_required;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private PayoutProvider provider = PayoutProvider.vietqr_manual;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Builder.Default
    @Column(name = "gross_amount", nullable = false)
    private BigDecimal grossAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "fee_deduction_amount", nullable = false)
    private BigDecimal feeDeductionAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "net_amount", nullable = false)
    private BigDecimal netAmount = BigDecimal.ZERO;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bank_bin")
    private String bankBin;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "transfer_content")
    private String transferContent;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Column(name = "bank_reference")
    private String bankReference;

    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_by")
    private User completedBy;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Manual Getter
    public UUID getId() { return id; }
    public User getRecipient() { return recipient; }
    public Order getOrder() { return order; }
    public RefundRequest getRefundRequest() { return refundRequest; }
    public PayoutType getType() { return type; }
    public PayoutStatus getStatus() { return status; }
    public PayoutProvider getProvider() { return provider; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getGrossAmount() { return grossAmount; }
    public BigDecimal getFeeDeductionAmount() { return feeDeductionAmount; }
    public BigDecimal getNetAmount() { return netAmount; }
    public String getBankCode() { return bankCode; }
    public String getBankBin() { return bankBin; }
    public String getAccountNumber() { return accountNumber; }
    public String getAccountName() { return accountName; }
    public String getTransferContent() { return transferContent; }
    public String getQrCodeUrl() { return qrCodeUrl; }
    public String getBankReference() { return bankReference; }
    public String getAdminNote() { return adminNote; }
    public User getCompletedBy() { return completedBy; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Manual Setter
    public void setRecipient(User recipient) { this.recipient = recipient; }
    public void setOrder(Order order) { this.order = order; }
    public void setRefundRequest(RefundRequest refundRequest) { this.refundRequest = refundRequest; }
    public void setType(PayoutType type) { this.type = type; }
    public void setStatus(PayoutStatus status) { this.status = status; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setGrossAmount(BigDecimal grossAmount) { this.grossAmount = grossAmount; }
    public void setFeeDeductionAmount(BigDecimal feeDeductionAmount) { this.feeDeductionAmount = feeDeductionAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public void setBankBin(String bankBin) { this.bankBin = bankBin; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public void setTransferContent(String transferContent) { this.transferContent = transferContent; }
    public void setQrCodeUrl(String qrCodeUrl) { this.qrCodeUrl = qrCodeUrl; }
    public void setBankReference(String reference) { this.bankReference = reference; }
    public void setAdminNote(String note) { this.adminNote = note; }
    public void setCompletedBy(User user) { this.completedBy = user; }
    public void setCompletedAt(LocalDateTime d) { this.completedAt = d; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Manual Builder
    public static PayoutBuilder builder() { return new PayoutBuilder(); }
    public static class PayoutBuilder {
        private Payout r = new Payout();
        public PayoutBuilder id(UUID id) { r.id = id; return this; }
        public PayoutBuilder recipient(User recipient) { r.recipient = recipient; return this; }
        public PayoutBuilder order(Order order) { r.order = order; return this; }
        public PayoutBuilder refundRequest(RefundRequest refundRequest) { r.refundRequest = refundRequest; return this; }
        public PayoutBuilder type(PayoutType type) { r.type = type; return this; }
        public PayoutBuilder status(PayoutStatus status) { r.status = status; return this; }
        public PayoutBuilder provider(PayoutProvider provider) { r.provider = provider; return this; }
        public PayoutBuilder amount(BigDecimal amount) { r.amount = amount; return this; }
        public PayoutBuilder grossAmount(BigDecimal grossAmount) { r.grossAmount = grossAmount; return this; }
        public PayoutBuilder feeDeductionAmount(BigDecimal feeDeductionAmount) { r.feeDeductionAmount = feeDeductionAmount; return this; }
        public PayoutBuilder netAmount(BigDecimal netAmount) { r.netAmount = netAmount; return this; }
        public PayoutBuilder bankBin(String bankBin) { r.bankBin = bankBin; return this; }
        public PayoutBuilder accountNumber(String accountNumber) { r.accountNumber = accountNumber; return this; }
        public PayoutBuilder accountName(String accountName) { r.accountName = accountName; return this; }
        public PayoutBuilder transferContent(String transferContent) { r.transferContent = transferContent; return this; }
        public Payout build() { return r; }
    }
}
