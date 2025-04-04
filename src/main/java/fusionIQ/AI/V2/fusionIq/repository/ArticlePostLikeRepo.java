package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.ArticlePostLike;
import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticlePostLikeRepo extends JpaRepository<ArticlePostLike, Long> {
    List<ArticlePostLike> findByArticlePost(ArticlePost articlePost);
    List<ArticlePostLike> findByUser(User user);
    List<ArticlePostLike> findByArticlePostAndUser(ArticlePost articlePost, User user);

    void deleteByArticlePost(ArticlePost articlePost);

    Optional<Object> findByUserIdAndArticlePostId(long userId, long postId);

}
