package fusionIQ.AI.V2.fusionIq.controller;
import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.AssignmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonRepo;
import fusionIQ.AI.V2.fusionIq.repository.SubmitAssignmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.AssignmentService;
import fusionIQ.AI.V2.fusionIq.service.SubmitAssignmentService;
import fusionIQ.AI.V2.fusionIq.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@RestController
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    AssignmentRepo assignmentRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    LessonRepo lessonRepo;

    @Autowired
    SubmitAssignmentService submitAssignmentService;

    @Autowired
    SubmitAssignmentRepo submitAssignmentRepo;

    @Autowired
    private UserService userService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByCourseId(@PathVariable Long courseId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId);
        if (assignments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<Assignment>> getAssignmentlessonsByLessonId(@PathVariable Long lessonId) {
        List<Assignment> assignments = assignmentService.getAssignmentlessonsByLessonId(lessonId);
        if (assignments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/saveLesson/{lessonId}/{courseId}")
    public ResponseEntity<Assignment> addAssignment(
            @PathVariable long lessonId,
            @PathVariable long courseId,
            @RequestParam String assignmentTitle,
            @RequestParam String assignmentTopicName,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam LocalDateTime reviewMeetDate,


            @RequestParam("document") MultipartFile assignmentDocument) {
        try {
            Assignment savedAssignment = assignmentService.addAssign(lessonId, courseId, assignmentTitle, assignmentTopicName, assignmentDocument, startDate, endDate, reviewMeetDate);
            return ResponseEntity.ok(savedAssignment);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/updateAssignment/{assignmentId}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable long assignmentId,
            @RequestParam String assignmentTitle,

            @RequestParam String assignmentTopicName,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam LocalDateTime reviewMeetDate,
            @RequestParam String assignmentDescription,
            @RequestParam Long maxScore,
            @RequestParam String assignmentAnswer,
            @RequestParam("document") MultipartFile assignmentDocument) {
        try {
            Assignment updatedAssignment = assignmentService.updateAssign(assignmentId, assignmentTitle, assignmentTopicName, assignmentDocument, startDate, endDate, reviewMeetDate, assignmentDescription, maxScore, assignmentAnswer);

            return ResponseEntity.ok(updatedAssignment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/deleteAssignment/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable long assignmentId) {
        try {
            assignmentService.deleteAssign(assignmentId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    @PostMapping("/add/{userId}/{lessonId}")
    public Assignment addNewAssignment(@PathVariable Long userId,
                                       @PathVariable Long lessonId,
                                       @RequestParam("file") MultipartFile assignmentDocument,
                                       @RequestParam String assignmentTitle,
                                       @RequestParam String assignmentTopicName) throws IOException {

        Optional<User> user = userRepo.findById(userId);
        Optional<Lesson> lesson = lessonRepo.findById(lessonId);

        if (user.isPresent() && lesson.isPresent()) {
            Assignment assignment = new Assignment();
            assignment.setAssignmentTitle(assignmentTitle);
            assignment.setAssignmentDocument(assignmentDocument.getBytes());


            assignment.setAssignmentTopicName(assignmentTopicName);


            return assignmentRepo.save(assignment);
        } else {
            throw new RuntimeException("userId or lessonId not found");
        }
    }

    @PostMapping("/sendAssignment/{userId}")
    public Assignment sendAssignment(@PathVariable Long userId,
                                     @RequestParam("file") MultipartFile assignmentDocument,
                                     @RequestParam String assignmentTitle,
                                     @RequestParam String assignmentTopicName,
                                     @RequestParam LocalDateTime startDate,
                                     @RequestParam LocalDateTime endDate,
                                     @RequestParam LocalDateTime reviewMeetDate) throws IOException {

        Optional<User> user = userRepo.findById(userId);

        if (user.isPresent()) {
            Assignment assignment = new Assignment();
            assignment.setAssignmentTitle(assignmentTitle);
            assignment.setAssignmentDocument(assignmentDocument.getBytes());
            assignment.setAssignmentTopicName(assignmentTopicName);
            assignment.setStartDate(startDate);
            assignment.setEndDate(endDate);
            assignment.setReviewMeetDate(reviewMeetDate);

            return assignmentRepo.save(assignment);
        } else {
            throw new RuntimeException("userId");
        }
    }


    @PostMapping("/saveNewAssignment")
    public ResponseEntity<Assignment> createNewAssignment(
            @RequestParam("assignmentTitle") String assignmentTitle,
            @RequestParam("assignmentTopicName") String assignmentTopicName,
            @RequestParam("lessonId") Long lessonId,
            @RequestParam("courseId") Long courseId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam("reviewMeetDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reviewMeetDate,
            @RequestParam("file") MultipartFile assignmentDocument,
            @RequestParam("teacherId") User teacherId) {

        Assignment createdAssignment = assignmentService.createNewAssignment(
                assignmentTitle, assignmentTopicName, lessonId, courseId, startDate, endDate, reviewMeetDate, assignmentDocument, teacherId);
        return ResponseEntity.ok(createdAssignment);
    }


    @GetMapping("/due-in-five-days")
    public List<Assignment> getAssignmentsDueInFiveDays() {
        return assignmentService.getAssignmentsDueInFiveDays();
    }


    @PostMapping("/add/{lessonId}")
    public Assignment addNewAssignment(
            @PathVariable Long lessonId,
            @RequestParam("file") MultipartFile assignmentDocument,
            @RequestParam String assignmentTitle,
            @RequestParam String assignmentTopicName) throws IOException {


        Optional<Lesson> lesson = lessonRepo.findById(lessonId);

        if (lesson.isPresent()) {
            Assignment assignment = new Assignment();
            assignment.setAssignmentTitle(assignmentTitle);
            assignment.setAssignmentDocument(assignmentDocument.getBytes());


            assignment.setAssignmentTopicName(assignmentTopicName);


            return assignmentRepo.save(assignment);
        } else {
            throw new RuntimeException("userId or lessonId not found");
        }
    }

    @GetMapping("/progress/{lessonId}")
    public double getProgress(@PathVariable Long lessonId, @RequestParam Long userId) {
        List<Assignment> totalAssignments = assignmentRepo.findByLessonId(lessonId);
        List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findByLessonIdAndUserId(lessonId, userId);

        if (totalAssignments.isEmpty()) {
            return 0;
        }

        return (double) submittedAssignments.size() / totalAssignments.size() * 100;
    }

    @GetMapping("/progressByModule/{lessonModuleId}")
    public double getProgressByModule(@PathVariable Long lessonModuleId, @RequestParam Long userId) {
        List<Assignment> totalAssignments = assignmentRepo.findByLessonModuleId(lessonModuleId);
        List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findByLessonModuleIdAndUserId(lessonModuleId, userId);

        if (totalAssignments.isEmpty()) {
            return 0;
        }

        return (double) submittedAssignments.size() / totalAssignments.size() * 100;
    }

    @GetMapping("/submittedByTotalModule/{lessonModuleId}")
    public String getSubmittedByTotalAssignmentsByModule(@PathVariable Long lessonModuleId, @RequestParam Long userId) {
        List<Assignment> totalAssignments = assignmentRepo.findByLessonModuleId(lessonModuleId);
        List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findByLessonModuleIdAndUserId(lessonModuleId, userId);

        int totalAssignmentsCount = totalAssignments.size();
        int submittedAssignmentsCount = submittedAssignments.size();

        return submittedAssignmentsCount + "/" + totalAssignmentsCount;
    }

    @GetMapping("/submittedByTotalCourse/{courseId}")
    public String getSubmittedByTotalAssignmentsByCourse(@PathVariable Long courseId, @RequestParam Long userId) {
        List<Assignment> totalAssignments = assignmentRepo.findByCourseId(courseId);
        List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findByCourseIdAndUserId(courseId, userId);

        int totalAssignmentsCount = totalAssignments.size();
        int submittedAssignmentsCount = submittedAssignments.size();

        return submittedAssignmentsCount + "/" + totalAssignmentsCount;
    }

    @PostMapping("/byLessonAndModule/{lessonId}/{lessonModuleId}")
    public List<Assignment> getAssignmentsByLessonIdAndLessonModuleId(@PathVariable Long lessonId,
                                                                      @PathVariable Long lessonModuleId,
                                                                      @RequestParam String assignmentTitle,
                                                                      @RequestParam String assignmentTopicName,
                                                                      @RequestParam LocalDateTime startDate,
                                                                      @RequestParam LocalDateTime endDate,
                                                                      @RequestParam LocalDateTime reviewMeetDate,
                                                                      @RequestParam String assignmentDescription,
                                                                      @RequestParam Long maxScore,
                                                                      @RequestParam String assignmentAnswer,
                                                                      @RequestParam("file") MultipartFile assignmentDocument) {
        return assignmentService.getAssignmentsByLessonIdAndLessonModuleId(lessonId, lessonModuleId, assignmentTitle, assignmentTopicName, startDate, endDate, reviewMeetDate, assignmentDescription, maxScore, assignmentAnswer, assignmentDocument);
    }

    @GetMapping("/getAssignmentByUser/{userId}")
    public List<Assignment> getAssignmentsByUserId(@PathVariable Long userId) {
        return assignmentService.getAssignmentsByUserId(userId);
    }

    @GetMapping("/upcomingAssignment/{userId}")
    public List<Assignment> getUpcomingAssignments(@PathVariable Long userId) {
        return assignmentService.getUpcomingAssignmentsByUserId(userId);
    }

    @GetMapping("/assignmentSubmissions/{userId}")
    public List<Map<String, Object>> getAssignmentTitlesAndSubmissionDates(@PathVariable Long userId) {
        return submitAssignmentService.getAssignmentsByUserId(userId).stream()
                .map(sa -> {
                    Map<String, Object> map = new HashMap<>();
                    if (sa.getAssignment() != null) {
                        map.put("assignmentTitle", sa.getAssignment().getAssignmentTitle());
                    } else {
                        map.put("assignmentTitle", "No Assignment Title");
                    }
                    map.put("submittedAt", sa.getSubmittedAt());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/byLessonModule/{lessonModuleId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByLessonModuleId(@PathVariable Long lessonModuleId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByLessonModuleId(lessonModuleId);
        return ResponseEntity.ok(assignments);
    }


    @PostMapping("/createByTeacher/{teacherId}/{studentId}")
    public Assignment createAssignmentByTeacher(
            @PathVariable long teacherId,
            @PathVariable long studentId,
            @RequestParam String assignmentTitle,
            @RequestParam String assignmentTopicName,
            @RequestParam String assignmentDescription,
            @RequestParam Long maxScore,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam LocalDateTime reviewMeetDate,
            @RequestParam("assignmentDocument") MultipartFile assignmentDocument) {

        return assignmentService.createAssignmentByTeacher(teacherId, studentId, assignmentTitle, assignmentTopicName, assignmentDescription, maxScore, startDate, endDate, reviewMeetDate, assignmentDocument);
    }

    @PostMapping("/createByTeacher/{teacherId}/{studentId}/{courseId}")
    public Assignment createAssignmentByTeacherByCourse(
            @PathVariable long teacherId,
            @PathVariable long studentId,
            @PathVariable long courseId,
            @RequestParam String assignmentTitle,
            @RequestParam String assignmentTopicName,
            @RequestParam String assignmentDescription,
            @RequestParam Long maxScore,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam LocalDateTime reviewMeetDate,
            @RequestParam("assignmentDocument") MultipartFile assignmentDocument) {

        return assignmentService.createAssignmentByTeacherByCourse(teacherId, studentId, courseId, assignmentTitle, assignmentTopicName, assignmentDescription, maxScore, startDate, endDate, reviewMeetDate, assignmentDocument);
    }


    @GetMapping("/{assignmentId}/submittedUsers")
    public ResponseEntity<List<User>> getUsersWhoSubmittedAssignment(@PathVariable Long assignmentId) {
        try {
            List<User> users = assignmentService.getUsersWhoSubmittedAssignment(assignmentId);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/createByTeacherCourse/{teacherId}/{courseId}")
    public Assignment createAssignmentByTeacherByCourseId(
            @PathVariable long teacherId,
            @PathVariable long courseId,
            @RequestParam String assignmentTitle,
            @RequestParam String assignmentTopicName,
            @RequestParam String assignmentDescription,
            @RequestParam Long maxScore,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam LocalDateTime reviewMeetDate,
            @RequestParam("assignmentDocument") MultipartFile assignmentDocument) {

        return assignmentService.createAssignmentByTeacherByCourseId(teacherId, courseId, assignmentTitle, assignmentTopicName, assignmentDescription, maxScore, startDate, endDate, reviewMeetDate, assignmentDocument);
    }

    @GetMapping("/byTeacher")
    public ResponseEntity<List<Assignment>> getAssignmentsByTeacherId(@RequestParam Long teacherId) {
        List<Assignment> assignments = assignmentRepo.findByTeacherId(teacherId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/byTeacherId")
    public ResponseEntity<List<Assignment>> getAssignmentsByTeacher(@RequestParam Long teacherId) {
        List<Assignment> assignments = assignmentRepo.findByTeacherId(teacherId);
        // To ensure lazy-loaded fields are fetched, we can iterate and access them
        assignments.forEach(assignment -> {
            if (assignment.getCourse() != null) {
                assignment.getCourse().getCourseTitle();
            }
        });
        return ResponseEntity.ok(assignments);
    }


    @PutMapping("/{teacherId}/{id}/updateAssignmentDetails")
    public Assignment updateAssignmentDetailsByIdAndTeacherId(
            @PathVariable Long teacherId,
            @PathVariable Long id,
            @RequestParam(required = false) String assignmentTitle,
            @RequestParam(required = false) String assignmentTopicName,
            @RequestParam(required = false) String assignmentDescription,
            @RequestParam(required = false) Long maxScore,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) LocalDateTime reviewMeetDate,
            @RequestParam(required = false) String assignmentAnswer,
            @RequestParam(required = false) byte[] assignmentDocument
    ) {
        return assignmentService.updateAssignmentFields(id, teacherId, assignmentTitle, assignmentTopicName, assignmentDescription,
                maxScore, startDate, endDate, reviewMeetDate, assignmentAnswer, assignmentDocument);
    }


    @GetMapping("/byCourseAndTeacher/{courseId}/{teacherId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByCourseIdAndTeacherId(
            @PathVariable Long courseId,
            @PathVariable Long teacherId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByCourseIdAndTeacherId(courseId, teacherId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/byCourseAndStudent/{courseId}/{studentId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByCourseIdAndStudentId(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByCourseIdAndStudentId(courseId, studentId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/getAssignment/{id}")
    public ResponseEntity<Object> getAssignmentById(@PathVariable Long id) {
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);
        if (assignment.isPresent()) {
            return new ResponseEntity<>(assignment.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Assignment not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/progress/course/{courseId}/user/{userId}")
    public ResponseEntity<Map<String, Double>> getProgressPercentageByCourseAndUser(
            @PathVariable Long courseId,
            @PathVariable Long userId) {
        Map<String, Double> progressPercentage = assignmentService.calculateProgressPercentage(courseId, userId);
        return ResponseEntity.ok(progressPercentage);
    }

    @PostMapping("/createAssignment/{userId}/{lessonId}")
    public ResponseEntity<Assignment> createAssignment(
            @PathVariable Long userId,
            @PathVariable Long lessonId,
            @RequestParam("file") MultipartFile assignmentDocument,
            @RequestParam String assignmentTitle,
            @RequestParam String assignmentTopicName) {

        try {
            Optional<User> user = userRepo.findById(userId);
            Optional<Lesson> lesson = lessonRepo.findById(lessonId);

            if (user.isPresent() && lesson.isPresent()) {
                Assignment assignment = new Assignment();
                assignment.setAssignmentTitle(assignmentTitle);
                assignment.setAssignmentTopicName(assignmentTopicName);
                assignment.setAssignmentDocument(assignmentDocument.getBytes());

                // Set the associated user and lesson
                assignment.setUser(user.get());
                assignment.setLesson(lesson.get());

                Assignment savedAssignment = assignmentRepo.save(assignment);
                return ResponseEntity.ok(savedAssignment);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }

    }

    @GetMapping("/byUserAndCourse/{userId}/{courseId}")
    public List<Assignment> getAssignmentsByUserAndCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId) {
        return assignmentService.getAssignmentsByUserAndCourse(userId, courseId);
    }

    @GetMapping("/progressByCourseAndUser")
    public ResponseEntity<Map<Long, Double>> getProgressByCourse(@RequestParam Long courseId, @RequestParam Long userId) {
        Map<Long, Double> progressMap = assignmentService.getProgressByModulesInCourse(courseId, userId);
        return ResponseEntity.ok(progressMap);
    }

    @GetMapping("/assignment-stats/byCourse")
    public String getAssignmentStatistics(@RequestParam Long courseId, @RequestParam Long userId) {
        return assignmentService.getAssignmentStatistics(courseId, userId);
    }

    @GetMapping("/assignment-submission-percentage")
    public double getSubmissionPercentage(@RequestParam Long courseId, @RequestParam Long userId) {
        return assignmentService.getSubmissionPercentage(courseId, userId);
    }

    @GetMapping("/progress")
    public double getAssignmentProgress(@RequestParam Long courseId, @RequestParam Long userId) {
        return assignmentService.calculateAssignmentProgress(courseId, userId);
    }

    @GetMapping("/progress/course/{courseId}")
    public Map<String, Double> getLessonAndModuleProgressByCourse(@PathVariable Long courseId) {
        return assignmentService.getLessonAndModuleProgressByCourse(courseId);
    }

    @GetMapping("/{assignmentId}/students/{studentId}/grade")
    public ResponseEntity<String> getGrade(@PathVariable Long assignmentId, @PathVariable Long studentId) {
        Optional<String> grade = assignmentService.getAssignmentGrade(studentId, assignmentId);
        return grade.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/progressByStudentId")
    public ResponseEntity<Map<String, Object>> getProgressOfIndividual(@RequestParam Long studentId,
                                                                       @RequestParam Long courseId) {
        Map<String, Object> progress = assignmentService.calculateProgressOfIndividual(studentId, courseId);
        return new ResponseEntity<>(progress, HttpStatus.OK);
    }

    @PostMapping("/submitNewByStudentId")
    public ResponseEntity<String> submitNewAssignment(@RequestParam Long studentId,
                                                      @RequestParam Long courseId,
                                                      @RequestParam Long assignmentId,
                                                      @RequestParam("file") MultipartFile submitAssignment,
                                                      @RequestParam(required = false) String assignmentAnswer) {
        try {
            // Check if the student has already submitted the assignment
            if (assignmentService.hasStudentSubmittedAssignment(studentId, assignmentId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Assignment already submitted");
            }

            assignmentService.submitNewAssignment(studentId, courseId, assignmentId, submitAssignment, assignmentAnswer);
            return ResponseEntity.ok("Assignment submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit assignment: " + e.getMessage());
        }
    }

    @GetMapping("/{assignmentId}/students/{studentId}/grades")
    public ResponseEntity<String> getGrades(@PathVariable Long assignmentId, @PathVariable Long studentId) {
        Optional<String> grade = assignmentService.getAssignmentGrades(studentId, assignmentId);
        return grade.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/submitAssignment")
    public ResponseEntity<SubmitAssignment> submitAssignment(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("userId") Long userId,
            @RequestParam(required = false) Long courseId,  // Set courseId as optional
            @RequestParam("file") MultipartFile submitAssignmentDocument,
            @RequestParam String assignmentAnswer) {

        // Check if the user has already submitted the assignment without considering courseId
        if (assignmentService.hasUserSubmittedAssignment(userId, assignmentId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        // Call the service to handle assignment submission
        SubmitAssignment submittedAssignment = assignmentService.submitAssignment(assignmentId, userId, courseId, submitAssignmentDocument, assignmentAnswer);
        return ResponseEntity.ok(submittedAssignment);
    }

    @GetMapping("/progress/details/{lessonId}")
    public ResponseEntity<Map<String, Object>> getProgressDetails(
            @PathVariable Long lessonId,
            @RequestParam Long userId) {
        Map<String, Object> progressDetails = assignmentService.getProgressDetails(lessonId, userId);
        return ResponseEntity.ok(progressDetails);
    }

    @GetMapping("/get/assignments/teacher/{teacherId}/student/{studentId}")
    public List<Map<String, Object>> getAssignmentsAndStudentsByTeacherIdAndStudentId(@PathVariable long teacherId, @PathVariable long studentId) {
        return assignmentService.getAssignmentsAndStudentsWithSubmitAssignmentsByTeacherIdAndStudentId(teacherId, studentId);
    }

    @GetMapping("/byLessonAndModule")
    public List<Assignment> getAssignmentsByLessonAndModule(@RequestParam Long lessonId, @RequestParam Long lessonModuleId) {
        return assignmentService.getAssignmentsByLessonAndModule(lessonId, lessonModuleId);
    }

    @GetMapping("/studentCount")
    public ResponseEntity<Integer> getStudentCount(
            @RequestParam long assignmentId,
            @RequestParam long teacherId) {

        int studentCount = assignmentService.getStudentCountByAssignmentAndTeacher(assignmentId, teacherId);
        return ResponseEntity.ok(studentCount);
    }

    @PutMapping("/updateAssignmentMultiple/{assignmentId}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable long assignmentId,
            @RequestParam(required = false) String assignmentTitle,
            @RequestParam(required = false) String assignmentTopicName,
            @RequestParam(required = false) String assignmentDescription,
            @RequestParam(required = false) Long maxScore,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) LocalDateTime reviewMeetDate,
            @RequestParam(required = false) MultipartFile assignmentDocument) {
        try {
            Assignment updatedAssignment = assignmentService.updateAssignments(
                    assignmentId, assignmentTitle, assignmentTopicName, assignmentDescription, maxScore,
                    startDate, endDate, reviewMeetDate, assignmentDocument);
            return ResponseEntity.ok(updatedAssignment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/courseAndStudentsByAssignmentId")
    public ResponseEntity<Map<String, Object>> getCourseAndStudentDetailsByAssignmentId(
            @RequestParam long assignmentId) {
        try {
            Map<String, Object> response = assignmentService.getCourseAndStudentDetailsByAssignmentId(assignmentId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/submissionSummary")
    public String getAssignmentSubmissionSummary(
            @RequestParam Long courseId,
            @RequestParam Long userId) {
        return assignmentService.getAssignmentSubmissionSummary(courseId, userId);
    }

    @PostMapping("/createMultipleAssignment")
    public ResponseEntity<Assignment> createAssignment(
            @RequestParam long teacherId,
            @RequestParam long courseId,
            @RequestParam String studentIds, // Accept as String
            @RequestParam String assignmentTitle,
            @RequestParam String assignmentTopicName,
            @RequestParam String assignmentDescription,
            @RequestParam Long maxScore,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam LocalDateTime reviewMeetDate,
            @RequestParam MultipartFile assignmentDocument) {


        List<Long> studentIdList = Arrays.stream(studentIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());


        Assignment assignment = assignmentService.createAssignment(
                teacherId, courseId, studentIdList, assignmentTitle, assignmentTopicName, assignmentDescription,
                maxScore, startDate, endDate, reviewMeetDate, assignmentDocument);

        return ResponseEntity.ok(assignment);
    }

    @PostMapping("/saveLessons/{lessonId}/{courseId}")
    public ResponseEntity<Assignment> addAssignment(
            @PathVariable long lessonId,
            @PathVariable long courseId,
            @RequestParam(required = false) Long lessonModuleId, // lessonModuleId is optional
            @RequestParam String assignmentTitle,
            @RequestParam String assignmentTopicName,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam LocalDateTime reviewMeetDate,
            @RequestParam("document") MultipartFile assignmentDocument) {
        try {
            Assignment savedAssignment = assignmentService.addNewAssign(
                    lessonId, courseId, lessonModuleId, assignmentTitle, assignmentTopicName,
                    assignmentDocument, startDate, endDate, reviewMeetDate
            );
            return ResponseEntity.ok(savedAssignment);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/teacher-items")
    public ResponseEntity<?> getCourseDetailsByTeacherId(@RequestParam Long teacherId) {
        if (teacherId == null) {
            return ResponseEntity.badRequest().body("Teacher ID must not be null");
        }

        try {
            List<Map<String, Object>> courseDetails = userService.getCourseDetailsByTeacherId(teacherId);
            if (courseDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No courses found for Teacher ID: " + teacherId);
            }
            return ResponseEntity.ok(courseDetails);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace(); // For debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred");
        }
    }

    @PostMapping("/createAssignmentByMock")
    public ResponseEntity<String> createAssignmentByMock(
            @RequestParam String assignmentTitle,
            @RequestParam String assignmentTopicName,
            @RequestParam String assignmentDescription,
            @RequestParam("file") MultipartFile assignmentDocument,
            @RequestParam Long mockId,
            @RequestParam Long teacherId) throws IOException {

        String result = assignmentService.createAssignmentByMock(
                assignmentTitle, assignmentTopicName, assignmentDescription,
                assignmentDocument, mockId, teacherId
        );

        if (result.equals("Assignment created successfully.")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/details/{lessonId}")
    public ResponseEntity<Map<String, Object>> getAssignmentDetails(
            @PathVariable Long lessonId,
            @RequestParam Long userId) {
        Map<String, Object> assignmentDetails = assignmentService.getAssignmentDetails(lessonId, userId);
        return ResponseEntity.ok(assignmentDetails);
    }

    @GetMapping("/assignmentsByTeacher/{teacherId}")
    public ResponseEntity<List<Map<String, Object>>> getAssignmentsByTeacher(@PathVariable long teacherId) {

        List<Map<String, Object>> assignmentDetails = assignmentService.getAssignmentsByTeacherAndCourse(teacherId);

        if (assignmentDetails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if no data is found
        }


        return new ResponseEntity<>(assignmentDetails, HttpStatus.OK);
    }

    @GetMapping("/assignmentsDetails/{lessonId}")
    public ResponseEntity<Map<String, Object>> getAssignmentsDetails(
            @PathVariable Long lessonId,
            @RequestParam Long userId) {


        Map<String, Object> assignmentDetails = assignmentService.getAssignmentsDetails(lessonId, userId);
        return ResponseEntity.ok(assignmentDetails);
    }

    @GetMapping("/getSubmit/progress")
    public Map<String, Object> getSubAssignmentProgress(@RequestParam Long courseId, @RequestParam Long userId) {
        return assignmentService.getSubAssignmentProgress(courseId, userId);
    }

    @GetMapping("/upcoming/{userId}")
    public ResponseEntity<List<Assignment>> getUpcomingAssignment(@PathVariable Long userId) {
        List<Assignment> assignments = assignmentService.getUpcomingAssignmentByUserId(userId);
        if (assignments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Assignment>> getUpcomingAssignmentsForUser(@RequestParam Long userId) {
        List<Assignment> assignments = assignmentService.getUpcomingAssignmentsForUser(userId);
        return ResponseEntity.ok(assignments);
    }
}