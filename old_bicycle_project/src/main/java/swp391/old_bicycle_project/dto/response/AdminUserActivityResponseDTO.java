package swp391.old_bicycle_project.dto.response;

import lombok.Builder;
import lombok.Data;
import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.entity.enums.ProductStatus;
import swp391.old_bicycle_project.entity.enums.UserStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AdminUserActivityResponseDTO {
    private UUID userId;
    private String email;
    private AppRole role;
    private UserStatus status;
    private boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long totalProducts;
    private long totalOrdersAsBuyer;
    private long totalOrdersAsSeller;
    private long totalReportsSubmitted;
    private long totalWishlistItems;
    private long totalConversations;
    private long unreadNotifications;
    private List<RecentProductItem> recentProducts;
    private List<RecentOrderItem> recentOrders;
    private List<RecentReportItem> recentReports;
    private List<RecentNotificationItem> recentNotifications;
    private List<RecentWishlistItem> recentWishlistItems;

    @Data
    @Builder
    public static class RecentProductItem {
        private UUID productId;
        private String title;
        private BigDecimal price;
        private ProductStatus status;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    public static class RecentOrderItem {
        private UUID orderId;
        private UUID productId;
        private String productTitle;
        private OrderStatus status;
        private PaymentMethod paymentMethod;
        private BigDecimal totalAmount;
        private String involvement;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    public static class RecentReportItem {
        private UUID reportId;
        private UUID targetId;
        private String targetType;
        private String reason;
        private String status;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    public static class RecentNotificationItem {
        private UUID notificationId;
        private String title;
        private String type;
        private Boolean isRead;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    public static class RecentWishlistItem {
        private UUID productId;
        private String productTitle;
        private ProductStatus productStatus;
        private LocalDateTime createdAt;
    }
}
