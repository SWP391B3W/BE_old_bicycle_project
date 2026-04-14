package swp391.old_bicycle_project.dto.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.entity.enums.PaymentOption;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequest {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @DecimalMin(value = "0.01", message = "Upfront amount must be greater than 0")
    private BigDecimal upfrontAmount;

    @Builder.Default
    private PaymentOption paymentOption = PaymentOption.partial;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
