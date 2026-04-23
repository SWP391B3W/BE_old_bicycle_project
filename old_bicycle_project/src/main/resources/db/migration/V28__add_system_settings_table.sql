CREATE TABLE IF NOT EXISTS system_settings (
    setting_key VARCHAR(255) PRIMARY KEY,
    setting_value TEXT NOT NULL,
    description TEXT
);

-- Initialize default platform fee rate (0.1 = 10%)
INSERT INTO system_settings (setting_key, setting_value, description)
VALUES ('platform_fee_rate', '0.1000', 'Tỷ lệ phí dịch vụ của sàn (0.1 = 10%)')
ON CONFLICT (setting_key) DO NOTHING;
