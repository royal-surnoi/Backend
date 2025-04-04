package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.ProjectRepo;
import fusionIQ.AI.V2.fusionIq.repository.SubmitProjectRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/project")
public class ProjectController implements Serializable {


    @Autowired
    private ProjectService projectService;

    @Autowired
    private SubmitAssignmentService submitAssignmentService;

    @Autowired
    private SubmitProjectService submitProjectService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    CourseRepo courseRepo;
    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    SubmitProjectRepo submitProjectRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AssignmentService assignmentService;



    @PostMapping("/saveProject/{courseId}")
    public ResponseEntity<Project> createProject(@RequestBody Project project, @PathVariable Long courseId) {
        try {
            Project savedProject = projectService.saveProject(project, courseId);
            return ResponseEntity.ok(savedProject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/addProject/{courseId}")
    public ResponseEntity<Project> addProject(@PathVariable Long courseId,
                                              @RequestParam("file") MultipartFile projectDocument,
                                              @RequestParam String projectTitle,
                                              @RequestParam String projectDescription,
                                              @RequestParam String projectDeadline) throws IOException, IOException {
        Project newProject = projectService.addProject(courseId, projectDocument, projectTitle, projectDescription, projectDeadline);
        return new ResponseEntity<>(newProject, HttpStatus.CREATED);
    }

    @GetMapping("/getBy/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Optional<Project> projectOpt = projectService.findProjectById(id);
        return projectOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/allProjects")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.findAllProjects();
        return ResponseEntity.ok(projects);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Project>> getProjectsByCourse(@PathVariable Long courseId) {
        List<Project> projects = projectService.findProjectsByCourse(courseId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/submissions/{projectId}")
    public ResponseEntity<List<Submission>> getProjectSubmissions(@PathVariable Long projectId) {
        List<Submission> submissions = projectService.getProjectSubmissions(projectId);
        return ResponseEntity.ok(submissions);
    }
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<Project> updateCourse(@PathVariable("id") long id, @RequestBody Project updatedProject) {
//        Optional<Project> existingProjectOptional = projectService.getProjectById(id);
//        if (existingProjectOptional.isPresent()) {
//            Project existingCourse = existingProjectOptional.get();
//
//            if (updatedProject.getProjectTitle() != null) {
//                existingCourse.setProjectTitle(updatedProject.getProjectTitle());
//            }
//            if (updatedProject.getProjectDescription() != null) {
//                existingCourse.setProjectDescription(updatedProject.getProjectDescription());
//            }
//
//
//            Project savedCourse = projectService.savingProject(existingCourse);
//            return ResponseEntity.ok(savedCourse);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PostMapping("/add/{userId}/{courseId}")
    public Project addNewProject(@PathVariable Long userId,
                                 @PathVariable Long courseId,
                                 @RequestParam("file") MultipartFile projectDocument,
                                 @RequestParam String projectTitle,
                                 @RequestParam String projectDescription) throws IOException {
        Optional<User> user = userRepo.findById(userId);
        Optional<Course> course = courseRepo.findById(courseId);

        if (user.isPresent() && course.isPresent()) {
            Project project = new Project();
            project.setProjectTitle(projectTitle);
            project.setProjectDocument(projectDocument.getBytes());
            project.setProjectDescription(projectDescription);
            return projectRepo.save(project);
        } else {
            throw new RuntimeException("userId or courseId not found");
        }
    }
    @PutMapping("/updateProjects/{courseId}")
    public ResponseEntity<Project> updateProject(
            @PathVariable long courseId,
            @RequestParam(required = false) String projectTitle,
            @RequestParam(required = false) String projectDescription,
            @RequestParam(required = false) LocalDateTime projectDeadline,
            @RequestParam(required = false) LocalDateTime reviewMeetDate,
            @RequestParam(required = false) Long maxTeam,
            @RequestParam(required = false) String gitUrl,
            @RequestParam(required = false) MultipartFile projectDocument) {

        Optional<Project> existingProjectOptional = projectService.getProjectById(courseId);
        if (existingProjectOptional.isPresent()) {
            Project existingProject = existingProjectOptional.get();

            if (projectTitle != null) {
                existingProject.setProjectTitle(projectTitle);
            }

            if (projectDescription != null) {
                existingProject.setProjectDescription(projectDescription);
            }

            if (projectDeadline != null) {
                existingProject.setProjectDeadline(projectDeadline);
            }
            if (reviewMeetDate != null) {
                existingProject.setReviewMeetDate(reviewMeetDate);
            }
            if (maxTeam != null) {
                existingProject.setMaxTeam(maxTeam);
            }
            if (gitUrl != null) {
                existingProject.setGitUrl(gitUrl);
            }

            if (projectDocument != null && !projectDocument.isEmpty()) {
                try {
                    byte[] documentBytes = projectDocument.getBytes();
                    existingProject.setProjectDocument(documentBytes);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }

            Project savedProject = projectService.saveProjects(existingProject);
            return ResponseEntity.ok(savedProject);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/course/{courseId}")
    public ResponseEntity<List<Project>> patchProjectsByCourseId(
            @PathVariable long courseId,
            @RequestBody Map<String, Object> updates) {

        List<Project> updatedProjects = projectService.patchProjectsByCourseId(courseId, updates);

        if (!updatedProjects.isEmpty()) {
            return ResponseEntity.ok(updatedProjects);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PostMapping("/saveNewProject")
    public ResponseEntity<Project> createNewProject(
            @RequestParam("projectTitle") String projectTitle,
            @RequestParam("projectDescription") String projectDescription,
            @RequestParam("courseId") Long courseId,
            @RequestParam("projectDeadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime projectDeadline,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("reviewMeetDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reviewMeetDate,
            @RequestParam("maxTeam") Long maxTeam,
            @RequestParam("gitUrl") String gitUrl,
            @RequestParam("file") MultipartFile projectDocument) {

        Project createdProject = projectService.createNewProject(projectTitle, projectDescription, courseId, projectDeadline, startDate,  reviewMeetDate,maxTeam,gitUrl, projectDocument);
        return ResponseEntity.ok(createdProject);
    }

    @PostMapping("/submitProject")
    public ResponseEntity<SubmitProject> submitProject(
            @RequestParam("projectId") Long projectId,
            @RequestParam("userId") Long userId,
            @RequestParam("userAnswer") String userAnswer,
            @RequestParam("file") MultipartFile submitProjectDocument) {

        SubmitProject submittedProject = projectService.submitProject(projectId, userId,userAnswer, submitProjectDocument);
        return ResponseEntity.ok(submittedProject);
    }
    @GetMapping("/due-in-five-days")

    public List<Project> getProjectsDueInFiveDays() {

        return projectService.getProjectsDueInFiveDays(); }

    @GetMapping("/projectProgress/{courseId}/user/{userId}")
    public double getUserProgressForCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        Course course = new Course();
        course.setId(courseId); // Assuming Course has a setter for ID

        User user = new User();
        user.setId(userId); // Assuming User has a setter for ID

        return projectService.calculateUserProgressForCourse(course, user);
    }

    @GetMapping("/progress/{courseId}/user/{userId}")
    public String getSubmittedByTotalProjects(@PathVariable Long courseId, @PathVariable Long userId) {
        Course course = new Course();
        course.setId(courseId); // Assuming Course has a setter for ID

        User user = new User();
        user.setId(userId); // Assuming User has a setter for ID

        return projectService.calculateProgressForCourse(course, user);
    }

    @GetMapping("/fraction")
    public String getSubmitProjectFractionByCourseIdAndUserId(@RequestParam Long courseId, @RequestParam Long userId) {
        return projectService.getSubmitProjectFractionByCourseIdAndUserId(courseId, userId);
    }


    @PostMapping("/createProjectToStudent/{teacherId}/{studentId}")
    public Project createProject(
            @PathVariable long teacherId,
            @PathVariable long studentId,
            @RequestParam String projectTitle,
            @RequestParam String projectDescription,
            @RequestParam String gitUrl,
            @RequestParam LocalDateTime projectDeadline,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime reviewMeetDate,
            @RequestParam("projectDocument") MultipartFile projectDocument) {

        return projectService.createProjectToStudent(teacherId, studentId, projectTitle, projectDescription, gitUrl, projectDeadline, startDate, reviewMeetDate, projectDocument);
    }

    @PostMapping("/createByTeacherByCourse/{teacherId}/{studentId}/{courseId}")
    public Project createProject(
            @PathVariable long teacherId,
            @PathVariable long studentId,
            @PathVariable long courseId,
            @RequestParam String projectTitle,
            @RequestParam String projectDescription,
            @RequestParam String gitUrl,
            @RequestParam LocalDateTime projectDeadline,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime reviewMeetDate,
            @RequestParam("projectDocument") MultipartFile projectDocument) {

        return projectService.createProjectToStudentByCourse(teacherId, studentId, courseId, projectTitle, projectDescription, gitUrl, projectDeadline, startDate, reviewMeetDate, projectDocument);
    }
    @GetMapping("/{projectId}/submittedUsers")
    public ResponseEntity<List<User>> getUsersWhoSubmittedProject(@PathVariable Long projectId) {
        try {
            List<User> users = projectService.getUsersWhoSubmittedProject(projectId);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createByTeacherByCourse/{teacherId}/{courseId}")
    public Project createProjectByTeacherAndCourse(
            @PathVariable long teacherId,
            @PathVariable long courseId,
            @RequestParam String projectTitle,
            @RequestParam String projectDescription,
            @RequestParam String gitUrl,
            @RequestParam LocalDateTime projectDeadline,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime reviewMeetDate,
            @RequestParam("projectDocument") MultipartFile projectDocument) {

        return projectService.createProjectByTeacherByCourse(teacherId, courseId, projectTitle, projectDescription, gitUrl, projectDeadline, startDate, reviewMeetDate, projectDocument);
    }

    @GetMapping("/projectByTeacher/{teacherId}")
    public List<Project> getProjectsByTeacherId(@PathVariable Long teacherId) {
        return projectService.getProjectsByTeacherId(teacherId);
    }


    @PutMapping("/{teacherId}/{id}/updateDetails")
    public Project updateProjectDetailsByIdAndTeacherId(
            @PathVariable Long teacherId,
            @PathVariable Long id,
            @RequestParam(required = false) String projectTitle,
            @RequestParam(required = false) String projectDescription,
            @RequestParam(required = false) byte[] projectDocument,
            @RequestParam(required = false) LocalDateTime projectDeadline,
            @RequestParam(required = false) LocalDateTime reviewMeetDate,
            @RequestParam(required = false) Long maxTeam,
            @RequestParam(required = false) String gitUrl
    ) {
        return projectService.updateProjectFields(id, teacherId, projectTitle, projectDescription, projectDocument,
                projectDeadline, reviewMeetDate, maxTeam, gitUrl);
    }

    @GetMapping("/course/{courseId}/teacher/{teacherId}")
    public List<Project> getProjectsByCourseIdAndTeacherId(
            @PathVariable Long courseId,
            @PathVariable Long teacherId) {
        return projectService.getProjectsByCourseIdAndTeacherId(courseId, teacherId);
    }

    @GetMapping("/course/{courseId}/student/{studentId}")
    public List<Project> getProjectsByCourseIdAndStudentId(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        return projectService.getProjectsByCourseIdAndStudentId(courseId, studentId);
    }

    @GetMapping("/byUserAndCourse/{userId}/{courseId}")
    public List<Project> getProjectsByUserAndCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId) {
        return projectService.getProjectsByUserAndCourse(userId, courseId);
    }

    @GetMapping("/projectProgressByStudentId")
    public ResponseEntity<Map<String, Object>> getIndividualStudentProgress(@RequestParam Long studentId, @RequestParam Long courseId) {
        Map<String, Object> progress = projectService.calculateProgress(studentId, courseId);
        return new ResponseEntity<>(progress, HttpStatus.OK);
    }

    @PostMapping("/submitNewProject")
    public ResponseEntity<SubmitProject> submitNewProject(
            @RequestParam("projectId") Long projectId,
            @RequestParam("userId") Long userId,
            @RequestParam("userAnswer") String userAnswer,
            @RequestParam("file") MultipartFile submitProjectDocument) {

        if (submitProjectRepo.existsByUserIdAndProjectId(userId, projectId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        SubmitProject submittedProject = projectService.submitNewProject(projectId, userId, userAnswer, submitProjectDocument);

        return ResponseEntity.ok(submittedProject);
    }

    @PostMapping("/submitNewProjectByStudentId")
    public ResponseEntity<String> submitNewProjectByStudent(@RequestParam Long studentId,
                                                            @RequestParam Long courseId,
                                                            @RequestParam Long projectId,
                                                            @RequestParam("file") MultipartFile submitProject,
                                                            @RequestParam(required = false) String userAnswer) {
        try {
            if (submitProjectRepo.existsByUserIdAndProjectId(studentId, projectId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Project already submitted");
            }

            projectService.submitNewProjectByStudent(studentId, courseId, projectId, submitProject, userAnswer);
            return ResponseEntity.ok("Project submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit project: " + e.getMessage());
        }
    }



    @GetMapping("/get/mentor/{teacherId}/student/{studentId}/projects")
    public List<Map<String, Object>> getProjectsByTeacherIdAndStudentId(@PathVariable long teacherId, @PathVariable long studentId) {
        return projectService.getProjectsByTeacherIdAndStudentId(teacherId, studentId);
    }


    @GetMapping("/details/byCourse/{courseId}")
    public ResponseEntity<Map<String, Object>> getProjectDetailsByCourseIdAndUserId(
            @PathVariable Long courseId, @RequestParam Long userId) {
        Map<String, Object> projectDetails = projectService.getProjectDetailsByCourseIdAndUserId(courseId, userId);
        return ResponseEntity.ok(projectDetails);
    }

    @PostMapping("/createMultipleProjects")
    public ResponseEntity<Project> createProject(
            @RequestParam long courseId,
            @RequestParam List<Long> studentIds,
            @RequestParam String projectTitle,
            @RequestParam String projectDescription,
            @RequestParam long teacherId,
            @RequestParam MultipartFile projectDocument,
            @RequestParam LocalDateTime projectDeadline,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime reviewMeetDate,
            @RequestParam(required = false) Long maxTeam,
            @RequestParam(required = false) String gitUrl) {
        try {
            Project project = projectService.createProject(courseId, studentIds, projectTitle, projectDescription, teacherId, projectDocument, projectDeadline, startDate, reviewMeetDate, maxTeam, gitUrl);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/studentCountProject")
    public ResponseEntity<Integer> getStudentCount(
            @RequestParam Long projectId,
            @RequestParam Long teacherId) {

        int studentCount = projectService.getStudentCountByProjectAndTeacher(projectId, teacherId);
        return ResponseEntity.ok(studentCount);
    }

    @PutMapping("/updateProjectMultiple/{projectId}")
    public ResponseEntity<Project> updateProject(
            @PathVariable long projectId,
            @RequestParam(required = false) String projectTitle,
            @RequestParam(required = false) String projectDescription,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) MultipartFile projectDocument,
            @RequestParam(required = false) LocalDateTime projectDeadline,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime reviewMeetDate,
            @RequestParam(required = false) Long maxTeam,
            @RequestParam(required = false) String gitUrl) {
        try {
            Project updatedProject = projectService.updateProject(projectId, projectTitle, projectDescription, teacherId, projectDocument, projectDeadline, startDate, reviewMeetDate, maxTeam, gitUrl);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/courseAndStudentsByProjectId")
    public ResponseEntity<Map<String, Object>> getCourseAndStudentDetailsByProjectId(
            @RequestParam long projectId) {
        try {
            Map<String, Object> response = projectService.getCourseAndStudentDetailsByProjectId(projectId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createProjectByMock")
    public ResponseEntity<String> createProjectByMock(
            @RequestParam String projectTitle,
            @RequestParam String projectDescription,
            @RequestParam("file") MultipartFile projectDocument,
            @RequestParam Long mockId,
            @RequestParam Long teacherId) throws IOException {

        String result = projectService.createProjectByMock(
                projectTitle, projectDescription, projectDocument, mockId, teacherId
        );

        if (result.equals("Project created successfully.")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/submissionsByMock/{teacherId}/{mockId}")
    public ResponseEntity<Map<String, Object>> getSubmittedAssignmentsAndProjects(
            @PathVariable Long teacherId, @PathVariable Long mockId) {
        Map<String, Object> submissions = projectService.getSubmittedAssignmentsAndProjects(teacherId, mockId);
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

//    @GetMapping("/submissionsByTeacher/{teacherId}")
//    public ResponseEntity<Map<String, Object>> getSubmittedAssignmentsAndProjects(
//            @PathVariable Long teacherId) {
//        Map<String, Object> submissions = projectService.getSubmittedAssignmentAndProject(teacherId);
//        return new ResponseEntity<>(submissions, HttpStatus.OK);
//    }

    @GetMapping("/postedByCourseByTeacher/{teacherId}")
    public ResponseEntity<List<Map<String, Object>>> getProjectsByTeacherAndCourse(@PathVariable long teacherId) {
        // Call the service method to get project details
        List<Map<String, Object>> projectDetails = projectService.getProjectsByTeacherAndCourse(teacherId);

        // Check if any projects are returned and handle the response accordingly
        if (projectDetails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content if no projects found
        }

        // Return the project details with 200 OK status
        return new ResponseEntity<>(projectDetails, HttpStatus.OK);
    }

    @PutMapping("/updateProjectsBy/{projectId}")
    public ResponseEntity<Project> updateProjectBy(
            @PathVariable long projectId,
            @RequestParam(required = false) String projectTitle,
            @RequestParam(required = false) String projectDescription,
            @RequestParam(required = false) LocalDateTime projectDeadline,
            @RequestParam(required = false) LocalDateTime reviewMeetDate,
            @RequestParam(required = false) Long maxTeam,
            @RequestParam(required = false) String gitUrl,
            @RequestParam(required = false) MultipartFile projectDocument) {

        Optional<Project> existingProjectOptional = projectService.getProjectById(projectId);
        if (existingProjectOptional.isPresent()) {
            Project existingProject = existingProjectOptional.get();

            if (projectTitle != null) {
                existingProject.setProjectTitle(projectTitle);
            }

            if (projectDescription != null) {
                existingProject.setProjectDescription(projectDescription);
            }

            if (projectDeadline != null) {
                existingProject.setProjectDeadline(projectDeadline);
            }
            if (reviewMeetDate != null) {
                existingProject.setReviewMeetDate(reviewMeetDate);
            }
            if (maxTeam != null) {
                existingProject.setMaxTeam(maxTeam);
            }
            if (gitUrl != null) {
                existingProject.setGitUrl(gitUrl);
            }

            if (projectDocument != null && !projectDocument.isEmpty()) {
                try {
                    byte[] documentBytes = projectDocument.getBytes();
                    existingProject.setProjectDocument(documentBytes);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }

            Project savedProject = projectService.saveProjectsBy(existingProject);
            return ResponseEntity.ok(savedProject);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/submissionsByTeacher/{teacherId}")
//    public ResponseEntity<Map<String, List<Map<String, Object>>>> getSubmittedAssignmentsAndProjects(
//            @PathVariable Long teacherId) {
//        Map<String, List<Map<String, Object>>> submissions = projectService.getSubmittedAssignmentAndProject(teacherId);
//
//        // Logging response size
//        System.out.println("Response size: assignments=" +
//                (submissions.get("submittedAssignments") != null ? submissions.get("submittedAssignments").size() : "null") +
//                ", projects=" +
//                (submissions.get("submittedProjects") != null ? submissions.get("submittedProjects").size() : "null"));
//
//        return new ResponseEntity<>(submissions, HttpStatus.OK);
//    }


    @GetMapping("/pending-feedback/{teacherId}")
    public ResponseEntity<Map<String, Object>> getPendingFeedback(@PathVariable Long teacherId) {
        List<SubmitAssignment> assignments = submitAssignmentService.getSubmittedAssignmentsWithoutFeedback(teacherId);
        List<SubmitProject> projects = submitProjectService.getSubmittedProjectsWithoutFeedback(teacherId);

        Map<String, Object> response = new HashMap<>();
        response.put("assignments", assignments);
        response.put("projects", projects);

        return ResponseEntity.ok(response);
    }

}