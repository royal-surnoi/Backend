package fusionIQ.AI.V2.fusionIq.repository;
import fusionIQ.AI.V2.fusionIq.data.AICoursePlan;
import fusionIQ.AI.V2.fusionIq.data.AICourseTopics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;import java.util.Optional;
@Repository
public interface AICoursePlanRepo extends JpaRepository<AICoursePlan, Long> {
    // Fetch course plan by ID and user ID
 Optional<AICoursePlan> findByIdAndUserId(Long id, Long userId);
    List<AICoursePlan> findByUserId(Long userId);
 }