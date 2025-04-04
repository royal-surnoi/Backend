
package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.TrainingRoom;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.TrainingRoomRepo;
import fusionIQ.AI.V2.fusionIq.service.TrainingRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrainingRoomServiceTest {

    @InjectMocks
    private TrainingRoomService trainingRoomService;

    @Mock
    private TrainingRoomRepo trainingRoomRepo;

    @Mock
    private JavaMailSender emailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRoom() {
        User teacher = new User();
        teacher.setId(1L);
        Course course = new Course();
        course.setId(1L);
        LocalDateTime scheduledTime = LocalDateTime.now().plusDays(1);

        TrainingRoom room = new TrainingRoom();
        room.setName("Room 1");
        room.setTeacher(teacher);
        room.setCourse(course);
        room.setScheduledTime(scheduledTime);

        when(trainingRoomRepo.save(any(TrainingRoom.class))).thenReturn(room);

        TrainingRoom createdRoom = trainingRoomService.createRoom("Room 1", teacher, course, scheduledTime);

        assertNotNull(createdRoom);
        assertEquals("Room 1", createdRoom.getName());
        assertEquals(teacher, createdRoom.getTeacher());
        assertEquals(course, createdRoom.getCourse());
        assertEquals(scheduledTime, createdRoom.getScheduledTime());
        verify(trainingRoomRepo, times(1)).save(any(TrainingRoom.class));
    }

    @Test
    void testAddStudentToRoom() {
        User student = new User();
        student.setId(2L);
        TrainingRoom room = new TrainingRoom();
        room.setId(1L);
        room.setStudents(new HashSet<>());

        when(trainingRoomRepo.findById(1L)).thenReturn(Optional.of(room));
        when(trainingRoomRepo.save(any(TrainingRoom.class))).thenReturn(room);

        TrainingRoom updatedRoom = trainingRoomService.addStudentToRoom(1L, student);

        assertNotNull(updatedRoom);
        assertTrue(updatedRoom.getStudents().contains(student));
        verify(trainingRoomRepo, times(1)).save(any(TrainingRoom.class));
    }

    @Test
    void testFindById() {
        TrainingRoom room = new TrainingRoom();
        room.setId(1L);

        when(trainingRoomRepo.findById(1L)).thenReturn(Optional.of(room));

        TrainingRoom foundRoom = trainingRoomService.findById(1L);

        assertNotNull(foundRoom);
        assertEquals(1L, foundRoom.getId());
    }

    @Test
    void testFindAll() {
        List<TrainingRoom> rooms = Arrays.asList(new TrainingRoom(), new TrainingRoom());

        when(trainingRoomRepo.findAll()).thenReturn(rooms);

        List<TrainingRoom> allRooms = trainingRoomService.findAll();

        assertEquals(2, allRooms.size());
        verify(trainingRoomRepo, times(1)).findAll();
    }

    @Test
    void testFindRoomsByCourse() {
        Long courseId = 1L;
        Long teacherId = 1L;
        List<TrainingRoom> rooms = Arrays.asList(new TrainingRoom(), new TrainingRoom());

        when(trainingRoomRepo.findByCourseIdAndUserId(courseId, teacherId)).thenReturn(rooms);

        List<TrainingRoom> foundRooms = trainingRoomService.findRoomsByCourse(courseId, teacherId);

        assertEquals(2, foundRooms.size());
        verify(trainingRoomRepo, times(1)).findByCourseIdAndUserId(courseId, teacherId);
    }

    @Test
    void testNotifyUsersOfRoomDetails() {
        TrainingRoom room = new TrainingRoom();
        room.setName("Room 1");

        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");

        Set<User> users = new HashSet<>();
        users.add(user);

        trainingRoomService.notifyUsersOfRoomDetails(room, users);

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}

