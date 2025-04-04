package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private SubmissionRepo submissionRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private  AnswerRepo answerRepo;

    @Autowired
    private  TeacherFeedbackRepo teacherFeedbackRepo;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private AssignmentRepo assignmentRepo;

    @Autowired
    private SubmitProjectRepo submitProjectRepo;

    @Autowired
    private SubmitAssignmentRepo submitAssignmentRepo;

    @Autowired
    private MentorRepo mentorRepo;

    @Autowired
    private MockTestInterviewRepository mockTestInterviewRepo;

    public Project saveProject(Project project, Long courseId) {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            project.setCourse(courseOpt.get());
        } else {
            throw new IllegalArgumentException("Course not found");
        }
        return projectRepo.save(project);
    }

    public Optional<Project> findProjectById(Long id) {
        return projectRepo.findById(id);
    }

    public List<Project> findAllProjects() {
        return projectRepo.findAll();
    }

    public void deleteProject(Long projectId) {
        submissionRepo.deleteByProjectId(projectId);
        projectRepo.deleteById(projectId);
        teacherFeedbackRepo.deleteByProjectId(projectId);
    }

    public List<Project> findProjectsByCourse(Long courseId) {
        return projectRepo.findByCourseId(courseId);
    }

    public List<Submission> getProjectSubmissions(Long projectId) {
        return submissionRepo.findByProjectId(projectId);
    }


    public Optional<Project> getProjectById(long id) {
        return projectRepo.findById(id);
    }

    public Project createProject(Project project) {
        return projectRepo.save(project);
    }

    public Project addProject(Long courseId, MultipartFile projectDocument, String projectTitle, String projectDescription, String projectDeadline) throws IOException {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (!courseOpt.isPresent()) {
            throw new IllegalArgumentException("Course not found");
        }

        Course course = courseOpt.get();
        Project newProject = new Project();
        newProject.setCourse(course);
        newProject.setProjectTitle(projectTitle);
        newProject.setProjectDescription(projectDescription);
        newProject.setProjectDocument(projectDocument.getBytes());
        newProject.setCreatedAt(LocalDateTime.now());
        newProject.setUpdatedAt(LocalDateTime.now());
        newProject.setProjectDeadline(LocalDateTime.now()); // Example deadline logic

        return projectRepo.save(newProject);
    }

    public Project saveProjects(Project existingProject) {
        return projectRepo.save(existingProject);
    }



    public List<Project> patchProjectsByCourseId(long courseId, Map<String, Object> updates) {
        Optional<Course> courseOptional = courseRepo.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            List<Project> projects = projectRepo.findByCourse(course);
            for (Project project : projects) {
                updates.forEach((key, value) -> {
                    switch (key) {
                        case "projectTitle":
                            project.setProjectTitle((String) value);
                            break;
                        case "projectDescription":
                            project.setProjectDescription((String) value);
                            break;
                        case "projectDocument":
                            project.setProjectDocument((byte[]) value);
                            break;
                        case "projectDeadline":
                            project.setProjectDeadline((LocalDateTime) value);
                            break;
                        // Add more cases as needed for other fields
                        default:
                            throw new IllegalArgumentException("Invalid field: " + key);
                    }
                });
                project.setUpdatedAt(LocalDateTime.now());
                projectRepo.save(project);
            }
            return projects;
        }
        return List.of();
    }

    public Project createNewProject(String projectTitle, String projectDescription, Long courseId, LocalDateTime projectDeadline, LocalDateTime startDate, LocalDateTime reviewMeetDate, Long maxTeam, String gitUrl, MultipartFile file) {
        Project project = new Project();
        project.setProjectTitle(projectTitle);
        project.setProjectDescription(projectDescription);
        project.setProjectDeadline(projectDeadline);
        project.setStartDate(startDate);

        project.setReviewMeetDate(reviewMeetDate);
        project.setMaxTeam(maxTeam);
        project.setGitUrl(gitUrl);
        try {
            project.setProjectDocument(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        project.setCourse(courseRepo.findById(courseId).orElse(null));

        Project savedProject = projectRepo.save(project);

        createNotification(savedProject);

        createPendingNotification(savedProject);

        return savedProject;
    }

    public SubmitProject submitProject(Long projectId, Long userId, String userAnswer, MultipartFile file) {
        SubmitProject submitProject = new SubmitProject();
        submitProject.setUser(userRepo.findById(userId).orElse(null));
        Project project = projectRepo.findById(projectId).orElse(null);
        if (project == null) {
            throw new IllegalArgumentException("Invalid project ID: " + projectId);
        }

        submitProject.setProject(project);
        submitProject.setCourse(project.getCourse());
        submitProject.setSubmittedAt(LocalDateTime.now());
        submitProject.setUserAnswer(userAnswer);

        try {
            submitProject.setSubmitProject(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        SubmitProject savedSubmitProject = submitProjectRepo.save(submitProject);

        createCompletionNotification(savedSubmitProject);

        return savedSubmitProject;
    }

    private void createNotification(Project project) {
        if (project.getCourse() != null && project.getCourse().getUser() != null) {
            LocalDateTime notificationDate = project.getProjectDeadline().minusDays(5);
            String content = "Reminder: The project '" + project.getProjectTitle() + "' is due on " + project.getProjectDeadline() + ".";

            Notification notification = new Notification();
            notification.setUser(project.getCourse().getUser()); // Assuming Course has a User
            notification.setContent(content);
            notification.setRead(notification.isRead());
            notification.setTimestamp(notificationDate);

            notificationRepo.save(notification);
        }
    }

    private void createCompletionNotification(SubmitProject submitProject) {
        String content = "Project '" + submitProject.getProject().getProjectTitle() + "' has been submitted.";

        Notification notification = new Notification();
        notification.setUser(submitProject.getUser());
        notification.setContent(content);
        notification.setTimestamp(LocalDateTime.now());

        notificationRepo.save(notification);
    }

    private void createPendingNotification(Project project) {
        if (project.getCourse() != null && project.getCourse().getUser() != null && project.getProjectDeadline().isBefore(LocalDateTime.now()) && !isProjectSubmitted(project.getId())) {
            String content = "Project '" + project.getProjectTitle() + "' is pending and was not submitted on time.";

            Notification notification = new Notification();
            notification.setUser(project.getCourse().getUser()); // Assuming Course has a User
            notification.setContent(content);
            notification.setRead(notification.isRead());
            notification.setTimestamp(LocalDateTime.now());

            notificationRepo.save(notification);
        }
    }

    private boolean isProjectSubmitted(Long projectId) {
        return submitProjectRepo.findByProjectId(projectId).isPresent();
    }

    public List<Project> getProjectsDueInFiveDays() {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime fiveDaysLater = now.plusDays(5);

        return projectRepo.findAll().stream()

                .filter(project -> project.getProjectDeadline() != null && project.getProjectDeadline().isAfter(now) && project.getProjectDeadline().isBefore(fiveDaysLater))

                .collect(Collectors.toList());

    }

    public double calculateUserProgressForCourse(Course course, User user) {
        long totalProjects = projectRepo.findByCourse(course).size();
        long submittedProjects = submitProjectRepo.countByProject_CourseAndUser(course, user);

        if (totalProjects == 0) {
            return 0; // Avoid division by zero
        }

        return (double) submittedProjects / totalProjects * 100;
    }

    public String calculateProgressForCourse(Course course, User user) {
        long totalProjects = projectRepo.findByCourse(course).size();
        long submittedProjects = submitProjectRepo.countByProject_CourseAndUser(course, user);

        return submittedProjects + "/" + totalProjects;
    }

    public String getSubmitProjectFractionByCourseIdAndUserId(Long courseId, Long userId) {
        Course course = new Course();
        course.setId(courseId);
        User user = new User();
        user.setId(userId);

        long submittedCount = submitProjectRepo.countByCourseAndUser(course, user);
        long totalCount = projectRepo.countByCourse(course);

        return submittedCount + "/" + totalCount;
    }

    public List<Project> getUpcomingProjectsForUser(Long userId) {
        return projectRepo.findUpcomingProjectsByUser(userId);
    }

    public List<User> getUsersWhoSubmittedProject(Long projectId) {
        List<Long> userIds = submitProjectRepo.findUserIdsByProjectId(projectId);
        return userRepo.findAllById(userIds);
    }
    public Project createProjectToStudent(long teacherId, long studentId, String projectTitle, String projectDescription, String gitUrl, LocalDateTime projectDeadline, LocalDateTime startDate, LocalDateTime reviewMeetDate, MultipartFile projectDocument) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        User student = userRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        Project project = new Project();
        project.setTeacher(teacher);
        project.setStudent(student);
        project.setProjectTitle(projectTitle);
        project.setProjectDescription(projectDescription);
        project.setGitUrl(gitUrl);
        project.setProjectDeadline(projectDeadline);
        project.setStartDate(startDate);
        project.setReviewMeetDate(reviewMeetDate);

        try {
            project.setProjectDocument(projectDocument.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store project document", e);
        }

        Project savedProjectByTeacher = projectRepo.save(project);

        createNotification(savedProjectByTeacher);

        createPendingNotification(savedProjectByTeacher);
        return savedProjectByTeacher;
    }

    public Project createProjectToStudentByCourse(long teacherId, long studentId, long courseId, String projectTitle, String projectDescription, String gitUrl, LocalDateTime projectDeadline, LocalDateTime startDate, LocalDateTime reviewMeetDate, MultipartFile projectDocument) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        User student = userRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        Project project = new Project();
        project.setTeacher(teacher);
        project.setStudent(student);
        project.setCourse(course);
        project.setProjectTitle(projectTitle);
        project.setProjectDescription(projectDescription);
        project.setGitUrl(gitUrl);
        project.setProjectDeadline(projectDeadline);
        project.setStartDate(startDate);
        project.setReviewMeetDate(reviewMeetDate);

        try {
            project.setProjectDocument(projectDocument.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store project document", e);
        }

        Project savedProjectByTeacherByCourse = projectRepo.save(project);

        createNotification(savedProjectByTeacherByCourse);

        createPendingNotification(savedProjectByTeacherByCourse);
        return savedProjectByTeacherByCourse;
    }

    public Project createProjectByTeacherByCourse(long teacherId, long courseId, String projectTitle, String projectDescription, String gitUrl, LocalDateTime projectDeadline, LocalDateTime startDate, LocalDateTime reviewMeetDate, MultipartFile projectDocument) {
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        Project project = new Project();
        project.setTeacher(teacher);
        project.setCourse(course);
        project.setProjectTitle(projectTitle);
        project.setProjectDescription(projectDescription);
        project.setGitUrl(gitUrl);
        project.setProjectDeadline(projectDeadline);
        project.setStartDate(startDate);
        project.setReviewMeetDate(reviewMeetDate);

        try {
            project.setProjectDocument(projectDocument.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store project document", e);
        }

        Project savedProjectByTeacherAndCourse = projectRepo.save(project);

        createNotification(savedProjectByTeacherAndCourse);

        createPendingNotification(savedProjectByTeacherAndCourse);
        return savedProjectByTeacherAndCourse;
    }

    public List<Project> getProjectsByTeacherId(Long teacherId) {
        return projectRepo.findProjectsByTeacherId(teacherId);
    }

    public Optional<Project> getProjectByIdAndTeacherId(Long id, Long teacherId) {
        return projectRepo.findById(id)
                .filter(project -> project.getTeacher().getId() == teacherId);
    }

    public Project updateProjectFields(Long id, Long teacherId,
                                       String projectTitle, String projectDescription, byte[] projectDocument,
                                       LocalDateTime projectDeadline, LocalDateTime reviewMeetDate, Long maxTeam, String gitUrl) {
        Optional<Project> optionalProject = getProjectByIdAndTeacherId(id, teacherId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();

            if (projectTitle != null) {
                project.setProjectTitle(projectTitle);
            }
            if (projectDescription != null) {
                project.setProjectDescription(projectDescription);
            }
            if (projectDocument != null) {
                project.setProjectDocument(projectDocument);
            }
            if (projectDeadline != null) {
                project.setProjectDeadline(projectDeadline);
            }
            if (reviewMeetDate != null) {
                project.setReviewMeetDate(reviewMeetDate);
            }
            if (maxTeam != null) {
                project.setMaxTeam(maxTeam);
            }
            if (gitUrl != null) {
                project.setGitUrl(gitUrl);
            }

            project.setUpdatedAt(LocalDateTime.now());
            return projectRepo.save(project);
        } else {
            throw new IllegalArgumentException("Project not found for id: " + id + " and teacherId: " + teacherId);
        }
    }

    public List<Project> getProjectsByCourseIdAndTeacherId(Long courseId, Long teacherId) {
        return projectRepo.findByCourseIdAndTeacherId(courseId, teacherId);
    }

    public List<Project> getProjectsByCourseIdAndStudentId(Long courseId, Long studentId) {
        return projectRepo.findByCourseIdAndStudentId(courseId, studentId);
    }

    public List<Project> getProjectsByUserAndCourse(Long userId, Long courseId) {
        return projectRepo.findByUserIdAndCourseId(userId, courseId);
    }

public Map<String, Object> calculateProgress(Long studentId, Long courseId) {
    List<Project> totalProjects = projectRepo.findByStudent_IdAndCourse_Id(studentId, courseId);
    List<SubmitProject> submittedProjects = submitProjectRepo.findByStudent_IdAndCourse_Id(studentId, courseId);

    int totalProjectsCount = totalProjects.size();
    int submittedProjectsCount = submittedProjects.size();

    double progressPercentage = totalProjectsCount == 0 ? 0 : ((double) submittedProjectsCount / totalProjectsCount) * 100;

    String courseTitle = courseRepo.findById(courseId)
            .map(Course::getCourseTitle)
            .orElse("Unknown Course");

    // Create a map to store the submission status of each project
    Map<Long, Boolean> projectSubmissionStatus = submittedProjects.stream()
            .collect(Collectors.toMap(
                    submission -> submission.getProject().getId(),
                    submission -> true
            ));

    List<Map<String, Object>> projectDetails = totalProjects.stream()
            .map(project -> {
                Map<String, Object> details = new HashMap<>();
                details.put("projectTitle", project.getProjectTitle());
                details.put("status", projectSubmissionStatus.containsKey(project.getId()) ? "Completed" : "Not Completed");
                return details;
            })
            .collect(Collectors.toList());

    Long teacherId = null;
    String teacherName = "Unknown";
    if (!totalProjects.isEmpty() && totalProjects.get(0).getTeacher() != null) {
        User teacher = totalProjects.get(0).getTeacher();
        teacherId = teacher.getId(); // Assuming User entity has a method getId
        teacherName = teacher.getName(); // Assuming User entity has a method getName
    }

    Map<String, Object> progressData = new HashMap<>();
    progressData.put("totalProjects", totalProjectsCount);
    progressData.put("submittedProjects", submittedProjectsCount);
    progressData.put("progressPercentage", progressPercentage);
    progressData.put("courseTitle", courseTitle);
    progressData.put("projects", projectDetails);
    progressData.put("teacherId", teacherId);
    progressData.put("teacherName", teacherName);

    return progressData;
}



    @Transactional
    public SubmitProject submitNewProject(Long projectId, Long userId, String userAnswer, MultipartFile file) {
        if (submitProjectRepo.existsByUserIdAndProjectId(userId, projectId)) {
            throw new IllegalStateException("Project already submitted");
        }

        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));

        SubmitProject submitProject = new SubmitProject();
        submitProject.setUser(user);
        submitProject.setProject(project);
        submitProject.setCourse(project.getCourse());
        submitProject.setSubmittedAt(LocalDateTime.now());
        submitProject.setUserAnswer(userAnswer);

        try {
            submitProject.setSubmitProject(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }

        submitProject.setSubmitted(true);

        SubmitProject savedSubmitProject = submitProjectRepo.save(submitProject);

        createCompletionNotification(savedSubmitProject);

        return savedSubmitProject;
    }

    @Transactional
    public void submitNewProjectByStudent(Long studentId, Long courseId, Long projectId, MultipartFile submitProject, String userAnswer) throws IOException {
        if (submitProjectRepo.existsByUserIdAndProjectId(studentId, projectId)) {
            throw new IllegalStateException("Project already submitted");
        }

        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        SubmitProject newSubmission = new SubmitProject();
        newSubmission.setSubmitProject(submitProject.getBytes());
        newSubmission.setUserAnswer(userAnswer);
        newSubmission.setStudent(student);
        newSubmission.setCourse(course);
        newSubmission.setProject(project);
        newSubmission.setSubmittedAt(LocalDateTime.now());
        newSubmission.setSubmitted(true);

        submitProjectRepo.save(newSubmission);

        createCompletionNotification(newSubmission);
    }


    public List<Map<String, Object>> getProjectsByTeacherIdAndStudentId(long teacherId, long studentId) {
        List<Object[]> results = projectRepo.getProjectsByTeacherIdAndStudentId(teacherId, studentId);
        return results.stream()
                .map(result -> Map.of(
                        "projectTitle", result[0],
                        "studentId", result[1],
                        "studentName", result[2],
                        "submitProject", result[3]  // Assuming this is the document field
                ))
                .collect(Collectors.toList());
    }



    public Map<String, Object> getProjectDetailsByCourseIdAndUserId(Long courseId, Long userId) {
        Map<String, Object> projectDetails = new HashMap<>();

        // Fetch projects by course ID
        List<Project> projects = projectRepo.findByCourseId(courseId);

        if (projects.isEmpty()) {
            throw new ResourceNotFoundException("No projects found for the given course ID");
        }

        List<Map<String, Object>> projectInfoList = new ArrayList<>();
        int totalProjects = projects.size();
        int completedProjects = 0;

        for (Project project : projects) {
            Map<String, Object> projectInfo = new HashMap<>();
            projectInfo.put("projectId", project.getId()); // Add projectId to the response
            projectInfo.put("courseTitle", project.getCourse().getCourseTitle());
            projectInfo.put("projectTitle", project.getProjectTitle());

            // Check submission status
            boolean isSubmitted = submitProjectRepo.existsByProjectIdAndUserIdAndIsSubmitted(project.getId(), userId, true);
            projectInfo.put("status", isSubmitted ? "completed" : "not completed");

            if (isSubmitted) {
                completedProjects++;
            }

            projectInfoList.add(projectInfo);
        }

        // Calculate progress
        double progress = (totalProjects == 0) ? 0 : (double) completedProjects / totalProjects * 100;

        projectDetails.put("projects", projectInfoList);
        projectDetails.put("progress", progress); // Add progress to the response

        return projectDetails;
    }

    public Project createProject(long courseId, List<Long> studentIds, String projectTitle, String projectDescription, long teacherId, MultipartFile projectDocument, LocalDateTime projectDeadline, LocalDateTime startDate, LocalDateTime reviewMeetDate, Long maxTeam, String gitUrl) {
        // Fetch course and teacher
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        User teacher = userRepo.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Fetch students
        List<User> students = userRepo.findAllById(studentIds);

        // Create new project
        Project project = new Project();
        project.setCourse(course);
        project.setTeacher(teacher);
        project.setProjectTitle(projectTitle);
        project.setProjectDescription(projectDescription);
        project.setStudentIds(studentIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        project.setProjectDeadline(projectDeadline);
        project.setStartDate(startDate);
        project.setReviewMeetDate(reviewMeetDate);
        project.setMaxTeam(maxTeam);
        project.setGitUrl(gitUrl);

        // Handle file upload
        try {
            if (projectDocument != null && !projectDocument.isEmpty()) {
                project.setProjectDocument(projectDocument.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store project document", e);
        }

        return projectRepo.save(project);
    }

    public int getStudentCountByProjectAndTeacher(long projectId, long teacherId) {
        // Fetch the project by ID and teacher ID
        Project project = projectRepo.findByIdAndTeacherId(projectId, teacherId);

        if (project != null && project.getStudentIds() != null) {
            // Split the studentIds string by comma and count the number of elements
            String[] studentIdsArray = project.getStudentIds().split(",");
            return studentIdsArray.length;
        }
        return 0;
    }

    public Project updateProject(long projectId,
                                 String projectTitle,
                                 String projectDescription,
                                 Long teacherId,
                                 MultipartFile projectDocument,
                                 LocalDateTime projectDeadline,
                                 LocalDateTime startDate,
                                 LocalDateTime reviewMeetDate,
                                 Long maxTeam,
                                 String gitUrl) {
        // Fetch the existing project
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        // Update fields if they are provided
        if (projectTitle != null) {
            project.setProjectTitle(projectTitle);
        }
        if (projectDescription != null) {
            project.setProjectDescription(projectDescription);
        }
        if (teacherId != null) {
            User teacher = userRepo.findById(teacherId).orElse(null);
            project.setTeacher(teacher);
        }
        if (projectDocument != null) {
            try {
                project.setProjectDocument(projectDocument.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process project document", e);
            }
        }
        if (projectDeadline != null) {
            project.setProjectDeadline(projectDeadline);
        }
        if (startDate != null) {
            project.setStartDate(startDate);
        }
        if (reviewMeetDate != null) {
            project.setReviewMeetDate(reviewMeetDate);
        }
        if (maxTeam != null) {
            project.setMaxTeam(maxTeam);
        }
        if (gitUrl != null) {
            project.setGitUrl(gitUrl);
        }

        // Save and return the updated project
        return projectRepo.save(project);
    }

    public Map<String, Object> getCourseAndStudentDetailsByProjectId(long projectId) {
        // Fetch project details
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Fetch course details
        Course course = project.getCourse();

        // Fetch student details
        List<Long> studentIds = Arrays.asList(project.getStudentIds().split(","))
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<User> students = userRepo.findAllById(studentIds);

        // Prepare response map
        Map<String, Object> response = new HashMap<>();
        response.put("course", course);
        response.put("students", students);

        return response;
    }

    public String createProjectByMock(String projectTitle, String projectDescription,
                                      MultipartFile projectDocument, Long mockId,
                                      Long teacherId) throws IOException {

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

        // Check if a project already exists with the same mockId
        Optional<Project> existingProjectOptional = projectRepo.findByMockTestInterviewId(mockId);
        if (existingProjectOptional.isPresent()) {
            return "A project with the specified mockId already exists.";
        }

        // Create and save the project
        Project project = new Project();
        project.setProjectTitle(projectTitle);
        project.setProjectDescription(projectDescription);
        project.setProjectDocument(projectDocument.getBytes());
        project.setMockTestInterview(mockTestInterview);
        project.setTeacher(teacher);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        projectRepo.save(project);

        return "Project created successfully.";
    }


    public Map<String, Object> getSubmittedAssignmentsAndProjects(Long teacherId, Long mockId) {
        // Fetch submitted assignments with student names
        List<Map<String, Object>> submittedAssignments = submitAssignmentRepo.findSubmissionsByTeacherIdAndMockId(teacherId, mockId);

        // Fetch submitted projects with student names
        List<Map<String, Object>> submittedProjects = submitProjectRepo.findSubmissionsByTeacherIdAndMockId(teacherId, mockId);

        // Prepare the response map
        Map<String, Object> response = new HashMap<>();
        response.put("submittedAssignments", submittedAssignments);
        response.put("submittedProjects", submittedProjects);

        return response;
    }

//    public Map<String, Object> getSubmittedAssignmentAndProject(Long teacherId) {
//        // Fetch all feedback entries related to assignments and projects for this teacher
//        List<Long> feedbackAssignmentIds = teacherFeedbackRepo.findAssignmentIdsByTeacherId(teacherId);
//        List<Long> feedbackProjectIds = teacherFeedbackRepo.findProjectIdsByTeacherId(teacherId);
//
//        // Fetch all submitted assignments related to the teacher's mock tests
//        List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findAllByTeacherId(teacherId);
//
//        // Map the submitted assignments to the desired output format, excluding those with feedback
//        List<Map<String, Object>> assignmentDetails = submittedAssignments.stream()
//                .filter(sa -> !feedbackAssignmentIds.contains(sa.getAssignment().getId()))  // Exclude assignments with feedback
//                .map(sa -> {
//                    Map<String, Object> submissionDetails = new HashMap<>();
//                    submissionDetails.put("submitAssignment", sa.getSubmitAssignment());
//                    submissionDetails.put("userAssignmentAnswer", sa.getUserAssignmentAnswer());
//                    submissionDetails.put("studentName", sa.getStudent().getName());
//                    submissionDetails.put("submittedAt", sa.getSubmittedAt());
//                    submissionDetails.put("mock_id", sa.getMockTestInterview().getId());
//                    submissionDetails.put("user_id", sa.getStudent().getId());
//                    submissionDetails.put("assignment_id", sa.getAssignment().getId());
//                    return submissionDetails;
//                })
//                .toList();
//
//        // Fetch all submitted projects related to the teacher's mock tests
//        List<SubmitProject> submittedProjects = submitProjectRepo.findAllByTeacherId(teacherId);
//
//        // Map the submitted projects to the desired output format, excluding those with feedback
//        List<Map<String, Object>> projectDetails = submittedProjects.stream()
//                .filter(sp -> !feedbackProjectIds.contains(sp.getProject().getId()))  // Exclude projects with feedback
//                .map(sp -> {
//                    Map<String, Object> submissionDetails = new HashMap<>();
//                    submissionDetails.put("submitProject", sp.getSubmitProject());
//                    submissionDetails.put("userAnswer", sp.getUserAnswer());
//                    submissionDetails.put("studentName", sp.getStudent().getName());
//                    submissionDetails.put("submittedAt", sp.getSubmittedAt());
//                    submissionDetails.put("mock_id", sp.getMockTestInterview().getId());
//                    submissionDetails.put("user_id", sp.getStudent().getId());
//                    submissionDetails.put("project_id", sp.getProject().getId());
//                    return submissionDetails;
//                })
//                .toList();
//
//        // Prepare the response map
//        Map<String, Object> response = new HashMap<>();
//        response.put("submittedAssignments", assignmentDetails);
//        response.put("submittedProjects", projectDetails);
//
//        return response;
//    }


    public List<Map<String, Object>> getProjectsByTeacherAndCourse(long teacherId) {
        // Fetch projects where course_id is not null and student_ids is null, filtered by teacherId
        List<Project> projects = projectRepo.findByTeacherIdAndCourseIdIsNotNullAndStudentIdsIsNull(teacherId);

        // Create a list to store the project and course details
        List<Map<String, Object>> projectDetails = new ArrayList<>();

        // Iterate through each project and extract the necessary fields
        projects.forEach(project -> {
            Map<String, Object> projectMap = new HashMap<>();
            projectMap.put("projectId", project.getId());
            projectMap.put("projectTitle", project.getProjectTitle());
            projectMap.put("projectDescription", project.getProjectDescription());
            projectMap.put("projectDocument", project.getProjectDocument());
            projectMap.put("projectDeadline", project.getProjectDeadline());
            projectMap.put("startDate", project.getStartDate());
            projectMap.put("reviewMeetDate", project.getReviewMeetDate());

            // Fetch course details from the project
            Course course = project.getCourse();
            if (course != null) {
                projectMap.put("courseId", course.getId());
                projectMap.put("courseTitle", course.getCourseTitle());
                projectMap.put("courseType", course.getCourseType());
            }

            projectDetails.add(projectMap);
        });

        return projectDetails;
    }
    public Optional<Project> getProjectById(Long projectId) {
        return projectRepo.findById(projectId);
    }

    // Method to save the project after making updates
    public Project saveProjectsBy(Project project) {
        return projectRepo.save(project);
    }

    // Method to update project details
    public Project updateProjectBy(long projectId,
                                   String projectTitle,
                                   String projectDescription,
                                   LocalDateTime projectDeadline,
                                   LocalDateTime reviewMeetDate,
                                   Long maxTeam,
                                   String gitUrl,
                                   MultipartFile projectDocument) throws IOException {

        // Fetching the project by ID
        Optional<Project> existingProjectOptional = getProjectById(projectId);

        // Checking if project exists
        if (existingProjectOptional.isPresent()) {
            Project existingProject = existingProjectOptional.get();

            // Update project title if provided
            if (projectTitle != null) {
                existingProject.setProjectTitle(projectTitle);
            }

            // Update project description if provided
            if (projectDescription != null) {
                existingProject.setProjectDescription(projectDescription);
            }

            // Update project deadline if provided
            if (projectDeadline != null) {
                existingProject.setProjectDeadline(projectDeadline);
            }

            // Update review meeting date if provided
            if (reviewMeetDate != null) {
                existingProject.setReviewMeetDate(reviewMeetDate);
            }

            // Update max team size if provided
            if (maxTeam != null) {
                existingProject.setMaxTeam(maxTeam);
            }

            // Update Git URL if provided
            if (gitUrl != null) {
                existingProject.setGitUrl(gitUrl);
            }

            // Update project document if provided
            if (projectDocument != null && !projectDocument.isEmpty()) {
                byte[] documentBytes = projectDocument.getBytes();
                existingProject.setProjectDocument(documentBytes);
            }

            // Save the updated project back to the database
            return saveProjects(existingProject);
        } else {
            // Handle the case where project was not found
            throw new IllegalArgumentException("Project not found with id: " + projectId);
        }
    }

    public List<Project> getProjectsByCourseId(Long courseId) {
        return projectRepo.findByCourseId(courseId);
    }

//    public Map<String, List<Map<String, Object>>> getSubmittedAssignmentAndProject(Long teacherId) {
//        System.out.println("Fetching submissions for teacher ID: " + teacherId);
//
//        // Fetch assignments and projects with feedback
//        List<Long> feedbackAssignmentIds = teacherFeedbackRepo.findAssignmentIdsByTeacherId(teacherId);
//        List<Long> feedbackProjectIds = teacherFeedbackRepo.findProjectIdsByTeacherId(teacherId);
//
//        System.out.println("Feedback Assignment IDs: " + feedbackAssignmentIds);
//        System.out.println("Feedback Project IDs: " + feedbackProjectIds);
//
//        // Fetch submitted assignments for the teacher
//        List<SubmitAssignment> submittedAssignments = submitAssignmentRepo.findAllByTeacherId(teacherId);
//        System.out.println("Found " + submittedAssignments.size() + " submitted assignments");
//
//        // Ensure assignments are not filtered out incorrectly
//        List<Map<String, Object>> assignmentDetails = submittedAssignments.stream()
//                .map(sa -> {
//                    Map<String, Object> submissionDetails = new HashMap<>();
//                    submissionDetails.put("submitAssignment", sa.getSubmitAssignment());
//                    submissionDetails.put("userAssignmentAnswer", sa.getUserAssignmentAnswer());
//                    submissionDetails.put("studentName", sa.getStudent() != null ? sa.getStudent().getName() : "Unknown");
//                    submissionDetails.put("submittedAt", sa.getSubmittedAt());
//                    submissionDetails.put("mock_id", sa.getMockTestInterview() != null ? sa.getMockTestInterview().getId() : null);
//                    submissionDetails.put("user_id", sa.getStudent() != null ? sa.getStudent().getId() : null);
//                    submissionDetails.put("assignment_id", sa.getAssignment() != null ? sa.getAssignment().getId() : null);
//
//                    boolean hasFeedback = sa.getAssignment() != null && feedbackAssignmentIds.contains(sa.getAssignment().getId());
//                    submissionDetails.put("hasFeedback", hasFeedback);
//
//                    return submissionDetails;
//                })
//                .collect(Collectors.toList());
//
//        // Fetch submitted projects for the teacher
//        List<SubmitProject> submittedProjects = submitProjectRepo.findAllByTeacherId(teacherId);
//        System.out.println("Found " + submittedProjects.size() + " submitted projects");
//
//        // Ensure projects are not filtered out incorrectly
//        List<Map<String, Object>> projectDetails = submittedProjects.stream()
//                .map(sp -> {
//                    Map<String, Object> submissionDetails = new HashMap<>();
//                    submissionDetails.put("submitProject", sp.getSubmitProject());
//                    submissionDetails.put("userAnswer", sp.getUserAnswer());
//                    submissionDetails.put("studentName", sp.getStudent() != null ? sp.getStudent().getName() : "Unknown");
//                    submissionDetails.put("submittedAt", sp.getSubmittedAt());
//                    submissionDetails.put("mock_id", sp.getMockTestInterview() != null ? sp.getMockTestInterview().getId() : null);
//                    submissionDetails.put("user_id", sp.getStudent() != null ? sp.getStudent().getId() : null);
//                    submissionDetails.put("project_id", sp.getProject() != null ? sp.getProject().getId() : null);
//
//                    boolean hasFeedback = sp.getProject() != null && feedbackProjectIds.contains(sp.getProject().getId());
//                    submissionDetails.put("hasFeedback", hasFeedback);
//
//                    return submissionDetails;
//                })
//                .filter(submission -> !(boolean) submission.get("hasFeedback")) // Exclude only if it has feedback
//                .collect(Collectors.toList());
//
//        // Prepare the response
//        Map<String, List<Map<String, Object>>> response = new HashMap<>();
//        response.put("submittedAssignments", assignmentDetails);
//        response.put("submittedProjects", projectDetails);
//
//        System.out.println("Returning " + assignmentDetails.size() + " assignments and " + projectDetails.size() + " projects");
//
//        return response;
//    }
}
