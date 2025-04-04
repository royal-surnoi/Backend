package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.AICourseAssignmentGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AICourseAssignmentGradeRepo extends JpaRepository<AICourseAssignmentGrade, Integer> {
}