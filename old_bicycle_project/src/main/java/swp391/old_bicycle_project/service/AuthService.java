package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.dto.auth.AuthResponse;
import swp391.old_bicycle_project.dto.auth.ChangePasswordRequest;
import swp391.old_bicycle_project.dto.auth.ForgotPasswordRequest;
import swp391.old_bicycle_project.dto.auth.LoginRequest;
import swp391.old_bicycle_project.dto.auth.ProfileUpdateRequest;
import swp391.old_bicycle_project.dto.auth.RefreshTokenRequest;
import swp391.old_bicycle_project.dto.auth.RegisterRequest;
import swp391.old_bicycle_project.dto.auth.ResendVerificationRequest;
import swp391.old_bicycle_project.dto.auth.ResetPasswordRequest;
import swp391.old_bicycle_project.entity.User;

/**
 * Service interface for Authentication and User Profile management.
 */
public interface AuthService {

    String register(RegisterRequest request);

    String resendVerificationEmail(ResendVerificationRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    String requestPasswordReset(ForgotPasswordRequest request);

    String resetPassword(ResetPasswordRequest request);

    void logout(User currentUser);

    String verifyEmail(String token);

    AuthResponse.UserInfo getCurrentUser(User currentUser);

    AuthResponse.UserInfo updateProfile(User currentUser, ProfileUpdateRequest request);

    String changePassword(User currentUser, ChangePasswordRequest request);
}
