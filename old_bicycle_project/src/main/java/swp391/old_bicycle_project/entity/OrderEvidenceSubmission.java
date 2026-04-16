package swp391.old_bicycle_project.entity;

import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.OrderEvidenceType;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "order_evidence_submissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_order_evidence_submission_order_type",
                        columnNames = {"order_id", "evidence_type"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEvidenceSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by_user_id", nullable = false)
    private User submittedByUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "submitted_by_role", nullable = false)
    private AppRole submittedByRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "evidence_type", nullable = false)
    private OrderEvidenceType evidenceType;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Builder.Default
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC, createdAt ASC")
    private List<OrderEvidenceFile> files = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addFile(OrderEvidenceFile file) {
        files.add(file);
        file.setSubmission(this);
    }

    // Manual Getter
    public UUID getId() { return id; }
    public Order getOrder() { return order; }
    public User getSubmittedByUser() { return submittedByUser; }
    public AppRole getSubmittedByRole() { return submittedByRole; }
    public OrderEvidenceType getEvidenceType() { return evidenceType; }
    public String getNote() { return note; }
    public List<OrderEvidenceFile> getFiles() { return files; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Builder
    public static OrderEvidenceSubmissionBuilder builder() { return new OrderEvidenceSubmissionBuilder(); }
    public static class OrderEvidenceSubmissionBuilder {
        private OrderEvidenceSubmission r = new OrderEvidenceSubmission();
        public OrderEvidenceSubmissionBuilder id(UUID id) { r.id = id; return this; }
        public OrderEvidenceSubmissionBuilder order(Order order) { r.order = order; return this; }
        public OrderEvidenceSubmissionBuilder submittedByUser(User user) { r.submittedByUser = user; return this; }
        public OrderEvidenceSubmissionBuilder submittedByRole(AppRole role) { r.submittedByRole = role; return this; }
        public OrderEvidenceSubmissionBuilder evidenceType(OrderEvidenceType type) { r.evidenceType = type; return this; }
        public OrderEvidenceSubmissionBuilder note(String note) { r.note = note; return this; }
        public OrderEvidenceSubmission build() { return r; }
    }
}
