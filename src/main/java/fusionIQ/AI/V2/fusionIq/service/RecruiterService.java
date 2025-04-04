package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import fusionIQ.AI.V2.fusionIq.data.Recruiter;
import fusionIQ.AI.V2.fusionIq.repository.RecruiterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;
    //    private final EmailSenderService emailSenderService;
    private static final SecureRandom RANDOM = new SecureRandom();  // Reusable Random instance

    @Autowired
    public RecruiterService(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    @Autowired
    private EmailSenderService emailSenderService;

    @Transactional
    public Recruiter registerRecruiter(Recruiter recruiter, JobAdmin jobAdmin) {
        // Check if a recruiter with the same email already exists
        Optional<Recruiter> existingRecruiter = recruiterRepository.findByRecruiterEmail(recruiter.getRecruiterEmail());
        if (existingRecruiter.isPresent()) {
            throw new RuntimeException("A recruiter with this email already exists: " + recruiter.getRecruiterEmail());
        }

        // Set the JobAdmin for the recruiter
        recruiter.setJobAdmin(jobAdmin);

        // Save the recruiter to the database
        Recruiter savedRecruiter = recruiterRepository.save(recruiter);

        // Send confirmation email to the recruiter
        sendRegistrationEmail(savedRecruiter, jobAdmin);

        return savedRecruiter;
    }


    private void sendRegistrationEmail(Recruiter recruiter, JobAdmin jobAdmin) {
        String subject = "Welcome to Job Portal";
        String message = "Hello " + recruiter.getRecruiterName() + ",\n\n" +
                "You have been successfully registered as a recruiter for the company " + jobAdmin.getJobAdminCompanyName() +
                " by Job Admin: " + jobAdmin.getJobAdminName() + ".\n\n" +
                "You can now log in using: " + "\n" +
                "Email: " + recruiter.getRecruiterEmail() + "\n" +
                "Password: " + recruiter.getRecruiterPassword() + "\n\n" +
                "Best regards,\nJob Portal Team";

        emailSenderService.sendEmail(recruiter.getRecruiterEmail(), subject, message);
    }

    // Find recruiter by email
    public Optional<Recruiter> findRecruiterByEmail(String recruiterEmail) {
        return recruiterRepository.findByRecruiterEmail(recruiterEmail);
    }

    public Recruiter loginRecruiter(String recruiterEmail, String recruiterPassword) {
        Optional<Recruiter> recruiter = recruiterRepository.findByRecruiterEmail(recruiterEmail);
        if (recruiter.isPresent() && recruiter.get().getRecruiterPassword().equals(recruiterPassword)) {
            return recruiter.get();
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }

    public JobAdmin getAdminByRecruiterId(Long recruiterId) {
        return recruiterRepository.findById(recruiterId)
                .map(Recruiter::getJobAdmin)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
    }

    public Recruiter getRecruiterById(Long recruiterId) {
        return recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found with ID: " + recruiterId));
    }

    public Recruiter updateRecruiterDetails(Long recruiterId, Recruiter updatedRecruiter, JobAdmin jobAdmin) {
        // Fetch the recruiter from the database using recruiterId
        Optional<Recruiter> optionalRecruiter = recruiterRepository.findById(recruiterId);

        if (optionalRecruiter.isPresent()) {
            Recruiter recruiter = optionalRecruiter.get();

            // Update the fields
            recruiter.setRecruiterName(updatedRecruiter.getRecruiterName());
            recruiter.setRecruiterEmail(updatedRecruiter.getRecruiterEmail());
            recruiter.setRecruiterPassword(updatedRecruiter.getRecruiterPassword());
            recruiter.setRecruiterRole(updatedRecruiter.getRecruiterRole());
            recruiter.setJobAdmin(jobAdmin); // Update the JobAdmin association

            // Save the updated recruiter back to the database
            recruiterRepository.save(recruiter);
            return recruiter;
        } else {
            throw new RuntimeException("Recruiter not found with ID: " + recruiterId);
        }
    }

    public Map<String, List<Map<String, Object>>> getRecruitersByJobAdminAndRole(Long jobAdminId, String recruiterRole) {
        List<Recruiter> recruiters = recruiterRepository.findByJobAdminIdAndRecruiterRole(jobAdminId, recruiterRole);

        // Filter out recruiters with null deportment and group by recruiterDeportment (case-insensitive)
        return recruiters.stream()
                .filter(recruiter -> recruiter.getRecruiterDeportment() != null) // Filter null deportments
                .collect(Collectors.groupingBy(
                        recruiter -> recruiter.getRecruiterDeportment().toLowerCase(),
                        Collectors.mapping(recruiter -> {
                            Map<String, Object> recruiterMap = new HashMap<>();
                            recruiterMap.put("id", recruiter.getId());
                            recruiterMap.put("recruiterEmail", recruiter.getRecruiterEmail());
                            recruiterMap.put("recruiterName", recruiter.getRecruiterName());
                            return recruiterMap;
                        }, Collectors.toList())
                ));
    }


    @Transactional
    public Long getJobAdminIdByRecruiterId(Long recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new IllegalArgumentException("Recruiter not found with ID: " + recruiterId));

        if (recruiter.getJobAdmin() == null) {
            throw new IllegalArgumentException("Job Admin not assigned for Recruiter with ID: " + recruiterId);
        }

        return recruiter.getJobAdmin().getId();
    }

    @Transactional
    public boolean generateAndSendOTP(String recruiterEmail) {
        Optional<Recruiter> recruiterOptional = recruiterRepository.findByRecruiterEmail(recruiterEmail);

        if (recruiterOptional.isPresent()) {
            Recruiter recruiter = recruiterOptional.get();

            // Generate a 6-digit OTP
            String otp = String.format("%06d", RANDOM.nextInt(1000000));

            // Update recruiter details
            recruiter.setRecruiterOTP(otp);
            recruiter.setRecruiterOTPGeneratedTime(LocalDateTime.now());

            try {
                recruiterRepository.save(recruiter); // Save updated recruiter

                // Send OTP email
                sendOTPEmail(recruiter, otp);
                return true;

            } catch (Exception e) {
                throw new RuntimeException("Failed to generate and send OTP: " + e.getMessage(), e);
            }
        }

        return false;  // Recruiter not found
    }

    private void sendOTPEmail(Recruiter recruiter, String otp) {
        String subject = "Your OTP for Recruiter Verification";
        String body = "Dear " + recruiter.getRecruiterName() + ",\n\n"
                + "Your One-Time Password (OTP) for verification is: " + otp + "\n\n"
                + "This OTP is valid for 10 minutes. Please do not share it with anyone.\n\n"
                + "Best regards,\n"
                + "FusionIQ AI Team";

        emailSenderService.sendEmail(recruiter.getRecruiterEmail(), subject, body);
    }

    public String changePassword(String recruiterEmail, String recruiterOTP, String newPassword) {
        Optional<Recruiter> recruiterOptional = recruiterRepository.findByRecruiterEmail(recruiterEmail);

        if (recruiterOptional.isEmpty()) {
            throw new IllegalArgumentException("Recruiter not found.");
        }

        Recruiter recruiter = recruiterOptional.get();

        if (recruiter.getRecruiterOTP() == null || recruiter.getRecruiterOTPGeneratedTime() == null) {
            throw new IllegalArgumentException("OTP not generated.");
        }

        if (!recruiter.getRecruiterOTP().equals(recruiterOTP)) {
            throw new IllegalArgumentException("Incorrect OTP.");
        }

        LocalDateTime otpExpiryTime = recruiter.getRecruiterOTPGeneratedTime().plusMinutes(10);
        if (LocalDateTime.now().isAfter(otpExpiryTime)) {
            throw new IllegalArgumentException("OTP expired. Please resend.");
        }

        recruiter.setRecruiterPassword(newPassword);

        recruiter.setRecruiterOTP(null);
        recruiter.setRecruiterOTPGeneratedTime(null);
        recruiterRepository.save(recruiter);

        try {
            emailSenderService.sendEmail(recruiterEmail, "Password Changed Successfully",
                    "Your password has been successfully changed on " + LocalDateTime.now() + ".");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }

        return "Password changed successfully.";
    }

    public List<Map<String, Object>> getPanelMembersByJobAdminId(Long jobAdminId) {
        String panelMemberRole = "panelMember"; // Assuming "panelMember" is the role for panel members
        List<Object[]> results = recruiterRepository.findPanelMembersByJobAdminId(jobAdminId, panelMemberRole);

        return results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", result[0]);
            map.put("recruiterName", result[1]);
            map.put("recruiterEmail", result[2]);
            map.put("recruiterRole", result[3]);
            map.put("recruiterDeportment", result[4]);
            map.put("createdAt", result[5]);
            return map;
        }).collect(Collectors.toList());
    }

    public String getRecruiterEmailById(Long recruiterId) {
        return recruiterRepository.findRecruiterEmailById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found with ID: " + recruiterId));
    }

}