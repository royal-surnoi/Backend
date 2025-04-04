package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/teacherFeedback")
public class TeacherFeedbackController {

    @Autowired
    private TeacherFeedbackService teacherFeedbackService;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private ProjectService projectService;

    @Autowired
    LessonModuleService lessonModuleService;


    @Autowired
    private LessonService lessonService;

    @GetMapping("/getAll")
    public List<TeacherFeedback> getAllFeedback() {
        return teacherFeedbackService.getAllFeedback();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TeacherFeedback> getFeedbackById(@PathVariable Long id) {
        return teacherFeedbackService.getFeedbackById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<TeacherFeedback> updateFeedback(
            @PathVariable Long id,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long quizId,
            @RequestParam(required = false) Long assignmentId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String feedback,
            @RequestParam(required = false) String grade) {

        Optional<TeacherFeedback> existingFeedbackOpt = teacherFeedbackService.getFeedbackById(id);

        if (!existingFeedbackOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Return 404 Not Found
        }

        TeacherFeedback existingFeedback = existingFeedbackOpt.get();

        // Update feedback fields as before

        TeacherFeedback updatedFeedback = teacherFeedbackService.updateFeedback(id, existingFeedback);
        return ResponseEntity.ok(updatedFeedback);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        try {
            teacherFeedbackService.deleteFeedback(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/gradesByUser/{userId}")
    public List<Map<String, String>> getGradesByUserId(@PathVariable Long userId) {
        return teacherFeedbackService.getGradesByUserId(userId);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TeacherFeedback>> getFeedbackByStudentId(@PathVariable Long studentId) {
        List<TeacherFeedback> feedbackList = teacherFeedbackService.getFeedbackByStudentId(studentId);
        if (feedbackList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(feedbackList);
    }

    @PostMapping("/{teacherId}/students/{studentId}/assignments/{assignmentId}")
    public ResponseEntity<TeacherFeedback> createFeedback(
            @PathVariable Long teacherId,
            @PathVariable Long studentId,
            @PathVariable Long assignmentId,
            @RequestBody TeacherFeedback feedbackDetails) {
        TeacherFeedback feedback = teacherFeedbackService.createFeedbackByAssignment(teacherId, studentId, assignmentId, feedbackDetails);
        return ResponseEntity.ok(feedback);
    }

    @PostMapping("/{teacherId}/students/{studentId}/quiz/{quizId}")
    public ResponseEntity<TeacherFeedback> createFeedbackByQuiz(
            @PathVariable Long teacherId,
            @PathVariable Long studentId,
            @PathVariable Long quizId,
            @RequestBody TeacherFeedback feedbackDetails) {
        TeacherFeedback feedback = teacherFeedbackService.createFeedbackByQuiz(teacherId, studentId, quizId, feedbackDetails);
        return ResponseEntity.ok(feedback);
    }

    @PostMapping("/{teacherId}/students/{studentId}/quizzes/{quizId}")
    public ResponseEntity<TeacherFeedback> createFeedbackByQuizzes(
            @PathVariable Long teacherId,
            @PathVariable Long studentId,
            @PathVariable Long quizId,
            @RequestBody TeacherFeedback feedbackDetails) {
        TeacherFeedback feedback = teacherFeedbackService.createFeedbackByQuizzes(teacherId, studentId, quizId, feedbackDetails);
        return ResponseEntity.ok(feedback);
    }



    @PostMapping("/create")
    public ResponseEntity<TeacherFeedback> createFeedback(
            @RequestParam Long teacherId,
            @RequestParam Long studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long quizId,
            @RequestParam(required = false) Long assignmentId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long lessonId,
            @RequestParam(required = false) Long lessonModuleId,
            @RequestParam String feedback,
            @RequestParam String grade) {

        if (teacherId == null || studentId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        User teacher = userService.getUserById(teacherId);
        User student = userService.getUserById(studentId);

        Course course = (courseId != null) ?
                courseService.getCourseById(courseId).orElse(null)
                : null;

        Quiz quiz = (quizId != null) ?
                quizService.getQuizsById(quizId).orElse(null)
                : null;

        Assignment assignment = (assignmentId != null) ?
                assignmentService.getAssignmentById(assignmentId).orElse(null)
                : null;

        Project project = (projectId != null) ?
                projectService.getProjectById(projectId).orElse(null)
                : null;

        LessonModule lessonModule = (lessonModuleId != null) ?
                lessonModuleService.getLessonModuleById(lessonModuleId).orElse(null)
                : null;
        Lesson lesson = (lessonId != null) ?
                lessonService.getLessonById(lessonId).orElse(null)
                : null;
        TeacherFeedback teacherFeedback = new TeacherFeedback();
        teacherFeedback.setTeacher(teacher);
        teacherFeedback.setStudent(student);
        teacherFeedback.setCourse(course);
        teacherFeedback.setQuiz(quiz);
        teacherFeedback.setAssignment(assignment);
        teacherFeedback.setProject(project);
        teacherFeedback.setLesson(lesson);
        teacherFeedback.setLessonModule(lessonModule);
        teacherFeedback.setFeedback(feedback);
        teacherFeedback.setGrade(grade);
        teacherFeedback.setCreatedAt(LocalDateTime.now());

        TeacherFeedback createdFeedback = teacherFeedbackService.createFeedback(teacherFeedback);
        return ResponseEntity.status(201).body(createdFeedback);
    }


    @GetMapping("/feedback")
    public ResponseEntity<List<TeacherFeedback>> getFeedback(
            @RequestParam Long teacherId,
            @RequestParam Long studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long quizId,
            @RequestParam(required = false) Long assignmentId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long lessonModuleId,
            @RequestParam(required = false) Long lessonId) {

        List<TeacherFeedback> feedbacks = teacherFeedbackService.getFeedback(
                teacherId, studentId, courseId, quizId, assignmentId, projectId, lessonModuleId, lessonId);

        return ResponseEntity.ok(feedbacks);
    }



}