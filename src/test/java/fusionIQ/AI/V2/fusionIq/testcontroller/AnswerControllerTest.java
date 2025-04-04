package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.AnswerController;
import fusionIQ.AI.V2.fusionIq.service.AnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class AnswerControllerTest {

    @Mock
    private AnswerService answerService;

    @InjectMocks
    private AnswerController answerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitAnswers_ShouldReturnSuccess() {
        // Arrange
        Long jobQuizId = 1L;
        Long userId = 1L;
        List<Map<String, Object>> answers = new ArrayList<>();
        Map<String, Object> answer = new HashMap<>();
        answer.put("questionId", 1);
        answer.put("selectedAnswer", "A");
        answers.add(answer);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("jobQuizId", jobQuizId);
        expectedResponse.put("userId", userId);
        expectedResponse.put("results", Arrays.asList(Map.of("questionId", 1L, "isCorrect", true)));

        when(answerService.saveAnswers(eq(jobQuizId), eq(userId), any())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = answerController.submitAnswers(jobQuizId, userId, answers);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void getCorrectAnswerPercentage_ShouldReturnPercentage() {
        // Arrange
        Long jobQuizId = 1L;
        double expectedPercentage = 75.0;

        when(answerService.calculateCorrectAnswerPercentage(jobQuizId)).thenReturn(expectedPercentage);

        // Act
        ResponseEntity<Map<String, Object>> response = answerController.getCorrectAnswerPercentage(jobQuizId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(jobQuizId, response.getBody().get("jobQuizId"));
        assertEquals(expectedPercentage, response.getBody().get("correctAnswerPercentage"));
    }

    @Test
    void submitAnswersWithPercentage_ShouldReturnSuccessWithPercentage() {
        // Arrange
        Long jobQuizId = 1L;
        Long userId = 1L;
        List<Map<String, Object>> answers = new ArrayList<>();
        Map<String, Object> answer = new HashMap<>();
        answer.put("questionId", 1);
        answer.put("selectedAnswer", "A");
        answers.add(answer);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("jobQuizId", jobQuizId);
        expectedResponse.put("userId", userId);
        expectedResponse.put("correctAnswerPercentage", 75.0);

        when(answerService.submitAnswersAndCalculatePercentage(eq(jobQuizId), eq(userId), any()))
                .thenReturn(expectedResponse);

        // Act
        ResponseEntity<Map<String, Object>> response =
                answerController.submitAnswersWithPercentage(jobQuizId, userId, answers);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void getQuizProgressDetails_ShouldReturnDetails() {
        // Arrange
        Long jobQuizId = 1L;
        Long userId = 1L;

        Map<String, Object> expectedDetails = new HashMap<>();
        expectedDetails.put("jobQuizId", jobQuizId);
        expectedDetails.put("userId", userId);
        expectedDetails.put("scorePercentage", 75.0);

        when(answerService.getQuizProgressDetails(jobQuizId, userId)).thenReturn(expectedDetails);

        // Act
        ResponseEntity<Map<String, Object>> response =
                answerController.getQuizProgressDetails(jobQuizId, userId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedDetails, response.getBody());
    }

    @Test
    void getProgressByJobAndRecruiterId_ShouldReturnProgress() {
        // Arrange
        Long jobId = 1L;
        Long recruiterId = 1L;

        List<Map<String, Object>> expectedProgress = new ArrayList<>();
        Map<String, Object> progress = new HashMap<>();
        progress.put("jobId", jobId);
        progress.put("recruiterId", recruiterId);
        progress.put("scorePercentage", 75.0);
        expectedProgress.add(progress);

        when(answerService.getProgressByJobAndRecruiterId(jobId, recruiterId))
                .thenReturn(expectedProgress);

        // Act
        ResponseEntity<List<Map<String, Object>>> response =
                answerController.getProgressByJobAndRecruiterId(jobId, recruiterId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedProgress, response.getBody());
    }

    @Test
    void getProgressByJobAndAdminId_ShouldReturnProgress() {
        // Arrange
        Long jobId = 1L;
        Long adminId = 1L;

        List<Map<String, Object>> expectedProgress = new ArrayList<>();
        Map<String, Object> progress = new HashMap<>();
        progress.put("jobId", jobId);
        progress.put("adminId", adminId);
        progress.put("scorePercentage", 75.0);
        expectedProgress.add(progress);

        when(answerService.getProgressByJobAndAdminId(jobId, adminId))
                .thenReturn(expectedProgress);

        // Act
        ResponseEntity<List<Map<String, Object>>> response =
                answerController.getProgressByJobAndAdminId(jobId, adminId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedProgress, response.getBody());
    }
}
