package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "job_quiz")
public class JobQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quiz_name", nullable = false)
    private String quizName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "recruiter_id")
    private Long recruiterId; // Nullable column

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "user_ids")
    private String userIds;

    @Column(name = "shortlisted_candidate_ids")
    private String shortlistedCandidateIds;

    @Column(name = "admin_id", nullable = true)
    private Long adminId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", insertable = false, updatable = false)
    private Job job;

    @OneToMany(mappedBy = "jobQuiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShortlistedCandidates> shortlistedCandidates;

    @OneToMany(mappedBy = "jobQuiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "jobQuiz", cascade = CascadeType.ALL)
    private List<Answer> answers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", insertable = false, updatable = false)
    private Recruiter recruiter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", insertable = false, updatable = false)
    private JobAdmin jobAdmin;


    public JobQuiz() {}

    public JobQuiz(Long id, String quizName, LocalDate startDate, LocalDate endDate, Long recruiterId, Long jobId, String userIds, String shortlistedCandidateIds, Long adminId, Job job, List<ShortlistedCandidates> shortlistedCandidates, List<Question> questions, List<Answer> answers, Recruiter recruiter, JobAdmin jobAdmin) {
        this.id = id;
        this.quizName = quizName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.recruiterId = recruiterId;
        this.jobId = jobId;
        this.userIds = userIds;
        this.shortlistedCandidateIds = shortlistedCandidateIds;
        this.adminId = adminId;
        this.job = job;
        this.shortlistedCandidates = shortlistedCandidates;
        this.questions = questions;
        this.answers = answers;
        this.recruiter = recruiter;
        this.jobAdmin = jobAdmin;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getShortlistedCandidateIds() {
        return shortlistedCandidateIds;
    }

    public void setShortlistedCandidateIds(String shortlistedCandidateIds) {
        this.shortlistedCandidateIds = shortlistedCandidateIds;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public List<ShortlistedCandidates> getShortlistedCandidates() {
        return shortlistedCandidates;
    }

    public void setShortlistedCandidates(List<ShortlistedCandidates> shortlistedCandidates) {
        this.shortlistedCandidates = shortlistedCandidates;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }

    public JobAdmin getJobAdmin() {
        return jobAdmin;
    }

    public void setJobAdmin(JobAdmin jobAdmin) {
        this.jobAdmin = jobAdmin;
    }

    @Override
    public String toString() {
        return "JobQuiz{" +
                "id=" + id +
                ", quizName='" + quizName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", recruiterId=" + recruiterId +
                ", jobId=" + jobId +
                ", userIds='" + userIds + '\'' +
                ", shortlistedCandidateIds='" + shortlistedCandidateIds + '\'' +
                ", adminId=" + adminId +
                ", job=" + job +
                ", shortlistedCandidates=" + shortlistedCandidates +
                ", questions=" + questions +
                ", answers=" + answers +
                ", recruiter=" + recruiter +
                ", jobAdmin=" + jobAdmin +
                '}';
    }
}

