package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_course_plan")
public class AICoursePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "course_plan_name", nullable = false)
    private String coursePlanName;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String learningGoal;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String courseStartingPoint;

    @Column(name = "preferences")
    private String preferences;

    @Column(name = "week_duration", nullable = false)
    private int weekDuration;

    @Column(name = "hours_per_week", nullable = false)
    private int hoursPerWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public AICoursePlan() {
    }

    public AICoursePlan(Long id, LocalDateTime createdAt, String coursePlanName, String learningGoal, String courseStartingPoint, String preferences, int weekDuration, int hoursPerWeek, User user) {
        this.id = id;
        this.createdAt = createdAt;
        this.coursePlanName = coursePlanName;
        this.learningGoal = learningGoal;
        this.courseStartingPoint = courseStartingPoint;
        this.preferences = preferences;
        this.weekDuration = weekDuration;
        this.hoursPerWeek = hoursPerWeek;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCoursePlanName() {
        return coursePlanName;
    }

    public void setCoursePlanName(String coursePlanName) {
        this.coursePlanName = coursePlanName;
    }

    public String getLearningGoal() {
        return learningGoal;
    }

    public void setLearningGoal(String learningGoal) {
        this.learningGoal = learningGoal;
    }

    public String getCourseStartingPoint() {
        return courseStartingPoint;
    }

    public void setCourseStartingPoint(String courseStartingPoint) {
        this.courseStartingPoint = courseStartingPoint;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public int getWeekDuration() {
        return weekDuration;
    }

    public void setWeekDuration(int weekDuration) {
        this.weekDuration = weekDuration;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(int hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AICoursePlan{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", coursePlanName='" + coursePlanName + '\'' +
                ", learningGoal='" + learningGoal + '\'' +
                ", courseStartingPoint='" + courseStartingPoint + '\'' +
                ", preferences='" + preferences + '\'' +
                ", weekDuration=" + weekDuration +
                ", hoursPerWeek=" + hoursPerWeek +
                ", user=" + user +
                '}';
    }
}

