package fusionIQ.AI.V2.fusionIq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserChatService {

    @Autowired
    private RestTemplate restTemplate;

    private final String fastApiUrl = "http://127.0.0.1:8000";

    public String handleUserQuery(int id, String userQuery) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", id);
        requestBody.put("user_query", userQuery);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        Map<String, Object> response = restTemplate.postForObject(fastApiUrl + "/chat", entity, Map.class);
        return response != null ? response.get("response").toString() : null;
    }
}