package swp391.old_bicycle_project.dto.product;

import swp391.old_bicycle_project.entity.enums.ConditionType;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductUpdateRequest {
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    private String title;

    @Size(max = 5000, message = "Mô tả không được vượt quá 5000 ký tự")
    private String description;

    @Positive(message = "Giá phải lớn hơn 0")
    @JsonAlias("price_amount")
    private BigDecimal price;

    @Positive(message = "Giá gốc phải lớn hơn 0")
    @JsonAlias("original_price")
    private BigDecimal originalPrice;
    @JsonAlias("brake_type_id")
    private UUID brakeTypeId;
    @JsonAlias("frame_material_id")
    private UUID frameMaterialId;
    @JsonAlias("brand_id")
    private UUID brandId;
    @JsonAlias("category_id")
    private UUID categoryId;

    @Size(max = 50, message = "Kích thước khung không được vượt quá 50 ký tự")
    @JsonAlias("frame_size")
    private String frameSize;

    @Size(max = 50, message = "Kích thước bánh không được vượt quá 50 ký tự")
    @JsonAlias("wheel_size")
    private String wheelSize;
    @JsonAlias("groupset_id")
    private UUID groupsetId;

    @Size(max = 100, message = "Groupset không được vượt quá 100 ký tự")
    private String groupset;
    private ConditionType condition;

    @Size(max = 100, message = "Tỉnh/thành không được vượt quá 100 ký tự")
    private String province;

    @Size(max = 150, message = "Quận/huyện không được vượt quá 150 ký tự")
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
