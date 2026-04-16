package swp391.old_bicycle_project.dto.auth;

import swp391.old_bicycle_project.entity.enums.AppRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email khong duoc de trong")
    @Email(message = "Email khong hop le")
    @Size(max = 255, message = "Email khong duoc vuot qua 255 ky tu")
    private String email;

    @NotBlank(message = "Mat khau khong duoc de trong")
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;

    @Size(max = 100, message = "Ten khong duoc vuot qua 100 ky tu")
    private String firstName;

    @Size(max = 100, message = "Ho khong duoc vuot qua 100 ky tu")
    private String lastName;

    @Size(max = 20, message = "So dien thoai khong duoc vuot qua 20 ky tu")
    private String phone;

    private AppRole role = AppRole.buyer;

    // Manual Getter
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public AppRole getRole() { return role; }

    // Manual Setter
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRole(AppRole role) { this.role = role; }
}
