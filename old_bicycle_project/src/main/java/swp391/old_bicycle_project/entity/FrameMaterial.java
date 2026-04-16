package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "frame_materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrameMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }

    public static FrameMaterialBuilder builder() {
        return new FrameMaterialBuilder();
    }

    public static class FrameMaterialBuilder {
        private FrameMaterial f = new FrameMaterial();
        public FrameMaterialBuilder id(UUID id) { f.id = id; return this; }
        public FrameMaterialBuilder name(String name) { f.name = name; return this; }
        public FrameMaterialBuilder description(String description) { f.description = description; return this; }
        public FrameMaterial build() { return f; }
    }
}
