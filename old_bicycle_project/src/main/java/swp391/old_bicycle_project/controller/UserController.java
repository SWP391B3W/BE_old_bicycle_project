package swp391.old_bicycle_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.dto.response.UserPublicResponseDTO;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.exception.AppException;
import swp391.old_bicycle_project.exception.ErrorCode;
import swp391.old_bicycle_project.repository.UserRepository;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{userId}/public")
    @Operation(summary = "Lấy thông tin công khai của người dùng")
    public ApiResponse<UserPublicResponseDTO> getPublicProfile(@PathVariable UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return ApiResponse.<UserPublicResponseDTO>builder()
                .result(UserPublicResponseDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .avatar(user.getAvatarUrl())
                        .joinedDate(user.getCreatedAt())
                        .role(user.getRole().name())
                        .averageRating(user.getAverageRating())
                        .totalReviews(user.getTotalReviews())
                        .build())
                .build();
    }
}
