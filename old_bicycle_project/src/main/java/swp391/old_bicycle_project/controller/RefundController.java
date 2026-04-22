package swp391.old_bicycle_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import swp391.old_bicycle_project.dto.request.RefundCreateRequestDTO;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.dto.response.RefundResponseDTO;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.service.RefundService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping(value = "/orders/{orderId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('BUYER')")
    @Operation(summary = "Người mua tạo yêu cầu hoàn tiền")
    public ApiResponse<RefundResponseDTO> requestRefund(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestPart("request") RefundCreateRequestDTO request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "files[]", required = false) List<MultipartFile> filesArray
    ) {
        List<MultipartFile> mergedFiles = mergeFiles(files, filesArray);
        return ApiResponse.<RefundResponseDTO>builder()
                .code(200)
                .message("Refund requested successfully")
                .result(refundService.requestRefund(orderId, currentUser, request, mergedFiles))
                .build();
    }

    private List<MultipartFile> mergeFiles(List<MultipartFile> first, List<MultipartFile> second) {
        List<MultipartFile> merged = new ArrayList<>();
        if (first != null && !first.isEmpty()) {
            merged.addAll(first);
        }
        if (second != null && !second.isEmpty()) {
            merged.addAll(second);
        }
        return merged;
    }
}
