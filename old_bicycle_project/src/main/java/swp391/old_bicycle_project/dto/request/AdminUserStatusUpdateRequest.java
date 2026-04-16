package swp391.old_bicycle_project.dto.request;

import swp391.old_bicycle_project.entity.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserStatusUpdateRequest {

    @NotNull(message = "User status is required")
    private UserStatus status;

    // Manual Getter
    public UserStatus getStatus() { return status; }

    // Manual Setter
    public void setStatus(UserStatus status) { this.status = status; }
}
