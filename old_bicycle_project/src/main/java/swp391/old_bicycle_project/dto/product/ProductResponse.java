package swp391.old_bicycle_project.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import swp391.old_bicycle_project.entity.enums.ConditionType;
import swp391.old_bicycle_project.entity.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProductResponse {

    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private ConditionType condition;
    private ProductStatus status;
    private String province;
    private String district;
    private String frameSize;
    private String wheelSize;
    private UUID groupsetId;
    private String groupset;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private SellerInfo seller;
    private String brandName;
    private UUID categoryId;
    private String categoryName;
    private String brakeTypeName;
    private String frameMaterialName;
    private List<ImageInfo> images;
    private boolean isVerified;
    private boolean lockedForTransaction;
    private boolean sellerActionLocked;
    private InspectionInfo inspection;

    public ProductResponse() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public ConditionType getCondition() { return condition; }
    public void setCondition(ConditionType condition) { this.condition = condition; }
    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getFrameSize() { return frameSize; }
    public void setFrameSize(String frameSize) { this.frameSize = frameSize; }
    public String getWheelSize() { return wheelSize; }
    public void setWheelSize(String wheelSize) { this.wheelSize = wheelSize; }
    public UUID getGroupsetId() { return groupsetId; }
    public void setGroupsetId(UUID groupsetId) { this.groupsetId = groupsetId; }
    public String getGroupset() { return groupset; }
    public void setGroupset(String groupset) { this.groupset = groupset; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public SellerInfo getSeller() { return seller; }
    public void setSeller(SellerInfo seller) { this.seller = seller; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getBrakeTypeName() { return brakeTypeName; }
    public void setBrakeTypeName(String brakeTypeName) { this.brakeTypeName = brakeTypeName; }
    public String getFrameMaterialName() { return frameMaterialName; }
    public void setFrameMaterialName(String frameMaterialName) { this.frameMaterialName = frameMaterialName; }
    public List<ImageInfo> getImages() { return images; }
    public void setImages(List<ImageInfo> images) { this.images = images; }
    
    @JsonProperty("isVerified")
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
    
    public boolean isLockedForTransaction() { return lockedForTransaction; }
    public void setLockedForTransaction(boolean lockedForTransaction) { this.lockedForTransaction = lockedForTransaction; }
    public boolean isSellerActionLocked() { return sellerActionLocked; }
    public void setSellerActionLocked(boolean sellerActionLocked) { this.sellerActionLocked = sellerActionLocked; }
    public InspectionInfo getInspection() { return inspection; }
    public void setInspection(InspectionInfo inspection) { this.inspection = inspection; }

    public static ProductResponseBuilder builder() {
        return new ProductResponseBuilder();
    }

    public static class ProductResponseBuilder {
        private ProductResponse pr = new ProductResponse();
        public ProductResponseBuilder id(UUID id) { pr.id = id; return this; }
        public ProductResponseBuilder title(String title) { pr.title = title; return this; }
        public ProductResponseBuilder description(String description) { pr.description = description; return this; }
        public ProductResponseBuilder price(BigDecimal price) { pr.price = price; return this; }
        public ProductResponseBuilder originalPrice(BigDecimal originalPrice) { pr.originalPrice = originalPrice; return this; }
        public ProductResponseBuilder condition(ConditionType condition) { pr.condition = condition; return this; }
        public ProductResponseBuilder status(ProductStatus status) { pr.status = status; return this; }
        public ProductResponseBuilder province(String province) { pr.province = province; return this; }
        public ProductResponseBuilder district(String district) { pr.district = district; return this; }
        public ProductResponseBuilder frameSize(String frameSize) { pr.frameSize = frameSize; return this; }
        public ProductResponseBuilder wheelSize(String wheelSize) { pr.wheelSize = wheelSize; return this; }
        public ProductResponseBuilder groupsetId(UUID groupsetId) { pr.groupsetId = groupsetId; return this; }
        public ProductResponseBuilder groupset(String groupset) { pr.groupset = groupset; return this; }
        public ProductResponseBuilder createdAt(LocalDateTime createdAt) { pr.createdAt = createdAt; return this; }
        public ProductResponseBuilder expiresAt(LocalDateTime expiresAt) { pr.expiresAt = expiresAt; return this; }
        public ProductResponseBuilder seller(SellerInfo seller) { pr.seller = seller; return this; }
        public ProductResponseBuilder brandName(String brandName) { pr.brandName = brandName; return this; }
        public ProductResponseBuilder categoryId(UUID categoryId) { pr.categoryId = categoryId; return this; }
        public ProductResponseBuilder categoryName(String categoryName) { pr.categoryName = categoryName; return this; }
        public ProductResponseBuilder brakeTypeName(String brakeTypeName) { pr.brakeTypeName = brakeTypeName; return this; }
        public ProductResponseBuilder frameMaterialName(String frameMaterialName) { pr.frameMaterialName = frameMaterialName; return this; }
        public ProductResponseBuilder images(List<ImageInfo> images) { pr.images = images; return this; }
        public ProductResponseBuilder isVerified(boolean isVerified) { pr.isVerified = isVerified; return this; }
        public ProductResponseBuilder lockedForTransaction(boolean locked) { pr.lockedForTransaction = locked; return this; }
        public ProductResponseBuilder sellerActionLocked(boolean locked) { pr.sellerActionLocked = locked; return this; }
        public ProductResponseBuilder inspection(InspectionInfo inspection) { pr.inspection = inspection; return this; }
        public ProductResponse build() { return pr; }
    }

    public static class SellerInfo {
        private UUID id;
        private String firstName;
        private String lastName;
        private String avatarUrl;
        private String phone;
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public static SellerInfoBuilder builder() { return new SellerInfoBuilder(); }
        public static class SellerInfoBuilder {
            private SellerInfo si = new SellerInfo();
            public SellerInfoBuilder id(UUID id) { si.id = id; return this; }
            public SellerInfoBuilder firstName(String firstName) { si.firstName = firstName; return this; }
            public SellerInfoBuilder lastName(String lastName) { si.lastName = lastName; return this; }
            public SellerInfoBuilder avatarUrl(String avatarUrl) { si.avatarUrl = avatarUrl; return this; }
            public SellerInfoBuilder phone(String phone) { si.phone = phone; return this; }
            public SellerInfo build() { return si; }
        }
    }

    public static class ImageInfo {
        private UUID id;
        private String url;
        private boolean isPrimary;
        private int displayOrder;
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        @JsonProperty("isPrimary")
        public boolean isPrimary() { return isPrimary; }
        public void setPrimary(boolean primary) { isPrimary = primary; }
        public int getDisplayOrder() { return displayOrder; }
        public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
        public static ImageInfoBuilder builder() { return new ImageInfoBuilder(); }
        public static class ImageInfoBuilder {
            private ImageInfo ii = new ImageInfo();
            public ImageInfoBuilder id(UUID id) { ii.id = id; return this; }
            public ImageInfoBuilder url(String url) { ii.url = url; return this; }
            public ImageInfoBuilder isPrimary(boolean isPrimary) { ii.isPrimary = isPrimary; return this; }
            public ImageInfoBuilder displayOrder(int displayOrder) { ii.displayOrder = displayOrder; return this; }
            public ImageInfo build() { return ii; }
        }
    }

    public static class InspectionInfo {
        private UUID id;
        private BigDecimal overallScore;
        private Boolean passed;
        private String reportFileUrl;
        private LocalDateTime validUntil;
        private LocalDateTime createdAt;
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public BigDecimal getOverallScore() { return overallScore; }
        public void setOverallScore(BigDecimal overallScore) { this.overallScore = overallScore; }
        public Boolean getPassed() { return passed; }
        public void setPassed(Boolean passed) { this.passed = passed; }
        public String getReportFileUrl() { return reportFileUrl; }
        public void setReportFileUrl(String reportFileUrl) { this.reportFileUrl = reportFileUrl; }
        public LocalDateTime getValidUntil() { return validUntil; }
        public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public static InspectionInfoBuilder builder() { return new InspectionInfoBuilder(); }
        public static class InspectionInfoBuilder {
            private InspectionInfo ii = new InspectionInfo();
            public InspectionInfoBuilder id(UUID id) { ii.id = id; return this; }
            public InspectionInfoBuilder overallScore(BigDecimal overallScore) { ii.overallScore = overallScore; return this; }
            public InspectionInfoBuilder passed(Boolean passed) { ii.passed = passed; return this; }
            public InspectionInfoBuilder reportFileUrl(String reportFileUrl) { ii.reportFileUrl = reportFileUrl; return this; }
            public InspectionInfoBuilder validUntil(LocalDateTime validUntil) { ii.validUntil = validUntil; return this; }
            public InspectionInfoBuilder createdAt(LocalDateTime createdAt) { ii.createdAt = createdAt; return this; }
            public InspectionInfo build() { return ii; }
        }
    }
}
