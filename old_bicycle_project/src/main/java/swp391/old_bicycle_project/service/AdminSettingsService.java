package swp391.old_bicycle_project.service;

import swp391.old_bicycle_project.entity.SystemSetting;
import java.util.List;

public interface AdminSettingsService {
    List<SystemSetting> getAllSettings();
    SystemSetting getSetting(String key);
    SystemSetting updatePlatformFee(double newRate);
}
