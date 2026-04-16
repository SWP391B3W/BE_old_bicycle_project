package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_verifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    // Manual Getter
    public UUID getId() { return id; }
    public User getUser() { return user; }
    public String getToken() { return token; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Builder
    public static EmailVerificationBuilder builder() { return new EmailVerificationBuilder(); }
    public static class EmailVerificationBuilder {
        private EmailVerification r = new EmailVerification();
        public EmailVerificationBuilder user(User user) { r.user = user; return this; }
        public EmailVerificationBuilder token(String token) { r.token = token; return this; }
        public EmailVerificationBuilder expiresAt(LocalDateTime d) { r.expiresAt = d; return this; }
        public EmailVerification build() { return r; }
    }
}
