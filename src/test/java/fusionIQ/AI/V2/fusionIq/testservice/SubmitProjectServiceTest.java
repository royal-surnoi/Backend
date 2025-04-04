package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.SubmitProjectRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.ProjectRepo;
import fusionIQ.AI.V2.fusionIq.service.SubmitProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class SubmitProjectServiceTest {

    @InjectMocks
    private SubmitProjectService submitProjectService;

    @Mock
    private SubmitProjectRepo submitProjectRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private ProjectRepo projectRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitProject() throws Exception {
        User user = new User();
        user.setId(1L);

        Course course = new Course();
        course.setId(1L);

        Project project = new Project();
        project.setId(1L);

        SubmitProject submitProject = new SubmitProject();
        submitProject.setId(1L);

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(course));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));
        when(submitProjectRepo.save(any(SubmitProject.class))).thenReturn(submitProject);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        SubmitProject result = submitProjectService.submitProject(1L, 1L, 1L, "Answer", file);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(submitProjectRepo, times(1)).save(any(SubmitProject.class));
    }

    @Test
    void testFindAllSubmissionProjects() {
        when(submitProjectRepo.findAll()).thenReturn(Collections.singletonList(new SubmitProject()));

        assertEquals(1, submitProjectService.findAllSubmissionProjects().size());
        verify(submitProjectRepo, times(1)).findAll();
    }

    @Test
    void testFindProjectSubmissionById() {
        SubmitProject submitProject = new SubmitProject();
        submitProject.setId(1L);

        when(submitProjectRepo.findById(1L)).thenReturn(Optional.of(submitProject));

        Optional<SubmitProject> result = submitProjectService.findProjectSubmissionById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(submitProjectRepo, times(1)).findById(1L);
    }

    @Test
    void testDeleteSubmitProject() {
        SubmitProject submitProject = new SubmitProject();
        submitProject.setId(1L);

        when(submitProjectRepo.findById(1L)).thenReturn(Optional.of(submitProject));
        doNothing().when(submitProjectRepo).deleteById(1L);

        submitProjectService.deleteSubmitProject(1L);

        verify(submitProjectRepo, times(1)).deleteById(1L);
    }

    @Test
    void testSubmitProjectByStudent() throws Exception {
        User student = new User();
        student.setId(1L);

        Course course = new Course();
        course.setId(1L);

        Project project = new Project();
        project.setId(1L);

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(student));
        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(course));
        when(projectRepo.findById(anyLong())).thenReturn(Optional.of(project));

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        SubmitProject savedProject = new SubmitProject();
        savedProject.setId(1L);
        when(submitProjectRepo.save(any(SubmitProject.class))).thenReturn(savedProject);

        submitProjectService.submitProjectByStudent(1L, 1L, 1L, file, "Answer");

        verify(submitProjectRepo, times(1)).save(any(SubmitProject.class));
    }
}

