package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.PostController;
import fusionIQ.AI.V2.fusionIq.data.Posts;
import fusionIQ.AI.V2.fusionIq.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    private Posts post;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        post = new Posts();
        post.setId(1L);
    }

    @Test
    public void testCreateImagePost() throws IOException {
        long userId = 1L;
        byte[] photoBytes = new byte[]{1, 2, 3};
        MultipartFile photo = new MockMultipartFile("photo", "test.jpg", "image/jpeg", photoBytes);

        when(postService.createImagePost(userId, photoBytes)).thenReturn(post);

        Posts createdPost = postController.createImagePost(userId, photo);

        assertNotNull(createdPost);
        assertEquals(post, createdPost);

        verify(postService, times(1)).createImagePost(userId, photoBytes);
    }

    @Test
    public void testCreateArticlePost() {
        long userId = 1L;
        String articleContent = "This is a test article.";

        when(postService.createArticlePost(userId, articleContent)).thenReturn(post);

        Posts createdPost = postController.createArticlePost(userId, articleContent);

        assertNotNull(createdPost);
        assertEquals(post, createdPost);

        verify(postService, times(1)).createArticlePost(userId, articleContent);
    }
}
