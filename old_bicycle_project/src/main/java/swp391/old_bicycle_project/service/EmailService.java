package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.entity.EmailVerification;
import swp391.old_bicycle_project.entity.PasswordResetToken;
import swp391.old_bicycle_project.entity.User;

import java.util.Optional;

/**
 * Service interface for sending emails and managing verification/reset tokens.
 */
public interface EmailService {

    void sendVerificationEmail(User user, String token);

    void sendPasswordResetEmail(User user, String token);

    EmailVerification createVerificationToken(User user);

    void deleteByUser(User user);

    Optional<EmailVerification> findByToken(String token);

    PasswordResetToken createPasswordResetToken(User user);

    Optional<PasswordResetToken> findPasswordResetToken(String token);

    void deletePasswordResetTokensByUser(User user);
}
