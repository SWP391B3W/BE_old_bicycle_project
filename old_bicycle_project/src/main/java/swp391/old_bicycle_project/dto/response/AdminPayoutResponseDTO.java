package swp391.old_bicycle_project.dto.response;

import swp391.old_bicycle_project.entity.enums.OrderFundingStatus;
import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.PayoutProvider;
import swp391.old_bicycle_project.entity.enums.PayoutStatus;
import swp391.old_bicycle_project.entity.enums.PayoutType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPayoutResponseDTO {
    private UUID id;
    private PayoutType type;
    private PayoutStatus status;
    private PayoutProvider provider;
    private BigDecimal amount;
    private BigDecimal grossAmount;
    private BigDecimal feeDeductionAmount;
    private BigDecimal netAmount;
    private UUID recipientId;
    private String recipientName;
    private String bankCode;
    private String bankBin;
    private String accountNumber;
    private String accountName;
    private String transferContent;
    private String qrCodeUrl;
    private String bankReference;
    private String adminNote;
    private UUID orderId;
    private OrderStatus orderStatus;
    private OrderFundingStatus fundingStatus;
    private UUID refundRequestId;
    private UUID productId;
    private String productTitle;
    private UUID buyerId;
    private String buyerName;
    private UUID sellerId;
    private String sellerName;
    private UUID completedById;
    private String completedByName;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;

    // Manual Builder
    public static AdminPayoutResponseDTOBuilder builder() { return new AdminPayoutResponseDTOBuilder(); }
    public static class AdminPayoutResponseDTOBuilder {
        private AdminPayoutResponseDTO r = new AdminPayoutResponseDTO();
        public AdminPayoutResponseDTOBuilder id(UUID id) { r.id = id; return this; }
        public AdminPayoutResponseDTOBuilder type(PayoutType type) { r.type = type; return this; }
        public AdminPayoutResponseDTOBuilder status(PayoutStatus status) { r.status = status; return this; }
        public AdminPayoutResponseDTOBuilder provider(PayoutProvider provider) { r.provider = provider; return this; }
        public AdminPayoutResponseDTOBuilder amount(BigDecimal amount) { r.amount = amount; return this; }
        public AdminPayoutResponseDTOBuilder grossAmount(BigDecimal grossAmount) { r.grossAmount = grossAmount; return this; }
        public AdminPayoutResponseDTOBuilder feeDeductionAmount(BigDecimal feeDeductionAmount) { r.feeDeductionAmount = feeDeductionAmount; return this; }
        public AdminPayoutResponseDTOBuilder netAmount(BigDecimal netAmount) { r.netAmount = netAmount; return this; }
        public AdminPayoutResponseDTOBuilder recipientId(UUID recipientId) { r.recipientId = recipientId; return this; }
        public AdminPayoutResponseDTOBuilder recipientName(String recipientName) { r.recipientName = recipientName; return this; }
        public AdminPayoutResponseDTOBuilder bankCode(String bankCode) { r.bankCode = bankCode; return this; }
        public AdminPayoutResponseDTOBuilder bankBin(String bankBin) { r.bankBin = bankBin; return this; }
        public AdminPayoutResponseDTOBuilder accountNumber(String accountNumber) { r.accountNumber = accountNumber; return this; }
        public AdminPayoutResponseDTOBuilder accountName(String accountName) { r.accountName = accountName; return this; }
        public AdminPayoutResponseDTOBuilder transferContent(String transferContent) { r.transferContent = transferContent; return this; }
        public AdminPayoutResponseDTOBuilder qrCodeUrl(String qrCodeUrl) { r.qrCodeUrl = qrCodeUrl; return this; }
        public AdminPayoutResponseDTOBuilder bankReference(String bankReference) { r.bankReference = bankReference; return this; }
        public AdminPayoutResponseDTOBuilder adminNote(String adminNote) { r.adminNote = adminNote; return this; }
        public AdminPayoutResponseDTOBuilder orderId(UUID orderId) { r.orderId = orderId; return this; }
        public AdminPayoutResponseDTOBuilder orderStatus(OrderStatus orderStatus) { r.orderStatus = orderStatus; return this; }
        public AdminPayoutResponseDTOBuilder fundingStatus(OrderFundingStatus fundingStatus) { r.fundingStatus = fundingStatus; return this; }
        public AdminPayoutResponseDTOBuilder refundRequestId(UUID refundRequestId) { r.refundRequestId = refundRequestId; return this; }
        public AdminPayoutResponseDTOBuilder productId(UUID productId) { r.productId = productId; return this; }
        public AdminPayoutResponseDTOBuilder productTitle(String productTitle) { r.productTitle = productTitle; return this; }
        public AdminPayoutResponseDTOBuilder buyerId(UUID buyerId) { r.buyerId = buyerId; return this; }
        public AdminPayoutResponseDTOBuilder buyerName(String buyerName) { r.buyerName = buyerName; return this; }
        public AdminPayoutResponseDTOBuilder sellerId(UUID sellerId) { r.sellerId = sellerId; return this; }
        public AdminPayoutResponseDTOBuilder sellerName(String sellerName) { r.sellerName = sellerName; return this; }
        public AdminPayoutResponseDTOBuilder completedById(UUID completedById) { r.completedById = completedById; return this; }
        public AdminPayoutResponseDTOBuilder completedByName(String completedByName) { r.completedByName = completedByName; return this; }
        public AdminPayoutResponseDTOBuilder completedAt(LocalDateTime completedAt) { r.completedAt = completedAt; return this; }
        public AdminPayoutResponseDTOBuilder createdAt(LocalDateTime createdAt) { r.createdAt = createdAt; return this; }
        public AdminPayoutResponseDTO build() { return r; }
    }
}
