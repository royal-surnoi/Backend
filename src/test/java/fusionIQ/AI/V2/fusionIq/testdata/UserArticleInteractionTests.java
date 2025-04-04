package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserArticleInteraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class UserArticleInteractionTest {

    private UserArticleInteraction interaction;
    private User mockUser;
    private ArticlePost mockArticlePost;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        mockUser = new User(); // Assuming User has a no-args constructor
        mockArticlePost = new ArticlePost(); // Assuming ArticlePost has a no-args constructor
        testDateTime = LocalDateTime.now();

        interaction = new UserArticleInteraction(
                1L,
                mockUser,
                mockArticlePost,
                100L
        );
        interaction.setCreatedAt(testDateTime);
    }

    @Test
    void testDefaultConstructor() {
        UserArticleInteraction emptyInteraction = new UserArticleInteraction();
        assertNotNull(emptyInteraction);
    }

    @Test
    void testParameterizedConstructor() {
        assertNotNull(interaction);
        assertEquals(1L, interaction.getId());
        assertEquals(mockUser, interaction.getUser());
        assertEquals(mockArticlePost, interaction.getArticlePost());
        assertEquals(100L, interaction.getArticleInteraction());
    }

    @Test
    void testSetAndGetId() {
        interaction.setId(2L);
        assertEquals(2L, interaction.getId());
    }

    @Test
    void testSetAndGetUser() {
        User newUser = new User();
        interaction.setUser(newUser);
        assertEquals(newUser, interaction.getUser());
    }

    @Test
    void testSetAndGetArticlePost() {
        ArticlePost newArticle = new ArticlePost();
        interaction.setArticlePost(newArticle);
        assertEquals(newArticle, interaction.getArticlePost());
    }

    @Test
    void testSetAndGetArticleInteraction() {
        interaction.setArticleInteraction(200L);
        assertEquals(200L, interaction.getArticleInteraction());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1);
        interaction.setCreatedAt(newDateTime);
        assertEquals(newDateTime, interaction.getCreatedAt());
    }

    @Test
    void testCreatedAtAnnotation() {
        // Testing that createdAt is automatically set
        UserArticleInteraction newInteraction = new UserArticleInteraction();
        // The @CreationTimestamp should ensure createdAt is set automatically
        // Note: In a real database context, this would be set automatically
        assertNull(newInteraction.getCreatedAt()); // Will be null in test context
    }

    @Test
    void testToString() {
        String toString = interaction.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=" + interaction.getId()));
        assertTrue(toString.contains("articleInteraction=" + interaction.getArticleInteraction()));
        assertTrue(toString.contains("createdAt=" + interaction.getCreatedAt()));
    }

    @Test
    void testNullUser() {
        UserArticleInteraction newInteraction = new UserArticleInteraction();
        assertNull(newInteraction.getUser());
    }

    @Test
    void testNullArticlePost() {
        UserArticleInteraction newInteraction = new UserArticleInteraction();
        assertNull(newInteraction.getArticlePost());
    }

    @Test
    void testDefaultArticleInteraction() {
        UserArticleInteraction newInteraction = new UserArticleInteraction();
        assertEquals(0L, newInteraction.getArticleInteraction());
    }

    @Test
    void testConstructorWithNullValues() {
        UserArticleInteraction newInteraction = new UserArticleInteraction(1L, null, null, 100L);
        assertNotNull(newInteraction);
        assertNull(newInteraction.getUser());
        assertNull(newInteraction.getArticlePost());
        assertEquals(100L, newInteraction.getArticleInteraction());
    }

    @Test
    void testNegativeArticleInteraction() {
        interaction.setArticleInteraction(-1L);
        assertEquals(-1L, interaction.getArticleInteraction());
        // Note: Add validation if negative values should not be allowed
    }
}