package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inspections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspector_id")
    private User inspector;

    @Column(name = "overall_score", precision = 3, scale = 1)
    private BigDecimal overallScore;

    @Column(name = "frame_score")
    private Integer frameScore;

    @Column(name = "fork_score")
    private Integer forkScore;

    @Column(name = "brakes_score")
    private Integer brakesScore;

    @Column(name = "drivetrain_score")
    private Integer drivetrainScore;

    @Column(name = "wheels_score")
    private Integer wheelsScore;

    @Column(name = "wear_percentage")
    private Integer wearPercentage;

    @Column(name = "expert_notes", columnDefinition = "TEXT")
    private String expertNotes;

    @Builder.Default
    @Column(name = "passed")
    private Boolean passed = false;

    @Column(name = "report_file_url", columnDefinition = "TEXT")
    private String reportFileUrl;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Manual Getters
    public UUID getId() { return id; }
    public Product getProduct() { return product; }
    public User getInspector() { return inspector; }
    public BigDecimal getOverallScore() { return overallScore; }
    public Integer getFrameScore() { return frameScore; }
    public Integer getForkScore() { return forkScore; }
    public Integer getBrakesScore() { return brakesScore; }
    public Integer getDrivetrainScore() { return drivetrainScore; }
    public Integer getWheelsScore() { return wheelsScore; }
    public Integer getWearPercentage() { return wearPercentage; }
    public String getExpertNotes() { return expertNotes; }
    public Boolean getPassed() { return passed; }
    public String getReportFileUrl() { return reportFileUrl; }
    public LocalDateTime getValidUntil() { return validUntil; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setProduct(Product product) { this.product = product; }
    public void setInspector(User inspector) { this.inspector = inspector; }
    public void setOverallScore(BigDecimal score) { this.overallScore = score; }
    public void setFrameScore(Integer score) { this.frameScore = score; }
    public void setForkScore(Integer score) { this.forkScore = score; }
    public void setBrakesScore(Integer score) { this.brakesScore = score; }
    public void setDrivetrainScore(Integer score) { this.drivetrainScore = score; }
    public void setWheelsScore(Integer score) { this.wheelsScore = score; }
    public void setWearPercentage(Integer pct) { this.wearPercentage = pct; }
    public void setExpertNotes(String notes) { this.expertNotes = notes; }
    public void setPassed(Boolean passed) { this.passed = passed; }
    public void setReportFileUrl(String url) { this.reportFileUrl = url; }
    public void setValidUntil(LocalDateTime d) { this.validUntil = d; }

    public static InspectionBuilder builder() {
        return new InspectionBuilder();
    }

    public static class InspectionBuilder {
        private Inspection i = new Inspection();
        public InspectionBuilder id(UUID id) { i.id = id; return this; }
        public InspectionBuilder product(Product product) { i.product = product; return this; }
        public InspectionBuilder inspector(User inspector) { i.inspector = inspector; return this; }
        public InspectionBuilder overallScore(BigDecimal score) { i.overallScore = score; return this; }
        public InspectionBuilder frameScore(Integer score) { i.frameScore = score; return this; }
        public InspectionBuilder forkScore(Integer score) { i.forkScore = score; return this; }
        public InspectionBuilder brakesScore(Integer score) { i.brakesScore = score; return this; }
        public InspectionBuilder drivetrainScore(Integer score) { i.drivetrainScore = score; return this; }
        public InspectionBuilder wheelsScore(Integer score) { i.wheelsScore = score; return this; }
        public InspectionBuilder wearPercentage(Integer pct) { i.wearPercentage = pct; return this; }
        public InspectionBuilder expertNotes(String notes) { i.expertNotes = notes; return this; }
        public InspectionBuilder passed(Boolean passed) { i.passed = passed; return this; }
        public InspectionBuilder reportFileUrl(String url) { i.reportFileUrl = url; return this; }
        public InspectionBuilder validUntil(LocalDateTime d) { i.validUntil = d; return this; }
        public InspectionBuilder createdAt(LocalDateTime d) { i.createdAt = d; return this; }
        public Inspection build() { return i; }
    }
}
