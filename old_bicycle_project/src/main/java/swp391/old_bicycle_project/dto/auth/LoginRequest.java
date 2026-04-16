package swp391.old_bicycle_project.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    // Manual Getter
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    // Manual Setter
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
