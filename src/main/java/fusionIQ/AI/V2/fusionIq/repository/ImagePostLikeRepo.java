package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.ImagePost;
import fusionIQ.AI.V2.fusionIq.data.ImagePostLike;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagePostLikeRepo extends JpaRepository<ImagePostLike, Long> {
    List<ImagePostLike> findByImagePost(ImagePost imagePost);
    List<ImagePostLike> findByUser(User user);
    List<ImagePostLike> findByImagePostAndUser(ImagePost imagePost, User user);

    void deleteByImagePost(ImagePost imagePost);

    Optional<Object> findByUserIdAndImagePostId(long userId, long postId);

}
