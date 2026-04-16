package swp391.old_bicycle_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "seller_reply", columnDefinition = "TEXT")
    private String sellerReply;

    @Column(name = "seller_replied_at")
    private LocalDateTime sellerRepliedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Manual Getter
    public UUID getId() { return id; }
    public Order getOrder() { return order; }
    public User getReviewer() { return reviewer; }
    public User getReviewee() { return reviewee; }
    public Integer getRating() { return rating; }
    public String getComment() { return comment; }
    public String getSellerReply() { return sellerReply; }
    public LocalDateTime getSellerRepliedAt() { return sellerRepliedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Setter
    public void setOrder(Order order) { this.order = order; }
    public void setReviewer(User user) { this.reviewer = user; }
    public void setReviewee(User user) { this.reviewee = user; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
    public void setSellerReply(String reply) { this.sellerReply = reply; }
    public void setSellerRepliedAt(LocalDateTime d) { this.sellerRepliedAt = d; }

    // Manual Builder
    public static ReviewBuilder builder() { return new ReviewBuilder(); }
    public static class ReviewBuilder {
        private Review r = new Review();
        public ReviewBuilder id(UUID id) { r.id = id; return this; }
        public ReviewBuilder order(Order order) { r.order = order; return this; }
        public ReviewBuilder reviewer(User user) { r.reviewer = user; return this; }
        public ReviewBuilder reviewee(User user) { r.reviewee = user; return this; }
        public ReviewBuilder rating(Integer rating) { r.rating = rating; return this; }
        public ReviewBuilder comment(String comment) { r.comment = comment; return this; }
        public Review build() { return r; }
    }
}
