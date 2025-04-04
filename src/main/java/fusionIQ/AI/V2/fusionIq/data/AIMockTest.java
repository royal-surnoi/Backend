package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AIMockTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String jobRole;
    private String jobDescription;
    private String userExperience;
    private double score;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user;

    public AIMockTest(long id, String jobRole, String jobDescription, String userExperience, double score, LocalDateTime createdAt, User user) {
        this.id = id;
        this.jobRole = jobRole;
        this.jobDescription = jobDescription;
        this.userExperience = userExperience;
        this.score = score;
        this.createdAt = createdAt;
        this.user = user;
    }

    public AIMockTest() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(String userExperience) {
        this.userExperience = userExperience;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AIMockTest{" +
                "id=" + id +
                ", jobRole='" + jobRole + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", userExperience='" + userExperience + '\'' +
                ", score=" + score +
                ", createdAt=" + createdAt +
                ", user=" + user +
                '}';
    }
}
