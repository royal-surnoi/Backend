package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.UserArticleInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserArticleInteractionRepo extends JpaRepository<UserArticleInteraction,Long> {
    void deleteByArticlePost(ArticlePost articlePost);

    // Fetch interactions where user_id and articleInteraction match
    List<UserArticleInteraction> findByUserIdAndArticleInteraction(long userId, long articleInteraction);

    // Fetch interactions where user_id, articleInteraction match and created_at is within the last 7 days
    List<UserArticleInteraction> findByUserIdAndArticleInteractionAndCreatedAtAfter(
            long userId, long articleInteraction, LocalDateTime createdAt);

    // Fetch interactions for a user where articleInteraction == 1
    List<UserArticleInteraction> findByUserIdAndArticleInteraction(Long userId, long interaction);

    List<UserArticleInteraction> findTop10ByUserIdAndArticleInteractionOrderByCreatedAtDesc(long userId, int articleInteraction);
}
