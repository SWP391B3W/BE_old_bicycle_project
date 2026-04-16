package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "brake_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrakeType {

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

    public static BrakeTypeBuilder builder() {
        return new BrakeTypeBuilder();
    }

    public static class BrakeTypeBuilder {
        private BrakeType b = new BrakeType();
        public BrakeTypeBuilder id(UUID id) { b.id = id; return this; }
        public BrakeTypeBuilder name(String name) { b.name = name; return this; }
        public BrakeTypeBuilder description(String description) { b.description = description; return this; }
        public BrakeType build() { return b; }
    }
}
