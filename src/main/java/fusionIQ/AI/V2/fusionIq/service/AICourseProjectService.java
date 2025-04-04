package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.AICoursePlan;
import fusionIQ.AI.V2.fusionIq.data.AICourseProject;
import fusionIQ.AI.V2.fusionIq.repository.AICoursePlanRepo;
import fusionIQ.AI.V2.fusionIq.repository.AICourseProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AICourseProjectService {
    @Autowired
    private AICourseProjectRepo aiCourseProjectRepo;

    @Autowired
    private AICoursePlanRepo coursePlanRepository;


    public Optional<AICourseProject> getProjectById(Long id) {
        return aiCourseProjectRepo.findById(id);
    }

    public List<String> getAllAnswersExcept(Long id) {
        return aiCourseProjectRepo.findAllAnswersExcept(id);
    }

    public List<AICourseProject> getProjectsByCoursePlanId(Long coursePlanId) {
        return aiCourseProjectRepo.findByAiCoursePlanId(coursePlanId);
    }

    public AICourseProject updateProjectAnswer(Long projectId, String userAnswer) {
        AICourseProject project = aiCourseProjectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("AICourseProject not found with id: " + projectId));
        project.setAiCourseProjectUserAnswer(userAnswer);
        return aiCourseProjectRepo.save(project);
    }

    public AICourseProject saveCourseProject(Long coursePlanId, String aiCourseProject) {
        // Fetch the related AICoursePlan entity
        Optional<AICoursePlan> coursePlanOptional = coursePlanRepository.findById(coursePlanId);
        if (coursePlanOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid course plan ID: " + coursePlanId);
        }

        AICoursePlan coursePlan = coursePlanOptional.get();

        // Create and populate the AICourseProject entity
        AICourseProject project = new AICourseProject();
        project.setAiCoursePlan(coursePlan);
        project.setAiCourseProject(aiCourseProject);

        // Save the project to the database
        return aiCourseProjectRepo.save(project);
    }
}