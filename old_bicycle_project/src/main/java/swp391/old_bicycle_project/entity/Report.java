package swp391.old_bicycle_project.entity;

import swp391.old_bicycle_project.entity.enums.ReportReason;
import swp391.old_bicycle_project.entity.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @Column(name = "target_id", nullable = false)
    private UUID targetId;

    @Column(name = "target_type", nullable = false)
    private String targetType;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "reason", columnDefinition = "report_reason")
    private ReportReason reason;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC, createdAt ASC")
    private List<ReportFile> evidenceFiles = new ArrayList<>();

    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "status", columnDefinition = "report_status")
    private ReportStatus status = ReportStatus.pending;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    public void addEvidenceFile(ReportFile file) {
        evidenceFiles.add(file);
        file.setReport(this);
    }

    // Manual Getter
    public UUID getId() { return id; }
    public User getReporter() { return reporter; }
    public UUID getTargetId() { return targetId; }
    public String getTargetType() { return targetType; }
    public ReportReason getReason() { return reason; }
    public String getDescription() { return description; }
    public ReportStatus getStatus() { return status; }
    public List<ReportFile> getEvidenceFiles() { return evidenceFiles; }
    public String getAdminNote() { return adminNote; }
    public User getProcessedBy() { return processedBy; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Setter
    public void setTargetType(String type) { this.targetType = type; }
    public void setStatus(ReportStatus status) { this.status = status; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }
    public void setProcessedBy(User user) { this.processedBy = user; }
    public void setProcessedAt(LocalDateTime d) { this.processedAt = d; }

    // Manual Builder
    public static ReportBuilder builder() { return new ReportBuilder(); }
    public static class ReportBuilder {
        private Report r = new Report();
        public ReportBuilder id(UUID id) { r.id = id; return this; }
        public ReportBuilder reporter(User reporter) { r.reporter = reporter; return this; }
        public ReportBuilder targetId(UUID targetId) { r.targetId = targetId; return this; }
        public ReportBuilder targetType(String targetType) { r.targetType = targetType; return this; }
        public ReportBuilder reason(ReportReason reason) { r.reason = reason; return this; }
        public ReportBuilder description(String description) { r.description = description; return this; }
        public ReportBuilder status(ReportStatus status) { r.status = status; return this; }
        public Report build() { return r; }
    }
}
