package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional

public class AIQuizService {
    @Autowired
    AIQuizRepo aiQuizRepo;

    @Autowired
    CourseRepo courseRepo;

    @Autowired
    UserRepo userRepo;
    @Autowired
    LessonRepo lessonRepo;

    @Autowired
    AIGradeRepo aiGradeRepo;

    @Autowired
    AIQuizQuestionRepo aiQuizQuestionRepo;

    @Autowired
    AIQuizAnswerRepo aiQuizAnswerRepo;

    @Autowired
    NotificationRepo notificationRepo;


    public AIQuiz createAIQuiz(Long courseId, Long lessonId, Long userId, String AIQuizName) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AIQuiz aiQuiz = new AIQuiz();
        aiQuiz.setAIQuizName(AIQuizName);
        aiQuiz.setCreatedAt(LocalDateTime.now());
        aiQuiz.setCourse(course);
        aiQuiz.setLesson(lesson);
        aiQuiz.setUser(user);

        return aiQuizRepo.save(aiQuiz);
    }


    public void deleteQuizById(Long id) {
        if (!aiQuizRepo.existsById(id)) {
            throw new RuntimeException("AIQuiz not found");
        }
        aiQuizRepo.deleteById(id);
    }

    public List<AIQuiz> getQuizzesByUserId(Long userId) {
        return aiQuizRepo.findByUserId(userId);
    }

    public AIQuiz getQuizById(Long quizId) {
        return aiQuizRepo.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    public AIQuizQuestion saveQuizQuestion(Long quizId, AIQuizQuestion aiQuizQuestion) {
        AIQuiz aiQuiz = getQuizById(quizId); // Fetching the quiz by its ID
        aiQuizQuestion.setAiQuiz(aiQuiz); // Linking the question to the quiz
        return aiQuizQuestionRepo.save(aiQuizQuestion); // Saving the question

    }

    public AIQuizAnswer saveQuizAnswer(Long userId, Long questionId, AIQuizAnswer aiQuizAnswer) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AIQuizQuestion aiQuizQuestion = aiQuizQuestionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        AIQuiz aiQuiz = aiQuizQuestion.getAiQuiz();

        aiQuizAnswer.setUser(user);
        aiQuizAnswer.setAiQuiz(aiQuiz);
        aiQuizAnswer.setAiQuizQuestion(aiQuizQuestion);

        return aiQuizAnswerRepo.save(aiQuizAnswer);
    }

    public List<AIQuiz> findByUserAndLesson(long userId, long lessonId) {
        return aiQuizRepo.findByUserIdAndLessonId(userId, lessonId);
    }

    public Map<String, Object> submitQuizAnswers(Long aiQuizId, Long userId, List<Map<String, Object>> answers) {
        // Check if the quiz has already been submitted by the user
        if (isQuizAlreadySubmitted(aiQuizId, userId)) {
            throw new RuntimeException("This AIQuiz has already been submitted by the user.");
        }

        AIQuiz aiQuiz = aiQuizRepo.findById(aiQuizId)
                .orElseThrow(() -> new RuntimeException("AIQuiz not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int correctAnswers = 0;
        int totalQuestions = answers.size();

        for (Map<String, Object> answerMap : answers) {
            String userAnswer = (String) answerMap.get("AIQuizUserAnswer");
            Long questionId = ((Number) answerMap.get("aiQuizQuestionId")).longValue();

            AIQuizQuestion question = aiQuizQuestionRepo.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("AIQuizQuestion not found"));

            boolean isCorrectAnswer = false;
            Object isCorrect = answerMap.get("isCorrectAnswer");
            if (isCorrect instanceof Boolean) {
                isCorrectAnswer = (Boolean) isCorrect;
            } else if (isCorrect instanceof String) {
                isCorrectAnswer = Boolean.parseBoolean((String) isCorrect);
            }

            AIQuizAnswer answer = new AIQuizAnswer();
            answer.setAIQuizUserAnswer(userAnswer);
            answer.setAiQuiz(aiQuiz);
            answer.setUser(user);
            answer.setAiQuizQuestion(question);
            answer.setCorrectAnswer(isCorrectAnswer);

            if (isCorrectAnswer) {
                correctAnswers++;
            }

            aiQuizAnswerRepo.save(answer);
        }

        double percentage = (double) correctAnswers / totalQuestions * 100;
        String ratio = correctAnswers + "/" + totalQuestions;

        String aiGrade = calculateGrade(percentage);

        AIGrade gradeEntity = new AIGrade();
        gradeEntity.setAIGrade(aiGrade);
        gradeEntity.setAiQuiz(aiQuiz);
        gradeEntity.setAIFeedback("Well done!"); // Example feedback
        aiGradeRepo.save(gradeEntity);

        Map<String, Object> response = new HashMap<>();
        response.put("aiQuizId", aiQuizId);
        response.put("userId", userId);
        response.put("totalQuestions", totalQuestions);
        response.put("correctAnswers", correctAnswers);
        response.put("percentage", percentage);
        response.put("ratio", ratio);
        response.put("aiGrade", aiGrade);

        return response;
    }


    private boolean isQuizAlreadySubmitted(Long quizId, Long userId) {
        return aiQuizAnswerRepo.existsByAiQuizIdAndUserId(quizId, userId);
    }

    private String calculateGrade(double percentage) {
        if (percentage >= 80) {
            return "A";
        } else if (percentage >= 60) {
            return "B";
        } else if (percentage >= 40) {
            return "C";
        } else if (percentage >= 20) {
            return "D";
        } else {
            return "E";
        }
    }

    @Transactional
    public Map<String, Object> getQuizResult(Long aiQuizId, Long userId) {
        AIQuiz aiQuiz = aiQuizRepo.findById(aiQuizId)
                .orElseThrow(() -> new RuntimeException("AIQuiz not found"));

        List<AIQuizAnswer> answers = aiQuizAnswerRepo.findByAiQuizIdAndUserId(aiQuizId, userId);

        int totalQuestions = answers.size();
        int correctAnswers = (int) answers.stream().filter(AIQuizAnswer::isCorrectAnswer).count();
        double percentage = (double) correctAnswers / totalQuestions * 100;
        String ratio = correctAnswers + "/" + totalQuestions;

        // Determine grade based on percentage
        String aiGrade;
        if (percentage >= 80) {
            aiGrade = "A";
        } else if (percentage >= 60) {
            aiGrade = "B";
        } else if (percentage >= 40) {
            aiGrade = "C";
        } else if (percentage >= 20) {
            aiGrade = "D";
        } else {
            aiGrade = "E";
        }

        // Save the grade to AIGrade table
        AIGrade gradeEntity = new AIGrade();
        gradeEntity.setAIGrade(aiGrade);
        gradeEntity.setAiQuiz(aiQuiz);
        gradeEntity.setAIFeedback("Feedback based on quiz performance"); // Example feedback, update as needed
        aiGradeRepo.save(gradeEntity);

        Map<String, Object> result = new HashMap<>();
        result.put("aiQuizId", aiQuizId);
        result.put("userId", userId);
        result.put("totalQuestions", totalQuestions);
        result.put("correctAnswers", correctAnswers);
        result.put("percentage", percentage);
        result.put("ratio", ratio);
        result.put("aiGrade", aiGrade); // Updated field name to AIGrade

        return result;
    }
}