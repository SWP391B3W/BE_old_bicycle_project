package swp391.old_bicycle_project.dto.assistant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistantMessageDTO {

    @NotBlank(message = "INVALID_KEY")
    private String role;

    @NotBlank(message = "INVALID_KEY")
    @Size(max = 2000, message = "INVALID_KEY")
    private String content;

    // Manual Getter
    public String getRole() { return role; }
    public String getContent() { return content; }

    // Manual Setter
    public void setRole(String role) { this.role = role; }
    public void setContent(String content) { this.content = content; }
}
