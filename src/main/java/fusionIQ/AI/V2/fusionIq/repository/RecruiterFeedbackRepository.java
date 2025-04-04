package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.RecruiterFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterFeedbackRepository extends JpaRepository<RecruiterFeedback, Long> {

    Optional<RecruiterFeedback> findByUserIdAndRecruiterIdAndJobId(Long userId, Long recruiterId, Long jobId);
}