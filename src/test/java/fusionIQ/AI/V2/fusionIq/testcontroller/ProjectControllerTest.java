package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.ProjectController;
import fusionIQ.AI.V2.fusionIq.data.Project;
import fusionIQ.AI.V2.fusionIq.data.SubmitProject;
import fusionIQ.AI.V2.fusionIq.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProject_Success() throws Exception {
        Project project = new Project();
        project.setId(1L);
        when(projectService.saveProject(any(Project.class), anyLong())).thenReturn(project);

        ResponseEntity<Project> response = projectController.createProject(project, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testCreateProject_BadRequest() throws Exception {
        when(projectService.saveProject(any(Project.class), anyLong())).thenThrow(new IllegalArgumentException());

        ResponseEntity<Project> response = projectController.createProject(new Project(), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetProjectById_Found() {
        Project project = new Project();
        project.setId(1L);
        when(projectService.findProjectById(anyLong())).thenReturn(Optional.of(project));

        ResponseEntity<Project> response = projectController.getProjectById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetProjectById_NotFound() {
        when(projectService.findProjectById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Project> response = projectController.getProjectById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllProjects() {
        List<Project> projects = Collections.singletonList(new Project());
        when(projectService.findAllProjects()).thenReturn(projects);

        ResponseEntity<List<Project>> response = projectController.getAllProjects();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testDeleteProject() {
        doNothing().when(projectService).deleteProject(anyLong());

        ResponseEntity<Void> response = projectController.deleteProject(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testSubmitProject_Success() throws Exception {
        SubmitProject submitProject = new SubmitProject();
        when(projectService.submitProject(anyLong(), anyLong(), anyString(), any(MultipartFile.class))).thenReturn(submitProject);

        ResponseEntity<SubmitProject> response = projectController.submitProject(1L, 1L, "Answer", mock(MultipartFile.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(submitProject, response.getBody());
    }
}
