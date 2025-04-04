package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.Comment;
import fusionIQ.AI.V2.fusionIq.data.ImagePost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByImagePostId(Long imagePostId);
    List<Comment> findByArticlePostId(Long articlePostId);
    List<Comment> findByUserId(Long userId);
    List<Comment> findByUserIdAndImagePostId(Long userId, Long imagePostId);
    List<Comment> findByUserIdAndArticlePostId(Long userId, Long articlePostId);



    long countByImagePostId(Long imagePostId);
    long countByArticlePostId(Long articlePostId);

    List<Comment> findByParentComment(Comment parentComment);

    List<Comment> findNestedCommentsByImagePostIdAndParentCommentId(long imagePostId, long parentCommentId);

    List<Comment> findNestedCommentsByArticlePostIdAndParentCommentId(long articlePostId, long parentCommentId);

    void deleteByArticlePost(ArticlePost articlePost);
    void deleteByImagePost(ImagePost imagePost);
}
 