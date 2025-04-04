package fusionIQ.AI.V2.fusionIq.testcontroller;


import fusionIQ.AI.V2.fusionIq.controller.UserArticleInteractionController;
import fusionIQ.AI.V2.fusionIq.data.UserArticleInteraction;
import fusionIQ.AI.V2.fusionIq.service.UserArticleInteractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserArticleInteractionControllerTest {

    @InjectMocks
    private UserArticleInteractionController userArticleInteractionController;

    @Mock
    private UserArticleInteractionService userArticleInteractionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostByUserIdAndArticleId() {
        Long userId = 1L;
        Long articleId = 1L;
        Long interaction = 5L;

        UserArticleInteraction userArticleInteraction = new UserArticleInteraction();
        userArticleInteraction.setArticleInteraction(interaction);

        when(userArticleInteractionService.postByUserIdAndArticleId(anyLong(), anyLong(), anyLong())).thenReturn(userArticleInteraction);

        UserArticleInteraction result = userArticleInteractionController.postByUserIdAndArticleId(userId, articleId, interaction);

        assertEquals(interaction, result.getArticleInteraction());
        verify(userArticleInteractionService, times(1)).postByUserIdAndArticleId(userId, articleId, interaction);
    }

    @Test
    void testGetByUserId() {
        Long userId = 1L;

        List<UserArticleInteraction> interactions = Arrays.asList(new UserArticleInteraction(), new UserArticleInteraction());

        when(userArticleInteractionService.getByUserId(anyLong())).thenReturn(interactions);

        List<UserArticleInteraction> result = userArticleInteractionController.getByUserId(userId);

        assertEquals(2, result.size());
        verify(userArticleInteractionService, times(1)).getByUserId(userId);
    }

    @Test
    void testGetByArticleId() {
        Long articleId = 1L;

        List<UserArticleInteraction> interactions = Arrays.asList(new UserArticleInteraction(), new UserArticleInteraction());

        when(userArticleInteractionService.getByArticleId(anyLong())).thenReturn(interactions);

        List<UserArticleInteraction> result = userArticleInteractionController.getByArticleId(articleId);

        assertEquals(2, result.size());
        verify(userArticleInteractionService, times(1)).getByArticleId(articleId);
    }

    @Test
    void testGetAll() {
        List<UserArticleInteraction> interactions = Arrays.asList(new UserArticleInteraction(), new UserArticleInteraction());

        when(userArticleInteractionService.getAll()).thenReturn(interactions);

        List<UserArticleInteraction> result = userArticleInteractionController.getAll();

        assertEquals(2, result.size());
        verify(userArticleInteractionService, times(1)).getAll();
    }

    @Test
    void testDeleteById() {
        Long id = 1L;

        doNothing().when(userArticleInteractionService).deleteById(id);

        userArticleInteractionController.deleteById(id);

        verify(userArticleInteractionService, times(1)).deleteById(id);
    }
}

