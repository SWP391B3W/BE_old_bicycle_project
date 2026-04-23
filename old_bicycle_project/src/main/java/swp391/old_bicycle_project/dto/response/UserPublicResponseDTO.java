package swp391.old_bicycle_project.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserPublicResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String avatar;
    private LocalDateTime joinedDate;
    private String role;
    private Double averageRating;
    private Integer totalReviews;

    public UserPublicResponseDTO() {}

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAvatar() { return avatar; }
    public LocalDateTime getJoinedDate() { return joinedDate; }
    public String getRole() { return role; }
    public Double getAverageRating() { return averageRating; }
    public Integer getTotalReviews() { return totalReviews; }

    public static UserPublicResponseDTOBuilder builder() { return new UserPublicResponseDTOBuilder(); }

    public static class UserPublicResponseDTOBuilder {
        private final UserPublicResponseDTO r = new UserPublicResponseDTO();
        public UserPublicResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public UserPublicResponseDTOBuilder firstName(String firstName) { r.firstName = firstName; return this; }
        public UserPublicResponseDTOBuilder lastName(String lastName) { r.lastName = lastName; return this; }
        public UserPublicResponseDTOBuilder avatar(String avatar) { r.avatar = avatar; return this; }
        public UserPublicResponseDTOBuilder joinedDate(LocalDateTime joinedDate) { r.joinedDate = joinedDate; return this; }
        public UserPublicResponseDTOBuilder role(String role) { r.role = role; return this; }
        public UserPublicResponseDTOBuilder averageRating(Double averageRating) { r.averageRating = averageRating; return this; }
        public UserPublicResponseDTOBuilder totalReviews(Integer totalReviews) { r.totalReviews = totalReviews; return this; }
        public UserPublicResponseDTO build() { return r; }
    }
}
