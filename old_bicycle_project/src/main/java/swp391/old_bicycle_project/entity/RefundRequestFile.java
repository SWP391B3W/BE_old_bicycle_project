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
@Table(name = "refund_request_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundRequestFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_request_id", nullable = false)
    private RefundRequest refundRequest;

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
    public RefundRequest getRefundRequest() { return refundRequest; }
    public String getFileUrl() { return fileUrl; }
    public String getFileName() { return fileName; }
    public String getContentType() { return contentType; }
    public Integer getSortOrder() { return sortOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Setter
    public void setRefundRequest(RefundRequest refundRequest) { this.refundRequest = refundRequest; }

    // Manual Builder
    public static RefundRequestFileBuilder builder() { return new RefundRequestFileBuilder(); }
    public static class RefundRequestFileBuilder {
        private RefundRequestFile r = new RefundRequestFile();
        public RefundRequestFileBuilder id(UUID id) { r.id = id; return this; }
        public RefundRequestFileBuilder refundRequest(RefundRequest refundRequest) { r.refundRequest = refundRequest; return this; }
        public RefundRequestFileBuilder fileUrl(String fileUrl) { r.fileUrl = fileUrl; return this; }
        public RefundRequestFileBuilder fileName(String fileName) { r.fileName = fileName; return this; }
        public RefundRequestFileBuilder contentType(String contentType) { r.contentType = contentType; return this; }
        public RefundRequestFileBuilder sortOrder(Integer sortOrder) { r.sortOrder = sortOrder; return this; }
        public RefundRequestFile build() { return r; }
    }
}
