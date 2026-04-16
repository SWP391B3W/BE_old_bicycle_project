package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "size_charts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_size_charts_category", columnNames = "category_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizeChart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "sizeChart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<SizeChartRow> rows = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Manual Getters
    public UUID getId() { return id; }
    public Category getCategory() { return category; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<SizeChartRow> getRows() { return rows; }

    public void setCategory(Category category) { this.category = category; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setRows(List<SizeChartRow> rows) { this.rows = rows; }

    public static SizeChartBuilder builder() {
        return new SizeChartBuilder();
    }

    public static class SizeChartBuilder {
        private SizeChart sc = new SizeChart();
        public SizeChartBuilder id(UUID id) { sc.id = id; return this; }
        public SizeChartBuilder category(Category category) { sc.category = category; return this; }
        public SizeChartBuilder name(String name) { sc.name = name; return this; }
        public SizeChartBuilder description(String description) { sc.description = description; return this; }
        public SizeChartBuilder rows(List<SizeChartRow> rows) { sc.rows = rows; return this; }
        public SizeChart build() { return sc; }
    }
}
