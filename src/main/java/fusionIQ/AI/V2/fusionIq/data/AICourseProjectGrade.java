package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

@Entity
@Table(name = "ai_course_project_grades")
public class AICourseProjectGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ai_course_project_id", nullable = false)
    private AICourseProject aiCourseProject;

    @Column(name = "user_id", nullable = false, columnDefinition = "TEXT")
    private String userId;

    @Column(name = "grade", nullable = false)
    private String grade;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Lob
    @Column(name = "feedback", columnDefinition = "LONGTEXT")
    private String feedback;

    public AICourseProjectGrade() {}

    public AICourseProjectGrade(AICourseProject aiCourseProject, String userId, String grade, Integer score, String feedback) {
        this.aiCourseProject = aiCourseProject;
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

    public AICourseProject getAiCourseProject() {
        return aiCourseProject;
    }

    public void setAiCourseProject(AICourseProject aiCourseProject) {
        this.aiCourseProject = aiCourseProject;
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
        return "AICourseProjectGrade{" +
                "id=" + id +
                ", aiCourseProject=" + aiCourseProject +
                ", userId='" + userId + '\'' +
                ", grade='" + grade + '\'' +
                ", score=" + score +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}