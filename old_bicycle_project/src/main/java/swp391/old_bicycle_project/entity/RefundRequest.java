package swp391.old_bicycle_project.entity;

import swp391.old_bicycle_project.entity.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "refund_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "evidence_note", columnDefinition = "TEXT")
    private String evidenceNote;

    @Builder.Default
    @OneToMany(mappedBy = "refundRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC, createdAt ASC")
    private List<RefundRequestFile> evidenceFiles = new ArrayList<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status = RefundStatus.pending;

    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;

    @Column(name = "refund_reference")
    private String refundReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addEvidenceFile(RefundRequestFile file) {
        evidenceFiles.add(file);
        file.setRefundRequest(this);
    }

    // Manual Getter
    public UUID getId() { return id; }
    public Order getOrder() { return order; }
    public User getRequester() { return requester; }
    public BigDecimal getAmount() { return amount; }
    public RefundStatus getStatus() { return status; }
    public String getReason() { return reason; }
    public Payment getPayment() { return payment; }
    public String getAdminNote() { return adminNote; }
    public User getReviewedBy() { return reviewedBy; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }

    public List<RefundRequestFile> getEvidenceFiles() { return evidenceFiles; }
    public String getRefundReference() { return refundReference; }
    public String getEvidenceNote() { return evidenceNote; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Manual Setter
    public void setOrder(Order order) { this.order = order; }
    public void setPayment(Payment payment) { this.payment = payment; }
    public void setRequester(User requester) { this.requester = requester; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setReason(String reason) { this.reason = reason; }
    public void setStatus(RefundStatus status) { this.status = status; }
    public void setAdminNote(String note) { this.adminNote = note; }
    public void setRefundReference(String reference) { this.refundReference = reference; }
    public void setReviewedBy(User user) { this.reviewedBy = user; }
    public void setReviewedAt(LocalDateTime d) { this.reviewedAt = d; }
    public void setProcessedAt(LocalDateTime d) { this.processedAt = d; }

    // Manual Builder
    public static RefundRequestBuilder builder() { return new RefundRequestBuilder(); }
    public static class RefundRequestBuilder {
        private RefundRequest r = new RefundRequest();
        public RefundRequestBuilder id(UUID id) { r.id = id; return this; }
        public RefundRequestBuilder order(Order order) { r.order = order; return this; }
        public RefundRequestBuilder payment(Payment payment) { r.payment = payment; return this; }
        public RefundRequestBuilder requester(User requester) { r.requester = requester; return this; }
        public RefundRequestBuilder amount(BigDecimal amount) { r.amount = amount; return this; }
        public RefundRequestBuilder reason(String reason) { r.reason = reason; return this; }
        public RefundRequestBuilder evidenceNote(String evidenceNote) { r.evidenceNote = evidenceNote; return this; }
        public RefundRequestBuilder adminNote(String adminNote) { r.adminNote = adminNote; return this; }
        public RefundRequestBuilder status(RefundStatus status) { r.status = status; return this; }
        public RefundRequestBuilder reviewedBy(User reviewedBy) { r.reviewedBy = reviewedBy; return this; }
        public RefundRequestBuilder reviewedAt(LocalDateTime reviewedAt) { r.reviewedAt = reviewedAt; return this; }
        public RefundRequestBuilder processedAt(LocalDateTime processedAt) { r.processedAt = processedAt; return this; }
        public RefundRequest build() { return r; }
    }
}
