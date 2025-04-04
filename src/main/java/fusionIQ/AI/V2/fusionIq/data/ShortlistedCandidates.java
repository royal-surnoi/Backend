package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shortlisted_candidates")
public class ShortlistedCandidates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_job_id", nullable = false)
    private ApplyJob applyJob;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private JobQuiz jobQuiz;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime shortlistedAt;

    @Column(nullable = false)
    private String status; // e.g., Shortlisted
    @Column(name = "recruiter_id", nullable = true)
    private Long recruiterId;

    @Column(name = "admin_id", nullable = true)
    private Long adminId;

    public ShortlistedCandidates() {}

    public ShortlistedCandidates(ApplyJob applyJob, Job job, String status,  Long recruiterId, Long adminId) {
        this.applyJob = applyJob;
        this.job = job;
        this.status = status;
        this.shortlistedAt = LocalDateTime.now();
        this.recruiterId = recruiterId;
        this.adminId = adminId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplyJob getApplyJob() {
        return applyJob;
    }

    public void setApplyJob(ApplyJob applyJob) {
        this.applyJob = applyJob;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public LocalDateTime getShortlistedAt() {
        return shortlistedAt;
    }

    public void setShortlistedAt(LocalDateTime shortlistedAt) {
        this.shortlistedAt = shortlistedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public JobQuiz getJobQuiz() {
        return jobQuiz;
    }

    public void setJobQuiz(JobQuiz jobQuiz) {
        this.jobQuiz = jobQuiz;
    }


public Long getRecruiterId() {
    return recruiterId;
}

    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
    @Override
    public String toString() {
        return "ShortlistedCandidates{" +
                "id=" + id +
                ", applyJob=" + applyJob +
                ", job=" + job +
                ", user=" + user +
                ", shortlistedAt=" + shortlistedAt +
                ", status='" + status + '\'' +
                ", recruiterId=" + recruiterId +
                ", adminId=" + adminId +
                '}';
    }
}