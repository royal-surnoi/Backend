package fusionIQ.AI.V2.fusionIq.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
public class AIQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String AIQuizName;



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

    public AIQuiz() {
        this.createdAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAIQuizName() {
        return AIQuizName;
    }

    public void setAIQuizName(String AIQuizName) {
        this.AIQuizName = AIQuizName;
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

    public AIQuiz(long id, String AIQuizName, Lesson lesson, Course course, User user, LocalDateTime createdAt) {
        this.id = id;
        this.AIQuizName = AIQuizName;
        this.lesson = lesson;
        this.course = course;
        this.user = user;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AIQuiz{" +
                "id=" + id +
                ", AIQuizName='" + AIQuizName + '\'' +
                ", lesson=" + lesson +
                ", course=" + course +
                ", user=" + user +
                ", createdAt=" + createdAt +
                '}';
    }

    public Map<Object, Object> getQuestions() {
        return getQuestions();
    }
}
