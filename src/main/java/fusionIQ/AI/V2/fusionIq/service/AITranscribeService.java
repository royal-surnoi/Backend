package fusionIQ.AI.V2.fusionIq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
@Service
public class AITranscribeService {
    @Autowired
    private RestTemplate restTemplate;

    private final String fastApiBaseUrl = "http://127.0.0.1:8000";

    public String fastForwardRequest(String endpoint) {
//        String url = fastApiBaseUrl + endpoint;
        if (endpoint.startsWith("/")) {
            endpoint = endpoint.substring(1);
        }
        String url = fastApiBaseUrl + "/" + endpoint; // Constructs the full URL


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else if (response.getStatusCode().is3xxRedirection()) {
                // Handle redirect if necessary
                URI location = response.getHeaders().getLocation();
                String redirectUrl = (location != null) ? location.toString() : null;
                if (redirectUrl != null) {
                    response = restTemplate.exchange(redirectUrl, HttpMethod.POST, entity, String.class);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        return response.getBody();
                    } else {
                        throw new RuntimeException("Failed to follow redirect: " + response.getStatusCode());
                    }
                } else {
                    throw new RuntimeException("Redirect URL is null");
                }
            } else {
                throw new RuntimeException("Failed to forward request: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to forward request", e);
        }
    }
}


