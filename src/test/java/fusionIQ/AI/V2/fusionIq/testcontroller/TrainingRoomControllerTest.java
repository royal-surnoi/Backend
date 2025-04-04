package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.TrainingRoomController;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.TrainingRoom;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.service.CourseService;
import fusionIQ.AI.V2.fusionIq.service.TrainingRoomService;
import fusionIQ.AI.V2.fusionIq.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TrainingRoomControllerTest {

    @InjectMocks
    private TrainingRoomController trainingRoomController;

    @Mock
    private TrainingRoomService trainingRoomService;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRoom() {
        User user = new User();
        user.setId(1L);
        Course course = new Course();
        course.setId(1L);
        LocalDateTime scheduledTime = LocalDateTime.now().plusDays(1);

        TrainingRoom room = new TrainingRoom();
        room.setName("Room 1");

        when(userService.findById(1L)).thenReturn(user);
        when(courseService.findById(1L)).thenReturn(course);
        when(trainingRoomService.createRoom("Room 1", user, course, scheduledTime)).thenReturn(room);

        ResponseEntity<TrainingRoom> response = trainingRoomController.createRoom("Room 1", 1L, 1L, scheduledTime);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Room 1", response.getBody().getName());
    }

    @Test
    void testGetRoomsByCourse() {
        Long courseId = 1L;
        Long teacherId = 1L;

        List<TrainingRoom> rooms = Arrays.asList(new TrainingRoom(), new TrainingRoom());

        when(trainingRoomService.findRoomsByCourse(courseId, teacherId)).thenReturn(rooms);

        ResponseEntity<List<TrainingRoom>> response = trainingRoomController.getRoomsByCourse(courseId, teacherId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testAddStudentToRoom() {
        User student = new User();
        student.setId(2L);
        TrainingRoom room = new TrainingRoom();
        room.setId(1L);

        when(userService.findById(2L)).thenReturn(student);
        when(trainingRoomService.addStudentToRoom(1L, student)).thenReturn(room);

        ResponseEntity<TrainingRoom> response = trainingRoomController.addStudentToRoom(1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        verify(trainingRoomService, times(1)).addStudentToRoom(1L, student);
    }

    @Test
    void testGetRoom() {
        TrainingRoom room = new TrainingRoom();
        room.setId(1L);

        when(trainingRoomService.findById(1L)).thenReturn(room);

        ResponseEntity<TrainingRoom> response = trainingRoomController.getRoom(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testCreateRoomToUsers() {
        User user = new User();
        user.setId(1L);
        Course course = new Course();
        course.setId(1L);
        LocalDateTime scheduledTime = LocalDateTime.now().plusDays(1);
        Set<User> users = new HashSet<>();
        users.add(user);

        TrainingRoom room = new TrainingRoom();
        room.setName("Room 1");

        when(userService.findById(1L)).thenReturn(user);
        when(courseService.findById(1L)).thenReturn(course);
        when(userService.findAllByIds(anyList())).thenReturn(users);
        when(trainingRoomService.createRoomToUsers("Room 1", user, course, scheduledTime, users)).thenReturn(room);

        ResponseEntity<TrainingRoom> response = trainingRoomController.createRoomToUsers("Room 1", 1L, 1L, scheduledTime, Arrays.asList(1L));

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Room 1", response.getBody().getName());
        verify(trainingRoomService, times(1)).notifyUsersOfRoomDetails(any(TrainingRoom.class), anySet());
    }

    @Test
    void testGetRoomDetails() {
        TrainingRoom room = new TrainingRoom();
        room.setId(1L);
        User teacher = new User();
        teacher.setId(1L);
        room.setTeacher(teacher);

        when(trainingRoomService.findById(1L)).thenReturn(room);

        ResponseEntity<TrainingRoom> response = trainingRoomController.getRoomDetails(1L, 1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetRoomsByCourseAndUser() {
        Long courseId = 1L;
        Long userId = 1L;

        List<TrainingRoom> rooms = Arrays.asList(new TrainingRoom(), new TrainingRoom());

        when(trainingRoomService.findRoomsByCourseAndUser(courseId, userId)).thenReturn(rooms);

        ResponseEntity<List<TrainingRoom>> response = trainingRoomController.getRoomsByCourseAndUser(courseId, userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
    }
}

