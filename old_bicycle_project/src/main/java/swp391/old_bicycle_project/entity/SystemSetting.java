package swp391.old_bicycle_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_settings")
public class SystemSetting {

    @Id
    @Column(name = "setting_key")
    private String key;

    @Column(name = "setting_value", nullable = false)
    private String value;

    @Column(name = "description")
    private String description;

    public SystemSetting() {}

    public SystemSetting(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static SystemSettingBuilder builder() { return new SystemSettingBuilder(); }

    public static class SystemSettingBuilder {
        private final SystemSetting r = new SystemSetting();
        public SystemSettingBuilder key(String key) { r.key = key; return this; }
        public SystemSettingBuilder value(String value) { r.value = value; return this; }
        public SystemSettingBuilder description(String description) { r.description = description; return this; }
        public SystemSetting build() { return r; }
    }
}
