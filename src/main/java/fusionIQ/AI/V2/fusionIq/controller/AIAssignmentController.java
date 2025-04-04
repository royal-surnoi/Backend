package fusionIQ.AI.V2.fusionIq.controller;
import fusionIQ.AI.V2.fusionIq.data.AIAssignment;
import fusionIQ.AI.V2.fusionIq.data.AICourseAssignment;
import fusionIQ.AI.V2.fusionIq.repository.AIAssignmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.AIAssignmentService;
import fusionIQ.AI.V2.fusionIq.service.AICourseAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class AIAssignmentController {
    @Autowired
    AIAssignmentRepo aiAssignmentRepo;

    @Autowired
    AIAssignmentService aiAssignmentService;

    @Autowired
    AICourseAssignmentService aiCourseAssignmentService;
    @Autowired
    CourseRepo courseRepo;
    @Autowired
    LessonRepo lessonRepo;
    @Autowired
    UserRepo userRepo;

    @PostMapping("/createAIAssignment/{courseId}/{userId}/{lessonId}")
    public ResponseEntity<AIAssignment> createAIAssignment(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @PathVariable Long userId,
            @RequestParam String AIAssignmentQuestion,
            @RequestParam(required = false) String AIAssignmentDescription, // Optional field
            @RequestParam String AIAssignmentAnswer) {


        if (AIAssignmentDescription == null) {
            AIAssignmentDescription = "";
        }

        AIAssignment savedAssignment = aiAssignmentService.saveAIAssignment(
                courseId, lessonId, userId, AIAssignmentQuestion, AIAssignmentAnswer, AIAssignmentDescription);

        return new ResponseEntity<>(savedAssignment, HttpStatus.CREATED);
    }
    @GetMapping("/get/{userId}/{courseId}")
    public ResponseEntity<List<AIAssignment>> getAssignmentsByUserIdAndCourseId(
            @PathVariable Long userId,
            @PathVariable Long courseId) {

        List<AIAssignment> assignments = aiAssignmentService.getAssignmentsByUserIdAndCourseId(userId, courseId);
        return ResponseEntity.ok(assignments);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AIAssignment> getAIAssignmentById(@PathVariable Long id) {
        Optional<AIAssignment> assignment = aiAssignmentService.getAIAssignmentById(id);
        return assignment.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/getAll/AIassignment")
    public ResponseEntity<List<AIAssignment>> getAllAssignments() {
        List<AIAssignment> assignments = aiAssignmentService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }
    @DeleteMapping("/AIAssignment/{id}")
    public ResponseEntity<Void> deleteAssignmentById(@PathVariable Long id) {
        if (aiAssignmentService.getAssignmentById(id).isPresent()) {
            aiAssignmentService.deleteAssignmentById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/{id}/user-answer")
    public ResponseEntity<AIAssignment> updateUserAnswer(@PathVariable Long id, @RequestBody String AIAssignmentUserAnswer) {
        AIAssignment updatedAssignment = aiAssignmentService.updateUserAnswer(id, AIAssignmentUserAnswer);
        return ResponseEntity.ok(updatedAssignment);
    }
    @GetMapping("/assignments/{id}/grade")
    public double getGrade(@PathVariable long id) {
        return aiAssignmentService.calculateGrade(id);
    }



    @GetMapping("/getByLesson/{userId}/{lessonId}")
    public ResponseEntity<List<AIAssignment>> getAssignmentsByUserIdAndLessonId(
            @PathVariable Long userId,
            @PathVariable Long lessonId) {
        // Log input values
        System.out.println("Fetching assignments for userId: " + userId + " and lessonId: " + lessonId);
        List<AIAssignment> assignments = aiAssignmentService.getAssignmentsByUserIdAndLessonId(userId, lessonId);
        // Log the result
        System.out.println("Assignments found: " + assignments);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/{id}/submitAssignment")
    public AIAssignment updateAIAssignment(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        String userAnswer = requestBody.get("userAnswer");
        String description = requestBody.get("description");
        return aiAssignmentService.updateAIAssignment(id, userAnswer, description);
    }

    @PutMapping("/{id}/updateUserAnswer")
    public AIAssignment updateAIAssignmentUserAnswer(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        return aiAssignmentService.updateAIAssignment(id, requestBody);
    }

    @GetMapping("/bycourseplan/{coursePlanId}")
    public ResponseEntity<List<AICourseAssignment>> getAssignmentsByCoursePlanId(@PathVariable Long coursePlanId) {
        List<AICourseAssignment> assignments = aiCourseAssignmentService.getAssignmentsByCoursePlanId(coursePlanId);
        return ResponseEntity.ok(assignments);
    }

    @PutMapping("/{id}/answer")
    public AICourseAssignment updateAssignmentAnswer(@PathVariable Long id, @RequestBody String userAnswer) {
        return aiCourseAssignmentService.updateAssignmentAnswer(id, userAnswer);
    }
}
