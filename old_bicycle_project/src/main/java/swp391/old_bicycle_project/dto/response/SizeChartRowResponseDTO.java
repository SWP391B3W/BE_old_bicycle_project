package swp391.old_bicycle_project.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class SizeChartRowResponseDTO {

    private UUID id;
    private String frameSize;
    private Integer heightMinCm;
    private Integer heightMaxCm;
    private String note;
    private Integer displayOrder;

    public SizeChartRowResponseDTO() {}

    public SizeChartRowResponseDTO(UUID id, String frameSize, Integer heightMinCm, Integer heightMaxCm, String note, Integer displayOrder) {
        this.id = id;
        this.frameSize = frameSize;
        this.heightMinCm = heightMinCm;
        this.heightMaxCm = heightMaxCm;
        this.note = note;
        this.displayOrder = displayOrder;
    }

    // Manual Getters
    public UUID getId() { return id; }
    public String getFrameSize() { return frameSize; }
    public Integer getHeightMinCm() { return heightMinCm; }
    public Integer getHeightMaxCm() { return heightMaxCm; }
    public String getNote() { return note; }
    public Integer getDisplayOrder() { return displayOrder; }

    public static SizeChartRowResponseDTOBuilder builder() {
        return new SizeChartRowResponseDTOBuilder();
    }

    public static class SizeChartRowResponseDTOBuilder {
        private SizeChartRowResponseDTO r = new SizeChartRowResponseDTO();
        public SizeChartRowResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public SizeChartRowResponseDTOBuilder frameSize(String frameSize) { r.frameSize = frameSize; return this; }
        public SizeChartRowResponseDTOBuilder heightMinCm(Integer heightMinCm) { r.heightMinCm = heightMinCm; return this; }
        public SizeChartRowResponseDTOBuilder heightMaxCm(Integer heightMaxCm) { r.heightMaxCm = heightMaxCm; return this; }
        public SizeChartRowResponseDTOBuilder note(String note) { r.note = note; return this; }
        public SizeChartRowResponseDTOBuilder displayOrder(Integer displayOrder) { r.displayOrder = displayOrder; return this; }
        public SizeChartRowResponseDTO build() { return r; }
    }
}
