package swp391.old_bicycle_project.dto.response;

import lombok.Builder;
import lombok.Data;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AdminUserResponseDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private String defaultAddress;
    private AppRole role;
    private UserStatus status;
    private boolean verified;
    private Double averageRating;
    private Integer totalReviews;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
