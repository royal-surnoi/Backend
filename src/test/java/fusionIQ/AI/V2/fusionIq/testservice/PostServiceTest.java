package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.Posts;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.PostRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepo postRepo;

    @Mock
    private UserRepo userRepo;

    private User user;
    private Posts post;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("John Doe");

        post = new Posts();
        post.setId(1L);
        post.setUser(user);
        post.setPostDate(LocalDateTime.now());
    }


    @Test
    public void testCreateImagePost_UserNotFound() {
        byte[] photoBytes = new byte[]{1, 2, 3};

        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            postService.createImagePost(user.getId(), photoBytes);
        });

        assertEquals("User not found", exception.getMessage());

        verify(postRepo, times(0)).save(any(Posts.class));
    }


    @Test
    public void testCreateArticlePost_UserNotFound() {
        String articleContent = "This is a test article.";

        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            postService.createArticlePost(user.getId(), articleContent);
        });

        assertEquals("User not found", exception.getMessage());

        verify(postRepo, times(0)).save(any(Posts.class));
    }
}

