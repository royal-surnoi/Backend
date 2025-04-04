package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.AIAssignmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.AIGradeRepo;
import fusionIQ.AI.V2.fusionIq.repository.AIQuizRepo;
import fusionIQ.AI.V2.fusionIq.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AIGradeService {

    @Autowired
    AIGradeRepo aiGradeRepo;
    @Autowired
    AIQuizRepo aiQuizRepo;
    @Autowired
    AIAssignmentRepo aiAssignmentRepo;

    @Autowired
    QuestionRepo questionRepo;

    public AIGrade saveAIQuizGrade(Long aiQuizId, String aiGrade, String aiFeedback) {
        AIQuiz aiQuiz = aiQuizRepo.findById(aiQuizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        AIGrade newAIGrade = new AIGrade();
        newAIGrade.setAIGrade(aiGrade);
        newAIGrade.setAIFeedback(aiFeedback);
        newAIGrade.setAiQuiz(aiQuiz);

        return aiGradeRepo.save(newAIGrade);
    }

    public AIGrade saveAIAssignmentGrade(Long assignmentId, String aiGrade, String aiFeedback) {
        AIAssignment aiAssignment = aiAssignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        AIGrade newAIGrade = new AIGrade();
        newAIGrade.setAIGrade(aiGrade);
        newAIGrade.setAIFeedback(aiFeedback);
        newAIGrade.setAiAssignment(aiAssignment);

        return aiGradeRepo.save(newAIGrade);
    }
    public List<AIGrade> getGradesByUserId(Long userId) {
        return aiGradeRepo.findByUserId(userId);
    }

    public List<AIGrade> getAllGrades() {
        return aiGradeRepo.findAll();
    }
    public void deleteGradeById(Long id) {
        if (aiGradeRepo.existsById(id)) {
            aiGradeRepo.deleteById(id);
        } else {
            throw new RuntimeException("Grade not found");
        }
    }

    public AIGrade submitGradeBasedOnAnswers(Long aiQuizId, List<Map<String, Object>> answers) {
        // Fetch the quiz and user details
        AIQuiz quiz = aiQuizRepo.findById(aiQuizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
//        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Calculate the total number of questions and correct answers
        int totalQuestions = quiz.getQuestions().size();
        int correctAnswers = 0;

        for (Map<String, Object> answer : answers) {
            Long questionId = ((Number) answer.get("questionId")).longValue();
            String selectedAnswer = (String) answer.get("selectedAnswer");

            Question question = questionRepo.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            // Check if the answer is correct
            if (question.getCorrectAnswer().equalsIgnoreCase(selectedAnswer)) {
                correctAnswers++;
            }
        }

        // Calculate the percentage of correct answers
        double percentage = (double) correctAnswers / totalQuestions * 100;

        // Determine the grade based on the percentage
        String grade = calculateGrade(percentage);

        // Create and save the AIGrade
        AIGrade newGrade = new AIGrade();
        newGrade.setAiQuiz(quiz);
        newGrade.setAIGrade(grade);

        return aiGradeRepo.save(newGrade);
    }

    private String calculateGrade(double percentage) {
        if (percentage >= 90) {
            return "A";
        } else if (percentage >= 80) {
            return "B";
        } else if (percentage >= 70) {
            return "C";
        } else if (percentage >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

}
