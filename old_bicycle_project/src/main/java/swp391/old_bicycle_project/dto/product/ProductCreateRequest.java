package swp391.old_bicycle_project.dto.product;

import swp391.old_bicycle_project.entity.enums.ConditionType;
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
    private BigDecimal price;

    private BigDecimal originalPrice;

    @NotNull(message = "Vui lòng chọn loại phanh")
    private UUID brakeTypeId;

    @NotNull(message = "Vui lòng chọn chất liệu khung")
    private UUID frameMaterialId;

    private UUID brandId;
    private UUID categoryId;

    @NotBlank(message = "Vui lòng nhập kích thước khung")
    private String frameSize;

    @NotBlank(message = "Vui lòng nhập kích thước bánh")
    private String wheelSize;

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
}
