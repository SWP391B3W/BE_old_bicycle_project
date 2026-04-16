package swp391.old_bicycle_project.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required")
    @Size(max = 255, message = "Current password must not exceed 255 characters")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String newPassword;

    // Manual Getter
    public String getCurrentPassword() { return currentPassword; }
    public String getNewPassword() { return newPassword; }

    // Manual Setter
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
