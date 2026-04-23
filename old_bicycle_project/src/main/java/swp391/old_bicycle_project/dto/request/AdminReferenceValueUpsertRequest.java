package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminReferenceValueUpsertRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
