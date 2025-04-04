package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.LongVideo;
import fusionIQ.AI.V2.fusionIq.data.ShortVideo;
import fusionIQ.AI.V2.fusionIq.data.VideoComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoCommentRepo extends JpaRepository<VideoComment, Long> {
    int countByShortVideoId(Long videoId);

    void deleteByLongVideo(LongVideo longVideo);

    void deleteByShortVideo(ShortVideo shortVideo);

    List<VideoComment> findByParentCommentId(Long parentCommentId);

}
