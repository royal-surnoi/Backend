package fusionIQ.AI.V2.fusionIq.data;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;


@Entity
public class AIQuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String AIOption1;
    private String AIOption2;
    private String AIOption3;
    private String AIOption4;
    private String AIQuizCorrectAnswer;
    private String AIQuizQuestion;
    private String AIQuizAnswerDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aiQuiz_id")
    private AIQuiz aiQuiz;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAIOption1() {
        return AIOption1;
    }

    public void setAIOption1(String AIOption1) {
        this.AIOption1 = AIOption1;
    }

    public String getAIOption2() {
        return AIOption2;
    }

    public void setAIOption2(String AIOption2) {
        this.AIOption2 = AIOption2;
    }

    public String getAIOption3() {
        return AIOption3;
    }

    public void setAIOption3(String AIOption3) {
        this.AIOption3 = AIOption3;
    }

    public String getAIOption4() {
        return AIOption4;
    }

    public void setAIOption4(String AIOption4) {
        this.AIOption4 = AIOption4;
    }

    public String getAIQuizCorrectAnswer() {
        return AIQuizCorrectAnswer;
    }

    public void setAIQuizCorrectAnswer(String AIQuizCorrectAnswer) {
        this.AIQuizCorrectAnswer = AIQuizCorrectAnswer;
    }

    public String getAIQuizQuestion() {
        return AIQuizQuestion;
    }

    public void setAIQuizQuestion(String AIQuizQuestion) {
        this.AIQuizQuestion = AIQuizQuestion;
    }

    public String getAIQuizAnswerDescription() {
        return AIQuizAnswerDescription;
    }

    public void setAIQuizAnswerDescription(String AIQuizAnswerDescription) {
        this.AIQuizAnswerDescription = AIQuizAnswerDescription;
    }

    public AIQuiz getAiQuiz() {
        return aiQuiz;
    }

    public void setAiQuiz(AIQuiz aiQuiz) {
        this.aiQuiz = aiQuiz;
    }

    public AIQuizQuestion(Long id, String AIOption1, String AIOption2, String AIOption3, String AIOption4, String AIQuizCorrectAnswer, String AIQuizQuestion, String AIQuizAnswerDescription, AIQuiz aiQuiz) {
        this.id = id;
        this.AIOption1 = AIOption1;
        this.AIOption2 = AIOption2;
        this.AIOption3 = AIOption3;
        this.AIOption4 = AIOption4;
        this.AIQuizCorrectAnswer = AIQuizCorrectAnswer;
        this.AIQuizQuestion = AIQuizQuestion;
        this.AIQuizAnswerDescription = AIQuizAnswerDescription;
        this.aiQuiz = aiQuiz;
    }

    @Override
    public String toString() {
        return "AIQuizQuestion{" +
                "id=" + id +
                ", AIOption1='" + AIOption1 + '\'' +
                ", AIOption2='" + AIOption2 + '\'' +
                ", AIOption3='" + AIOption3 + '\'' +
                ", AIOption4='" + AIOption4 + '\'' +
                ", AIQuizCorrectAnswer='" + AIQuizCorrectAnswer + '\'' +
                ", AIQuizQuestion='" + AIQuizQuestion + '\'' +
                ", AIQuizAnswerDescription='" + AIQuizAnswerDescription + '\'' +
                ", aiQuiz=" + aiQuiz +
                '}';
    }

    public AIQuizQuestion() {
    }
}
