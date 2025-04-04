package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.RecruiterFeedback;
import fusionIQ.AI.V2.fusionIq.service.RecruiterFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/feedback")
public class RecruiterFeedbackController {

    @Autowired
    private RecruiterFeedbackService recruiterFeedbackService;

    // Endpoint to give feedback
    @PostMapping("/{userId}/{recruiterId}/{jobId}")
    public ResponseEntity<RecruiterFeedback> giveFeedback(
            @PathVariable Long userId,
            @PathVariable Long recruiterId,
            @PathVariable Long jobId,
            @RequestParam String feedbackLevel,
            @RequestParam int feedbackScore) {

        try {
            // Call the service without the score parameter
            RecruiterFeedback feedback = recruiterFeedbackService.giveFeedback(
                    userId, recruiterId, jobId, feedbackLevel, feedbackScore);

            return ResponseEntity.ok(feedback);
        } catch (IllegalArgumentException e) {
            // Return a bad request response if the feedbackLevel is invalid
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint to get feedback
    @GetMapping("/{userId}/{recruiterId}/{jobId}")
    public ResponseEntity<RecruiterFeedback> getFeedback(
            @PathVariable Long userId,
            @PathVariable Long recruiterId,
            @PathVariable Long jobId) {

        Optional<RecruiterFeedback> feedback = recruiterFeedbackService.getFeedback(userId, recruiterId, jobId);
        return feedback.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
