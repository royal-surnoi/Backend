package fusionIQ.AI.V2.fusionIq.repository;


import fusionIQ.AI.V2.fusionIq.data.UserShortVideoInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserShortVideoInteractionRepo extends JpaRepository<UserShortVideoInteraction, Long> {

    List<UserShortVideoInteraction> findByUserId(long userId);
    List<UserShortVideoInteraction> findByShortVideoId(long shortVideoId);
    // Custom query to filter by user_id, interaction, and recent created_at
    List<UserShortVideoInteraction> findByUserIdAndShortVideoInteractionAndCreatedAtAfter(
            long userId, long shortVideoInteraction, LocalDateTime createdAt);

    // Fetch interactions for a user where interaction == 1
    List<UserShortVideoInteraction> findByUserIdAndShortVideoInteraction(Long userId, long interaction);
}


