package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.LongVideo;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.VideoComment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LongVideoTest {

    private LongVideo longVideo;

    @BeforeEach
    void setUp() {
        longVideo = new LongVideo();
    }

    @Test
    void testLongVideoDefaultValues() {
        assertThat(longVideo.getCreatedAt()).isNotNull();
        assertThat(longVideo.getUpdatedDate()).isNotNull();
        assertThat(longVideo.getVideoComments()).isNotNull();
        assertThat(longVideo.getLikedByUsers()).isNotNull();
        assertThat(longVideo.getLongVideoLikes()).isEqualTo(0);
        assertThat(longVideo.getLongVideoShares()).isEqualTo(0);
        assertThat(longVideo.getLongVideoViews()).isEqualTo(0);
    }

    @Test
    void testSetAndGetLongVideoTitle() {
        longVideo.setLongVideoTitle("Test Long Video");
        assertThat(longVideo.getLongVideoTitle()).isEqualTo("Test Long Video");
    }

    @Test
    void testSetAndGetLongVideoDescription() {
        longVideo.setLongVideoDescription("A long video for testing purposes");
        assertThat(longVideo.getLongVideoDescription()).isEqualTo("A long video for testing purposes");
    }

    @Test
    void testSetAndGetS3Key() {
        longVideo.setS3Key("test/s3/key");
        assertThat(longVideo.getS3Key()).isEqualTo("test/s3/key");
    }

    @Test
    void testSetAndGetS3Url() {
        longVideo.setS3Url("https://s3-url/test");
        assertThat(longVideo.getS3Url()).isEqualTo("https://s3-url/test");
    }

    @Test
    void testSetAndGetLanguage() {
        longVideo.setLanguage("English");
        assertThat(longVideo.getLanguage()).isEqualTo("English");
    }

    @Test
    void testSetAndGetUser() {
        User user = new User();
        user.setId(1L); // Set a valid user ID
        longVideo.setUser(user);
        assertThat(longVideo.getUser()).isEqualTo(user);
    }

    @Test
    void testAddVideoComment() {
        VideoComment comment = new VideoComment();
        comment.setVideoCommentContent("Great video!");
        longVideo.getVideoComments().add(comment);

        assertThat(longVideo.getVideoComments()).hasSize(1);
        assertThat(longVideo.getVideoComments().get(0).getVideoCommentContent()).isEqualTo("Great video!");
    }

    @Test
    void testAddLike() {
        User user = new User();
        user.setId(1L); // Set a valid user ID
        longVideo.getLikedByUsers().add(user);

        assertThat(longVideo.getLikedByUsers()).contains(user);
    }

    @Test
    void testSetLongVideoLikes() {
        longVideo.setLongVideoLikes(10);
        assertThat(longVideo.getLongVideoLikes()).isEqualTo(10);
    }

    @Test
    void testSetLongVideoShares() {
        longVideo.setLongVideoShares(5);
        assertThat(longVideo.getLongVideoShares()).isEqualTo(5);
    }

    @Test
    void testSetLongVideoViews() {
        longVideo.setLongVideoViews(100);
        assertThat(longVideo.getLongVideoViews()).isEqualTo(100);
    }

    @Test
    void testSetTag() {
        longVideo.setTag("Education");
        assertThat(longVideo.getTag()).isEqualTo("Education");
    }

    @Test
    void testSetUpdatedDate() {
        LocalDateTime now = LocalDateTime.now();
        longVideo.setUpdatedDate(now);
        assertThat(longVideo.getUpdatedDate()).isEqualTo(now);
    }

    @Test
    public void testLongVideo() {
        LongVideo longVideo = new LongVideo();
        longVideo.setLongVideoTitle("Test Long Video");

        // Explicitly setting the fields to be checked
        longVideo.setS3Key(null); // Ensure this is null
        longVideo.setS3Url(null); // Ensure this is null
        longVideo.setLongVideoDescription(null); // Ensure this is null
        longVideo.setLanguage(null); // Ensure this is null
        longVideo.setTag(null); // Ensure this is null

        // Assert each field individually instead of using `equals`
        assertEquals("Test Long Video", longVideo.getLongVideoTitle());
        assertNull(longVideo.getS3Key());
        assertNull(longVideo.getS3Url());
        assertNull(longVideo.getLongVideoDescription());
        assertNull(longVideo.getLanguage());
        assertNull(longVideo.getTag());

        // Additional assertions as needed
        assertThat(longVideo.getVideoComments()).isEmpty();
        assertThat(longVideo.getLikedByUsers()).isEmpty();
        assertThat(longVideo.getLongVideoLikes()).isEqualTo(0);
        assertThat(longVideo.getLongVideoShares()).isEqualTo(0);
        assertThat(longVideo.getLongVideoViews()).isEqualTo(0);
    }
}
