package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.AiApiController;
import fusionIQ.AI.V2.fusionIq.service.AiApiService;
import fusionIQ.AI.V2.fusionIq.service.ArticlePostService;
import fusionIQ.AI.V2.fusionIq.service.ImagePostService;
import fusionIQ.AI.V2.fusionIq.service.LongVideoService;
import fusionIQ.AI.V2.fusionIq.service.ShortVideoService;
import fusionIQ.AI.V2.fusionIq.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
public class AiApiControllerTest {
    private MockMvc mockMvc;
    @Mock
    private LongVideoService longVideoService;
    @Mock
    private ShortVideoService shortVideoService;
    @Mock
    private ArticlePostService articlePostService;
    @Mock
    private ImagePostService imagePostService;
    @Mock
    private UserService userService;
    @Mock
    private AiApiService apiService;
    @InjectMocks
    private AiApiController aiApiController;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(aiApiController).build();
    }
    @Test
    public void testSuggestFriends() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_id", 1);
        when(apiService.forwardRequest(eq("/suggest_friends"), any(Map.class))).thenReturn("{\"result\":\"success\"}");
        mockMvc.perform(post("/suggestFriendsRecommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user_id\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFeedShortVideos() throws Exception {
        String responseJson = "[{\"user_id\": 1, \"short_video_id\": 456}]";
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", "Jane Doe");
        when(apiService.forwardRequest(eq("/short_video_feed"), any(Map.class))).thenReturn(responseJson);
        when(userService.getUserDetailsById(1L)).thenReturn(userDetails);
        when(shortVideoService.getShortVideoDetails(1L, 456L)).thenReturn(new HashMap<>());
        mockMvc.perform(post("/feedShortVideos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user_id\":1}"))
                .andExpect(status().isOk());
    }
    // Add more tests for other endpoints if necessary
}

