package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class JobQuizProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String userName;
    private String userEmail;

    private Long recruiterId;
    private String recruiterName;
    private String recruiterEmail;

    private Long adminId;
    private String adminName;
    private String adminEmail;

    private Double scorePercentage;
    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "job_quiz_id")
    private JobQuiz jobQuiz;

    @OneToMany(mappedBy = "jobQuizProgress", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;


    public JobQuizProgress() {
    }

    public JobQuizProgress(Long id, Long userId, String userName, String userEmail, Long recruiterId, String recruiterName, String recruiterEmail, Long adminId, String adminName, String adminEmail, Double scorePercentage, LocalDateTime completedAt, JobQuiz jobQuiz, List<Answer> answers) {
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
        this.scorePercentage = scorePercentage;
        this.completedAt = completedAt;
        this.jobQuiz = jobQuiz;
        this.answers = answers;
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

    public Double getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(Double scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public JobQuiz getJobQuiz() {
        return jobQuiz;
    }

    public void setJobQuiz(JobQuiz jobQuiz) {
        this.jobQuiz = jobQuiz;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    // toString method
    @Override
    public String toString() {
        return "JobQuizProgress{" +
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
                ", scorePercentage=" + scorePercentage +
                ", completedAt=" + completedAt +
                ", jobQuiz=" + jobQuiz +
                '}';
    }
}
