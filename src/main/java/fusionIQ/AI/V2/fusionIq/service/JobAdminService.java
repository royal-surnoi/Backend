package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import fusionIQ.AI.V2.fusionIq.repository.JobAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class JobAdminService {

    private final JobAdminRepository jobAdminRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final int OTP_LENGTH = 6;
    private static final String NUMBERS = "0123456789";
    private static final Random RANDOM = new Random();

    @Autowired
    public JobAdminService(JobAdminRepository jobAdminRepository) {
        this.jobAdminRepository = jobAdminRepository;
    }

    public boolean isEmailExists(String jobAdminEmail) {
        return jobAdminRepository.findByJobAdminEmail(jobAdminEmail).isPresent();
    }

    public JobAdmin validatePassword(String jobAdminEmail, String jobAdminPassword) {
        Optional<JobAdmin> jobAdmin = jobAdminRepository.findByJobAdminEmail(jobAdminEmail);

        // Check password only if email exists
        if (jobAdmin.isPresent() && jobAdmin.get().getJobAdminPassword().equals(jobAdminPassword)) {
            return jobAdmin.get();
        }

        // Return null if the password is incorrect
        return null;
    }

    public JobAdmin findById(Long jobAdminId) {
        Optional<JobAdmin> jobAdmin = jobAdminRepository.findById(jobAdminId);
        return jobAdmin.orElseThrow(() -> new RuntimeException("Job Admin not found with ID: " + jobAdminId));
    }

    public JobAdmin registerJobAdmin(

            String jobAdminName, String jobAdminCompanyName,

            String jobAdminEmail, String jobAdminPassword,

            String role, String jobAdminConfirmPassword) {

        // Validate password match

        if (!jobAdminPassword.equals(jobAdminConfirmPassword)) {

            throw new RuntimeException("Passwords do not match");

        }

        // Check if the email is already registered

        Optional<JobAdmin> existingAdmin = jobAdminRepository.findByJobAdminEmail(jobAdminEmail);

        if (existingAdmin.isPresent()) {

            throw new RuntimeException("Email is already registered");

        }

        // Create new JobAdmin object and set its fields

        JobAdmin jobAdmin = new JobAdmin();

        jobAdmin.setJobAdminEmail(jobAdminEmail);

        jobAdmin.setJobAdminPassword(jobAdminPassword);

        jobAdmin.setJobAdminName(jobAdminName);

        jobAdmin.setJobAdminCompanyName(jobAdminCompanyName);

        jobAdmin.setRole(role); // Set role

        // Save and return the JobAdmin

        return jobAdminRepository.save(jobAdmin);

    }


    public JobAdmin updateCompanyDetails(
            Long jobAdminId, String companyDescription, String companyTypeOfIndustry, String companyWebsiteLink,
            int companyStrength, String companyLocation, String companyLicense, String companyGstNumber,
            String companyCinNumber, MultipartFile companyLogo, MultipartFile companyLicenseDocument,
            MultipartFile companyGstDocument, MultipartFile companyCinDocument, String companyAboutDescription,
            String companyPhoneNumber, String companyOverviewDescription,
            MultipartFile descriptionBackground, MultipartFile aboutBackground,
            MultipartFile addJobBackground, MultipartFile overviewBackground, MultipartFile contactBackground) throws IOException {

        JobAdmin jobAdmin = findById(jobAdminId);

        // Update company details
        jobAdmin.setCompanyDescription(companyDescription);
        jobAdmin.setCompanyTypeOfIndustry(companyTypeOfIndustry);
        jobAdmin.setCompanyWebsiteLink(companyWebsiteLink);
        jobAdmin.setCompanyStrength(companyStrength);
        jobAdmin.setCompanyLocation(companyLocation);
        jobAdmin.setCompanyLicense(companyLicense);
        jobAdmin.setCompanyGstNumber(companyGstNumber);
        jobAdmin.setCompanyCinNumber(companyCinNumber);
        jobAdmin.setCompanyAboutDescription(companyAboutDescription);
        jobAdmin.setCompanyPhoneNumber(companyPhoneNumber);
        jobAdmin.setCompanyOverviewDescription(companyOverviewDescription);

        // Update background images if provided
        jobAdmin.setCompanyLogo(companyLogo != null ? companyLogo.getBytes() : null);
        jobAdmin.setCompanyLicenseDocument(companyLicenseDocument != null ? companyLicenseDocument.getBytes() : null);
        jobAdmin.setCompanyGstDocument(companyGstDocument != null ? companyGstDocument.getBytes() : null);
        jobAdmin.setCompanyCinDocument(companyCinDocument != null ? companyCinDocument.getBytes() : null);
        jobAdmin.setDescriptionBackground(descriptionBackground != null ? descriptionBackground.getBytes() : null);
        jobAdmin.setAboutBackground(aboutBackground != null ? aboutBackground.getBytes() : null);
        jobAdmin.setAddJobBackground(addJobBackground != null ? addJobBackground.getBytes() : null);
        jobAdmin.setOverviewBackground(overviewBackground != null ? overviewBackground.getBytes() : null);
        jobAdmin.setContactBackground(contactBackground != null ? contactBackground.getBytes() : null);

        return jobAdminRepository.save(jobAdmin);
    }


    public JobAdmin getJobAdminWithSensitiveDataExcluded(Long jobAdminId) {
        JobAdmin jobAdmin = findById(jobAdminId);

        jobAdmin.setJobAdminPassword(null);

        return jobAdmin;
    }

    public JobAdmin updateCompanyLocation(Long jobAdminId, double companyLatitude, double companyLongitude) {
        JobAdmin jobAdmin = findById(jobAdminId);

        // Update latitude and longitude fields
        jobAdmin.setCompanyLatitude(companyLatitude);
        jobAdmin.setCompanyLongitude(companyLongitude);

        return jobAdminRepository.save(jobAdmin);
    }

    public JobAdmin addNewAdmin(Long AdminId, JobAdmin newAdmin) {
        // Fetch existing admin
        JobAdmin existingAdmin = jobAdminRepository.findById(AdminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin with ID " + AdminId + " not found"));

        // Set properties for the new admin, copying any shared details from the existing admin
        newAdmin.setId(null); // Ensure it's treated as a new entity
        if (newAdmin.getCompanyLocation() == null) {
            newAdmin.setCompanyLocation(existingAdmin.getCompanyLocation());
        }
        if (newAdmin.getCompanyTypeOfIndustry() == null) {
            newAdmin.setCompanyTypeOfIndustry(existingAdmin.getCompanyTypeOfIndustry());
        }

        // Save the new admin
        return jobAdminRepository.save(newAdmin);
    }

    public JobAdmin findByJobAdminEmail(String jobAdminEmail) {
        return jobAdminRepository.findByJobAdminEmail(jobAdminEmail)
                .orElse(null);
    }

    public Integer generateAndSaveOTP(String jobAdminEmail) {
        // Generate OTP
        Integer otp = generateOTP();

        // Find JobAdmin by email
        JobAdmin jobAdmin = jobAdminRepository.findByJobAdminEmail(jobAdminEmail)
                .orElseThrow(() -> new RuntimeException("Email not registered"));

        // Save OTP to database
        jobAdmin.setAdminOtp(otp);
        jobAdminRepository.save(jobAdmin);

        return otp;
    }

    private Integer generateOTP() {
        StringBuilder otpBuilder = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            int index = RANDOM.nextInt(NUMBERS.length());
            otpBuilder.append(NUMBERS.charAt(index));
        }
        return Integer.parseInt(otpBuilder.toString());
    }

    public boolean changePassword(String jobAdminEmail, int adminOtp, String newPassword) {
        // Find JobAdmin by email
        JobAdmin jobAdmin = jobAdminRepository.findByJobAdminEmail(jobAdminEmail).orElse(null);

        if (jobAdmin == null || jobAdmin.getAdminOtp() != adminOtp) {
            return false; // JobAdmin not found or OTP does not match
        }

        // Update the password
        jobAdmin.setJobAdminPassword(newPassword);
        jobAdminRepository.save(jobAdmin);

        // Send email after password update
        sendPasswordUpdateEmail(jobAdmin.getJobAdminEmail(), jobAdmin.getJobAdminCompanyName());

        return true;
    }

    private void sendPasswordUpdateEmail(String email, String name) {
        // Get the current timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = now.format(formatter);

        // Create email content
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Updated Successfully");

        // Personalize the email with the user's name and timestamp
        message.setText("Dear " + name + ",\n\nYour password has been updated successfully on " + formattedTimestamp + ".");

        // Send the email
        javaMailSender.send(message);
    }

    public List<Map<String, Object>> getAllCompanyDetails() {
        List<JobAdmin> jobAdmins = jobAdminRepository.findAll();
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (JobAdmin jobAdmin : jobAdmins) {
            Map<String, Object> response = new HashMap<>();
            response.put("jobAdminCompanyName", jobAdmin.getJobAdminCompanyName());
            response.put("companyLogo", jobAdmin.getCompanyLogo());
            responseList.add(response);
        }
        return responseList;
    }

}