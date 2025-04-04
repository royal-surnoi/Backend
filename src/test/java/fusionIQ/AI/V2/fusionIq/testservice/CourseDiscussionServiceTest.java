package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.CourseDiscussion;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.CourseDiscussionRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.CourseDiscussionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseDiscussionServiceTest {

    @Mock
    private CourseDiscussionRepo courseDiscussionRepo;

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CourseDiscussionService courseDiscussionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCourseDiscussions() {
        List<CourseDiscussion> courseDiscussions = Arrays.asList(new CourseDiscussion(), new CourseDiscussion());

        when(courseDiscussionRepo.findAll()).thenReturn(courseDiscussions);

        List<CourseDiscussion> result = courseDiscussionService.getAllCourseDiscussions();

        assertEquals(2, result.size());
        verify(courseDiscussionRepo, times(1)).findAll();
    }

    @Test
    void testGetCourseDiscussionById() {
        CourseDiscussion courseDiscussion = new CourseDiscussion();
        courseDiscussion.setId(1L);

        when(courseDiscussionRepo.findById(anyLong())).thenReturn(Optional.of(courseDiscussion));

        Optional<CourseDiscussion> result = courseDiscussionService.getCourseDiscussionById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(courseDiscussionRepo, times(1)).findById(1L);
    }

    @Test
    void testCreateCourseDiscussion() {
        Course course = new Course();
        course.setId(1L);
        User user = new User();
        user.setId(1L);
        CourseDiscussion courseDiscussion = new CourseDiscussion();
        courseDiscussion.setDiscussionContent("Test Discussion");

        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(course));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(courseDiscussionRepo.save(any(CourseDiscussion.class))).thenReturn(courseDiscussion);

        CourseDiscussion result = courseDiscussionService.createCourseDiscussion(1L, 1L, courseDiscussion);

        assertNotNull(result);
        assertEquals("Test Discussion", result.getDiscussionContent());
        verify(courseDiscussionRepo, times(1)).save(courseDiscussion);
    }

    @Test
    void testUpdateCourseDiscussion() {
        CourseDiscussion existingCourseDiscussion = new CourseDiscussion();
        existingCourseDiscussion.setId(1L);
        existingCourseDiscussion.setDiscussionContent("Old Content");

        CourseDiscussion updatedCourseDiscussion = new CourseDiscussion();
        updatedCourseDiscussion.setDiscussionContent("New Content");
        updatedCourseDiscussion.setReplyContent("New Reply");

        when(courseDiscussionRepo.findById(anyLong())).thenReturn(Optional.of(existingCourseDiscussion));
        when(courseDiscussionRepo.save(any(CourseDiscussion.class))).thenReturn(existingCourseDiscussion);

        CourseDiscussion result = courseDiscussionService.updateCourseDiscussion(1L, updatedCourseDiscussion);

        assertEquals("New Content", result.getDiscussionContent());
        assertEquals("New Reply", result.getReplyContent());
        verify(courseDiscussionRepo, times(1)).findById(1L);
        verify(courseDiscussionRepo, times(1)).save(existingCourseDiscussion);
    }

    @Test
    void testDeleteCourseDiscussion() {
        doNothing().when(courseDiscussionRepo).deleteById(anyLong());

        courseDiscussionService.deleteCourseDiscussion(1L);

        verify(courseDiscussionRepo, times(1)).deleteById(1L);
    }
}

