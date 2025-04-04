package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.AiSkillProgress;
import fusionIQ.AI.V2.fusionIq.service.AiSkillProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AiSkillProgressController {



    @Autowired
    AiSkillProgressService aiSkillProgressService;

    @PostMapping("/saveSkill/{userId}")
    public ResponseEntity<AiSkillProgress> addAISkill(@PathVariable Long userId, @RequestBody AiSkillProgress aiSkillProgress) {
        aiSkillProgress.setUserId(userId);
        AiSkillProgress savedAiSkillProgress = aiSkillProgressService.saveAISkill(aiSkillProgress);
        return new ResponseEntity<>(savedAiSkillProgress, HttpStatus.CREATED);
    }
    @GetMapping("/getSkill/{userId}")
    public ResponseEntity<List<AiSkillProgress>> getAISkillsByUserId(@PathVariable Long userId) {
        List<AiSkillProgress> aiSkillProgresses = aiSkillProgressService.getAISkillsByUserId(userId);
        return new ResponseEntity<>(aiSkillProgresses, HttpStatus.OK);
    }

}
