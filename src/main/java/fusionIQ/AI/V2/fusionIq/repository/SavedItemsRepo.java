package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedItemsRepo extends JpaRepository<SavedItems, Long> {
    List<SavedItems> findByUserIdAndImagePostIsNotNull(Long userId);

    List<SavedItems> findByUserIdAndArticlePostIsNotNull(Long userId);

    List<SavedItems> findByUserIdAndShortVideoIsNotNull(Long userId);

    List<SavedItems> findByUserIdAndLongVideoIsNotNull(Long userId);

    List<SavedItems> findByUserId(Long userId);

    SavedItems findByUserAndImagePost(User user, ImagePost imagePost);
    SavedItems findByUserAndArticlePost(User user, ArticlePost articlePost);
    SavedItems findByUserAndShortVideo(User user, ShortVideo shortVideo);
    SavedItems findByUserAndLongVideo(User user, LongVideo longVideo);

    void deleteByImagePost(ImagePost imagePost);

    void deleteByArticlePost(ArticlePost articlePost);

    void deleteByShortVideo(ShortVideo shortVideo);

    void deleteByLongVideo(LongVideo longVideo);
}
