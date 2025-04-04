package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/answers")

public class AnswerController {

    @Autowired
    private AnswerRepo answerRepo;

    @Autowired
    private JobQuizProgressRepo jobQuizProgressRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private JobQuizRepository jobQuizRepository;

    @PostMapping("/jobquiz/{jobQuizId}/user/{userId}/submitanswers")
    public ResponseEntity<Map<String, Object>> submitAnswers(
            @PathVariable Long jobQuizId,
            @PathVariable Long userId,
            @RequestBody List<Map<String, Object>> answers) {

        Map<String, Object> response = answerService.saveAnswers(jobQuizId, userId, answers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/jobquiz/{jobQuizId}/percentage")
    public ResponseEntity<Map<String, Object>> getCorrectAnswerPercentage(@PathVariable Long jobQuizId) {
        double percentage = answerService.calculateCorrectAnswerPercentage(jobQuizId);

        Map<String, Object> response = new HashMap<>();
        response.put("jobQuizId", jobQuizId);
        response.put("correctAnswerPercentage", percentage);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/jobquiz/{jobQuizId}/user/{userId}/submitanswerswithpercentage")
    public ResponseEntity<Map<String, Object>> submitAnswersWithPercentage(
            @PathVariable Long jobQuizId,
            @PathVariable Long userId,
            @RequestBody List<Map<String, Object>> answers) {
        System.out.println("submitAnswersWithPercentage called with jobQuizId: " + jobQuizId + " and userId: " + userId);
        Map<String, Object> response = answerService.submitAnswersAndCalculatePercentage(jobQuizId, userId, answers);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/{jobQuizId}/{userId}/details")
    public ResponseEntity<Map<String, Object>> getQuizProgressDetails(
            @PathVariable Long jobQuizId,
            @PathVariable Long userId) {
        Map<String, Object> response = answerService.getQuizProgressDetails(jobQuizId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getProgress/{jobId}/{recruiterId}")
    public ResponseEntity<List<Map<String, Object>>> getProgressByJobAndRecruiterId(
            @PathVariable Long jobId,
            @PathVariable Long recruiterId) {
        List<Map<String, Object>> response = answerService.getProgressByJobAndRecruiterId(jobId, recruiterId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getProgressByAdmin/{jobId}/{adminId}")
    public ResponseEntity<List<Map<String, Object>>> getProgressByJobAndAdminId(
            @PathVariable Long jobId,
            @PathVariable Long adminId) {
        List<Map<String, Object>> response = answerService.getProgressByJobAndAdminId(jobId, adminId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/isAnswered")
    public ResponseEntity<Boolean> isQuizAnswered(@RequestParam Long userId, @RequestParam Long jobQuizId) {
        try {
            List<JobQuizProgress> progress = jobQuizProgressRepo.findByUserIdAndJobQuizId(userId, jobQuizId);
            return ResponseEntity.ok(progress.size() >= 1);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }


    }
}

