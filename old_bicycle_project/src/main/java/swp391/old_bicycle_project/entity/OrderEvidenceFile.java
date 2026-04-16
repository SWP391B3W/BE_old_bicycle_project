package swp391.old_bicycle_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_evidence_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEvidenceFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private OrderEvidenceSubmission submission;

    @Column(name = "file_url", nullable = false, columnDefinition = "TEXT")
    private String fileUrl;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "content_type")
    private String contentType;

    @Builder.Default
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Manual Getter
    public UUID getId() { return id; }
    public OrderEvidenceSubmission getSubmission() { return submission; }
    public String getFileUrl() { return fileUrl; }
    public String getFileName() { return fileName; }
    public String getContentType() { return contentType; }
    public Integer getSortOrder() { return sortOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Setter
    public void setSubmission(OrderEvidenceSubmission submission) { this.submission = submission; }

    // Manual Builder
    public static OrderEvidenceFileBuilder builder() { return new OrderEvidenceFileBuilder(); }
    public static class OrderEvidenceFileBuilder {
        private OrderEvidenceFile r = new OrderEvidenceFile();
        public OrderEvidenceFileBuilder id(UUID id) { r.id = id; return this; }
        public OrderEvidenceFileBuilder submission(OrderEvidenceSubmission submission) { r.submission = submission; return this; }
        public OrderEvidenceFileBuilder fileUrl(String fileUrl) { r.fileUrl = fileUrl; return this; }
        public OrderEvidenceFileBuilder fileName(String fileName) { r.fileName = fileName; return this; }
        public OrderEvidenceFileBuilder contentType(String contentType) { r.contentType = contentType; return this; }
        public OrderEvidenceFileBuilder sortOrder(Integer sortOrder) { r.sortOrder = sortOrder; return this; }
        public OrderEvidenceFileBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public OrderEvidenceFile build() { return r; }
    }
}
