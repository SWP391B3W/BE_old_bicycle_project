package swp391.old_bicycle_project.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResendVerificationRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    // Manual Getter
    public String getEmail() { return email; }

    // Manual Setter
    public void setEmail(String email) { this.email = email; }
}
