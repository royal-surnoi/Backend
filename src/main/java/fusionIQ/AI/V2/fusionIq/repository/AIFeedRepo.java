package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.AiFeed;
import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.ImagePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AIFeedRepo extends JpaRepository<AiFeed,Long> {

    @Query("SELECT af FROM AiFeed af WHERE af.feedInteraction = true ORDER BY af.createdAt DESC")
    List<AiFeed> findTop10ByFeedInteractionTrueOrderByCreatedAtDesc(Long userId);

    void deleteByImagePost(ImagePost imagePost);

    void deleteByArticlePost(ArticlePost articlePost);

    List<AiFeed> findByFeedType(String feedType);

    List<AiFeed> findByUserIdAndFeedInteractionOrderByCreatedAtDesc(Long userId, boolean feedInteraction);
    List<AiFeed> findByUserIdOrderByCreatedAtDesc(Long userId);

}
