package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.AICourseAssignmentGrade;
import fusionIQ.AI.V2.fusionIq.service.AICourseAssignmentGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aicourseassignmentgrades")
public class AICourseAssignmentGradeController {

    @Autowired
    private AICourseAssignmentGradeService assignmentGradeService;

    @PostMapping("/insertassignmentgrade")
    public ResponseEntity<AICourseAssignmentGrade> createAssignmentGrade(@RequestBody AICourseAssignmentGrade assignmentGrade) {
        AICourseAssignmentGrade createdGrade = assignmentGradeService.createAssignmentGrade(assignmentGrade);
        return ResponseEntity.ok(createdGrade);
    }


}