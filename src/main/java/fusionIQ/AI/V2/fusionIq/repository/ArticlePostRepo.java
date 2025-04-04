package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticlePostRepo extends JpaRepository<ArticlePost, Long> {
    List<ArticlePost> findByUserOrderByPostDateDesc(User user);

    @Query("SELECT ap FROM ArticlePost ap ORDER BY ap.postDate DESC")
    List<ArticlePost> findAllOrderByPostDateDesc();

    ArticlePost findByUserIdAndId(Long userId, Long id);

    @Query("SELECT ap, pd, u " +
            "FROM ArticlePost ap " +
            "JOIN ap.user u " +
            "JOIN PersonalDetails pd ON pd.user.id = u.id " +
            "WHERE u.id = :userId")
    List<Object[]> findArticlePostsWithPersonalDetailsAndUser(@Param("userId") Long userId);

    @Query("SELECT ap, pd, u FROM ArticlePost ap " +
            "JOIN User u ON ap.user.id = u.id " +
            "LEFT JOIN PersonalDetails pd ON u.id = pd.user.id")
    List<Object[]> findAllArticlePostsWithPersonalDetails();

    @Query("SELECT a FROM ArticlePost a WHERE a.postDate >= :fromDate ORDER BY (a.articleLikeCount + a.articleShareCount) DESC")
    List<ArticlePost> findTrendingArticlesInLast48Hours(@Param("fromDate") LocalDateTime fromDate);



    List<ArticlePost> findByCategoryIsNull();
    List<ArticlePost> findByCategory(String category);
    //List<ArticlePost> findArticlesByCategoryAndPostDateAfter(String category, LocalDateTime postDate);

    @Query("SELECT a FROM ArticlePost a " +
            "WHERE a.category = :category AND a.postDate >= :startDate " +
            "ORDER BY (a.articleLikeCount + a.articleShareCount) DESC")
    List<ArticlePost> findTrendingArticlesByCategory(String category, LocalDateTime startDate);

    // Fetch ArticlePosts by category excluding those posted in the last 7 days
    List<ArticlePost> findByCategoryAndPostDateBefore(String category, LocalDateTime date);
}