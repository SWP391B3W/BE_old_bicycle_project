package swp391.old_bicycle_project.entity;

import swp391.old_bicycle_project.entity.enums.FinancialTransactionEntryType;
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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "financial_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payout_id")
    private Payout payout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_request_id")
    private RefundRequest refundRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false)
    private FinancialTransactionEntryType entryType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Builder.Default
    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "VND";

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @ColumnTransformer(read = "metadata::text", write = "?::jsonb")
    private String metadata;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Manual Getter
    public UUID getId() { return id; }

    // Manual Builder
    public static FinancialTransactionBuilder builder() { return new FinancialTransactionBuilder(); }
    public static class FinancialTransactionBuilder {
        private FinancialTransaction r = new FinancialTransaction();
        public FinancialTransactionBuilder order(Order order) { r.order = order; return this; }
        public FinancialTransactionBuilder payment(Payment payment) { r.payment = payment; return this; }
        public FinancialTransactionBuilder payout(Payout payout) { r.payout = payout; return this; }
        public FinancialTransactionBuilder refundRequest(RefundRequest refundRequest) { r.refundRequest = refundRequest; return this; }
        public FinancialTransactionBuilder entryType(FinancialTransactionEntryType entryType) { r.entryType = entryType; return this; }
        public FinancialTransactionBuilder amount(BigDecimal amount) { r.amount = amount; return this; }
        public FinancialTransactionBuilder currency(String currency) { r.currency = currency; return this; }
        public FinancialTransactionBuilder note(String note) { r.note = note; return this; }
        public FinancialTransactionBuilder metadata(String metadata) { r.metadata = metadata; return this; }
        public FinancialTransaction build() { return r; }
    }
}
