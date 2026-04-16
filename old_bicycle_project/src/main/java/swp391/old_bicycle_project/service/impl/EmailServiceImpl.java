package swp391.old_bicycle_project.service.impl;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.UriComponentsBuilder;
import swp391.old_bicycle_project.entity.EmailVerification;
import swp391.old_bicycle_project.entity.PasswordResetToken;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.repository.EmailVerificationRepository;
import swp391.old_bicycle_project.repository.PasswordResetTokenRepository;
import swp391.old_bicycle_project.service.EmailService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${app.frontend-url:http://localhost:8080}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${auth.password-reset-expiration:3600000}")
    private long passwordResetExpiration;

    @Override
    @Transactional
    public EmailVerification createVerificationToken(User user) {
        emailVerificationRepository.deleteByUser(user);

        EmailVerification verification = EmailVerification.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();
        return emailVerificationRepository.save(verification);
    }

    @Override
    @Transactional
    public PasswordResetToken createPasswordResetToken(User user) {
        passwordResetTokenRepository.deleteByUser(user);

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusSeconds(passwordResetExpiration / 1000))
                .build();
        return passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public Optional<EmailVerification> findByToken(String token) {
        return emailVerificationRepository.findByToken(token);
    }

    @Override
    public Optional<PasswordResetToken> findPasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {
        emailVerificationRepository.deleteByUser(user);
    }

    @Override
    @Transactional
    public void deletePasswordResetTokensByUser(User user) {
        passwordResetTokenRepository.deleteByUser(user);
    }

    @Override
    @Async
    public void sendVerificationEmail(User user, String token) {
        sendEmail(
                user.getEmail(),
                "Xác thực tài khoản - Old Bicycles Marketplace",
                renderTemplate("email-verification.html", Map.of(
                        "displayName", resolveDisplayName(user),
                        "actionUrl", buildFrontendActionUrl("/verify-email", token)
                ))
        );
    }

    @Override
    @Async
    public void sendPasswordResetEmail(User user, String token) {
        sendEmail(
                user.getEmail(),
                "Đặt lại mật khẩu - Old Bicycles Marketplace",
                renderTemplate("password-reset.html", Map.of(
                        "displayName", resolveDisplayName(user),
                        "actionUrl", buildFrontendActionUrl("/reset-password", token)
                ))
        );
    }

    private void sendEmail(String recipient, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
            log.info("Sent email to {}", recipient);
        } catch (Exception exception) {
            log.error("Cannot send email to {}: {}", recipient, exception.getMessage());
        }
    }

    private String resolveDisplayName(User user) {
        return user.getFirstName() != null && !user.getFirstName().isBlank()
                ? user.getFirstName()
                : user.getEmail();
    }

    private String buildFrontendActionUrl(String path, String token) {
        String normalizedFrontendUrl = frontendUrl == null ? "" : frontendUrl.trim();
        if (normalizedFrontendUrl.endsWith("/")) {
            normalizedFrontendUrl = normalizedFrontendUrl.substring(0, normalizedFrontendUrl.length() - 1);
        }

        return UriComponentsBuilder.fromUriString(normalizedFrontendUrl)
                .path(path)
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    private String renderTemplate(String templateName, Map<String, String> placeholders) {
        try {
            ClassPathResource resource = new ClassPathResource("mail/" + templateName);
            String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                html = html.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            return html;
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot load email template: " + templateName, exception);
        }
    }
}
