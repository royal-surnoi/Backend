package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.CourseGroupController;
import fusionIQ.AI.V2.fusionIq.data.CourseGroup;
import fusionIQ.AI.V2.fusionIq.service.CourseGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourseGroupControllerTest {

    @Mock
    private CourseGroupService courseGroupService;

    @InjectMocks
    private CourseGroupController courseGroupController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCourseGroupsByTeacherId() {
        Long teacherId = 1L;
        CourseGroup group1 = new CourseGroup();
        CourseGroup group2 = new CourseGroup();
        List<CourseGroup> groups = Arrays.asList(group1, group2);

        when(courseGroupService.getCourseGroupsByTeacherId(teacherId)).thenReturn(groups);

        ResponseEntity<List<CourseGroup>> response = courseGroupController.getCourseGroupsByTeacherId(teacherId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(courseGroupService, times(1)).getCourseGroupsByTeacherId(teacherId);
    }

    @Test
    void testUpdateCourseGroup() {
        Long groupId = 1L;
        CourseGroup updatedGroup = new CourseGroup();
        updatedGroup.setGroupName("Updated Group Name");

        when(courseGroupService.updateCourseGroup(groupId, updatedGroup)).thenReturn(Optional.of(updatedGroup));

        ResponseEntity<CourseGroup> response = courseGroupController.updateCourseGroup(groupId, updatedGroup);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Group Name", response.getBody().getGroupName());
        verify(courseGroupService, times(1)).updateCourseGroup(groupId, updatedGroup);
    }

    @Test
    void testUpdateCourseGroup_NotFound() {
        Long groupId = 1L;
        CourseGroup updatedGroup = new CourseGroup();

        when(courseGroupService.updateCourseGroup(groupId, updatedGroup)).thenReturn(Optional.empty());

        ResponseEntity<CourseGroup> response = courseGroupController.updateCourseGroup(groupId, updatedGroup);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(courseGroupService, times(1)).updateCourseGroup(groupId, updatedGroup);
    }

    @Test
    void testDeleteCourseGroup() {
        Long groupId = 1L;

        doNothing().when(courseGroupService).deleteCourseGroup(groupId);

        ResponseEntity<Void> response = courseGroupController.deleteCourseGroup(groupId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courseGroupService, times(1)).deleteCourseGroup(groupId);
    }

    @Test
    void testDeleteCourseGroupByGroupName() {
        String groupName = "Test Group";

        doNothing().when(courseGroupService).deleteCourseGroupByGroupName(groupName);

        ResponseEntity<Void> response = courseGroupController.deleteCourseGroupByGroupName(groupName);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courseGroupService, times(1)).deleteCourseGroupByGroupName(groupName);
    }
}
