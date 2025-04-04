package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private SubmissionRepo submissionRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private SubmitProjectRepo submitProjectRepo;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProject_Success() {
        Project project = new Project();
        Course course = new Course();
        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(course));
        when(projectRepo.save(any(Project.class))).thenReturn(project);

        Project savedProject = projectService.saveProject(project, 1L);

        assertNotNull(savedProject);
    }

    @Test
    void testSaveProject_CourseNotFound() {
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.saveProject(new Project(), 1L);
        });

        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    void testFindProjectById_Found() {
        Project project = new Project();
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));

        Optional<Project> foundProject = projectService.findProjectById(1L);

        assertTrue(foundProject.isPresent());
    }

    @Test
    void testFindProjectById_NotFound() {
        when(projectRepo.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Project> foundProject = projectService.findProjectById(1L);

        assertFalse(foundProject.isPresent());
    }






}

