package fusionIQ.AI.V2.fusionIq.controller;


import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import fusionIQ.AI.V2.fusionIq.data.JwtResponse;
import fusionIQ.AI.V2.fusionIq.service.EmailSenderService;
import fusionIQ.AI.V2.fusionIq.service.JobAdminService;
import fusionIQ.AI.V2.fusionIq.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobAdmin")
public class JobAdminController {

    private final JobAdminService jobAdminService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailSenderService emailSenderService;

    public JobAdminController(JobAdminService jobAdminService) {
        this.jobAdminService = jobAdminService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginJobAdmin(
            @RequestParam String jobAdminEmail,
            @RequestParam String jobAdminPassword) {
        // Check for email existence first
        if (!jobAdminService.isEmailExists(jobAdminEmail)) {
            return new ResponseEntity<>("Incorrect username", HttpStatus.UNAUTHORIZED);
        }

        // Validate password if email exists
        JobAdmin jobAdmin = jobAdminService.validatePassword(jobAdminEmail, jobAdminPassword);
        if (jobAdmin == null) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
        }

        // Generate a JWT token
        String jwtToken = tokenService.generateToken(jobAdminEmail);

        // Prepare the response with the token
        JwtResponse response = new JwtResponse(
                jwtToken,
                jobAdmin.getId(),
                jobAdmin.getJobAdminName(),
                jobAdmin.getJobAdminEmail(),
                null // Add devices if applicable
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerJobAdmin(
            @RequestParam String jobAdminName,
            @RequestParam String jobAdminCompanyName,
            @RequestParam String jobAdminEmail,
            @RequestParam String jobAdminPassword,
            @RequestParam String jobAdminConfirmPassword,
            @RequestParam String role) {
        try {
            // Call the service to register
            JobAdmin jobAdmin = jobAdminService.registerJobAdmin(
                    jobAdminName, jobAdminCompanyName, jobAdminEmail,
                    jobAdminPassword, role, jobAdminConfirmPassword);

            return new ResponseEntity<>(jobAdmin, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @PatchMapping("/updateCompany/{jobAdminId}")
    public ResponseEntity<JobAdmin> updateCompanyDetails(
            @PathVariable Long jobAdminId,
            @RequestParam String companyDescription,
            @RequestParam String companyTypeOfIndustry,
            @RequestParam String companyWebsiteLink,
            @RequestParam Integer companyStrength,
            @RequestParam String companyLocation,
            @RequestParam String companyLicense,
            @RequestParam String companyGstNumber,
            @RequestParam String companyCinNumber,
            @RequestParam MultipartFile companyLogo,
            @RequestParam MultipartFile companyLicenseDocument,
            @RequestParam MultipartFile companyGstDocument,
            @RequestParam MultipartFile companyCinDocument,
            @RequestParam String companyAboutDescription,
            @RequestParam String companyPhoneNumber,
            @RequestParam String companyOverviewDescription,
            @RequestParam(required = false) MultipartFile descriptionBackground,
            @RequestParam(required = false) MultipartFile aboutBackground,
            @RequestParam(required = false) MultipartFile addJobBackground,
            @RequestParam(required = false) MultipartFile overviewBackground,
            @RequestParam(required = false) MultipartFile contactBackground) {

        try {
            JobAdmin updatedJobAdmin = jobAdminService.updateCompanyDetails(
                    jobAdminId, companyDescription, companyTypeOfIndustry, companyWebsiteLink, companyStrength,
                    companyLocation, companyLicense, companyGstNumber, companyCinNumber, companyLogo,
                    companyLicenseDocument, companyGstDocument, companyCinDocument, companyAboutDescription,
                    companyPhoneNumber, companyOverviewDescription,
                    descriptionBackground, aboutBackground, addJobBackground, overviewBackground, contactBackground);
            return new ResponseEntity<>(updatedJobAdmin, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/get/{jobAdminId}")
    public ResponseEntity<JobAdmin> getJobAdminById(@PathVariable Long jobAdminId) {
        try {
            JobAdmin jobAdmin = jobAdminService.getJobAdminWithSensitiveDataExcluded(jobAdminId);
            return new ResponseEntity<>(jobAdmin, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/updateLocation/{jobAdminId}")
    public ResponseEntity<JobAdmin> updateCompanyLocation(
            @PathVariable Long jobAdminId,
            @RequestParam Double companyLatitude,
            @RequestParam Double companyLongitude) {
        try {
            JobAdmin updatedJobAdmin = jobAdminService.updateCompanyLocation(jobAdminId, companyLatitude, companyLongitude);
            return new ResponseEntity<>(updatedJobAdmin, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{AdminId}/add-new-admin")
    public ResponseEntity<JobAdmin> addNewAdmin(
            @PathVariable Long AdminId,
            @RequestBody JobAdmin newAdmin) {
        JobAdmin createdAdmin = jobAdminService.addNewAdmin(AdminId, newAdmin);
        return ResponseEntity.ok(createdAdmin);
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<String> sendOTP(@RequestParam String jobAdminEmail) {
        try {
            // Check if JobAdmin exists
            JobAdmin jobAdmin = jobAdminService.findByJobAdminEmail(jobAdminEmail);
            if (jobAdmin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("You don't have an account. Please register.");
            }

            // Generate and save OTP
            Integer generatedOtp = jobAdminService.generateAndSaveOTP(jobAdminEmail);

            // Send OTP via email
            String emailSubject = "Your OTP for Verification";
            String emailBody = "Dear " + jobAdmin.getJobAdminName() + ",\n\n"
                    + "Your OTP for verification is: " + generatedOtp + "\n\n"
                    + "This OTP will expire shortly. Please do not share it with anyone.\n\n"
                    + "Best regards,\nYour Application Team";

            emailSenderService.sendEmail(jobAdminEmail, emailSubject, emailBody);

            return ResponseEntity.ok("OTP sent successfully to your email");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP: " + e.getMessage());
        }
    }

    @PutMapping("/change-password-otp")
    public ResponseEntity<String> changePassword(@RequestParam String jobAdminEmail,
                                                 @RequestParam int adminOtp,
                                                 @RequestParam String newPassword) {
        boolean isUpdated = jobAdminService.changePassword(jobAdminEmail, adminOtp, newPassword);

        if (isUpdated) {
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Incorrect OTP or JobAdmin not found");
        }
    }

    @GetMapping("/getAllCompanyDetails")
    public ResponseEntity<List<Map<String, Object>>> getAllCompanyDetails() {
        List<Map<String, Object>> companyDetails = jobAdminService.getAllCompanyDetails();
        return new ResponseEntity<>(companyDetails, HttpStatus.OK);
    }
}