package swp391.old_bicycle_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import swp391.old_bicycle_project.service.OrderService;

@Component
@RequiredArgsConstructor
public class OrderAutoCompletionScheduler {

    private static final Logger log = LoggerFactory.getLogger(OrderAutoCompletionScheduler.class);
    private final OrderService orderService;

    @Scheduled(
            fixedDelayString = "${order.auto-complete.scan-interval-ms:60000}",
            initialDelayString = "${order.auto-complete.initial-delay-ms:20000}"
    )
    public void autoCompleteEligibleOrders() {
        int autoCompletedCount = orderService.autoCompleteOverdueBuyerConfirmations();
        if (autoCompletedCount > 0) {
            log.info("Auto-completed {} order(s) after buyer confirmation timeout", autoCompletedCount);
        }
    }
}
