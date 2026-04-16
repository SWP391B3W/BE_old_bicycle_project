package swp391.old_bicycle_project.service.impl;

import swp391.old_bicycle_project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentExpiryScheduler {

    private static final Logger log = LoggerFactory.getLogger(PaymentExpiryScheduler.class);
    private final PaymentService paymentService;

    @Scheduled(
            fixedDelayString = "${payment.expiry.scan-interval-ms:60000}",
            initialDelayString = "${payment.expiry.initial-delay-ms:15000}"
    )
    public void expireOverduePayments() {
        int expiredCount = paymentService.expireOverdueUpfrontPayments();
        if (expiredCount > 0) {
            log.info("Auto-expired {} overdue upfront payment order(s)", expiredCount);
        }
    }
}
