package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "size_chart_rows",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_size_chart_rows_chart_frame_size", columnNames = {"size_chart_id", "frame_size"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizeChartRow {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "size_chart_id", nullable = false)
    private SizeChart sizeChart;

    @Column(name = "frame_size", nullable = false)
    private String frameSize;

    @Column(name = "height_min_cm", nullable = false)
    private Integer heightMinCm;

    @Column(name = "height_max_cm", nullable = false)
    private Integer heightMaxCm;

    @Column(columnDefinition = "text")
    private String note;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    // Manual Getters
    public UUID getId() { return id; }
    public SizeChart getSizeChart() { return sizeChart; }
    public String getFrameSize() { return frameSize; }
    public Integer getHeightMinCm() { return heightMinCm; }
    public Integer getHeightMaxCm() { return heightMaxCm; }
    public String getNote() { return note; }
    public Integer getDisplayOrder() { return displayOrder; }

    public void setSizeChart(SizeChart sizeChart) { this.sizeChart = sizeChart; }
    public void setFrameSize(String frameSize) { this.frameSize = frameSize; }
    public void setHeightMinCm(Integer h) { this.heightMinCm = h; }
    public void setHeightMaxCm(Integer h) { this.heightMaxCm = h; }
    public void setNote(String note) { this.note = note; }
    public void setDisplayOrder(Integer order) { this.displayOrder = order; }

    public static SizeChartRowBuilder builder() {
        return new SizeChartRowBuilder();
    }

    public static class SizeChartRowBuilder {
        private SizeChartRow scr = new SizeChartRow();
        public SizeChartRowBuilder id(UUID id) { scr.id = id; return this; }
        public SizeChartRowBuilder sizeChart(SizeChart sizeChart) { scr.sizeChart = sizeChart; return this; }
        public SizeChartRowBuilder frameSize(String frameSize) { scr.frameSize = frameSize; return this; }
        public SizeChartRowBuilder heightMinCm(Integer h) { scr.heightMinCm = h; return this; }
        public SizeChartRowBuilder heightMaxCm(Integer h) { scr.heightMaxCm = h; return this; }
        public SizeChartRowBuilder note(String note) { scr.note = note; return this; }
        public SizeChartRowBuilder displayOrder(Integer order) { scr.displayOrder = order; return this; }
        public SizeChartRow build() { return scr; }
    }
}
