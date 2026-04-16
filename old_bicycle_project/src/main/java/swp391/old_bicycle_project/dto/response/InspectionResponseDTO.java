package swp391.old_bicycle_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionResponseDTO {
    private UUID id;
    private UUID productId;
    private UUID inspectorId;
    private BigDecimal overallScore;
    private Integer frameScore;
    private Integer forkScore;
    private Integer brakesScore;
    private Integer drivetrainScore;
    private Integer wheelsScore;
    private Integer wearPercentage;
    private String expertNotes;
    private Boolean passed;
    private String reportFileUrl;
    private LocalDateTime validUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Manual Getters
    public UUID getId() { return id; }
    public UUID getProductId() { return productId; }
    public UUID getInspectorId() { return inspectorId; }
    public BigDecimal getOverallScore() { return overallScore; }
    public Boolean getPassed() { return passed; }
    public String getReportFileUrl() { return reportFileUrl; }

    public static InspectionResponseDTOBuilder builder() {
        return new InspectionResponseDTOBuilder();
    }

    public static class InspectionResponseDTOBuilder {
        private InspectionResponseDTO k = new InspectionResponseDTO();
        public InspectionResponseDTOBuilder id(UUID id) { k.id = id; return this; }
        public InspectionResponseDTOBuilder productId(UUID productId) { k.productId = productId; return this; }
        public InspectionResponseDTOBuilder inspectorId(UUID inspectorId) { k.inspectorId = inspectorId; return this; }
        public InspectionResponseDTOBuilder overallScore(BigDecimal score) { k.overallScore = score; return this; }
        public InspectionResponseDTOBuilder frameScore(Integer score) { k.frameScore = score; return this; }
        public InspectionResponseDTOBuilder forkScore(Integer score) { k.forkScore = score; return this; }
        public InspectionResponseDTOBuilder brakesScore(Integer score) { k.brakesScore = score; return this; }
        public InspectionResponseDTOBuilder drivetrainScore(Integer score) { k.drivetrainScore = score; return this; }
        public InspectionResponseDTOBuilder wheelsScore(Integer score) { k.wheelsScore = score; return this; }
        public InspectionResponseDTOBuilder wearPercentage(Integer pct) { k.wearPercentage = pct; return this; }
        public InspectionResponseDTOBuilder expertNotes(String notes) { k.expertNotes = notes; return this; }
        public InspectionResponseDTOBuilder passed(Boolean passed) { k.passed = passed; return this; }
        public InspectionResponseDTOBuilder reportFileUrl(String url) { k.reportFileUrl = url; return this; }
        public InspectionResponseDTOBuilder validUntil(LocalDateTime d) { k.validUntil = d; return this; }
        public InspectionResponseDTOBuilder createdAt(LocalDateTime d) { k.createdAt = d; return this; }
        public InspectionResponseDTOBuilder updatedAt(LocalDateTime d) { k.updatedAt = d; return this; }
        public InspectionResponseDTO build() { return k; }
    }
}
