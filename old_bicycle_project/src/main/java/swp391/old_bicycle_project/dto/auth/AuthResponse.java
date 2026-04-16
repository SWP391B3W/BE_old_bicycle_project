package swp391.old_bicycle_project.dto.auth;

import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserInfo user;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String refreshToken, String tokenType, Long expiresIn, UserInfo user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    // Manual Getter (Explicitly for Safety)
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getTokenType() { return tokenType; }
    public Long getExpiresIn() { return expiresIn; }
    public UserInfo getUser() { return user; }

    // Manual Builder
    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }
    public static class AuthResponseBuilder {
        private AuthResponse r = new AuthResponse();
        public AuthResponseBuilder accessToken(String accessToken) { r.accessToken = accessToken; return this; }
        public AuthResponseBuilder refreshToken(String refreshToken) { r.refreshToken = refreshToken; return this; }
        public AuthResponseBuilder tokenType(String tokenType) { r.tokenType = tokenType; return this; }
        public AuthResponseBuilder expiresIn(Long expiresIn) { r.expiresIn = expiresIn; return this; }
        public AuthResponseBuilder user(UserInfo user) { r.user = user; return this; }
        public AuthResponse build() { return r; }
    }

    @Data
    @Builder
    public static class UserInfo {
        private UUID id;
        private String email;
        private String firstName;
        private String lastName;
        private String phone;
        private String avatarUrl;
        private String defaultAddress;
        private AppRole role;
        private UserStatus status;
        private boolean isVerified;

        public UserInfo() {}

        public UserInfo(UUID id, String email, String firstName, String lastName, String phone, String avatarUrl, String defaultAddress, AppRole role, UserStatus status, boolean isVerified) {
            this.id = id;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.avatarUrl = avatarUrl;
            this.defaultAddress = defaultAddress;
            this.role = role;
            this.status = status;
            this.isVerified = isVerified;
        }

        // Manual Getter
        public UUID getId() { return id; }
        public String getEmail() { return email; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhone() { return phone; }
        public String getAvatarUrl() { return avatarUrl; }
        public String getDefaultAddress() { return defaultAddress; }
        public AppRole getRole() { return role; }
        public UserStatus getStatus() { return status; }
        public boolean isVerified() { return isVerified; }

        // Manual Builder
        public static UserInfoBuilder builder() { return new UserInfoBuilder(); }
        public static class UserInfoBuilder {
            private UserInfo r = new UserInfo();
            public UserInfoBuilder id(UUID id) { r.id = id; return this; }
            public UserInfoBuilder email(String email) { r.email = email; return this; }
            public UserInfoBuilder firstName(String firstName) { r.firstName = firstName; return this; }
            public UserInfoBuilder lastName(String lastName) { r.lastName = lastName; return this; }
            public UserInfoBuilder phone(String phone) { r.phone = phone; return this; }
            public UserInfoBuilder avatarUrl(String avatarUrl) { r.avatarUrl = avatarUrl; return this; }
            public UserInfoBuilder defaultAddress(String defaultAddress) { r.defaultAddress = defaultAddress; return this; }
            public UserInfoBuilder role(AppRole role) { r.role = role; return this; }
            public UserInfoBuilder status(UserStatus status) { r.status = status; return this; }
            public UserInfoBuilder isVerified(boolean isVerified) { r.isVerified = isVerified; return this; }
            public UserInfo build() { return r; }
        }
    }
}
