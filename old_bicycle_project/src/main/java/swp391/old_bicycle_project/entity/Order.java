package swp391.old_bicycle_project.entity;

import swp391.old_bicycle_project.entity.enums.OrderStatus;
import swp391.old_bicycle_project.entity.enums.OrderCancelReason;
import swp391.old_bicycle_project.entity.enums.OrderFundingStatus;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.entity.enums.PaymentOption;
import swp391.old_bicycle_project.entity.enums.PlatformFeeStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "deposit_amount")
    private BigDecimal depositAmount;

    @Column(name = "required_upfront_amount")
    private BigDecimal requiredUpfrontAmount;

    @Builder.Default
    @Column(name = "paid_amount")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "remaining_amount")
    private BigDecimal remainingAmount;

    @Column(name = "service_fee")
    private BigDecimal serviceFee;

    @Builder.Default
    @Column(name = "fee_base_amount", nullable = false)
    private BigDecimal feeBaseAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "platform_fee_rate", nullable = false)
    private BigDecimal platformFeeRate = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "platform_fee_total", nullable = false)
    private BigDecimal platformFeeTotal = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "buyer_fee_amount", nullable = false)
    private BigDecimal buyerFeeAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "seller_fee_amount", nullable = false)
    private BigDecimal sellerFeeAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "buyer_charge_amount", nullable = false)
    private BigDecimal buyerChargeAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "seller_gross_payout_amount", nullable = false)
    private BigDecimal sellerGrossPayoutAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "seller_net_payout_amount", nullable = false)
    private BigDecimal sellerNetPayoutAmount = BigDecimal.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "platform_fee_status", nullable = false, columnDefinition = "platform_fee_status")
    private PlatformFeeStatus platformFeeStatus = PlatformFeeStatus.not_applicable;

    @Column(name = "platform_fee_recognized_at")
    private LocalDateTime platformFeeRecognizedAt;

    @Column(name = "platform_fee_reversed_at")
    private LocalDateTime platformFeeReversedAt;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_option")
    private PaymentOption paymentOption = PaymentOption.partial;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "status", columnDefinition = "order_status")
    private OrderStatus status = OrderStatus.pending;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "funding_status")
    private OrderFundingStatus fundingStatus = OrderFundingStatus.unpaid;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "payment_method", columnDefinition = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "payment_deadline")
    private LocalDateTime paymentDeadline;

    @Column(name = "buyer_confirmation_deadline")
    private LocalDateTime buyerConfirmationDeadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancel_reason")
    private OrderCancelReason cancelReason;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Manual Getters
    public UUID getId() { return id; }
    public User getBuyer() { return buyer; }
    public Product getProduct() { return product; }
    public User getSeller() { return seller; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public BigDecimal getDepositAmount() { return depositAmount; }
    public BigDecimal getRequiredUpfrontAmount() { return requiredUpfrontAmount; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public BigDecimal getServiceFee() { return serviceFee; }
    public PlatformFeeStatus getPlatformFeeStatus() { return platformFeeStatus; }
    public OrderStatus getStatus() { return status; }
    public OrderFundingStatus getFundingStatus() { return fundingStatus; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public BigDecimal getFeeBaseAmount() { return feeBaseAmount; }
    public BigDecimal getPlatformFeeRate() { return platformFeeRate; }
    public BigDecimal getPlatformFeeTotal() { return platformFeeTotal; }
    public BigDecimal getBuyerFeeAmount() { return buyerFeeAmount; }
    public BigDecimal getSellerFeeAmount() { return sellerFeeAmount; }
    public BigDecimal getBuyerChargeAmount() { return buyerChargeAmount; }
    public BigDecimal getSellerGrossPayoutAmount() { return sellerGrossPayoutAmount; }
    public BigDecimal getSellerNetPayoutAmount() { return sellerNetPayoutAmount; }
    public LocalDateTime getPlatformFeeRecognizedAt() { return platformFeeRecognizedAt; }
    public LocalDateTime getPlatformFeeReversedAt() { return platformFeeReversedAt; }
    public PaymentOption getPaymentOption() { return paymentOption; }
    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public LocalDateTime getPaymentDeadline() { return paymentDeadline; }
    public LocalDateTime getBuyerConfirmationDeadline() { return buyerConfirmationDeadline; }
    public OrderCancelReason getCancelReason() { return cancelReason; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Manual Setters
    public void setId(UUID id) { this.id = id; }
    public void setBuyer(User buyer) { this.buyer = buyer; }
    public void setProduct(Product product) { this.product = product; }
    public void setSeller(User seller) { this.seller = seller; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
    public void setRequiredUpfrontAmount(BigDecimal requiredUpfrontAmount) { this.requiredUpfrontAmount = requiredUpfrontAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
    public void setServiceFee(BigDecimal serviceFee) { this.serviceFee = serviceFee; }
    public void setPlatformFeeStatus(PlatformFeeStatus platformFeeStatus) { this.platformFeeStatus = platformFeeStatus; }
    public void setPlatformFeeRecognizedAt(LocalDateTime platformFeeRecognizedAt) { this.platformFeeRecognizedAt = platformFeeRecognizedAt; }
    public void setPlatformFeeReversedAt(LocalDateTime platformFeeReversedAt) { this.platformFeeReversedAt = platformFeeReversedAt; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setFundingStatus(OrderFundingStatus fundingStatus) { this.fundingStatus = fundingStatus; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setAcceptedAt(LocalDateTime acceptedAt) { this.acceptedAt = acceptedAt; }
    public void setPaymentDeadline(LocalDateTime paymentDeadline) { this.paymentDeadline = paymentDeadline; }
    public void setBuyerConfirmationDeadline(LocalDateTime buyerConfirmationDeadline) { this.buyerConfirmationDeadline = buyerConfirmationDeadline; }
    public void setCancelReason(OrderCancelReason cancelReason) { this.cancelReason = cancelReason; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

    // Manual Builder
    public static OrderBuilder builder() { return new OrderBuilder(); }
    public static class OrderBuilder {
        private Order r = new Order();
        public OrderBuilder id(UUID id) { r.id = id; return this; }
        public OrderBuilder buyer(User buyer) { r.buyer = buyer; return this; }
        public OrderBuilder product(Product product) { r.product = product; return this; }
        public OrderBuilder seller(User seller) { r.seller = seller; return this; }
        public OrderBuilder totalAmount(BigDecimal totalAmount) { r.totalAmount = totalAmount; return this; }
        public OrderBuilder depositAmount(BigDecimal depositAmount) { r.depositAmount = depositAmount; return this; }
        public OrderBuilder requiredUpfrontAmount(BigDecimal requiredUpfrontAmount) { r.requiredUpfrontAmount = requiredUpfrontAmount; return this; }
        public OrderBuilder paidAmount(BigDecimal paidAmount) { r.paidAmount = paidAmount; return this; }
        public OrderBuilder remainingAmount(BigDecimal remainingAmount) { r.remainingAmount = remainingAmount; return this; }
        public OrderBuilder serviceFee(BigDecimal serviceFee) { r.serviceFee = serviceFee; return this; }
        public OrderBuilder platformFeeStatus(PlatformFeeStatus platformFeeStatus) { r.platformFeeStatus = platformFeeStatus; return this; }
        public OrderBuilder paymentOption(PaymentOption paymentOption) { r.paymentOption = paymentOption; return this; }
        public OrderBuilder feeBaseAmount(BigDecimal feeBaseAmount) { r.feeBaseAmount = feeBaseAmount; return this; }
        public OrderBuilder platformFeeRate(BigDecimal platformFeeRate) { r.platformFeeRate = platformFeeRate; return this; }
        public OrderBuilder platformFeeTotal(BigDecimal platformFeeTotal) { r.platformFeeTotal = platformFeeTotal; return this; }
        public OrderBuilder buyerFeeAmount(BigDecimal buyerFeeAmount) { r.buyerFeeAmount = buyerFeeAmount; return this; }
        public OrderBuilder sellerFeeAmount(BigDecimal sellerFeeAmount) { r.sellerFeeAmount = sellerFeeAmount; return this; }
        public OrderBuilder buyerChargeAmount(BigDecimal buyerChargeAmount) { r.buyerChargeAmount = buyerChargeAmount; return this; }
        public OrderBuilder sellerGrossPayoutAmount(BigDecimal sellerGrossPayoutAmount) { r.sellerGrossPayoutAmount = sellerGrossPayoutAmount; return this; }
        public OrderBuilder sellerNetPayoutAmount(BigDecimal sellerNetPayoutAmount) { r.sellerNetPayoutAmount = sellerNetPayoutAmount; return this; }
        public OrderBuilder status(OrderStatus status) { r.status = status; return this; }
        public OrderBuilder fundingStatus(OrderFundingStatus fundingStatus) { r.fundingStatus = fundingStatus; return this; }
        public OrderBuilder paymentMethod(PaymentMethod paymentMethod) { r.paymentMethod = paymentMethod; return this; }
        public OrderBuilder buyerConfirmationDeadline(LocalDateTime buyerConfirmationDeadline) { r.buyerConfirmationDeadline = buyerConfirmationDeadline; return this; }
        public Order build() { return r; }
    }
}
