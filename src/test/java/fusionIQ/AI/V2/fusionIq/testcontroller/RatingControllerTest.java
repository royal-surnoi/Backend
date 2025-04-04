package fusionIQ.AI.V2.fusionIq.testcontroller;




import fusionIQ.AI.V2.fusionIq.controller.RatingController;
import fusionIQ.AI.V2.fusionIq.data.Rating;
import fusionIQ.AI.V2.fusionIq.service.RatingService;
import fusionIQ.AI.V2.fusionIq.repository.RatingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RatingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RatingRepo ratingRepo;

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
    }


    @Test
    public void testGetAverageRating_NoRatings() throws Exception {
        when(ratingRepo.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/average"))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));
    }

    @Test
    public void testGetAverageRating_WithRatings() throws Exception {
        Rating rating = new Rating();
        rating.setRatingValue(4);

        when(ratingRepo.findAll()).thenReturn(Collections.singletonList(rating));

        mockMvc.perform(get("/average"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.0"));
    }

    @Test
    public void testGetRatingById_Found() throws Exception {
        Rating rating = new Rating();
        rating.setId(1L);

        when(ratingRepo.findById(1L)).thenReturn(Optional.of(rating));

        mockMvc.perform(get("/course/1/rating"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    public void testGetRatingById_NotFound() throws Exception {
        when(ratingRepo.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/course/1/rating"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Rating not found"));
    }

    @Test
    public void testGetAverageRatingByCourseId() throws Exception {
        when(ratingService.calculateAverageRating(1L)).thenReturn(4.5);

        mockMvc.perform(get("/average/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.5"));
    }
}
