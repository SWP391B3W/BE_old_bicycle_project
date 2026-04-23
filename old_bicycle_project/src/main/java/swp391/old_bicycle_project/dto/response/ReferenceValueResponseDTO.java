package swp391.old_bicycle_project.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ReferenceValueResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    public ReferenceValueResponseDTO() {}

    public ReferenceValueResponseDTO(UUID id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static ReferenceValueResponseDTOBuilder builder() {
        return new ReferenceValueResponseDTOBuilder();
    }

    public static class ReferenceValueResponseDTOBuilder {
        private UUID id;
        private String name;
        private String description;
        private LocalDateTime createdAt;

        public ReferenceValueResponseDTOBuilder id(UUID id) { this.id = id; return this; }
        public ReferenceValueResponseDTOBuilder name(String name) { this.name = name; return this; }
        public ReferenceValueResponseDTOBuilder description(String description) { this.description = description; return this; }
        public ReferenceValueResponseDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ReferenceValueResponseDTO build() {
            return new ReferenceValueResponseDTO(id, name, description, createdAt);
        }
    }
}
