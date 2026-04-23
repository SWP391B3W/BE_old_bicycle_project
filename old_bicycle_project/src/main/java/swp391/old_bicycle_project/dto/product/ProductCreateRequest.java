package swp391.old_bicycle_project.dto.product;

import swp391.old_bicycle_project.entity.enums.ConditionType;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductCreateRequest {

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    private String description;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    @JsonAlias("price_amount")
    private BigDecimal price;

    @JsonAlias("original_price")
    private BigDecimal originalPrice;

    @NotNull(message = "Vui lòng chọn loại phanh")
    @JsonAlias("brake_type_id")
    private UUID brakeTypeId;

    @NotNull(message = "Vui lòng chọn chất liệu khung")
    @JsonAlias("frame_material_id")
    private UUID frameMaterialId;

    @JsonAlias("brand_id")
    private UUID brandId;
    @JsonAlias("category_id")
    private UUID categoryId;

    @NotBlank(message = "Vui lòng nhập kích thước khung")
    @JsonAlias("frame_size")
    private String frameSize;

    @NotBlank(message = "Vui lòng nhập kích thước bánh")
    @JsonAlias("wheel_size")
    private String wheelSize;

    @JsonAlias("groupset_id")
    private UUID groupsetId;

    private String groupset;

    private ConditionType condition;

    @NotBlank(message = "Vui lòng chọn tỉnh/thành")
    private String province;

    private String district;

    // Manual Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public UUID getBrakeTypeId() { return brakeTypeId; }
    public UUID getFrameMaterialId() { return frameMaterialId; }
    public UUID getBrandId() { return brandId; }
    public UUID getCategoryId() { return categoryId; }
    public String getFrameSize() { return frameSize; }
    public String getWheelSize() { return wheelSize; }
    public UUID getGroupsetId() { return groupsetId; }
    public String getGroupset() { return groupset; }
    public ConditionType getCondition() { return condition; }
    public String getProvince() { return province; }
    public String getDistrict() { return district; }

    // Manual Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public void setBrakeTypeId(UUID brakeTypeId) { this.brakeTypeId = brakeTypeId; }
    public void setFrameMaterialId(UUID frameMaterialId) { this.frameMaterialId = frameMaterialId; }
    public void setBrandId(UUID brandId) { this.brandId = brandId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }
    public void setFrameSize(String frameSize) { this.frameSize = frameSize; }
    public void setWheelSize(String wheelSize) { this.wheelSize = wheelSize; }
    public void setGroupsetId(UUID groupsetId) { this.groupsetId = groupsetId; }
    public void setGroupset(String groupset) { this.groupset = groupset; }
    public void setCondition(ConditionType condition) { this.condition = condition; }
    public void setProvince(String province) { this.province = province; }
    public void setDistrict(String district) { this.district = district; }
}
