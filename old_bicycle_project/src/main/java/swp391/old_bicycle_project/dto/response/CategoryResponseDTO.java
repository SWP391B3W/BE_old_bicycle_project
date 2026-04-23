package swp391.old_bicycle_project.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CategoryResponseDTO {

    private UUID id;
    private String name;
    private String slug;
    private UUID parentId;
    private String parentName;
    private LocalDateTime createdAt;

    public CategoryResponseDTO() {}

    public CategoryResponseDTO(UUID id, String name, String slug, UUID parentId, String parentName, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.parentId = parentId;
        this.parentName = parentName;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public UUID getParentId() { return parentId; }
    public String getParentName() { return parentName; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static CategoryResponseDTOBuilder builder() {
        return new CategoryResponseDTOBuilder();
    }

    public static class CategoryResponseDTOBuilder {
        private UUID id;
        private String name;
        private String slug;
        private UUID parentId;
        private String parentName;
        private LocalDateTime createdAt;

        public CategoryResponseDTOBuilder id(UUID id) { this.id = id; return this; }
        public CategoryResponseDTOBuilder name(String name) { this.name = name; return this; }
        public CategoryResponseDTOBuilder slug(String slug) { this.slug = slug; return this; }
        public CategoryResponseDTOBuilder parentId(UUID parentId) { this.parentId = parentId; return this; }
        public CategoryResponseDTOBuilder parentName(String parentName) { this.parentName = parentName; return this; }
        public CategoryResponseDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CategoryResponseDTO build() {
            return new CategoryResponseDTO(id, name, slug, parentId, parentName, createdAt);
        }
    }
}
