package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.AICourseAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AICourseAssignmentRepo extends JpaRepository<AICourseAssignment, Long> {
    @Query("SELECT a.aiCourseAssignmentUserAnswer FROM AICourseAssignment a WHERE a.id != :id")
    List<String> findAllAnswersExcept(@Param("id") Long id);

    List<AICourseAssignment> findByAiCoursePlanId(Long aiCoursePlanId);
}