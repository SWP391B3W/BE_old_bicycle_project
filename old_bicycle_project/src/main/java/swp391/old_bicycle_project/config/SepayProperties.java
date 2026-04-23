package swp391.old_bicycle_project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "payment.sepay")
public class SepayProperties {
    private boolean mockMode = true;
    private String apiBaseUrl = "https://userapi.sepay.vn";
    private String apiToken;
    private String webhookApiKey;
    private String bankAccountId;
    private String bankBin;
    private String accountNumber;
    private String accountName;

    // Manual Getters
    public boolean isMockMode() { return mockMode; }
    public String getApiBaseUrl() { return apiBaseUrl; }
    public String getApiToken() { return apiToken; }
    public String getWebhookApiKey() { return webhookApiKey; }
    public String getBankAccountId() { return bankAccountId; }
    public String getBankBin() { return bankBin; }
    public String getAccountNumber() { return accountNumber; }
    public String getAccountName() { return accountName; }

    // Manual Setters
    public void setMockMode(boolean mockMode) { this.mockMode = mockMode; }
    public void setApiBaseUrl(String apiBaseUrl) { this.apiBaseUrl = apiBaseUrl; }
    public void setApiToken(String apiToken) { this.apiToken = apiToken; }
    public void setWebhookApiKey(String webhookApiKey) { this.webhookApiKey = webhookApiKey; }
    public void setBankAccountId(String bankAccountId) { this.bankAccountId = bankAccountId; }
    public void setBankBin(String bankBin) { this.bankBin = bankBin; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
}
