package swp391.old_bicycle_project.dto.auth;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileUpdateRequest {
    @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
    private String firstName;

    @Size(max = 100, message = "Họ không được vượt quá 100 ký tự")
    private String lastName;

    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    private String phone;

    @Size(max = 500, message = "Avatar URL không được vượt quá 500 ký tự")
    private String avatarUrl;

    @Size(max = 500, message = "Địa chỉ mặc định không được vượt quá 500 ký tự")
    private String defaultAddress;
}
