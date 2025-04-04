package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.CourseToolController;
import fusionIQ.AI.V2.fusionIq.data.CourseTools;
import fusionIQ.AI.V2.fusionIq.service.CourseToolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourseToolControllerTest {

    @Mock
    private CourseToolService courseToolService;

    @InjectMocks
    private CourseToolController courseToolController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCourseImage_Success() throws IOException {
        Long courseId = 1L;
        String toolName = "Tool Name";
        String skillName = "Skill Name";
        String coursePrerequisites = "Course Prerequisites";

        MultipartFile toolImage = mock(MultipartFile.class);
        MultipartFile skillImage = mock(MultipartFile.class);

        CourseTools courseTools = new CourseTools();

        when(courseToolService.saveCourseImage(courseId, toolImage, skillName, skillImage, toolName, coursePrerequisites))
                .thenReturn(courseTools);

        ResponseEntity<CourseTools> response = courseToolController.saveCourseImage(courseId, toolImage, skillImage, toolName, skillName, coursePrerequisites);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(courseTools, response.getBody());
        verify(courseToolService, times(1)).saveCourseImage(courseId, toolImage, skillName, skillImage, toolName, coursePrerequisites);
    }

    @Test
    void testGetAllTools_Success() {
        CourseTools tool1 = new CourseTools();
        CourseTools tool2 = new CourseTools();
        List<CourseTools> tools = Arrays.asList(tool1, tool2);

        when(courseToolService.findAllTools()).thenReturn(tools);

        ResponseEntity<List<CourseTools>> response = courseToolController.getAllTools();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(courseToolService, times(1)).findAllTools();
    }

    @Test
    void testGetCourseToolsByCourse_Success() {
        Long courseId = 1L;
        CourseTools tool1 = new CourseTools();
        CourseTools tool2 = new CourseTools();
        List<CourseTools> tools = Arrays.asList(tool1, tool2);

        when(courseToolService.findToolsByCourseId(courseId)).thenReturn(tools);

        ResponseEntity<List<CourseTools>> response = courseToolController.getCourseToolsByCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(courseToolService, times(1)).findToolsByCourseId(courseId);
    }

    @Test
    void testUpdateCourseTools_Success() {
        Long id = 1L;
        CourseTools updatedTools = new CourseTools();
        updatedTools.setToolName("Updated Tool Name");

        when(courseToolService.updateCourseTools(id, updatedTools)).thenReturn(updatedTools);

        ResponseEntity<CourseTools> response = courseToolController.updateCourseTools(id, updatedTools);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTools, response.getBody());
        verify(courseToolService, times(1)).updateCourseTools(id, updatedTools);
    }

    @Test
    void testPatchCourseToolsByCourseId_Success() {
        Long courseId = 1L;
        Map<String, Object> updates = Map.of("toolName", "Patched Tool Name");

        CourseTools patchedTools = new CourseTools();
        patchedTools.setToolName("Patched Tool Name");

        when(courseToolService.patchCourseToolsByCourseId(courseId, updates)).thenReturn(patchedTools);

        ResponseEntity<CourseTools> response = courseToolController.patchCourseToolsByCourseId(courseId, updates);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patchedTools, response.getBody());
        verify(courseToolService, times(1)).patchCourseToolsByCourseId(courseId, updates);
    }

    @Test
    void testDeleteCourseToolsProject_Success() {
        Long id = 1L;

        doNothing().when(courseToolService).deleteCourseToolsProject(id);

        ResponseEntity<Void> response = courseToolController.deleteCourseToolsProject(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courseToolService, times(1)).deleteCourseToolsProject(id);
    }

    @Test
    void testSaveMultipleCourseTools_Success() throws IOException {
        Long courseId = 1L;

        MultipartFile toolImage1 = mock(MultipartFile.class);
        MultipartFile toolImage2 = mock(MultipartFile.class);
        List<MultipartFile> toolImages = Arrays.asList(toolImage1, toolImage2);

        MultipartFile skillImage1 = mock(MultipartFile.class);
        MultipartFile skillImage2 = mock(MultipartFile.class);
        List<MultipartFile> skillImages = Arrays.asList(skillImage1, skillImage2);

        List<String> toolNames = Arrays.asList("Tool 1", "Tool 2");
        List<String> skillNames = Arrays.asList("Skill 1", "Skill 2");
        List<String> coursePrerequisites = Arrays.asList("Prerequisite 1", "Prerequisite 2");

        CourseTools courseTool1 = new CourseTools();
        CourseTools courseTool2 = new CourseTools();
        List<CourseTools> courseToolsList = Arrays.asList(courseTool1, courseTool2);

        when(courseToolService.saveMultipleCourseTools(courseId, toolImages, skillNames, skillImages, toolNames, coursePrerequisites))
                .thenReturn(courseToolsList);

        ResponseEntity<List<CourseTools>> response = courseToolController.saveMultipleCourseTools(courseId, toolImages, skillImages, toolNames, skillNames, coursePrerequisites);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(courseToolService, times(1)).saveMultipleCourseTools(courseId, toolImages, skillNames, skillImages, toolNames, coursePrerequisites);
    }
}
