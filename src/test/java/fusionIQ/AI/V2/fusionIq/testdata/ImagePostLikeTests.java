package fusionIQ.AI.V2.fusionIq.testdata;


import fusionIQ.AI.V2.fusionIq.data.ImagePost;
import fusionIQ.AI.V2.fusionIq.data.ImagePostLike;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImagePostLikeTests {

    @Test
    void testImagePostLikeCreation() {
        // Arrange
        User user = new User();
        user.setId(1L); // Set user ID
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L); // Set image post ID

        // Act
        ImagePostLike imagePostLike = new ImagePostLike(user, imagePost);

        // Assert
        assertThat(imagePostLike).isNotNull();
        assertThat(imagePostLike.getUser()).isEqualTo(user);
        assertThat(imagePostLike.getImagePost()).isEqualTo(imagePost);
    }

    @Test
    void testSetAndGetUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);
        ImagePostLike imagePostLike = new ImagePostLike(user, imagePost);

        // Act
        User newUser = new User();
        newUser.setId(2L);
        imagePostLike.setUser(newUser);

        // Assert
        assertThat(imagePostLike.getUser()).isEqualTo(newUser);
    }

    @Test
    void testSetAndGetImagePost() {
        // Arrange
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);
        ImagePostLike imagePostLike = new ImagePostLike(user, imagePost);

        // Act
        ImagePost newImagePost = new ImagePost();
        newImagePost.setId(2L);
        imagePostLike.setImagePost(newImagePost);

        // Assert
        assertThat(imagePostLike.getImagePost()).isEqualTo(newImagePost);
    }

    @Test
    void testToStringMethod() {
        // Arrange
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);
        ImagePostLike imagePostLike = new ImagePostLike(user, imagePost);

        // Act
        String result = imagePostLike.toString();

        // Assert
        assertThat(result).contains("ImagePostLike");
        assertThat(result).contains("id=" + imagePostLike.getId());
        assertThat(result).contains("user=" + user.toString());
        assertThat(result).contains("imagePost=" + imagePost.toString());
    }

    @Test
    void testIdSetterAndGetter() {
        // Arrange
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);
        ImagePostLike imagePostLike = new ImagePostLike(user, imagePost);

        // Act
        imagePostLike.setId(100L);

        // Assert
        assertThat(imagePostLike.getId()).isEqualTo(100L);
    }
}

