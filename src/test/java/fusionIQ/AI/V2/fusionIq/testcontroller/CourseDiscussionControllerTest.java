package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.CourseDiscussionController;
import fusionIQ.AI.V2.fusionIq.data.CourseDiscussion;
import fusionIQ.AI.V2.fusionIq.service.CourseDiscussionService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CourseDiscussionControllerTest {

    @Mock
    private CourseDiscussionService courseDiscussionService;

    @InjectMocks
    private CourseDiscussionController courseDiscussionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCourseDiscussions() {
        CourseDiscussion discussion1 = new CourseDiscussion();
        CourseDiscussion discussion2 = new CourseDiscussion();
        List<CourseDiscussion> discussions = Arrays.asList(discussion1, discussion2);

        when(courseDiscussionService.getAllCourseDiscussions()).thenReturn(discussions);

        List<CourseDiscussion> result = courseDiscussionController.getAllCourseDiscussions();

        assertEquals(2, result.size());
        verify(courseDiscussionService, times(1)).getAllCourseDiscussions();
    }

    @Test
    void testGetCourseDiscussionById() {
        CourseDiscussion discussion = new CourseDiscussion();
        discussion.setId(1L);

        when(courseDiscussionService.getCourseDiscussionById(1L)).thenReturn(Optional.of(discussion));

        ResponseEntity<CourseDiscussion> response = courseDiscussionController.getCourseDiscussionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(discussion, response.getBody());
    }

    @Test
    void testGetCourseDiscussionById_NotFound() {
        when(courseDiscussionService.getCourseDiscussionById(1L)).thenReturn(Optional.empty());

        ResponseEntity<CourseDiscussion> response = courseDiscussionController.getCourseDiscussionById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }




    @Test
    void testUpdateCourseDiscussion() {
        Long discussionId = 1L;
        CourseDiscussion updatedDiscussion = new CourseDiscussion();
        updatedDiscussion.setId(discussionId);
        updatedDiscussion.setDiscussionContent("Updated Content");

        when(courseDiscussionService.updateCourseDiscussion(discussionId, updatedDiscussion)).thenReturn(updatedDiscussion);

        ResponseEntity<CourseDiscussion> response = courseDiscussionController.updateCourseDiscussion(discussionId, updatedDiscussion);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDiscussion, response.getBody());
    }

    @Test
    void testDeleteCourseDiscussion() {
        Long discussionId = 1L;

        doNothing().when(courseDiscussionService).deleteCourseDiscussion(discussionId);

        ResponseEntity<Void> response = courseDiscussionController.deleteCourseDiscussion(discussionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courseDiscussionService, times(1)).deleteCourseDiscussion(discussionId);
    }
}
