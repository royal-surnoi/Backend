package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AiFeedTests {

    private AiFeed aiFeed;
    private User user;
    private ArticlePost articlePost;
    private ImagePost imagePost;
    private ShortVideo shortVideo;
    private LongVideo longVideo;

    @BeforeEach
    void setUp() {
        user = new User(); // Assuming User class is defined elsewhere
        user.setId(1L);

        articlePost = new ArticlePost(); // Assuming ArticlePost class is defined elsewhere
        articlePost.setId(1L);

        imagePost = new ImagePost(); // Assuming ImagePost class is defined elsewhere
        imagePost.setId(1L);

        shortVideo = new ShortVideo(); // Assuming ShortVideo class is defined elsewhere
        shortVideo.setId(1L);

        longVideo = new LongVideo(); // Assuming LongVideo class is defined elsewhere
        longVideo.setId(1L);

        aiFeed = new AiFeed();
    }

    @Test
    void testDefaultConstructor() {
        assertThat(aiFeed.getCreatedAt()).isNotNull();
        assertThat(aiFeed.getFeedType()).isNull();
        assertThat(aiFeed.isFeedInteraction()).isFalse();
        assertThat(aiFeed.getUser()).isNull();
        assertThat(aiFeed.getArticlePost()).isNull();
        assertThat(aiFeed.getImagePost()).isNull();
        assertThat(aiFeed.getShortVideo()).isNull();
        assertThat(aiFeed.getLongVideo()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        LocalDateTime now = LocalDateTime.now();
        AiFeed aiFeed = new AiFeed(1L, "Article", true, now, user, articlePost, imagePost, shortVideo, longVideo);

        assertThat(aiFeed.getId()).isEqualTo(1L);
        assertThat(aiFeed.getFeedType()).isEqualTo("Article");
        assertThat(aiFeed.isFeedInteraction()).isTrue();
        assertThat(aiFeed.getCreatedAt()).isEqualTo(now);
        assertThat(aiFeed.getUser()).isEqualTo(user);
        assertThat(aiFeed.getArticlePost()).isEqualTo(articlePost);
        assertThat(aiFeed.getImagePost()).isEqualTo(imagePost);
        assertThat(aiFeed.getShortVideo()).isEqualTo(shortVideo);
        assertThat(aiFeed.getLongVideo()).isEqualTo(longVideo);
    }

    @Test
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        aiFeed.setId(2L);
        aiFeed.setFeedType("Video");
        aiFeed.setFeedInteraction(false);
        aiFeed.setCreatedAt(now);
        aiFeed.setUser(user);
        aiFeed.setArticlePost(articlePost);
        aiFeed.setImagePost(imagePost);
        aiFeed.setShortVideo(shortVideo);
        aiFeed.setLongVideo(longVideo);

        assertThat(aiFeed.getId()).isEqualTo(2L);
        assertThat(aiFeed.getFeedType()).isEqualTo("Video");
        assertThat(aiFeed.isFeedInteraction()).isFalse();
        assertThat(aiFeed.getCreatedAt()).isEqualTo(now);
        assertThat(aiFeed.getUser()).isEqualTo(user);
        assertThat(aiFeed.getArticlePost()).isEqualTo(articlePost);
        assertThat(aiFeed.getImagePost()).isEqualTo(imagePost);
        assertThat(aiFeed.getShortVideo()).isEqualTo(shortVideo);
        assertThat(aiFeed.getLongVideo()).isEqualTo(longVideo);
    }

    @Test
    void testToString() {
        aiFeed.setId(1L);
        aiFeed.setFeedType("Image");
        aiFeed.setFeedInteraction(true);
        aiFeed.setUser(user);
        aiFeed.setArticlePost(articlePost);
        aiFeed.setImagePost(imagePost);
        aiFeed.setShortVideo(shortVideo);
        aiFeed.setLongVideo(longVideo);

        String expected = "AiFeed{id=1, feedType='Image', feedInteraction=true, createdAt=" + aiFeed.getCreatedAt() +
                ", user=" + user + ", articlePost=" + articlePost + ", imagePost=" + imagePost +
                ", shortVideo=" + shortVideo + ", longVideo=" + longVideo + "}";
        assertThat(aiFeed.toString()).isEqualTo(expected);
    }
}
