package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.ShortVideo;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserShortVideoInteraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserShortVideoInteractionTest {

    private UserShortVideoInteraction userShortVideoInteraction;
    private User user;
    private ShortVideo shortVideo;
    private LocalDateTime createdAt;

    @BeforeEach
    void setUp() {
        user = new User(); // Assuming User class exists
        shortVideo = new ShortVideo(); // Assuming ShortVideo class exists
        createdAt = LocalDateTime.now();

        userShortVideoInteraction = new UserShortVideoInteraction();
        userShortVideoInteraction.setId(1L);
        userShortVideoInteraction.setUser(user);
        userShortVideoInteraction.setShortVideo(shortVideo);
        userShortVideoInteraction.setShortVideoInteraction(5L);
        userShortVideoInteraction.setCreatedAt(createdAt);
    }

    @Test
    void testGetId() {
        assertEquals(1L, userShortVideoInteraction.getId());
    }

    @Test
    void testSetId() {
        userShortVideoInteraction.setId(2L);
        assertEquals(2L, userShortVideoInteraction.getId());
    }

    @Test
    void testGetUser() {
        assertEquals(user, userShortVideoInteraction.getUser());
    }

    @Test
    void testSetUser() {
        User newUser = new User();
        userShortVideoInteraction.setUser(newUser);
        assertEquals(newUser, userShortVideoInteraction.getUser());
    }

    @Test
    void testGetShortVideo() {
        assertEquals(shortVideo, userShortVideoInteraction.getShortVideo());
    }

    @Test
    void testSetShortVideo() {
        ShortVideo newShortVideo = new ShortVideo();
        userShortVideoInteraction.setShortVideo(newShortVideo);
        assertEquals(newShortVideo, userShortVideoInteraction.getShortVideo());
    }

    @Test
    void testGetShortVideoInteraction() {
        assertEquals(5L, userShortVideoInteraction.getShortVideoInteraction());
    }

    @Test
    void testSetShortVideoInteraction() {
        userShortVideoInteraction.setShortVideoInteraction(10L);
        assertEquals(10L, userShortVideoInteraction.getShortVideoInteraction());
    }

    @Test
    void testGetCreatedAt() {
        assertEquals(createdAt, userShortVideoInteraction.getCreatedAt());
    }

    @Test
    void testSetCreatedAt() {
        LocalDateTime newCreatedAt = LocalDateTime.now().plusDays(1);
        userShortVideoInteraction.setCreatedAt(newCreatedAt);
        assertEquals(newCreatedAt, userShortVideoInteraction.getCreatedAt());
    }

    @Test
    void testToString() {
        String expected = "UserShortVideoInteraction{" +
                "id=1" +
                ", user=" + user +
                ", shortVideo=" + shortVideo +
                ", shortVideoInteraction=5" +
                ", createdAt=" + createdAt +
                '}';
        assertEquals(expected, userShortVideoInteraction.toString());
    }

    @Test
    void testNoArgsConstructor() {
        UserShortVideoInteraction newInteraction = new UserShortVideoInteraction();
        assertNotNull(newInteraction);
    }

    @Test
    void testAllArgsConstructor() {
        UserShortVideoInteraction newInteraction = new UserShortVideoInteraction(1L, user, shortVideo, 5L);
        assertNotNull(newInteraction);
        assertEquals(1L, newInteraction.getId());
        assertEquals(user, newInteraction.getUser());
        assertEquals(shortVideo, newInteraction.getShortVideo());
        assertEquals(5L, newInteraction.getShortVideoInteraction());
    }
}