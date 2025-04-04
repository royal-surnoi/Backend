package fusionIQ.AI.V2.fusionIq.data;


import jakarta.persistence.*;

@Entity
public class AIGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String AIGrade;

    @Column(length = 100000)
    private String AIFeedback;

    @ManyToOne
    @JoinColumn(name = "aiAssignment_id")
    private AIAssignment aiAssignment;

    @ManyToOne
    @JoinColumn(name = "aiQuiz_id")
    private AIQuiz aiQuiz;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAIGrade() {
        return AIGrade;
    }

    public void setAIGrade(String AIGrade) {
        this.AIGrade = AIGrade;
    }

    public String getAIFeedback() {
        return AIFeedback;
    }

    public void setAIFeedback(String AIFeedback) {
        this.AIFeedback = AIFeedback;
    }

    public AIAssignment getAiAssignment() {
        return aiAssignment;
    }

    public void setAiAssignment(AIAssignment aiAssignment) {
        this.aiAssignment = aiAssignment;
    }

    public AIQuiz getAiQuiz() {
        return aiQuiz;
    }

    public void setAiQuiz(AIQuiz aiQuiz) {
        this.aiQuiz = aiQuiz;
    }

    public AIGrade(Long id, String AIGrade, String AIFeedback, AIAssignment aiAssignment, AIQuiz aiQuiz) {
        this.id = id;
        this.AIGrade = AIGrade;
        this.AIFeedback = AIFeedback;
        this.aiAssignment = aiAssignment;
        this.aiQuiz = aiQuiz;
    }

    @Override
    public String toString() {
        return "AIGrade{" +
                "id=" + id +
                ", AIGrade='" + AIGrade + '\'' +
                ", AIFeedback='" + AIFeedback + '\'' +
                ", aiAssignment=" + aiAssignment +
                ", aiQuiz=" + aiQuiz +
                '}';
    }

    public AIGrade() {
    }
}
