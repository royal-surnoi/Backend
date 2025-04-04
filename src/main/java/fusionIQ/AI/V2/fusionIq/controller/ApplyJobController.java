package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.ApplyJob;
import fusionIQ.AI.V2.fusionIq.service.ApplyJobService;
import fusionIQ.AI.V2.fusionIq.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplyJobController {

    private final ApplyJobService applyJobService;
    @Autowired
    private JobService jobService;

    public ApplyJobController(ApplyJobService applyJobService) {
        this.applyJobService = applyJobService;
    }

    @PostMapping("/{jobId}/{userId}")
    public ApplyJob applyJob(@PathVariable Long jobId, @PathVariable Long userId,
                             @RequestParam(required = false) String status,
                             @RequestParam("resume") MultipartFile resume) throws Exception {
        if (status == null || status.isEmpty()) {
            status = "applied";
        }

        byte[] resumeBytes = resume.getBytes();

        return applyJobService.applyJob(jobId, userId, status, resumeBytes);
    }



    @GetMapping("/user/{userId}")
    public List<ApplyJob> getApplicationsByUserId(@PathVariable Long userId) {
        return applyJobService.getApplicationsByUserId(userId);
    }

    @GetMapping("/job/{jobId}")
    public Map<String, Object> getApplicationsByJobId(@PathVariable Long jobId) {
        return applyJobService.getApplicationsByJobId(jobId);
    }

    // PUT API to update job application status
    @PutMapping("/{applicationId}/status")
    public ApplyJob updateApplicationStatus(@PathVariable Long applicationId, @RequestParam String status) {
        return applyJobService.updateApplicationStatus(applicationId, status);
    }

    // New GET API to get the current status of a job application for a specific user and job
    @GetMapping("/{jobId}/{userId}/status")
    public String getApplicationStatus(@PathVariable Long jobId, @PathVariable Long userId) {
        ApplyJob application = applyJobService.getApplicationByJobAndUser(jobId, userId);
        if (application != null) {
            return application.getStatus();
        } else {
            return "No application found for the provided job and user.";
        }
    }
    @GetMapping("/getApplicants/{jobId}")
    public Map<String, Object> getApplicationsWithEducationByJobId(@PathVariable Long jobId) {
        return applyJobService.getApplicationsWithEducationByJobId(jobId);
    }


    @DeleteMapping("/delete/{applicationId}")
    public String deleteApplication(@PathVariable Long applicationId) {
        try {
            applyJobService.deleteApplication(applicationId);
            return "Application with ID " + applicationId + " has been successfully deleted.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/{applicationId}/withdraw")
    public ApplyJob updateWithdrawStatus(@PathVariable Long applicationId) {
        return applyJobService.updateWithdrawStatus(applicationId);
    }

    @GetMapping("/user/{userId}/active")
    public List<ApplyJob> getActiveApplicationsByUserId(@PathVariable Long userId) {
        return applyJobService.getActiveApplicationsByUserId(userId);
    }

    @PutMapping("/{applicationId}/reapply")
    public String reapplyForJob(@PathVariable Long applicationId) {
        applyJobService.reapplyForJob(applicationId);
        return "Reapplication was successful.";
    }
    @GetMapping("isApplied/{jobId}/{userId}")
    public ResponseEntity<Map<String, Boolean>> isJobOpenAndApplied(@PathVariable Long jobId, @PathVariable Long userId) {
        boolean isOpen = jobService.checkIfJobIsOpen(jobId);
        boolean isApplied = applyJobService.Isapply(userId, jobId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isOpen", isOpen);
        response.put("isApplied", isApplied);
        return ResponseEntity.ok(response);}
}
