package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.SelfIntroductionVideos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SelfIntroductionVideosRepo extends JpaRepository<SelfIntroductionVideos, Long> {
    SelfIntroductionVideos findByUserId(Long userId);

    @Query("SELECT s FROM SelfIntroductionVideos s WHERE s.user.id = :userId")
    Optional<SelfIntroductionVideos> findBySelfIntroductionVideoUserId(@Param("userId") Long userId);

    @Query("SELECT s FROM SelfIntroductionVideos s WHERE s.user.id = :userId")
    Optional<SelfIntroductionVideos> findByUserId2(@Param("userId") Long userId);

}
