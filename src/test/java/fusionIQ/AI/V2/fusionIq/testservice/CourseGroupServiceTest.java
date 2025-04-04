package fusionIQ.AI.V2.fusionIq.testservice;


import fusionIQ.AI.V2.fusionIq.data.CourseGroup;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.CourseGroupRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.CourseGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CourseGroupServiceTest {

    @Mock
    private CourseGroupRepo courseGroupRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CourseGroupService courseGroupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProjectCourseGroup() {
        User teacher = new User();
        teacher.setId(1L);

        CourseGroup courseGroup = new CourseGroup();
        courseGroup.setGroupName("Test Group");
        courseGroup.setTeacher(teacher);

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(courseGroupRepo.save(any(CourseGroup.class))).thenReturn(courseGroup);

        CourseGroup result = courseGroupService.createProjectCourseGroup("Test Group", 1L);

        assertNotNull(result);
        assertEquals("Test Group", result.getGroupName());
        verify(userRepo, times(1)).findById(1L);
        verify(courseGroupRepo, times(1)).save(any(CourseGroup.class));
    }

    @Test
    void testCreateProjectCourseGroup_TeacherNotFound() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> courseGroupService.createProjectCourseGroup("Test Group", 1L));
        verify(userRepo, times(1)).findById(1L);
    }

    @Test
    void testGetCourseGroupsByTeacherId() {
        CourseGroup courseGroup1 = new CourseGroup();
        courseGroup1.setGroupName("Group 1");
        CourseGroup courseGroup2 = new CourseGroup();
        courseGroup2.setGroupName("Group 2");

        when(courseGroupRepo.findByTeacherId(anyLong())).thenReturn(List.of(courseGroup1, courseGroup2));

        List<CourseGroup> result = courseGroupService.getCourseGroupsByTeacherId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(courseGroupRepo, times(1)).findByTeacherId(1L);
    }

    @Test
    void testUpdateCourseGroup() {
        CourseGroup existingCourseGroup = new CourseGroup();
        existingCourseGroup.setId(1L);
        existingCourseGroup.setGroupName("Old Group Name");

        CourseGroup updatedCourseGroup = new CourseGroup();
        updatedCourseGroup.setGroupName("New Group Name");

        when(courseGroupRepo.findById(anyLong())).thenReturn(Optional.of(existingCourseGroup));
        when(courseGroupRepo.save(any(CourseGroup.class))).thenReturn(existingCourseGroup);

        Optional<CourseGroup> result = courseGroupService.updateCourseGroup(1L, updatedCourseGroup);

        assertTrue(result.isPresent());
        assertEquals("New Group Name", result.get().getGroupName());
        verify(courseGroupRepo, times(1)).findById(1L);
        verify(courseGroupRepo, times(1)).save(any(CourseGroup.class));
    }

    @Test
    void testUpdateCourseGroup_GroupNotFound() {
        when(courseGroupRepo.findById(anyLong())).thenReturn(Optional.empty());

        Optional<CourseGroup> result = courseGroupService.updateCourseGroup(1L, new CourseGroup());

        assertFalse(result.isPresent());
        verify(courseGroupRepo, times(1)).findById(1L);
    }

    @Test
    void testDeleteCourseGroup() {
        doNothing().when(courseGroupRepo).deleteById(anyLong());

        courseGroupService.deleteCourseGroup(1L);

        verify(courseGroupRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCourseGroupByGroupName() {
        doNothing().when(courseGroupRepo).deleteByGroupName(anyString());

        courseGroupService.deleteCourseGroupByGroupName("Test Group");

        verify(courseGroupRepo, times(1)).deleteByGroupName("Test Group");
    }
}

