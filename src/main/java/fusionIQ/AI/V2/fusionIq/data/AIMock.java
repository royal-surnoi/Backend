package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AIMock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String jobRole;
    @Lob
    @Column(name = "job_description", length = 20000)
    private String jobDescription;
    private int mockScore;
    private String feedBack;
    @Column(name = "experience")
    private int experience;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user;

    public AIMock(long id, String jobRole, String jobDescription, int mockScore, String feedBack, int experience, LocalDateTime createdAt, User user) {
        this.id = id;
        this.jobRole = jobRole;
        this.jobDescription = jobDescription;
        this.mockScore = mockScore;
        this.feedBack = feedBack;
        this.experience = experience;
        this.createdAt = createdAt;
        this.user = user;
    }

    public AIMock() {
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

    public int getMockScore() {
        return mockScore;
    }

    public void setMockScore(int mockScore) {
        this.mockScore = mockScore;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
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
        return "AIMock{" +
                "id=" + id +
                ", jobRole='" + jobRole + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", mockScore=" + mockScore +
                ", feedBack='" + feedBack + '\'' +
                ", experience=" + experience +
                ", createdAt=" + createdAt +
                ", user=" + user +
                '}';
    }
}
