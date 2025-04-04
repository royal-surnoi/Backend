package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.AssignmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonRepo;
import fusionIQ.AI.V2.fusionIq.repository.SubmitAssignmentRepo;
import fusionIQ.AI.V2.fusionIq.service.SubmitAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SubmitAssignmentController {

    @Autowired
    private SubmitAssignmentService submitAssignmentService;

    @Autowired
    SubmitAssignmentRepo submitAssignmentRepo;

    @Autowired
    LessonRepo lessonRepo;

    @Autowired
    AssignmentRepo assignmentRepo;

    @PostMapping("/saveSubmitAssignment/{userId}/{lessonId}")
    public ResponseEntity<SubmitAssignment> assignmentSubmit(
            @PathVariable long userId,
            @PathVariable long lessonId,
            @RequestParam("file") MultipartFile submitAssignment,
            @RequestParam String userAssignmentAnswer) {
        try {
            SubmitAssignment submitAssignment1 = submitAssignmentService.submitAssignment(userId, lessonId, submitAssignment, userAssignmentAnswer);
            return ResponseEntity.ok(submitAssignment1);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/allAssignmentSubmissions")
    public ResponseEntity<List<SubmitAssignment>> getAllSubmission() {
        List<SubmitAssignment> submitAssignments = submitAssignmentService.findAllSubmissions();
        return ResponseEntity.ok(submitAssignments);
    }

    @GetMapping("/getSubmissionBy/{id}")
    public ResponseEntity<SubmitAssignment> getSubmissionById(@PathVariable Long id) {
        Optional<SubmitAssignment> submitAssignment = submitAssignmentService.findSubmissionById(id);
        return submitAssignment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getSubmitAssignmentByLesson/{lessonId}")
    public ResponseEntity<List<SubmitAssignment>> getSubmitAssignmentByLessonId(@PathVariable Long lessonId) {
        List<SubmitAssignment> submitAssignments = submitAssignmentService.getSubmitAssignmentByLessonId(lessonId);
        return ResponseEntity.ok(submitAssignments);
    }

    @GetMapping("/getSubmitAssignmentByUser/{userId}")
    public ResponseEntity<List<SubmitAssignment>> getSubmitAssignmentByUserId(@PathVariable Long userId) {
        List<SubmitAssignment> submitAssignments1 = submitAssignmentService.getSubmitAssignmentByUserId(userId);
        return ResponseEntity.ok(submitAssignments1);
    }

    @DeleteMapping("/deleteSubmissionAssignment/{id}")
    public void deleteSubmitAssignment(@PathVariable Long id) {
        submitAssignmentService.deleteSubmitAssignment(id);
    }


    @PostMapping("/saveSubmitAssignmentByModule/{userId}/{lessonModuleId}/{assignmentId}")
    public ResponseEntity<SubmitAssignment> assignmentSubmitByModule(
            @PathVariable long userId,
            @PathVariable long lessonModuleId,
            @PathVariable long assignmentId,
            @RequestParam("file") MultipartFile submitAssignment,
            @RequestParam String userAssignmentAnswer) {
        try {
            SubmitAssignment submitAssignment1 = submitAssignmentService.submitAssignmentByModule(userId, lessonModuleId, assignmentId, submitAssignment, userAssignmentAnswer);
            return ResponseEntity.ok(submitAssignment1);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }

    }




    @PostMapping("/saveSubmitAssignmentByUser/{userId}/{assignmentId}")
    public ResponseEntity<SubmitAssignment> assignmentSubmitByUser(
            @PathVariable long userId,
            @PathVariable long assignmentId,
            @RequestParam("file") MultipartFile submitAssignment,
            @RequestParam String userAssignmentAnswer) {
        try {
            SubmitAssignment submitAssignment1 = submitAssignmentService.submitAssignmentByModule(userId, assignmentId, submitAssignment,userAssignmentAnswer);
            return ResponseEntity.ok(submitAssignment1);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    @GetMapping("/assignments/")
    public List<SubmitAssignment> getAssignmentsByUserIdAndLessonId(@RequestParam Long userId, @RequestParam Long lessonId) {
        return submitAssignmentService.getAssignmentsByUserIdAndLessonId(userId, lessonId);
    }

//    @GetMapping("/getSubmitAssignmentBy/{assignmentId}/{userId}")
//    public ResponseEntity<SubmitAssignment> getSubmitAssignmentByAssignmentIdAndUserId(
//            @PathVariable Long assignmentId,
//            @PathVariable Long userId) {
//        Optional<SubmitAssignment> submitAssignment = submitAssignmentRepo.findByAssignmentIdAndUserId(assignmentId, userId);
//        return submitAssignment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @GetMapping("/getSubmitAssignmentBy/{assignmentId}/{userId}")
    public ResponseEntity<?> getSubmitAssignmentByAssignmentIdAndUserId(
            @PathVariable Long assignmentId,
            @PathVariable Long userId) {
        List<Object[]> result = submitAssignmentRepo.findPartialByAssignmentIdAndUserId(assignmentId, userId);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Assuming there will be only one result (because assignmentId and userId are unique together)
        Object[] fields = result.get(0);
        Map<String, Object> response = new HashMap<>();
        response.put("submitAssignmentId", fields[0]);
        response.put("userAssignmentAnswer", fields[1]);
        response.put("userId", fields[2]);
        response.put("assignmentDocument", fields[3]);
        response.put("assignmentId", fields[4]);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/submitByStudentId")
    public ResponseEntity<String> submitAssignment(@RequestParam Long studentId,
                                                   @RequestParam Long courseId,
                                                   @RequestParam Long assignmentId,
                                                   @RequestParam("file") MultipartFile submitAssignment,
                                                   @RequestParam(required = false) String userAssignmentAnswer) {
        try {
            submitAssignmentService.submitAssignment(studentId, courseId, assignmentId, submitAssignment, userAssignmentAnswer);
            return ResponseEntity.ok("Assignment submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit assignment: " + e.getMessage());
        }
    }

    @PostMapping("/submitAssignmentByMock")
    public ResponseEntity<Map<String, Object>> submitAssignmentByMock(
            @RequestParam Long studentId,
            @RequestParam Long assignmentId,
            @RequestParam Long mockId,
            @RequestParam(required = false) MultipartFile submitAssignment,
            @RequestParam(required = false) String userAssignmentAnswer) throws IOException {

        SubmitAssignment newSubmission = submitAssignmentService.submitAssignmentByMock(studentId, assignmentId, mockId, submitAssignment, userAssignmentAnswer);

        // Return a simplified response without the file data
        Map<String, Object> response = new HashMap<>();
        response.put("id", newSubmission.getId());
        response.put("studentId", newSubmission.getStudent().getId());
        response.put("assignmentId", newSubmission.getAssignment().getId());
        response.put("submittedAt", newSubmission.getSubmittedAt());
        response.put("userAssignmentAnswer", newSubmission.getUserAssignmentAnswer());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/submitted-students/{assignmentId}")
    public ResponseEntity<List<Map<String, Object>>> getSubmittedAssignmentsAndStudentDetailsByAssignmentId(@PathVariable Long assignmentId) {
        List<Map<String, Object>> submittedAssignments = submitAssignmentService.getSubmittedAssignmentsAndStudentDetailsByAssignmentId(assignmentId);
        return ResponseEntity.ok(submittedAssignments);
    }


    @PostMapping("/submitAssignmentByUserAndLesson/{userId}/{assignmentId}/{lessonId}")
    public ResponseEntity<Map<String, Object>> submitAssignmentByUserAndLesson(
            @PathVariable Long userId,
            @PathVariable Long assignmentId,
            @PathVariable Long lessonId,
            @RequestParam("file") MultipartFile submitAssignment,
            @RequestParam String userAssignmentAnswer) {

        try {
            // Call the service to handle assignment submission
            SubmitAssignment submittedAssignment = submitAssignmentService.submitAssignmentByUserAndLesson(userId, assignmentId, lessonId, submitAssignment, userAssignmentAnswer);

            // Return a simplified response without the file data
            Map<String, Object> response = new HashMap<>();
            response.put("id", submittedAssignment.getId());
            response.put("userId", submittedAssignment.getUser().getId());
            response.put("assignmentId", submittedAssignment.getAssignment().getId());
            response.put("lessonId", lessonId);
            response.put("submittedAt", submittedAssignment.getSubmittedAt());
            response.put("userAssignmentAnswer", submittedAssignment.getUserAssignmentAnswer());
            response.put("isSubmitted", submittedAssignment.isSubmitted()); // Add isSubmitted status

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to submit assignment: " + e.getMessage()));
        }
    }

    @GetMapping("/assignmentDetails/{lessonId}")
    public ResponseEntity<Map<String, Object>> getLessonDetails(
            @PathVariable Long lessonId,
            @RequestParam Long userId) {

        Map<String, Object> lessonDetails = submitAssignmentService.getLessonDetails(lessonId, userId);
        return ResponseEntity.ok(lessonDetails);
    }

    @GetMapping("get/count")
    public long getSubmittedAssignmentCount(@RequestParam Long userId, @RequestParam Long assignmentId, @RequestParam Long lessonId) {
        return submitAssignmentService.getSubmittedAssignmentCount(userId, assignmentId, lessonId);
    }

    @GetMapping("/pendingFeedback/{teacherId}")
    public ResponseEntity<List<SubmitAssignment>> getSubmittedAssignmentsWithoutFeedback(@PathVariable Long teacherId) {
        List<SubmitAssignment> assignments = submitAssignmentService.getSubmittedAssignmentsWithoutFeedback(teacherId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/ByMockAndStudent")
    public ResponseEntity<List<SubmitAssignment>> getAssignmentsByMockAndStudent(@RequestParam Long mockId, @RequestParam Long studentId) {
        List<SubmitAssignment> assignments = submitAssignmentService.getAssignmentsByMockAndStudent(mockId, studentId);
        return ResponseEntity.ok(assignments);
    }
}