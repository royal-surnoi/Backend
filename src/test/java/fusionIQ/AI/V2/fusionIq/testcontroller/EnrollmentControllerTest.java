package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.EnrollmentController;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Enrollment;
import fusionIQ.AI.V2.fusionIq.data.LoginRequest;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.service.EnrollmentService;
import fusionIQ.AI.V2.fusionIq.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EnrollmentControllerTest {

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private EnrollmentController enrollmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEnrollment_Success() {
        long userId = 1L;
        long courseId = 1L;
        Enrollment enrollment = new Enrollment();
        Enrollment savedEnrollment = new Enrollment();

        when(enrollmentService.saveEnrollment(enrollment, userId, courseId)).thenReturn(savedEnrollment);

        ResponseEntity<Enrollment> response = enrollmentController.createEnrollment(enrollment, userId, courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedEnrollment, response.getBody());
        verify(enrollmentService, times(1)).saveEnrollment(enrollment, userId, courseId);
    }

    @Test
    void testCreateEnrollment_BadRequest() {
        long userId = 1L;
        long courseId = 1L;
        Enrollment enrollment = new Enrollment();

        when(enrollmentService.saveEnrollment(enrollment, userId, courseId)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Enrollment> response = enrollmentController.createEnrollment(enrollment, userId, courseId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(enrollmentService, times(1)).saveEnrollment(enrollment, userId, courseId);
    }

    @Test
    void testGetEnrollmentById_Success() {
        long enrollmentId = 1L;
        Enrollment enrollment = new Enrollment();

        when(enrollmentService.findEnrollmentById(enrollmentId)).thenReturn(Optional.of(enrollment));

        ResponseEntity<Enrollment> response = enrollmentController.getEnrollmentById(enrollmentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(enrollment, response.getBody());
        verify(enrollmentService, times(1)).findEnrollmentById(enrollmentId);
    }

    @Test
    void testGetEnrollmentById_NotFound() {
        long enrollmentId = 1L;

        when(enrollmentService.findEnrollmentById(enrollmentId)).thenReturn(Optional.empty());

        ResponseEntity<Enrollment> response = enrollmentController.getEnrollmentById(enrollmentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(enrollmentService, times(1)).findEnrollmentById(enrollmentId);
    }

    @Test
    void testGetAllEnrollments_Success() {
        Enrollment enrollment1 = new Enrollment();
        Enrollment enrollment2 = new Enrollment();
        List<Enrollment> enrollments = Arrays.asList(enrollment1, enrollment2);

        when(enrollmentService.findAllEnrollments()).thenReturn(enrollments);

        ResponseEntity<List<Enrollment>> response = enrollmentController.getAllEnrollments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(enrollmentService, times(1)).findAllEnrollments();
    }

    @Test
    void testGetEnrollmentsByUser_Success() {
        long userId = 1L;
        Enrollment enrollment1 = new Enrollment();
        Enrollment enrollment2 = new Enrollment();
        List<Enrollment> enrollments = Arrays.asList(enrollment1, enrollment2);

        when(enrollmentService.findEnrollmentsByUser(userId)).thenReturn(enrollments);

        ResponseEntity<List<Enrollment>> response = enrollmentController.getEnrollmentsByUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(enrollmentService, times(1)).findEnrollmentsByUser(userId);
    }

    @Test
    void testGetEnrollmentsByCourse_Success() {
        long courseId = 1L;
        Enrollment enrollment1 = new Enrollment();
        Enrollment enrollment2 = new Enrollment();
        List<Enrollment> enrollments = Arrays.asList(enrollment1, enrollment2);

        when(enrollmentService.findEnrollmentsByCourse(courseId)).thenReturn(enrollments);

        ResponseEntity<List<Enrollment>> response = enrollmentController.getEnrollmentsByCourse(courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(enrollmentService, times(1)).findEnrollmentsByCourse(courseId);
    }

    @Test
    void testDeleteEnrollmentByUserAndCourse_Success() {
        long userId = 1L;
        long courseId = 1L;

        doNothing().when(enrollmentService).deleteEnrollment(userId, courseId);

        ResponseEntity<Void> response = enrollmentController.deleteEnrollmentByUserAndCourse(userId, courseId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(enrollmentService, times(1)).deleteEnrollment(userId, courseId);
    }

    @Test
    void testGetEnrollmentByUserAndCourse_Success() {
        long userId = 1L;
        long courseId = 1L;
        Enrollment enrollment = new Enrollment();

        when(enrollmentService.findEnrollmentByUserAndCourse(userId, courseId)).thenReturn(enrollment);

        ResponseEntity<Enrollment> response = enrollmentController.getEnrollmentByUserAndCourse(userId, courseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(enrollment, response.getBody());
        verify(enrollmentService, times(1)).findEnrollmentByUserAndCourse(userId, courseId);
    }

    @Test
    void testGetEnrollmentByUserAndCourse_NotFound() {
        long userId = 1L;
        long courseId = 1L;

        when(enrollmentService.findEnrollmentByUserAndCourse(userId, courseId)).thenReturn(null);

        ResponseEntity<Enrollment> response = enrollmentController.getEnrollmentByUserAndCourse(userId, courseId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(enrollmentService, times(1)).findEnrollmentByUserAndCourse(userId, courseId);
    }

    @Test
    void testUpdateEnrollment_Success() {
        long enrollmentId = 1L;
        Enrollment updatedEnrollment = new Enrollment();
        Enrollment savedEnrollment = new Enrollment();

        when(enrollmentService.updateEnrollment(enrollmentId, updatedEnrollment)).thenReturn(savedEnrollment);

        ResponseEntity<Enrollment> response = enrollmentController.updateEnrollment(enrollmentId, updatedEnrollment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedEnrollment, response.getBody());
        verify(enrollmentService, times(1)).updateEnrollment(enrollmentId, updatedEnrollment);
    }

    @Test
    void testUpdateEnrollment_BadRequest() {
        long enrollmentId = 1L;
        Enrollment updatedEnrollment = new Enrollment();

        when(enrollmentService.updateEnrollment(enrollmentId, updatedEnrollment)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Enrollment> response = enrollmentController.updateEnrollment(enrollmentId, updatedEnrollment);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(enrollmentService, times(1)).updateEnrollment(enrollmentId, updatedEnrollment);
    }

    @Test
    void testGenerateOtp_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");

        String otp = "123456";

        when(enrollmentService.generateAndSaveOTP(loginRequest.getEmail())).thenReturn(otp);

        ResponseEntity<String> response = enrollmentController.sendOtp(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OTP has been sent to your email", response.getBody());
        verify(enrollmentService, times(1)).generateAndSaveOTP(loginRequest.getEmail());
        verify(enrollmentService, times(1)).sendOTPByEmail(loginRequest.getEmail(), otp);
    }

    @Test
    void testGenerateOtp_Failure() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");

        when(enrollmentService.generateAndSaveOTP(loginRequest.getEmail())).thenReturn(null);

        ResponseEntity<String> response = enrollmentController.sendOtp(loginRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Failed to generate OTP", response.getBody());
        verify(enrollmentService, times(1)).generateAndSaveOTP(loginRequest.getEmail());
        verify(enrollmentService, never()).sendOTPByEmail(anyString(), anyString());
    }



    @Test
    void testVerifyOtp_Failure() {
        String email = "test@example.com";
        String otp = "123456";
        long userId = 1L;
        long courseId = 1L;

        when(enrollmentService.verifyOTP(email, otp)).thenReturn(false);

        ResponseEntity<String> response = enrollmentController.verifyOTP(email, otp, userId, courseId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid or expired OTP", response.getBody());
        verify(enrollmentService, times(1)).verifyOTP(email, otp);
        verify(enrollmentService, never()).enrollInCourse(anyLong(), anyLong());
        verify(enrollmentService, never()).sendEnrollmentConfirmationEmail(anyString(), anyString(), anyString(), anyLong());
        verify(notificationService, never()).createNotification(anyLong(), anyString());
    }
}
