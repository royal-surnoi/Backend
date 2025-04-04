package fusionIQ.AI.V2.fusionIq.testservice;


import fusionIQ.AI.V2.fusionIq.data.Criteria;
import fusionIQ.AI.V2.fusionIq.data.Grade;
import fusionIQ.AI.V2.fusionIq.data.Project;
import fusionIQ.AI.V2.fusionIq.repository.CriteriaRepo;
import fusionIQ.AI.V2.fusionIq.repository.GradeRepo;
import fusionIQ.AI.V2.fusionIq.repository.ProjectRepo;
import fusionIQ.AI.V2.fusionIq.service.GradingService;
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

public class GradingServiceTest {

    @InjectMocks
    private GradingService gradingService;

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private CriteriaRepo criteriaRepo;

    @Mock
    private GradeRepo gradeRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProject_Success() {
        Project project = new Project();
        when(projectRepo.save(any(Project.class))).thenReturn(project);

        Project result = gradingService.addProject(project);

        assertEquals(project, result);
        verify(projectRepo, times(1)).save(project);
    }

    @Test
    void testAddCriteria_Success() {
        Criteria criteria = new Criteria();
        when(criteriaRepo.save(any(Criteria.class))).thenReturn(criteria);

        Criteria result = gradingService.addCriteria(criteria);

        assertEquals(criteria, result);
        verify(criteriaRepo, times(1)).save(criteria);
    }

    @Test
    void testAddGrade_Success() {
        Grade grade = new Grade();
        when(gradeRepo.save(any(Grade.class))).thenReturn(grade);

        Grade result = gradingService.addGrade(grade);

        assertEquals(grade, result);
        verify(gradeRepo, times(1)).save(grade);
    }



    @Test
    void testAddGradeByProjectAndCriteria_ProjectNotFound() {
        when(projectRepo.findById(anyLong())).thenReturn(Optional.empty());

        Grade grade = new Grade();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            gradingService.addGrade(1L, 1L, grade);
        });

        assertEquals("Project not found", exception.getMessage());
        verify(gradeRepo, times(0)).save(any(Grade.class));
    }

    @Test
    void testAddGradeByProjectAndCriteria_CriteriaNotFound() {
        Project project = new Project();
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(criteriaRepo.findById(anyLong())).thenReturn(Optional.empty());

        Grade grade = new Grade();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            gradingService.addGrade(1L, 1L, grade);
        });

        assertEquals("Criteria not found", exception.getMessage());
        verify(gradeRepo, times(0)).save(any(Grade.class));
    }

    @Test
    void testGetGradesForProject_Success() {
        List<Grade> grades = Arrays.asList(new Grade(), new Grade());
        when(gradeRepo.findAllByProjectId(anyLong())).thenReturn(grades);

        List<Grade> result = gradingService.getGradesForProject(1L);

        assertEquals(grades, result);
        verify(gradeRepo, times(1)).findAllByProjectId(1L);
    }


}
