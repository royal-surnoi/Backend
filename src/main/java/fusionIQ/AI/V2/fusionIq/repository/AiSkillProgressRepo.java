package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.AiSkillProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiSkillProgressRepo extends JpaRepository<AiSkillProgress,Long> {
    List<AiSkillProgress> findByUserId(Long userId);
}
