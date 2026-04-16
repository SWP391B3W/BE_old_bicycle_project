package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wishlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(Wishlist.WishlistId.class)
public class Wishlist {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Manual Getter
    public User getUser() { return user; }
    public Product getProduct() { return product; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Setter
    public void setUser(User user) { this.user = user; }
    public void setProduct(Product product) { this.product = product; }

    // Manual Builder
    public static WishlistBuilder builder() { return new WishlistBuilder(); }
    public static class WishlistBuilder {
        private Wishlist r = new Wishlist();
        public WishlistBuilder user(User user) { r.user = user; return this; }
        public WishlistBuilder product(Product product) { r.product = product; return this; }
        public Wishlist build() { return r; }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishlistId implements Serializable {
        private UUID user;
        private UUID product;
    }
}
