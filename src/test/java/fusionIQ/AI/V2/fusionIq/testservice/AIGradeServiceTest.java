package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.AIAssignment;
import fusionIQ.AI.V2.fusionIq.data.AIGrade;
import fusionIQ.AI.V2.fusionIq.data.AIQuiz;
import fusionIQ.AI.V2.fusionIq.repository.AIAssignmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.AIGradeRepo;
import fusionIQ.AI.V2.fusionIq.repository.AIQuizRepo;
import fusionIQ.AI.V2.fusionIq.service.AIGradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AIGradeServiceTest {

    @Mock
    private AIGradeRepo aiGradeRepo;

    @Mock
    private AIQuizRepo aiQuizRepo;

    @Mock
    private AIAssignmentRepo aiAssignmentRepo;

    @InjectMocks
    private AIGradeService aiGradeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveAIQuizGrade() {
        AIQuiz aiQuiz = new AIQuiz();
        AIGrade aiGrade = new AIGrade();

        when(aiQuizRepo.findById(anyLong())).thenReturn(Optional.of(aiQuiz));
        when(aiGradeRepo.save(any(AIGrade.class))).thenReturn(aiGrade);

        AIGrade savedGrade = aiGradeService.saveAIQuizGrade(1L, "A", "Good job");

        assertNotNull(savedGrade);
        verify(aiQuizRepo, times(1)).findById(anyLong());
        verify(aiGradeRepo, times(1)).save(any(AIGrade.class));
    }

    @Test
    public void testSaveAIQuizGrade_QuizNotFound() {
        when(aiQuizRepo.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            aiGradeService.saveAIQuizGrade(1L, "A", "Good job");
        });

        assertEquals("Quiz not found", exception.getMessage());
        verify(aiQuizRepo, times(1)).findById(anyLong());
        verify(aiGradeRepo, times(0)).save(any(AIGrade.class));
    }

    @Test
    public void testSaveAIAssignmentGrade() {
        AIAssignment aiAssignment = new AIAssignment();
        AIGrade aiGrade = new AIGrade();

        when(aiAssignmentRepo.findById(anyLong())).thenReturn(Optional.of(aiAssignment));
        when(aiGradeRepo.save(any(AIGrade.class))).thenReturn(aiGrade);

        AIGrade savedGrade = aiGradeService.saveAIAssignmentGrade(1L, "B", "Needs improvement");

        assertNotNull(savedGrade);
        verify(aiAssignmentRepo, times(1)).findById(anyLong());
        verify(aiGradeRepo, times(1)).save(any(AIGrade.class));
    }

    @Test
    public void testSaveAIAssignmentGrade_AssignmentNotFound() {
        when(aiAssignmentRepo.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            aiGradeService.saveAIAssignmentGrade(1L, "B", "Needs improvement");
        });

        assertEquals("Assignment not found", exception.getMessage());
        verify(aiAssignmentRepo, times(1)).findById(anyLong());
        verify(aiGradeRepo, times(0)).save(any(AIGrade.class));
    }

    @Test
    public void testGetGradesByUserId() {
        List<AIGrade> grades = Arrays.asList(new AIGrade(), new AIGrade());

        when(aiGradeRepo.findByUserId(anyLong())).thenReturn(grades);

        List<AIGrade> result = aiGradeService.getGradesByUserId(1L);

        assertEquals(grades.size(), result.size());
        verify(aiGradeRepo, times(1)).findByUserId(anyLong());
    }

    @Test
    public void testGetAllGrades() {
        List<AIGrade> grades = Arrays.asList(new AIGrade(), new AIGrade());

        when(aiGradeRepo.findAll()).thenReturn(grades);

        List<AIGrade> result = aiGradeService.getAllGrades();

        assertEquals(grades.size(), result.size());
        verify(aiGradeRepo, times(1)).findAll();
    }

    @Test
    public void testDeleteGradeById() {
        when(aiGradeRepo.existsById(anyLong())).thenReturn(true);
        doNothing().when(aiGradeRepo).deleteById(anyLong());

        aiGradeService.deleteGradeById(1L);

        verify(aiGradeRepo, times(1)).existsById(anyLong());
        verify(aiGradeRepo, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteGradeById_GradeNotFound() {
        when(aiGradeRepo.existsById(anyLong())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            aiGradeService.deleteGradeById(1L);
        });

        assertEquals("Grade not found", exception.getMessage());
        verify(aiGradeRepo, times(1)).existsById(anyLong());
        verify(aiGradeRepo, times(0)).deleteById(anyLong());
    }
}

