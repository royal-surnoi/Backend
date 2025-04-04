package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.ShortlistedCandidates;
import fusionIQ.AI.V2.fusionIq.data.ApplyJob;
import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.repository.ApplyJobRepository;
import fusionIQ.AI.V2.fusionIq.repository.ShortlistedCandidatesRepository;
import fusionIQ.AI.V2.fusionIq.service.ApplyJobService;
import fusionIQ.AI.V2.fusionIq.service.JobService;
import fusionIQ.AI.V2.fusionIq.service.ShortlistedCandidatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shortlisted")
public class ShortlistedCandidatesController {


    @Autowired
    private ApplyJobRepository applyJobRepository;

    @Autowired
    private ShortlistedCandidatesRepository shortlistedCandidatesRepository;

    private final ShortlistedCandidatesService shortlistedCandidatesService;
    private final ApplyJobService applyJobService;
    private final JobService jobService;

    public ShortlistedCandidatesController(ShortlistedCandidatesService shortlistedCandidatesService,
                                           ApplyJobService applyJobService,
                                           JobService jobService) {
        this.shortlistedCandidatesService = shortlistedCandidatesService;
        this.applyJobService = applyJobService;
        this.jobService = jobService;
    }


    @PostMapping("/{jobId}/{applicantId}")
    public ResponseEntity<Map<String, Object>> shortlistCandidate(
            @PathVariable Long jobId,
            @PathVariable Long applicantId,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");
        if (status == null || status.isEmpty()) {
            throw new RuntimeException("Status is required in the request body");
        }

        ShortlistedCandidates shortlistedCandidate = shortlistedCandidatesService.shortlistCandidate(jobId, applicantId, status);

        // Map the response to include relevant data
        Map<String, Object> response = new HashMap<>();
        response.put("id", shortlistedCandidate.getId());
        response.put("status", shortlistedCandidate.getStatus());
        response.put("shortlistedAt", shortlistedCandidate.getShortlistedAt());
        response.put("jobId", shortlistedCandidate.getJob().getId());
        response.put("applicantId", shortlistedCandidate.getApplyJob().getId());
        response.put("userId", shortlistedCandidate.getUser().getId());
        response.put("recruiterId", shortlistedCandidate.getRecruiterId());
        response.put("adminId", shortlistedCandidate.getAdminId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/shortlistMultipleCandidates/{jobId}")
    public ResponseEntity<Map<String, Object>> shortlistMultipleCandidates(
            @PathVariable Long jobId,
            @RequestBody Map<String, Object> request) {

        // Extract applicant IDs and status from the request body
        List<Long> applicantIds = ((List<?>) request.get("applicantIds"))
                .stream()
                .map(id -> {
                    if (id instanceof Integer) {
                        return ((Integer) id).longValue();
                    } else if (id instanceof Long) {
                        return (Long) id;
                    } else {
                        throw new IllegalArgumentException("Invalid applicant ID type: " + id.getClass());
                    }
                })
                .collect(Collectors.toList());

        String status = (String) request.get("status");

        // Validate inputs
        if (applicantIds == null || applicantIds.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Applicant IDs are required"));
        }
        if (status == null || status.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Status is required"));
        }

        // Call the service method to process the shortlisting
        Map<String, Object> result = shortlistedCandidatesService.shortlistMultipleCandidates(jobId, applicantIds, status);

        return ResponseEntity.ok(result);
    }
    @GetMapping("/job/{jobId}")
    public Map<String, Object> getShortlistedCandidatesByJobId(@PathVariable Long jobId) {
        List<Map<String, Object>> shortlistedCandidates = shortlistedCandidatesService.getShortlistedCandidatesWithDetailsByJobId(jobId);
        Map<String, Object> response = new HashMap<>();
        response.put("shortlistedCandidates", shortlistedCandidates);
        return response;
    }

    @GetMapping("/all")
    public Map<String, Object> getAllShortlistedCandidates() {
        List<Map<String, Object>> shortlistedCandidates = shortlistedCandidatesService.getAllShortlistedCandidatesWithDetails();
        Map<String, Object> response = new HashMap<>();
        response.put("shortlistedCandidates", shortlistedCandidates);
        return response;
    }


    @PostMapping("/candidate")
    public ResponseEntity<?> shortlistCandidate(
            @RequestParam Long adminId,
            @RequestParam Long applyJobId,
            @RequestParam String status // e.g., "Shortlisted"
    ) {
        // Find the ApplyJob by ID
        ApplyJob applyJob = applyJobRepository.findById(applyJobId)
                .orElseThrow(() -> new RuntimeException("ApplyJob not found with id: " + applyJobId));

        // Create a new ShortlistedCandidates instance
        ShortlistedCandidates shortlistedCandidate = new ShortlistedCandidates();
        shortlistedCandidate.setApplyJob(applyJob);
        shortlistedCandidate.setJob(applyJob.getJob());
        shortlistedCandidate.setUser(applyJob.getUser());
        shortlistedCandidate.setStatus(status);
        shortlistedCandidate.setAdminId(adminId);
        shortlistedCandidate.setShortlistedAt(java.time.LocalDateTime.now());

        // Save the new shortlisted candidate
        shortlistedCandidatesRepository.save(shortlistedCandidate);

        return ResponseEntity.ok("Candidate successfully shortlisted");
    }

    @PostMapping("/multiple-candidates")
    public ResponseEntity<?> shortlistMultipleCandidates(
            @RequestBody Map<String, Object> requestBody
    ) {
        Long adminId = requestBody.containsKey("adminId")
                ? Long.valueOf(requestBody.get("adminId").toString())
                : null;
        List<Map<String, Object>> candidates = requestBody.containsKey("candidates")
                ? (List<Map<String, Object>>) requestBody.get("candidates")
                : null;

        if (adminId == null || candidates == null) {
            return ResponseEntity.badRequest().body("Invalid request payload: Missing adminId or candidates");
        }

        List<ShortlistedCandidates> shortlistedCandidatesList = new ArrayList<>();
        for (Map<String, Object> candidate : candidates) {
            Long applyJobId = candidate.containsKey("applyJobId")
                    ? Long.valueOf(candidate.get("applyJobId").toString())
                    : null;
            String status = candidate.containsKey("status")
                    ? candidate.get("status").toString()
                    : null;

            if (applyJobId == null || status == null) {
                continue; // Skip invalid entries
            }

            ApplyJob applyJob = applyJobRepository.findById(applyJobId)
                    .orElseThrow(() -> new RuntimeException("ApplyJob not found with id: " + applyJobId));

            ShortlistedCandidates shortlistedCandidate = new ShortlistedCandidates();
            shortlistedCandidate.setApplyJob(applyJob);
            shortlistedCandidate.setJob(applyJob.getJob());
            shortlistedCandidate.setUser(applyJob.getUser());
            shortlistedCandidate.setStatus(status);
            shortlistedCandidate.setAdminId(adminId);
            shortlistedCandidate.setShortlistedAt(LocalDateTime.now());

            shortlistedCandidatesList.add(shortlistedCandidate);
        }

        shortlistedCandidatesRepository.saveAll(shortlistedCandidatesList);

        return ResponseEntity.ok("Multiple candidates successfully shortlisted");
    }


}
