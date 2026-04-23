package swp391.old_bicycle_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import swp391.old_bicycle_project.dto.request.ReportProcessDTO;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.dto.response.ReportResponseDTO;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.ReportStatus;
import swp391.old_bicycle_project.service.ReportService;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

    private final ReportService reportService;

    @GetMapping
    @Operation(summary = "Admin lấy danh sách tất cả báo cáo (có filter)")
    public ApiResponse<Page<ReportResponseDTO>> getAllReports(
            @RequestParam(required = false) ReportStatus status,
            @RequestParam(required = false) String targetType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.<Page<ReportResponseDTO>>builder()
                .result(reportService.getAllReports(status, targetType, pageable))
                .build();
    }

    @PutMapping("/{reportId}/process")
    @Operation(summary = "Admin xử lý báo cáo vi phạm")
    public ApiResponse<ReportResponseDTO> processReport(
            @PathVariable UUID reportId,
            @AuthenticationPrincipal User adminUser,
            @RequestBody ReportProcessDTO processDTO) {
        return ApiResponse.<ReportResponseDTO>builder()
                .result(reportService.processReport(reportId, processDTO, adminUser.getId()))
                .build();
    }
}
