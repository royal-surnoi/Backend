package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

@Entity
@Table(name = "ai_course_assignment_grades")
public class AICourseAssignmentGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ai_course_assignment_id", nullable = false)
    private AICourseAssignment aiCourseAssignment;

    @Column(name = "user_id", nullable = false, columnDefinition = "TEXT")
    private String userId;

    @Column(name = "grade", nullable = false)
    private String grade;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Lob
    @Column(name = "feedback", columnDefinition = "LONGTEXT")
    private String feedback;

    public AICourseAssignmentGrade() {}

    public AICourseAssignmentGrade(AICourseAssignment aiCourseAssignment, String userId, String grade, Integer score, String feedback) {
        this.aiCourseAssignment = aiCourseAssignment;
        this.userId = userId;
        this.grade = grade;
        this.score = score;
        this.feedback = feedback;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AICourseAssignment getAiCourseAssignment() {
        return aiCourseAssignment;
    }

    public void setAiCourseAssignment(AICourseAssignment aiCourseAssignment) {
        this.aiCourseAssignment = aiCourseAssignment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "AICourseAssignmentGrade{" +
                "id=" + id +
                ", aiCourseAssignment=" + aiCourseAssignment +
                ", userId='" + userId + '\'' +
                ", grade='" + grade + '\'' +
                ", score=" + score +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}