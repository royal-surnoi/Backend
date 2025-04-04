package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "apply_jobs")
public class ApplyJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "applyJob", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShortlistedCandidates> shortlistedCandidates;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;


    private String status; // e.g., Applied, Shortlisted, Rejected
    @Column(name = "last_updated", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")  // Ensure correct format in response
    private LocalDateTime lastUpdated;


    @Lob
    @Column(name = "resume", columnDefinition = "LONGBLOB")  // Ensure column is defined as BLOB in DB
    private byte[] resume;

    private String withdraw;
    private Long recruiterId;
    private Long adminId;

    public ApplyJob() {}

    public ApplyJob(User user, Job job, String status, byte[] resume) {
        this.user = user;
        this.job = job;
        this.status = status;
        this.resume = resume;
        this.withdraw = "no";
        this.lastUpdated = LocalDateTime.now();

    }
    @PrePersist
    protected void onCreate() {
        this.lastUpdated = LocalDateTime.now();
    }

    // Automatically update timestamp on update
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public byte[] getResume() {
        return resume;
    }

    public void setResume(byte[] resume) {
        this.resume = resume;
    }

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }
    public List<ShortlistedCandidates> getShortlistedCandidates() {
        return shortlistedCandidates;
    }

    public void setShortlistedCandidates(List<ShortlistedCandidates> shortlistedCandidates) {
        this.shortlistedCandidates = shortlistedCandidates;
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
        return "ApplyJob{" +
                "id=" + id +
                ", user=" + user +
                ", job=" + job +
                ", status='" + status + '\'' +
                ", lastUpdated=" + lastUpdated +
                ", resume=" + Arrays.toString(resume) +
                ", withdraw='" + withdraw + '\'' +
                ", shortlistedCandidates=" + shortlistedCandidates +
                '}';
    }



}



