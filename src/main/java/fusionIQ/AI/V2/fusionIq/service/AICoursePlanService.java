package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.AICoursePlan;
import fusionIQ.AI.V2.fusionIq.data.AICourseTopics;
import fusionIQ.AI.V2.fusionIq.repository.AICoursePlanRepo;
import fusionIQ.AI.V2.fusionIq.repository.AICourseTopicsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AICoursePlanService {

    @Autowired
    private AICoursePlanRepo coursePlanRepository;

    @Autowired
    private AICourseTopicsRepo topicsRepository;

    // Fetch course plan details by user ID and course plan ID
    public Optional<AICoursePlan> getCoursePlanByUserAndId(Long userId, Long coursePlanId) {
        return coursePlanRepository.findByIdAndUserId(coursePlanId, userId);
    }

    // Fetch topics by course plan ID
    public List<AICourseTopics> getTopicsByCoursePlanId(Long coursePlanId) {
        return topicsRepository.findByAiCoursePlanId(coursePlanId);
    }

    // Fetch combined course plan and topics
    public Map<String, Object> getCoursePlanAndTopics(Long userId, Long coursePlanId) {
        Map<String, Object> response = new HashMap<>();

        // Fetch course plan
        Optional<AICoursePlan> coursePlan = getCoursePlanByUserAndId(userId, coursePlanId);
        if (coursePlan.isPresent()) {
            response.put("coursePlan", coursePlan.get());
        } else {
            response.put("message", "Course plan not found for the given user and course plan ID.");
            return response;
        }

        // Fetch topics for the course plan
        List<AICourseTopics> topics = getTopicsByCoursePlanId(coursePlanId);
        response.put("topics", topics);

        return response;
    }

    public List<AICoursePlan> getCoursePlansByUserId(Long userId) {
        return coursePlanRepository.findByUserId(userId);
    }

    public List<AICourseTopics> getTopicsByCoursePlanIdandsorted(Long coursePlanId) {
        return topicsRepository.findByAiCoursePlanIdOrderByWeekNumberAsc(coursePlanId);
    }

    public AICoursePlan getAICoursePlanById(Long id) {
        return coursePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AICoursePlan not found with id: " + id));
    }

    public AICoursePlan saveCoursePlan(AICoursePlan coursePlan) {
        return coursePlanRepository.save(coursePlan);
    }

    public AICourseTopics saveCourseTopic(int weekNumber, String mainTopicTitle, String subTopic, String explanation,
                                          Long coursePlanId, String videoS3Key, String videoS3Url, Long videoCourseId) {
        // Fetch the related AICoursePlan entity
        Optional<AICoursePlan> coursePlanOptional = coursePlanRepository.findById(coursePlanId);
        if (coursePlanOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid course plan ID: " + coursePlanId);
        }

        AICoursePlan coursePlan = coursePlanOptional.get();

        // Create and populate the AICourseTopics entity
        AICourseTopics topic = new AICourseTopics();
        topic.setWeekNumber(weekNumber);
        topic.setMainTopicTitle(mainTopicTitle);
        topic.setSubTopic(subTopic);
        topic.setExplanation(explanation);
        topic.setAiCoursePlan(coursePlan);
        topic.setVideoS3Key(videoS3Key);
        topic.setVideoS3Url(videoS3Url);
        topic.setVideoCourseId(videoCourseId);

        // Save the topic to the database
        return topicsRepository.save(topic);
    }
}