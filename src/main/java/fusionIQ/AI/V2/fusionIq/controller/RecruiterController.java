package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import fusionIQ.AI.V2.fusionIq.data.Recruiter;
import fusionIQ.AI.V2.fusionIq.data.RecruiterResponse;
import fusionIQ.AI.V2.fusionIq.service.JobAdminService;
import fusionIQ.AI.V2.fusionIq.service.RecruiterService;
import fusionIQ.AI.V2.fusionIq.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    private final RecruiterService recruiterService;

    @Autowired
    private TokenService tokenService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @Autowired
    private JobAdminService jobAdminService;

    @PostMapping("/register/{jobAdminId}")
    public ResponseEntity<?> registerRecruiter(@RequestBody Recruiter recruiter, @PathVariable Long jobAdminId) {
        try {
            // Retrieve JobAdmin
            JobAdmin jobAdmin = jobAdminService.findById(jobAdminId);
            if (jobAdmin == null) {
                return ResponseEntity.badRequest().body("Job Admin not found.");
            }

            // Register the recruiter
            Recruiter savedRecruiter = recruiterService.registerRecruiter(recruiter, jobAdmin);
            return ResponseEntity.ok(savedRecruiter);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginRecruiter(@RequestParam String recruiterEmail, @RequestParam String recruiterPassword) {
        // Check if recruiter exists by email
        Optional<Recruiter> recruiterOptional = recruiterService.findRecruiterByEmail(recruiterEmail);

        if (recruiterOptional.isEmpty()) {
            // Return error if email is incorrect
            return new ResponseEntity<>("Incorrect username", HttpStatus.UNAUTHORIZED);
        }

        Recruiter recruiter = recruiterOptional.get();
        if (!recruiter.getRecruiterPassword().equals(recruiterPassword)) {
            // Return error if password is incorrect
            return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
        }

        // Generate JWT token if credentials are correct
        String jwtToken = tokenService.generateToken(recruiterEmail);

        // Prepare response
        RecruiterResponse response = new RecruiterResponse(
                jwtToken,
                recruiter.getId(),
                recruiter.getRecruiterName(),
                recruiter.getRecruiterEmail()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admins/{recruiterId}")
    public ResponseEntity<?> getAdminByRecruiterId(@PathVariable Long recruiterId) {
        try {
            JobAdmin jobAdmin = recruiterService.getAdminByRecruiterId(recruiterId);
            return ResponseEntity.ok(jobAdmin);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/adminsRecruiters/{recruiterId}")
    public ResponseEntity<?> getAdminAndRecruiterById(@PathVariable Long recruiterId) {
        try {
            // Fetch the recruiter and JobAdmin details
            Recruiter recruiter = recruiterService.getRecruiterById(recruiterId);
            JobAdmin jobAdmin = recruiter.getJobAdmin();

            // Combine both details into a map
            Map<String, Object> response = new HashMap<>();
            response.put("recruiter", recruiter);
            response.put("jobAdmin", jobAdmin);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }


@PutMapping("/updateRecruiterDetails/{recruiterId}/{adminId}")
    public ResponseEntity<?> updateRecruiterDetails(
            @PathVariable Long recruiterId,
            @PathVariable Long adminId,
            @RequestBody Recruiter updatedRecruiter) {

        // Retrieve JobAdmin using jobAdminId
        JobAdmin jobAdmin = jobAdminService.findById(adminId);
        if (jobAdmin == null) {
            return ResponseEntity.badRequest().body("Job Admin not found.");
        }

        // Update the recruiter details
        Recruiter savedRecruiter = recruiterService.updateRecruiterDetails(recruiterId, updatedRecruiter, jobAdmin);

        return ResponseEntity.ok(savedRecruiter);
    }

    @GetMapping("/{id}/jobAdminId")
    public ResponseEntity<Long> getJobAdminIdByRecruiterId(@PathVariable Long id) {
        Long jobAdminId = recruiterService.getJobAdminIdByRecruiterId(id);
        return ResponseEntity.ok(jobAdminId);
    }
    @GetMapping("/by-job-admin/{jobAdminId}")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getRecruitersByJobAdminAndRole(
            @PathVariable Long jobAdminId,
            @RequestParam String recruiterRole) {

        Map<String, List<Map<String, Object>>> recruitersGrouped =
                recruiterService.getRecruitersByJobAdminAndRole(jobAdminId, recruiterRole);

        return ResponseEntity.ok(recruitersGrouped);
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateAndSendOTP(@RequestParam String recruiterEmail) {  // Parameter name matches entity
        try {
            boolean result = recruiterService.generateAndSendOTP(recruiterEmail);
            if (result) {
                return ResponseEntity.ok().body("OTP sent successfully to " + recruiterEmail);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Recruiter not found with email please contact your company admin: " + recruiterEmail);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating OTP: " + e.getMessage());
        }
    }

    @PostMapping("/change-password-with-otp")
    public ResponseEntity<String> changePassword(@RequestParam String recruiterEmail,
                                                 @RequestParam String recruiterOTP,
                                                 @RequestParam String newPassword) {
        try {
            String result = recruiterService.changePassword(recruiterEmail, recruiterOTP, newPassword);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/panelMember/{jobAdminId}")
    public List<Map<String, Object>> getPanelMembersByJobAdminId(@PathVariable Long jobAdminId) {
        return recruiterService.getPanelMembersByJobAdminId(jobAdminId);
    }

    @GetMapping("/{recruiterId}/email")
    public ResponseEntity<String> getRecruiterEmail(@PathVariable Long recruiterId) {
        try {
            String recruiterEmail = recruiterService.getRecruiterEmailById(recruiterId);
            return ResponseEntity.ok(recruiterEmail);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}