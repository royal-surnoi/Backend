package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    @Autowired
    AssignmentRepo assignmentRepo;

    @Autowired
    LessonRepo lessonRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    SubmitAssignmentRepo submitAssignmentRepo;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private AnswerRepo answerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    LessonModuleRepo lessonModuleRepo;

    @Autowired
    TeacherFeedbackRepo teacherFeedbackRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private SubmitProjectRepo submitProjectRepo;

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    private String assignmentDocument;

    @Autowired
    private MentorRepo mentorRepo;

    @Autowired
    private MockTestInterviewRepository mockTestInterviewRepo;


    public List<Assignment> getAssignmentsByCourseId(Long courseId) {
        return assignmentRepo.findByCourseIdWithLessons(courseId);
    }

    public List<Assignment> getAssignmentlessonsByLessonId(Long lessonId) {
        return assignmentRepo.findAssignmentBylessonId(lessonId);
    }


    public Assignment addAssign(Long lessonId, Long courseId, String assignmentTitle,
                                String assignmentTopicName, MultipartFile assignmentDocument, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime reviewMeetDate) throws IOException {
        Optional<Lesson> lessonOptional = lessonRepo.findById(lessonId);
        Optional<Course> courseOptional = courseRepo.findById(courseId);
        if (lessonOptional.isPresent() && courseOptional.isPresent()) {

            Assignment assignment = new Assignment();
            assignment.setLesson(lessonOptional.get());
            assignment.setAssignmentTitle(assignmentTitle);
            assignment.setAssignmentTopicName(assignmentTopicName);
            assignment.setAssignmentDocument(assignmentDocument.getBytes());
            assignment.setCourse(courseOptional.get());
            assignment.setReviewMeetDate(reviewMeetDate);
            assignment.setEndDate(endDate);
            assignment.setStartDate(startDate);

            return assignmentRepo.save(assignment);
        } else {
            throw new IllegalArgumentException("Lesson not found or Course not found");
        }
    }


    public Assignment updateAssignment(Long id, Assignment updatedAssignment) {
        return assignmentRepo.findById(id).map(assignment -> {
            assignment.setAssignmentTitle(updatedAssignment.getAssignmentTitle());
            assignment.setAssignmentTopicName(updatedAssignment.getAssignmentTopicName());
            assignment.setStartDate(updatedAssignment.getStartDate());
            assignment.setEndDate(updatedAssignment.getEndDate());
            assignment.setReviewMeetDate(updatedAssignment.getReviewMeetDate());
            assignment.setAssignmentDocument(updatedAssignment.getAssignmentDocument());
            assignment.setLesson(updatedAssignment.getLesson());
            assignment.setCourse(updatedAssignment.getCourse());
            assignment.setSubmissions(updatedAssignment.getSubmissions());
            setTimestamps(assignment); // Ensure timestamps are set during update
            return assignmentRepo.save(assignment);
        }).orElseGet(() -> {
            updatedAssignment.setId(id);
            setTimestamps(updatedAssignment); // Ensure timestamps are set for new assignments
            return assignmentRepo.save(updatedAssignment);
        });
    }

    public Assignment updateAssign(long assignmentId, String assignmentTitle, String assignmentTopicName,
                                   MultipartFile assignmentDocument, LocalDateTime startDate, LocalDateTime endDate,
                                   LocalDateTime reviewMeetDate, String assignmentDescription, Long maxScore, String assignmentAnswer) {
        return assignmentRepo.findById(assignmentId).map(assignment -> {
            assignment.setAssignmentTitle(assignmentTitle);
            assignment.setAssignmentTopicName(assignmentTopicName);
            assignment.setStartDate(startDate);
            assignment.setEndDate(endDate);
            assignment.setReviewMeetDate(reviewMeetDate);
            assignment.setAssignmentDescription(assignmentDescription);
            assignment.setMaxScore(maxScore);
            assignment.setAssignmentAnswer(assignmentAnswer);
            if (assignmentDocument != null && !assignmentDocument.isEmpty()) {
                try {
                    assignment.setAssignmentDocument(assignmentDocument.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read assignment document", e);
                }
            }
            setTimestamps(assignment);
            return assignmentRepo.save(assignment);
        }).orElseThrow(() -> new IllegalArgumentException("Assignment not found with id: " + assignmentId));
    }


    public void deleteAssignment(Long id) {
        assignmentRepo.deleteById(id);
    }

    private void setTimestamps(Assignment assignment) {
        LocalDateTime now = LocalDateTime.now();
        if (assignment.getCreatedAt() == null) {
            assignment.setCreatedAt(now);
        }
        assignment.setUpdatedAt(now);
    }

    public void deleteAssign(long assignmentId) {
        if (!assignmentRepo.existsById(assignmentId)) {
            throw new IllegalArgumentException("Assignment not found");
        }
        submitAssignmentRepo.deleteByAssignmentId(assignmentId);
        teacherFeedbackRepo.deleteByAssignmentId(assignmentId);
        assignmentRepo.deleteById(assignmentId);
    }


    public List<Assignment> getAssignmentsByCourse(Course course) {
        return assignmentRepo.findByCourse(course);
    }


    public List<Assignment> getAssignmentsDueInFiveDays() {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime fiveDaysLater = now.plusDays(5);

        return assignmentRepo.findAll().stream()

                .filter(assignment -> assignment.getEndDate() != null && assignment.getEndDate().isAfter(now) && assignment.getEndDate().isBefore(fiveDaysLater))

                .collect(Collectors.toList());

    }

    public Assignment createNewAssignment(String assignmentTitle, String assignmentTopicName, Long lessonId, Long courseId, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime reviewMeetDate, MultipartFile file, User teacherId) {

        Assignment assignment = new Assignment();
        assignment.setAssignmentTitle(assignmentTitle);
        assignment.setAssignmentTopicName(assignmentTopicName);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        assignment.setReviewMeetDate(reviewMeetDate);
        assignment.setTeacher(teacherId);

        try {
            assignment.setAssignmentDocument(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();

        }

        assignment.setLesson(lessonRepo.findById(lessonId).orElse(null));
        assignment.setCourse(courseRepo.findById(courseId).orElse(null));

        Assignment savedAssignment = assignmentRepo.save(assignment);

        createNotification(savedAssignment);

        createPendingNotification(savedAssignment);

        return savedAssignment;
    }








    private void createNotification(Assignment assignment) {
        if (assignment.getCourse() != null) {
            LocalDateTime notificationDate = assignment.getEndDate().minusDays(5);
            String content = "Reminder: The assignment '" + assignment.getAssignmentTitle() + "' is due on " + assignment.getEndDate() + ".";

            Notification notification = new Notification();
            notification.setUser(assignment.getCourse().getUser()); // Assuming Course has a User
            notification.setContent(content);
            notification.setTimestamp(notificationDate);

            notificationRepo.save(notification);
        }
    }

    private void createCompletionNotification(SubmitAssignment submitAssignment) {
        String content = "Assignment '" + submitAssignment.getAssignment().getAssignmentTitle() + "' has been submitted.";

        Notification notification = new Notification();
        notification.setUser(submitAssignment.getUser());
        notification.setContent(content);
        notification.setRead(notification.isRead());
        notification.setTimestamp(LocalDateTime.now());

        notificationRepo.save(notification);
    }

    private void createPendingNotification(Assignment assignment) {
        if (assignment.getCourse() != null && assignment.getEndDate().isBefore(LocalDateTime.now()) && !isAssignmentSubmitted(assignment.getId())) {
            String content = "Assignment '" + assignment.getAssignmentTitle() + "' is pending and was not submitted on time.";

            Notification notification = new Notification();
            notification.setUser(assignment.getCourse().getUser()); // Assuming Course has a User
            notification.setContent(content);
            notification.setRead(notification.isRead());
            notification.setTimestamp(LocalDateTime.now());

            notificationRepo.save(notification);
        }
    }

    private boolean isAssignmentSubmitted(Long assignmentId) {
        return submitAssignmentRepo.findByAssignmentId(assignmentId).isPresent();
    }


    public List<Assignment> getAssignmentsByLessonIdAndLessonModuleId(Long lessonId, Long lessonModuleId, String assignmentTitle, String assignmentTopicName, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime reviewMeetDate, String assignmentDescription, Long maxScore, String assignmentAnswer, MultipartFile assignmentDocument) {
        Optional<Lesson> lesson = lessonRepo.findById(lessonId);
        Optional<LessonModule> lessonModule = lessonModuleRepo.findById(lessonModuleId);

        if (!lesson.isPresent() || !lessonModule.isPresent()) {
            throw new IllegalArgumentException("Invalid lessonId or lessonModuleId");
        }

        byte[] documentBytes = null;
        try {
            documentBytes = assignmentDocument.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assignment assignment = new Assignment();
        assignment.setLesson(lesson.get());
        assignment.setLessonModule(lessonModule.get());
        assignment.setAssignmentTitle(assignmentTitle);
        assignment.setAssignmentTopicName(assignmentTopicName);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        assignment.setReviewMeetDate(reviewMeetDate);
        assignment.setAssignmentDescription(assignmentDescription);

        assignment.setMaxScore(maxScore);
        assignment.setAssignmentAnswer(assignmentAnswer);
        assignment.setAssignmentDocument(documentBytes);

        assignmentRepo.save(assignment);

        return assignmentRepo.findByLessonIdAndLessonModuleId(lessonId, lessonModuleId);
    }



    public String getSubmittedAssignmentsByTotal(Long id) {
        return "Handle this method separately";
    }


    public List<Assignment> getUpcomingAssessmentsForUser(Long userId) {
        return assignmentRepo.findUpcomingAssessmentsByUser(userId);

    }

    public List<Assignment> getAssignmentsByUserId(Long userId) {
        return assignmentRepo.findByLesson_Course_UserId(userId);
    }
    public List<Assignment> getUpcomingAssignmentsByUserId(Long userId) {
        LocalDateTime currentDate = LocalDateTime.now();
        return assignmentRepo.findUpcomingAssignmentsByUserId(userId, currentDate);
    }



    public List<Assignment> getAssignmentsByLessonModuleId(Long lessonModuleId) {
        return assignmentRepo.findByLessonModuleId(lessonModuleId);
    }


    public Assignment createAssignmentByTeacher(long teacherId, long studentId, String assignmentTitle, String assignmentTopicName, String assignmentDescription, Long maxScore, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime reviewMeetDate, MultipartFile assignmentDocument) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        User student = userRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        Assignment assignment = new Assignment();
        assignment.setTeacher(teacher);
        assignment.setStudent(student);
        assignment.setAssignmentTitle(assignmentTitle);
        assignment.setAssignmentTopicName(assignmentTopicName);
        assignment.setAssignmentDescription(assignmentDescription);
        assignment.setMaxScore(maxScore);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        assignment.setReviewMeetDate(reviewMeetDate);

        try {
            assignment.setAssignmentDocument(assignmentDocument.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store assignment document", e);
        }

        Assignment savedAssignmentByTeacher = assignmentRepo.save(assignment);

        createNotification(savedAssignmentByTeacher);

        createPendingNotification(savedAssignmentByTeacher);

        return savedAssignmentByTeacher;
    }

    public Assignment createAssignmentByTeacherByCourse(long teacherId, long studentId,long courseId, String assignmentTitle, String assignmentTopicName, String assignmentDescription, Long maxScore, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime reviewMeetDate, MultipartFile assignmentDocument) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        User student = userRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        Assignment assignment = new Assignment();
        assignment.setTeacher(teacher);
        assignment.setStudent(student);
        assignment.setCourse(course);
        assignment.setAssignmentTitle(assignmentTitle);
        assignment.setAssignmentTopicName(assignmentTopicName);
        assignment.setAssignmentDescription(assignmentDescription);
        assignment.setMaxScore(maxScore);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        assignment.setReviewMeetDate(reviewMeetDate);

        try {
            assignment.setAssignmentDocument(assignmentDocument.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store assignment document", e);
        }

        Assignment savedAssignmentByTeacher = assignmentRepo.save(assignment);

        createNotification(savedAssignmentByTeacher);

        createPendingNotification(savedAssignmentByTeacher);

        return savedAssignmentByTeacher;
    }

    public List<User> getUsersWhoSubmittedAssignment(Long assignmentId) {
        List<Long> userIds = submitAssignmentRepo.findUserIdsByAssignmentId(assignmentId);
        return userRepo.findAllById(userIds);
    }

    public Assignment createAssignmentByTeacherByCourseId(long teacherId,long courseId, String assignmentTitle, String assignmentTopicName, String assignmentDescription, Long maxScore, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime reviewMeetDate, MultipartFile assignmentDocument) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));

        Course course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        Assignment assignment = new Assignment();
        assignment.setTeacher(teacher);

        assignment.setCourse(course);
        assignment.setAssignmentTitle(assignmentTitle);
        assignment.setAssignmentTopicName(assignmentTopicName);
        assignment.setAssignmentDescription(assignmentDescription);
        assignment.setMaxScore(maxScore);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        assignment.setReviewMeetDate(reviewMeetDate);

        try {
            assignment.setAssignmentDocument(assignmentDocument.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store assignment document", e);
        }

        Assignment savedAssignmentByTeacher = assignmentRepo.save(assignment);

        createNotification(savedAssignmentByTeacher);

        createPendingNotification(savedAssignmentByTeacher);

        return savedAssignmentByTeacher;
    }
    public List<Assignment> getAssignmentsByTeacherId(Long teacherId) {
        return assignmentRepo.findByTeacherId(teacherId);
    }





    public Optional<Assignment> getAssignmentByIdAndTeacherId(Long id, Long teacherId) {
        return assignmentRepo.findByIdAndTeacherId(id, teacherId);
    }

    public Assignment updateAssignmentFields(Long id, Long teacherId,
                                             String assignmentTitle, String assignmentTopicName, String assignmentDescription,
                                             Long maxScore, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime reviewMeetDate,
                                             String assignmentAnswer, byte[] assignmentDocument) {
        Optional<Assignment> optionalAssignment = getAssignmentByIdAndTeacherId(id, teacherId);
        if (optionalAssignment.isPresent()) {
            Assignment assignment = optionalAssignment.get();

            if (assignmentTitle != null) {
                assignment.setAssignmentTitle(assignmentTitle);
            }
            if (assignmentTopicName != null) {
                assignment.setAssignmentTopicName(assignmentTopicName);
            }
            if (assignmentDescription != null) {
                assignment.setAssignmentDescription(assignmentDescription);
            }
            if (maxScore != null) {
                assignment.setMaxScore(maxScore);
            }
            if (startDate != null) {
                assignment.setStartDate(startDate);
            }
            if (endDate != null) {
                assignment.setEndDate(endDate);
            }
            if (reviewMeetDate != null) {
                assignment.setReviewMeetDate(reviewMeetDate);
            }
            if (assignmentAnswer != null) {
                assignment.setAssignmentAnswer(assignmentAnswer);
            }
            if (assignmentDocument != null) {
                assignment.setAssignmentDocument(assignmentDocument);
            }

            assignment.setUpdatedAt(LocalDateTime.now());
            return assignmentRepo.save(assignment);
        } else {
            throw new IllegalArgumentException("Assignment not found for id: " + id + " and teacherId: " + teacherId);
        }
    }
    public List<Assignment> getAssignmentsByCourseIdAndTeacherId(Long courseId, Long teacherId) {
        return assignmentRepo.findByCourseIdAndTeacherId(courseId, teacherId);
    }

    public List<Assignment> getAssignmentsByCourseIdAndStudentId(Long courseId, Long studentId) {
        return assignmentRepo.findByCourseIdAndStudentId(courseId, studentId);
    }

    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepo.findById(id);
    }


    public Map<String, Double> calculateProgressPercentage(Long courseId, Long userId) {
        int totalAssignments = assignmentRepo.countByCourseId(courseId);
        int submittedAssignments = submitAssignmentRepo.countByCourseIdAndUserId(courseId, userId);

        int totalProjects = projectRepo.countByCourseId(courseId);
        int submittedProjects = submitProjectRepo.countByCourseIdAndUserId(courseId, userId);

        int totalQuizzes = quizRepo.countByCourseId(courseId);
        int submittedQuizzes = answerRepo.countByCourseIdAndUserId(courseId, userId);

        double assignmentPercentage = totalAssignments == 0 ? 0 : (submittedAssignments / (double) totalAssignments) * 100;
        double projectPercentage = totalProjects == 0 ? 0 : (submittedProjects / (double) totalProjects) * 100;
        double quizPercentage = totalQuizzes == 0 ? 0 : (submittedQuizzes / (double) totalQuizzes) * 100;

        double overallProgress = (assignmentPercentage + projectPercentage + quizPercentage) / 3;

        Map<String, Double> progressPercentage = new HashMap<>();
        progressPercentage.put("overallProgress", overallProgress);

        return progressPercentage;
    }




    public double calculateAssignmentProgress(Long courseId, Long userId) {
        List<Assignment> totalAssignments = assignmentRepo.findByCourseId(courseId);
        int totalAssignmentsCount = totalAssignments.size();

        long submittedAssignmentsCount = submitAssignmentRepo.countByUserIdAndAssignmentCourseId(userId, courseId);

        if (totalAssignmentsCount == 0) {
            return 0; // Avoid division by zero
        }

        return (double) submittedAssignmentsCount / totalAssignmentsCount * 100;
    }



    public Map<String, Double> getLessonAndModuleProgressByCourse(Long courseId) {
        Map<String, Double> progressMap = new HashMap<>();
        progressMap.put("lessonProgress", calculateLessonProgressByCourse(courseId));
        progressMap.put("lessonModuleProgress", calculateLessonModuleProgressByCourse(courseId));
        return progressMap;
    }

    private double calculateLessonProgressByCourse(Long courseId) {
        List<Assignment> assignments = assignmentRepo.findByCourseId(courseId);
        return calculateProgressForLessons(assignments);
    }

    private double calculateLessonModuleProgressByCourse(Long courseId) {
        List<Assignment> assignments = assignmentRepo.findByCourseId(courseId);
        return calculateProgressForLessonModules(assignments);
    }

    private double calculateProgressForLessons(List<Assignment> assignments) {
        if (assignments.isEmpty()) return 0;

        long totalLessons = assignments.stream()
                .map(Assignment::getLesson)
                .distinct()
                .count();

        long completedLessons = assignments.stream()
                .filter(assignment -> assignment.getAssignmentAnswer() != null && !assignment.getAssignmentAnswer().isEmpty())
                .map(Assignment::getLesson)
                .distinct()
                .count();

        return totalLessons == 0 ? 0 : (double) completedLessons / totalLessons * 100;
    }

    private double calculateProgressForLessonModules(List<Assignment> assignments) {
        if (assignments.isEmpty()) return 0;

        long totalModules = assignments.stream()
                .map(Assignment::getLessonModule)
                .distinct()
                .count();

        long completedModules = assignments.stream()
                .filter(assignment -> assignment.getAssignmentAnswer() != null && !assignment.getAssignmentAnswer().isEmpty())
                .map(Assignment::getLessonModule)
                .distinct()
                .count();

        return totalModules == 0 ? 0 : (double) completedModules / totalModules * 100;
    }

    public List<Assignment> getAssignmentsByUserAndCourse(Long userId, Long courseId) {
        return assignmentRepo.findByUserIdAndCourseId(userId, courseId);
    }

    public Map<Long, Double> getProgressByModulesInCourse(Long courseId, Long userId) {
        List<Long> lessonModuleIds = assignmentRepo.findDistinctLessonModuleIdsByCourseId(courseId);
        System.out.println("LessonModule IDs: " + lessonModuleIds);

        Map<Long, Double> moduleProgressMap = new HashMap<>();

        for (Long lessonModuleId : lessonModuleIds) {
            if (lessonModuleId != null) {
                List<Assignment> totalAssignments = assignmentRepo.findByLessonModuleId(lessonModuleId);
                System.out.println("Total Assignments for Module " + lessonModuleId + ": " + totalAssignments.size());

                List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findByLessonModuleIdAndUserId(lessonModuleId, userId);
                System.out.println("Submitted Assignments for Module " + lessonModuleId + ": " + submittedAssignments.size());

                double progress = totalAssignments.isEmpty() ? 0 : (double) submittedAssignments.size() / totalAssignments.size() * 100;
                moduleProgressMap.put(lessonModuleId, progress);
            }
        }

        System.out.println("Module Progress Map: " + moduleProgressMap);
        return moduleProgressMap;
    }

    @Transactional
    public String getAssignmentStatistics(Long courseId, Long userId) {
        long totalAssignments = assignmentRepo.countTotalAssignmentsByCourseId(courseId);
        long submittedAssignments = submitAssignmentRepo.countSubmittedAssignmentsByCourseIdAndUserId(courseId, userId);

        return submittedAssignments + "/" + totalAssignments;
    }

    @Transactional
    public double getSubmissionPercentage(Long courseId, Long userId) {
        long totalAssignments = assignmentRepo.countTotalAssignmentsByCourseId(courseId);
        long submittedAssignments = submitAssignmentRepo.countSubmittedAssignmentsByCourseIdAndUserId(courseId, userId);

        if (totalAssignments == 0) {
            return 0.0; // Avoid division by zero
        }

        return (double) submittedAssignments / totalAssignments * 100;
    }

    public Optional<String> getAssignmentGrade(Long studentId, Long assignmentId) {
        Optional<TeacherFeedback> feedbackOptional = teacherFeedbackRepo.findByStudentIdAndAssignmentId(studentId, assignmentId);

        if (feedbackOptional.isPresent()) {
            TeacherFeedback feedback = feedbackOptional.get();
            return Optional.ofNullable(feedback.getGrade());
        } else {
            return Optional.empty();
        }
    }

    public Map<String, Object> calculateProgressOfIndividual(Long studentId, Long courseId) {
        List<Assignment> assignments = assignmentRepo.findByCourse_Id(courseId);
        List<SubmitAssignment> submissions = submitAssignmentRepo.findByStudent_IdAndCourse_Id(studentId, courseId);

        int totalAssignments = assignments.size();
        int submittedAssignments = submissions.size();

        double progressPercentage = totalAssignments == 0 ? 0 : ((double) submittedAssignments / totalAssignments) * 100;

        String courseTitle = courseRepo.findById(courseId)
                .map(Course::getCourseTitle)
                .orElse("Unknown Course");

        // Create a map to store the submission status of each assignment
        Map<Long, Boolean> assignmentSubmissionStatus = submissions.stream()
                .collect(Collectors.toMap(
                        submission -> submission.getAssignment().getId(),
                        submission -> true
                ));

        List<Map<String, Object>> assignmentDetails = assignments.stream()
                .map(assignment -> {
                    Map<String, Object> details = new HashMap<>();
                    details.put("assignment_id",assignment.getId());
                    details.put("assignmentTitle", assignment.getAssignmentTitle());
                    details.put("status", assignmentSubmissionStatus.containsKey(assignment.getId()) ? "Completed" : "Not Completed");
                    return details;
                })
                .collect(Collectors.toList());

        Long teacherId = null;
        String teacherName = "Unknown";
        if (!assignments.isEmpty() && assignments.get(0).getTeacher() != null) {
            User teacher = assignments.get(0).getTeacher();
            teacherId = teacher.getId(); // Assuming User entity has a method getId
            teacherName = teacher.getName(); // Assuming User entity has a method getName
        }

        Map<String, Object> progressData = new HashMap<>();
        progressData.put("totalAssignments", totalAssignments);
        progressData.put("submittedAssignments", submittedAssignments);
        progressData.put("progressPercentage", progressPercentage);
        progressData.put("courseTitle", courseTitle);
        progressData.put("assignments", assignmentDetails);
        progressData.put("teacherId", teacherId);
        progressData.put("teacherName", teacherName);

        return progressData;
    }


    @Transactional
    public void submitNewAssignment(Long studentId, Long courseId, Long assignmentId, MultipartFile submitAssignment, String assignmentAnswer) throws IOException {
        if (submitAssignmentRepo.existsByStudentIdAndAssignmentId(studentId, assignmentId)) {
            throw new IllegalStateException("Assignment already submitted");
        }

        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        SubmitAssignment newSubmission = new SubmitAssignment();
        newSubmission.setSubmitAssignment(submitAssignment.getBytes());
        newSubmission.setUserAssignmentAnswer(assignmentAnswer);
        newSubmission.setStudent(student);
        newSubmission.setCourse(course);
        newSubmission.setAssignment(assignment);
        newSubmission.setSubmittedAt(LocalDateTime.now());
        newSubmission.setSubmitted(true);

        submitAssignmentRepo.save(newSubmission);

        createCompletionNotification(newSubmission);
    }

    public boolean hasStudentSubmittedAssignment(Long studentId, Long assignmentId) {
        return submitAssignmentRepo.existsByStudentIdAndAssignmentId(studentId, assignmentId);
    }

    public Optional<String> getAssignmentGrades(Long studentId, Long assignmentId) {
        Optional<TeacherFeedback> feedbackOptional = teacherFeedbackRepo.findByStudentIdAndAssignmentId(studentId, assignmentId);

        if (feedbackOptional.isPresent()) {
            TeacherFeedback feedback = feedbackOptional.get();
            return Optional.ofNullable(feedback.getGrade());
        } else {
            return Optional.empty();
        }
    }


    public double getProgressByModulesInCourses(Long courseId, Long userId) {
        List<Long> lessonModuleIds = assignmentRepo.findDistinctLessonModuleIdsByCourseId(courseId);
        long totalAssignments = 0;
        long submittedAssignments = 0;

        for (Long lessonModuleId : lessonModuleIds) {
            if (lessonModuleId != null) {
                List<Assignment> assignments = assignmentRepo.findByLessonModuleId(lessonModuleId);
                totalAssignments += assignments.size();

                List<SubmitAssignment> submissions = submitAssignmentRepo.findByLessonModuleIdAndUserId(lessonModuleId, userId);
                submittedAssignments += submissions.size();
            }
        }

        if (totalAssignments == 0) {
            return 0.0; // Avoid division by zero if there are no assignments
        }

        return (double) submittedAssignments / totalAssignments * 100;
    }


    public boolean hasUserSubmittedAssignment(Long userId, Long assignmentId) {
        // Check if the user has already submitted the assignment (and it's marked as submitted)
        return submitAssignmentRepo.existsByUserIdAndAssignmentIdAndIsSubmittedTrue(userId, assignmentId);
    }

    public SubmitAssignment submitAssignment(Long assignmentId, Long userId, Long courseId, MultipartFile submitAssignmentDocument, String assignmentAnswer) {
        // Retrieve the assignment and user from the database
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment ID"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // Create a new SubmitAssignment object
        SubmitAssignment submitAssignment = new SubmitAssignment();
        submitAssignment.setAssignment(assignment);
        submitAssignment.setUser(user);
        submitAssignment.setUserAssignmentAnswer(assignmentAnswer);
        submitAssignment.setSubmittedAt(LocalDateTime.now());
        submitAssignment.setSubmitted(true);  // Ensure this is set correctly

        // Handle optional courseId
        if (courseId != null) {
            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
            submitAssignment.setCourse(course);
        }

        // Handle file submission
        try {
            byte[] documentBytes = submitAssignmentDocument.getBytes();
            submitAssignment.setSubmitAssignment(documentBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process document", e);
        }

        // Save the submission to the database
        return submitAssignmentRepo.save(submitAssignment);
    }

    public List<Assignment> getAssignmentsByLessonAndModule(Long lessonId, Long lessonModuleId) {
        return assignmentRepo.findByLessonIdAndLessonModuleId(lessonId, lessonModuleId);
    }



    public Map<String, Object> getProgressDetails(Long lessonId, Long userId) {
        Map<String, Object> progressDetails = new HashMap<>();

        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        Course course = lesson.getCourse();

        List<Assignment> totalAssignments = assignmentRepo.findByLessonId(lessonId);
        List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findByLessonIdAndUserIdAndIsSubmittedTrue(lessonId, userId);

        int totalAssignmentsCount = totalAssignments.size();
        int submittedAssignmentsCount = submittedAssignments.size();
        int unsubmittedAssignmentsCount = totalAssignmentsCount - submittedAssignmentsCount;

        double progressPercentage = 0.0;
        if (totalAssignmentsCount > 0) {
            progressPercentage = (double) submittedAssignmentsCount / totalAssignmentsCount * 100;
        }

        List<String> assignmentTitles = totalAssignments.stream()
                .map(Assignment::getAssignmentTitle)
                .collect(Collectors.toList());


        List<Long> submittedAssignmentIds = submittedAssignments.stream()
                .map(submitAssignment -> submitAssignment.getAssignment().getId())
                .collect(Collectors.toList());


        progressDetails.put("courseTitle", course.getCourseTitle());
        progressDetails.put("lessonTitle", lesson.getLessonTitle());
        progressDetails.put("assignmentTitles", assignmentTitles);
        progressDetails.put("totalAssignmentsCount", totalAssignmentsCount);
        progressDetails.put("submittedAssignmentsCount", submittedAssignmentsCount);
        progressDetails.put("unsubmittedAssignmentsCount", unsubmittedAssignmentsCount);
        progressDetails.put("progressPercentage", progressPercentage);
        progressDetails.put("assignmentIds", submittedAssignmentIds);

        return progressDetails;
    }


    public Assignment createAssignmentByLessonAndModule(
            long lessonId, long lessonModuleId, String assignmentTitle, String assignmentTopicName,
            String assignmentDescription, Long maxScore, LocalDateTime startDate,
            LocalDateTime endDate, LocalDateTime reviewMeetDate, MultipartFile assignmentDocument) {

        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        LessonModule lessonModule = lessonModuleRepo.findById(lessonModuleId)
                .orElseThrow(() -> new ResourceNotFoundException("LessonModule not found"));

        Assignment assignment = new Assignment();
        assignment.setLesson(lesson);
        assignment.setLessonModule(lessonModule);
        assignment.setAssignmentTitle(assignmentTitle);
        assignment.setAssignmentTopicName(assignmentTopicName);
        assignment.setAssignmentDescription(assignmentDescription);
        assignment.setMaxScore(maxScore);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        assignment.setReviewMeetDate(reviewMeetDate);

        try {
            assignment.setAssignmentDocument(assignmentDocument.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error processing assignment document", e);
        }

        return assignmentRepo.save(assignment);
    }

    public List<Map<String, Object>> getAssignmentsAndStudentsWithSubmitAssignmentsByTeacherIdAndStudentId(long teacherId, long studentId) {
        List<Object[]> results = assignmentRepo.getAssignmentsAndStudentsByTeacherIdAndStudentId(teacherId, studentId);
        return results.stream()
                .map(result -> {
                    SubmitAssignment submitAssignment = getSubmitAssignmentByStudentId(studentId);
                    return Map.of(
                            "assignmentId", result[0],
                            "studentId", result[1],
                            "studentName", result[2],
                            "submitAssignment", submitAssignment
                    );
                })
                .collect(Collectors.toList());
    }

    private SubmitAssignment getSubmitAssignmentByStudentId(long studentId) {
        return submitAssignmentRepo.findByStudentId(studentId);
    }






    public int getStudentCountByAssignmentAndTeacher(long assignmentId, long teacherId) {
        return assignmentRepo.countStudentIdsByAssignmentIdAndTeacherId(assignmentId, teacherId);
    }

    public Assignment updateAssignments(long assignmentId,
                                        String assignmentTitle,
                                        String assignmentTopicName,
                                        String assignmentDescription,
                                        Long maxScore,
                                        LocalDateTime startDate,
                                        LocalDateTime endDate,
                                        LocalDateTime reviewMeetDate,
                                        MultipartFile assignmentDocument) {
        // Fetch the existing assignment
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id " + assignmentId));

        // Update fields if provided
        if (assignmentTitle != null) {
            assignment.setAssignmentTitle(assignmentTitle);
        }
        if (assignmentTopicName != null) {
            assignment.setAssignmentTopicName(assignmentTopicName);
        }
        if (assignmentDescription != null) {
            assignment.setAssignmentDescription(assignmentDescription);
        }
        if (maxScore != null) {
            assignment.setMaxScore(maxScore);
        }
        if (startDate != null) {
            assignment.setStartDate(startDate);
        }
        if (endDate != null) {
            assignment.setEndDate(endDate);
        }
        if (reviewMeetDate != null) {
            assignment.setReviewMeetDate(reviewMeetDate);
        }
        if (assignmentDocument != null) {
            try {
                assignment.setAssignmentDocument(assignmentDocument.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process assignment document", e);
            }
        }

        // Save and return the updated assignment
        return assignmentRepo.save(assignment);
    }

    public Map<String, Object> getCourseAndStudentDetailsByAssignmentId(long assignmentId) {
        // Fetch assignment details
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Fetch course details
        Course course = assignment.getCourse();

        // Fetch student details
        List<Long> studentIds = Arrays.asList(assignment.getStudentIds().split(","))
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<User> students = userRepo.findAllById(studentIds);

        // Prepare response map
        Map<String, Object> response = new HashMap<>();
        response.put("assignment", assignment);
        response.put("course", course);
        response.put("students", students);

        return response;
    }

    public String getAssignmentSubmissionSummary(Long courseId, Long userId) {
        // Count total assignments in lessons that belong to the course
        long totalAssignments = assignmentRepo.countAssignmentsInLessonsByCourse(courseId);

        // Count submitted assignments by the user in the course
        long submittedAssignments = submitAssignmentRepo.countSubmittedAssignmentsByCourseAndUser(courseId, userId);

        return submittedAssignments + "/" + totalAssignments;
    }

    public Assignment createAssignment(long teacherId, long courseId, List<Long> studentIds,
                                       String assignmentTitle, String assignmentTopicName,
                                       String assignmentDescription, Long maxScore,
                                       LocalDateTime startDate, LocalDateTime endDate,
                                       LocalDateTime reviewMeetDate, MultipartFile assignmentDocument) {

        // Fetch teacher and students from repositories
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        List<User> students = userRepo.findAllById(studentIds);

        Assignment assignment = new Assignment();
        assignment.setTeacher(teacher);
        assignment.setCourse(courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found")));
        assignment.setAssignmentTitle(assignmentTitle);
        assignment.setAssignmentTopicName(assignmentTopicName);
        assignment.setAssignmentDescription(assignmentDescription);
        assignment.setMaxScore(maxScore);
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        assignment.setReviewMeetDate(reviewMeetDate);

        // Convert student IDs list to comma-separated string
        String studentIdsString = studentIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        assignment.setStudentIds(studentIdsString);

        try {
            assignment.setAssignmentDocument(assignmentDocument.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store assignment document", e);
        }

        return assignmentRepo.save(assignment);
    }


    public Assignment addNewAssign(Long lessonId, Long courseId, Long lessonModuleId,
                                   String assignmentTitle, String assignmentTopicName,
                                   MultipartFile assignmentDocument,
                                   LocalDateTime startDate, LocalDateTime endDate,
                                   LocalDateTime reviewMeetDate) throws IOException {
        // Fetch the lesson
        Optional<Lesson> lessonOptional = lessonRepo.findById(lessonId);

        // Check if the lesson is directly mapped to the course
        if (lessonOptional.isPresent()) {
            Lesson lesson = lessonOptional.get();

            // Case 1: Lesson is directly mapped to the course (no lessonModuleId)
            if (lessonModuleId == null) {
                Optional<Course> courseOptional = courseRepo.findById(courseId);
                if (courseOptional.isPresent()) {
                    // Create and save the assignment
                    return saveAssignment(lesson, courseOptional.get(), null, assignmentTitle,
                            assignmentTopicName, assignmentDocument, startDate,
                            endDate, reviewMeetDate);
                } else {
                    throw new IllegalArgumentException("Course not found");
                }
            }

            // Case 2: Lesson is mapped to a lessonModule which is mapped to the course
            Optional<LessonModule> lessonModuleOptional = lessonModuleRepo.findById(lessonModuleId);
            if (lessonModuleOptional.isPresent() && lessonModuleOptional.get().getCourse().getId()==(courseId)) {
                return saveAssignment(lesson, lessonModuleOptional.get().getCourse(),
                        lessonModuleOptional.get(), assignmentTitle, assignmentTopicName,
                        assignmentDocument, startDate, endDate, reviewMeetDate);
            } else {
                throw new IllegalArgumentException("LessonModule not found or does not match the course");
            }
        } else {
            throw new IllegalArgumentException("Lesson not found");
        }
    }

    // Helper method to handle assignment saving logic
    private Assignment saveAssignment(Lesson lesson, Course course, LessonModule lessonModule,
                                      String assignmentTitle, String assignmentTopicName,
                                      MultipartFile assignmentDocument, LocalDateTime startDate,
                                      LocalDateTime endDate, LocalDateTime reviewMeetDate) throws IOException {
        Assignment assignment = new Assignment();
        assignment.setLesson(lesson);
        assignment.setAssignmentTitle(assignmentTitle);
        assignment.setAssignmentTopicName(assignmentTopicName);
        assignment.setAssignmentDocument(assignmentDocument.getBytes());
        assignment.setCourse(course);
        if (lessonModule != null) {
            assignment.setLessonModule(lessonModule);
        }
        assignment.setReviewMeetDate(reviewMeetDate);
        assignment.setEndDate(endDate);
        assignment.setStartDate(startDate);
        return assignmentRepo.save(assignment);
    }



    public List<String> getAllTeacherPostedItems(Long teacherId) {
        if (teacherId == null) {
            throw new IllegalArgumentException("Teacher ID must not be null");
        }

        // Verify that the teacher exists in the Mentor table
        Mentor mentor = mentorRepo.findByUserId(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with ID " + teacherId + " not found."));

        // Fetch all student IDs associated with the teacher
        List<Long> studentIds = userRepo.findDistinctStudentIdsByTeacherId(teacherId);
        if (studentIds == null || studentIds.isEmpty()) {
            throw new EntityNotFoundException("No students found for Teacher with ID " + teacherId);
        }

        // Collect all assignments and projects
        List<String> results = new ArrayList<>();
        for (Long studentId : studentIds) {
            if (studentId == null) {
                throw new IllegalArgumentException("Student ID must not be null");
            }
            results.addAll(fetchAssignmentsForStudent(teacherId, studentId));
            results.addAll(fetchProjectsForStudent(teacherId, studentId));
        }

        // Fetch student names from the studentIds column
        for (Long studentId : studentIds) {
            if (studentId == null) {
                throw new IllegalArgumentException("Student ID must not be null");
            }
            User student = userRepo.findById(studentId)
                    .orElseThrow(() -> new EntityNotFoundException("Student with ID " + studentId + " not found."));
            results.add("Student Name: " + student.getName());
        }

        if (results.isEmpty()) {
            throw new EntityNotFoundException("No items found for Teacher with ID " + teacherId);
        }

        return results;
    }

    private List<String> fetchAssignmentsForStudent(Long teacherId, Long studentId) {
        // Implement logic to fetch assignments for the student
        return assignmentRepo.findAssignmentsByTeacherIdAndStudentId(teacherId, studentId)
                .stream()
                .map(Assignment::getAssignmentTitle)
                .collect(Collectors.toList());
    }

    private List<String> fetchProjectsForStudent(Long teacherId, Long studentId) {
        // Implement logic to fetch projects for the student
        // Example: return projectRepo.findProjectsByTeacherIdAndStudentId(teacherId, studentId);
        return new ArrayList<>(); // Placeholder
    }



    public String createAssignmentByMock(String assignmentTitle, String assignmentTopicName,
                                         String assignmentDescription, MultipartFile assignmentDocument,
                                         Long mockId, Long teacherId) throws IOException {

        // Validate the teacherId
        Optional<User> teacherOptional = userRepo.findById(teacherId);
        if (!teacherOptional.isPresent()) {
            return "Invalid teacher ID.";
        }

        User teacher = teacherOptional.get();

        // Check if the user is a mentor
        Optional<Mentor> mentorOptional = mentorRepo.findByUserId(teacherId);
        if (!mentorOptional.isPresent()) {
            return "The specified teacher is not a mentor.";
        }

        // Validate the mockId
        Optional<MockTestInterview> mockTestInterviewOptional = mockTestInterviewRepo.findById(mockId);
        if (!mockTestInterviewOptional.isPresent()) {
            return "Invalid mock test interview ID.";
        }

        MockTestInterview mockTestInterview = mockTestInterviewOptional.get();

        // Check if the mockId already exists in the Assignment table
        Optional<Assignment> existingAssignment = assignmentRepo.findByMockTestInterview(mockTestInterview);
        if (existingAssignment.isPresent()) {
            return "Assignment for this mock test interview ID already exists.";
        }

        // Create and save the assignment
        Assignment assignment = new Assignment();
        assignment.setAssignmentTitle(assignmentTitle);
        assignment.setAssignmentTopicName(assignmentTopicName);
        assignment.setAssignmentDescription(assignmentDescription);
        assignment.setAssignmentDocument(assignmentDocument.getBytes());
        assignment.setMockTestInterview(mockTestInterview);
        assignment.setTeacher(teacher);
        assignment.setCreatedAt(LocalDateTime.now());
        assignment.setUpdatedAt(LocalDateTime.now());

        assignmentRepo.save(assignment);

        return "Assignment created successfully.";
    }

    public Map<String, Object> getAssignmentDetails(Long lessonId, Long userId) {
        Map<String, Object> assignmentDetails = new HashMap<>();

        // Fetch lesson details
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        // Initialize course variable by checking lesson or lessonModule
        final Course course;
        if (lesson.getCourse() != null) {
            course = lesson.getCourse();
        } else if (lesson.getLessonModule() != null && lesson.getLessonModule().getCourse() != null) {
            course = lesson.getLessonModule().getCourse();
        } else {
            throw new ResourceNotFoundException("Course not associated with the lesson or lesson module");
        }

        // Fetch assignments related to the lesson
        List<Assignment> totalAssignments = assignmentRepo.findByLessonId(lessonId);

        // Fetch submitted assignments for the given lesson and user
        List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findByLessonIdAndStudentIdAndIsSubmittedTrue(lessonId, userId);

        // Calculate the total number of assignments and the number of submitted assignments
        int totalAssignmentsCount = totalAssignments.size();
        int submittedAssignmentsCount = submittedAssignments.size();
        int unsubmittedAssignmentsCount = totalAssignmentsCount - submittedAssignmentsCount;

        // Calculate progress percentage
        double progressPercentage = 0.0;
        if (totalAssignmentsCount > 0) {
            progressPercentage = (double) submittedAssignmentsCount / totalAssignmentsCount * 100;
        }

        // Collect assignment titles, IDs, and their submission status
        List<Map<String, String>> assignmentsWithStatus = totalAssignments.stream().map(assignment -> {
            Map<String, String> details = new HashMap<>();
            details.put("assignmentId", String.valueOf(assignment.getId())); // Correct assignmentId
            details.put("assignmentTitle", assignment.getAssignmentTitle());
            details.put("courseTitle", course.getCourseTitle()); // Use the courseTitle from the appropriate course
            details.put("lessonTitle", lesson.getLessonTitle());

            // Check if the assignment has been submitted using .equals() to compare Long values
            boolean isSubmitted = submittedAssignments.stream()
                    .anyMatch(submitAssignment -> Long.valueOf(submitAssignment.getAssignment().getId())
                            .equals(assignment.getId()));

            details.put("status", isSubmitted ? "Completed" : "Incomplete");

            return details;
        }).collect(Collectors.toList());

        // Set all the calculated data in the response map
        assignmentDetails.put("courseTitle", course.getCourseTitle()); // Set the courseTitle from the determined course
        assignmentDetails.put("lessonTitle", lesson.getLessonTitle());
        assignmentDetails.put("totalAssignmentsCount", totalAssignmentsCount);
        assignmentDetails.put("submittedAssignmentsCount", submittedAssignmentsCount);
        assignmentDetails.put("unsubmittedAssignmentsCount", unsubmittedAssignmentsCount);
        assignmentDetails.put("progressPercentage", progressPercentage); // Include progress percentage
        assignmentDetails.put("assignments", assignmentsWithStatus);

        return assignmentDetails;
    }


    public List<Map<String, Object>> getAssignmentsByTeacherAndCourse(long teacherId) {
        // Fetch assignments by teacherId where course_id is not null, both lesson_id and lesson_module_id are null, and studentIds is null
        List<Assignment> assignments = assignmentRepo.findByTeacherIdAndCourseIdIsNotNullAndLessonIdIsNullAndLessonModuleIdIsNullAndStudentIdsIsNull(teacherId);

        // Create a list of maps to store the required fields
        List<Map<String, Object>> assignmentDetails = new ArrayList<>();

        // Iterate through each assignment and extract the necessary fields
        assignments.forEach(assignment -> {
            Map<String, Object> assignmentMap = new HashMap<>();
            assignmentMap.put("assignmentId", assignment.getId());
            assignmentMap.put("assignmentTitle", assignment.getAssignmentTitle());
            assignmentMap.put("assignmentTopicName", assignment.getAssignmentTopicName());
            assignmentMap.put("assignmentDescription", assignment.getAssignmentDescription());
            assignmentMap.put("maxScore", assignment.getMaxScore());
            assignmentMap.put("startDate", assignment.getStartDate());
            assignmentMap.put("endDate", assignment.getEndDate());
            assignmentMap.put("reviewMeetDate", assignment.getReviewMeetDate());
            assignmentMap.put("assignmentAnswer", assignment.getAssignmentAnswer());
            assignmentMap.put("assignmentDocument", assignment.getAssignmentDocument());

            // Fetch course details if available
            Course course = assignment.getCourse();
            if (course != null) {
                assignmentMap.put("courseId", course.getId());
                assignmentMap.put("courseTitle", course.getCourseTitle());
                assignmentMap.put("courseType", course.getCourseType());
            }

            assignmentDetails.add(assignmentMap);
        });

        return assignmentDetails;
    }

    public Map<String, Object> getAssignmentsDetails(Long lessonId, Long userId) {
        Map<String, Object> assignmentDetails = new HashMap<>();

        // Fetch all assignments for the lesson
        List<Assignment> totalAssignments = assignmentRepo.findByLessonId(lessonId);

        // Fetch all submitted assignments by user for the lesson
        List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findByLessonIdAndUserIdAndIsSubmittedTrue(lessonId, userId);

        // Calculate total and submitted assignment counts
        int totalAssignmentsCount = totalAssignments.size();
        int submittedAssignmentsCount = submittedAssignments.size();
        int unsubmittedAssignmentsCount = totalAssignmentsCount - submittedAssignmentsCount;

        // Calculate progress percentage
        double progressPercentage = 0.0;
        if (totalAssignmentsCount > 0) {
            progressPercentage = (double) submittedAssignmentsCount / totalAssignmentsCount * 100;
        }

        // Collect assignment titles, IDs, and their submission status
        List<Map<String, String>> assignmentsWithStatus = totalAssignments.stream().map(assignment -> {
            Map<String, String> details = new HashMap<>();
            details.put("assignmentId", String.valueOf(assignment.getId()));
            details.put("assignmentTitle", assignment.getAssignmentTitle());

            // Check if the assignment has been submitted using the correct field comparison
            boolean isSubmitted = submittedAssignments.stream()
                    .anyMatch(submitAssignment -> submitAssignment.getAssignment().getId()==(assignment.getId()));

            details.put("status", isSubmitted ? "Completed" : "Incomplete");
            return details;
        }).collect(Collectors.toList());

        // Set all the calculated data in the response map
        assignmentDetails.put("lessonId", lessonId);
        assignmentDetails.put("totalAssignmentsCount", totalAssignmentsCount);
        assignmentDetails.put("submittedAssignmentsCount", submittedAssignmentsCount);
        assignmentDetails.put("unsubmittedAssignmentsCount", unsubmittedAssignmentsCount);
        assignmentDetails.put("progressPercentage", progressPercentage);
        assignmentDetails.put("assignments", assignmentsWithStatus);

        return assignmentDetails;
    }

    public Map<String, Object> getSubAssignmentProgress(Long courseId, Long userId) {
        Map<String, Object> result = new HashMap<>();

        // Check if the user is enrolled in the course
        if (!enrollmentRepo.isUserEnrolledInCourse(userId, courseId)) {
            result.put("message", "User is not enrolled in the course");
            return result;
        }

        // Get total assignments for the course
        long totalAssignments = assignmentRepo.countAssignmentsByCourseId(courseId);

        // Check if there are assignments for the course
        if (totalAssignments == 0) {
            result.put("message", "No assignments found for this course");
            result.put("totalAssignments", totalAssignments);
            result.put("submittedAssignments", 0L);
            result.put("progress", "0/0");  // No assignments, so display "0/0"
            result.put("percentage", 0.0);  // Percentage progress is 0%
            return result;
        }

        // Get the number of submitted assignments by the user for the course
        long submittedAssignments = submitAssignmentRepo.countSubmittedAssignmentsByUserIdAndCourseId(userId, courseId);

        // Calculate progress as a percentage
        double percentageProgress = (double) submittedAssignments / totalAssignments * 100;

        // Return the progress in "submitted/total" format along with percentage
        result.put("totalAssignments", totalAssignments);
        result.put("submittedAssignments", submittedAssignments);
        result.put("total", submittedAssignments + "/" + totalAssignments);  // Format as "submitted/total"
        result.put("percentage", percentageProgress);  // Progress as a percentage

        return result;
    }

    public List<Assignment> getUpcomingAssignmentByUserId(Long userId) {
        LocalDateTime currentDate = LocalDateTime.now();  // Get the current date and time
        return assignmentRepo.findAssignmentsByStartDateAfterAndUserId(currentDate, userId);
    }

    public List<Assignment> getUpcomingAssignmentsForUser(Long userId) {
        return assignmentRepo.findUpcomingAssignmentsByUserId(userId);
    }
    public List<Assignment> getFutureAssignmentsByUserId(Long userId) {
        return assignmentRepo.findFutureAssignmentsByUserId(userId);
    }
}










