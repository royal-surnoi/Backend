package fusionIQ.AI.V2.fusionIq.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String projectTitle;
    private String projectDescription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @Lob
    @Column(length = 100000)
    private byte[] projectDocument;

    @ManyToOne
    @JoinColumn(name = "mock_id")
    private MockTestInterview mockTestInterview;

    private LocalDateTime projectDeadline;
    private LocalDateTime startDate;

    private LocalDateTime reviewMeetDate;

    private Long maxTeam;

    private String gitUrl;

    private String studentIds;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL)
    private List<Submission> submissions;


    @OneToMany(mappedBy = "project")
    private List<Grade> grades;



    public Project() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.reviewMeetDate = LocalDateTime.now();
        this.startDate = LocalDateTime.now();
        this.projectDeadline = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public byte[] getProjectDocument() {
        return projectDocument;
    }

    public void setProjectDocument(byte[] projectDocument) {
        this.projectDocument = projectDocument;
    }

    public MockTestInterview getMockTestInterview() {
        return mockTestInterview;
    }

    public void setMockTestInterview(MockTestInterview mockTestInterview) {
        this.mockTestInterview = mockTestInterview;
    }

    public LocalDateTime getProjectDeadline() {
        return projectDeadline;
    }

    public void setProjectDeadline(LocalDateTime projectDeadline) {
        this.projectDeadline = projectDeadline;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getReviewMeetDate() {
        return reviewMeetDate;
    }

    public void setReviewMeetDate(LocalDateTime reviewMeetDate) {
        this.reviewMeetDate = reviewMeetDate;
    }

    public Long getMaxTeam() {
        return maxTeam;
    }

    public void setMaxTeam(Long maxTeam) {
        this.maxTeam = maxTeam;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(String studentIds) {
        this.studentIds = studentIds;
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

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public Project(long id, Course course, String projectTitle, String projectDescription, User user, User teacher, User student, byte[] projectDocument, MockTestInterview mockTestInterview, LocalDateTime projectDeadline, LocalDateTime startDate, LocalDateTime reviewMeetDate, Long maxTeam, String gitUrl, String studentIds, LocalDateTime createdAt, LocalDateTime updatedAt, List<Submission> submissions, List<Grade> grades) {
        this.id = id;
        this.course = course;
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.user = user;
        this.teacher = teacher;
        this.student = student;
        this.projectDocument = projectDocument;
        this.mockTestInterview = mockTestInterview;
        this.projectDeadline = projectDeadline;
        this.startDate = startDate;
        this.reviewMeetDate = reviewMeetDate;
        this.maxTeam = maxTeam;
        this.gitUrl = gitUrl;
        this.studentIds = studentIds;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.submissions = submissions;
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", course=" + course +
                ", projectTitle='" + projectTitle + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                ", user=" + user +
                ", teacher=" + teacher +
                ", student=" + student +
                ", projectDocument=" + Arrays.toString(projectDocument) +
                ", mockTestInterview=" + mockTestInterview +
                ", projectDeadline=" + projectDeadline +
                ", startDate=" + startDate +
                ", reviewMeetDate=" + reviewMeetDate +
                ", maxTeam=" + maxTeam +
                ", gitUrl='" + gitUrl + '\'' +
                ", studentIds='" + studentIds + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", submissions=" + submissions +
                ", grades=" + grades +
                '}';
    }
}

