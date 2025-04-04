package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.AIAssignment;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.AIAssignmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AIAssignmentService {

    @Autowired
    CourseRepo courseRepo;

    @Autowired
    LessonRepo lessonRepo;

    @Autowired
    UserRepo userRepo;
    @Autowired
    AIAssignmentRepo aiAssignmentRepo;


    public AIAssignment saveAIAssignment(Long courseId, Long lessonId, Long userId, String AIAssignmentQuestion, String AIAssignmentAnswer, String AIAssignmentDescription) {
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new RuntimeException("Lesson not found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        AIAssignment aiAssignment = new AIAssignment();
        aiAssignment.setCourse(course);
        aiAssignment.setLesson(lesson);
        aiAssignment.setUser(user);
        aiAssignment.setAIAssignmentQuestion(AIAssignmentQuestion);
        aiAssignment.setAIAssignmentAnswer(AIAssignmentAnswer);
        aiAssignment.setAIAssignmentDescription(AIAssignmentDescription);
        aiAssignment.setCreatedAt(LocalDateTime.now());

        return aiAssignmentRepo.save(aiAssignment);
    }

    public List<AIAssignment> getAssignmentsByUserIdAndCourseId(Long userId, Long courseId) {
        return aiAssignmentRepo.findByUserIdAndCourseId(userId, courseId);
    }

    public Optional<AIAssignment> getAIAssignmentById(Long id) {
        return aiAssignmentRepo.findById(id);
    }

    public List<AIAssignment> getAllAssignments() {
        return aiAssignmentRepo.findAll();
    }

    public Optional<AIAssignment> getAssignmentById(Long id) {
        return aiAssignmentRepo.findById(id);
    }

    public void deleteAssignmentById(Long id) {
        aiAssignmentRepo.deleteById(id);
    }

    public AIAssignment updateUserAnswer(Long id, String AIAssignmentUserAnswer) {
        AIAssignment aiAssignment = aiAssignmentRepo.findById(id).orElseThrow(() -> new RuntimeException("AIAssignment not found"));
        aiAssignment.setAIAssignmentUserAnswer(AIAssignmentUserAnswer);
        return aiAssignmentRepo.save(aiAssignment);
    }

    public double calculateGrade(long assignmentId) {
        AIAssignment assignment = aiAssignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        // For simplicity, assume we have a basic grading logic where we check if the user's answer matches the correct answer
        String correctAnswer = assignment.getAIAssignmentAnswer();
        String userAnswer = assignment.getAIAssignmentUserAnswer();

        if (correctAnswer == null || userAnswer == null) {
            throw new IllegalArgumentException("Answers cannot be null");
        }

        double grade = 0.0;

        if (correctAnswer.equalsIgnoreCase(userAnswer)) {
            grade = 100.0;
        }

        return grade;
    }

    public List<AIAssignment> getAssignmentsByUserIdAndLessonId(Long userId, Long lessonId) {
        return aiAssignmentRepo.findByUserIdAndLessonId(userId, lessonId);
    }

    public AIAssignment updateAIAssignment(Long id, String userAnswer, String description) {
        Optional<AIAssignment> optionalAIAssignment = aiAssignmentRepo.findById(id);

        if (optionalAIAssignment.isPresent()) {
            AIAssignment aiAssignment = optionalAIAssignment.get();
            aiAssignment.setAIAssignmentUserAnswer(userAnswer);
            aiAssignment.setAIAssignmentDescription(description);
            return aiAssignmentRepo.save(aiAssignment);
        } else {
            throw new RuntimeException("AIAssignment not found with id: " + id);
        }
    }

    public AIAssignment updateAIAssignment(Long id, Map<String, String> updates) {
        Optional<AIAssignment> optionalAIAssignment = aiAssignmentRepo.findById(id);

        if (optionalAIAssignment.isPresent()) {
            AIAssignment aiAssignment = optionalAIAssignment.get();

            // Update only the provided fields
            if (updates.containsKey("userAnswer")) {
                aiAssignment.setAIAssignmentUserAnswer(updates.get("userAnswer"));
            }
            if (updates.containsKey("description")) {
                aiAssignment.setAIAssignmentDescription(updates.get("description"));
            }

            // Save the updated entity
            return aiAssignmentRepo.save(aiAssignment);
        } else {
            throw new RuntimeException("AIAssignment not found with id: " + id);
        }
    }
}