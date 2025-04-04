
package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.ShortVideo;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShortVideoRepo extends JpaRepository<ShortVideo, Long> {
    List<ShortVideo> findByUserId(long userId) throws UserNotFoundException;

    void delete(ShortVideo shortVideo);
    @Query("SELECT v FROM ShortVideo v WHERE :userId MEMBER OF v.likedByUsers")
    List<ShortVideo> findByLikedByUsersContaining(@Param("userId") Long userId);

    @Query("SELECT sv FROM ShortVideo sv JOIN sv.likedByUsers u WHERE sv.id = :videoId AND u = :user")
    Optional<ShortVideo> findByIdAndLikedByUsersContaining(@Param("videoId") Long videoId, @Param("user") User user);

    Object findByUserIdAndId(Long userId, Long id);
    @Query("SELECT sv, pd, u " +
            "FROM ShortVideo sv " +
            "LEFT JOIN sv.user u " +
            "LEFT JOIN u.personalDetails pd")
    List<Object[]> findAllShortVideosWithPersonalDetails();
    @Query("SELECT sv FROM ShortVideo sv WHERE sv.createdAt >= :fromDate ORDER BY (sv.shortVideoLikes + sv.shortVideoShares + sv.shortVideoViews) DESC")
    List<ShortVideo> findTrendingShortVideosInLast48Hours(@Param("fromDate") LocalDateTime fromDate);
    List<ShortVideo> findByCategoryIsNull();
    List<ShortVideo> findByCategory(String category);

    @Query("SELECT v FROM ShortVideo v WHERE v.category = :category AND (v.createdAt >= :fromDate OR v.updatedDate >= :fromDate) ORDER BY (v.shortVideoLikes + v.shortVideoShares + v.shortVideoViews) DESC")
    List<ShortVideo> findTrendingVideosByCategory(@Param("category") String category, @Param("fromDate") LocalDateTime fromDate);
    // Fetch videos by category excluding those created in the last 7 days
    List<ShortVideo> findByCategoryAndCreatedAtBefore(String category, LocalDateTime date);
}

