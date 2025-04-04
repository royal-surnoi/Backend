package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.UserImageInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserImageInteractionRepo extends JpaRepository<UserImageInteraction,Long> {
    List<UserImageInteraction> findByUserId(long userId);

    List<UserImageInteraction> findByImagePostId(long imagePostId);

    // Fetch interactions where user_id and imageInteraction match
    List<UserImageInteraction> findByUserIdAndImageInteraction(long userId, long imageInteraction);

    // Fetch interactions where user_id, imageInteraction match and created_at is within the last 7 days
    List<UserImageInteraction> findByUserIdAndImageInteractionAndCreatedAtAfter(
            long userId, long imageInteraction, LocalDateTime createdAt);

    // Fetch interactions for a user where interaction == 1
    List<UserImageInteraction> findByUserIdAndImageInteraction(Long userId, long interaction);

    List<UserImageInteraction> findTop10ByUserIdAndImageInteractionOrderByCreatedAtDesc(long userId, int imageInteraction);
}
