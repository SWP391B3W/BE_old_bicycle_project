package swp391.old_bicycle_project.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token không được để trống")
    private String refreshToken;

    // Manual Getter
    public String getRefreshToken() { return refreshToken; }

    // Manual Setter
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
