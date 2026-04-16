package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Builder.Default
    @Column(name = "is_read")
    private Boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Manual Getter
    public UUID getId() { return id; }
    public Conversation getConversation() { return conversation; }
    public User getSender() { return sender; }
    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }
    public Boolean getIsRead() { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Builder
    public static MessageBuilder builder() { return new MessageBuilder(); }
    public static class MessageBuilder {
        private Message r = new Message();
        public MessageBuilder id(UUID id) { r.id = id; return this; }
        public MessageBuilder conversation(Conversation conversation) { r.conversation = conversation; return this; }
        public MessageBuilder sender(User sender) { r.sender = sender; return this; }
        public MessageBuilder content(String content) { r.content = content; return this; }
        public MessageBuilder imageUrl(String imageUrl) { r.imageUrl = imageUrl; return this; }
        public MessageBuilder isRead(Boolean isRead) { r.isRead = isRead; return this; }
        public Message build() { return r; }
    }
}
