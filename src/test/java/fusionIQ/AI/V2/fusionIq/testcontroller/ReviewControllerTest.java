package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.ReviewController;
import fusionIQ.AI.V2.fusionIq.data.Review;
import fusionIQ.AI.V2.fusionIq.service.ReviewService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }



    @Test
    public void testGetReviewById_Found() throws Exception {
        Review review = new Review();
        review.setId(1L);

        when(reviewService.findReviewById(1L)).thenReturn(Optional.of(review));

        mockMvc.perform(get("/review/getBy/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    public void testGetReviewById_NotFound() throws Exception {
        when(reviewService.findReviewById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/review/getBy/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllReviews() throws Exception {
        Review review = new Review();
        review.setId(1L);

        when(reviewService.findAllReviews()).thenReturn(Collections.singletonList(review));

        mockMvc.perform(get("/review/allReviews"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testDeleteReview() throws Exception {
        mockMvc.perform(delete("/review/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetReviewsByUser() throws Exception {
        Review review = new Review();
        review.setId(1L);

        when(reviewService.findReviewsByUser(1L)).thenReturn(Collections.singletonList(review));

        mockMvc.perform(get("/review/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testGetReviewsByCourse() throws Exception {
        Review review = new Review();
        review.setId(1L);

        when(reviewService.findReviewsByCourse(1L)).thenReturn(Collections.singletonList(review));

        mockMvc.perform(get("/review/course/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testUpdateReview() throws Exception {
        Review existingReview = new Review();
        existingReview.setId(1L);
        existingReview.setReviewComment("Good course");
        existingReview.setRating(4L);

        when(reviewService.getReviewById(1L)).thenReturn(Optional.of(existingReview));
        when(reviewService.savingReview(existingReview)).thenReturn(existingReview);

        mockMvc.perform(put("/review/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reviewComment\":\"Updated review\",\"rating\":5}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"reviewComment\":\"Updated review\",\"rating\":5}"));
    }

    @Test
    public void testUpdateReview_NotFound() throws Exception {
        when(reviewService.getReviewById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/review/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reviewComment\":\"Updated review\",\"rating\":5}"))
                .andExpect(status().isNotFound());
    }
}

