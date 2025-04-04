package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.CourseMessageController;
import fusionIQ.AI.V2.fusionIq.data.CourseMessage;
import fusionIQ.AI.V2.fusionIq.service.CourseMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourseMessageControllerTest {

    @Mock
    private CourseMessageService courseMessageService;

    @InjectMocks
    private CourseMessageController courseMessageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendCourseMessage_Success() {
        Long courseId = 1L;
        Long fromUserId = 1L;
        Long toUserId = 2L;
        String subject = "Test Subject";
        String message = "Test Message";

        CourseMessage courseMessage = new CourseMessage();
        courseMessage.setSubject(subject);
        courseMessage.setCourseMessage(message);

        when(courseMessageService.sendCourseMessage(courseId, fromUserId, toUserId, subject, message))
                .thenReturn(courseMessage);

        ResponseEntity<CourseMessage> response = courseMessageController.sendCourseMessage(
                courseId, fromUserId, toUserId, subject, message);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(courseMessage, response.getBody());
        verify(courseMessageService, times(1)).sendCourseMessage(courseId, fromUserId, toUserId, subject, message);
    }

    @Test
    void testSendCourseMessage_BadRequest() {
        Long courseId = 1L;
        Long fromUserId = 1L;
        Long toUserId = 2L;
        String subject = "Test Subject";
        String message = "Test Message";

        when(courseMessageService.sendCourseMessage(courseId, fromUserId, toUserId, subject, message))
                .thenThrow(new IllegalArgumentException("Invalid fromUserId"));

        ResponseEntity<CourseMessage> response = courseMessageController.sendCourseMessage(
                courseId, fromUserId, toUserId, subject, message);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(courseMessageService, times(1)).sendCourseMessage(courseId, fromUserId, toUserId, subject, message);
    }

    @Test
    void testSendCourseMessage_InternalServerError() {
        Long courseId = 1L;
        Long fromUserId = 1L;
        Long toUserId = 2L;
        String subject = "Test Subject";
        String message = "Test Message";

        when(courseMessageService.sendCourseMessage(courseId, fromUserId, toUserId, subject, message))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<CourseMessage> response = courseMessageController.sendCourseMessage(
                courseId, fromUserId, toUserId, subject, message);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(courseMessageService, times(1)).sendCourseMessage(courseId, fromUserId, toUserId, subject, message);
    }

    @Test
    void testGetMessagesByCourseId() {
        Long courseId = 1L;
        CourseMessage message1 = new CourseMessage();
        CourseMessage message2 = new CourseMessage();
        List<CourseMessage> messages = Arrays.asList(message1, message2);

        when(courseMessageService.getMessagesByCourseId(courseId)).thenReturn(messages);

        ResponseEntity<List<CourseMessage>> response = courseMessageController.getMessagesByCourseId(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(courseMessageService, times(1)).getMessagesByCourseId(courseId);
    }

    @Test
    void testGetMessagesByCourseId_InternalServerError() {
        Long courseId = 1L;

        when(courseMessageService.getMessagesByCourseId(courseId))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<List<CourseMessage>> response = courseMessageController.getMessagesByCourseId(courseId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(courseMessageService, times(1)).getMessagesByCourseId(courseId);
    }

    @Test
    void testGetAllMessages() {
        CourseMessage message1 = new CourseMessage();
        CourseMessage message2 = new CourseMessage();
        List<CourseMessage> messages = Arrays.asList(message1, message2);

        when(courseMessageService.getAllMessages()).thenReturn(messages);

        ResponseEntity<List<CourseMessage>> response = courseMessageController.getAllMessages();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(courseMessageService, times(1)).getAllMessages();
    }

    @Test
    void testGetAllMessages_InternalServerError() {
        when(courseMessageService.getAllMessages()).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<List<CourseMessage>> response = courseMessageController.getAllMessages();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(courseMessageService, times(1)).getAllMessages();
    }
}
