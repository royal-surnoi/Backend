package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/submission")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;
    @Autowired
    SubmissionRepo submissionRepo;
    @Autowired
    ActivityRepo activityRepo;
    @Autowired
    ProjectRepo projectRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CriteriaRepo criteriaRepo;
    @Autowired
    GradeRepo gradeRepo;

    @PostMapping("/save/{activityId}/{userId}/{projectId}")
    public ResponseEntity<Submission> createSubmission(@RequestBody Submission submission, @PathVariable Long activityId, @PathVariable Long userId, @PathVariable Long projectId) {
        try {
            Submission savedSubmission = submissionService.saveSubmission(submission, activityId, userId, projectId);
            return ResponseEntity.ok(savedSubmission);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getBy/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        Optional<Submission> submissionOpt = submissionService.findSubmissionById(id);
        return submissionOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Submission>> getAllSubmissions() {
        List<Submission> submissions = submissionService.findAllSubmissions();
        return ResponseEntity.ok(submissions);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<Submission>> getSubmissionsByActivity(@PathVariable Long activityId) {
        List<Submission> submissions = submissionService.findSubmissionsByActivity(activityId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Submission>> getSubmissionsByProject(@PathVariable Long projectId) {
        List<Submission> submissions = submissionService.findSubmissionsByProject(projectId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Submission>> getSubmissionsByUser(@PathVariable Long userId) {
        List<Submission> submissions = submissionService.findSubmissionsByUser(userId);
        return ResponseEntity.ok(submissions);
    }

    @PostMapping("/save/{userId}/{courseId}")
    public ResponseEntity<Submission> createSubmission(@RequestBody Submission submission, @PathVariable Long userId, @PathVariable Long courseId) {
        try {
            Submission savedSubmission = submissionService.addSubmission(submission, userId,courseId);
            return ResponseEntity.ok(savedSubmission);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
