package swp391.old_bicycle_project.dto.auth;

import swp391.old_bicycle_project.entity.enums.AppRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 255, message = "Email không được vượt quá 255 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;

    @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
    private String firstName;

    @Size(max = 100, message = "Họ không được vượt quá 100 ký tự")
    private String lastName;

    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    private String phone;

    private AppRole role = AppRole.buyer;
}
