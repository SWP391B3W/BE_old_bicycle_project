package swp391.old_bicycle_project.dto.response;

import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AdminUserResponseDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private String defaultAddress;
    private AppRole role;
    private UserStatus status;
    private boolean isVerified;
    private Double averageRating;
    private Integer totalReviews;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AdminUserResponseDTO() {}

    public AdminUserResponseDTO(UUID id, String email, String firstName, String lastName, String fullName, String phone, String avatarUrl, String defaultAddress, AppRole role, UserStatus status, boolean isVerified, Double averageRating, Integer totalReviews, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.defaultAddress = defaultAddress;
        this.role = role;
        this.status = status;
        this.isVerified = isVerified;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Manual Getter
    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getDefaultAddress() { return defaultAddress; }
    public AppRole getRole() { return role; }
    public UserStatus getStatus() { return status; }
    public boolean isVerified() { return isVerified; }
    public Double getAverageRating() { return averageRating; }
    public Integer getTotalReviews() { return totalReviews; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Manual Builder
    public static AdminUserResponseDTOBuilder builder() { return new AdminUserResponseDTOBuilder(); }
    public static class AdminUserResponseDTOBuilder {
        private AdminUserResponseDTO r = new AdminUserResponseDTO();
        public AdminUserResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public AdminUserResponseDTOBuilder email(String email) { r.email = email; return this; }
        public AdminUserResponseDTOBuilder firstName(String firstName) { r.firstName = firstName; return this; }
        public AdminUserResponseDTOBuilder lastName(String lastName) { r.lastName = lastName; return this; }
        public AdminUserResponseDTOBuilder fullName(String fullName) { r.fullName = fullName; return this; }
        public AdminUserResponseDTOBuilder phone(String phone) { r.phone = phone; return this; }
        public AdminUserResponseDTOBuilder avatarUrl(String avatarUrl) { r.avatarUrl = avatarUrl; return this; }
        public AdminUserResponseDTOBuilder defaultAddress(String defaultAddress) { r.defaultAddress = defaultAddress; return this; }
        public AdminUserResponseDTOBuilder role(AppRole role) { r.role = role; return this; }
        public AdminUserResponseDTOBuilder status(UserStatus status) { r.status = status; return this; }
        public AdminUserResponseDTOBuilder isVerified(boolean isVerified) { r.isVerified = isVerified; return this; }
        public AdminUserResponseDTOBuilder averageRating(Double averageRating) { r.averageRating = averageRating; return this; }
        public AdminUserResponseDTOBuilder totalReviews(Integer totalReviews) { r.totalReviews = totalReviews; return this; }
        public AdminUserResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public AdminUserResponseDTOBuilder updatedAt(LocalDateTime updatedAt) { r.updatedAt = updatedAt; return this; }
        public AdminUserResponseDTO build() { return r; }
    }
}
