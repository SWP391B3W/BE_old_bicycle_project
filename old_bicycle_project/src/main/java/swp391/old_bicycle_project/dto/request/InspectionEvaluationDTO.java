package swp391.old_bicycle_project.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionEvaluationDTO {

    @NotNull(message = "Frame score is required")
    @Min(value = 1, message = "Score must be between 1 and 5")
    @Max(value = 5, message = "Score must be between 1 and 5")
    private Integer frameScore;

    @NotNull(message = "Fork score is required")
    @Min(value = 1, message = "Score must be between 1 and 5")
    @Max(value = 5, message = "Score must be between 1 and 5")
    private Integer forkScore;

    @NotNull(message = "Brakes score is required")
    @Min(value = 1, message = "Score must be between 1 and 5")
    @Max(value = 5, message = "Score must be between 1 and 5")
    private Integer brakesScore;

    @NotNull(message = "Drivetrain score is required")
    @Min(value = 1, message = "Score must be between 1 and 5")
    @Max(value = 5, message = "Score must be between 1 and 5")
    private Integer drivetrainScore;

    @NotNull(message = "Wheels score is required")
    @Min(value = 1, message = "Score must be between 1 and 5")
    @Max(value = 5, message = "Score must be between 1 and 5")
    private Integer wheelsScore;

    @NotNull(message = "Wear percentage is required")
    @Min(value = 0, message = "Wear percentage must be between 0 and 100")
    @Max(value = 100, message = "Wear percentage must be between 0 and 100")
    private Integer wearPercentage;

    private String expertNotes;

    @NotNull(message = "Passed status is required")
    private Boolean passed;

    // Manual Getters
    public Integer getFrameScore() { return frameScore; }
    public Integer getForkScore() { return forkScore; }
    public Integer getBrakesScore() { return brakesScore; }
    public Integer getDrivetrainScore() { return drivetrainScore; }
    public Integer getWheelsScore() { return wheelsScore; }
    public Integer getWearPercentage() { return wearPercentage; }
    public String getExpertNotes() { return expertNotes; }
    public Boolean getPassed() { return passed; }

    // Manual Builder
    public static InspectionEvaluationDTOBuilder builder() { return new InspectionEvaluationDTOBuilder(); }
    public static class InspectionEvaluationDTOBuilder {
        private InspectionEvaluationDTO r = new InspectionEvaluationDTO();
        public InspectionEvaluationDTOBuilder frameScore(Integer frameScore) { r.frameScore = frameScore; return this; }
        public InspectionEvaluationDTOBuilder forkScore(Integer forkScore) { r.forkScore = forkScore; return this; }
        public InspectionEvaluationDTOBuilder brakesScore(Integer brakesScore) { r.brakesScore = brakesScore; return this; }
        public InspectionEvaluationDTOBuilder drivetrainScore(Integer drivetrainScore) { r.drivetrainScore = drivetrainScore; return this; }
        public InspectionEvaluationDTOBuilder wheelsScore(Integer wheelsScore) { r.wheelsScore = wheelsScore; return this; }
        public InspectionEvaluationDTOBuilder wearPercentage(Integer wearPercentage) { r.wearPercentage = wearPercentage; return this; }
        public InspectionEvaluationDTOBuilder expertNotes(String expertNotes) { r.expertNotes = expertNotes; return this; }
        public InspectionEvaluationDTOBuilder passed(Boolean passed) { r.passed = passed; return this; }
        public InspectionEvaluationDTO build() { return r; }
    }
}
