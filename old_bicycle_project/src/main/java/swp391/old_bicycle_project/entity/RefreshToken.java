package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

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

    // Manual Getters
    public String getToken() { return token; }
    public User getUser() { return user; }
    public LocalDateTime getExpiresAt() { return expiresAt; }

    public void setToken(String token) { this.token = token; }
    public void setUser(User user) { this.user = user; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public static RefreshTokenBuilder builder() {
        return new RefreshTokenBuilder();
    }

    public static class RefreshTokenBuilder {
        private RefreshToken rt = new RefreshToken();
        public RefreshTokenBuilder id(UUID id) { rt.id = id; return this; }
        public RefreshTokenBuilder user(User user) { rt.user = user; return this; }
        public RefreshTokenBuilder token(String token) { rt.token = token; return this; }
        public RefreshTokenBuilder expiresAt(LocalDateTime expiresAt) { rt.expiresAt = expiresAt; return this; }
        public RefreshToken build() { return rt; }
    }
}
