package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Comment;
import fusionIQ.AI.V2.fusionIq.data.NestedComment;
import fusionIQ.AI.V2.fusionIq.data.VideoComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NestedCommentRepo extends JpaRepository<NestedComment,Long> {
    List<NestedComment> findByParentComment(VideoComment parentComment);

    List<NestedComment> findByBaseComment(Comment baseComment);

    void deleteByBaseCommentId(Long commentId);

    List<NestedComment> findByBaseCommentId(Long commentId);

}
