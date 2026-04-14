package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.OrderEvidenceType;

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
}
