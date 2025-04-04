package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.ArticlePostLike;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticlePostLikeTests {

    private ArticlePostLike articlePostLike;
    private User user;
    private ArticlePost articlePost;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User"); // Ensure required fields are set

        articlePost = new ArticlePost();
        articlePost.setId(1L);
        articlePost.setArticle("Sample Article");

        articlePostLike = new ArticlePostLike();
    }

    @Test
    void testDefaultConstructor() {
        // Ensure default constructor sets default values correctly
        assertThat(articlePostLike.getId()).isEqualTo(0L); // Default value for long (can be 0L if not explicitly set)
        assertThat(articlePostLike.getUser()).isNull();
        assertThat(articlePostLike.getArticlePost()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        ArticlePostLike articlePostLike = new ArticlePostLike(user, articlePost);

        assertThat(articlePostLike.getUser()).isEqualTo(user);
        assertThat(articlePostLike.getArticlePost()).isEqualTo(articlePost);
    }

    @Test
    void testSettersAndGetters() {
        articlePostLike.setId(2L);
        articlePostLike.setUser(user);
        articlePostLike.setArticlePost(articlePost);

        assertThat(articlePostLike.getId()).isEqualTo(2L);
        assertThat(articlePostLike.getUser()).isEqualTo(user);
        assertThat(articlePostLike.getArticlePost()).isEqualTo(articlePost);
    }

    @Test
    void testToString() {
        articlePostLike.setId(1L);
        articlePostLike.setUser(user);
        articlePostLike.setArticlePost(articlePost);
        articlePostLike.setArticleLikedAt(LocalDateTime.now());  // Initialize articleLikedAt

        String expected = "ArticlePostLike{" +
                "id=" + 1L +
                ", user=" + user +
                ", articlePost=" + articlePost +
                ", articleLikedAt=" + articlePostLike.getArticleLikedAt() +
                '}';

        assertThat(articlePostLike.toString()).isEqualTo(expected);
    }

}
