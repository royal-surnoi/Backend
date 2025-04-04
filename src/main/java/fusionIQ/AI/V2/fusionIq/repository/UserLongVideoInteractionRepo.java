package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.UserLongVideoInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserLongVideoInteractionRepo extends JpaRepository<UserLongVideoInteraction, Long> {

    // Fetch interactions where user_id and longVideoInteraction match
    List<UserLongVideoInteraction> findByUserIdAndLongVideoInteraction(long userId, long longVideoInteraction);

    // Fetch interactions where user_id, longVideoInteraction match and created_at is within the last 7 days
    List<UserLongVideoInteraction> findByUserIdAndLongVideoInteractionAndCreatedAtAfter(
            long userId, long longVideoInteraction, LocalDateTime createdAt);

    // Fetch interactions for a user where interaction == 1
    List<UserLongVideoInteraction> findByUserIdAndLongVideoInteraction(Long userId, long interaction);
}