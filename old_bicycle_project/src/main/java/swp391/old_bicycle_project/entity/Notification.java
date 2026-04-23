package swp391.old_bicycle_project.entity;

import swp391.old_bicycle_project.entity.enums.NotificationType;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "type", nullable = false, columnDefinition = "notification_type")
    private NotificationType type;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @ColumnTransformer(read = "metadata::text", write = "?::jsonb")
    private String metadata;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(ZoneOffset.UTC);

    public Notification() {}

    @PrePersist
    void ensureCreatedAt() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(ZoneOffset.UTC);
        }
        if (isRead == null) {
            isRead = false;
        }
    }

    // Getters
    public UUID getId() { return id; }
    public User getUser() { return user; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public NotificationType getType() { return type; }
    public Boolean getIsRead() { return isRead; }
    public String getMetadata() { return metadata; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setType(NotificationType type) { this.type = type; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder
    public static NotificationBuilder builder() { return new NotificationBuilder(); }

    public static class NotificationBuilder {
        private final Notification r = new Notification();
        public NotificationBuilder id(UUID id) { r.id = id; return this; }
        public NotificationBuilder user(User user) { r.user = user; return this; }
        public NotificationBuilder title(String title) { r.title = title; return this; }
        public NotificationBuilder content(String content) { r.content = content; return this; }
        public NotificationBuilder type(NotificationType type) { r.type = type; return this; }
        public NotificationBuilder isRead(Boolean isRead) { r.isRead = isRead; return this; }
        public NotificationBuilder metadata(String metadata) { r.metadata = metadata; return this; }
        public NotificationBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public Notification build() { return r; }
    }
}
