package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.AICoursePlan;
import fusionIQ.AI.V2.fusionIq.data.AICourseTopics;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.service.AICoursePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/aiCoursePlans")
public class AICoursePlanController {

    @Autowired
    private AICoursePlanService courseService;


    @GetMapping("/{userId}/{coursePlanId}")
    public Map<String, Object> getCoursePlanDetails(
            @PathVariable Long userId,
            @PathVariable Long coursePlanId) {
        return courseService.getCoursePlanAndTopics(userId, coursePlanId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AICoursePlan>> getCoursePlansByUserId(@PathVariable Long userId) {
        List<AICoursePlan> coursePlans = courseService.getCoursePlansByUserId(userId);
        return ResponseEntity.ok(coursePlans);
    }

    @GetMapping("/bycourseplan/{coursePlanId}")
    public ResponseEntity<List<AICourseTopics>> getTopicsByCoursePlanId(@PathVariable Long coursePlanId) {
        List<AICourseTopics> topics = courseService.getTopicsByCoursePlanId(coursePlanId);
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{id}")
    public AICoursePlan getAICoursePlanById(@PathVariable Long id) {
        return courseService.getAICoursePlanById(id);
    }

    @PostMapping("/aicourse")
    public ResponseEntity<AICoursePlan> createCoursePlan(@RequestBody Map<String, Object> request) {
        // Extract fields from the request map
        LocalDateTime createdAt = LocalDateTime.parse((String) request.get("createdAt"));
        String coursePlanName = (String) request.get("coursePlanName");
        String learningGoal = (String) request.get("learningGoal");
        String courseStartingPoint = (String) request.get("courseStartingPoint");
        String preferences = (String) request.get("preferences");
        int weekDuration = (int) request.get("weekDuration");
        int hoursPerWeek = (int) request.get("hoursPerWeek");
        Long userId = ((Number) request.get("userId")).longValue();

        // Create and map the entity
        AICoursePlan coursePlan = new AICoursePlan();
        coursePlan.setCreatedAt(createdAt);
        coursePlan.setCoursePlanName(coursePlanName);
        coursePlan.setLearningGoal(learningGoal);
        coursePlan.setCourseStartingPoint(courseStartingPoint);
        coursePlan.setPreferences(preferences);
        coursePlan.setWeekDuration(weekDuration);
        coursePlan.setHoursPerWeek(hoursPerWeek);


        User user = new User();
        user.setId(userId);
        coursePlan.setUser(user);


        AICoursePlan savedPlan = courseService.saveCoursePlan(coursePlan);
        return ResponseEntity.ok(savedPlan);
    }

    @PostMapping("/postaicoursetopics")
    public ResponseEntity<AICourseTopics> createCourseTopic(@RequestBody Map<String, Object> request) {
        // Extract fields from the request map
        int weekNumber = (int) request.get("week_number");
        String mainTopicTitle = (String) request.get("main_topic_title");
        String subTopic = (String) request.get("sub_topic");
        String explanation = (String) request.get("explanation");
        Long coursePlanId = ((Number) request.get("ai_course_plan_id")).longValue();
        String videoS3Key = (String) request.get("video_s3key");
        String videoS3Url = (String) request.get("video_s3url");
        Long videoCourseId = request.containsKey("video_course_id") ? ((Number) request.get("video_course_id")).longValue() : null;


        AICourseTopics topic = courseService.saveCourseTopic(
                weekNumber, mainTopicTitle, subTopic, explanation, coursePlanId, videoS3Key, videoS3Url, videoCourseId);


        return ResponseEntity.ok(topic);
    }
}