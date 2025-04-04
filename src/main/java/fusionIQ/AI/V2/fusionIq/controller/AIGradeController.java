package fusionIQ.AI.V2.fusionIq.controller;


import fusionIQ.AI.V2.fusionIq.data.AIGrade;
import fusionIQ.AI.V2.fusionIq.repository.AIGradeRepo;
import fusionIQ.AI.V2.fusionIq.service.AIGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AIGradeController {

    @Autowired
    AIGradeService aiGradeService;

    @Autowired
    AIGradeRepo aiGradeRepo;

    @PostMapping("/createAIQuizGrade")
    public ResponseEntity<AIGrade> createAIQuizGrade(
            @RequestParam Long aiQuizId,
            @RequestParam String aiGrade,
            @RequestParam String aiFeedback) {

        AIGrade savedAIGrade = aiGradeService.saveAIQuizGrade(aiQuizId, aiGrade, aiFeedback);
        return new ResponseEntity<>(savedAIGrade, HttpStatus.CREATED);
    }
    @PostMapping("/createAIAssignmentGrade")
    public ResponseEntity<AIGrade> createAIAssignmentGrade(
            @RequestParam Long aiAssignmnetId,
            @RequestParam String aiGrade,
            @RequestParam String aiFeedback) {

        AIGrade savedAIGrade = aiGradeService.saveAIAssignmentGrade(aiAssignmnetId, aiGrade, aiFeedback);
        return new ResponseEntity<>(savedAIGrade, HttpStatus.CREATED);
    }
    @GetMapping("/get/grade/{UserId}")
    public ResponseEntity<List<AIGrade>> getGradesByUserId(@RequestParam Long userId) {
        List<AIGrade> grades = aiGradeService.getGradesByUserId(userId);
        return ResponseEntity.ok(grades);
    }
    @GetMapping("/get/AllAIQuiz")
    public ResponseEntity<List<AIGrade>> getAllGrades() {
        List<AIGrade> grades = aiGradeService.getAllGrades();
        return ResponseEntity.ok(grades);
    }
    @DeleteMapping("AiQuiz/{id}")
    public ResponseEntity<Void> deleteGradeById(@PathVariable Long id) {
        try {
            aiGradeService.deleteGradeById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/submitAIQuizGrade/{aiQuizId}")
    public ResponseEntity<AIGrade> submitAIQuizGrade(
            @PathVariable Long aiQuizId,
//            @PathVariable Long userId,
            @RequestBody List<Map<String, Object>> answers) {

        AIGrade savedAIGrade = aiGradeService.submitGradeBasedOnAnswers(aiQuizId, answers);

        return new ResponseEntity<>(savedAIGrade, HttpStatus.CREATED);
    }

}
