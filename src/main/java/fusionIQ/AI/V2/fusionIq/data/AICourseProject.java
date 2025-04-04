package fusionIQ.AI.V2.fusionIq.data;


import jakarta.persistence.*;

@Entity
@Table(name = "ai_course_projects")
public class AICourseProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String aiCourseProject;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String aiCourseProjectUserAnswer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ai_course_plan_id", nullable = false)
    private AICoursePlan aiCoursePlan;

    public AICourseProject() {}

    public AICourseProject(Long id, String aiCourseProject, AICoursePlan aiCoursePlan) {
        this.id = id;
        this.aiCourseProject = aiCourseProject;
        this.aiCoursePlan = aiCoursePlan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAiCourseProject() {
        return aiCourseProject;
    }

    public void setAiCourseProject(String aiCourseProject) {
        this.aiCourseProject = aiCourseProject;
    }

    public String getAiCourseProjectUserAnswer() {
        return aiCourseProjectUserAnswer;
    }

    public void setAiCourseProjectUserAnswer(String aiCourseProjectUserAnswer) {
        this.aiCourseProjectUserAnswer = aiCourseProjectUserAnswer;
    }

    public AICoursePlan getAiCoursePlan() {
        return aiCoursePlan;
    }

    public void setAiCoursePlan(AICoursePlan aiCoursePlan) {
        this.aiCoursePlan = aiCoursePlan;
    }

    @Override
    public String toString() {
        return "AICourseProject{" +
                "id=" + id +
                ", aiCourseProject='" + aiCourseProject + '\'' +
                ", aiCourseProjectUserAnswer='" + aiCourseProjectUserAnswer + '\'' +
                ", aiCoursePlan=" + aiCoursePlan +
                '}';
    }
}