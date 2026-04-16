package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLogoUrl() { return logoUrl; }

    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public static BrandBuilder builder() {
        return new BrandBuilder();
    }

    public static class BrandBuilder {
        private Brand b = new Brand();
        public BrandBuilder id(UUID id) { b.id = id; return this; }
        public BrandBuilder name(String name) { b.name = name; return this; }
        public BrandBuilder description(String description) { b.description = description; return this; }
        public BrandBuilder logoUrl(String logoUrl) { b.logoUrl = logoUrl; return this; }
        public Brand build() { return b; }
    }
}
