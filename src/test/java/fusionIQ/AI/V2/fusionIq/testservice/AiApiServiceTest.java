package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.service.AiApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AiApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AiApiService aiApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testForwardRequest_Success() {
        String mockResponse = "{\"message\": \"success\"}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("key", "value");

        String response = aiApiService.forwardRequest("/testEndpoint", requestBody);

        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(restTemplate, times(1)).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testForwardRequest_Redirect() {
        String redirectUrl = "http://localhost:8000/redirectEndpoint";
        ResponseEntity<String> initialResponse = ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl)).build();
        String finalResponseBody = "{\"message\": \"redirected success\"}";
        ResponseEntity<String> finalResponse = new ResponseEntity<>(finalResponseBody, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(initialResponse)
                .thenReturn(finalResponse);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("key", "value");

        String response = aiApiService.forwardRequest("/testRedirectEndpoint", requestBody);

        assertNotNull(response);
        assertEquals(finalResponseBody, response);
        verify(restTemplate, times(2)).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testForwardRequest_ClientError() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("key", "value");

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                aiApiService.forwardRequest("/testClientError", requestBody)
        );

        assertTrue(exception.getMessage().contains("Failed to forward request"));
        verify(restTemplate, times(1)).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testForwardRequest_ServerError() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("key", "value");

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                aiApiService.forwardRequest("/testServerError", requestBody)
        );

        assertTrue(exception.getMessage().contains("Failed to forward request"));
        verify(restTemplate, times(1)).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class));
    }
}

