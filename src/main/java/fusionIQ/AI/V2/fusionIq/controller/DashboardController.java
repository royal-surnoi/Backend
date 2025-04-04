package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/overview")
    public ResponseEntity<DashboardOverview> getOverview(@RequestParam Long instructorId) {
        DashboardOverview overview = dashboardService.getDashboardOverview(instructorId);
        return ResponseEntity.ok(overview);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getCourses(@RequestParam Long instructorId) {
        List<Course> courses = dashboardService.getInstructorCourses(instructorId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/instructor")
    public ResponseEntity<User> getInstructor(@RequestParam String email) {
        User instructor = dashboardService.getInstructorByEmail(email);
        return ResponseEntity.ok(instructor);
    }


    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam Long userId) {
        List<Notification> notifications = dashboardService.getNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/assignments")
    public ResponseEntity<List<Assignment>> getAssignments(@RequestParam Long courseId) {
        List<Assignment> assignments = dashboardService.getAssignmentsByCourse(courseId);
        return ResponseEntity.ok(assignments);
    }

    // Additional endpoints for creating assignments, scheduling sessions, etc.
    @GetMapping("/upcoming-items")
    public ResponseEntity<UpcomingItemsResponse> getUpcomingItems(@RequestParam Long userId) {
        UpcomingItemsResponse upcomingItems = dashboardService.getUpcomingItemsForUser(userId);
        return ResponseEntity.ok(upcomingItems);
    }


    @GetMapping("/enrolled-students")
    public ResponseEntity<List<UserEnrollmentResponse>> getEnrolledStudents(@RequestParam Long instructorId) {
        List<UserEnrollmentResponse> enrolledStudents = dashboardService.getEnrolledStudentsByInstructor(instructorId);
        return ResponseEntity.ok(enrolledStudents);
    }


}
