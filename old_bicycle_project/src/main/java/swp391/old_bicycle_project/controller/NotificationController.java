package swp391.old_bicycle_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Operation(summary = "Get current user's notifications")
    public ApiResponse<Page<NotificationResponseDTO>> getMyNotifications(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.<Page<NotificationResponseDTO>>builder()
                .result(notificationService.getUserNotifications(currentUser.getId(), pageable))
                .build();
    }

    @GetMapping("/me/unread-count")
    @Operation(summary = "Get current user's unread notification count")
    public ApiResponse<Long> getMyUnreadCount(@AuthenticationPrincipal User currentUser) {
        return ApiResponse.<Long>builder()
                .result(notificationService.getUnreadCount(currentUser.getId()))
                .build();
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark a notification as read")
    public ApiResponse<Void> markAsRead(
            @AuthenticationPrincipal User currentUser,
            @PathVariable UUID id
    ) {
        notificationService.markAsRead(id, currentUser.getId());
        return ApiResponse.<Void>builder().build();
    }

    @PutMapping("/me/read-all")
    @Operation(summary = "Mark all current user's notifications as read")
    public ApiResponse<Void> markAllAsRead(@AuthenticationPrincipal User currentUser) {
        notificationService.markAllAsRead(currentUser.getId());
        return ApiResponse.<Void>builder().build();
    }
}
