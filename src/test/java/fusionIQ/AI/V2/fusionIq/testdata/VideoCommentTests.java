package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.LongVideo;
import fusionIQ.AI.V2.fusionIq.data.ShortVideo;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.VideoComment;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class VideoCommentTests {

    @Test
    void testDefaultConstructor() {
        VideoComment videoComment = new VideoComment();

        // Verifying that the default constructor initializes fields correctly
        assertThat(videoComment.getId()).isNull();
        assertThat(videoComment.getVideoCommentContent()).isNull();
        assertThat(videoComment.getUser()).isNull();
        assertThat(videoComment.getShortVideo()).isNull();
        assertThat(videoComment.getLongVideo()).isNull();
        assertThat(videoComment.getParentComment()).isNull();
        assertThat(videoComment.getCreatedAt()).isNull();
        assertThat(videoComment.getLikes()).isEqualTo(0); // Default value for int
    }

    @Test
    void testSettersAndGetters() {
        VideoComment videoComment = new VideoComment();
        User user = new User();
        ShortVideo shortVideo = new ShortVideo();
        LongVideo longVideo = new LongVideo();
        VideoComment parentComment = new VideoComment();
        LocalDateTime createdAt = LocalDateTime.now();

        videoComment.setId(1L);
        videoComment.setVideoCommentContent("This is a test comment.");
        videoComment.setUser(user);
        videoComment.setShortVideo(shortVideo);
        videoComment.setLongVideo(longVideo);
        videoComment.setParentComment(parentComment);
        videoComment.setCreatedAt(createdAt);
        videoComment.setLikes(10);

        // Verifying the setters and getters
        assertThat(videoComment.getId()).isEqualTo(1L);
        assertThat(videoComment.getVideoCommentContent()).isEqualTo("This is a test comment.");
        assertThat(videoComment.getUser()).isEqualTo(user);
        assertThat(videoComment.getShortVideo()).isEqualTo(shortVideo);
        assertThat(videoComment.getLongVideo()).isEqualTo(longVideo);
        assertThat(videoComment.getParentComment()).isEqualTo(parentComment);
        assertThat(videoComment.getCreatedAt()).isEqualTo(createdAt);
        assertThat(videoComment.getLikes()).isEqualTo(10);
    }

    @Test
    void testIncrementLikes() {
        VideoComment videoComment = new VideoComment();
        videoComment.setLikes(5);

        // Incrementing likes
        videoComment.incrementLikes();

        // Verifying the likes increment
        assertThat(videoComment.getLikes()).isEqualTo(6);
    }

    @Test
    void testToString() {
        User user = new User();
        ShortVideo shortVideo = new ShortVideo();
        LongVideo longVideo = new LongVideo();
        VideoComment parentComment = new VideoComment();
        LocalDateTime createdAt = LocalDateTime.now();

        VideoComment videoComment = new VideoComment();
        videoComment.setId(1L);
        videoComment.setVideoCommentContent("This is a test comment.");
        videoComment.setUser(user);
        videoComment.setShortVideo(shortVideo);
        videoComment.setLongVideo(longVideo);
        videoComment.setParentComment(parentComment);
        videoComment.setCreatedAt(createdAt);

        String expectedToString = "VideoComment{" +
                "id=" + videoComment.getId() +
                ", videoCommentContent='" + videoComment.getVideoCommentContent() + '\'' +
                ", user=" + videoComment.getUser() +
                ", shortVideo=" + videoComment.getShortVideo() +
                ", longVideo=" + videoComment.getLongVideo() +
                ", parentComment=" + videoComment.getParentComment() +
                ", createdAt=" + videoComment.getCreatedAt() +
                '}';

        // Verifying the toString method
        assertThat(videoComment.toString()).isEqualTo(expectedToString);
    }
}
