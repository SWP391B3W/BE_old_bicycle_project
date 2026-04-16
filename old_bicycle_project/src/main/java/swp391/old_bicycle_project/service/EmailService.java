package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.entity.EmailVerification;
import swp391.old_bicycle_project.entity.PasswordResetToken;
import swp391.old_bicycle_project.entity.User;

import java.util.Optional;

public interface EmailService {

    EmailVerification createVerificationToken(User user);

    PasswordResetToken createPasswordResetToken(User user);

    Optional<EmailVerification> findByToken(String token);

    Optional<PasswordResetToken> findPasswordResetToken(String token);

    void deleteByUser(User user);

    void deletePasswordResetTokensByUser(User user);

    void sendVerificationEmail(User user, String token);

    void sendPasswordResetEmail(User user, String token);
}
