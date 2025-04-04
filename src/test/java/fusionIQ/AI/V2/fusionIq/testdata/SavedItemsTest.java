package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

class SavedItemsTest {

    @Test
    void testDefaultConstructor() {
        SavedItems savedItems = new SavedItems();
        assertThat(savedItems.getSavedAt()).isNotNull(); // Should be initialized to now
    }

    @Test
    void testImagePostConstructor() {
        User user = new User();
        ImagePost imagePost = new ImagePost(); // Assuming default constructor is available

        SavedItems savedItems = new SavedItems(user, imagePost);

        assertThat(savedItems.getUser()).isEqualTo(user);
        assertThat(savedItems.getImagePost()).isEqualTo(imagePost);
        assertThat(savedItems.getArticlePost()).isNull();
        assertThat(savedItems.getShortVideo()).isNull();
        assertThat(savedItems.getLongVideo()).isNull();
        assertThat(savedItems.getSavedAt()).isNotNull();
    }

    @Test
    void testArticlePostConstructor() {
        User user = new User();
        ArticlePost articlePost = new ArticlePost(); // Assuming default constructor is available

        SavedItems savedItems = new SavedItems(user, articlePost);

        assertThat(savedItems.getUser()).isEqualTo(user);
        assertThat(savedItems.getArticlePost()).isEqualTo(articlePost);
        assertThat(savedItems.getImagePost()).isNull();
        assertThat(savedItems.getShortVideo()).isNull();
        assertThat(savedItems.getLongVideo()).isNull();
        assertThat(savedItems.getSavedAt()).isNotNull();
    }

    @Test
    void testShortVideoConstructor() {
        User user = new User();
        ShortVideo shortVideo = new ShortVideo(); // Assuming default constructor is available

        SavedItems savedItems = new SavedItems(user, shortVideo);

        assertThat(savedItems.getUser()).isEqualTo(user);
        assertThat(savedItems.getShortVideo()).isEqualTo(shortVideo);
        assertThat(savedItems.getImagePost()).isNull();
        assertThat(savedItems.getArticlePost()).isNull();
        assertThat(savedItems.getLongVideo()).isNull();
        assertThat(savedItems.getSavedAt()).isNotNull();
    }

    @Test
    void testLongVideoConstructor() {
        User user = new User();
        LongVideo longVideo = new LongVideo(); // Assuming default constructor is available

        SavedItems savedItems = new SavedItems(user, longVideo);

        assertThat(savedItems.getUser()).isEqualTo(user);
        assertThat(savedItems.getLongVideo()).isEqualTo(longVideo);
        assertThat(savedItems.getImagePost()).isNull();
        assertThat(savedItems.getArticlePost()).isNull();
        assertThat(savedItems.getShortVideo()).isNull();
        assertThat(savedItems.getSavedAt()).isNotNull();
    }

    @Test
    void testSettersAndGetters() {
        SavedItems savedItems = new SavedItems();

        User user = new User();
        ImagePost imagePost = new ImagePost();
        ArticlePost articlePost = new ArticlePost();
        ShortVideo shortVideo = new ShortVideo();
        LongVideo longVideo = new LongVideo();
        LocalDateTime savedAt = LocalDateTime.now();

        savedItems.setUser(user);
        savedItems.setImagePost(imagePost);
        savedItems.setArticlePost(articlePost);
        savedItems.setShortVideo(shortVideo);
        savedItems.setLongVideo(longVideo);
        savedItems.setSavedAt(savedAt);

        assertThat(savedItems.getUser()).isEqualTo(user);
        assertThat(savedItems.getImagePost()).isEqualTo(imagePost);
        assertThat(savedItems.getArticlePost()).isEqualTo(articlePost);
        assertThat(savedItems.getShortVideo()).isEqualTo(shortVideo);
        assertThat(savedItems.getLongVideo()).isEqualTo(longVideo);
        assertThat(savedItems.getSavedAt()).isEqualTo(savedAt);
    }

    @Test
    void testToString() {
        User user = new User();
        ImagePost imagePost = new ImagePost();
        LocalDateTime savedAt = LocalDateTime.now();

        SavedItems savedItems = new SavedItems(user, imagePost);

        String expectedString = "SavedItems{" +
                "id=" + savedItems.getId() + // id will be generated, so it should be checked properly if a specific value is required
                ", user=" + user +
                ", imagePost=" + imagePost +
                ", articlePost=null" +
                ", shortVideo=null" +
                ", longVideo=null" +
                ", savedAt=" + savedAt +
                '}';
        assertThat(savedItems.toString()).contains("SavedItems{");
    }
}
