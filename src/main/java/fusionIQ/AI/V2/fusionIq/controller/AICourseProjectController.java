package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.AICourseProject;
import fusionIQ.AI.V2.fusionIq.service.AICourseProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class AICourseProjectController {

    @Autowired
    private AICourseProjectService aiCourseProjectService;


    public static class AICourseProjectResponse {
        private Long id;
        private String aiCourseProject;
        private String aiCourseProjectUserAnswer;
        private Long aiCoursePlanId;

        public AICourseProjectResponse(Long id, String aiCourseProject, String aiCourseProjectUserAnswer, Long aiCoursePlanId) {
            this.id = id;
            this.aiCourseProject = aiCourseProject;
            this.aiCourseProjectUserAnswer = aiCourseProjectUserAnswer;
            this.aiCoursePlanId = aiCoursePlanId;
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAiCourseProject() {
            return aiCourseProject;
        }

        public void setAiCourseProject(String aiCourseProject) {
            this.aiCourseProject = aiCourseProject;
        }

        public String getAiCourseProjectUserAnswer() {
            return aiCourseProjectUserAnswer;
        }

        public void setAiCourseProjectUserAnswer(String aiCourseProjectUserAnswer) {
            this.aiCourseProjectUserAnswer = aiCourseProjectUserAnswer;
        }

        public Long getAiCoursePlanId() {
            return aiCoursePlanId;
        }

        public void setAiCoursePlanId(Long aiCoursePlanId) {
            this.aiCoursePlanId = aiCoursePlanId;
        }
    }

    @GetMapping("projectdetails/{id}")
    public ResponseEntity<AICourseProjectResponse> getProjectById(@PathVariable Long id) {
        return aiCourseProjectService.getProjectById(id)
                .map(project -> {
                    AICourseProjectResponse response = new AICourseProjectResponse(
                            project.getId(),
                            project.getAiCourseProject(),
                            project.getAiCourseProjectUserAnswer(),
                            project.getAiCoursePlan().getId() // Extract ai_course_plan_id
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/answers/{id}")
    public ResponseEntity<List<String>> getAllAnswersExcept(@PathVariable Long id) {
        List<String> answers = aiCourseProjectService.getAllAnswersExcept(id);

        if (answers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(answers);
    }

    @GetMapping("/by-course-plan/{coursePlanId}")
    public ResponseEntity<List<AICourseProject>> getProjectsByCoursePlanId(@PathVariable Long coursePlanId) {
        List<AICourseProject> projects = aiCourseProjectService.getProjectsByCoursePlanId(coursePlanId);
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{id}/answer")
    public AICourseProject updateProjectAnswer(@PathVariable Long id, @RequestBody String userAnswer) {
        return aiCourseProjectService.updateProjectAnswer(id, userAnswer);
    }
    @PostMapping ("/postaicourseproject")
    public ResponseEntity<AICourseProject> createCourseProject(@RequestBody Map<String, Object> request) {
        // Extract fields from the request map
        Long coursePlanId = ((Number) request.get("ai_course_plan_id")).longValue();
        String aiCourseProject = (String) request.get("ai_course_project");

        // Save the project using the service
        AICourseProject project = aiCourseProjectService.saveCourseProject(coursePlanId, aiCourseProject);

        // Return the saved project
        return ResponseEntity.ok(project);
    }
}