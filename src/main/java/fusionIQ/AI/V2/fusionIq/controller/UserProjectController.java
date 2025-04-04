package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.UserProjects;
import fusionIQ.AI.V2.fusionIq.service.UserProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class UserProjectController {
    @Autowired
    private UserProjectService projectService;

    // Add or Update Project for a specific user
    @PostMapping("/addOrUpdate/{userProfileId}")
    public ResponseEntity<UserProjects> addOrUpdateProject(
            @RequestParam String projectName,
            @RequestParam String client,
            @RequestParam String projectDescription,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam String skillsUsed,
            @RequestParam String projectLink,
            @RequestParam(required = false) MultipartFile projectImage,
            @RequestParam(required = false) MultipartFile projectVideo,
            @PathVariable Long userProfileId) {  // Change from userId to userProfileId

        // Create a new project entity using the request parameters
        UserProjects project = new  UserProjects();
        project.setProjectName(projectName);
        project.setClient(client);
        project.setProjectDescription(projectDescription);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setSkillsUsed(skillsUsed);
        project.setProjectLink(projectLink);

        // Handle file uploads
        if (projectImage != null && !projectImage.isEmpty()) {
            try {
                project.setProjectImage(projectImage.getBytes());
            } catch (Exception e) {
                // Handle exceptions
                return ResponseEntity.badRequest().build();
            }
        }

        if (projectVideo != null && !projectVideo.isEmpty()) {
            try {
                project.setProjectVideo(projectVideo.getBytes());
            } catch (Exception e) {
                // Handle exceptions
                return ResponseEntity.badRequest().build();
            }
        }

        // Save the project using the userProfileId
        UserProjects savedProject = projectService.saveProject(project, userProfileId);
        return ResponseEntity.ok(savedProject);
    }

    // Get all Projects by User ID
//    @GetMapping("/user/{user-ProfileId}")
//    public ResponseEntity<List< UserProjects>> getProjectsByUserId(@PathVariable Long userId) {
//        List< UserProjects> projects = projectService.getProjectsByUserId(userId);
//        return ResponseEntity.ok(projects);
//    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserProjects>> getProjectsByUserId(@PathVariable Long userId) {
        List<UserProjects> projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }


    // Get Project by Project ID
    @GetMapping("/{projectId}")
    public ResponseEntity< UserProjects> getProjectById(@PathVariable Long projectId) {
        UserProjects project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    // Delete Project by Project ID
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/update/{userId}/{projectId}")
    public ResponseEntity<UserProjects> updateProject(
            @PathVariable Long userId,
            @PathVariable Long projectId,
            @RequestParam String projectName,
            @RequestParam String client,
            @RequestParam String projectDescription,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam String skillsUsed,
            @RequestParam String projectLink,
            @RequestParam(required = false) MultipartFile projectImage,
            @RequestParam(required = false) MultipartFile projectVideo) {

        // Fetch the existing project by projectId and userId for validation
        UserProjects existingProject = projectService.getProjectByUserIdAndProjectId(userId, projectId);
        if (existingProject == null) {
            return ResponseEntity.notFound().build();  // Return 404 if project not found
        }

        // Update fields
        existingProject.setProjectName(projectName);
        existingProject.setClient(client);
        existingProject.setProjectDescription(projectDescription);
        existingProject.setStartDate(startDate);
        existingProject.setEndDate(endDate);
        existingProject.setSkillsUsed(skillsUsed);
        existingProject.setProjectLink(projectLink);

        // Handle optional file updates
        try {
            if (projectImage != null && !projectImage.isEmpty()) {
                existingProject.setProjectImage(projectImage.getBytes());
            }
            if (projectVideo != null && !projectVideo.isEmpty()) {
                existingProject.setProjectVideo(projectVideo.getBytes());
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Save the updated project
        UserProjects updatedProject = projectService.saveProject(existingProject, userId);
        return ResponseEntity.ok(updatedProject);
    }



}


