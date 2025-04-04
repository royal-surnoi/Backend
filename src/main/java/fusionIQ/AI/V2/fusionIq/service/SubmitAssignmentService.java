package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubmitAssignmentService {

    @Autowired
    private SubmitAssignmentRepo submitAssignmentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LessonRepo lessonRepo;

    @Autowired
    LessonModuleRepo lessonModuleRepo;

    @Autowired
    AssignmentRepo assignmentRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private MockTestInterviewRepository mockTestInterviewRepo;

    @Autowired
    private TeacherFeedbackRepo teacherFeedbackRepo;

    public SubmitAssignment submitAssignment(Long userId, Long lessonId, MultipartFile submitAssignment,String userAssignmentAnswer) throws IOException {
        Optional<User> userOpt = userRepo.findById(userId);
        Optional<Lesson> lessonOptional = lessonRepo.findById(lessonId);

        if (userOpt.isPresent() && lessonOptional.isPresent()) {
            SubmitAssignment submitAssignment1 = new SubmitAssignment();
            submitAssignment1.setUser(userOpt.get());
            submitAssignment1.setLesson(lessonOptional.get());
            submitAssignment1.setSubmitAssignment(submitAssignment.getBytes());
            submitAssignment1.setUserAssignmentAnswer(userAssignmentAnswer);

            return submitAssignmentRepo.save(submitAssignment1);
        } else {
            throw new IllegalArgumentException("User or Lesson not found");
        }
    }

    public List<SubmitAssignment> findAllSubmissions() {
        return submitAssignmentRepo.findAll();
    }

    public Optional<SubmitAssignment> findSubmissionById(Long id) {
        return submitAssignmentRepo.findById(id);
    }

    public List<SubmitAssignment> getSubmitAssignmentByLessonId(Long lessonId) {
        return submitAssignmentRepo.findByLessonId(lessonId);
    }

    public List<SubmitAssignment> getSubmitAssignmentByUserId(Long userId) {
        return submitAssignmentRepo.findByUserId(userId);
    }

    public void deleteSubmitAssignment(Long id) {
        Optional<SubmitAssignment> submitAssignment = submitAssignmentRepo.findById(id);
        if (submitAssignment.isPresent()) {
            submitAssignmentRepo.deleteById(id);
        } else {
            throw new RuntimeException("SubmitAssignment not found with id " + id);
        }
    }

    public SubmitAssignment submitAssignmentByModule(Long userId, Long lessonModuleId, Long assignmentId,MultipartFile submitAssignment,String userAssignmentAnswer) throws IOException {
        Optional<User> userOpt = userRepo.findById(userId);

        Optional<LessonModule> lessonModuleOptional = lessonModuleRepo.findById(lessonModuleId);
        Optional<Assignment> assignmentOptional = assignmentRepo.findById(assignmentId);

        if (userOpt.isPresent() && lessonModuleOptional.isPresent() && assignmentOptional.isPresent()) {
            SubmitAssignment submitAssignment1 = new SubmitAssignment();
            submitAssignment1.setUser(userOpt.get());

            submitAssignment1.setLessonModule(lessonModuleOptional.get());
            submitAssignment1.setAssignment(assignmentOptional.get());
            submitAssignment1.setSubmitAssignment(submitAssignment.getBytes());
            submitAssignment1.setUserAssignmentAnswer(userAssignmentAnswer);
            return submitAssignmentRepo.save(submitAssignment1);
        } else {
            throw new IllegalArgumentException("User or Assignment or LessonModule or Assignment not found");
        }
    }

    public List<SubmitAssignment> getAssignmentsByUserId(Long userId) {
        return submitAssignmentRepo.findAssignmentsByUserId(userId);
    }

    public SubmitAssignment submitAssignmentByModule(long userId, long assignmentId, MultipartFile file,String userAssignmentAnswer) throws IOException {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        SubmitAssignment submitAssignment = new SubmitAssignment();
        submitAssignment.setUser(user);
        submitAssignment.setAssignment(assignment);
        submitAssignment.setSubmitAssignment(file.getBytes());
        submitAssignment.setSubmittedAt(LocalDateTime.now());
        submitAssignment.setUserAssignmentAnswer(userAssignmentAnswer);

        return submitAssignmentRepo.save(submitAssignment);
    }
    public List<SubmitAssignment> getAssignmentsByUserIdAndLessonId(Long userId, Long lessonId) {
        return submitAssignmentRepo.findByUserIdAndLessonId(userId, lessonId);
    }

    public void submitAssignment(Long studentId, Long courseId, Long assignmentId, MultipartFile submitAssignment, String userAssignmentAnswer) throws IOException {
        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        SubmitAssignment newSubmission = new SubmitAssignment();
        newSubmission.setSubmitAssignment(submitAssignment.getBytes());
        newSubmission.setUserAssignmentAnswer(userAssignmentAnswer);
        newSubmission.setStudent(student);
        newSubmission.setCourse(course);
        newSubmission.setAssignment(assignment);
        newSubmission.setSubmittedAt(LocalDateTime.now());

        submitAssignmentRepo.save(newSubmission);
    }

    public Optional<SubmitAssignment> getSubmitAssignmentByUserIdAndAssignmentIdAndMockId(Long userId, Long assignmentId, Long mockId) {
        return submitAssignmentRepo.findByUserIdAndAssignmentIdAndMockTestInterviewId(userId, assignmentId, mockId);
    }

    public SubmitAssignment submitAssignmentByMock(Long studentId, Long assignmentId, Long mockId, MultipartFile submitAssignment, String userAssignmentAnswer) throws IOException {
        User student = userRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Assignment assignment = assignmentRepo.findById(assignmentId).orElseThrow(() -> new RuntimeException("Assignment not found"));
        MockTestInterview mockTestInterview = mockTestInterviewRepo.findById(mockId).orElseThrow(() -> new RuntimeException("MockTestInterview not found"));

        SubmitAssignment newSubmission = new SubmitAssignment();
        newSubmission.setStudent(student);
        newSubmission.setAssignment(assignment);
        newSubmission.setMockTestInterview(mockTestInterview);

        // Set file content only if it is provided
        if (submitAssignment != null && !submitAssignment.isEmpty()) {
            newSubmission.setSubmitAssignment(submitAssignment.getBytes());
        }

        newSubmission.setUserAssignmentAnswer(userAssignmentAnswer);
        newSubmission.setSubmitted(true);
        newSubmission.setSubmittedAt(LocalDateTime.now());

        return submitAssignmentRepo.save(newSubmission);
    }


    public List<Map<String, Object>> getSubmittedAssignmentsAndStudentDetailsByAssignmentId(Long assignmentId) {
        List<Object[]> results = submitAssignmentRepo.findSubmittedAssignmentsAndStudentDetailsByAssignmentId(assignmentId);

        List<Map<String, Object>> submittedAssignments = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> assignmentMap = new HashMap<>();
            assignmentMap.put("submitAssignmentId", row[0]);
            assignmentMap.put("submittedAt", row[1]);
            assignmentMap.put("userAssignmentAnswer", row[2]);
            assignmentMap.put("submitAssignment", row[3]);  // Add the document (byte array)
            assignmentMap.put("studentId", row[4]);
            assignmentMap.put("studentName", row[5]);
            assignmentMap.put("studentEmail", row[6]);  // Add email to the response
            submittedAssignments.add(assignmentMap);
        }
        return submittedAssignments;
    }

    public SubmitAssignment submitAssignmentByUserAndLesson(Long userId, Long assignmentId, Long lessonId, MultipartFile submitAssignmentDocument, String userAssignmentAnswer) throws IOException {
        // Retrieve the user, assignment, and lesson from the database
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment ID"));
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lesson ID"));

        // Create a new SubmitAssignment object
        SubmitAssignment submitAssignment = new SubmitAssignment();
        submitAssignment.setAssignment(assignment);
        submitAssignment.setUser(user);
        submitAssignment.setLesson(lesson);
        submitAssignment.setUserAssignmentAnswer(userAssignmentAnswer);
        submitAssignment.setSubmittedAt(LocalDateTime.now());

        // Set isSubmitted to true after submission
        submitAssignment.setSubmitted(true);  // This will be persisted as 1 in the database

        // Handle the file upload
        byte[] documentBytes = submitAssignmentDocument.getBytes();
        submitAssignment.setSubmitAssignment(documentBytes);

        // Save the submission to the database
        return submitAssignmentRepo.save(submitAssignment);
    }


    public Map<String, Object> getLessonDetails(Long lessonId, Long userId) {
        // Fetch lesson details
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("lessonTitle", lesson.getLessonTitle());
        response.put("lessonId", lessonId);

        // Initialize course variable by checking lesson or lessonModule
        final Course course;
        if (lesson.getCourse() != null) {
            course = lesson.getCourse();
            response.put("courseTitle", course.getCourseTitle());
            response.put("courseId", course.getId());
        } else if (lesson.getLessonModule() != null && lesson.getLessonModule().getCourse() != null) {
            course = lesson.getLessonModule().getCourse();
            response.put("courseTitle", course.getCourseTitle());
            response.put("courseId", course.getId());
        } else {
            throw new ResourceNotFoundException("Course not associated with the lesson or lesson module");
        }

        // Fetch count of assignments by lessonId
        List<Assignment> assignments = assignmentRepo.findByLessonId(lessonId);
        int totalAssignments = assignments.size();
        response.put("totalAssignments", totalAssignments);

        // Fetch submitted assignments for the given lesson and user
        List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findByLessonIdAndUserId(lessonId, userId);
        int submittedAssignmentsCount = submittedAssignments.size();
        response.put("submittedAssignmentsCount", submittedAssignmentsCount);

        // Calculate progress percentage (submitted/total * 100)
        double progressPercentage = 0.0;
        if (totalAssignments > 0) {
            progressPercentage = ((double) submittedAssignmentsCount / totalAssignments) * 100;
        }
        response.put("progressPercentage", progressPercentage);

        // Collect assignment titles, IDs, and their submission status
        List<Map<String, String>> assignmentsWithStatus = assignments.stream().map(assignment -> {
            Map<String, String> details = new HashMap<>();
            details.put("assignmentId", String.valueOf(assignment.getId()));
            details.put("assignmentTitle", assignment.getAssignmentTitle());

            boolean isSubmitted = submittedAssignments.stream()
                    .anyMatch(submitAssignment -> submitAssignment.getAssignment().getId() == assignment.getId());

            details.put("status", isSubmitted ? "Completed" : "Incomplete");

            return details;
        }).collect(Collectors.toList());

        response.put("assignments", assignmentsWithStatus);

        return response;
    }
    public long getSubmittedAssignmentCount(Long userId, Long assignmentId, Long lessonId) {
        return submitAssignmentRepo.countByUserIdAndAssignmentIdAndLessonId(userId, assignmentId, lessonId);
    }

    public List<SubmitAssignment> getSubmittedAssignmentsWithoutFeedback(Long teacherId) {
        // Get all submitted assignments for the given teacher
        List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findByAssignmentTeacherIdAndIsSubmittedTrue(teacherId);

        // Filter out assignments that already have feedback
        return submittedAssignments.stream()
                .filter(submission -> !teacherFeedbackRepo.existsByAssignmentAndTeacherId(submission.getAssignment(), teacherId))
                .collect(Collectors.toList());
    }

    public List<SubmitAssignment> getAssignmentsByMockAndStudent(Long mockId, Long studentId) {
        return submitAssignmentRepo.findByMockTestInterview_IdAndStudent_Id(mockId, studentId);
    }
}
