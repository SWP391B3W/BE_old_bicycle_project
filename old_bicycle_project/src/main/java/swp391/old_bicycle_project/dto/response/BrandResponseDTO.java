package swp391.old_bicycle_project.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class BrandResponseDTO {

    private UUID id;
    private String name;
    private String logoUrl;
    private LocalDateTime createdAt;

    public BrandResponseDTO() {}

    public BrandResponseDTO(UUID id, String name, String logoUrl, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.createdAt = createdAt;
    }

    // Manual Getter
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getLogoUrl() { return logoUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Builder
    public static BrandResponseDTOBuilder builder() { return new BrandResponseDTOBuilder(); }
    public static class BrandResponseDTOBuilder {
        private BrandResponseDTO r = new BrandResponseDTO();
        public BrandResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public BrandResponseDTOBuilder name(String name) { r.name = name; return this; }
        public BrandResponseDTOBuilder logoUrl(String logoUrl) { r.logoUrl = logoUrl; return this; }
        public BrandResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public BrandResponseDTO build() { return r; }
    }
}
