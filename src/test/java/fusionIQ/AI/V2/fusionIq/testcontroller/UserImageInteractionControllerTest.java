package fusionIQ.AI.V2.fusionIq.testcontroller;




import fusionIQ.AI.V2.fusionIq.controller.UserImageInteractionController;
import fusionIQ.AI.V2.fusionIq.data.UserImageInteraction;
import fusionIQ.AI.V2.fusionIq.service.UserImageInteractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserImageInteractionControllerTest {

    @InjectMocks
    private UserImageInteractionController controller;

    @Mock
    private UserImageInteractionService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUserImageInteraction() {
        UserImageInteraction interaction = new UserImageInteraction();
        when(service.createUserImageInteraction(any(UserImageInteraction.class))).thenReturn(interaction);

        ResponseEntity<UserImageInteraction> response = controller.createUserImageInteraction(interaction);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(interaction, response.getBody());
        verify(service, times(1)).createUserImageInteraction(any(UserImageInteraction.class));
    }

    @Test
    public void testGetUserImageInteractionById_Found() {
        UserImageInteraction interaction = new UserImageInteraction();
        when(service.getUserImageInteractionById(anyLong())).thenReturn(Optional.of(interaction));

        ResponseEntity<UserImageInteraction> response = controller.getUserImageInteractionById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(interaction, response.getBody());
        verify(service, times(1)).getUserImageInteractionById(anyLong());
    }

    @Test
    public void testGetUserImageInteractionById_NotFound() {
        when(service.getUserImageInteractionById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<UserImageInteraction> response = controller.getUserImageInteractionById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(service, times(1)).getUserImageInteractionById(anyLong());
    }

    @Test
    public void testGetUserImageInteractionsByUserId() {
        List<UserImageInteraction> interactions = Arrays.asList(new UserImageInteraction(), new UserImageInteraction());
        when(service.getUserImageInteractionsByUserId(anyLong())).thenReturn(interactions);

        ResponseEntity<List<UserImageInteraction>> response = controller.getUserImageInteractionsByUserId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).getUserImageInteractionsByUserId(anyLong());
    }

    @Test
    public void testGetUserImageInteractionsByImagePostId() {
        List<UserImageInteraction> interactions = Arrays.asList(new UserImageInteraction(), new UserImageInteraction());
        when(service.getUserImageInteractionsByImagePostId(anyLong())).thenReturn(interactions);

        ResponseEntity<List<UserImageInteraction>> response = controller.getUserImageInteractionsByImagePostId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).getUserImageInteractionsByImagePostId(anyLong());
    }

    @Test
    public void testGetAllUserImageInteractions() {
        List<UserImageInteraction> interactions = Arrays.asList(new UserImageInteraction(), new UserImageInteraction());
        when(service.getAllUserImageInteractions()).thenReturn(interactions);

        ResponseEntity<List<UserImageInteraction>> response = controller.getAllUserImageInteractions();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).getAllUserImageInteractions();
    }

    @Test
    public void testDeleteUserImageInteractionById() {
        doNothing().when(service).deleteUserImageInteractionById(anyLong());

        ResponseEntity<Void> response = controller.deleteUserImageInteractionById(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteUserImageInteractionById(anyLong());
    }
}

