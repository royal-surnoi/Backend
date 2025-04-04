package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Comment;
import fusionIQ.AI.V2.fusionIq.data.ImagePost;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserImageInteraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImagePostTest {

    private ImagePost imagePost;
    private User user;
    private byte[] testPhoto;
    private LocalDateTime testDate;
    private List<UserImageInteraction> interactions;
    private List<Comment> comments;

    @BeforeEach
    void setUp() {
        user = new User(); // Initialize with required User properties
        testPhoto = "test image".getBytes();
        testDate = LocalDateTime.now();
        interactions = new ArrayList<>();
        comments = new ArrayList<>();

        imagePost = new ImagePost();
    }

    @Test
    void testConstructorAndGetters() {
        ImagePost post = new ImagePost(1L, testPhoto, "Nature", user, interactions,
                testDate, testDate, 10, 2, 5, "Test Description", "TestTag", comments);

        assertEquals(1L, post.getId());
        assertArrayEquals(testPhoto, post.getPhoto());
        assertEquals("Nature", post.getCategory());
        assertEquals(user, post.getUser());
        assertEquals(interactions, post.getUserImageInteraction());
        assertEquals(testDate, post.getPostDate());
        assertEquals(testDate, post.getUpdatedDate());
        assertEquals(10, post.getImageLikeCount());
        assertEquals(2, post.getImageDislikes());
        assertEquals(5, post.getImageShareCount());
        assertEquals("Test Description", post.getImageDescription());
        assertEquals("TestTag", post.getTag());
        assertEquals(comments, post.getComments());
    }

    @Test
    void testSetId() {
        imagePost.setId(1L);
        assertEquals(1L, imagePost.getId());
    }

    @Test
    void testSetPhoto() {
        imagePost.setPhoto(testPhoto);
        assertArrayEquals(testPhoto, imagePost.getPhoto());
    }

    @Test
    void testSetCategory() {
        imagePost.setCategory("Nature");
        assertEquals("Nature", imagePost.getCategory());
    }

    @Test
    void testSetUser() {
        imagePost.setUser(user);
        assertEquals(user, imagePost.getUser());
    }

    @Test
    void testSetPostDate() {
        imagePost.setPostDate(testDate);
        assertEquals(testDate, imagePost.getPostDate());
    }

    @Test
    void testSetImageLikeCount() {
        imagePost.setImageLikeCount(10);
        assertEquals(10, imagePost.getImageLikeCount());
    }

    @Test
    void testSetImageDislikes() {
        imagePost.setImageDislikes(5);
        assertEquals(5, imagePost.getImageDislikes());
    }

    @Test
    void testSetImageShareCount() {
        imagePost.setImageShareCount(3);
        assertEquals(3, imagePost.getImageShareCount());
    }

    @Test
    void testSetImageDescription() {
        String description = "Test Description";
        imagePost.setImageDescription(description);
        assertEquals(description, imagePost.getImageDescription());
    }

    @Test
    void testSetUserImageInteraction() {
        List<UserImageInteraction> newInteractions = new ArrayList<>();
        imagePost.setUserImageInteraction(newInteractions);
        assertEquals(newInteractions, imagePost.getUserImageInteraction());
    }

    @Test
    void testSetTag() {
        String tag = "TestTag";
        imagePost.setTag(tag);
        assertEquals(tag, imagePost.getTag());
    }

    @Test
    void testSetUpdatedDate() {
        imagePost.setUpdatedDate(testDate);
        assertEquals(testDate, imagePost.getUpdatedDate());
    }

    @Test
    void testSetComments() {
        List<Comment> newComments = new ArrayList<>();
        imagePost.setComments(newComments);
        assertEquals(newComments, imagePost.getComments());
    }

    @Test
    void testToString() {
        imagePost.setId(1L);
        imagePost.setPhoto(testPhoto);
        imagePost.setUser(user);
        imagePost.setPostDate(testDate);
        imagePost.setImageLikeCount(10);
        imagePost.setImageDescription("Test Description");
        imagePost.setTag("TestTag");

        String toString = imagePost.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("imageDescription='Test Description'"));
        assertTrue(toString.contains("tag='TestTag'"));
        assertTrue(toString.contains("imageLikeCount=10"));
    }

    @Test
    void testNullableFields() {
        assertNull(imagePost.getImageDescription());
        assertNull(imagePost.getTag());
        assertNull(imagePost.getCategory());
    }
}