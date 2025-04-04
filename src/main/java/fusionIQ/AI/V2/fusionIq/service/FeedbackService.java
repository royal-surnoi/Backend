package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Feedback;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.FeedbackRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FeedbackRepo feedbackRepo;

    public Feedback createFeedback(Long userId, boolean feedbackValue) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Feedback feedback = new Feedback();
        feedback.setFeedback(feedbackValue);
        feedback.setUser(user);
        return feedbackRepo.save(feedback);
    }

    public Feedback findById(Long id) {
        return feedbackRepo.findById(id).orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    public List<Feedback> findByUserId(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return feedbackRepo.findByUserId(userId);
    }
}
