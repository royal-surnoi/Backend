package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.DashboardController;
import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.service.DashboardService;
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

class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOverview_Success() {
        Long instructorId = 1L;
        DashboardOverview overview = new DashboardOverview(5, 100, 3, 20);

        when(dashboardService.getDashboardOverview(instructorId)).thenReturn(overview);

        ResponseEntity<DashboardOverview> response = dashboardController.getOverview(instructorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(overview, response.getBody());
        verify(dashboardService, times(1)).getDashboardOverview(instructorId);
    }

    @Test
    void testGetCourses_Success() {
        Long instructorId = 1L;
        Course course1 = new Course();
        Course course2 = new Course();
        List<Course> courses = Arrays.asList(course1, course2);

        when(dashboardService.getInstructorCourses(instructorId)).thenReturn(courses);

        ResponseEntity<List<Course>> response = dashboardController.getCourses(instructorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(dashboardService, times(1)).getInstructorCourses(instructorId);
    }

    @Test
    void testGetInstructor_Success() {
        String email = "instructor@example.com";
        User instructor = new User();

        when(dashboardService.getInstructorByEmail(email)).thenReturn(instructor);

        ResponseEntity<User> response = dashboardController.getInstructor(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(instructor, response.getBody());
        verify(dashboardService, times(1)).getInstructorByEmail(email);
    }

    @Test
    void testGetNotifications_Success() {
        Long userId = 1L;
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(dashboardService.getNotificationsByUser(userId)).thenReturn(notifications);

        ResponseEntity<List<Notification>> response = dashboardController.getNotifications(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(dashboardService, times(1)).getNotificationsByUser(userId);
    }

    @Test
    void testGetAssignments_Success() {
        Long courseId = 1L;
        Assignment assignment1 = new Assignment();
        Assignment assignment2 = new Assignment();
        List<Assignment> assignments = Arrays.asList(assignment1, assignment2);

        when(dashboardService.getAssignmentsByCourse(courseId)).thenReturn(assignments);

        ResponseEntity<List<Assignment>> response = dashboardController.getAssignments(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(dashboardService, times(1)).getAssignmentsByCourse(courseId);
    }

    @Test
    void testGetUpcomingItems_Success() {
        Long userId = 1L;
        UpcomingItemsResponse upcomingItems = new UpcomingItemsResponse(Arrays.asList(new Assignment()), Arrays.asList(new Quiz()), Arrays.asList(new Project()));

        when(dashboardService.getUpcomingItemsForUser(userId)).thenReturn(upcomingItems);

        ResponseEntity<UpcomingItemsResponse> response = dashboardController.getUpcomingItems(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(upcomingItems, response.getBody());
        verify(dashboardService, times(1)).getUpcomingItemsForUser(userId);
    }

    @Test
    void testGetEnrolledStudents_Success() {
        Long instructorId = 1L;
        UserEnrollmentResponse student1 = new UserEnrollmentResponse(new User(), Arrays.asList("Course 1"));
        UserEnrollmentResponse student2 = new UserEnrollmentResponse(new User(), Arrays.asList("Course 2"));
        List<UserEnrollmentResponse> enrolledStudents = Arrays.asList(student1, student2);

        when(dashboardService.getEnrolledStudentsByInstructor(instructorId)).thenReturn(enrolledStudents);

        ResponseEntity<List<UserEnrollmentResponse>> response = dashboardController.getEnrolledStudents(instructorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(dashboardService, times(1)).getEnrolledStudentsByInstructor(instructorId);
    }
}
