package swp391.old_bicycle_project.dto.product;

import swp391.old_bicycle_project.entity.enums.ConditionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(description = "Bộ lọc query cho API lấy danh sách xe. Mọi field đều là tùy chọn.")
public class ProductFilterRequest {
    @Schema(description = "Từ khóa tìm trong tiêu đề xe")
    private String keyword;

    @Schema(description = "ID thương hiệu")
    private UUID brandId;

    @Schema(description = "ID danh mục")
    private UUID categoryId;

    @Schema(description = "ID loại phanh")
    private UUID brakeTypeId;

    @Schema(description = "ID chất liệu khung")
    private UUID frameMaterialId;

    @Schema(description = "Tình trạng xe")
    private ConditionType condition;

    @Schema(description = "Kích thước khung")
    private String frameSize;

    @Schema(description = "Kích thước bánh")
    private String wheelSize;

    @Schema(description = "Nhóm truyền động")
    private String groupset;

    @Schema(description = "ID groupset")
    private UUID groupsetId;

    @Schema(description = "Giá tối thiểu")
    private BigDecimal minPrice;

    @Schema(description = "Giá tối đa")
    private BigDecimal maxPrice;

    @Schema(description = "Tỉnh hoặc thành phố")
    private String province;

    @Schema(description = "Chỉ lấy xe có inspection hợp lệ")
    private Boolean hasInspection;

    @Schema(description = "Cách sắp xếp")
    private String sortBy;

    // Manual Getters
    public String getKeyword() { return keyword; }
    public UUID getBrandId() { return brandId; }
    public UUID getCategoryId() { return categoryId; }
    public UUID getBrakeTypeId() { return brakeTypeId; }
    public UUID getFrameMaterialId() { return frameMaterialId; }
    public ConditionType getCondition() { return condition; }
    public String getFrameSize() { return frameSize; }
    public String getWheelSize() { return wheelSize; }
    public String getGroupset() { return groupset; }
    public UUID getGroupsetId() { return groupsetId; }
    public BigDecimal getMinPrice() { return minPrice; }
    public BigDecimal getMaxPrice() { return maxPrice; }
    public String getProvince() { return province; }
    public Boolean getHasInspection() { return hasInspection; }
    public String getSortBy() { return sortBy; }

    // Manual Setters
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public void setBrandId(UUID brandId) { this.brandId = brandId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }
    public void setBrakeTypeId(UUID brakeTypeId) { this.brakeTypeId = brakeTypeId; }
    public void setFrameMaterialId(UUID frameMaterialId) { this.frameMaterialId = frameMaterialId; }
    public void setCondition(ConditionType condition) { this.condition = condition; }
    public void setFrameSize(String frameSize) { this.frameSize = frameSize; }
    public void setWheelSize(String wheelSize) { this.wheelSize = wheelSize; }
    public void setGroupset(String groupset) { this.groupset = groupset; }
    public void setGroupsetId(UUID groupsetId) { this.groupsetId = groupsetId; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    public void setProvince(String province) { this.province = province; }
    public void setHasInspection(Boolean hasInspection) { this.hasInspection = hasInspection; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
}
