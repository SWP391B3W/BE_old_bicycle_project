package swp391.old_bicycle_project.controller;

import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.dto.response.WishlistItemResponseDTO;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Quản lý danh sách xe yêu thích")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Thêm sản phẩm vào danh sách yêu thích")
    public ApiResponse<WishlistItemResponseDTO> addToWishlist(
            @PathVariable UUID productId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ApiResponse.<WishlistItemResponseDTO>builder()
                .result(wishlistService.addProduct(currentUser.getId(), productId))
                .build();
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Xóa sản phẩm khỏi danh sách yêu thích")
    public ApiResponse<String> removeFromWishlist(
            @PathVariable UUID productId,
            @AuthenticationPrincipal User currentUser
    ) {
        wishlistService.removeProduct(currentUser.getId(), productId);
        return ApiResponse.<String>builder()
                .result("Đã xóa khỏi danh sách yêu thích")
                .build();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lấy danh sách xe yêu thích của người dùng")
    public ApiResponse<List<WishlistItemResponseDTO>> getMyWishlist(
            @AuthenticationPrincipal User currentUser
    ) {
        return ApiResponse.<List<WishlistItemResponseDTO>>builder()
                .result(wishlistService.getWishlist(currentUser.getId()))
                .build();
    }
}
