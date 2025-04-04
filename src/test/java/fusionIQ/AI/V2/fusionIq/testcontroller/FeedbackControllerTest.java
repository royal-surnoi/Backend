package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.FeedbackController;
import fusionIQ.AI.V2.fusionIq.data.Feedback;
import fusionIQ.AI.V2.fusionIq.service.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFeedback_Success() {
        Long userId = 1L;
        boolean feedbackValue = true;
        Feedback feedback = new Feedback();

        when(feedbackService.createFeedback(userId, feedbackValue)).thenReturn(feedback);

        Feedback result = feedbackController.createFeedback(userId, feedbackValue);

        assertEquals(feedback, result);
        verify(feedbackService, times(1)).createFeedback(userId, feedbackValue);
    }

    @Test
    void testGetFeedbackById_Success() {
        Long feedbackId = 1L;
        Feedback feedback = new Feedback();

        when(feedbackService.findById(feedbackId)).thenReturn(feedback);

        Feedback result = feedbackController.getFeedbackById(feedbackId);

        assertEquals(feedback, result);
        verify(feedbackService, times(1)).findById(feedbackId);
    }

    @Test
    void testGetFeedbackByUserId_Success() {
        Long userId = 1L;
        Feedback feedback1 = new Feedback();
        Feedback feedback2 = new Feedback();
        List<Feedback> feedbackList = Arrays.asList(feedback1, feedback2);

        when(feedbackService.findByUserId(userId)).thenReturn(feedbackList);

        List<Feedback> result = feedbackController.getFeedbackByUserId(userId);

        assertEquals(2, result.size());
        verify(feedbackService, times(1)).findByUserId(userId);
    }

    @Test
    void testCreateFeedback_UserNotFound() {
        Long userId = 1L;
        boolean feedbackValue = true;

        when(feedbackService.createFeedback(userId, feedbackValue)).thenThrow(new RuntimeException("User not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            feedbackController.createFeedback(userId, feedbackValue);
        });

        assertEquals("User not found", exception.getMessage());
        verify(feedbackService, times(1)).createFeedback(userId, feedbackValue);
    }

    @Test
    void testGetFeedbackById_NotFound() {
        Long feedbackId = 1L;

        when(feedbackService.findById(feedbackId)).thenThrow(new RuntimeException("Feedback not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            feedbackController.getFeedbackById(feedbackId);
        });

        assertEquals("Feedback not found", exception.getMessage());
        verify(feedbackService, times(1)).findById(feedbackId);
    }

    @Test
    void testGetFeedbackByUserId_UserNotFound() {
        Long userId = 1L;

        when(feedbackService.findByUserId(userId)).thenThrow(new RuntimeException("User not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            feedbackController.getFeedbackByUserId(userId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(feedbackService, times(1)).findByUserId(userId);
    }
}
