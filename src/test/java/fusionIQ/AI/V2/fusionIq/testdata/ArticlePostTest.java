package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.Comment;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserArticleInteraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ArticlePostTest {

    private ArticlePost articlePost;
    private User mockUser;
    private LocalDateTime testDateTime;
    private List<Comment> comments;
    private List<UserArticleInteraction> interactions;

    @BeforeEach
    void setUp() {
        mockUser = new User(); // Assuming User has a no-args constructor
        testDateTime = LocalDateTime.now();
        comments = new ArrayList<>();
        interactions = new ArrayList<>();

        articlePost = new ArticlePost(
                1L,
                "Test Article Content",
                mockUser,
                testDateTime,
                testDateTime,
                "Technology",
                0,
                0,
                0,
                "tech",
                comments,
                interactions
        );
    }

    @Test
    void testDefaultConstructor() {
        ArticlePost emptyArticle = new ArticlePost();
        assertNotNull(emptyArticle);
        assertNotNull(emptyArticle.getPostDate());
    }

    @Test
    void testParameterizedConstructor() {
        assertNotNull(articlePost);
        assertEquals(1L, articlePost.getId());
        assertEquals("Test Article Content", articlePost.getArticle());
        assertEquals(mockUser, articlePost.getUser());
        assertEquals(testDateTime, articlePost.getPostDate());
        assertEquals(testDateTime, articlePost.getUpdatedDate());
        assertEquals(0, articlePost.getArticleLikeCount());
        assertEquals(0, articlePost.getArticleDislikes());
        assertEquals(0, articlePost.getArticleShareCount());
        assertEquals("tech", articlePost.getTag());
        assertEquals("Technology", articlePost.getCategory());
        assertEquals(comments, articlePost.getComments());
        assertEquals(interactions, articlePost.getUserArticleInteraction());
    }

    @Test
    void testSetAndGetId() {
        articlePost.setId(2L);
        assertEquals(2L, articlePost.getId());
    }

    @Test
    void testSetAndGetArticle() {
        String newArticle = "Updated Article Content";
        articlePost.setArticle(newArticle);
        assertEquals(newArticle, articlePost.getArticle());
    }

    @Test
    void testSetAndGetUser() {
        User newUser = new User();
        articlePost.setUser(newUser);
        assertEquals(newUser, articlePost.getUser());
    }

    @Test
    void testSetAndGetPostDate() {
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1);
        articlePost.setPostDate(newDateTime);
        assertEquals(newDateTime, articlePost.getPostDate());
    }

    @Test
    void testSetAndGetUpdatedDate() {
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1);
        articlePost.setUpdatedDate(newDateTime);
        assertEquals(newDateTime, articlePost.getUpdatedDate());
    }

    @Test
    void testSetAndGetArticleLikeCount() {
        articlePost.setArticleLikeCount(10);
        assertEquals(10, articlePost.getArticleLikeCount());
    }

    @Test
    void testSetAndGetArticleDislikes() {
        articlePost.setArticleDislikes(5);
        assertEquals(5, articlePost.getArticleDislikes());
    }

    @Test
    void testSetAndGetArticleShareCount() {
        articlePost.setArticleShareCount(15);
        assertEquals(15, articlePost.getArticleShareCount());
    }

    @Test
    void testSetAndGetTag() {
        articlePost.setTag("newTag");
        assertEquals("newTag", articlePost.getTag());
    }

    @Test
    void testSetAndGetCategory() {
        articlePost.setCategory("Science");
        assertEquals("Science", articlePost.getCategory());
    }

    @Test
    void testSetAndGetComments() {
        List<Comment> newComments = new ArrayList<>();
        Comment comment = new Comment(); // Assuming Comment has a no-args constructor
        newComments.add(comment);

        articlePost.setComments(newComments);
        assertEquals(newComments, articlePost.getComments());
    }

    @Test
    void testSetAndGetUserArticleInteraction() {
        List<UserArticleInteraction> newInteractions = new ArrayList<>();
        UserArticleInteraction interaction = new UserArticleInteraction(); // Assuming UserArticleInteraction has a no-args constructor
        newInteractions.add(interaction);

        articlePost.setUserArticleInteraction(newInteractions);
        assertEquals(newInteractions, articlePost.getUserArticleInteraction());
    }

    @Test
    void testToString() {
        String toString = articlePost.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=" + articlePost.getId()));
        assertTrue(toString.contains("article='" + articlePost.getArticle() + "'"));
        assertTrue(toString.contains("tag='" + articlePost.getTag() + "'"));
        assertTrue(toString.contains("category='" + articlePost.getCategory() + "'"));
    }
}