package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDTO {

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    private String comment;

    // Manual Getter
    public Integer getRating() { return rating; }
    public String getComment() { return comment; }

    // Manual Builder
    public static ReviewRequestDTOBuilder builder() { return new ReviewRequestDTOBuilder(); }
    public static class ReviewRequestDTOBuilder {
        private ReviewRequestDTO r = new ReviewRequestDTO();
        public ReviewRequestDTOBuilder rating(Integer rating) { r.rating = rating; return this; }
        public ReviewRequestDTOBuilder comment(String comment) { r.comment = comment; return this; }
        public ReviewRequestDTO build() { return r; }
    }
}
