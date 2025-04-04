package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.CourseMessage;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.CourseMessageRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.CourseMessageService;
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

class CourseMessageServiceTest {

    @Mock
    private CourseMessageRepo courseMessageRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CourseMessageService courseMessageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendCourseMessage_Success() {
        User fromUser = new User();
        fromUser.setId(1L);

        User toUser = new User();
        toUser.setId(2L);

        CourseMessage courseMessage = new CourseMessage();
        courseMessage.setFrom(fromUser);
        courseMessage.setTo(toUser);
        courseMessage.setSubject("Test Subject");
        courseMessage.setCourseMessage("Test Message");
        courseMessage.setSentAt(LocalDateTime.now());

        when(userRepo.findById(1L)).thenReturn(Optional.of(fromUser));
        when(userRepo.findById(2L)).thenReturn(Optional.of(toUser));
        when(courseMessageRepo.save(any(CourseMessage.class))).thenReturn(courseMessage);

        CourseMessage result = courseMessageService.sendCourseMessage(1L, 1L, 2L, "Test Subject", "Test Message");

        assertNotNull(result);
        assertEquals("Test Subject", result.getSubject());
        assertEquals("Test Message", result.getCourseMessage());
        assertEquals(fromUser, result.getFrom());
        assertEquals(toUser, result.getTo());
        verify(userRepo, times(1)).findById(1L);
        verify(userRepo, times(1)).findById(2L);
        verify(courseMessageRepo, times(1)).save(any(CourseMessage.class));
    }

    @Test
    void testSendCourseMessage_InvalidFromUserId() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> courseMessageService.sendCourseMessage(1L, 1L, 2L, "Test Subject", "Test Message"));

        verify(userRepo, times(1)).findById(1L);
        verify(userRepo, times(0)).findById(2L);
        verify(courseMessageRepo, times(0)).save(any(CourseMessage.class));
    }

    @Test
    void testSendCourseMessage_InvalidToUserId() {
        User fromUser = new User();
        fromUser.setId(1L);

        when(userRepo.findById(1L)).thenReturn(Optional.of(fromUser));
        when(userRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> courseMessageService.sendCourseMessage(1L, 1L, 2L, "Test Subject", "Test Message"));

        verify(userRepo, times(1)).findById(1L);
        verify(userRepo, times(1)).findById(2L);
        verify(courseMessageRepo, times(0)).save(any(CourseMessage.class));
    }

    @Test
    void testGetMessagesByCourseId() {
        CourseMessage courseMessage1 = new CourseMessage();
        courseMessage1.setCourseMessage("Message 1");

        CourseMessage courseMessage2 = new CourseMessage();
        courseMessage2.setCourseMessage("Message 2");

        when(courseMessageRepo.findByCourseId(anyLong())).thenReturn(List.of(courseMessage1, courseMessage2));

        List<CourseMessage> result = courseMessageService.getMessagesByCourseId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Message 1", result.get(0).getCourseMessage());
        assertEquals("Message 2", result.get(1).getCourseMessage());
        verify(courseMessageRepo, times(1)).findByCourseId(1L);
    }

    @Test
    void testGetAllMessages() {
        CourseMessage courseMessage1 = new CourseMessage();
        courseMessage1.setCourseMessage("Message 1");

        CourseMessage courseMessage2 = new CourseMessage();
        courseMessage2.setCourseMessage("Message 2");

        when(courseMessageRepo.findAll()).thenReturn(List.of(courseMessage1, courseMessage2));

        List<CourseMessage> result = courseMessageService.getAllMessages();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Message 1", result.get(0).getCourseMessage());
        assertEquals("Message 2", result.get(1).getCourseMessage());
        verify(courseMessageRepo, times(1)).findAll();
    }
}

