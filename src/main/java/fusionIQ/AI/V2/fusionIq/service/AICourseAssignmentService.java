package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.AICourseAssignment;
import fusionIQ.AI.V2.fusionIq.data.AICoursePlan;
import fusionIQ.AI.V2.fusionIq.repository.AICourseAssignmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.AICoursePlanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AICourseAssignmentService {

    @Autowired
    private AICourseAssignmentRepo aiCourseAssignmentRepo;

    public Optional<AICourseAssignment> getAssignmentById(Long id) {
        return aiCourseAssignmentRepo.findById(id);
    }

    public List<String> getAllAnswersExcept(Long id) {
        return aiCourseAssignmentRepo.findAllAnswersExcept(id);
    }
    public List<AICourseAssignment> getAssignmentsByCoursePlanId(Long coursePlanId) {
        return aiCourseAssignmentRepo.findByAiCoursePlanId(coursePlanId);
    }

    public AICourseAssignment updateAssignmentAnswer(Long assignmentId, String userAnswer) {
        AICourseAssignment assignment = aiCourseAssignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("AICourseAssignment not found with id: " + assignmentId));
        assignment.setAiCourseAssignmentUserAnswer(userAnswer);
        return aiCourseAssignmentRepo.save(assignment);
    }
    @Autowired
    private AICoursePlanRepo coursePlanRepository;

    public AICourseAssignment saveAssignment(Long coursePlanId, int weekNumber, String aiCourseAssignment) {
        // Fetch the related AICoursePlan entity
        Optional<AICoursePlan> coursePlan = coursePlanRepository.findById(coursePlanId);

        if (coursePlan.isEmpty()) {
            throw new IllegalArgumentException("Invalid course plan ID: " + coursePlanId);
        }

        // Create and save the assignment
        AICourseAssignment assignment = new AICourseAssignment();
        assignment.setWeekNumber(weekNumber);
        assignment.setAiCourseAssignment(aiCourseAssignment);
        assignment.setAiCoursePlan(coursePlan.get());
        return aiCourseAssignmentRepo.save(assignment);
    }
}