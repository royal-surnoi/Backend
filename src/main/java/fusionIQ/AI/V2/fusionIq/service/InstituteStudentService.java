package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Institute;
import fusionIQ.AI.V2.fusionIq.data.InstituteStudent;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.InstituteRepo;
import fusionIQ.AI.V2.fusionIq.repository.InstituteStudentRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class InstituteStudentService {

    @Autowired
    private InstituteStudentRepo instituteStudentRepo;

    @Autowired
    private InstituteRepo instituteRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final int OTP_LENGTH = 6;
    private static final String NUMBERS = "0123456789";
    private static final Random RANDOM = new Random();

    public InstituteStudent registerStudentWithOtp(long instituteId, long userId, InstituteStudent student) {
        // Check if email already exists
        if (instituteStudentRepo.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists: " + student.getEmail());
        }

        // Check if the user already has an account
        Optional<InstituteStudent> existingStudentOpt = instituteStudentRepo.findByUserId(userId);
        if (existingStudentOpt.isPresent()) {
            InstituteStudent existingStudent = existingStudentOpt.get();
            throw new RuntimeException("User with email " + existingStudent.getEmail() + " already has an account");
        }

        // Fetch the institute
        Optional<Institute> instituteOpt = instituteRepo.findById(instituteId);
        if (instituteOpt.isEmpty()) {
            throw new RuntimeException("Institute not found with ID: " + instituteId);
        }
        Institute institute = instituteOpt.get();

        // Fetch the user
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        User user = userOpt.get();

        // Validate password and confirmPassword
        if (!student.getPassword().equals(student.getConfirmPassword())) {
            throw new RuntimeException("Password and Confirm Password do not match");
        }

        // Generate OTP
        String otp = generateOtp();
        student.setOtp(otp); // Set OTP in the student entity

        // Send OTP via email
        sendOtpEmail(student.getEmail(), otp);

        // Set default status, associate institute and user
        student.setStatus(InstituteStudent.Status.PENDING);
        student.setInstitute(institute);
        student.setUser(user);

        // Save student
        return instituteStudentRepo.save(student);
    }


    private String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }
        return otp.toString();
    }

    private void sendOtpEmail(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your OTP for student Registration");
            message.setText("Dear Student,\n\nYour OTP for registration is: " + otp + "\n\nThank you.");
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    public String verifyOtp(Long id, String otp) {
        // Find student by ID
        InstituteStudent student = instituteStudentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));

        // Check OTP
        if (student.getOtp().equals(otp)) {
            student.setVerified_Otp("YES"); // Update verified OTP field
            instituteStudentRepo.save(student); // Save the changes
            return "OTP verified successfully!";
        } else {
            return "Incorrect OTP";
        }
    }
    public List<InstituteStudent> getAcceptedStudentsByUserId(Long userId) {
        return instituteStudentRepo.findByStatusAndUserId(InstituteStudent.Status.ACCEPTED, userId);
    }
    public InstituteStudent getStudentById(Long id) {
        return instituteStudentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found for ID: " + id));
    }


    public List<InstituteStudent> getVerifiedStudentsByInstituteId(Long instituteId) {
        // Fetching students with otpVerified = "YES" and status = "PENDING"
        return instituteStudentRepo.findVerifiedStudentsByInstituteIdAndStatus(instituteId, "YES", InstituteStudent.Status.PENDING);
    }
    public String updateStudentStatus(Long id, String status) {
        Optional<InstituteStudent> studentOptional = instituteStudentRepo.findById(id);

        if (studentOptional.isPresent()) {
            InstituteStudent student = studentOptional.get();
            InstituteStudent.Status newStatus;

            // Validate the status
            try {
                newStatus = InstituteStudent.Status.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return "Invalid status. Please provide ACCEPTED or PENDING.";
            }

            // Update the status and notify if changed
            if (!student.getStatus().equals(newStatus)) {
                student.setStatus(newStatus);
                instituteStudentRepo.save(student);

                // Send email notification
                sendEmail(student.getEmail(), "Student Status Update",
                        "Dear " + student.getStudentName() + ",\n\nYour status has been updated to: " + newStatus + ".\n\nThank you.");

                return "Student status updated to " + newStatus;
            } else {
                return "The student is already in the " + newStatus + " status.";
            }
        } else {
            return "Student not found with ID: " + id;
        }
    }


    private void sendEmail(String email, String subject, String message) {
        // Your email logic here (use JavaMailSender or any other email service)
        System.out.println("Sending email to: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
    }

}