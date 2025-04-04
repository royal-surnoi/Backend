package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.Recruiter;
import fusionIQ.AI.V2.fusionIq.data.RecruiterFeedback;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.RecruiterFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RecruiterFeedbackService {

    @Autowired
    private RecruiterFeedbackRepository recruiterFeedbackRepository;

    // Method to give feedback
    public RecruiterFeedback giveFeedback(Long userId, Long recruiterId, Long jobId, String feedbackLevel, int feedbackScore) {
        // Convert the feedbackLevel String to the corresponding enum value
        RecruiterFeedback.FeedbackLevel level = RecruiterFeedback.FeedbackLevel.valueOf(feedbackLevel.toUpperCase()); // This converts string to enum

        RecruiterFeedback feedback = new RecruiterFeedback();
        feedback.setUser(new User(userId));  // Assuming a User constructor with ID
        feedback.setRecruiter(new Recruiter(recruiterId));  // Assuming a Recruiter constructor with ID
        feedback.setJob(new Job(jobId));  // Assuming a Job constructor with ID
        feedback.setFeedbackLevel(level);  // Set the enum value for feedback level
        feedback.setFeedbackScore(feedbackScore);  // Set the feedback score
        feedback.setTimestamp(LocalDateTime.now());  // Set the timestamp

        return recruiterFeedbackRepository.save(feedback);  // Save to the repository
    }

    // Method to get feedback
    public Optional<RecruiterFeedback> getFeedback(Long userId, Long recruiterId, Long jobId) {
        return recruiterFeedbackRepository.findByUserIdAndRecruiterIdAndJobId(userId, recruiterId, jobId);
    }
}
