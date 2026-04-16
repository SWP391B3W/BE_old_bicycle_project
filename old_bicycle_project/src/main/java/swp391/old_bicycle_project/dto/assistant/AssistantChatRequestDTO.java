package swp391.old_bicycle_project.dto.assistant;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssistantChatRequestDTO {

    @NotEmpty(message = "INVALID_KEY")
    @Valid
    private List<AssistantMessageDTO> messages;

    // Manual Getter
    public List<AssistantMessageDTO> getMessages() { return messages; }

    // Manual Setter
    public void setMessages(List<AssistantMessageDTO> messages) { this.messages = messages; }
}
