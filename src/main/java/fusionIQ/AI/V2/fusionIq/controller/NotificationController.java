package fusionIQ.AI.V2.fusionIq.controller;


import fusionIQ.AI.V2.fusionIq.data.Notification;
import fusionIQ.AI.V2.fusionIq.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(@PathVariable Long userId

    ) {
        List<Notification> notifications = notificationService.getNotificationsByUser(userId);
        notifications = notificationService.processNotificationsWithActionUserImage(notifications);
        return ResponseEntity.ok(notifications);
    }
    @PutMapping("/read/{notificationId}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<Long> getUnreadMessageCount(@PathVariable Long userId) {
        try {
            long count = notificationService.getUnreadMessageCount(userId);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

//    @GetMapping("/user/{userId}/notifications")
//    public ResponseEntity<Page<Notification>> getNotificationsByUser(
//            @PathVariable Long userId,
//            @RequestParam(defaultValue = "0") int page,  // Default page number is 0
//            @RequestParam(defaultValue = "10") int size  // Default size is 10 records per page
//    ) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Notification> notificationsPage = notificationService.getNotificationsByUser(userId, pageable);
//        return ResponseEntity.ok(notificationsPage); // Return the paginated list of notifications with a 200 OK status
//    }
}

