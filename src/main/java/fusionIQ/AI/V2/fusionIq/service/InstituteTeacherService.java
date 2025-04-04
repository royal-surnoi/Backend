package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Institute;
import fusionIQ.AI.V2.fusionIq.data.InstituteTeacher;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.InstituteRepo;
import fusionIQ.AI.V2.fusionIq.repository.InstituteTeacherRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class InstituteTeacherService {

    @Autowired
    private InstituteTeacherRepo instituteTeacherRepo;

    @Autowired
    private InstituteRepo instituteRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final int OTP_LENGTH = 6;
    private static final String NUMBERS = "0123456789";
    private static final Random RANDOM = new Random();

    // Check if email exists
    public boolean isEmailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    // Check if userId exists
    public boolean isUserIdExists(Long userId) {
        return userRepo.findById(userId).isPresent();
    }

    // Register teacher and send OTP
    public InstituteTeacher registerTeacherAndSendOTP(InstituteTeacher instituteTeacher, Long instituteId, Long userId) {
        Institute institute = instituteRepo.findById(instituteId)
                .orElseThrow(() -> new RuntimeException("Institute not found with id " + instituteId));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        instituteTeacher.setInstitute(institute);
        instituteTeacher.setUser(user);

        if (instituteTeacher.getStatus() == null) {
            instituteTeacher.setStatus(InstituteTeacher.Status.PENDING);
        }

        InstituteTeacher savedTeacher = instituteTeacherRepo.save(instituteTeacher);

        String otp = generateRandomOTP();
        instituteTeacher.setOtp(otp);
        instituteTeacherRepo.save(savedTeacher);

        sendEmail(instituteTeacher.getEmail(), "Your OTP for Registration", "Your OTP is: " + otp);

        return savedTeacher;
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    private String generateRandomOTP() {
        StringBuilder otpBuilder = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otpBuilder.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }
        return otpBuilder.toString();
    }

    public String verifyOtp(Long id, String otp) {
        Optional<InstituteTeacher> teacherOptional = instituteTeacherRepo.findById(id);

        if (teacherOptional.isPresent()) {
            InstituteTeacher teacher = teacherOptional.get();
            if (teacher.getOtp().equals(otp)) {
                teacher.setVerified_Otp("YES");
                instituteTeacherRepo.save(teacher);
                return "OTP Verified Successfully";
            } else {
                return "Incorrect OTP";
            }
        } else {
            return "Teacher not found";
        }
    }
    public List<InstituteTeacher> getVerifiedTeachersByInstituteId(Long instituteId) {
        // Fetching teachers with otpVerified = "YES" and status = "PENDING"
        return instituteTeacherRepo.findVerifiedTeachersByInstituteIdAndStatus(instituteId, "YES", InstituteTeacher.Status.PENDING);
    }


    public String updateTeacherStatus(Long id, String status) {
        Optional<InstituteTeacher> teacherOptional = instituteTeacherRepo.findById(id);

        if (teacherOptional.isPresent()) {
            InstituteTeacher teacher = teacherOptional.get();
            InstituteTeacher.Status newStatus;

            // Validate status
            try {
                newStatus = InstituteTeacher.Status.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return "Invalid status. Please provide ACCEPTED or REJECTED.";
            }

            // Update status and notify if changed
            if (!teacher.getStatus().equals(newStatus)) {
                teacher.setStatus(newStatus);
                instituteTeacherRepo.save(teacher);

                sendEmail(teacher.getEmail(), "Teacher Status Update",
                        "Dear " + teacher.getTeacher_Name() + ",\n\nYour status has been updated to: " + newStatus + ".\n\nThank you.");

                return "Teacher status updated to " + newStatus;
            } else {
                return "The teacher is already in the " + newStatus + " status.";
            }
        } else {
            return "Teacher not found with ID: " + id;
        }
    }



    public List<InstituteTeacher> getAcceptedTeachersByUserId(Long userId) {
        // Fetching the data based on the user ID and status "ACCEPTED"
        List<InstituteTeacher> teachers = instituteTeacherRepo.findByStatusAndUserId(InstituteTeacher.Status.ACCEPTED, userId);

        // Optional: Log the number of teachers found
        System.out.println("Found " + teachers.size() + " accepted teachers for user ID: " + userId);

        return teachers;  // Return the list of teachers
    }
    public InstituteTeacher getTeacherById(Long id) {
        return instituteTeacherRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found for ID: " + id));
    }


}