package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserPasswordResetRequest {

    @NotBlank(message = "New password is required")
    private String newPassword;

    // Manual Getter
    public String getNewPassword() { return newPassword; }

    // Manual Setter
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
