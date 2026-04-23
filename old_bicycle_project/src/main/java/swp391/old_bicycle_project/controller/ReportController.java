package swp391.old_bicycle_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swp391.old_bicycle_project.dto.request.ReportRequestDTO;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.dto.response.ReportResponseDTO;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.service.ReportService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Gửi báo cáo vi phạm (kèm file)")
    public ApiResponse<ReportResponseDTO> submitReport(
            @AuthenticationPrincipal User currentUser,
            @ModelAttribute ReportRequestDTO requestDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return ApiResponse.<ReportResponseDTO>builder()
                .result(reportService.submitReport(currentUser.getId(), requestDTO, files))
                .build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lấy danh sách báo cáo tôi đã gửi")
    public ApiResponse<Page<ReportResponseDTO>> getMyReports(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.<Page<Page<ReportResponseDTO>>>builder()
                .result(reportService.getMyReports(currentUser.getId(), pageable))
                .build();
    }
}
