package swp391.old_bicycle_project.dto.assistant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssistantChatResponseDTO {
    private String reply;

    public AssistantChatResponseDTO() {}

    public AssistantChatResponseDTO(String reply) {
        this.reply = reply;
    }

    // Manual Getter
    public String getReply() { return reply; }

    // Manual Builder
    public static AssistantChatResponseDTOBuilder builder() { return new AssistantChatResponseDTOBuilder(); }
    public static class AssistantChatResponseDTOBuilder {
        private AssistantChatResponseDTO r = new AssistantChatResponseDTO();
        public AssistantChatResponseDTOBuilder reply(String reply) { r.reply = reply; return this; }
        public AssistantChatResponseDTO build() { return r; }
    }
}
