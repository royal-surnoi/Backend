package fusionIQ.AI.V2.fusionIq.testservice;


import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.EnrollmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.NotificationRepo;
import fusionIQ.AI.V2.fusionIq.service.EnrollmentService;
import fusionIQ.AI.V2.fusionIq.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class EnrollmentServiceTest {

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Mock
    private EnrollmentRepo enrollmentRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationRepo notificationRepo;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testSaveEnrollment_UserOrCourseNotFound() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());

        Enrollment enrollment = new Enrollment();

        try {
            enrollmentService.saveEnrollment(enrollment, 1L, 1L);
        } catch (IllegalArgumentException e) {
            assertEquals("User or Course not found", e.getMessage());
        }
    }

    @Test
    void testGenerateRandomOTP() {
        String otp = EnrollmentService.generateRandomOTP();
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"));
    }

    @Test
    void testVerifyOTP_Success() {
        User user = new User();
        user.setOtp("123456");
        user.setOtpGeneratedTime(LocalDateTime.now());

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        boolean result = enrollmentService.verifyOTP("test@example.com", "123456");

        assertTrue(result);
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void testVerifyOTP_Failure() {
        User user = new User();
        user.setOtp("123456");
        user.setOtpGeneratedTime(LocalDateTime.now().minusMinutes(11)); // Expired OTP

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        boolean result = enrollmentService.verifyOTP("test@example.com", "123456");

        assertEquals(false, result);
    }

    @Test
    void testUpdateEnrollment_Success() {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setProgress(50L);

        when(enrollmentRepo.findById(anyLong())).thenReturn(Optional.of(enrollment));
        when(enrollmentRepo.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment updatedEnrollment = new Enrollment();
        updatedEnrollment.setProgress(80L);

        Enrollment result = enrollmentService.updateEnrollment(1L, updatedEnrollment);

        assertEquals(80L, result.getProgress());
    }

    @Test
    void testUpdateEnrollment_NotFound() {
        when(enrollmentRepo.findById(anyLong())).thenReturn(Optional.empty());

        Enrollment updatedEnrollment = new Enrollment();

        try {
            enrollmentService.updateEnrollment(1L, updatedEnrollment);
        } catch (IllegalArgumentException e) {
            assertEquals("Enrollment not found", e.getMessage());
        }
    }
}
