package swp391.old_bicycle_project.dto.response;

import swp391.old_bicycle_project.entity.enums.AppRole;
import swp391.old_bicycle_project.entity.enums.NotificationType;
import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.entity.enums.ProductStatus;
import swp391.old_bicycle_project.entity.enums.ReportReason;
import swp391.old_bicycle_project.entity.enums.ReportStatus;
import swp391.old_bicycle_project.entity.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

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

    public AdminUserActivityResponseDTO() {}

    // Manual Builder
    public static AdminUserActivityResponseDTOBuilder builder() { return new AdminUserActivityResponseDTOBuilder(); }
    public static class AdminUserActivityResponseDTOBuilder {
        private AdminUserActivityResponseDTO r = new AdminUserActivityResponseDTO();
        public AdminUserActivityResponseDTOBuilder userId(UUID userId) { r.userId = userId; return this; }
        public AdminUserActivityResponseDTOBuilder email(String email) { r.email = email; return this; }
        public AdminUserActivityResponseDTOBuilder role(AppRole role) { r.role = role; return this; }
        public AdminUserActivityResponseDTOBuilder status(UserStatus status) { r.status = status; return this; }
        public AdminUserActivityResponseDTOBuilder verified(boolean verified) { r.verified = verified; return this; }
        public AdminUserActivityResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public AdminUserActivityResponseDTOBuilder updatedAt(LocalDateTime updatedAt) { r.updatedAt = updatedAt; return this; }
        public AdminUserActivityResponseDTOBuilder totalProducts(long totalProducts) { r.totalProducts = totalProducts; return this; }
        public AdminUserActivityResponseDTOBuilder totalOrdersAsBuyer(long totalOrdersAsBuyer) { r.totalOrdersAsBuyer = totalOrdersAsBuyer; return this; }
        public AdminUserActivityResponseDTOBuilder totalOrdersAsSeller(long totalOrdersAsSeller) { r.totalOrdersAsSeller = totalOrdersAsSeller; return this; }
        public AdminUserActivityResponseDTOBuilder totalReportsSubmitted(long totalReportsSubmitted) { r.totalReportsSubmitted = totalReportsSubmitted; return this; }
        public AdminUserActivityResponseDTOBuilder totalWishlistItems(long totalWishlistItems) { r.totalWishlistItems = totalWishlistItems; return this; }
        public AdminUserActivityResponseDTOBuilder totalConversations(long totalConversations) { r.totalConversations = totalConversations; return this; }
        public AdminUserActivityResponseDTOBuilder unreadNotifications(long unreadNotifications) { r.unreadNotifications = unreadNotifications; return this; }
        public AdminUserActivityResponseDTOBuilder recentProducts(List<RecentProductItem> recentProducts) { r.recentProducts = recentProducts; return this; }
        public AdminUserActivityResponseDTOBuilder recentOrders(List<RecentOrderItem> recentOrders) { r.recentOrders = recentOrders; return this; }
        public AdminUserActivityResponseDTOBuilder recentReports(List<RecentReportItem> recentReports) { r.recentReports = recentReports; return this; }
        public AdminUserActivityResponseDTOBuilder recentNotifications(List<RecentNotificationItem> recentNotifications) { r.recentNotifications = recentNotifications; return this; }
        public AdminUserActivityResponseDTOBuilder recentWishlistItems(List<RecentWishlistItem> recentWishlistItems) { r.recentWishlistItems = recentWishlistItems; return this; }
        public AdminUserActivityResponseDTO build() { return r; }
    }

    @Data
    @Builder
    public static class RecentProductItem {
        private UUID productId;
        private String title;
        private BigDecimal price;
        private ProductStatus status;
        private LocalDateTime createdAt;

        public RecentProductItem() {}

        public static RecentProductItemBuilder builder() { return new RecentProductItemBuilder(); }
        public static class RecentProductItemBuilder {
            private RecentProductItem r = new RecentProductItem();
            public RecentProductItemBuilder productId(UUID productId) { r.productId = productId; return this; }
            public RecentProductItemBuilder title(String title) { r.title = title; return this; }
            public RecentProductItemBuilder price(BigDecimal price) { r.price = price; return this; }
            public RecentProductItemBuilder status(ProductStatus status) { r.status = status; return this; }
            public RecentProductItemBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
            public RecentProductItem build() { return r; }
        }
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

        public RecentOrderItem() {}

        public static RecentOrderItemBuilder builder() { return new RecentOrderItemBuilder(); }
        public static class RecentOrderItemBuilder {
            private RecentOrderItem r = new RecentOrderItem();
            public RecentOrderItemBuilder orderId(UUID orderId) { r.orderId = orderId; return this; }
            public RecentOrderItemBuilder productId(UUID productId) { r.productId = productId; return this; }
            public RecentOrderItemBuilder productTitle(String productTitle) { r.productTitle = productTitle; return this; }
            public RecentOrderItemBuilder status(OrderStatus status) { r.status = status; return this; }
            public RecentOrderItemBuilder paymentMethod(PaymentMethod paymentMethod) { r.paymentMethod = paymentMethod; return this; }
            public RecentOrderItemBuilder totalAmount(BigDecimal totalAmount) { r.totalAmount = totalAmount; return this; }
            public RecentOrderItemBuilder involvement(String involvement) { r.involvement = involvement; return this; }
            public RecentOrderItemBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
            public RecentOrderItem build() { return r; }
        }
    }

    @Data
    @Builder
    public static class RecentReportItem {
        private UUID reportId;
        private UUID targetId;
        private String targetType;
        private ReportReason reason;
        private ReportStatus status;
        private LocalDateTime createdAt;

        public RecentReportItem() {}

        public static RecentReportItemBuilder builder() { return new RecentReportItemBuilder(); }
        public static class RecentReportItemBuilder {
            private RecentReportItem r = new RecentReportItem();
            public RecentReportItemBuilder reportId(UUID reportId) { r.reportId = reportId; return this; }
            public RecentReportItemBuilder targetId(UUID targetId) { r.targetId = targetId; return this; }
            public RecentReportItemBuilder targetType(String targetType) { r.targetType = targetType; return this; }
            public RecentReportItemBuilder reason(ReportReason reason) { r.reason = reason; return this; }
            public RecentReportItemBuilder status(ReportStatus status) { r.status = status; return this; }
            public RecentReportItemBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
            public RecentReportItem build() { return r; }
        }
    }

    @Data
    @Builder
    public static class RecentNotificationItem {
        private UUID notificationId;
        private String title;
        private NotificationType type;
        private Boolean isRead;
        private LocalDateTime createdAt;

        public RecentNotificationItem() {}

        public static RecentNotificationItemBuilder builder() { return new RecentNotificationItemBuilder(); }
        public static class RecentNotificationItemBuilder {
            private RecentNotificationItem r = new RecentNotificationItem();
            public RecentNotificationItemBuilder notificationId(UUID notificationId) { r.notificationId = notificationId; return this; }
            public RecentNotificationItemBuilder title(String title) { r.title = title; return this; }
            public RecentNotificationItemBuilder type(NotificationType type) { r.type = type; return this; }
            public RecentNotificationItemBuilder isRead(Boolean isRead) { r.isRead = isRead; return this; }
            public RecentNotificationItemBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
            public RecentNotificationItem build() { return r; }
        }
    }

    @Data
    @Builder
    public static class RecentWishlistItem {
        private UUID productId;
        private String productTitle;
        private ProductStatus productStatus;
        private LocalDateTime createdAt;

        public RecentWishlistItem() {}

        public static RecentWishlistItemBuilder builder() { return new RecentWishlistItemBuilder(); }
        public static class RecentWishlistItemBuilder {
            private RecentWishlistItem r = new RecentWishlistItem();
            public RecentWishlistItemBuilder productId(UUID productId) { r.productId = productId; return this; }
            public RecentWishlistItemBuilder productTitle(String productTitle) { r.productTitle = productTitle; return this; }
            public RecentWishlistItemBuilder productStatus(ProductStatus productStatus) { r.productStatus = productStatus; return this; }
            public RecentWishlistItemBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
            public RecentWishlistItem build() { return r; }
        }
    }
}
