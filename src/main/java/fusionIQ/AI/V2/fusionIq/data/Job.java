package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_title")
    private String jobTitle;
    @Lob
    @Column(name = "job_description", length = 20000)
    private String jobDescription;

    @Lob
    @Column(name = "basic_job_qualification", length = 20000)
    private String basicJobQualification;

    @Lob
    @Column(name = "primary_roles", length = 20000)
    private String primaryRoles;

    @Lob
    @Column(name = "main_responsibilities", length = 20000)
    private String mainResponsibilities;


    private String requiredSkills;
    private String location;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String jobType;
    private String status;
    private Integer vacancyCount;
    private Integer appliedCount;
    private Integer numberOfLevels;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // New fields for job requirements
    @Column(name = "required_education")
    private String requiredEducation;

    @Column(name = "required_education_stream")
    private String requiredEducationStream;

    @Column(name = "required_percentage")
    private Double requiredPercentage;

    @Column(name = "required_passout_year")
    private Integer requiredPassoutYear;

    @Column(name = "required_work_experience")
    private Integer requiredWorkExperience;



    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    @ManyToOne
    @JoinColumn(name = "job_admin_id")
    private JobAdmin jobAdmin;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedJobs> savedJobs;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruiterFeedback> recruiterFeedbacks;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<ApplyJob> applyJobs;

    public Job() {
        this.createdAt = LocalDateTime.now();
        this.status = "open";
    }
    public Job(Long id) {
        this.id = id;
    }

    public Job(Long id, String jobTitle, String jobDescription, String basicJobQualification, String primaryRoles, String mainResponsibilities, String requiredSkills, String location, BigDecimal minSalary, BigDecimal maxSalary, String jobType, String status, Integer vacancyCount, Integer appliedCount, Integer numberOfLevels, LocalDateTime createdAt, String requiredEducation, String requiredEducationStream, Double requiredPercentage, Integer requiredPassoutYear, Integer requiredWorkExperience, Recruiter recruiter, JobAdmin jobAdmin, List<SavedJobs> savedJobs, List<RecruiterFeedback> recruiterFeedbacks, List<ApplyJob> applyJobs) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.basicJobQualification = basicJobQualification;
        this.primaryRoles = primaryRoles;
        this.mainResponsibilities = mainResponsibilities;
        this.requiredSkills = requiredSkills;
        this.location = location;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.jobType = jobType;
        this.status = status;
        this.vacancyCount = vacancyCount;
        this.appliedCount = appliedCount;
        this.numberOfLevels = numberOfLevels;
        this.createdAt = createdAt;
        this.requiredEducation = requiredEducation;
        this.requiredEducationStream = requiredEducationStream;
        this.requiredPercentage = requiredPercentage;
        this.requiredPassoutYear = requiredPassoutYear;
        this.requiredWorkExperience = requiredWorkExperience;
        this.recruiter = recruiter;
        this.jobAdmin = jobAdmin;
        this.savedJobs = savedJobs;
        this.recruiterFeedbacks = recruiterFeedbacks;
        this.applyJobs = applyJobs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getBasicJobQualification() {
        return basicJobQualification;
    }

    public void setBasicJobQualification(String basicJobQualification) {
        this.basicJobQualification = basicJobQualification;
    }

    public String getPrimaryRoles() {
        return primaryRoles;
    }

    public void setPrimaryRoles(String primaryRoles) {
        this.primaryRoles = primaryRoles;
    }

    public String getMainResponsibilities() {
        return mainResponsibilities;
    }

    public void setMainResponsibilities(String mainResponsibilities) {
        this.mainResponsibilities = mainResponsibilities;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVacancyCount() {
        return vacancyCount;
    }

    public void setVacancyCount(Integer vacancyCount) {
        this.vacancyCount = vacancyCount;
    }

    public Integer getAppliedCount() {
        return appliedCount;
    }

    public void setAppliedCount(Integer appliedCount) {
        this.appliedCount = appliedCount;
    }

    public Integer getNumberOfLevels() {
        return numberOfLevels;
    }

    public void setNumberOfLevels(Integer numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRequiredEducation() {
        return requiredEducation;
    }

    public void setRequiredEducation(String requiredEducation) {
        this.requiredEducation = requiredEducation;
    }

    public String getRequiredEducationStream() {
        return requiredEducationStream;
    }

    public void setRequiredEducationStream(String requiredEducationStream) {
        this.requiredEducationStream = requiredEducationStream;
    }

    public Double getRequiredPercentage() {
        return requiredPercentage;
    }

    public void setRequiredPercentage(Double requiredPercentage) {
        this.requiredPercentage = requiredPercentage;
    }

    public Integer getRequiredPassoutYear() {
        return requiredPassoutYear;
    }

    public void setRequiredPassoutYear(Integer requiredPassoutYear) {
        this.requiredPassoutYear = requiredPassoutYear;
    }

    public Integer getRequiredWorkExperience() {
        return requiredWorkExperience;
    }

    public void setRequiredWorkExperience(Integer requiredWorkExperience) {
        this.requiredWorkExperience = requiredWorkExperience;
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

    public List<SavedJobs> getSavedJobs() {
        return savedJobs;
    }

    public void setSavedJobs(List<SavedJobs> savedJobs) {
        this.savedJobs = savedJobs;
    }

    public List<RecruiterFeedback> getRecruiterFeedbacks() {
        return recruiterFeedbacks;
    }

    public void setRecruiterFeedbacks(List<RecruiterFeedback> recruiterFeedbacks) {
        this.recruiterFeedbacks = recruiterFeedbacks;
    }

    public List<ApplyJob> getApplyJobs() {
        return applyJobs;
    }

    public void setApplyJobs(List<ApplyJob> applyJobs) {
        this.applyJobs = applyJobs;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", basicJobQualification='" + basicJobQualification + '\'' +
                ", primaryRoles='" + primaryRoles + '\'' +
                ", mainResponsibilities='" + mainResponsibilities + '\'' +
                ", requiredSkills='" + requiredSkills + '\'' +
                ", location='" + location + '\'' +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                ", jobType='" + jobType + '\'' +
                ", status='" + status + '\'' +
                ", vacancyCount=" + vacancyCount +
                ", appliedCount=" + appliedCount +
                ", numberOfLevels=" + numberOfLevels +
                ", createdAt=" + createdAt +
                ", requiredEducation='" + requiredEducation + '\'' +
                ", requiredEducationStream='" + requiredEducationStream + '\'' +
                ", requiredPercentage=" + requiredPercentage +
                ", requiredPassoutYear=" + requiredPassoutYear +
                ", requiredWorkExperience=" + requiredWorkExperience +
                ", recruiter=" + recruiter +
                ", jobAdmin=" + jobAdmin +
                ", savedJobs=" + savedJobs +
                ", recruiterFeedbacks=" + recruiterFeedbacks +
                ", applyJobs=" + applyJobs +
                '}';
    }
}


