
package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.ImagePost;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ImagePostRepo extends JpaRepository<ImagePost, Long> {
    List<ImagePost> findByUser(User user);

    @Query("SELECT p FROM ImagePost p ORDER BY p.postDate DESC")
    List<ImagePost> findAllOrderByPostDateDesc();

    @Query("SELECT p FROM ImagePost p WHERE p.user = ?1 ORDER BY p.postDate DESC")
    List<ImagePost> findByUserOrderByPostDateDesc(User user);

    ImagePost findByUserIdAndId(Long userId, Long id);

    @Query("SELECT ip, pd, u FROM ImagePost ip " +
            "JOIN User u ON ip.user.id = u.id " +
            "LEFT JOIN PersonalDetails pd ON u.id = pd.user.id " +
            "WHERE u.id = :userId")
    List<Object[]> findImagePostsWithPersonalDetailsAndUser(@Param("userId") Long userId);

    @Query("SELECT ip, pd, u FROM ImagePost ip " +
            "JOIN User u ON ip.user.id = u.id " +
            "LEFT JOIN PersonalDetails pd ON u.id = pd.user.id")
    List<Object[]> findAllImagePostsWithPersonalDetails();
    List<ImagePost> findByCategoryIsNull();
    @Query("SELECT i FROM ImagePost i WHERE i.postDate >= :fromDate ORDER BY (i.imageLikeCount + i.imageShareCount) DESC")
    List<ImagePost> findTrendingImagesInLast48Hours(@Param("fromDate") LocalDateTime fromDate);
    List<ImagePost> findByCategory(String category);
    //List<ImagePost> findByCategoryAndPostDateAfter(String category, LocalDateTime postDate);

    @Query("SELECT ip FROM ImagePost ip WHERE ip.category = :category AND ip.postDate >= :tenDaysAgo ORDER BY (ip.imageLikeCount + ip.imageShareCount) DESC")
    List<ImagePost> findTrendingPostsByCategory(String category, LocalDateTime tenDaysAgo);

    // Fetch ImagePosts by category excluding those posted in the last 7 days
    List<ImagePost> findByCategoryAndPostDateBefore(String category, LocalDateTime date);
}
