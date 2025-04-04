package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.AICourseAssignment;
import fusionIQ.AI.V2.fusionIq.data.AICourseAssignmentGrade;
import fusionIQ.AI.V2.fusionIq.repository.AICourseAssignmentGradeRepo;
import fusionIQ.AI.V2.fusionIq.repository.AICourseAssignmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AICourseAssignmentGradeService {

    @Autowired
    private AICourseAssignmentGradeRepo assignmentGradeRepo;

    @Autowired
    private AICourseAssignmentRepo assignmentRepo;

    public AICourseAssignmentGrade createAssignmentGrade(AICourseAssignmentGrade assignmentGrade) {
        // Ensure aiCourseAssignment is managed
        AICourseAssignment managedAssignment = assignmentRepo
                .findById(assignmentGrade.getAiCourseAssignment().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid aiCourseAssignment ID"));

        assignmentGrade.setAiCourseAssignment(managedAssignment);

        // Save the grade
        return assignmentGradeRepo.save(assignmentGrade);
    }
}