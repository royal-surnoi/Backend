package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.AiSkillProgress;
import fusionIQ.AI.V2.fusionIq.repository.AiSkillProgressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AiSkillProgressService {

@Autowired
AiSkillProgressRepo aiSkillProgressRepo;


    public AiSkillProgress saveAISkill(AiSkillProgress aiSkillProgress) {
        return aiSkillProgressRepo.save(aiSkillProgress);
    }

    public List<AiSkillProgress> getAISkillsByUserId(Long userId) {
        return aiSkillProgressRepo.findByUserId(userId);
    }

}
