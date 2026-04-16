package swp391.old_bicycle_project.entity;

import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.UserStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "default_address")
    private String defaultAddress;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(columnDefinition = "app_role")
    private AppRole role = AppRole.buyer;

    @Column(name = "is_verified")
    private boolean isVerified = false;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(columnDefinition = "user_status")
    private UserStatus status = UserStatus.active;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User() {}

    // Manual Getters
    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getDefaultAddress() { return defaultAddress; }
    public AppRole getRole() { return role; }
    public boolean isVerified() { return isVerified; }
    public UserStatus getStatus() { return status; }
    public Double getAverageRating() { return averageRating; }
    public Integer getTotalReviews() { return totalReviews; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Manual Setters
    public void setId(UUID id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public void setDefaultAddress(String defaultAddress) { this.defaultAddress = defaultAddress; }
    public void setRole(AppRole role) { this.role = role; }
    public void setVerified(boolean verified) { isVerified = verified; }
    public void setStatus(UserStatus status) { this.status = status; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.banned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.active;
    }

    // Manual Builder
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private User u = new User();
        public UserBuilder id(UUID id) { u.id = id; return this; }
        public UserBuilder email(String email) { u.email = email; return this; }
        public UserBuilder passwordHash(String passwordHash) { u.passwordHash = passwordHash; return this; }
        public UserBuilder firstName(String firstName) { u.firstName = firstName; return this; }
        public UserBuilder lastName(String lastName) { u.lastName = lastName; return this; }
        public UserBuilder phone(String phone) { u.phone = phone; return this; }
        public UserBuilder avatarUrl(String avatarUrl) { u.avatarUrl = avatarUrl; return this; }
        public UserBuilder role(AppRole role) { u.role = role; return this; }
        public UserBuilder isVerified(boolean isVerified) { u.isVerified = isVerified; return this; }
        public UserBuilder status(UserStatus status) { u.status = status; return this; }
        public User build() { return u; }
    }

    // Helper method
    public String getFullName() {
        if (firstName == null && lastName == null) return "Unknown";
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }
}
