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
public class UserPublicResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String avatar;
    private LocalDateTime joinedDate;
    private String role;
    private Double averageRating;
    private Integer totalReviews;
}
