package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import swp391.old_bicycle_project.entity.enums.UserStatus;

@Getter
@Setter
public class AdminUserStatusUpdateRequest {

    @NotNull(message = "User status is required")
    private UserStatus status;
}
