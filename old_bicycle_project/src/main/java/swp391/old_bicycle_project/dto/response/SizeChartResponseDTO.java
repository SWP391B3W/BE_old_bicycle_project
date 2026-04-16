package swp391.old_bicycle_project.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class SizeChartResponseDTO {

    private UUID id;
    private UUID categoryId;
    private String categoryName;
    private String name;
    private String description;
    private List<SizeChartRowResponseDTO> rows;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SizeChartResponseDTO() {}

    public SizeChartResponseDTO(UUID id, UUID categoryId, String categoryName, String name, String description, List<SizeChartRowResponseDTO> rows, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.name = name;
        this.description = description;
        this.rows = rows;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Manual Getter
    public UUID getId() { return id; }
    public UUID getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<SizeChartRowResponseDTO> getRows() { return rows; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static SizeChartResponseDTOBuilder builder() {
        return new SizeChartResponseDTOBuilder();
    }

    public static class SizeChartResponseDTOBuilder {
        private SizeChartResponseDTO r = new SizeChartResponseDTO();
        public SizeChartResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public SizeChartResponseDTOBuilder categoryId(UUID categoryId) { r.categoryId = categoryId; return this; }
        public SizeChartResponseDTOBuilder categoryName(String categoryName) { r.categoryName = categoryName; return this; }
        public SizeChartResponseDTOBuilder name(String name) { r.name = name; return this; }
        public SizeChartResponseDTOBuilder description(String description) { r.description = description; return this; }
        public SizeChartResponseDTOBuilder rows(List<SizeChartRowResponseDTO> rows) { r.rows = rows; return this; }
        public SizeChartResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public SizeChartResponseDTOBuilder updatedAt(LocalDateTime updatedAt) { r.updatedAt = updatedAt; return this; }
        public SizeChartResponseDTO build() { return r; }
    }
}
