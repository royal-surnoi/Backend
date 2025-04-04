package fusionIQ.AI.V2.fusionIq.controller;
import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.service.AssignmentService;
import fusionIQ.AI.V2.fusionIq.service.EnrollmentService;
import fusionIQ.AI.V2.fusionIq.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private NotificationService notificationService;


    @PostMapping("/add/{userId}/{courseId}")
    public ResponseEntity<Enrollment> createEnrollment(@RequestBody Enrollment enrollment, @PathVariable long userId, @PathVariable long courseId) {
        try {
            Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment, userId, courseId);
            return ResponseEntity.ok(savedEnrollment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/getBy/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable long id) {
        Optional<Enrollment> enrollmentOpt = enrollmentService.findEnrollmentById(id);
        return enrollmentOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/all")
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.findAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByUser(@PathVariable long userId) {
        List<Enrollment> enrollments = enrollmentService.findEnrollmentsByUser(userId);
        return ResponseEntity.ok(enrollments);
    }
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable long courseId) {
        List<Enrollment> enrollments = enrollmentService.findEnrollmentsByCourse(courseId);
        return ResponseEntity.ok(enrollments);
    }
    @DeleteMapping("/user/{userId}/course/{courseId}")
    public ResponseEntity<Void> deleteEnrollmentByUserAndCourse(@RequestParam long userId, @RequestParam long courseId) {
        enrollmentService.deleteEnrollment(userId, courseId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/user/{userId}/course/{courseId}")
    public ResponseEntity<Enrollment> getEnrollmentByUserAndCourse(
            @PathVariable long userId,
            @PathVariable long courseId) {
        Enrollment enrollment = enrollmentService.findEnrollmentByUserAndCourse(userId, courseId);
        if (enrollment != null) {
            return ResponseEntity.ok(enrollment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Enrollment> updateEnrollment(@PathVariable long id, @RequestBody Enrollment updatedEnrollment) {
        try {
            Enrollment savedEnrollment = enrollmentService.updateEnrollment(id, updatedEnrollment);
            return ResponseEntity.ok(savedEnrollment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
@PostMapping("/generateOtp")
public ResponseEntity<String> sendOtp(@RequestBody LoginRequest loginRequest) {
    String otp = enrollmentService.generateAndSaveOTP(loginRequest.getEmail());
    if (otp != null) {
        enrollmentService.sendOTPByEmail(loginRequest.getEmail(), otp);
        return new ResponseEntity<>("OTP has been sent to your email", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Failed to generate OTP", HttpStatus.NOT_FOUND);
    }
}

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOTP(@RequestParam String email,
                                            @RequestParam String otp,
                                            @RequestParam Long userId,
                                            @RequestParam Long courseId) {
        boolean isOTPValid = enrollmentService.verifyOTP(email, otp);
        if (isOTPValid) {
            Enrollment savedEnrollment = enrollmentService.enrollInCourse(userId, courseId);

            User user = savedEnrollment.getUser();
            Course course = savedEnrollment.getCourse();
            enrollmentService.sendEnrollmentConfirmationEmail(user.getEmail(), user.getName(), course.getCourseTitle(), course.getCourseFee());
            // Create notification for the teacher
            User teacher = course.getUser();
            String notificationContent = "Student " + user.getName() + " has enrolled in your course: " + course.getCourseTitle();
            notificationService.createNotification(teacher.getId(), notificationContent);

            return new ResponseEntity<>("OTP verified and enrolled successfully. Confirmation email sent.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid or expired OTP", HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/enroll/user/{userId}/course/{courseId}")
    public ResponseEntity<Enrollment> getEnrollmentByUserIdAndCourseId(@PathVariable Long userId, @PathVariable Long courseId) {
        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentByUserIdAndCourseId(userId, courseId);
        return enrollment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/progress")
    public List<Enrollment> getProgress(@RequestParam Long courseId, @RequestParam Long userId) {
        return enrollmentService.getProgress(courseId, userId);
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<Boolean> checkEnrollmentStatus(@PathVariable Long userId) {
        boolean isEnrolled = enrollmentService.isUserEnrolled(userId);
        return ResponseEntity.ok(isEnrolled);
    }


}