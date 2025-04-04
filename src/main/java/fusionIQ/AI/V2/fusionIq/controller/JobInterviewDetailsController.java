package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.JobInterviewDetails;

import fusionIQ.AI.V2.fusionIq.repository.JobInterviewDetailsRepo;

import fusionIQ.AI.V2.fusionIq.service.JobInterviewDetailsService;

import fusionIQ.AI.V2.fusionIq.service.TrainingRoomService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.mail.SimpleMailMessage;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import java.util.Optional;

import java.util.UUID;

@RestController

@RequestMapping("/api/interviews")

public class JobInterviewDetailsController {

    @Autowired

    private JobInterviewDetailsRepo jobInterviewDetailsRepo;

    private final JobInterviewDetailsService jobInterviewDetailsService;

    public JobInterviewDetailsController(JobInterviewDetailsService jobInterviewDetailsService) {

        this.jobInterviewDetailsService = jobInterviewDetailsService;

    }


    @PostMapping("/shortlist")

    public ResponseEntity<?> shortlistCandidate(

            @RequestBody JobInterviewDetails request,

            @RequestParam Long jobId) {

        List<JobInterviewDetails> existingDetails =

                jobInterviewDetailsService.findByUserIdAndJobId(request.getUserId(), jobId);


        JobInterviewDetails savedDetails;

        // Save new shortlist details

        if (request.getRecruiterId() != null) {

            savedDetails = jobInterviewDetailsService.saveShortlistDetails(

                    request.getUserId(), request.getUserName(), request.getUserEmail(),

                    request.getRecruiterId(), request.getRecruiterName(), request.getRecruiterEmail(),

                    request.getCurrentFeedback(), jobId, null, null, null,existingDetails

            );

        } else if (request.getAdminId() != null) {

            savedDetails = jobInterviewDetailsService.saveShortlistDetails(

                    request.getUserId(), request.getUserName(), request.getUserEmail(),

                    null, null, null, request.getCurrentFeedback(), jobId,

                    request.getAdminId(), request.getAdminName(), request.getAdminEmail(),existingDetails

            );

        }

        else if (jobId == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID is required");

        }

        else {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(savedDetails, HttpStatus.CREATED);

    }

    @PutMapping("/update/{id}")

    public ResponseEntity<JobInterviewDetails> updateInterviewDetails(

            @PathVariable Long id,

            @RequestBody JobInterviewDetails updatedDetails) {

        JobInterviewDetails savedDetails = jobInterviewDetailsService.updateInterviewDetails(id, updatedDetails);

        return ResponseEntity.ok(savedDetails);

    }


    @GetMapping("/interviewDetails/{jobId}/{adminId}")

    public ResponseEntity<JobInterviewDetails> getByJobIdAndAdminId(

            @PathVariable Long jobId,

            @PathVariable Long adminId) {

        JobInterviewDetails details = jobInterviewDetailsRepo.findByJobIdAndAdminId(jobId, adminId);

        return ResponseEntity.ok(details);

    }

    // API to get details by jobId and userId

    @GetMapping("/interviewDetails/{jobId}/user/{userId}")

    public ResponseEntity<List<JobInterviewDetails>> getByJobIdAndUserId(

            @PathVariable Long jobId,

            @PathVariable Long userId) {

        List<JobInterviewDetails> details = jobInterviewDetailsRepo.findByJobIdAndUserId(jobId, userId);

        return ResponseEntity.ok(details);

    }

    @PutMapping("/update-score-feedback/{interviewId}")

    public ResponseEntity<String> updateScoreAndFeedback(

            @PathVariable Long interviewId,

            @RequestParam Integer interviewerScore,

            @RequestParam String interviewerFeedback) {

        try {

            // Fetch the existing interview details

            JobInterviewDetails interviewDetails = jobInterviewDetailsRepo.findById(interviewId)

                    .orElseThrow(() -> new IllegalArgumentException("Interview details not found for id: " + interviewId));

            // Update the score and feedback

            interviewDetails.setInterviewerScore(interviewerScore);

            interviewDetails.setInterviewerFeedback(interviewerFeedback);

            // Save the updated details back to the database

            jobInterviewDetailsRepo.save(interviewDetails);

            return ResponseEntity.ok("Interview score and feedback updated successfully.");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

                    .body("Failed to update interview score and feedback: " + e.getMessage());

        }

    }

    @PostMapping("/save-score-feedback")

    public ResponseEntity<String> saveScoreAndFeedback(

            @RequestParam Long interviewId,

            @RequestParam Integer interviewerScore,

            @RequestParam String interviewerFeedback) {

        try {

            JobInterviewDetails interviewDetails = jobInterviewDetailsRepo.findById(interviewId)

                    .orElseThrow(() -> new IllegalArgumentException("Interview not found for ID: " + interviewId));

            interviewDetails.setInterviewerScore(interviewerScore);

            interviewDetails.setInterviewerFeedback(interviewerFeedback);

            jobInterviewDetailsRepo.save(interviewDetails);

            return ResponseEntity.ok("Interview score and feedback saved successfully.");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

                    .body("Error saving interview score and feedback: " + e.getMessage());

        }

    }

    // Get interview score and feedback

    @GetMapping("/score-feedback/{interviewId}")

    public ResponseEntity<JobInterviewDetails> getScoreAndFeedback(@PathVariable Long interviewId) {

        try {

            JobInterviewDetails interviewDetails = jobInterviewDetailsRepo.findById(interviewId)

                    .orElseThrow(() -> new IllegalArgumentException("Interview not found for ID: " + interviewId));

            return ResponseEntity.ok(interviewDetails);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)

                    .body(null);

        }


    }

    @GetMapping("/interviewDetails/job/{jobId}/{UserId}")

    public ResponseEntity<JobInterviewDetails> getByJobIdAndRecruiterId(

            @PathVariable Long jobId,

            @PathVariable Long UserId) {

        JobInterviewDetails details = jobInterviewDetailsRepo.findByJobIdAndUserIdgetone(jobId, UserId);

        return ResponseEntity.ok(details);

    }


    @GetMapping("/today/recruiter/{recruiterId}")

    public List<JobInterviewDetails> getTodayInterviewsByRecruiterId(@PathVariable Long recruiterId) {

        return jobInterviewDetailsService.getTodayInterviewsByRecruiterId(recruiterId);

    }

    @GetMapping("/today/admin/{adminId}")

    public ResponseEntity<List<JobInterviewDetails>> getInterviewsFromTodayByAdminId(

            @PathVariable Long adminId) {

        List<JobInterviewDetails> interviews = jobInterviewDetailsService.findInterviewsFromTodayByAdminId(adminId);

        return ResponseEntity.ok(interviews);

    }

    @GetMapping("/today/user/{userId}")

    public ResponseEntity<List<JobInterviewDetails>> getInterviewsFromTodayByUserId(

            @PathVariable Long userId) {

        List<JobInterviewDetails> interviews = jobInterviewDetailsService.findInterviewsFromTodayByUserId(userId);

        return ResponseEntity.ok(interviews);

    }

    @GetMapping("/today/interviewer/{email}")
    public ResponseEntity<List<JobInterviewDetails>> getInterviewsFromTodayByInterviewerEmail(
            @PathVariable String email) {
        List<JobInterviewDetails> interviews = jobInterviewDetailsService.findInterviewsFromTodayByInterviewerEmail(email);
        return ResponseEntity.ok(interviews);
    }




}

