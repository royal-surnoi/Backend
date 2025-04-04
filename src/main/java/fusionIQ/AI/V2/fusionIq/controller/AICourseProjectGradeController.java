package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.AICourseProjectGrade;
import fusionIQ.AI.V2.fusionIq.service.AICourseProjectGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aicourseprojectgrades")
public class AICourseProjectGradeController {

    @Autowired
    private AICourseProjectGradeService projectGradeService;

    @PostMapping("/insertprojectgrade")
    public ResponseEntity<AICourseProjectGrade> createProjectGrade(@RequestBody AICourseProjectGrade projectGrade) {
        AICourseProjectGrade createdGrade = projectGradeService.createProjectGrade(projectGrade);
        return ResponseEntity.ok(createdGrade);
    }
}