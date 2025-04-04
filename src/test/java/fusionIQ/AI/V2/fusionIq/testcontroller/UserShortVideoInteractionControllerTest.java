package fusionIQ.AI.V2.fusionIq.testcontroller;


import fusionIQ.AI.V2.fusionIq.controller.UserShortVideoInteractionController;
import fusionIQ.AI.V2.fusionIq.data.UserShortVideoInteraction;
import fusionIQ.AI.V2.fusionIq.service.UserShortVideoInteractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserShortVideoInteractionControllerTest {

    @InjectMocks
    private UserShortVideoInteractionController controller;

    @Mock
    private UserShortVideoInteractionService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPostInteraction() {
        UserShortVideoInteraction interaction = new UserShortVideoInteraction();
        when(service.post(anyLong(), anyLong(), anyLong())).thenReturn(interaction);

        ResponseEntity<UserShortVideoInteraction> response = controller.postInteraction(1L, 1L, 100L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(interaction, response.getBody());
        verify(service, times(1)).post(anyLong(), anyLong(), anyLong());
    }

    @Test
    public void testGetByUserId() {
        List<UserShortVideoInteraction> interactions = Arrays.asList(new UserShortVideoInteraction(), new UserShortVideoInteraction());
        when(service.getByUserId(anyLong())).thenReturn(interactions);

        ResponseEntity<List<UserShortVideoInteraction>> response = controller.getByUserId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).getByUserId(anyLong());
    }

    @Test
    public void testGetByShortVideoId() {
        List<UserShortVideoInteraction> interactions = Arrays.asList(new UserShortVideoInteraction(), new UserShortVideoInteraction());
        when(service.getByShortVideoId(anyLong())).thenReturn(interactions);

        ResponseEntity<List<UserShortVideoInteraction>> response = controller.getByShortVideoId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).getByShortVideoId(anyLong());
    }

    @Test
    public void testGetAll() {
        List<UserShortVideoInteraction> interactions = Arrays.asList(new UserShortVideoInteraction(), new UserShortVideoInteraction());
        when(service.getAll()).thenReturn(interactions);

        ResponseEntity<List<UserShortVideoInteraction>> response = controller.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).getAll();
    }

    @Test
    public void testDeleteById() {
        doNothing().when(service).deleteById(anyLong());

        ResponseEntity<Void> response = controller.deleteById(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteById(anyLong());
    }
}
