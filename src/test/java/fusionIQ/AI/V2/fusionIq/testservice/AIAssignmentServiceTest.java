package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.AIAssignment;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.AIAssignmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.AIAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AIAssignmentServiceTest {

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private LessonRepo lessonRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private AIAssignmentRepo aiAssignmentRepo;

    @InjectMocks
    private AIAssignmentService aiAssignmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveAIAssignment() {
        Course course = new Course();
        Lesson lesson = new Lesson();
        User user = new User();
        AIAssignment assignment = new AIAssignment();

        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(course));
        when(lessonRepo.findById(anyLong())).thenReturn(Optional.of(lesson));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(aiAssignmentRepo.save(any(AIAssignment.class))).thenReturn(assignment);

        AIAssignment savedAssignment = aiAssignmentService.saveAIAssignment(1L, 1L, 1L, "Question", "Answer", "Description");

        assertNotNull(savedAssignment);
        verify(aiAssignmentRepo, times(1)).save(any(AIAssignment.class));
    }

    @Test
    public void testGetAssignmentsByUserIdAndCourseId() {
        List<AIAssignment> assignments = Arrays.asList(new AIAssignment(), new AIAssignment());

        when(aiAssignmentRepo.findByUserIdAndCourseId(anyLong(), anyLong())).thenReturn(assignments);

        List<AIAssignment> result = aiAssignmentService.getAssignmentsByUserIdAndCourseId(1L, 1L);

        assertEquals(assignments.size(), result.size());
        verify(aiAssignmentRepo, times(1)).findByUserIdAndCourseId(anyLong(), anyLong());
    }

    @Test
    public void testGetAIAssignmentById() {
        AIAssignment assignment = new AIAssignment();
        when(aiAssignmentRepo.findById(anyLong())).thenReturn(Optional.of(assignment));

        Optional<AIAssignment> result = aiAssignmentService.getAIAssignmentById(1L);

        assertTrue(result.isPresent());
        verify(aiAssignmentRepo, times(1)).findById(anyLong());
    }

    @Test
    public void testGetAIAssignmentById_NotFound() {
        when(aiAssignmentRepo.findById(anyLong())).thenReturn(Optional.empty());

        Optional<AIAssignment> result = aiAssignmentService.getAIAssignmentById(1L);

        assertFalse(result.isPresent());
        verify(aiAssignmentRepo, times(1)).findById(anyLong());
    }

    @Test
    public void testGetAllAssignments() {
        List<AIAssignment> assignments = Arrays.asList(new AIAssignment(), new AIAssignment());

        when(aiAssignmentRepo.findAll()).thenReturn(assignments);

        List<AIAssignment> result = aiAssignmentService.getAllAssignments();

        assertEquals(assignments.size(), result.size());
        verify(aiAssignmentRepo, times(1)).findAll();
    }

    @Test
    public void testDeleteAssignmentById() {
        doNothing().when(aiAssignmentRepo).deleteById(anyLong());

        aiAssignmentService.deleteAssignmentById(1L);

        verify(aiAssignmentRepo, times(1)).deleteById(anyLong());
    }

    @Test
    public void testUpdateUserAnswer() {
        AIAssignment assignment = new AIAssignment();
        assignment.setAIAssignmentUserAnswer("Old Answer");

        when(aiAssignmentRepo.findById(anyLong())).thenReturn(Optional.of(assignment));
        when(aiAssignmentRepo.save(any(AIAssignment.class))).thenReturn(assignment);

        AIAssignment updatedAssignment = aiAssignmentService.updateUserAnswer(1L, "New Answer");

        assertEquals("New Answer", updatedAssignment.getAIAssignmentUserAnswer());
        verify(aiAssignmentRepo, times(1)).save(any(AIAssignment.class));
    }

    @Test
    public void testCalculateGrade_CorrectAnswer() {
        AIAssignment assignment = new AIAssignment();
        assignment.setAIAssignmentAnswer("Correct Answer");
        assignment.setAIAssignmentUserAnswer("Correct Answer");

        when(aiAssignmentRepo.findById(anyLong())).thenReturn(Optional.of(assignment));

        double grade = aiAssignmentService.calculateGrade(1L);

        assertEquals(100.0, grade);
    }

    @Test
    public void testCalculateGrade_IncorrectAnswer() {
        AIAssignment assignment = new AIAssignment();
        assignment.setAIAssignmentAnswer("Correct Answer");
        assignment.setAIAssignmentUserAnswer("Wrong Answer");

        when(aiAssignmentRepo.findById(anyLong())).thenReturn(Optional.of(assignment));

        double grade = aiAssignmentService.calculateGrade(1L);

        assertEquals(0.0, grade);
    }

    @Test
    public void testGetAssignmentsByUserIdAndLessonId() {
        List<AIAssignment> assignments = Arrays.asList(new AIAssignment(), new AIAssignment());

        when(aiAssignmentRepo.findByUserIdAndLessonId(anyLong(), anyLong())).thenReturn(assignments);

        List<AIAssignment> result = aiAssignmentService.getAssignmentsByUserIdAndLessonId(1L, 1L);

        assertEquals(assignments.size(), result.size());
        verify(aiAssignmentRepo, times(1)).findByUserIdAndLessonId(anyLong(), anyLong());
    }

    @Test
    public void testUpdateAIAssignment() {
        AIAssignment assignment = new AIAssignment();
        assignment.setAIAssignmentUserAnswer("Old Answer");
        assignment.setAIAssignmentDescription("Old Description");

        when(aiAssignmentRepo.findById(anyLong())).thenReturn(Optional.of(assignment));
        when(aiAssignmentRepo.save(any(AIAssignment.class))).thenReturn(assignment);

        AIAssignment updatedAssignment = aiAssignmentService.updateAIAssignment(1L, "New Answer", "New Description");

        assertEquals("New Answer", updatedAssignment.getAIAssignmentUserAnswer());
        assertEquals("New Description", updatedAssignment.getAIAssignmentDescription());
        verify(aiAssignmentRepo, times(1)).save(any(AIAssignment.class));
    }

    @Test
    public void testUpdateAIAssignment_WithUpdates() {
        AIAssignment assignment = new AIAssignment();
        assignment.setAIAssignmentUserAnswer("Old Answer");
        assignment.setAIAssignmentDescription("Old Description");

        when(aiAssignmentRepo.findById(anyLong())).thenReturn(Optional.of(assignment));
        when(aiAssignmentRepo.save(any(AIAssignment.class))).thenReturn(assignment);

        AIAssignment updatedAssignment = aiAssignmentService.updateAIAssignment(1L, Map.of("userAnswer", "New Answer", "description", "New Description"));

        assertEquals("New Answer", updatedAssignment.getAIAssignmentUserAnswer());
        assertEquals("New Description", updatedAssignment.getAIAssignmentDescription());
        verify(aiAssignmentRepo, times(1)).save(any(AIAssignment.class));
    }
}

