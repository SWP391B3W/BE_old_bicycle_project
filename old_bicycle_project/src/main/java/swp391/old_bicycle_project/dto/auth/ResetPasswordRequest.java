package swp391.old_bicycle_project.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Token reset là bắt buộc")
    @Size(max = 255, message = "Token reset không được vượt quá 255 ký tự")
    private String token;

    @NotBlank(message = "Mật khẩu mới là bắt buộc")
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String newPassword;
}
