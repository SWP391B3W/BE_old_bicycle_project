package swp391.old_bicycle_project.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GhnConfig {
    @Value("${ghn.api.url}")
    private String apiUrl;

    @Value("${ghn.api.token}")
    private String apiToken;

    @Value("${ghn.shop.id}")
    private Integer shopId;

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public Integer getShopId() {
        return shopId;
    }
}
