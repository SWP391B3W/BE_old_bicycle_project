package swp391.old_bicycle_project.entity;

import swp391.old_bicycle_project.entity.enums.ConditionType;
import swp391.old_bicycle_project.entity.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brake_type_id")
    private BrakeType brakeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frame_material_id")
    private FrameMaterial frameMaterial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupset_id")
    private Groupset groupsetReference;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "original_price", precision = 15, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "frame_size")
    private String frameSize;

    @Column(name = "wheel_size")
    private String wheelSize;

    private String groupset;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(columnDefinition = "condition_type")
    @Builder.Default
    private ConditionType condition = ConditionType.used;

    private String province;
    private String district;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(columnDefinition = "product_status")
    @Builder.Default
    private ProductStatus status = ProductStatus.pending;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Manual Getters to bypass Lombok issues
    public UUID getId() { return id; }
    public User getSeller() { return seller; }
    public Brand getBrand() { return brand; }
    public Category getCategory() { return category; }
    public BrakeType getBrakeType() { return brakeType; }
    public FrameMaterial getFrameMaterial() { return frameMaterial; }
    public Groupset getGroupsetReference() { return groupsetReference; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public String getFrameSize() { return frameSize; }
    public String getWheelSize() { return wheelSize; }
    public String getGroupset() { return groupset; }
    public ConditionType getCondition() { return condition; }
    public String getProvince() { return province; }
    public String getDistrict() { return district; }
    public ProductStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public List<ProductImage> getImages() { return images; }
    
    public void setId(UUID id) { this.id = id; }
    public void setSeller(User seller) { this.seller = seller; }
    public void setBrand(Brand brand) { this.brand = brand; }
    public void setCategory(Category category) { this.category = category; }
    public void setBrakeType(BrakeType brakeType) { this.brakeType = brakeType; }
    public void setFrameMaterial(FrameMaterial frameMaterial) { this.frameMaterial = frameMaterial; }
    public void setGroupsetReference(Groupset groupsetReference) { this.groupsetReference = groupsetReference; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public void setFrameSize(String frameSize) { this.frameSize = frameSize; }
    public void setWheelSize(String wheelSize) { this.wheelSize = wheelSize; }
    public void setGroupset(String groupset) { this.groupset = groupset; }
    public void setCondition(ConditionType condition) { this.condition = condition; }
    public void setProvince(String province) { this.province = province; }
    public void setDistrict(String district) { this.district = district; }
    public void setStatus(ProductStatus status) { this.status = status; }
    public void setExpiresAt(LocalDateTime d) { this.expiresAt = d; }
    public void setDeletedAt(LocalDateTime d) { this.deletedAt = d; }
    public void setImages(List<ProductImage> images) { this.images = images; }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private Product p = new Product();
        public ProductBuilder id(UUID id) { p.id = id; return this; }
        public ProductBuilder seller(User seller) { p.seller = seller; return this; }
        public ProductBuilder brand(Brand brand) { p.brand = brand; return this; }
        public ProductBuilder category(Category category) { p.category = category; return this; }
        public ProductBuilder brakeType(BrakeType brakeType) { p.brakeType = brakeType; return this; }
        public ProductBuilder frameMaterial(FrameMaterial frameMaterial) { p.frameMaterial = frameMaterial; return this; }
        public ProductBuilder groupsetReference(Groupset groupsetReference) { p.groupsetReference = groupsetReference; return this; }
        public ProductBuilder title(String title) { p.title = title; return this; }
        public ProductBuilder description(String description) { p.description = description; return this; }
        public ProductBuilder price(BigDecimal price) { p.price = price; return this; }
        public ProductBuilder originalPrice(BigDecimal originalPrice) { p.originalPrice = originalPrice; return this; }
        public ProductBuilder frameSize(String frameSize) { p.frameSize = frameSize; return this; }
        public ProductBuilder wheelSize(String wheelSize) { p.wheelSize = wheelSize; return this; }
        public ProductBuilder groupset(String groupset) { p.groupset = groupset; return this; }
        public ProductBuilder condition(ConditionType condition) { p.condition = condition; return this; }
        public ProductBuilder province(String province) { p.province = province; return this; }
        public ProductBuilder district(String district) { p.district = district; return this; }
        public ProductBuilder status(ProductStatus status) { p.status = status; return this; }
        public ProductBuilder images(List<ProductImage> images) { p.images = images; return this; }
        public Product build() { return p; }
    }
}
