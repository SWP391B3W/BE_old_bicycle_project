package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminBrandUpsertRequest {

    @NotBlank(message = "Brand name is required")
    private String name;

    private String logoUrl;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
}
