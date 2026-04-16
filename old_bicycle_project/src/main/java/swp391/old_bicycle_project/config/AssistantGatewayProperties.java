package swp391.old_bicycle_project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "assistant.gateway")
public class AssistantGatewayProperties {
    private boolean enabled = false;
    private String apiBaseUrl = "https://ai-gateway.vercel.sh/v1/chat/completions";
    private String apiKey;
    private String model = "google/gemini-2.5-flash-lite";
    private double temperature = 0.2;
    private int maxTokens = 600;

    // Manual Getters
    public boolean isEnabled() { return enabled; }
    public String getApiBaseUrl() { return apiBaseUrl; }
    public String getApiKey() { return apiKey; }
    public String getModel() { return model; }
    public double getTemperature() { return temperature; }
    public int getMaxTokens() { return maxTokens; }

    // Manual Setters
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setApiBaseUrl(String apiBaseUrl) { this.apiBaseUrl = apiBaseUrl; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public void setModel(String model) { this.model = model; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
}
