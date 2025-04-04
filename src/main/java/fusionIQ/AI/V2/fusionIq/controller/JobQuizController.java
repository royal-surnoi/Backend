package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.ShortlistedCandidatesRepository;
import fusionIQ.AI.V2.fusionIq.service.JobQuizService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobquiz")
public class JobQuizController {

    private final JobQuizService jobQuizService;

    @Autowired
    private ShortlistedCandidatesRepository shortlistedCandidatesRepository;

    public JobQuizController(JobQuizService jobQuizService) {
        this.jobQuizService = jobQuizService;
    }

    @PostMapping("/{recruiterId}/{jobId}")
    public ResponseEntity<JobQuiz> createJobQuizForUsers(
            @PathVariable Long recruiterId,
            @PathVariable Long jobId,
            @RequestParam List<Long> userIds,
            @RequestParam String quizName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        // Convert the list of userIds to a comma-separated string
        String userIdsString = userIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        // Call the service to create the JobQuiz
        JobQuiz jobQuiz = jobQuizService.createJobQuizForUsers(
                recruiterId, jobId, userIdsString, quizName, startDate, endDate
        );

        if (jobQuiz.getUserIds() != null && !jobQuiz.getUserIds().isEmpty()) {
            List<Long> userIdslist = Arrays.stream(jobQuiz.getUserIds().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            for (Long userId : userIdslist) {
                Optional<ShortlistedCandidates> applyJob = shortlistedCandidatesRepository.findByUserIdAndJobId(userId, jobId);
                if (applyJob.isPresent()) {
                    applyJob.get().setStatus("level-1"); // Set Level to level-1
                    shortlistedCandidatesRepository.save(applyJob.get()); // Save updated user
                }
            }
        }

        return ResponseEntity.ok(jobQuiz);
    }

    @PostMapping("/admin/{adminId}/job/{jobId}")
    public ResponseEntity<Object> createJobQuiz(
            @PathVariable Long adminId,
            @PathVariable Long jobId,
            @RequestBody Map<String, Object> requestBody) {
        try {
            String quizName = (String) requestBody.get("quizName");
            LocalDate startDate = LocalDate.parse((String) requestBody.get("startDate"));
            LocalDate endDate = LocalDate.parse((String) requestBody.get("endDate"));
            Long recruiterId = requestBody.containsKey("recruiterId") ? ((Number) requestBody.get("recruiterId")).longValue() : null;

            // Get userIds from the request body and convert to a comma-separated string
            List<Integer> userIds = (List<Integer>) requestBody.get("userIds");
            String userIdsString = userIds.stream().map(String::valueOf).collect(Collectors.joining(","));

            List<Map<String, Object>> questionsData = (List<Map<String, Object>>) requestBody.get("questions");

            // Pass userIdsString to the service

            JobQuiz jobQuiz = jobQuizService.createQuizWithQuestionsAndOptionalRecruiter(
                    adminId,
                    jobId,
                    recruiterId,
                    quizName,
                    startDate,
                    endDate,
                    userIdsString,
                    questionsData
            );

            if (jobQuiz.getUserIds() != null && !jobQuiz.getUserIds().isEmpty()) {
                List<Long> userIdslist = Arrays.stream(jobQuiz.getUserIds().split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                for (Long userId : userIdslist) {
                    Optional<ShortlistedCandidates> applyJob = shortlistedCandidatesRepository.findByUserIdAndJobId(userId, jobId);
                    if (applyJob.isPresent()) {
                        applyJob.get().setStatus("level-1"); // Set Level to level-1
                        shortlistedCandidatesRepository.save(applyJob.get()); // Save updated user
                    }
                }
            }

            return ResponseEntity.ok(Map.of("quiz", jobQuiz));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{jobId}/{jobQuizId}/questions")
    public ResponseEntity<List<Question>> addQuestionsToJobQuiz(
            @PathVariable Long jobId,
            @PathVariable Long jobQuizId,
            @RequestBody List<Question> questions) {

        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("Questions list cannot be empty or null.");
        }

        List<Question> savedQuestions = jobQuizService.addQuestionsToJobQuiz(jobQuizId, jobId, questions);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestions);
    }

    @GetMapping("/{jobId}/user/{userId}/details")
    public ResponseEntity<List<Map<String, Object>>> getJobQuizzesByJobAndUser(
            @PathVariable Long jobId,
            @PathVariable Long userId) {

        List<JobQuiz> jobQuizzes = jobQuizService.getJobQuizzesByUserAndJob(jobId, userId);

        List<Map<String, Object>> response = jobQuizzes.stream().map(jobQuiz -> {
            Map<String, Object> details = new LinkedHashMap<>();
            details.put("userId", userId);
            details.put("jobId", jobId);
            details.put("jobQuizId", jobQuiz.getId());
            details.put("quizName", jobQuiz.getQuizName());
            details.put("recruiterId", jobQuiz.getRecruiterId());
            List<Map<String, Object>> questionDetails = jobQuiz.getQuestions().stream().map(question -> {
                Map<String, Object> questionMap = new LinkedHashMap<>();
                questionMap.put("id", question.getId());
                questionMap.put("text", question.getText());
                questionMap.put("optionA", question.getOptionA());
                questionMap.put("optionB", question.getOptionB());
                questionMap.put("optionC", question.getOptionC());
                questionMap.put("optionD", question.getOptionD());
                questionMap.put("correctAnswer", question.getCorrectAnswer());
                return questionMap;
            }).collect(Collectors.toList());
            details.put("questions", questionDetails);
            return details;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobQuiz>> getJobQuizByUserId(@PathVariable Long userId) {
        List<JobQuiz> jobQuizzes = jobQuizService.getJobQuizByUserId(userId);
        return ResponseEntity.ok(jobQuizzes);
    }

    @GetMapping("/users")
    public ResponseEntity<List<JobQuiz>> getJobQuizByUserIds(@RequestParam List<Long> userIds) {
        List<JobQuiz> jobQuizzes = jobQuizService.getJobQuizByUserIds(userIds);
        return ResponseEntity.ok(jobQuizzes);
    }

   }
