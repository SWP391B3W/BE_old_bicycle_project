package swp391.old_bicycle_project.dto.order;

import lombok.*;
import swp391.old_bicycle_project.entity.enums.OrderCancelReason;
import swp391.old_bicycle_project.entity.enums.OrderFundingStatus;
import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.entity.enums.PaymentOption;
import swp391.old_bicycle_project.entity.enums.PlatformFeeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private UUID id;
    private UUID productId;
    private String productTitle;
    private UUID buyerId;
    private String buyerName;
    private UUID sellerId;
    private String sellerName;
    private BigDecimal totalAmount;
    private BigDecimal depositAmount;
    private BigDecimal requiredUpfrontAmount;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private BigDecimal serviceFee;
    private BigDecimal platformFeeTotal;
    private PlatformFeeStatus platformFeeStatus;
    private PaymentOption paymentOption;
    private OrderStatus status;
    private OrderFundingStatus fundingStatus;
    private PaymentMethod paymentMethod;
    private LocalDateTime acceptedAt;
    private LocalDateTime paymentDeadline;
    private OrderCancelReason cancelReason;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
