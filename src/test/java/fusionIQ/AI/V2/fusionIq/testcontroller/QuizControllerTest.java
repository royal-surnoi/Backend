package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.QuizController;
import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.service.QuizService;
import fusionIQ.AI.V2.fusionIq.service.QuizProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class QuizControllerTest {

    @Mock
    private QuizService quizService;

    @Mock
    private QuizProgressService quizProgressService;

    @InjectMocks
    private QuizController quizController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllQuizzes_Success() {
        List<Quiz> quizzes = Collections.singletonList(new Quiz());
        when(quizService.getAllQuizzes()).thenReturn(quizzes);

        ResponseEntity<List<Quiz>> response = quizController.getAllQuizzes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetQuizById_Found() {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        when(quizService.getQuizById(anyLong())).thenReturn(quiz);

        ResponseEntity<Quiz> response = quizController.getQuizById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetQuizById_NotFound() {
        when(quizService.getQuizById(anyLong())).thenThrow(new ResourceNotFoundException("Quiz not found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            quizController.getQuizById(1L);
        });

        // Optionally, you can also assert the message
        assertEquals("Quiz not found", exception.getMessage());
    }

    @Test
    void testSubmitAnswer_Success() {
        Answer answer = new Answer();
        when(quizService.submitAnswer(anyLong(), anyLong(), anyLong(), anyString())).thenReturn(answer);

        ResponseEntity<Answer> response = quizController.submitAnswer(1L, 1L, 1L, "A");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(answer, response.getBody());
    }




    @Test
    void testCalculateCorrectAnswerPercentage_Success() {
        when(quizService.calculateCorrectAnswerPercentage(anyLong(), anyLong())).thenReturn(80.0);

        ResponseEntity<Double> response = quizController.getCorrectAnswerPercentage(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(80.0, response.getBody());
    }

    @Test
    void testAddQuestionsToQuiz_Success() {
        when(quizService.addQuestionsToQuiz(anyLong(), anyList())).thenReturn(Collections.singletonList(new Question()));

        ResponseEntity<List<Question>> response = quizController.addQuestionsToQuiz(1L, Collections.singletonList(new Question()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetQuizzesByCourseId_Success() {
        List<Quiz> quizzes = Collections.singletonList(new Quiz());
        when(quizService.getQuizzesByCourseId(anyLong())).thenReturn(quizzes);

        ResponseEntity<List<Quiz>> response = quizController.getQuizzesByCourseId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetUsersWhoSubmittedQuiz_Success() {
        when(quizProgressService.getUsersWhoSubmittedQuiz(anyLong())).thenReturn(Collections.singletonList(new User()));

        ResponseEntity<List<User>> response = quizController.getUsersWhoSubmittedQuiz(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }



    @Test
    void testMarkQuizAsCompleted_QuizNotFound() {
        when(quizService.findByIdWithCourse(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<String> response = quizController.markQuizAsCompleted(1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Quiz with ID 1 not found", response.getBody());
    }
}
