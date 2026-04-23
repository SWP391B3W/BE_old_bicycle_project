package swp391.old_bicycle_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swp391.old_bicycle_project.entity.SystemSetting;
import swp391.old_bicycle_project.entity.User;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.NotificationType;
import swp391.old_bicycle_project.repository.SystemSettingRepository;
import swp391.old_bicycle_project.repository.UserRepository;
import swp391.old_bicycle_project.service.AdminSettingsService;
import swp391.old_bicycle_project.service.NotificationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSettingsServiceImpl implements AdminSettingsService {

    private final SystemSettingRepository systemSettingRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public List<SystemSetting> getAllSettings() {
        return systemSettingRepository.findAll();
    }

    @Override
    public SystemSetting getSetting(String key) {
        return systemSettingRepository.findByKey(key)
                .orElse(null);
    }

    @Override
    @Transactional
    public SystemSetting updatePlatformFee(double newRate) {
        String rateString = String.valueOf(newRate);
        SystemSetting setting = systemSettingRepository.findByKey("platform_fee_rate")
                .orElse(SystemSetting.builder()
                        .key("platform_fee_rate")
                        .description("Tỷ lệ phí dịch vụ của sàn (0.1 = 10%)")
                        .build());
        
        setting.setValue(rateString);
        SystemSetting savedSetting = systemSettingRepository.save(setting);

        // Notify all sellers
        notifySellersOfFeeChange(newRate);

        return savedSetting;
    }

    private void notifySellersOfFeeChange(double newRate) {
        List<User> sellers = userRepository.findByRole(AppRole.seller);
        String percentage = (int) (newRate * 100) + "%";
        String title = "Thông báo thay đổi phí dịch vụ sàn";
        String content = "Phí dịch vụ của sàn đã được cập nhật mới là " + percentage + ". Phí mới sẽ được áp dụng cho các giao dịch được tạo từ thời điểm này.";

        for (User seller : sellers) {
            notificationService.sendNotification(
                    seller.getId(),
                    title,
                    content,
                    NotificationType.system,
                    "{\"type\":\"fee_update\",\"newRate\":" + newRate + "}"
            );
        }
    }
}
