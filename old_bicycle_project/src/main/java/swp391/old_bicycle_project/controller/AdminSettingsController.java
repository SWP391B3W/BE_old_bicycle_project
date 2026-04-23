package swp391.old_bicycle_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swp391.old_bicycle_project.entity.SystemSetting;
import swp391.old_bicycle_project.service.AdminSettingsService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/settings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSettingsController {

    private final AdminSettingsService adminSettingsService;

    @GetMapping
    public swp391.old_bicycle_project.dto.response.ApiResponse<List<SystemSetting>> getAllSettings() {
        return swp391.old_bicycle_project.dto.response.ApiResponse.<List<SystemSetting>>builder()
                .result(adminSettingsService.getAllSettings())
                .build();
    }

    @PutMapping("/platform-fee")
    public swp391.old_bicycle_project.dto.response.ApiResponse<SystemSetting> updatePlatformFee(@RequestParam double rate) {
        if (rate < 0 || rate > 1) {
            return swp391.old_bicycle_project.dto.response.ApiResponse.<SystemSetting>builder()
                    .code(400)
                    .message("Tỷ lệ phí phải từ 0 đến 1")
                    .build();
        }
        return swp391.old_bicycle_project.dto.response.ApiResponse.<SystemSetting>builder()
                .result(adminSettingsService.updatePlatformFee(rate))
                .build();
    }
}
