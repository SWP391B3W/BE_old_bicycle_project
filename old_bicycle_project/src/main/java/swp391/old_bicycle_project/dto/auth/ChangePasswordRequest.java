package swp391.old_bicycle_project.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Mật khẩu hiện tại là bắt buộc")
    @Size(max = 255, message = "Mật khẩu hiện tại không được vượt quá 255 ký tự")
    private String currentPassword;

    @NotBlank(message = "Mật khẩu mới là bắt buộc")
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String newPassword;
}
