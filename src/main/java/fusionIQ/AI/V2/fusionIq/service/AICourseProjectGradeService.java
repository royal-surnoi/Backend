package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.AICourseProject;
import fusionIQ.AI.V2.fusionIq.data.AICourseProjectGrade;
import fusionIQ.AI.V2.fusionIq.repository.AICourseProjectGradeRepo;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AICourseProjectGradeService {

    @Autowired
    private AICourseProjectGradeRepo projectGradeRepo;

    @Autowired
    private EntityManager entityManager;

    public AICourseProjectGrade createProjectGrade(AICourseProjectGrade projectGrade) {
        // Attach the detached entity to the persistence context
        AICourseProject managedProject = entityManager.find(AICourseProject.class, projectGrade.getAiCourseProject().getId());

        if (managedProject == null) {
            throw new IllegalArgumentException("AICourseProject with ID " + projectGrade.getAiCourseProject().getId() + " does not exist.");
        }

        // Assign the managed project to the grade
        projectGrade.setAiCourseProject(managedProject);

        // Save the grade
        return projectGradeRepo.save(projectGrade);
    }
}