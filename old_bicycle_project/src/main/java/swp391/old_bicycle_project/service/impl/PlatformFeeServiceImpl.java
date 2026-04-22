package swp391.old_bicycle_project.service.impl;

import swp391.old_bicycle_project.entity.enums.PaymentMethod;
import swp391.old_bicycle_project.entity.enums.PlatformFeeStatus;
import swp391.old_bicycle_project.service.PlatformFeeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PlatformFeeServiceImpl implements PlatformFeeService {

    private static final BigDecimal PLATFORM_FEE_RATE = new BigDecimal("0.1000");

    @Override
    public PlatformFeeQuote calculate(BigDecimal totalAmount, BigDecimal protectedAmount, PaymentMethod paymentMethod) {
        BigDecimal normalizedTotalAmount = normalize(totalAmount);
        BigDecimal normalizedProtectedAmount = normalize(protectedAmount);

        if (!isPlatformFeeApplicable(paymentMethod)
                || normalizedTotalAmount.compareTo(BigDecimal.ZERO) <= 0
                || normalizedProtectedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return new PlatformFeeQuote(
                    normalizedTotalAmount,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    normalizedProtectedAmount,
                    normalizedProtectedAmount,
                    normalizedProtectedAmount,
                    PlatformFeeStatus.not_applicable);
        }

        BigDecimal platformFeeTotal = normalizedTotalAmount
            .multiply(PLATFORM_FEE_RATE)
            .setScale(0, RoundingMode.HALF_UP);
        BigDecimal buyerFeeAmount = BigDecimal.ZERO;
        BigDecimal sellerFeeAmount = platformFeeTotal;
        BigDecimal buyerChargeAmount = normalizedProtectedAmount;
        BigDecimal sellerGrossPayoutAmount = normalizedProtectedAmount;
        BigDecimal sellerNetPayoutAmount = normalizedProtectedAmount.subtract(sellerFeeAmount);

        return new PlatformFeeQuote(
                normalizedTotalAmount,
                PLATFORM_FEE_RATE,
                platformFeeTotal,
                buyerFeeAmount,
                sellerFeeAmount,
                buyerChargeAmount,
                sellerGrossPayoutAmount,
                sellerNetPayoutAmount,
                PlatformFeeStatus.pending);
    }

    private boolean isPlatformFeeApplicable(PaymentMethod paymentMethod) {
        return paymentMethod != null && paymentMethod != PaymentMethod.cash;
    }

    private BigDecimal normalize(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
