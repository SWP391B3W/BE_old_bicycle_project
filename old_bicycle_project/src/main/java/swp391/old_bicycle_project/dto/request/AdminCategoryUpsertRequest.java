package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AdminCategoryUpsertRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    @NotBlank(message = "Category slug is required")
    private String slug;

    private UUID parentId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public UUID getParentId() { return parentId; }
    public void setParentId(UUID parentId) { this.parentId = parentId; }
}
