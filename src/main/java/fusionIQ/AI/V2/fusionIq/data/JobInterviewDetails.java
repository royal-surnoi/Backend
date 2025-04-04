package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_interview_details")
public class JobInterviewDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String userName;

    @Column(nullable = false, length = 100)
    private String userEmail;

    @Column(nullable = true)
    private Long recruiterId;

    @Column(nullable = true, length = 100)
    private String recruiterName;

    @Column(nullable = true, length = 100)
    private String recruiterEmail;

    @Column(nullable = true)
    private Long adminId;

    @Column(nullable = true, length = 100)
    private String adminName;

    @Column(nullable = true, length = 100)
    private String adminEmail;

    @Column(nullable = true)
    private LocalDateTime interviewTimestamp;

    @Column(nullable = true, length = 255)
    private String interviewLink;

    @Column(nullable = true, length = 255)
    private String interviewScoreLink;

    @Column(nullable = true, length = 50000)
    private String interviewDescription;

    @Column(nullable = true, length = 100)
    private String interviewerEmail;

    @Column(nullable = true, length = 100)
    private String interviewerName;

    private Integer interviewerScore;

    @Column(nullable = true, length = 500)
    private String interviewerFeedback;

    @Column(nullable = true, length = 50)
    private String currentLevel;

    @Column(nullable = true, length = 50)
    private String upcomingLevel;

    @Column(nullable = true, length = 5000)
    private String currentFeedback;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    // Default constructor
    public JobInterviewDetails() {}

    // Parameterized constructor
    public JobInterviewDetails(Long id, Long userId, String userName, String userEmail, Long recruiterId, String recruiterName,
                               String recruiterEmail, Long adminId, String adminName, String adminEmail,
                               LocalDateTime interviewTimestamp, String interviewLink, String interviewScoreLink,
                               String interviewDescription, String interviewerEmail, String interviewerName,
                               Integer interviewerScore, String interviewerFeedback, String currentLevel, String upcomingLevel,
                               String currentFeedback, Job job) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.recruiterId = recruiterId;
        this.recruiterName = recruiterName;
        this.recruiterEmail = recruiterEmail;
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.interviewTimestamp = interviewTimestamp;
        this.interviewLink = interviewLink;
        this.interviewScoreLink = interviewScoreLink;
        this.interviewDescription = interviewDescription;
        this.interviewerEmail = interviewerEmail;
        this.interviewerName = interviewerName;
        this.interviewerScore = interviewerScore;
        this.interviewerFeedback = interviewerFeedback;
        this.currentLevel = currentLevel;
        this.upcomingLevel = upcomingLevel;
        this.currentFeedback = currentFeedback;
        this.job = job;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public String getRecruiterEmail() {
        return recruiterEmail;
    }

    public void setRecruiterEmail(String recruiterEmail) {
        this.recruiterEmail = recruiterEmail;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public LocalDateTime getInterviewTimestamp() {
        return interviewTimestamp;
    }

    public void setInterviewTimestamp(LocalDateTime interviewTimestamp) {
        this.interviewTimestamp = interviewTimestamp;
    }

    public String getInterviewLink() {
        return interviewLink;
    }

    public void setInterviewLink(String interviewLink) {
        this.interviewLink = interviewLink;
    }

    public String getInterviewScoreLink() {
        return interviewScoreLink;
    }

    public void setInterviewScoreLink(String interviewScoreLink) {
        this.interviewScoreLink = interviewScoreLink;
    }

    public String getInterviewDescription() {
        return interviewDescription;
    }

    public void setInterviewDescription(String interviewDescription) {
        this.interviewDescription = interviewDescription;
    }

    public String getInterviewerEmail() {
        return interviewerEmail;
    }

    public void setInterviewerEmail(String interviewerEmail) {
        this.interviewerEmail = interviewerEmail;
    }

    public String getInterviewerName() {
        return interviewerName;
    }

    public void setInterviewerName(String interviewerName) {
        this.interviewerName = interviewerName;
    }

    public Integer getInterviewerScore() {
        return interviewerScore;
    }

    public void setInterviewerScore(Integer interviewerScore) {
        this.interviewerScore = interviewerScore;
    }

    public String getInterviewerFeedback() {
        return interviewerFeedback;
    }

    public void setInterviewerFeedback(String interviewerFeedback) {
        this.interviewerFeedback = interviewerFeedback;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getUpcomingLevel() {
        return upcomingLevel;
    }

    public void setUpcomingLevel(String upcomingLevel) {
        this.upcomingLevel = upcomingLevel;
    }

    public String getCurrentFeedback() {
        return currentFeedback;
    }

    public void setCurrentFeedback(String currentFeedback) {
        this.currentFeedback = currentFeedback;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "JobInterviewDetails{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", recruiterId=" + recruiterId +
                ", recruiterName='" + recruiterName + '\'' +
                ", recruiterEmail='" + recruiterEmail + '\'' +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", adminEmail='" + adminEmail + '\'' +
                ", interviewTimestamp=" + interviewTimestamp +
                ", interviewLink='" + interviewLink + '\'' +
                ", interviewScoreLink='" + interviewScoreLink + '\'' +
                ", interviewDescription='" + interviewDescription + '\'' +
                ", interviewerEmail='" + interviewerEmail + '\'' +
                ", interviewerName='" + interviewerName + '\'' +
                ", interviewerScore=" + interviewerScore +
                ", interviewerFeedback='" + interviewerFeedback + '\'' +
                ", currentLevel='" + currentLevel + '\'' +
                ", upcomingLevel='" + upcomingLevel + '\'' +
                ", currentFeedback='" + currentFeedback + '\'' +
                ", job=" + job +
                '}';
    }
}
