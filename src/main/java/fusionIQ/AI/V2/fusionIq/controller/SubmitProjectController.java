package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.SubmitProject;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.EnrollmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.SubmitProjectRepo;
import fusionIQ.AI.V2.fusionIq.service.SubmitProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class SubmitProjectController {

    @Autowired
    private SubmitProjectService submitProjectService;

    @Autowired
    private SubmitProjectRepo submitProjectRepo;

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    @Autowired
    private CourseRepo courseRepo;

    @PostMapping("/saveSubmitProject/{userId}/{courseId}/{projectId}")
    public ResponseEntity<?> projectSubmit(
            @PathVariable long userId,
            @PathVariable long courseId,
            @PathVariable long projectId,
            @RequestParam String userAnswer,
            @RequestParam("file") MultipartFile submitProject) {
        try {
            SubmitProject submitProject1 = submitProjectService.submitProject(userId, courseId, projectId, userAnswer, submitProject);
            return ResponseEntity.ok(submitProject1);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the file.");
        }
    }


    @GetMapping("/allProjectSubmissions")
    public ResponseEntity<List<SubmitProject>> getAllProjectSubmission() {
        List<SubmitProject> submitProjects = submitProjectService.findAllSubmissionProjects();
        return ResponseEntity.ok(submitProjects);
    }

    @GetMapping("/getProjectSubmissionBy/{id}")
    public ResponseEntity<SubmitProject> getProjectSubmissionById(@PathVariable Long id) {
        Optional<SubmitProject> submitProject = submitProjectService.findProjectSubmissionById(id);
        return submitProject.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getSubmitProjectByCourse/{courseId}")
    public ResponseEntity<List<SubmitProject>> getSubmitProjectByCourseId(@PathVariable Long courseId) {
        List<SubmitProject> submitProjects = submitProjectService.getSubmitProjectByCourseId(courseId);
        return ResponseEntity.ok(submitProjects);
    }

    @GetMapping("/getSubmitProjectByUser/{userId}")
    public ResponseEntity<List<SubmitProject>> getSubmitProjectByUserId(@PathVariable Long userId) {
        List<SubmitProject> submitProjects = submitProjectService.getSubmitProjectByUserId(userId);
        return ResponseEntity.ok(submitProjects);
    }

    @DeleteMapping("/deleteSubmissionProject/{id}")
    public void deleteSubmitProject(@PathVariable Long id) {
        submitProjectService.deleteSubmitProject(id);
    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<SubmitProject> submitProject(
            @PathVariable Long courseId,
            @RequestParam Long userId,
            @RequestParam Long projectId,
            @RequestParam MultipartFile submitProjectFile,
            @RequestParam String userAnswer) throws IOException {

        SubmitProject submitProject = submitProjectService.submitProject(courseId, userId, projectId, submitProjectFile.getBytes(), userAnswer);
        return ResponseEntity.status(HttpStatus.CREATED).body(submitProject);
    }

    @GetMapping("/course/{courseId}/monthlyStats")
    public ResponseEntity<Map<String, Object>> getMonthlyStats(@PathVariable Long courseId) {
        Optional<Course> courseOptional = courseRepo.findById(courseId);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Course course = courseOptional.get();
        LocalDateTime createdAt = course.getCreatedAt();
        int startYear = createdAt.getYear();
        int currentYear = LocalDateTime.now().getYear();

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> monthlyData = new ArrayList<>();

        for (int year = startYear; year <= currentYear; year++) {
            List<Object[]> enrollmentsData = enrollmentRepo.countEnrollmentsByCourseIdAndMonth(courseId, year);
            List<Object[]> submissionsData = submitProjectRepo.countUniqueUsersByCourseIdAndMonth(courseId, year);

            Map<Integer, Long> enrollmentsByMonth = mapMonthData(enrollmentsData);
            Map<Integer, Long> submissionsByMonth = mapMonthData(submissionsData);

            for (int month = 1; month <= 12; month++) {
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("year", year);
                monthData.put("month", month);
                monthData.put("totalEnrollers", enrollmentsByMonth.getOrDefault(month, 0L));
                monthData.put("submittedUsers", submissionsByMonth.getOrDefault(month, 0L));
                monthlyData.add(monthData);
            }
        }

        response.put("monthlyData", monthlyData);
        return ResponseEntity.ok(response);
    }

    private Map<Integer, Long> mapMonthData(List<Object[]> data) {
        Map<Integer, Long> monthData = new HashMap<>();
        for (Object[] record : data) {
            int month = (Integer) record[0];
            long count = (Long) record[1];
            monthData.put(month, count);
        }
        return monthData;
    }

    @PostMapping("/submitProjectByStudentId")
    public ResponseEntity<String> submitProjectByStudent(@RequestParam Long studentId,
                                                @RequestParam Long courseId,
                                                @RequestParam Long projectId,
                                                @RequestParam("file") MultipartFile submitProject,
                                                @RequestParam(required = false) String userAnswer) {
        try {
            submitProjectService.submitProjectByStudent(studentId, courseId, projectId, submitProject, userAnswer);
            return ResponseEntity.ok("Project submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit project: " + e.getMessage());
        }
    }

    @PostMapping("/submitProjectByMock")
    public ResponseEntity<SubmitProject> submitProjectByMock(
            @RequestParam Long studentId,
            @RequestParam Long projectId,
            @RequestParam Long mockId,
            @RequestParam("file") MultipartFile submitProject,
            @RequestParam(required = false) String userAnswer) throws IOException {

        SubmitProject newSubmission = submitProjectService.submitProjectByMock(studentId, projectId, mockId, submitProject, userAnswer);
        return ResponseEntity.ok(newSubmission);
    }


    @GetMapping("/pending-feedback/{teacherId}")
    public ResponseEntity<List<SubmitProject>> getSubmittedProjectsWithoutFeedback(@PathVariable Long teacherId) {
        List<SubmitProject> submissions = submitProjectService.getSubmittedProjectsWithoutFeedback(teacherId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/byMockAndStudent")
    public ResponseEntity<List<SubmitProject>> getProjectsByMockAndStudent(@RequestParam Long mockId, @RequestParam Long studentId) {
        List<SubmitProject> projects = submitProjectService.getProjectsByMockAndStudent(mockId, studentId);
        return ResponseEntity.ok(projects);
    }
}
