package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherFeedbackService {

    @Autowired
    private TeacherFeedbackRepo teacherFeedbackRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AssignmentRepo assignmentRepo;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private LessonRepo lessonRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonModuleService lessonModuleService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubmitAssignmentRepo submitAssignmentRepo;

    @Autowired
    private SubmitProjectRepo submitProjectRepo;

    @Autowired
    private QuizProgressRepo quizProgressRepo;


    public List<TeacherFeedback> getAllFeedback() {
        return teacherFeedbackRepo.findAll();
    }

    public Optional<TeacherFeedback> getFeedbackById(Long id) {
        return teacherFeedbackRepo.findById(id);
    }

    public TeacherFeedback createFeedback(TeacherFeedback feedback) {
        return teacherFeedbackRepo.save(feedback);
    }

    public TeacherFeedback updateFeedback(Long id, TeacherFeedback feedback) {
        Optional<TeacherFeedback> optionalFeedback = teacherFeedbackRepo.findById(id);
        if (optionalFeedback.isPresent()) {
            TeacherFeedback existingFeedback = optionalFeedback.get();
            if (feedback.getTeacher() != null) existingFeedback.setTeacher(feedback.getTeacher());
            if (feedback.getStudent() != null) existingFeedback.setStudent(feedback.getStudent());
            if (feedback.getCourse() != null) existingFeedback.setCourse(feedback.getCourse());
            if (feedback.getQuiz() != null) existingFeedback.setQuiz(feedback.getQuiz());
            if (feedback.getAssignment() != null) existingFeedback.setAssignment(feedback.getAssignment());
            if (feedback.getProject() != null) existingFeedback.setProject(feedback.getProject());
            if (feedback.getFeedback() != null) existingFeedback.setFeedback(feedback.getFeedback());
            if (feedback.getGrade() != null) existingFeedback.setGrade(feedback.getGrade());

            return teacherFeedbackRepo.save(existingFeedback);
        } else {
            throw new IllegalArgumentException("Feedback not found");
        }
    }


    public void deleteFeedback(Long id) {
        teacherFeedbackRepo.deleteById(id);
    }

    public List<Map<String, String>> getGradesByUserId(Long userId) {
        List<TeacherFeedback> feedbackList = teacherFeedbackRepo.findByStudentId(userId);
        return feedbackList.stream().map(this::formatFeedback).collect(Collectors.toList());
    }

    private Map<String, String> formatFeedback(TeacherFeedback feedback) {
        Map<String, String> feedbackMap = new HashMap<>();

        if (feedback.getAssignment() != null) {
            feedbackMap.put("Title", feedback.getAssignment().getAssignmentTitle());
        } else if (feedback.getProject() != null) {
            feedbackMap.put("Title", feedback.getProject().getProjectTitle());
        } else if (feedback.getQuiz() != null) {
            feedbackMap.put("Title", feedback.getQuiz().getQuizName());
        } else {
            feedbackMap.put("Title", "Unknown");
        }

        feedbackMap.put("Grade", feedback.getGrade());
        feedbackMap.put("Feedback", feedback.getFeedback());
        feedbackMap.put("CreatedAt", feedback.getCreatedAt().toString());

        return feedbackMap;
    }

    public TeacherFeedback createFeedbackByAssignment(Long teacherId, Long studentId, Long assignmentId, TeacherFeedback feedbackDetails) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        User student = userRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Assignment assignment = assignmentRepo.findById(assignmentId).orElseThrow(() -> new RuntimeException("Assignment not found"));

        feedbackDetails.setTeacher(teacher);
        feedbackDetails.setStudent(student);
        feedbackDetails.setAssignment(assignment);
        feedbackDetails.setCreatedAt(LocalDateTime.now());

        return teacherFeedbackRepo.save(feedbackDetails);
    }


    public TeacherFeedback createFeedbackByQuiz(Long teacherId, Long studentId, Long quizId, TeacherFeedback feedbackDetails) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        User student = userRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));

        feedbackDetails.setTeacher(teacher);
        feedbackDetails.setStudent(student);
        feedbackDetails.setQuiz(quiz);
        feedbackDetails.setCreatedAt(LocalDateTime.now());

        System.out.println("Before saving: " + feedbackDetails);
        TeacherFeedback savedFeedback = teacherFeedbackRepo.save(feedbackDetails);
        System.out.println("After saving: " + savedFeedback);

        return savedFeedback;
    }


    public List<TeacherFeedback> getFeedbackByStudentId(Long studentId) {
        return teacherFeedbackRepo.findByStudentId(studentId);
    }


    public TeacherFeedback createFeedbackByQuizzes(Long teacherId, Long studentId, Long quizId, TeacherFeedback feedbackDetails) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        User student = userRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));

        feedbackDetails.setTeacher(teacher);
        feedbackDetails.setStudent(student);
        feedbackDetails.setQuiz(quiz);
        feedbackDetails.setCreatedAt(LocalDateTime.now());

        // Logging for debugging
        System.out.println("TeacherFeedback details before saving: " + feedbackDetails);

        return teacherFeedbackRepo.save(feedbackDetails);
    }


    public List<TeacherFeedback> getFeedback(Long teacherId, Long studentId, Long courseId, Long quizId, Long assignmentId, Long projectId, Long lessonId, Long lessonModuleId) {
        return teacherFeedbackRepo.findAllByParams(teacherId, studentId, courseId, quizId, assignmentId, projectId, lessonId, lessonModuleId);
    }


}
