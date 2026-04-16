package swp391.old_bicycle_project.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Reset token is required")
    @Size(max = 255, message = "Reset token must not exceed 255 characters")
    private String token;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String newPassword;

    // Manual Getter
    public String getToken() { return token; }
    public String getNewPassword() { return newPassword; }

    // Manual Setter
    public void setToken(String token) { this.token = token; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
