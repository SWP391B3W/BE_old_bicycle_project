package swp391.old_bicycle_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {
    private UUID id;
    private UUID orderId;
    private UUID reviewerId;
    private String reviewerName;
    private UUID revieweeId;
    private String revieweeName;
    private Integer rating;
    private String comment;
    private String sellerReply;
    private LocalDateTime sellerRepliedAt;
    private LocalDateTime createdAt;

    // Manual Builder
    public static ReviewResponseDTOBuilder builder() { return new ReviewResponseDTOBuilder(); }
    public static class ReviewResponseDTOBuilder {
        private ReviewResponseDTO r = new ReviewResponseDTO();
        public ReviewResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public ReviewResponseDTOBuilder orderId(UUID orderId) { r.orderId = orderId; return this; }
        public ReviewResponseDTOBuilder reviewerId(UUID reviewerId) { r.reviewerId = reviewerId; return this; }
        public ReviewResponseDTOBuilder reviewerName(String reviewerName) { r.reviewerName = reviewerName; return this; }
        public ReviewResponseDTOBuilder revieweeId(UUID revieweeId) { r.revieweeId = revieweeId; return this; }
        public ReviewResponseDTOBuilder revieweeName(String revieweeName) { r.revieweeName = revieweeName; return this; }
        public ReviewResponseDTOBuilder rating(Integer rating) { r.rating = rating; return this; }
        public ReviewResponseDTOBuilder comment(String comment) { r.comment = comment; return this; }
        public ReviewResponseDTOBuilder sellerReply(String sellerReply) { r.sellerReply = sellerReply; return this; }
        public ReviewResponseDTOBuilder sellerRepliedAt(LocalDateTime sellerRepliedAt) { r.sellerRepliedAt = sellerRepliedAt; return this; }
        public ReviewResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public ReviewResponseDTO build() { return r; }
    }
}
