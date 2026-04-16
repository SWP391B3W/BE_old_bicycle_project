package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequestDTO {

    @NotNull(message = "Conversation ID is required")
    private UUID conversationId;

    private UUID senderId;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    private String imageUrl;

    // Manual Getter
    public UUID getConversationId() { return conversationId; }
    public UUID getSenderId() { return senderId; }
    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }

    // Manual Builder
    public static MessageRequestDTOBuilder builder() { return new MessageRequestDTOBuilder(); }
    public static class MessageRequestDTOBuilder {
        private MessageRequestDTO r = new MessageRequestDTO();
        public MessageRequestDTOBuilder conversationId(UUID id) { r.conversationId = id; return this; }
        public MessageRequestDTOBuilder senderId(UUID id) { r.senderId = id; return this; }
        public MessageRequestDTOBuilder content(String content) { r.content = content; return this; }
        public MessageRequestDTOBuilder imageUrl(String url) { r.imageUrl = url; return this; }
        public MessageRequestDTO build() { return r; }
    }
}
