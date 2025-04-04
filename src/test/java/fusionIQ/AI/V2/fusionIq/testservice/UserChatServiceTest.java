package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.service.UserChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserChatServiceTest {

    @InjectMocks
    private UserChatService userChatService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleUserQuery_Success() {
        // Arrange
        int id = 1;
        String userQuery = "What is the weather today?";
        String expectedResponse = "It's sunny today.";

        // Mocking the response from the external API
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("response", expectedResponse);

        // Mocking the behavior of restTemplate.postForObject
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(mockResponse);

        // Act
        String actualResponse = userChatService.handleUserQuery(id, userQuery);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testHandleUserQuery_NullResponse() {
        // Arrange
        int id = 1;
        String userQuery = "What is the weather today?";

        // Mocking the behavior of restTemplate.postForObject to return null
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(null);

        // Act
        String actualResponse = userChatService.handleUserQuery(id, userQuery);

        // Assert
        assertEquals(null, actualResponse);
    }
}


