package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, columnDefinition = "text")
    private String url;

    @Column(name = "is_primary")
    @Builder.Default
    private boolean isPrimary = false;

    @Column(name = "display_order")
    @Builder.Default
    private int displayOrder = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Manual Getters
    public UUID getId() { return id; }
    public Product getProduct() { return product; }
    public String getUrl() { return url; }
    public boolean isPrimary() { return isPrimary; }
    public int getDisplayOrder() { return displayOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setProduct(Product product) { this.product = product; }
    public void setUrl(String url) { this.url = url; }
    public void setPrimary(boolean primary) { isPrimary = primary; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public static ProductImageBuilder builder() {
        return new ProductImageBuilder();
    }

    public static class ProductImageBuilder {
        private ProductImage pi = new ProductImage();
        public ProductImageBuilder id(UUID id) { pi.id = id; return this; }
        public ProductImageBuilder product(Product product) { pi.product = product; return this; }
        public ProductImageBuilder url(String url) { pi.url = url; return this; }
        public ProductImageBuilder isPrimary(boolean isPrimary) { pi.isPrimary = isPrimary; return this; }
        public ProductImageBuilder displayOrder(int displayOrder) { pi.displayOrder = displayOrder; return this; }
        public ProductImage build() { return pi; }
    }
}
