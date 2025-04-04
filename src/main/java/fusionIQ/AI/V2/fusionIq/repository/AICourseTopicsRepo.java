package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.AICoursePlan;
import fusionIQ.AI.V2.fusionIq.data.AICourseTopics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AICourseTopicsRepo extends JpaRepository<AICourseTopics, Long> {
    // Fetch topics by course plan ID
    List<AICourseTopics> findByAiCoursePlanId(Long aiCoursePlanId);

    List<AICourseTopics> findByAiCoursePlanIdOrderByWeekNumberAsc(Long aiCoursePlanId);
}