package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.PostEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PostEventListener {
    @Autowired
    private NotificationService notificationService;

    @EventListener
    public void handlePostEvent(PostEvent event) {
        Long userId = event.getUserId();
        String message = "New post from user you follow";
        notificationService.createNotification(userId, message);
    }
}
