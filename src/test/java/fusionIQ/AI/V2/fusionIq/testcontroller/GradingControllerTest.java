package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.GradingController;
import fusionIQ.AI.V2.fusionIq.data.Criteria;
import fusionIQ.AI.V2.fusionIq.data.Grade;
import fusionIQ.AI.V2.fusionIq.data.Project;
import fusionIQ.AI.V2.fusionIq.service.GradingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GradingControllerTest {

    @Mock
    private GradingService gradingService;

    @InjectMocks
    private GradingController gradingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddGrade_Success() {
        Grade grade = new Grade();
        when(gradingService.addGrade(grade)).thenReturn(grade);

        Grade result = gradingController.addGrade(grade);

        assertEquals(grade, result);
        verify(gradingService, times(1)).addGrade(grade);
    }

    @Test
    void testAddCriteria_Success() {
        Criteria criteria = new Criteria();
        when(gradingService.addCriteria(criteria)).thenReturn(criteria);

        Criteria result = gradingController.addCriteria(criteria);

        assertEquals(criteria, result);
        verify(gradingService, times(1)).addCriteria(criteria);
    }


    @Test
    void testGetGradesForProject_Success() {
        Long projectId = 1L;
        List<Grade> grades = Arrays.asList(new Grade(), new Grade());

        when(gradingService.getGradesForProject(projectId)).thenReturn(grades);

        List<Grade> result = gradingController.getGradesForProject(projectId);

        assertEquals(2, result.size());
        verify(gradingService, times(1)).getGradesForProject(projectId);
    }

    @Test
    void testCalculateFinalGrade_Success() {
        Long projectId = 1L;
        double finalGrade = 85.0;

        when(gradingService.calculateFinalGrade(projectId)).thenReturn(finalGrade);

        double result = gradingController.calculateFinalGrade(projectId);

        assertEquals(finalGrade, result, 0.01);
        verify(gradingService, times(1)).calculateFinalGrade(projectId);
    }
}
