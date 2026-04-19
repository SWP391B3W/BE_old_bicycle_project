package swp391.old_bicycle_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swp391.old_bicycle_project.dto.product.ProductResponse;
import swp391.old_bicycle_project.dto.request.AdminProductStatusUpdateRequest;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.entity.enums.ProductStatus;
import swp391.old_bicycle_project.service.ProductService;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get products for admin moderation")
    public ApiResponse<Page<ProductResponse>> getAdminProducts(
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) UUID sellerId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ApiResponse.<Page<ProductResponse>>builder()
                .code(200)
                .message("Admin products fetched successfully")
                .result(productService.getAllForAdmin(status, sellerId, keyword, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get product detail for admin")
    public ApiResponse<ProductResponse> getAdminProductById(@PathVariable UUID id) {
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Admin product detail fetched successfully")
                .result(productService.getAdminById(id))
                .build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change product status by admin")
    public ApiResponse<ProductResponse> changeProductStatus(
            @PathVariable UUID id,
            @Valid @RequestBody AdminProductStatusUpdateRequest request
    ) {
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Product status updated successfully")
                .result(productService.changeStatus(id, request.getStatus()))
                .build();
    }
}
