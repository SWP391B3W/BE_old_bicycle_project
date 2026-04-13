package swp391.old_bicycle_project.controller;

import swp391.old_bicycle_project.dto.auth.AuthResponse;
import swp391.old_bicycle_project.dto.auth.ChangePasswordRequest;
import swp391.old_bicycle_project.dto.auth.ForgotPasswordRequest;
import swp391.old_bicycle_project.dto.auth.LoginRequest;
import swp391.old_bicycle_project.dto.auth.ProfileUpdateRequest;
import swp391.old_bicycle_project.dto.auth.RefreshTokenRequest;
import swp391.old_bicycle_project.dto.auth.RegisterRequest;
import swp391.old_bicycle_project.dto.auth.ResendVerificationRequest;
import swp391.old_bicycle_project.dto.auth.ResetPasswordRequest;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản", security = {})
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.<String>builder()
                .result(authService.register(request))
                .build();
    }

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", security = {})
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.<AuthResponse>builder()
                .result(authService.login(request))
                .build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "Làm mới access token", security = {})
    public ApiResponse<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.<AuthResponse>builder()
                .result(authService.refreshToken(request))
                .build();
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Yêu cầu quên mật khẩu", security = {})
    public ApiResponse<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ApiResponse.<String>builder()
                .result(authService.requestPasswordReset(request))
                .build();
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Gửi lại email xác thực", security = {})
    public ApiResponse<String> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        return ApiResponse.<String>builder()
                .result(authService.resendVerificationEmail(request))
                .build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Đặt lại mật khẩu", security = {})
    public ApiResponse<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ApiResponse.<String>builder()
                .result(authService.resetPassword(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@AuthenticationPrincipal User currentUser) {
        authService.logout(currentUser);
        return ApiResponse.<String>builder()
                .result("Đăng xuất thành công")
                .build();
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Xác thực email", security = {})
    public ApiResponse<String> verifyEmail(@RequestParam String token) {
        return ApiResponse.<String>builder()
                .result(authService.verifyEmail(token))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<AuthResponse.UserInfo> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        return ApiResponse.<AuthResponse.UserInfo>builder()
                .result(authService.getCurrentUser(currentUser))
                .build();
    }

    @PatchMapping("/profile")
    public ApiResponse<AuthResponse.UserInfo> updateProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        return ApiResponse.<AuthResponse.UserInfo>builder()
                .result(authService.updateProfile(currentUser, request))
                .build();
    }

    @PatchMapping("/change-password")
    public ApiResponse<String> changePassword(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        return ApiResponse.<String>builder()
                .result(authService.changePassword(currentUser, request))
                .build();
    }
}
