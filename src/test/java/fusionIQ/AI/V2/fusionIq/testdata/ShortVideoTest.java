package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.ShortVideo;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.VideoComment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.*;

class ShortVideoTest {

    private ShortVideo shortVideo;
    private User mockUser;
    private List<VideoComment> mockComments;
    private Set<User> mockLikedUsers;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockComments = new ArrayList<>();
        mockLikedUsers = new HashSet<>();
        testDateTime = LocalDateTime.now();

        shortVideo = new ShortVideo(
                1L,
                "Test Video",
                "Entertainment",
                "test-key",
                "https://example.com/video",
                "Test Description",
                testDateTime,
                testDateTime,
                "English",
                mockUser,
                mockComments,
                mockLikedUsers,
                100,
                50,
                1000,
                "test-tag",
                "2:30"
        );
    }

    @Test
    void testDefaultConstructor() {
        ShortVideo newVideo = new ShortVideo();
        assertNotNull(newVideo);
        assertNotNull(newVideo.getCreatedAt());
        assertNotNull(newVideo.getUpdatedDate());
    }

    @Test
    void testParameterizedConstructor() {
        assertNotNull(shortVideo);
        assertEquals(1L, shortVideo.getId());
        assertEquals("Test Video", shortVideo.getShortVideoTitle());
        assertEquals("Entertainment", shortVideo.getCategory());
        assertEquals("test-key", shortVideo.getS3Key());
        assertEquals("https://example.com/video", shortVideo.getS3Url());
        assertEquals("Test Description", shortVideo.getShortVideoDescription());
        assertEquals("English", shortVideo.getLanguage());
        assertEquals(mockUser, shortVideo.getUser());
        assertEquals(mockComments, shortVideo.getVideoComments());
        assertEquals(mockLikedUsers, shortVideo.getLikedByUsers());
        assertEquals(100, shortVideo.getShortVideoLikes());
        assertEquals(50, shortVideo.getShortVideoShares());
        assertEquals(1000, shortVideo.getShortVideoViews());
        assertEquals("test-tag", shortVideo.getTag());
        assertEquals("2:30", shortVideo.getShortVideoDuration());
    }

    @Test
    void testSetAndGetId() {
        shortVideo.setId(2L);
        assertEquals(2L, shortVideo.getId());
    }

    @Test
    void testSetAndGetShortVideoTitle() {
        shortVideo.setShortVideoTitle("New Title");
        assertEquals("New Title", shortVideo.getShortVideoTitle());
    }

    @Test
    void testSetAndGetS3Key() {
        shortVideo.setS3Key("new-key");
        assertEquals("new-key", shortVideo.getS3Key());
    }

    @Test
    void testSetAndGetS3Url() {
        shortVideo.setS3Url("https://example.com/new-video");
        assertEquals("https://example.com/new-video", shortVideo.getS3Url());
    }

    @Test
    void testSetAndGetDescription() {
        shortVideo.setShortVideoDescription("New Description");
        assertEquals("New Description", shortVideo.getShortVideoDescription());
    }

    @Test
    void testSetAndGetLanguage() {
        shortVideo.setLanguage("Spanish");
        assertEquals("Spanish", shortVideo.getLanguage());
    }

    @Test
    void testSetAndGetCategory() {
        shortVideo.setCategory("Education");
        assertEquals("Education", shortVideo.getCategory());
    }

    @Test
    void testSetAndGetLikeTimestamps() {
        Map<Long, LocalDateTime> timestamps = new HashMap<>();
        timestamps.put(1L, testDateTime);
        shortVideo.setLikeTimestamps(timestamps);
        assertEquals(timestamps, shortVideo.getLikeTimestamps());
    }

    @Test
    void testLikedByUsersCollection() {
        Set<User> newUsers = new HashSet<>();
        User newUser = new User();
        newUsers.add(newUser);
        shortVideo.setLikedByUsers(newUsers);
        assertEquals(newUsers, shortVideo.getLikedByUsers());
        assertTrue(shortVideo.getLikedByUsers().contains(newUser));
    }

    @Test
    void testVideoCommentsCollection() {
        List<VideoComment> newComments = new ArrayList<>();
        VideoComment comment = new VideoComment();
        newComments.add(comment);
        shortVideo.setVideoComments(newComments);
        assertEquals(newComments, shortVideo.getVideoComments());
        assertTrue(shortVideo.getVideoComments().contains(comment));
    }

    @Test
    void testSetAndGetMetrics() {
        shortVideo.setShortVideoLikes(200);
        shortVideo.setShortVideoShares(100);
        shortVideo.setShortVideoViews(2000);
        assertEquals(200, shortVideo.getShortVideoLikes());
        assertEquals(100, shortVideo.getShortVideoShares());
        assertEquals(2000, shortVideo.getShortVideoViews());
    }

    @Test
    void testSetAndGetDuration() {
        shortVideo.setShortVideoDuration("3:45");
        assertEquals("3:45", shortVideo.getShortVideoDuration());
    }

    @Test
    void testToString() {
        String toString = shortVideo.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=" + shortVideo.getId()));
        assertTrue(toString.contains("shortVideoTitle='" + shortVideo.getShortVideoTitle() + "'"));
        assertTrue(toString.contains("category='" + shortVideo.getCategory() + "'"));
    }

    @Test
    void testNegativeMetrics() {
        shortVideo.setShortVideoLikes(-1);
        shortVideo.setShortVideoShares(-1);
        shortVideo.setShortVideoViews(-1);
        assertEquals(-1, shortVideo.getShortVideoLikes());
        assertEquals(-1, shortVideo.getShortVideoShares());
        assertEquals(-1, shortVideo.getShortVideoViews());
        // Note: Add validation if negative values should not be allowed
    }
}
