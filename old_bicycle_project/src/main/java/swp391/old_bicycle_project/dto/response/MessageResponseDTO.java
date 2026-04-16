package swp391.old_bicycle_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDTO {
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String senderName;
    private String content;
    private String imageUrl;
    private Boolean isRead;
    private LocalDateTime createdAt;

    // Manual Builder
    public static MessageResponseDTOBuilder builder() { return new MessageResponseDTOBuilder(); }
    public static class MessageResponseDTOBuilder {
        private MessageResponseDTO r = new MessageResponseDTO();
        public MessageResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public MessageResponseDTOBuilder conversationId(UUID conversationId) { r.conversationId = conversationId; return this; }
        public MessageResponseDTOBuilder senderId(UUID senderId) { r.senderId = senderId; return this; }
        public MessageResponseDTOBuilder senderName(String senderName) { r.senderName = senderName; return this; }
        public MessageResponseDTOBuilder content(String content) { r.content = content; return this; }
        public MessageResponseDTOBuilder imageUrl(String imageUrl) { r.imageUrl = imageUrl; return this; }
        public MessageResponseDTOBuilder isRead(Boolean isRead) { r.isRead = isRead; return this; }
        public MessageResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public MessageResponseDTO build() { return r; }
    }
}
