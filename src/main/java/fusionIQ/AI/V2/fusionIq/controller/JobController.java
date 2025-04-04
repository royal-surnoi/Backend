package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import fusionIQ.AI.V2.fusionIq.data.Recruiter;
import fusionIQ.AI.V2.fusionIq.repository.JobRepository;
import fusionIQ.AI.V2.fusionIq.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    @Autowired
    private JobRepository jobRepository;
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // API to create a job by adminId and recruiterId
    @PostMapping("/createJob/{adminId}/{recruiterId}")
    public ResponseEntity<Job> createJob(@PathVariable Long adminId, @PathVariable Long recruiterId, @RequestBody Job job) {
        Job createdJob = jobService.createJob(adminId, recruiterId, job);
        return ResponseEntity.ok(createdJob);
    }

    @PostMapping("/createJob/{adminId}")
    public ResponseEntity<Job> createJob(@PathVariable Long adminId, @RequestBody Job job) {
        Job createdJob = jobService.createJob(adminId,  job);
        return ResponseEntity.ok(createdJob);
    }


    // Get jobs by recruiterId
    @GetMapping("/recruiter/{recruiterId}")
    public List<Job> getJobsByRecruiterId(@PathVariable Long recruiterId) {
        return jobService.getJobsByRecruiterId(recruiterId);
    }

    // Get jobs by adminId
    @GetMapping("/admin/{adminId}")
    public List<Job> getJobsByAdminId(@PathVariable Long adminId) {
        return jobService.getJobsByAdminId(adminId);
    }

    // Existing endpoint to get all jobs
    @GetMapping("/allJobs")
    public List<Job> getAllJobs() {
        // Call the service method to get the cleaned job data
        return jobService.getAllJobs();
    }
    @GetMapping("/activeJobs")
    public List<Job> getActiveJobs() {
        // Call the service method to get jobs where vacancyCount is not null or 0, and status is not "closed"
        return jobService.getActiveJobs();
    }


    @GetMapping("/skills/location")
    public ResponseEntity<List<Job>> getJobsBySkillsAndLocation(@RequestParam(required = false) String skills,
                                                                @RequestParam(required = false) String location) {
        List<Job> jobs = jobService.findJobsBySkillsAndLocation(skills, location);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{jodId}")
    public ResponseEntity<Job> getJobDetails(@PathVariable Long jodId) {
        Optional<Job> job = jobService.getJobDetails(jodId);
        return job.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/recruiters/{jobAdminId}")public List<Recruiter> getRecruitersByJobAdminId(@PathVariable Long jobAdminId)
    {
        return jobService.getRecruitersByJobAdminId(jobAdminId);
    }


    @DeleteMapping("/deleteJob/{adminId}/{recruiterId}/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long adminId, @PathVariable Long recruiterId, @PathVariable Long jobId) {
        jobService.deleteJob(adminId, recruiterId, jobId);
        return ResponseEntity.ok("Job deleted successfully.");
    }

    @DeleteMapping("/deleteJob/{adminId}/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long adminId, @PathVariable Long jobId) {
        jobService.deleteJob(adminId, jobId);
        return ResponseEntity.ok("Job deleted successfully.");
    }
    @PutMapping("/updateJob/{jobId}/{adminId}")
    public ResponseEntity<Job> updateJob(@PathVariable Long jobId, @PathVariable Long adminId, @RequestBody Job updatedJob) {
        Job job = jobService.updateJob(jobId, adminId, updatedJob);
        return ResponseEntity.ok(job);
    }

    @PutMapping("update/{jobId}/{recruiterId}")
    public ResponseEntity<Job> updateJobByRecruiter(
            @PathVariable Long jobId,
            @PathVariable Long recruiterId,
            @RequestBody Job updatedJob) {
        Job job = jobService.updateJobByRecruiter(jobId, recruiterId, updatedJob);
        return ResponseEntity.ok(job);
    }

    // Endpoint to delete a job
    @DeleteMapping("delete/{jobId}/{recruiterId}")
    public ResponseEntity<Void> deleteJobByRecruiter(
            @PathVariable Long jobId,
            @PathVariable Long recruiterId) {
        jobService.deleteJobByRecruiter(jobId, recruiterId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/recruiter/{recruiterId}/details")
    public ResponseEntity<List<Map<String, Object>>> getJobsWithSelectedDetails(@PathVariable Long recruiterId) {
        List<Map<String, Object>> jobs = jobService.getJobsWithSelectedDetails(recruiterId);
        return ResponseEntity.ok(jobs);
    }
    // API to fetch jobs by admin ID (only job and admin details)
    @GetMapping("/admin/{adminId}/details")
    public List<Job> getJobsWithAdminDetails(@PathVariable Long adminId) {
        return jobService.getJobsWithAdminDetails(adminId);
    }
    @PutMapping("/jobClose/{recruiterId}/{jobId}")
    public ResponseEntity<String> closeJob(@PathVariable Long recruiterId, @PathVariable Long jobId) {
        try {
            boolean isClosed = jobService.closeJobByRecruiterIdAndJobId(recruiterId, jobId);
            if (isClosed) {
                return ResponseEntity.ok("Job status updated to 'closed'");
            } else {
                return ResponseEntity.status(404).body("Job not found or not accessible by this recruiter");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error closing job: " + e.getMessage());
        }
    }
    @PutMapping("/jobOpen/{recruiterId}/{jobId}")
    public ResponseEntity<String> openJob(@PathVariable Long recruiterId, @PathVariable Long jobId) {
        try {
            boolean isOpened = jobService.openJobByRecruiterIdAndJobId(recruiterId, jobId);
            if (isOpened) {
                return ResponseEntity.ok("Job status updated to 'open'");
            } else {
                return ResponseEntity.status(404).body("Job not found or not accessible by this recruiter");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error opening job: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAnyJob/{adminId}/{jobId}")
    public ResponseEntity<String> deleteJobByAdmin(@PathVariable Long adminId, @PathVariable Long jobId) {
        jobService.deleteJobByAdmin(adminId, jobId);
        return ResponseEntity.ok("Job deleted successfully by admin.");
    }

    @PutMapping("/{jobId}/status")
    public ResponseEntity<String> updateJobStatus(
            @PathVariable Long jobId,
            @RequestParam(required = false) Long recruiterId,
            @RequestParam(required = false) Long jobAdminId,
            @RequestParam String status) {
        try {
            jobService.updateJobStatus(jobId, recruiterId, jobAdminId, status);
            return ResponseEntity.ok("Job status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the job status.");
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Job>> getActiveJobs(
            @RequestParam(required = false) Long recruiterId,
            @RequestParam(required = false) Long jobAdminId) {
        try {
            if ((recruiterId != null && jobAdminId != null) || (recruiterId == null && jobAdminId == null)) {
                return ResponseEntity.badRequest().body(null);
            }
            List<Job> jobs = jobService.getActiveJobs(recruiterId, jobAdminId);
            return ResponseEntity.ok(jobs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/activeAdminJobs")
    public ResponseEntity<List<Job>> getActiveJobs(@RequestParam Long jobAdminId) {
        try {
            if (jobAdminId == null) {
                return ResponseEntity.badRequest().body(null);
            }
            List<Job> jobs = jobService.getActiveJobs(jobAdminId);
            return ResponseEntity.ok(jobs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

