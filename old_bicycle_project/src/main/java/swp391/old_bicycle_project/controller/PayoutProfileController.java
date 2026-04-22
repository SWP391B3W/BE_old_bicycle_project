package swp391.old_bicycle_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swp391.old_bicycle_project.dto.request.PayoutProfileUpsertRequestDTO;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.dto.response.PayoutProfileResponseDTO;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.service.PayoutService;

@RestController
@RequestMapping("/api/payout-profiles")
@RequiredArgsConstructor
public class PayoutProfileController {

    private final PayoutService payoutService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lấy payout profile của người dùng hiện tại")
    public ApiResponse<PayoutProfileResponseDTO> getMyProfile(@AuthenticationPrincipal User currentUser) {
        return ApiResponse.<PayoutProfileResponseDTO>builder()
                .code(200)
                .message("Payout profile fetched successfully")
                .result(payoutService.getMyProfile(currentUser))
                .build();
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Tạo/cập nhật payout profile của người dùng hiện tại")
    public ApiResponse<PayoutProfileResponseDTO> upsertMyProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody PayoutProfileUpsertRequestDTO request
    ) {
        return ApiResponse.<PayoutProfileResponseDTO>builder()
                .code(200)
                .message("Payout profile updated successfully")
                .result(payoutService.upsertMyProfile(currentUser, request))
                .build();
    }
}
