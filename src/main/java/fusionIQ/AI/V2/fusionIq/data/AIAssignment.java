package fusionIQ.AI.V2.fusionIq.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AIAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "LONGTEXT")
    private String AIAssignmentQuestion;

    @Column(columnDefinition = "LONGTEXT")
    private String AIAssignmentAnswer;

    @Column(columnDefinition = "LONGTEXT")
    private String AIAssignmentUserAnswer;
    @Column(columnDefinition = "LONGTEXT")
    private String AIAssignmentDescription;


    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAIAssignmentQuestion() {
        return AIAssignmentQuestion;
    }

    public void setAIAssignmentQuestion(String AIAssignmentQuestion) {
        this.AIAssignmentQuestion = AIAssignmentQuestion;
    }

    public String getAIAssignmentAnswer() {
        return AIAssignmentAnswer;
    }

    public void setAIAssignmentAnswer(String AIAssignmentAnswer) {
        this.AIAssignmentAnswer = AIAssignmentAnswer;
    }

    public String getAIAssignmentUserAnswer() {
        return AIAssignmentUserAnswer;
    }

    public void setAIAssignmentUserAnswer(String AIAssignmentUserAnswer) {
        this.AIAssignmentUserAnswer = AIAssignmentUserAnswer;
    }

    public String getAIAssignmentDescription() {
        return AIAssignmentDescription;
    }

    public void setAIAssignmentDescription(String AIAssignmentDescription) {
        this.AIAssignmentDescription = AIAssignmentDescription;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public AIAssignment(long id, String AIAssignmentQuestion, String AIAssignmentAnswer, String AIAssignmentUserAnswer, String AIAssignmentDescription, Lesson lesson, Course course, User user, LocalDateTime createdAt) {
        this.id = id;
        this.AIAssignmentQuestion = AIAssignmentQuestion;
        this.AIAssignmentAnswer = AIAssignmentAnswer;
        this.AIAssignmentUserAnswer = AIAssignmentUserAnswer;
        this.AIAssignmentDescription = AIAssignmentDescription;
        this.lesson = lesson;
        this.course = course;
        this.user = user;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AIAssignment{" +
                "id=" + id +
                ", AIAssignmentQuestion='" + AIAssignmentQuestion + '\'' +
                ", AIAssignmentAnswer='" + AIAssignmentAnswer + '\'' +
                ", AIAssignmentUserAnswer='" + AIAssignmentUserAnswer + '\'' +
                ", AIAssignmentDescription='" + AIAssignmentDescription + '\'' +
                ", lessonId=" + (lesson != null ? lesson.getId() : "null") +
                ", courseId=" + (course != null ? course.getId() : "null") +
                ", userId=" + (user != null ? user.getId() : "null") +
                ", createdAt=" + createdAt +
                '}';
    }


    public AIAssignment() {
        this.createdAt = LocalDateTime.now();
    }
}
