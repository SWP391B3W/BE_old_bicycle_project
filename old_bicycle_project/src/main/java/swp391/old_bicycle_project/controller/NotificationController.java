package swp391.old_bicycle_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/me/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Long> getUnreadCount(@AuthenticationPrincipal User currentUser) {
        return ApiResponse.<Long>builder()
                .code(200)
                .message("Fetched unread notification count successfully")
                .result(notificationService.getUnreadCount(currentUser.getId()))
                .build();
    }
}
