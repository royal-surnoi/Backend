package fusionIQ.AI.V2.fusionIq.testservice;


import fusionIQ.AI.V2.fusionIq.data.ShortVideo;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserShortVideoInteraction;
import fusionIQ.AI.V2.fusionIq.repository.UserShortVideoInteractionRepo;
import fusionIQ.AI.V2.fusionIq.service.UserShortVideoInteractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserShortVideoInteractionServiceTest {

    @InjectMocks
    private UserShortVideoInteractionService service;

    @Mock
    private UserShortVideoInteractionRepo repo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPostInteraction() {
        UserShortVideoInteraction interaction = new UserShortVideoInteraction();
        when(repo.save(any(UserShortVideoInteraction.class))).thenReturn(interaction);

        UserShortVideoInteraction result = service.post(1L, 1L, 100L);
        assertEquals(interaction, result);
        verify(repo, times(1)).save(any(UserShortVideoInteraction.class));
    }

    @Test
    public void testGetByUserId() {
        List<UserShortVideoInteraction> interactions = Arrays.asList(new UserShortVideoInteraction(), new UserShortVideoInteraction());
        when(repo.findByUserId(anyLong())).thenReturn(interactions);

        List<UserShortVideoInteraction> result = service.getByUserId(1L);
        assertEquals(2, result.size());
        verify(repo, times(1)).findByUserId(anyLong());
    }

    @Test
    public void testGetByShortVideoId() {
        List<UserShortVideoInteraction> interactions = Arrays.asList(new UserShortVideoInteraction(), new UserShortVideoInteraction());
        when(repo.findByShortVideoId(anyLong())).thenReturn(interactions);

        List<UserShortVideoInteraction> result = service.getByShortVideoId(1L);
        assertEquals(2, result.size());
        verify(repo, times(1)).findByShortVideoId(anyLong());
    }

    @Test
    public void testGetAll() {
        List<UserShortVideoInteraction> interactions = Arrays.asList(new UserShortVideoInteraction(), new UserShortVideoInteraction());
        when(repo.findAll()).thenReturn(interactions);

        List<UserShortVideoInteraction> result = service.getAll();
        assertEquals(2, result.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    public void testDeleteById() {
        doNothing().when(repo).deleteById(anyLong());

        service.deleteById(1L);
        verify(repo, times(1)).deleteById(anyLong());
    }
}

