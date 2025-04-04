package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;



@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String preferences;
    private String profession;
    private String userLanguage;

    private String otp;
    private LocalDateTime otpGeneratedTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] userImage;

    @Enumerated(EnumType.STRING)
    private OnlineStatus onlineStatus;

    private LocalDateTime lastSeen;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> devices = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplyJob> applyJobs;

    // New Mappings
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDocunment> documents = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProjects> projects = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSkills> skills = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educationDetails = new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruiterFeedback> recruiterFeedbacks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobCommunity> jobCommunities = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostCommunity> postCommunities = new ArrayList<>();


    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Education education;
    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private PersonalDetails personalDetails;



    @Column(length = 1500)
    private String userDescription;



    public enum OnlineStatus {
        ONLINE,
        OFFLINE
    }

    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Institute> institutes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstituteTeacher> instituteTeachers = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpGeneratedTime() {
        return otpGeneratedTime;
    }

    public void setOtpGeneratedTime(LocalDateTime otpGeneratedTime) {
        this.otpGeneratedTime = otpGeneratedTime;
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

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(OnlineStatus onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<ApplyJob> getApplyJobs() {
        return applyJobs;
    }

    public void setApplyJobs(List<ApplyJob> applyJobs) {
        this.applyJobs = applyJobs;
    }

    public List<UserDocunment> getDocuments() {
        return documents;
    }

    public void setDocuments(List<UserDocunment> documents) {
        this.documents = documents;
    }

    public List<UserProjects> getProjects() {
        return projects;
    }

    public void setProjects(List<UserProjects> projects) {
        this.projects = projects;
    }

    public List<UserSkills> getSkills() {
        return skills;
    }

    public void setSkills(List<UserSkills> skills) {
        this.skills = skills;
    }

    public void setSelfIntroductionVideos(SelfIntroductionVideos videos) {
    }

    public List<Education> getEducationDetails() {
        return educationDetails;
    }

    public void setEducationDetails(List<Education> educationDetails) {
        this.educationDetails = educationDetails;
    }

    public List<RecruiterFeedback> getRecruiterFeedbacks() {
        return recruiterFeedbacks;
    }

    public void setRecruiterFeedbacks(List<RecruiterFeedback> recruiterFeedbacks) {
        this.recruiterFeedbacks = recruiterFeedbacks;
    }

    public List<JobCommunity> getJobCommunities() {
        return jobCommunities;
    }

    public void setJobCommunities(List<JobCommunity> jobCommunities) {
        this.jobCommunities = jobCommunities;
    }

    public List<PostCommunity> getPostCommunities() {
        return postCommunities;
    }

    public void setPostCommunities(List<PostCommunity> postCommunities) {
        this.postCommunities = postCommunities;
    }

    public List<InstituteTeacher> getInstituteTeachers() {
        return instituteTeachers;
    }

    public void setInstituteTeachers(List<InstituteTeacher> instituteTeachers) {
        this.instituteTeachers = instituteTeachers;
    }

    public List<Institute> getInstitutes() {
        return institutes;
    }

    public void setInstitutes(List<Institute> institutes) {
        this.institutes = institutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", preferences='" + preferences + '\'' +
                ", profession='" + profession + '\'' +
                ", userLanguage='" + userLanguage + '\'' +
                ", otp='" + otp + '\'' +
                ", otpGeneratedTime=" + otpGeneratedTime +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userImage=" + Arrays.toString(userImage) +
                ", onlineStatus=" + onlineStatus +
                ", lastSeen=" + lastSeen +
                ", devices=" + devices +
                ", applyJobs=" + applyJobs +
                ", documents=" + documents +
                ", projects=" + projects +
                ", skills=" + skills +
                ", educationDetails=" + educationDetails +
                ", recruiterFeedbacks=" + recruiterFeedbacks +
                ", jobCommunities=" + jobCommunities +
                ", postCommunities=" + postCommunities +
                ", education=" + education +
                ", personalDetails=" + personalDetails +
                ", userDescription='" + userDescription + '\'' +
                ", institutes=" + institutes +
                ", instituteTeachers=" + instituteTeachers +
                '}';
    }

    public User(Long id) {
        this.id = id;
    }
}
