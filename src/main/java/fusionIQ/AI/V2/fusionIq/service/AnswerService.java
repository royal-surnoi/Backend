package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepo answerRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JobQuizRepository jobQuizRepository;

    @Autowired
    private JobQuizProgressRepo jobQuizProgressRepo;

    public AnswerService(AnswerRepo answerRepo, JobQuizProgressRepo jobQuizProgressRepo) {
        this.answerRepo = answerRepo;
        this.jobQuizProgressRepo = jobQuizProgressRepo;
    }

    public Map<String, Object> saveAnswers(Long jobQuizId, Long userId, List<Map<String, Object>> answers) {
        JobQuiz jobQuiz = jobQuizRepository.findById(jobQuizId)
                .orElseThrow(() -> new RuntimeException("Job Quiz not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Map<String, Object>> results = new ArrayList<>();

        for (Map<String, Object> answerData : answers) {
            Long questionId = Long.parseLong(answerData.get("questionId").toString());
            String selectedAnswer = answerData.get("selectedAnswer").toString();

            Question question = questionRepo.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(selectedAnswer);

            // Save the answer
            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setUser(user);
            answer.setJobQuiz(jobQuiz);
            answer.setSelectedAnswer(selectedAnswer);
            answer.setCorrect(isCorrect);
            answerRepo.save(answer);

            // Add individual result to the response
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("questionId", questionId);
            result.put("isCorrect", isCorrect);
            results.add(result);
        }

        // Final response structure
        Map<String, Object> finalResponse = new LinkedHashMap<>();
        finalResponse.put("jobQuizId", jobQuizId);
        finalResponse.put("userId", userId);
        finalResponse.put("results", results);
        return finalResponse;
    }
    public double calculateCorrectAnswerPercentage(Long jobQuizId) {
        long totalQuestions = answerRepo.countTotalQuestionsByJobQuizId(jobQuizId);
        if (totalQuestions == 0) {
            return 0.0; // Avoid division by zero
        }

        long correctAnswers = answerRepo.countCorrectAnswersByJobQuizId(jobQuizId);
        long attemptedAnswers = answerRepo.countAttemptedAnswersByJobQuizId(jobQuizId);

        // Calculate incorrect due to not attempting
        long unansweredQuestions = totalQuestions - attemptedAnswers;
        long totalIncorrectAnswers = (attemptedAnswers - correctAnswers) + unansweredQuestions;

        // Correct percentage
        return (double) correctAnswers / totalQuestions * 100;
    }

    public Map<String, Object> submitAnswersAndCalculatePercentage(Long jobQuizId, Long userId, List<Map<String, Object>> answers) {
        // Fetch job quiz details
        JobQuiz jobQuiz = jobQuizRepository.findById(jobQuizId)
                .orElseThrow(() -> new RuntimeException("JobQuiz not found"));

        // Calculate percentage logic
        double correctAnswerPercentage = calculatePercentage(jobQuiz, answers);

        // Fetch user details
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Determine recruiter or admin details
        Recruiter recruiter = jobQuiz.getRecruiter();
        JobAdmin jobAdmin= jobQuiz.getJobAdmin(); // Assuming JobQuiz has an admin relationship

        if (recruiter == null && jobAdmin == null) {
            throw new RuntimeException("Neither recruiter nor admin is associated with this JobQuiz");
        }

        // Prepare the JobQuizProgress entity
        JobQuizProgress jobQuizProgress = new JobQuizProgress();
        jobQuizProgress.setUserId(user.getId());
        jobQuizProgress.setUserName(user.getName());
        jobQuizProgress.setUserEmail(user.getEmail());

        if (recruiter != null) {
            jobQuizProgress.setRecruiterId(recruiter.getId());
            jobQuizProgress.setRecruiterName(recruiter.getRecruiterName());
            jobQuizProgress.setRecruiterEmail(recruiter.getRecruiterEmail());
        } else if (jobAdmin != null) {
            jobQuizProgress.setAdminId(jobAdmin.getId());
            jobQuizProgress.setAdminName(jobAdmin.getJobAdminName());
            jobQuizProgress.setAdminEmail(jobAdmin.getJobAdminEmail());
        }

        jobQuizProgress.setScorePercentage(correctAnswerPercentage);
        jobQuizProgress.setJobQuiz(jobQuiz);
        jobQuizProgress.setCompletedAt(LocalDateTime.now());

        // Create and associate the Answer entities with JobQuizProgress
        List<Answer> answerEntities = new ArrayList<>();
        for (Map<String, Object> answerData : answers) {
            Long questionId = ((Integer) answerData.get("questionId")).longValue();
            String selectedAnswer = (String) answerData.get("selectedAnswer");

            // Fetch the question details from the JobQuiz
            Question question = jobQuiz.getQuestions().stream()
                    .filter(q -> q.getId().equals(questionId))
                    .findFirst()
                    .orElse(null);

            if (question != null) {
                Answer answer = new Answer();
                answer.setQuestion(question);
                answer.setSelectedAnswer(selectedAnswer);
                answer.setCorrect(question.getCorrectAnswer().equalsIgnoreCase(selectedAnswer));
                answer.setJobQuiz(jobQuiz);
                answer.setJobQuizProgress(jobQuizProgress);
                answer.setUser(user);
                answerEntities.add(answer);
            }
        }

        // Save the JobQuizProgress and associated Answers
        jobQuizProgress.setAnswers(answerEntities);
        jobQuizProgressRepo.save(jobQuizProgress);

        // Prepare response
        Map<String, Object> response = new LinkedHashMap<>();
        if (recruiter != null) {
            response.put("recruiterId", recruiter.getId());
            response.put("recruiterName", recruiter.getRecruiterName());
            response.put("recruiterEmail", recruiter.getRecruiterEmail());
        } else if (jobAdmin != null) {
            response.put("adminId", jobAdmin.getId());
            response.put("adminName", jobAdmin.getJobAdminName());
            response.put("adminEmail", jobAdmin.getJobAdminEmail());
        }
        response.put("jobQuizId", jobQuiz.getId());
        response.put("jobQuizName", jobQuiz.getQuizName());
        response.put("jobId", jobQuiz.getJobId());
        response.put("userId", user.getId());
        response.put("userName", user.getName());
        response.put("userEmail", user.getEmail());
        response.put("results", answers);
        response.put("correctAnswerPercentage", correctAnswerPercentage);

        return response;
    }


    private double calculatePercentage(JobQuiz jobQuiz, List<Map<String, Object>> answers) {
        long totalQuestions = answers.size();
        long correctAnswers = 0;

        // Iterate through answers and check if the selected answer matches the correct answer
        for (Map<String, Object> answer : answers) {
            // Convert questionId from Integer to Long
            Long questionId = ((Integer) answer.get("questionId")).longValue();
            String selectedAnswer = (String) answer.get("selectedAnswer");

            // Fetch the question details from the JobQuiz (assuming JobQuiz has a list of questions)
            Question question = jobQuiz.getQuestions().stream()
                    .filter(q -> q.getId().equals(questionId))
                    .findFirst()
                    .orElse(null);

            if (question != null && question.getCorrectAnswer().equalsIgnoreCase(selectedAnswer)) {
                correctAnswers++;
            }
        }

        return (double) correctAnswers / totalQuestions * 100;
    }



    public Map<String, Object> getQuizProgressDetails(Long jobQuizId, Long userId) {
        // Fetch JobQuizProgress details
        JobQuizProgress jobQuizProgress = jobQuizProgressRepo.findByJobQuizIdAndUserId(jobQuizId, userId)
                .orElseThrow(() -> new RuntimeException("JobQuizProgress not found for the given JobQuiz ID and User ID"));

        // Fetch submitted answers
        List<Answer> answers = answerRepo.findByJobQuizIdAndUserId(jobQuizId, userId);

        // Build response
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("recruiterId", jobQuizProgress.getRecruiterId());
        response.put("recruiterName", jobQuizProgress.getRecruiterName());
        response.put("recruiterEmail", jobQuizProgress.getRecruiterEmail());
        response.put("jobQuizId", jobQuizProgress.getJobQuiz().getId());
        response.put("jobQuizName", jobQuizProgress.getJobQuiz().getQuizName());
        response.put("jobId", jobQuizProgress.getJobQuiz().getJobId());
        response.put("userId", jobQuizProgress.getUserId());
        response.put("userName", jobQuizProgress.getUserName());
        response.put("userEmail", jobQuizProgress.getUserEmail());
        response.put("scorePercentage", jobQuizProgress.getScorePercentage());

        // Add submitted answers to response
        response.put("submittedAnswers", answers.stream().map(answer -> {
            Map<String, Object> answerDetails = new LinkedHashMap<>();
            answerDetails.put("questionId", answer.getQuestion().getId());
            answerDetails.put("questionText", answer.getQuestion().getText());
            answerDetails.put("selectedAnswer", answer.getSelectedAnswer());
            answerDetails.put("isCorrect", answer.isCorrect());
            return answerDetails;
        }).collect(Collectors.toList()));

        return response;
    }
    public List<Map<String, Object>> getProgressByJobAndRecruiterId(Long jobId, Long recruiterId) {

        // Fetch all progress entries for the given job ID and recruiter ID

        List<JobQuizProgress> jobQuizProgressList = jobQuizProgressRepo.findByJobIdAndRecruiterId(jobId, recruiterId);

        // Map progress data to the desired format

        return jobQuizProgressList.stream().map(progress -> {

            Map<String, Object> progressDetails = new LinkedHashMap<>();

            progressDetails.put("userId", progress.getUserId());

            progressDetails.put("userName", progress.getUserName());

            progressDetails.put("userEmail", progress.getUserEmail());

            // Fetch user details, including image URL

            User user = userRepo.findById(progress.getUserId())

                    .orElseThrow(() -> new RuntimeException("User not found"));

            progressDetails.put("userImage", user.getUserImage()); // Assuming imageUrl exists in User entity

            progressDetails.put("recruiterId", progress.getRecruiterId());

            progressDetails.put("recruiterName", progress.getRecruiterName());

            progressDetails.put("recruiterEmail", progress.getRecruiterEmail());

            progressDetails.put("jobQuizId", progress.getJobQuiz().getId());

            progressDetails.put("jobQuizName", progress.getJobQuiz().getQuizName());

            progressDetails.put("jobId", progress.getJobQuiz().getJobId()); // Fetch jobId from JobQuiz

            progressDetails.put("jobTitle", progress.getJobQuiz().getJob().getJobTitle()); // Fetch jobId from JobQuiz

            progressDetails.put("scorePercentage", progress.getScorePercentage());

            progressDetails.put("completedAt", progress.getCompletedAt());

            // Fetch answers related to this job quiz progress

            List<Answer> answers = answerRepo.findByJobQuizProgress(progress);

            // Map answers

            List<Map<String, Object>> answerDetails = answers.stream().map(answer -> {

                Map<String, Object> answerMap = new LinkedHashMap<>();

                answerMap.put("questionId", answer.getQuestion().getId());

                answerMap.put("questionText", answer.getQuestion().getText());

                answerMap.put("selectedAnswer", answer.getSelectedAnswer());

                answerMap.put("isCorrect", answer.isCorrect());

                return answerMap;

            }).collect(Collectors.toList());

            progressDetails.put("answers", answerDetails);

            return progressDetails;

        }).collect(Collectors.toList());

    }

    public List<Map<String, Object>> getProgressByJobAndAdminId(Long jobId, Long adminId) {
        // Fetch all progress entries for the given job ID and admin ID
        List<JobQuizProgress> jobQuizProgressList = jobQuizProgressRepo.findByJobIdAndAdminId(jobId, adminId);

        // Map progress data to the desired format
        return jobQuizProgressList.stream().map(progress -> {
            Map<String, Object> progressDetails = new LinkedHashMap<>();

            progressDetails.put("userId", progress.getUserId());
            progressDetails.put("userName", progress.getUserName());
            progressDetails.put("userEmail", progress.getUserEmail());

            // Fetch user details, including image URL
            User user = userRepo.findById(progress.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            progressDetails.put("userImage", user.getUserImage()); // Assuming imageUrl exists in User entity

            progressDetails.put("adminId", adminId);
            progressDetails.put("adminName", progress.getAdminName());
            progressDetails.put("adminEmail", progress.getAdminEmail());

            progressDetails.put("jobQuizId", progress.getJobQuiz().getId());
            progressDetails.put("jobQuizName", progress.getJobQuiz().getQuizName());
            progressDetails.put("jobId", progress.getJobQuiz().getJobId()); // Fetch jobId from JobQuiz
            progressDetails.put("jobTitle", progress.getJobQuiz().getJob().getJobTitle()); // Fetch jobId from JobQuiz

            progressDetails.put("scorePercentage", progress.getScorePercentage());
            progressDetails.put("completedAt", progress.getCompletedAt());

            // Fetch answers related to this job quiz progress
            List<Answer> answers = answerRepo.findByJobQuizProgress(progress);

            // Map answers
            List<Map<String, Object>> answerDetails = answers.stream().map(answer -> {
                Map<String, Object> answerMap = new LinkedHashMap<>();
                answerMap.put("questionId", answer.getQuestion().getId());
                answerMap.put("questionText", answer.getQuestion().getText());
                answerMap.put("selectedAnswer", answer.getSelectedAnswer());
                answerMap.put("isCorrect", answer.isCorrect());
                return answerMap;
            }).collect(Collectors.toList());

            progressDetails.put("answers", answerDetails);

            return progressDetails;
        }).collect(Collectors.toList());
    }


}

