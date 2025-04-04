package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TrainingRoomService trainingRoomService;
    @Autowired
    ProjectService projectService;
    @Autowired
    QuizService quizService;

    @Autowired
    AssignmentRepo assignmentRepo;
    @Autowired
    QuizRepo quizRepo;
    @Autowired
    ProjectRepo projectRepo;

    public DashboardOverview getDashboardOverview(Long instructorId) {
        User instructor = userRepo.findById(instructorId).orElseThrow(() -> new IllegalArgumentException("Invalid instructor ID"));

        List<Course> courses = courseService.getCoursesByUser(instructor);
        int totalCourses = courses.size();
        int activeStudents = courses.stream().mapToInt(course -> enrollmentService.getEnrollmentsByCourse(course).size()).sum();
        List<TrainingRoom> upcomingRooms = trainingRoomService.getUpcomingRoomsForInstructor(instructor);

        int upcomingClasses = upcomingRooms.size();

        int pendingAssignments = 10;

        return new DashboardOverview(totalCourses, activeStudents, upcomingClasses, pendingAssignments);
    }


    public List<Course> getInstructorCourses(Long instructorId) {
        User instructor = userRepo.findById(instructorId).orElseThrow(() -> new IllegalArgumentException("Invalid instructor ID"));
        return courseService.getCoursesByUser(instructor);
    }

    public User getInstructorByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
    }



    public List<Notification> getNotificationsByUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        return notificationService.getNotificationsByUser(user);
    }

    public List<Assignment> getAssignmentsByCourse(Long courseId) {
        Course course = courseService.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
        return assignmentService.getAssignmentsByCourse(course);
    }
    public UpcomingItemsResponse getUpcomingItemsForUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        LocalDateTime currentDate = LocalDateTime.now();

        List<Assignment> assessments = assignmentService.getUpcomingAssessmentsForUser(userId);
        List<Quiz> quizzes = quizService.getUpcomingQuizzesForUser(userId);
        List<Project> projects = projectService.getUpcomingProjectsForUser(userId);

        return new UpcomingItemsResponse(assessments, quizzes, projects);
    }


    public List<UserEnrollmentResponse> getEnrolledStudentsByInstructor(Long instructorId) {
        User instructor = userRepo.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid instructor ID"));

        List<Course> courses = courseService.getCoursesByUser(instructor);
        Map<User, List<String>> userCourseMap = new HashMap<>();

        for (Course course : courses) {
            List<User> students = enrollmentService.getEnrollmentsByCourse(course)
                    .stream()
                    .map(Enrollment::getUser)
                    .distinct()  // Ensure no duplicates
                    .collect(Collectors.toList());
            for (User student : students) {
                userCourseMap.computeIfAbsent(student, k -> new ArrayList<>()).add(course.getCourseTitle());
            }
        }

        List<UserEnrollmentResponse> response = new ArrayList<>();
        for (Map.Entry<User, List<String>> entry : userCourseMap.entrySet()) {
            response.add(new UserEnrollmentResponse(entry.getKey(), entry.getValue()));
        }

        return response;
    }

}
