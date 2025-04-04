package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Enrollment;
import fusionIQ.AI.V2.fusionIq.data.Notification;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;


@Service

public class EnrollmentService {

    @Autowired
    private EnrollmentRepo enrollmentRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private AssignmentRepo assignmentRepo;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private ProjectRepo projectRepo;



    public Enrollment saveEnrollment(Enrollment enrollment, long userId, long courseId) {
        Optional<User> userOpt = userRepo.findById(userId);
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (userOpt.isPresent() && courseOpt.isPresent()) {
            enrollment.setUser(userOpt.get());
            enrollment.setCourse(courseOpt.get());
        } else {
            throw new IllegalArgumentException("User or Course not found");
        }
        Enrollment savedEnrollment = enrollmentRepo.save(enrollment);

        String content = "You have successfully enrolled in the course: " + courseOpt.get().getCourseTitle();
        notificationService.createNotification(userId, content);

        return savedEnrollment;
    }


    public Optional<Enrollment> findEnrollmentById(long id) {
        return enrollmentRepo.findById(id);
    }

    public List<Enrollment> findAllEnrollments() {
        return enrollmentRepo.findAll();
    }

    public void deleteEnrollment(long userId, long courseId) {
        enrollmentRepo.deleteById(userId);
    }

    public List<Enrollment> findEnrollmentsByUser(long userId) {
        List<Enrollment> enrollments = enrollmentRepo.findByUserId(userId);

        // Set progress to 0 if it's null
        enrollments.forEach(enrollment -> {
            if (enrollment.getProgress() == null) {
                enrollment.setProgress(0L);
            }
        });

        return enrollments;
    }

    public List<Enrollment> findEnrollmentsByCourse(long courseId) {
        return enrollmentRepo.findByCourseId(courseId);

    }
    public Enrollment findEnrollmentByUserAndCourse(long userId, long courseId) {
        return enrollmentRepo.findByUserIdAndCourseId(userId, courseId);
    }

    public Enrollment updateEnrollment(long id, Enrollment updatedEnrollment) {
        Optional<Enrollment> existingEnrollmentOpt = enrollmentRepo.findById(id);
        if (existingEnrollmentOpt.isPresent()) {
            Enrollment existingEnrollment = existingEnrollmentOpt.get();


            if (updatedEnrollment.getEnrollmentDate() != null) {
                existingEnrollment.setEnrollmentDate(updatedEnrollment.getEnrollmentDate());
            }
            if (updatedEnrollment.getProgress() != null) {
                existingEnrollment.setProgress(updatedEnrollment.getProgress());
            }

            return enrollmentRepo.save(existingEnrollment);
        } else {
            throw new IllegalArgumentException("Enrollment not found");
        }
    }

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 10;


    public String generateAndSaveOTP(String email) {
        String otp = generateRandomOTP();
        User user = userRepo.findByEmail(email).orElse(null);
        if (user != null) {
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            userRepo.save(user);
            return otp;
        }
        return null;
    }

    private static final String NUMBERS = "0123456789";
    private static final Random RANDOM = new Random();

    public static String generateRandomOTP() {
        StringBuilder otpBuilder = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otpBuilder.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }
        return otpBuilder.toString();
    }


    public void sendOTPByEmail(String email, String otp) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Your One-Time Password (OTP)");

            String content = "<p>Your One Time Password to Enroll FusionIQ.AI is: <b>" + otp + "</b>.</p>"
                    + "<p>Please do not share this code with anyone.</p>"
                    + "<p>This OTP is valid for 10 minutes.</p>";

            helper.setText(content, true); // Set to true to enable HTML

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyOTP(String email, String otp) {
        User user = userRepo.findByEmail(email).orElse(null);
        if (user != null && user.getOtp() != null && user.getOtp().equals(otp)) {
            if (isOTPExpired(user.getOtpGeneratedTime())) {
                return false;
            }
            user.setOtp(null);
            user.setOtpGeneratedTime(null);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    private boolean isOTPExpired(LocalDateTime otpGeneratedTime) {
        return otpGeneratedTime.plusMinutes(OTP_EXPIRY_MINUTES).isBefore(LocalDateTime.now());
    }

    public Enrollment enrollInCourse(Long userId, Long courseId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enroll = new Enrollment();
        enroll.setUser(user);
        enroll.setCourse(course);

        return enrollmentRepo.save(enroll);
    }

    public void sendEnrollmentConfirmationEmail(String to, String Name, String courseTitle, Long courseFee) {
        String subject = "Course Enrollment Successful";
        String body = "Dear " + Name + ",\n\n" +
                "You have successfully enrolled in the course: " + courseTitle + ".\n" +
                "Course Fee: $" + courseFee + "\n\n" +
                "We appreciate your trust in us and look forward to providing you with the best learning experience. If you have any questions or need assistance, please feel free to reach out to us. We will get back to you as soon as possible.\n\n" +
                "Best regards,\n" +
                "FUSIONIQ.AI";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

public List<Enrollment> getProgress(long courseId, long userId) {
    return enrollmentRepo.findProgressByCourseIdAndUserId(courseId, userId);
}

    public List<Enrollment> getEnrollmentsByCourse(Course course) {
        return enrollmentRepo.findByCourse(course);
    }

    public List<Enrollment> getEnrollmentsByUser(User user) {
        return enrollmentRepo.findByUser(user);
    }

    public Notification createNotification(Long userId, String content) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setContent(content);
        return notificationRepo.save(notification);
    }

    public Optional<Enrollment> getEnrollmentByUserIdAndCourseId(Long userId, Long courseId) {
        return enrollmentRepo.findByUserIdAndCourseId(userId, courseId);
    }

    public List<Enrollment> findByUserId(Long userId) {
        List<Enrollment> enrollments = enrollmentRepo.findByUserId(userId);
        if (enrollments == null || enrollments.isEmpty()) {
            System.out.println("No enrollments found for userId: " + userId);
        } else {
            System.out.println("Enrollments found: " + enrollments);
        }
        return enrollments;
    }

    public boolean isUserEnrolled(Long userId) {
        return enrollmentRepo.existsByUserId(userId);
    }


    public Map<String, Boolean> getEnrollmentStatus(Long userId, Long courseId) {
        boolean isEnrolled = enrollmentRepo.existsByUserIdAndCourseId(userId, courseId);
        boolean isCompleted = false;
        if (isEnrolled) {
            Optional<Enrollment> enrollment = enrollmentRepo.findByUserIdAndCourseId(userId, courseId);
            if (enrollment.isPresent() && enrollment.get().getProgress() != null) {
                isCompleted = enrollment.get().getProgress() == 100L;
            }
        }

        Map<String, Boolean> status = new HashMap<>();
        status.put("isEnrolled", isEnrolled);
        status.put("isCompleted", isCompleted);

        return status;
    }

    public List<Enrollment> findByCourseId(Long courseId) {
        return enrollmentRepo.findByCourseId(courseId);
    }
}
