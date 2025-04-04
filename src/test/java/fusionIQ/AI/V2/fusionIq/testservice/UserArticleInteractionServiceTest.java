package fusionIQ.AI.V2.fusionIq.testservice;


import fusionIQ.AI.V2.fusionIq.data.UserArticleInteraction;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.repository.UserArticleInteractionRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.repository.ArticlePostRepo;
import fusionIQ.AI.V2.fusionIq.service.UserArticleInteractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserArticleInteractionServiceTest {

    @InjectMocks
    private UserArticleInteractionService userArticleInteractionService;

    @Mock
    private UserArticleInteractionRepo userArticleInteractionRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ArticlePostRepo articlePostRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostByUserIdAndArticleId_Success() {
        Long userId = 1L;
        Long articleId = 1L;
        Long interaction = 5L;

        User user = new User();
        user.setId(userId);

        ArticlePost articlePost = new ArticlePost();
        articlePost.setId(articleId);

        UserArticleInteraction userArticleInteraction = new UserArticleInteraction();
        userArticleInteraction.setUser(user);
        userArticleInteraction.setArticlePost(articlePost);
        userArticleInteraction.setArticleInteraction(interaction);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(articlePostRepo.findById(articleId)).thenReturn(Optional.of(articlePost));
        when(userArticleInteractionRepo.save(any(UserArticleInteraction.class))).thenReturn(userArticleInteraction);

        UserArticleInteraction result = userArticleInteractionService.postByUserIdAndArticleId(userId, articleId, interaction);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(articleId, result.getArticlePost().getId());
        assertEquals(interaction, result.getArticleInteraction());
    }

    @Test
    void testPostByUserIdAndArticleId_UserNotFound() {
        Long userId = 1L;
        Long articleId = 1L;
        Long interaction = 5L;

        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        UserArticleInteraction result = userArticleInteractionService.postByUserIdAndArticleId(userId, articleId, interaction);

        assertNull(result);
    }

    @Test
    void testGetByUserId() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        UserArticleInteraction interaction1 = new UserArticleInteraction();
        interaction1.setUser(user);

        UserArticleInteraction interaction2 = new UserArticleInteraction();
        interaction2.setUser(user);

        List<UserArticleInteraction> interactions = Arrays.asList(interaction1, interaction2);

        when(userArticleInteractionRepo.findAll()).thenReturn(interactions);

        List<UserArticleInteraction> result = userArticleInteractionService.getByUserId(userId);

        assertEquals(2, result.size());
    }

    @Test
    void testGetByArticleId() {
        Long articleId = 1L;

        ArticlePost articlePost = new ArticlePost();
        articlePost.setId(articleId);

        UserArticleInteraction interaction1 = new UserArticleInteraction();
        interaction1.setArticlePost(articlePost);

        UserArticleInteraction interaction2 = new UserArticleInteraction();
        interaction2.setArticlePost(articlePost);

        List<UserArticleInteraction> interactions = Arrays.asList(interaction1, interaction2);

        when(userArticleInteractionRepo.findAll()).thenReturn(interactions);

        List<UserArticleInteraction> result = userArticleInteractionService.getByArticleId(articleId);

        assertEquals(2, result.size());
    }

    @Test
    void testGetAll() {
        List<UserArticleInteraction> interactions = Arrays.asList(new UserArticleInteraction(), new UserArticleInteraction());

        when(userArticleInteractionRepo.findAll()).thenReturn(interactions);

        List<UserArticleInteraction> result = userArticleInteractionService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    void testDeleteById() {
        Long id = 1L;

        doNothing().when(userArticleInteractionRepo).deleteById(id);

        userArticleInteractionService.deleteById(id);

        verify(userArticleInteractionRepo, times(1)).deleteById(id);
    }
}


