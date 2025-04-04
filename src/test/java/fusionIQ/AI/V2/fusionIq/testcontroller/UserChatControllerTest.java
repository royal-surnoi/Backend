package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.UserChatController;
import fusionIQ.AI.V2.fusionIq.service.UserChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserChatControllerTest {

    @InjectMocks
    private UserChatController userChatController;

    @Mock
    private UserChatService userChatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testChat_Success() {
        // Arrange
        int id = 1;
        String userQuery = "What is the weather today?";
        String expectedResponse = "It's sunny today.";

        // Mocking the service method
        when(userChatService.handleUserQuery(anyInt(), anyString())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<?> responseEntity = userChatController.chat(id, userQuery);

        // Assert
        assertEquals(200, responseEntity.getStatusCode().value()); // Updated line
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void testChat_InternalServerError() {
        // Arrange
        int id = 1;
        String userQuery = "What is the weather today?";

        // Mocking the service method to throw an exception
        when(userChatService.handleUserQuery(anyInt(), anyString())).thenThrow(new RuntimeException("Some error occurred"));

        // Act
        ResponseEntity<?> responseEntity = userChatController.chat(id, userQuery);

        // Assert
        assertEquals(500, responseEntity.getStatusCode().value()); // Updated line
        assertEquals("Internal Server Error: Some error occurred", responseEntity.getBody());
    }
}
