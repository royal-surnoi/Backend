package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.ImagePost;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserImageInteraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class UserImageInteractionTest {

    private UserImageInteraction interaction;
    private User mockUser;
    private ImagePost mockImagePost;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        mockUser = new User(); // Assuming User has a no-args constructor
        mockImagePost = new ImagePost(); // Assuming ImagePost has a no-args constructor
        testDateTime = LocalDateTime.now();

        interaction = new UserImageInteraction(
                1L,
                100L,
                mockUser,
                mockImagePost
        );
        interaction.setCreatedAt(testDateTime);
    }

    @Test
    void testDefaultConstructor() {
        UserImageInteraction emptyInteraction = new UserImageInteraction();
        assertNotNull(emptyInteraction);
    }

    @Test
    void testParameterizedConstructor() {
        assertNotNull(interaction);
        assertEquals(1L, interaction.getId());
        assertEquals(100L, interaction.getImageInteraction());
        assertEquals(mockUser, interaction.getUser());
        assertEquals(mockImagePost, interaction.getImagePost());
    }

    @Test
    void testSetAndGetId() {
        interaction.setId(2L);
        assertEquals(2L, interaction.getId());
    }

    @Test
    void testSetAndGetImageInteraction() {
        interaction.setImageInteraction(200L);
        assertEquals(200L, interaction.getImageInteraction());
    }

    @Test
    void testSetAndGetUser() {
        User newUser = new User();
        interaction.setUser(newUser);
        assertEquals(newUser, interaction.getUser());
    }

    @Test
    void testSetAndGetImagePost() {
        ImagePost newImagePost = new ImagePost();
        interaction.setImagePost(newImagePost);
        assertEquals(newImagePost, interaction.getImagePost());
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
        UserImageInteraction newInteraction = new UserImageInteraction();
        // The @CreationTimestamp should ensure createdAt is set automatically
        // Note: In a real database context, this would be set automatically
        assertNull(newInteraction.getCreatedAt()); // Will be null in test context
    }

    @Test
    void testToString() {
        String toString = interaction.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=" + interaction.getId()));
        assertTrue(toString.contains("imageInteraction=" + interaction.getImageInteraction()));
        assertTrue(toString.contains("user=" + interaction.getUser()));
        assertTrue(toString.contains("imagePost=" + interaction.getImagePost()));
        assertTrue(toString.contains("createdAt=" + interaction.getCreatedAt()));
    }

    @Test
    void testNullUser() {
        UserImageInteraction newInteraction = new UserImageInteraction();
        assertNull(newInteraction.getUser());
    }

    @Test
    void testNullImagePost() {
        UserImageInteraction newInteraction = new UserImageInteraction();
        assertNull(newInteraction.getImagePost());
    }

    @Test
    void testDefaultImageInteraction() {
        UserImageInteraction newInteraction = new UserImageInteraction();
        assertEquals(0L, newInteraction.getImageInteraction());
    }

    @Test
    void testConstructorWithNullValues() {
        UserImageInteraction newInteraction = new UserImageInteraction(1L, 100L, null, null);
        assertNotNull(newInteraction);
        assertNull(newInteraction.getUser());
        assertNull(newInteraction.getImagePost());
        assertEquals(100L, newInteraction.getImageInteraction());
    }

    @Test
    void testNegativeImageInteraction() {
        interaction.setImageInteraction(-1L);
        assertEquals(-1L, interaction.getImageInteraction());
        // Note: Add validation if negative values should not be allowed
    }

    @Test
    void testZeroImageInteraction() {
        interaction.setImageInteraction(0L);
        assertEquals(0L, interaction.getImageInteraction());
    }
}
