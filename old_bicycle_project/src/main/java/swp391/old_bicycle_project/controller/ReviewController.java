package swp391.old_bicycle_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import swp391.old_bicycle_project.dto.request.ReviewReplyRequestDTO;
import swp391.old_bicycle_project.dto.request.ReviewRequestDTO;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.dto.response.ReviewResponseDTO;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.service.ReviewService;

import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('BUYER')")
    @Operation(summary = "Đăng đánh giá cho đơn hàng")
    public ApiResponse<ReviewResponseDTO> submitReview(
            @AuthenticationPrincipal User currentUser,
            @RequestBody @Valid ReviewRequestDTO requestDTO) {
        return ApiResponse.<ReviewResponseDTO>builder()
                .result(reviewService.submitReview(requestDTO.getOrderId(), currentUser.getId(), requestDTO))
                .build();
    }

    @GetMapping("/seller/{sellerId}")
    @Operation(summary = "Lấy danh sách đánh giá của một Seller")
    public ApiResponse<Page<ReviewResponseDTO>> getSellerReviews(
            @PathVariable UUID sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.<Page<ReviewResponseDTO>>builder()
                .result(reviewService.getSellerReviews(sellerId, pageable))
                .build();
    }

    @GetMapping("/buyer/{buyerId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    @Operation(summary = "Lấy danh sách đánh giá mà một Buyer đã thực hiện")
    public ApiResponse<Page<ReviewResponseDTO>> getBuyerReviews(
            @PathVariable UUID buyerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.<Page<ReviewResponseDTO>>builder()
                .result(reviewService.getBuyerReviews(buyerId, pageable))
                .build();
    }

    @PostMapping("/{reviewId}/reply")
    @PreAuthorize("hasRole('SELLER')")
    @Operation(summary = "Seller phản hồi đánh giá")
    public ApiResponse<ReviewResponseDTO> replyToReview(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal User currentUser,
            @RequestBody @Valid ReviewReplyRequestDTO requestDTO) {
        return ApiResponse.<ReviewResponseDTO>builder()
                .result(reviewService.replyToReview(reviewId, currentUser.getId(), requestDTO))
                .build();
    }
}
