package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class AIQuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("AIQuizUserAnswer")
    private String AIQuizUserAnswer;

    private boolean isCorrectAnswer;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private AIQuiz aiQuiz;

    @ManyToOne
    @JoinColumn
    private AIQuizQuestion aiQuizQuestion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAIQuizUserAnswer() {
        return AIQuizUserAnswer;
    }

    public void setAIQuizUserAnswer(String AIQuizUserAnswer) {
        this.AIQuizUserAnswer = AIQuizUserAnswer;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AIQuiz getAiQuiz() {
        return aiQuiz;
    }

    public void setAiQuiz(AIQuiz aiQuiz) {
        this.aiQuiz = aiQuiz;
    }

    public AIQuizQuestion getAiQuizQuestion() {
        return aiQuizQuestion;
    }

    public void setAiQuizQuestion(AIQuizQuestion aiQuizQuestion) {
        this.aiQuizQuestion = aiQuizQuestion;
    }

    public AIQuizAnswer(long id, String AIQuizUserAnswer, boolean isCorrectAnswer, User user, AIQuiz aiQuiz, AIQuizQuestion aiQuizQuestion) {
        this.id = id;
        this.AIQuizUserAnswer = AIQuizUserAnswer;
        this.isCorrectAnswer = isCorrectAnswer;
        this.user = user;
        this.aiQuiz = aiQuiz;
        this.aiQuizQuestion = aiQuizQuestion;
    }

    public AIQuizAnswer() {
    }

    @Override
    public String toString() {
        return "AIQuizAnswer{" +
                "id=" + id +
                ", AIQuizUserAnswer='" + AIQuizUserAnswer + '\'' +
                ", isCorrectAnswer=" + isCorrectAnswer +
                ", user=" + user +
                ", aiQuiz=" + aiQuiz +
                ", aiQuizQuestion=" + aiQuizQuestion +
                '}';
    }

}
