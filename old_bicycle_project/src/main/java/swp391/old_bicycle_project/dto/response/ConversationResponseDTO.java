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
public class ConversationResponseDTO {
    private UUID id;
    private UUID productId;
    private String productTitle;
    private UUID buyerId;
    private String buyerName;
    private UUID sellerId;
    private String sellerName;
    private String lastMessage;
    private long unreadCount;
    private LocalDateTime updatedAt;

    // Manual Builder
    public static ConversationResponseDTOBuilder builder() { return new ConversationResponseDTOBuilder(); }
    public static class ConversationResponseDTOBuilder {
        private ConversationResponseDTO r = new ConversationResponseDTO();
        public ConversationResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public ConversationResponseDTOBuilder productId(UUID productId) { r.productId = productId; return this; }
        public ConversationResponseDTOBuilder productTitle(String productTitle) { r.productTitle = productTitle; return this; }
        public ConversationResponseDTOBuilder buyerId(UUID buyerId) { r.buyerId = buyerId; return this; }
        public ConversationResponseDTOBuilder buyerName(String buyerName) { r.buyerName = buyerName; return this; }
        public ConversationResponseDTOBuilder sellerId(UUID sellerId) { r.sellerId = sellerId; return this; }
        public ConversationResponseDTOBuilder sellerName(String sellerName) { r.sellerName = sellerName; return this; }
        public ConversationResponseDTOBuilder lastMessage(String lastMessage) { r.lastMessage = lastMessage; return this; }
        public ConversationResponseDTOBuilder unreadCount(long unreadCount) { r.unreadCount = unreadCount; return this; }
        public ConversationResponseDTOBuilder updatedAt(LocalDateTime updatedAt) { r.updatedAt = updatedAt; return this; }
        public ConversationResponseDTO build() { return r; }
    }
}
