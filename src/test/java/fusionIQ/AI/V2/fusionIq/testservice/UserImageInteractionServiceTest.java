package fusionIQ.AI.V2.fusionIq.testservice;


import fusionIQ.AI.V2.fusionIq.data.UserImageInteraction;
import fusionIQ.AI.V2.fusionIq.repository.UserImageInteractionRepo;
import fusionIQ.AI.V2.fusionIq.service.UserImageInteractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserImageInteractionServiceTest {

    @InjectMocks
    private UserImageInteractionService service;

    @Mock
    private UserImageInteractionRepo repo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUserImageInteraction() {
        UserImageInteraction interaction = new UserImageInteraction();
        when(repo.save(any(UserImageInteraction.class))).thenReturn(interaction);

        UserImageInteraction result = service.createUserImageInteraction(interaction);
        assertEquals(interaction, result);
        verify(repo, times(1)).save(any(UserImageInteraction.class));
    }

    @Test
    public void testGetUserImageInteractionById() {
        UserImageInteraction interaction = new UserImageInteraction();
        when(repo.findById(anyLong())).thenReturn(Optional.of(interaction));

        Optional<UserImageInteraction> result = service.getUserImageInteractionById(1L);
        assertEquals(Optional.of(interaction), result);
        verify(repo, times(1)).findById(anyLong());
    }

    @Test
    public void testGetUserImageInteractionsByUserId() {
        List<UserImageInteraction> interactions = Arrays.asList(new UserImageInteraction(), new UserImageInteraction());
        when(repo.findByUserId(anyLong())).thenReturn(interactions);

        List<UserImageInteraction> result = service.getUserImageInteractionsByUserId(1L);
        assertEquals(2, result.size());
        verify(repo, times(1)).findByUserId(anyLong());
    }

    @Test
    public void testGetUserImageInteractionsByImagePostId() {
        List<UserImageInteraction> interactions = Arrays.asList(new UserImageInteraction(), new UserImageInteraction());
        when(repo.findByImagePostId(anyLong())).thenReturn(interactions);

        List<UserImageInteraction> result = service.getUserImageInteractionsByImagePostId(1L);
        assertEquals(2, result.size());
        verify(repo, times(1)).findByImagePostId(anyLong());
    }

    @Test
    public void testGetAllUserImageInteractions() {
        List<UserImageInteraction> interactions = Arrays.asList(new UserImageInteraction(), new UserImageInteraction());
        when(repo.findAll()).thenReturn(interactions);

        List<UserImageInteraction> result = service.getAllUserImageInteractions();
        assertEquals(2, result.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    public void testDeleteUserImageInteractionById() {
        doNothing().when(repo).deleteById(anyLong());

        service.deleteUserImageInteractionById(1L);
        verify(repo, times(1)).deleteById(anyLong());
    }
}
