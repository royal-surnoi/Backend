package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.AIGradeController;
import fusionIQ.AI.V2.fusionIq.data.AIGrade;
import fusionIQ.AI.V2.fusionIq.service.AIGradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AIGradeControllerTest {

    @InjectMocks
    private AIGradeController aiGradeController;

    @Mock
    private AIGradeService aiGradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAIQuizGrade() {
        Long quizId = 1L;
        String grade = "A";
        String feedback = "Good job";

        AIGrade aiGrade = new AIGrade();
        aiGrade.setId(1L);
        aiGrade.setAIGrade(grade);
        aiGrade.setAIFeedback(feedback);

        when(aiGradeService.saveAIQuizGrade(quizId, grade, feedback)).thenReturn(aiGrade);

        ResponseEntity<AIGrade> response = aiGradeController.createAIQuizGrade(quizId, grade, feedback);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(aiGrade, response.getBody());
    }

    @Test
    void testCreateAIAssignmentGrade() {
        Long assignmentId = 1L;
        String grade = "B";
        String feedback = "Needs improvement";

        AIGrade aiGrade = new AIGrade();
        aiGrade.setId(1L);
        aiGrade.setAIGrade(grade);
        aiGrade.setAIFeedback(feedback);

        when(aiGradeService.saveAIAssignmentGrade(assignmentId, grade, feedback)).thenReturn(aiGrade);

        ResponseEntity<AIGrade> response = aiGradeController.createAIAssignmentGrade(assignmentId, grade, feedback);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(aiGrade, response.getBody());
    }

    @Test
    void testGetGradesByUserId() {
        Long userId = 1L;

        List<AIGrade> grades = new ArrayList<>();
        grades.add(new AIGrade());

        when(aiGradeService.getGradesByUserId(userId)).thenReturn(grades);

        ResponseEntity<List<AIGrade>> response = aiGradeController.getGradesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grades, response.getBody());
    }

    @Test
    void testGetAllGrades() {
        List<AIGrade> grades = new ArrayList<>();
        grades.add(new AIGrade());

        when(aiGradeService.getAllGrades()).thenReturn(grades);

        ResponseEntity<List<AIGrade>> response = aiGradeController.getAllGrades();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grades, response.getBody());
    }

    @Test
    void testDeleteGradeById() {
        Long id = 1L;

        doNothing().when(aiGradeService).deleteGradeById(id);

        ResponseEntity<Void> response = aiGradeController.deleteGradeById(id);

        verify(aiGradeService, times(1)).deleteGradeById(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteGradeById_NotFound() {
        Long id = 1L;

        doThrow(new RuntimeException("Grade not found")).when(aiGradeService).deleteGradeById(id);

        ResponseEntity<Void> response = aiGradeController.deleteGradeById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

