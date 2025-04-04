package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.AIAssignmentController;
import fusionIQ.AI.V2.fusionIq.data.AIAssignment;
import fusionIQ.AI.V2.fusionIq.service.AIAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AIAssignmentControllerTest {

    @InjectMocks
    private AIAssignmentController aiAssignmentController;

    @Mock
    private AIAssignmentService aiAssignmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAIAssignment() {
        Long courseId = 1L;
        Long lessonId = 1L;
        Long userId = 1L;
        String question = "Sample Question";
        String description = "Sample Description";
        String answer = "Sample Answer";

        AIAssignment assignment = new AIAssignment();
        assignment.setId(1L);
        assignment.setAIAssignmentQuestion(question);
        assignment.setAIAssignmentDescription(description);
        assignment.setAIAssignmentAnswer(answer);

        when(aiAssignmentService.saveAIAssignment(courseId, lessonId, userId, question, answer, description))
                .thenReturn(assignment);

        ResponseEntity<AIAssignment> response = aiAssignmentController.createAIAssignment(courseId, lessonId, userId, question, description, answer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(assignment, response.getBody());
    }

    @Test
    void testGetAssignmentsByUserIdAndCourseId() {
        Long userId = 1L;
        Long courseId = 1L;

        List<AIAssignment> assignments = new ArrayList<>();
        assignments.add(new AIAssignment());

        when(aiAssignmentService.getAssignmentsByUserIdAndCourseId(userId, courseId)).thenReturn(assignments);

        ResponseEntity<List<AIAssignment>> response = aiAssignmentController.getAssignmentsByUserIdAndCourseId(userId, courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assignments, response.getBody());
    }

    @Test
    void testGetAIAssignmentById() {
        Long id = 1L;

        AIAssignment assignment = new AIAssignment();
        assignment.setId(id);

        when(aiAssignmentService.getAIAssignmentById(id)).thenReturn(Optional.of(assignment));

        ResponseEntity<AIAssignment> response = aiAssignmentController.getAIAssignmentById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assignment, response.getBody());
    }

    @Test
    void testGetAIAssignmentById_NotFound() {
        Long id = 1L;

        when(aiAssignmentService.getAIAssignmentById(id)).thenReturn(Optional.empty());

        ResponseEntity<AIAssignment> response = aiAssignmentController.getAIAssignmentById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllAssignments() {
        List<AIAssignment> assignments = new ArrayList<>();
        assignments.add(new AIAssignment());

        when(aiAssignmentService.getAllAssignments()).thenReturn(assignments);

        ResponseEntity<List<AIAssignment>> response = aiAssignmentController.getAllAssignments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assignments, response.getBody());
    }

    @Test
    void testDeleteAIAssignmentById() {
        Long id = 1L;

        when(aiAssignmentService.getAssignmentById(id)).thenReturn(Optional.of(new AIAssignment()));

        ResponseEntity<Void> response = aiAssignmentController.deleteAssignmentById(id);

        verify(aiAssignmentService, times(1)).deleteAssignmentById(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteAIAssignmentById_NotFound() {
        Long id = 1L;

        when(aiAssignmentService.getAssignmentById(id)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = aiAssignmentController.deleteAssignmentById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateUserAnswer() {
        Long id = 1L;
        String newAnswer = "Updated Answer";

        AIAssignment assignment = new AIAssignment();
        assignment.setId(id);
        assignment.setAIAssignmentUserAnswer(newAnswer);

        when(aiAssignmentService.updateUserAnswer(id, newAnswer)).thenReturn(assignment);

        ResponseEntity<AIAssignment> response = aiAssignmentController.updateUserAnswer(id, newAnswer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assignment, response.getBody());
    }

    @Test
    void testCalculateGrade() {
        Long assignmentId = 1L;
        double expectedGrade = 100.0;

        when(aiAssignmentService.calculateGrade(assignmentId)).thenReturn(expectedGrade);

        double grade = aiAssignmentController.getGrade(assignmentId);

        assertEquals(expectedGrade, grade, 0.001);
    }

    @Test
    void testUpdateAIAssignment() {
        Long id = 1L;
        String newAnswer = "Updated Answer";
        String newDescription = "Updated Description";

        AIAssignment assignment = new AIAssignment();
        assignment.setId(id);
        assignment.setAIAssignmentUserAnswer(newAnswer);
        assignment.setAIAssignmentDescription(newDescription);

        when(aiAssignmentService.updateAIAssignment(id, newAnswer, newDescription)).thenReturn(assignment);

        AIAssignment updatedAssignment = aiAssignmentController.updateAIAssignment(id, Map.of("userAnswer", newAnswer, "description", newDescription));

        assertEquals(newAnswer, updatedAssignment.getAIAssignmentUserAnswer());
        assertEquals(newDescription, updatedAssignment.getAIAssignmentDescription());
    }
}
