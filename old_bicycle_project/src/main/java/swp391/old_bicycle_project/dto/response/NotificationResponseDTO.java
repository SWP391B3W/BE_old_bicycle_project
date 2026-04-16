package swp391.old_bicycle_project.dto.response;

import swp391.old_bicycle_project.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {
    private UUID id;
    private UUID userId;
    private String title;
    private String content;
    private NotificationType type;
    private Boolean isRead;
    private String metadata;
    private OffsetDateTime createdAt;

    // Manual Builder
    public static NotificationResponseDTOBuilder builder() { return new NotificationResponseDTOBuilder(); }
    public static class NotificationResponseDTOBuilder {
        private NotificationResponseDTO r = new NotificationResponseDTO();
        public NotificationResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public NotificationResponseDTOBuilder userId(UUID userId) { r.userId = userId; return this; }
        public NotificationResponseDTOBuilder title(String title) { r.title = title; return this; }
        public NotificationResponseDTOBuilder content(String content) { r.content = content; return this; }
        public NotificationResponseDTOBuilder type(NotificationType type) { r.type = type; return this; }
        public NotificationResponseDTOBuilder isRead(Boolean isRead) { r.isRead = isRead; return this; }
        public NotificationResponseDTOBuilder metadata(String metadata) { r.metadata = metadata; return this; }
        public NotificationResponseDTOBuilder createdAt(OffsetDateTime createdAt) { r.createdAt = createdAt; return this; }
        public NotificationResponseDTO build() { return r; }
    }
}
