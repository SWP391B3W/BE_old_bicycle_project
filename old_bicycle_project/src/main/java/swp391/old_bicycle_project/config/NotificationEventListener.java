package swp391.old_bicycle_project.config;

import swp391.old_bicycle_project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NotificationEventListener.class);
    private final NotificationService notificationService;

    @Async
    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        log.info("Received notification event for user: {}", event.getUserId());
        try {
            notificationService.sendNotification(
                    event.getUserId(),
                    event.getTitle(),
                    event.getContent(),
                    event.getType(),
                    event.getMetadata()
            );
        } catch (Exception e) {
            log.error("Failed to process notification event for user {}: {}", event.getUserId(), e.getMessage());
        }
    }
}
