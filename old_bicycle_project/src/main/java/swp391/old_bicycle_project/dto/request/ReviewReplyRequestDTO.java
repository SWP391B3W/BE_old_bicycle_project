package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewReplyRequestDTO {

    @NotBlank(message = "Reply is required")
    private String reply;

    // Manual Getter
    public String getReply() { return reply; }

    // Manual Builder
    public static ReviewReplyRequestDTOBuilder builder() { return new ReviewReplyRequestDTOBuilder(); }
    public static class ReviewReplyRequestDTOBuilder {
        private ReviewReplyRequestDTO r = new ReviewReplyRequestDTO();
        public ReviewReplyRequestDTOBuilder reply(String reply) { r.reply = reply; return this; }
        public ReviewReplyRequestDTO build() { return r; }
    }
}
