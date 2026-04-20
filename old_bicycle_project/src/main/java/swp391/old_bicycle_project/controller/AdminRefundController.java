package swp391.old_bicycle_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swp391.old_bicycle_project.dto.request.RefundReviewRequestDTO;
import swp391.old_bicycle_project.dto.response.AdminRefundResponseDTO;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.dto.response.RefundResponseDTO;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.RefundStatus;
import swp391.old_bicycle_project.service.RefundService;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/refunds")
@RequiredArgsConstructor
public class AdminRefundController {

    private final RefundService refundService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin lấy danh sách yêu cầu hoàn tiền")
    public ApiResponse<Page<AdminRefundResponseDTO>> getAdminRefunds(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) RefundStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ApiResponse.<Page<AdminRefundResponseDTO>>builder()
                .code(200)
                .message("Admin refunds fetched successfully")
                .result(refundService.getAdminRefunds(keyword, status, page, size))
                .build();
    }

    @PatchMapping("/{refundId}/review")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin duyệt/từ chối/hoàn tất yêu cầu hoàn tiền")
    public ApiResponse<RefundResponseDTO> reviewRefund(
            @PathVariable UUID refundId,
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody RefundReviewRequestDTO request
    ) {
        return ApiResponse.<RefundResponseDTO>builder()
                .code(200)
                .message("Refund reviewed successfully")
                .result(refundService.reviewRefund(refundId, currentUser, request))
                .build();
    }
}
