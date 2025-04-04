package fusionIQ.AI.V2.fusionIq.testdata;


import fusionIQ.AI.V2.fusionIq.data.Posts;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PostsTests {

    @Test
    void testPostCreation() {
        User user = new User();
        user.setId(1L); // Set user ID or any required fields

        LocalDateTime now = LocalDateTime.now();

        Posts post = new Posts();
        post.setId(1L);
        post.setArticle("This is a test article");
        post.setPhoto(new byte[]{1, 2, 3}); // Example photo bytes
        post.setUser(user);
        post.setPostDate(now);

        assertThat(post).isNotNull();
        assertThat(post.getId()).isEqualTo(1L);
        assertThat(post.getArticle()).isEqualTo("This is a test article");
        assertThat(post.getUser()).isEqualTo(user);
        assertThat(post.getPostDate()).isEqualTo(now);
    }

    @Test
    void testPostDefaultConstructor() {
        Posts post = new Posts();

        assertThat(post.getId()).isEqualTo(0); // Default value for long
        assertThat(post.getArticle()).isNull();
        assertThat(post.getPhoto()).isNull();
        assertThat(post.getUser()).isNull();
        assertThat(post.getPostDate()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        Posts post = new Posts();
        User user = new User();
        user.setId(1L); // Set user ID or any required fields

        post.setId(2L);
        post.setArticle("New Article");
        post.setPhoto(new byte[]{4, 5, 6});
        post.setUser(user);
        post.setPostDate(LocalDateTime.now());

        assertThat(post.getId()).isEqualTo(2L);
        assertThat(post.getArticle()).isEqualTo("New Article");
        assertThat(post.getPhoto()).isEqualTo(new byte[]{4, 5, 6});
        assertThat(post.getUser()).isEqualTo(user);
        assertThat(post.getPostDate()).isNotNull();
    }

    @Test
    void testToString() {
        User user = new User();
        user.setId(1L); // Set user ID or any required fields
        LocalDateTime now = LocalDateTime.now();

        Posts post = new Posts();
        post.setId(3L);
        post.setArticle("ToString Test");
        post.setPhoto(new byte[]{7, 8, 9});
        post.setUser(user);
        post.setPostDate(now);

        String expectedString = "Posts{id=3, photo=[7, 8, 9], article='ToString Test', user=" + user + ", postDate=" + now + '}';
        assertThat(post.toString()).isEqualTo(expectedString);
    }

    @Test
    void testPostWithNullFields() {
        Posts post = new Posts();

        post.setArticle(null);
        post.setPhoto(null);
        post.setUser(null);
        post.setPostDate(null);

        assertThat(post.getArticle()).isNull();
        assertThat(post.getPhoto()).isNull();
        assertThat(post.getUser()).isNull();
        assertThat(post.getPostDate()).isNull();
    }
}

