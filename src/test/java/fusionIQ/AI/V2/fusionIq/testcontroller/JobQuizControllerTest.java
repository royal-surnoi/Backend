package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.JobQuizController;
import fusionIQ.AI.V2.fusionIq.data.JobQuiz;
import fusionIQ.AI.V2.fusionIq.service.JobQuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.*;

class JobQuizControllerTest {
    @Mock
    private JobQuizService jobQuizService;

    private JobQuizController jobQuizController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobQuizController = new JobQuizController(jobQuizService);
    }

    @Test
    void testCreateJobQuizForUsers() {
        // Arrange
        Long recruiterId = 1L;
        Long jobId = 2L;
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);
        String quizName = "Test Quiz";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);

        JobQuiz expectedQuiz = new JobQuiz();
        expectedQuiz.setId(1L);
        when(jobQuizService.createJobQuizForUsers(any(), any(), anyString(), any(), any(), any()))
                .thenReturn(expectedQuiz);

        // Act
        ResponseEntity<JobQuiz> response = jobQuizController.createJobQuizForUsers(
                recruiterId, jobId, userIds, quizName, startDate, endDate);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(jobQuizService).createJobQuizForUsers(
                eq(recruiterId), eq(jobId), anyString(), eq(quizName), eq(startDate), eq(endDate));
    }

    @Test
    void testGetJobQuizzesByJobAndUser() {
        // Arrange
        Long jobId = 1L;
        Long userId = 2L;
        List<JobQuiz> quizzes = new ArrayList<>();
        JobQuiz quiz = new JobQuiz();
        quiz.setId(1L);
        quizzes.add(quiz);

        when(jobQuizService.getJobQuizzesByUserAndJob(jobId, userId)).thenReturn(quizzes);

        // Act
        ResponseEntity<List<Map<String, Object>>> response =
                jobQuizController.getJobQuizzesByJobAndUser(jobId, userId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
}