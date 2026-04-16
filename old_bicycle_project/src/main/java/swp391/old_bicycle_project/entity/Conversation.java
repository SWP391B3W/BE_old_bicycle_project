package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "conversations",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_conversations_product_buyer_seller",
                columnNames = {"product_id", "buyer_id", "seller_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Manual Getter
    public UUID getId() { return id; }
    public Product getProduct() { return product; }
    public User getBuyer() { return buyer; }
    public User getSeller() { return seller; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Manual Setter
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Manual Builder
    public static ConversationBuilder builder() { return new ConversationBuilder(); }
    public static class ConversationBuilder {
        private Conversation r = new Conversation();
        public ConversationBuilder id(UUID id) { r.id = id; return this; }
        public ConversationBuilder product(Product product) { r.product = product; return this; }
        public ConversationBuilder buyer(User buyer) { r.buyer = buyer; return this; }
        public ConversationBuilder seller(User seller) { r.seller = seller; return this; }
        public Conversation build() { return r; }
    }
}
