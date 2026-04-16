package swp391.old_bicycle_project.entity;

import swp391.old_bicycle_project.entity.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Builder.Default
    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @ColumnTransformer(read = "metadata::text", write = "?::jsonb")
    private String metadata;

    @Builder.Default
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(ZoneOffset.UTC);

    @PrePersist
    void ensureCreatedAt() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(ZoneOffset.UTC);
        }
    }

    // Manual Getter
    public UUID getId() { return id; }
    public User getUser() { return user; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public NotificationType getType() { return type; }
    public Boolean getIsRead() { return isRead; }
    public String getMetadata() { return metadata; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Setter for isRead (Service uses setIsRead)
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    // Manual Builder
    public static NotificationBuilder builder() { return new NotificationBuilder(); }
    public static class NotificationBuilder {
        private Notification r = new Notification();
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
