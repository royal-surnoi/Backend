package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.MockTestInterview;
import fusionIQ.AI.V2.fusionIq.data.TrainingRoom;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.MockTestInterviewRepository;
import fusionIQ.AI.V2.fusionIq.repository.TrainingRoomRepo;
import fusionIQ.AI.V2.fusionIq.repository.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainingRoomService {
    @Autowired
    private TrainingRoomRepo trainingRoomRepo;

    @Autowired
    private VideoRepo videoRepo;
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private MockTestInterviewRepository mockTestInterviewRepo;

    @Autowired
    private UserService userService;

    public TrainingRoom createRoom(String name, User teacher, Course course, LocalDateTime scheduledTime) {
        TrainingRoom room = new TrainingRoom();
        room.setName(name);
        room.setTeacher(teacher);
        room.setCourse(course);
        room.setScheduledTime(scheduledTime); // Set scheduled time
        room.setScheduledTime(scheduledTime);
        room.setConferenceUrl(generateConferenceUrl());
        return trainingRoomRepo.save(room);
    }

    public TrainingRoom addStudentToRoom(Long roomId, User student) {
        TrainingRoom room = findById(roomId);
        if (room != null) {
            room.getStudents().add(student);
            trainingRoomRepo.save(room);
        }
        return room;
    }


    private String generateConferenceUrl() {
        return "https://meet.jit.si/" + UUID.randomUUID().toString();
    }


    public TrainingRoom findById(Long roomId) {
        return trainingRoomRepo.findById(roomId).orElse(null);
    }

    public List<TrainingRoom> findAll() {
        return trainingRoomRepo.findAll();
    }

    public List<TrainingRoom> getUpcomingRoomsForInstructor(User instructor) {
        LocalDateTime now = LocalDateTime.now();
        return trainingRoomRepo.findByTeacherAndScheduledTimeAfter(instructor, now);
    }

    public List<TrainingRoom> findRoomsByCourse(Long courseId,Long teacherId) {
        return trainingRoomRepo.findByCourseIdAndUserId(courseId,teacherId);
    }

    public TrainingRoom createClass(String name, User teacher, LocalDateTime scheduledTime) {
        TrainingRoom room = new TrainingRoom();
        room.setName(name);
        room.setTeacher(teacher);
        room.setScheduledTime(scheduledTime);
        room.setConferenceUrl(generateConferenceUrl());
        return trainingRoomRepo.save(room);
    }






    public TrainingRoom createRoomToUsers(String name, User teacher, Course course, LocalDateTime scheduledTime, Set<User> users) {
        TrainingRoom room = new TrainingRoom();
        room.setName(name);
        room.setTeacher(teacher);
        room.setCourse(course);
        room.setScheduledTime(scheduledTime);
        room.setConferenceUrl(generateConferenceUrl());
        room.setStudents(users);
        return trainingRoomRepo.save(room);
    }

    public void notifyUsersOfRoomDetails(TrainingRoom room, Set<User> users) {
        for (User user : users) {
            String subject = "Training Room Details: " + room.getName();
            String message = "Dear " + user.getName() + ",\n\nYou have been added to the training room: " + room.getName() +
                    "\nScheduled Time: " + room.getScheduledTime() +
                    "\n\nConference URL: " + room.getConferenceUrl() +
                    "\n\nBest Regards,\nYour Training Team";
            sendEmail(user.getEmail(), subject, message);
        }
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
    public List<TrainingRoom> findRoomsByCourseAndUser(Long courseId, Long userId) {
        List<TrainingRoom> rooms = trainingRoomRepo.findByCourseId(courseId);
        return rooms.stream()
                .filter(room -> room.getTeacher().getId() == (userId) ||
                        room.getStudents().stream().anyMatch(student -> student.getId() == (userId)))
                .collect(Collectors.toList());
    }

    public TrainingRoom createForMock(String name, Long teacherId, Long mockId) {
        // Fetch the teacher by teacherId
        User teacher = userService.findById(teacherId);
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher not found for id: " + teacherId);
        }

        // Fetch the MockTestInterview by mockId
        MockTestInterview mockTestInterview = mockTestInterviewRepo.findById(mockId)
                .orElseThrow(() -> new IllegalArgumentException("MockTestInterview not found for id: " + mockId));

        // Create and populate the TrainingRoom object
        TrainingRoom room = new TrainingRoom();
        room.setName(name);
        room.setTeacher(teacher);
        room.setConferenceUrl(generateConferenceUrl());
        room.setMockTestInterview(mockTestInterview);

        // Save and return the created TrainingRoom
        return trainingRoomRepo.save(room);
    }

    public List<Map<String, Object>> getTrainingRoomsByMockId(Long mockId) {
        // Fetch training rooms by mockId
        List<TrainingRoom> trainingRooms = trainingRoomRepo.findByMockTestInterviewId(mockId);

        // Create a list of maps to store the required fields
        List<Map<String, Object>> trainingRoomDetails = new ArrayList<>();

        // Extract necessary fields from each training room
        trainingRooms.forEach(trainingRoom -> {
            Map<String, Object> trainingRoomMap = new HashMap<>();
            trainingRoomMap.put("trainingRoomId", trainingRoom.getId());
            trainingRoomMap.put("name", trainingRoom.getName());
            trainingRoomMap.put("conferenceUrl", trainingRoom.getConferenceUrl());
            trainingRoomDetails.add(trainingRoomMap);
        });

        return trainingRoomDetails;
    }

    public List<TrainingRoom> getTrainingRoomsByCourseId(Long courseId) {
        return trainingRoomRepo.findByCourse_Id(courseId);
    }
}
