package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Feedback;
import fusionIQ.AI.V2.fusionIq.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/saveFeedback/{userId}")
    public Feedback createFeedback(@PathVariable Long userId, @RequestParam boolean feedback) {
        return feedbackService.createFeedback(userId, feedback);
    }

    @GetMapping("getFeedback/{id}")
    public Feedback getFeedbackById(@PathVariable Long id) {
        return feedbackService.findById(id);
    }

    @GetMapping("/getUserFeedback/{userId}")
    public List<Feedback> getFeedbackByUserId(@PathVariable Long userId) {
        return feedbackService.findByUserId(userId);
    }
}
