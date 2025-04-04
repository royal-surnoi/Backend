package fusionIQ.AI.V2.fusionIq.controller;


import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.service.CourseService;
import fusionIQ.AI.V2.fusionIq.service.MockTestInterviewService;
import fusionIQ.AI.V2.fusionIq.service.TrainingRoomService;
import fusionIQ.AI.V2.fusionIq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/training-rooms")
public class TrainingRoomController {

    @Autowired
    private TrainingRoomService trainingRoomService;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private MockTestInterviewService mockTestInterviewService;


    @PostMapping("/create")
    public ResponseEntity<TrainingRoom> createRoom(@RequestParam String name,
                                                   @RequestParam Long userId,
                                                   @RequestParam long courseId,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Course course = courseService.findById(courseId);
        if (course == null) {
            return ResponseEntity.badRequest().body(null);
        }

        TrainingRoom room = trainingRoomService.createRoom(name, user, course, scheduledTime);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/by-course/{courseId}/{teacherId}")
    public ResponseEntity<List<TrainingRoom>> getRoomsByCourse(@PathVariable Long courseId, @PathVariable Long teacherId) {
        List<TrainingRoom> rooms = trainingRoomService.findRoomsByCourse(courseId,teacherId);
        if
        (rooms.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/{roomId}/add-student")
    public ResponseEntity<TrainingRoom> addStudentToRoom(@PathVariable Long roomId, @RequestParam Long studentId) {
        User student = userService.findById(studentId);
        if (student == null) {
            return ResponseEntity.badRequest().body(null);
        }

        TrainingRoom room = trainingRoomService.addStudentToRoom(roomId, student);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(room);
    }


    @GetMapping("/{roomId}")
    public ResponseEntity<TrainingRoom> getRoom(@PathVariable Long roomId) {
        TrainingRoom room = trainingRoomService.findById(roomId);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }

    @GetMapping("/allTrainingRooms")
    public ResponseEntity<List<TrainingRoom>> getAllRooms() {
        List<TrainingRoom> rooms = trainingRoomService.findAll();
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/createRoomToUsers")
    public ResponseEntity<TrainingRoom> createRoomToUsers(@RequestParam String name,
                                                   @RequestParam Long userId,
                                                   @RequestParam long courseId,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime,
                                                   @RequestParam List<Long> userIds) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Course course = courseService.findById(courseId);
        if (course == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Set<User> users = userService.findAllByIds(userIds);
        if (users.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        TrainingRoom room = trainingRoomService.createRoomToUsers(name, user, course, scheduledTime, users);
        trainingRoomService.notifyUsersOfRoomDetails(room, users);
        return ResponseEntity.ok(room);
    }
    @PostMapping("/createRoom")
    public ResponseEntity<TrainingRoom> createRoom(@RequestParam String name,
                                                   @RequestParam Long userId,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        TrainingRoom room = trainingRoomService.createClass(name, user, scheduledTime);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/{roomId}/details")
    public ResponseEntity<TrainingRoom> getRoomDetails(@PathVariable Long roomId, @RequestParam Long userId) {
        TrainingRoom room = trainingRoomService.findById(roomId);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the user is part of the room (either as a teacher or a student)
        if (room.getTeacher().getId() == (userId) || room.getStudents().stream().anyMatch(student -> student.getId() == (userId))) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/by-course/{courseId}/user/{userId}")
    public ResponseEntity<List<TrainingRoom>> getRoomsByCourseAndUser(@PathVariable Long courseId, @PathVariable Long userId) {
        List<TrainingRoom> rooms = trainingRoomService.findRoomsByCourseAndUser(courseId, userId);
        if (rooms.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rooms);
    }

//    @PostMapping("/createForMock")
//    public ResponseEntity<TrainingRoom> createRoomForMock(
//            @RequestParam String name,
//            @RequestParam Long teacherId,
//            @RequestParam Long mockId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime) {
//
//        try {
//            // Call the service to create a TrainingRoom
//            TrainingRoom room = trainingRoomService.createForMock(name, teacherId, mockId, scheduledTime);
//            return ResponseEntity.ok(room);
//        } catch (IllegalArgumentException e) {
//            // If there's an issue with the inputs (e.g., teacher or mockTestInterview not found)
//            return ResponseEntity.badRequest().body(null);
//        }



    @PostMapping("/createForMock")
    public ResponseEntity<TrainingRoom> createRoomForMock(
            @RequestParam String name,
            @RequestParam Long teacherId,
            @RequestParam Long mockId) {

        try {
            // Call the service to create a TrainingRoom
            TrainingRoom room = trainingRoomService.createForMock(name, teacherId, mockId);
            return ResponseEntity.ok(room);
        } catch (IllegalArgumentException e) {
            // If there's an issue with the inputs (e.g., teacher or mockTestInterview not found)
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getDetailsByMockId/{mockId}")
    public ResponseEntity<List<Map<String, Object>>> getTrainingRoomsByMockId(@PathVariable Long mockId) {
        // Call the service method to get training room details
        List<Map<String, Object>> trainingRoomDetails = trainingRoomService.getTrainingRoomsByMockId(mockId);

        // Check if the list is empty and return an appropriate response
        if (trainingRoomDetails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if no data is found
        }

        // Return the training room details with a 200 OK status
        return new ResponseEntity<>(trainingRoomDetails, HttpStatus.OK);
    }

    @GetMapping("/rooms/{courseId}")
    public List<TrainingRoom> getTrainingRoomsByCourseId(@PathVariable Long courseId) {
        return trainingRoomService.getTrainingRoomsByCourseId(courseId);
    }
}






