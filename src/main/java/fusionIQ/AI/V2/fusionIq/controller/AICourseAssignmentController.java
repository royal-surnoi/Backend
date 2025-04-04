package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.AICourseAssignment;
import fusionIQ.AI.V2.fusionIq.service.AICourseAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai-course-assignments")
public class AICourseAssignmentController {

    @Autowired
    private AICourseAssignmentService aiCourseAssignmentService;

    public static class AICourseAssignmentResponse {
        private Long id;
        private String aiCourseAssignment;
        private String aiCourseAssignmentUserAnswer;
        private Long aiCoursePlanId;

        public AICourseAssignmentResponse(Long id, String aiCourseAssignment, String aiCourseAssignmentUserAnswer, Long aiCoursePlanId) {
            this.id = id;
            this.aiCourseAssignment = aiCourseAssignment;
            this.aiCourseAssignmentUserAnswer = aiCourseAssignmentUserAnswer;
            this.aiCoursePlanId = aiCoursePlanId;
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAiCourseAssignment() {
            return aiCourseAssignment;
        }

        public void setAiCourseAssignment(String aiCourseAssignment) {
            this.aiCourseAssignment = aiCourseAssignment;
        }

        public String getAiCourseAssignmentUserAnswer() {
            return aiCourseAssignmentUserAnswer;
        }

        public void setAiCourseAssignmentUserAnswer(String aiCourseAssignmentUserAnswer) {
            this.aiCourseAssignmentUserAnswer = aiCourseAssignmentUserAnswer;
        }

        public Long getAiCoursePlanId() {
            return aiCoursePlanId;
        }

        public void setAiCoursePlanId(Long aiCoursePlanId) {
            this.aiCoursePlanId = aiCoursePlanId;
        }
    }

    @GetMapping("assignmentdetails/{id}")
    public ResponseEntity<AICourseAssignmentResponse> getAssignmentById(@PathVariable Long id) {
        return aiCourseAssignmentService.getAssignmentById(id)
                .map(assignment -> {
                    AICourseAssignmentResponse response = new AICourseAssignmentResponse(
                            assignment.getId(),
                            assignment.getAiCourseAssignment(),
                            assignment.getAiCourseAssignmentUserAnswer(),
                            assignment.getAiCoursePlan().getId() // Extract ai_course_plan_id
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/assignmentanswers/{id}")
    public ResponseEntity<List<String>> getAllAnswersExcept(@PathVariable Long id) {
        List<String> answers = aiCourseAssignmentService.getAllAnswersExcept(id);

        if (answers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(answers);
    }

    @PostMapping("/aicourseassignment")
    public ResponseEntity<AICourseAssignment> createAssignment(@RequestBody Map<String, Object> request) {

        Long coursePlanId = ((Number) request.get("ai_course_plan_id")).longValue();
        int weekNumber = (int) request.get("week_number");
        String aiCourseAssignment = (String) request.get("ai_course_assignment");


        AICourseAssignment assignment = aiCourseAssignmentService.saveAssignment(coursePlanId, weekNumber, aiCourseAssignment);


        return ResponseEntity.ok(assignment);
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