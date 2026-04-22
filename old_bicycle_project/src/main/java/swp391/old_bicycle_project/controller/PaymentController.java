package swp391.old_bicycle_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.dto.response.PaymentRequestResponseDTO;
import swp391.old_bicycle_project.dto.response.PaymentResponseDTO;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.service.PaymentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/orders/{orderId}/upfront-request")
    @PreAuthorize("hasRole('BUYER')")
    @Operation(summary = "Tạo yêu cầu thanh toán ứng trước")
    public ApiResponse<PaymentRequestResponseDTO> createUpfrontPaymentRequest(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ApiResponse.<PaymentRequestResponseDTO>builder()
                .code(200)
                .message("Payment request created successfully")
                .result(paymentService.createUpfrontPaymentRequest(orderId, currentUser))
                .build();
    }

    @GetMapping("/orders/{orderId}")
    @PreAuthorize("hasAnyRole('BUYER','SELLER','ADMIN')")
    @Operation(summary = "Lấy lịch sử thanh toán theo đơn")
    public ApiResponse<List<PaymentResponseDTO>> getOrderPayments(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ApiResponse.<List<PaymentResponseDTO>>builder()
                .code(200)
                .message("Payments fetched successfully")
                .result(paymentService.getOrderPayments(orderId, currentUser))
                .build();
    }

    @PostMapping(value = "/sepay/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Webhook nhận trạng thái thanh toán từ SePay")
    public ApiResponse<Void> handleSepayWebhook(
            @RequestBody(required = false) String rawPayload,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader
    ) {
        paymentService.handleSepayWebhook(rawPayload, authorizationHeader);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Webhook processed successfully")
                .build();
    }
}
