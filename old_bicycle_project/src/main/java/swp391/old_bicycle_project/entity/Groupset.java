package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "groupsets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Groupset {

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

    public static GroupsetBuilder builder() {
        return new GroupsetBuilder();
    }

    public static class GroupsetBuilder {
        private Groupset g = new Groupset();
        public GroupsetBuilder id(UUID id) { g.id = id; return this; }
        public GroupsetBuilder name(String name) { g.name = name; return this; }
        public GroupsetBuilder description(String description) { g.description = description; return this; }
        public Groupset build() { return g; }
    }
}
