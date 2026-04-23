package swp391.old_bicycle_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.dto.response.NotificationResponseDTO;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.service.NotificationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Page<NotificationResponseDTO>> getMyNotifications(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.<Page<NotificationResponseDTO>>builder()
                .code(200)
                .message("Fetched notifications successfully")
                .result(notificationService.getUserNotifications(currentUser.getId(), pageable))
                .build();
    }

    @GetMapping("/me/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Long> getUnreadCount(@AuthenticationPrincipal User currentUser) {
        return ApiResponse.<Long>builder()
                .code(200)
                .message("Fetched unread notification count successfully")
                .result(notificationService.getUnreadCount(currentUser.getId()))
                .build();
    }

    @RequestMapping(value = "/{notificationId}/read", method = {RequestMethod.PATCH, RequestMethod.PUT})
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> markAsRead(
            @PathVariable UUID notificationId,
            @AuthenticationPrincipal User currentUser) {
        notificationService.markAsRead(notificationId, currentUser.getId());
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Notification marked as read")
                .build();
    }

    @RequestMapping(value = "/me/read-all", method = {RequestMethod.PATCH, RequestMethod.PUT})
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> markAllAsRead(@AuthenticationPrincipal User currentUser) {
        notificationService.markAllAsRead(currentUser.getId());
        return ApiResponse.<Void>builder()
                .code(200)
                .message("All notifications marked as read")
                .build();
    }
}
