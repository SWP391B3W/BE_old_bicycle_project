package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public String getDescription() { return description; }
    public Category getParent() { return parent; }

    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSlug(String slug) { this.slug = slug; }
    public void setDescription(String description) { this.description = description; }
    public void setParent(Category parent) { this.parent = parent; }

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }

    public static class CategoryBuilder {
        private Category c = new Category();
        public CategoryBuilder id(UUID id) { c.id = id; return this; }
        public CategoryBuilder name(String name) { c.name = name; return this; }
        public CategoryBuilder slug(String slug) { c.slug = slug; return this; }
        public CategoryBuilder description(String description) { c.description = description; return this; }
        public CategoryBuilder parent(Category parent) { c.parent = parent; return this; }
        public Category build() { return c; }
    }
}
