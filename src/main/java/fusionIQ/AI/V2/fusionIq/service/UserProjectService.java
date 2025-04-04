package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserProjects;
import fusionIQ.AI.V2.fusionIq.repository.UserProjectsRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProjectService {
    @Autowired
    private UserProjectsRepo projectRepository;

    @Autowired
    private UserRepo userProfileRepository;

    // Add or Update Project for a specific user
    public UserProjects saveProject( UserProjects project, Long userProfileId) {
        // Fetch the UserProfile by userProfileId
        User userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with ID: " + userProfileId));

        // Associate the user profile with the project
        project.setUser(userProfile);

        // Save and return the project
        return projectRepository.save(project);
    }
    public UserProjects getProjectByUserIdAndProjectId(Long userId, Long projectId) {
        return projectRepository.findById(projectId)
                .filter(project -> project.getUser().getId() == userId) // Use == for primitive comparison
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId + " for User ID: " + userId));
    }

    public List<UserProjects> getProjectsByUserId(Long userId) {
        return projectRepository.findByUser_Id(userId);
    }

    // Get Project by Project ID
    public UserProjects getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
    }

    // Delete Project by Project ID
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }
}



