package swp391.old_bicycle_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swp391.old_bicycle_project.dto.order.OrderCreateRequest;
import swp391.old_bicycle_project.dto.order.OrderResponse;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('BUYER')")
    @Operation(summary = "Tạo đơn mua xe")
    public ApiResponse<OrderResponse> createOrder(
            @AuthenticationPrincipal User currentUser,
            @RequestBody @Valid OrderCreateRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .message("Order created successfully")
                .result(orderService.createOrder(currentUser, request))
                .build();
    }

    @GetMapping("/me")
    @Operation(summary = "Lấy danh sách đơn của tôi")
    public ApiResponse<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal User currentUser) {
        return ApiResponse.<List<OrderResponse>>builder()
                .message("Fetched orders successfully")
                .result(orderService.getMyOrders(currentUser))
                .build();
    }

    @PatchMapping("/{orderId}/accept")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Operation(summary = "Người bán chấp nhận đơn")
    public ApiResponse<OrderResponse> acceptOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal User currentUser) {
        return ApiResponse.<OrderResponse>builder()
                .message("Order accepted successfully")
                .result(orderService.acceptOrder(orderId, currentUser))
                .build();
    }

    @PatchMapping("/{orderId}/confirm-deposit")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Operation(summary = "Người bán xác nhận đã nhận cọc")
    public ApiResponse<OrderResponse> confirmDeposit(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal User currentUser) {
        return ApiResponse.<OrderResponse>builder()
                .message("Deposit confirmed successfully")
                .result(orderService.confirmDeposit(orderId, currentUser))
                .build();
    }

    @PatchMapping(value = "/{orderId}/complete", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Operation(summary = "Người bán báo đã giao xe")
    public ApiResponse<OrderResponse> completeOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal User currentUser,
            @RequestPart(value = "note", required = false) String note,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return ApiResponse.<OrderResponse>builder()
                .message("Delivery reported successfully")
                .result(orderService.completeOrder(orderId, currentUser, note, files))
                .build();
    }

    @PatchMapping(value = "/{orderId}/confirm-received", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('BUYER', 'ADMIN')")
    @Operation(summary = "Người mua xác nhận đã nhận xe")
    public ApiResponse<OrderResponse> confirmReceived(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal User currentUser,
            @RequestPart(value = "note", required = false) String note,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return ApiResponse.<OrderResponse>builder()
                .message("Order receipt confirmed successfully")
                .result(orderService.confirmReceived(orderId, currentUser, note, files))
                .build();
    }

    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN')")
    @Operation(summary = "Huỷ đơn")
    public ApiResponse<OrderResponse> cancelOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal User currentUser) {
        return ApiResponse.<OrderResponse>builder()
                .message("Order cancelled successfully")
                .result(orderService.cancelOrder(orderId, currentUser))
                .build();
    }
}
