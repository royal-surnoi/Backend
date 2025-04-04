package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recruiters")
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recruiterName;
    private String recruiterEmail;
    private String recruiterPassword;
    private String recruiterRole;
    private String recruiterDeportment ;

    private String recruiterOTP;
    private LocalDateTime recruiterOTPGeneratedTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "job_admin_id")
    private JobAdmin jobAdmin;  // Recruiter is added by a Job Admin

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
    private List<JobQuiz> jobQuizzes;



    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> recruiterJobs;

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruiterFeedback> recruiterFeedbacks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Recruiter() {}

    public Recruiter(Long id) {
        this.id = id;
    }

    public String getRecruiterDeportment() {
        return recruiterDeportment;
    }

    public void setRecruiterDeportment(String recruiterDeportment) {
        this.recruiterDeportment = recruiterDeportment;
    }

    public List<RecruiterFeedback> getRecruiterFeedbacks() {
        return recruiterFeedbacks;
    }

    public void setRecruiterFeedbacks(List<RecruiterFeedback> recruiterFeedbacks) {
        this.recruiterFeedbacks = recruiterFeedbacks;
    }

    public Recruiter(Long id, String recruiterName, String recruiterEmail, String recruiterPassword, String recruiterRole, String recruiterDeportment, LocalDateTime createdAt, LocalDateTime updatedAt, JobAdmin jobAdmin, List<JobQuiz> jobQuizzes, List<Job> recruiterJobs, List<RecruiterFeedback> recruiterFeedbacks) {
        this.id = id;
        this.recruiterName = recruiterName;
        this.recruiterEmail = recruiterEmail;
        this.recruiterPassword = recruiterPassword;
        this.recruiterRole = recruiterRole;
        this.recruiterDeportment = recruiterDeportment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.jobAdmin = jobAdmin;
        this.jobQuizzes = jobQuizzes;
        this.recruiterJobs = recruiterJobs;
        this.recruiterFeedbacks = recruiterFeedbacks;
    }




    public Long getId() {
        return id;
    }

    public String getRecruiterOTP() {
        return recruiterOTP;
    }

    public void setRecruiterOTP(String recruiterOTP) {
        this.recruiterOTP = recruiterOTP;
    }

    public LocalDateTime getRecruiterOTPGeneratedTime() {
        return recruiterOTPGeneratedTime;
    }

    public void setRecruiterOTPGeneratedTime(LocalDateTime recruiterOTPGeneratedTime) {
        this.recruiterOTPGeneratedTime = recruiterOTPGeneratedTime;
    }


    public Recruiter(Long id, String recruiterName, String recruiterEmail, String recruiterPassword, String recruiterRole, String recruiterDeportment, String recruiterOTP, LocalDateTime recruiterOTPGeneratedTime, LocalDateTime createdAt, LocalDateTime updatedAt, JobAdmin jobAdmin, List<JobQuiz> jobQuizzes, List<Job> recruiterJobs, List<RecruiterFeedback> recruiterFeedbacks, User user) {
        this.id = id;
        this.recruiterName = recruiterName;
        this.recruiterEmail = recruiterEmail;
        this.recruiterPassword = recruiterPassword;
        this.recruiterRole = recruiterRole;
        this.recruiterDeportment = recruiterDeportment;
        this.recruiterOTP = recruiterOTP;
        this.recruiterOTPGeneratedTime = recruiterOTPGeneratedTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.jobAdmin = jobAdmin;
        this.jobQuizzes = jobQuizzes;
        this.recruiterJobs = recruiterJobs;
        this.recruiterFeedbacks = recruiterFeedbacks;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Recruiter{" +
                "id=" + id +
                ", recruiterName='" + recruiterName + '\'' +
                ", recruiterEmail='" + recruiterEmail + '\'' +
                ", recruiterPassword='" + recruiterPassword + '\'' +
                ", recruiterRole='" + recruiterRole + '\'' +
                ", recruiterDeportment='" + recruiterDeportment + '\'' +
                ", recruiterOTP='" + recruiterOTP + '\'' +
                ", recruiterOTPGeneratedTime=" + recruiterOTPGeneratedTime +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", jobAdmin=" + jobAdmin +
                ", jobQuizzes=" + jobQuizzes +
                ", recruiterJobs=" + recruiterJobs +
                ", recruiterFeedbacks=" + recruiterFeedbacks +
                ", user=" + user +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRecruiterPassword() {
        return recruiterPassword;
    }

    public void setRecruiterPassword(String recruiterPassword) {
        this.recruiterPassword = recruiterPassword;
    }

    public List<Job> getRecruiterJobs() {
        return recruiterJobs;
    }

    public void setRecruiterJobs(List<Job> recruiterJobs) {
        this.recruiterJobs = recruiterJobs;
    }

    public JobAdmin getJobAdmin() {
        return jobAdmin;
    }

    public void setJobAdmin(JobAdmin jobAdmin) {
        this.jobAdmin = jobAdmin;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    //    public Company getCompany() {
//        return company;
//    }
//
//    public void setCompany(Company company) {
//        this.company = company;
//    }

    public String getRecruiterRole() {
        return recruiterRole;
    }

    public void setRecruiterRole(String recruiterRole) {
        this.recruiterRole = recruiterRole;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public List<JobQuiz> getJobQuizzes() {
        return jobQuizzes;
    }

    public void setJobQuizzes(List<JobQuiz> jobQuizzes) {
        this.jobQuizzes = jobQuizzes;
    }
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}