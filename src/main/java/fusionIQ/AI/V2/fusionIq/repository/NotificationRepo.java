package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Notification;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);



    List<Notification> findByUserId(Long userId);



    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.contentType = 'message' AND n.isRead = false")
    long countUnreadMessagesByUserId(@Param("userId") Long userId);
}
