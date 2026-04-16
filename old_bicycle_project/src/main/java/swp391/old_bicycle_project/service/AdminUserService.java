package swp391.old_bicycle_project.service;

import org.springframework.data.domain.Page;
import swp391.old_bicycle_project.dto.response.AdminUserActivityResponseDTO;
import swp391.old_bicycle_project.dto.response.AdminUserResponseDTO;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.UserStatus;

import java.util.UUID;

public interface AdminUserService {

    Page<AdminUserResponseDTO> getAllUsers(String keyword, AppRole role, UserStatus status, Boolean verified, int page, int size);

    AdminUserResponseDTO getUserById(UUID userId);

    AdminUserResponseDTO updateUserStatus(UUID userId, UserStatus status, UUID adminId);

    String resetUserPassword(UUID userId, String newPassword);

    AdminUserActivityResponseDTO getUserActivity(UUID userId);
}
