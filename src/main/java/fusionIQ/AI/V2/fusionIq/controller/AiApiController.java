package fusionIQ.AI.V2.fusionIq.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fusionIQ.AI.V2.fusionIq.service.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AiApiController {

    @Autowired
    private LongVideoService longVideoService;
    @Autowired
    private ShortVideoService shortVideoService;
    @Autowired
    private ArticlePostService articlePostService;
    @Autowired
    private ImagePostService imagePostService;
    @Autowired
    private UserService userService;
    @Autowired
    private AiApiService apiService;
    @Autowired
    AITranscribeService aiTranscribeService;

    private ObjectMapper objectMapper = new ObjectMapper();


    @PostMapping("/suggestFriendsRecommendations")
    public ResponseEntity<String> suggestFriends(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/suggest_friends", requestBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/homeRecommendations")
    public ResponseEntity<String> homeRecommendations(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/homerecommendations", requestBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/relatedCoursesRecommendations")
    public ResponseEntity<String> relatedCourses(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/candidate_recommendations", requestBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chatRecommendations")
    public ResponseEntity<String> chat(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/chat", requestBody);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/transcribeRecommendations")
    public ResponseEntity<Map<String, Object>> transcribe() {

        String response = aiTranscribeService.fastForwardRequest("/transcribe");


        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "Transcription and translation completed for all videos");
        responseMap.put("transcripts", response); // Assuming `response` contains the transcripts

        return ResponseEntity.ok(responseMap);
    }


    @PostMapping("/generateQuiz")
    public ResponseEntity<String> generateQuiz(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/generate_quiz", requestBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generateAssignment")
    public ResponseEntity<String> generateAssignment(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/generate_assignment/generate_assignment", requestBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submitAnswer")
    public ResponseEntity<String> submitAnswer(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/submit_answer/submit_answer", requestBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/evaluateAssignment")
    public ResponseEntity<String> evaluateAssignment(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/evaluate_assignment", requestBody);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/skillRecommendations")
    public ResponseEntity<String> skillRecommendations(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/skill_recommendations", requestBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/languageRecommendations")
    public ResponseEntity<String> languageRecommendations(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/language_recommendations", requestBody);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/mentorSuggestions")
    public ResponseEntity<String> mentorSuggestions(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/mentor_suggestions", requestBody);
        return ResponseEntity.ok(response);
    }


    @Cacheable(value = "feedRecommendations", key = "#requestBody")
    @PostMapping("/feedRecommendations")
    public ResponseEntity<List<Map<String, Object>>> feedRecommendations(@RequestBody Map<String, Object> requestBody) {
        try {
            String endpoint = "/feedRecommendations";
            String response = apiService.forwardRequest(endpoint, requestBody);
            List<Map<String, Object>> recommendedPosts = convertJsonToListOfMaps(response);
            List<Map<String, Object>> fullDetails = recommendedPosts.stream()
                    .map(this::processRecommendedPost)
                    .filter(post -> post != null)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fullDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @Cacheable(value = "feedShortVideos", key = "#requestBody")
    @PostMapping("/feedShortVideos")
    public ResponseEntity<List<Map<String, Object>>> feedShortVideos(@RequestBody Map<String, Object> requestBody) {
        try {
            String endpoint = "/short_video_feed";
            String response = apiService.forwardRequest(endpoint, requestBody);
            List<Map<String, Object>> recommendedShortVideos = convertJsonToListOfMaps(response);
            List<Map<String, Object>> fullDetails = recommendedShortVideos.stream()
                    .map(this::processShortVideo)
                    .filter(video -> video != null)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fullDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    private Map<String, Object> processRecommendedPost(Map<String, Object> post) {
        try {
            Long userId = getLongValue(post, "user_id");
            if (userId == null) return null;
            Map<String, Object> userDetails = userService.getUserDetailsById(userId);
            Map<String, Object> postDetails = new HashMap<>(post);
            postDetails.put("user", userDetails);
            String type = (String) post.get("type");
            if (type != null) {
                switch (type) {
                    case "long_video":
                        Long longVideoId = getLongValue(post, "long_video_id");
                        if (longVideoId != null) {
                            postDetails.put("videoDetails", longVideoService.getFullVideoDetails(userId, longVideoId));
                        }
                        break;
                    case "image_post":
                        Long imageId = getLongValue(post, "image_id");
                        if (imageId != null) {
                            postDetails.put("imageDetails", imagePostService.getFullImageDetails(userId, imageId));
                        }
                        break;
                    case "article_post":
                        Long articleId = getLongValue(post, "article_id");
                        if (articleId != null) {
                            postDetails.put("articleDetails", articlePostService.getFullArticleDetails(userId, articleId));
                        }
                        break;
                }
            }
            return postDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private Map<String, Object> processShortVideo(Map<String, Object> video) {
        try {
            Long userId = getLongValue(video, "user_id");
            Long shortVideoId = getLongValue(video, "short_video_id");
            if (userId == null || shortVideoId == null) return null;
            Map<String, Object> userDetails = userService.getUserDetailsById(userId);
            Map<String, Object> videoDetails = new HashMap<>(video);
            videoDetails.put("user", userDetails);
            videoDetails.put("shortVideoDetails", shortVideoService.getShortVideoDetails(userId, shortVideoId));
            return videoDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    private List<Map<String, Object>> convertJsonToListOfMaps(String json) throws IOException {
        JsonNode rootNode = objectMapper.readTree(json);
        if (rootNode.isArray()) {
            return objectMapper.convertValue(rootNode, new TypeReference<List<Map<String, Object>>>() {});
        } else if (rootNode.isObject()) {
            JsonNode arrayNode = findArrayNode(rootNode);
            if (arrayNode != null && arrayNode.isArray()) {
                return objectMapper.convertValue(arrayNode, new TypeReference<List<Map<String, Object>>>() {});
            }
        }
        return new ArrayList<>();
    }
    private JsonNode findArrayNode(JsonNode node) {
        if (node.isArray()) {
            return node;
        } else if (node.isObject()) {
            for (JsonNode child : node) {
                JsonNode arrayNode = findArrayNode(child);
                if (arrayNode != null && arrayNode.isArray()) {
                    return arrayNode;
                }
            }
        }
        return null;
    }

    @PostMapping("/home_edu_recommendations")
    public ResponseEntity<String> recommend_courses(@RequestBody Map<String, Object> requestBody) {
        String response = apiService.forwardRequest("/recommend_courses", requestBody);
        return ResponseEntity.ok(response);
    }

}
