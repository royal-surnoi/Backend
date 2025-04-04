package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
public class SubmitProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(length = 100000)
    private byte[] submitProject;

    private String userAnswer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submittedAt;

    private boolean isSubmitted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn
    private Course course;

    @ManyToOne
    @JoinColumn
    private Project project;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "mock_id")
    private MockTestInterview mockTestInterview;

    public SubmitProject() {
        this.submittedAt=LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getSubmitProject() {
        return submitProject;
    }

    public void setSubmitProject(byte[] submitProject) {
        this.submitProject = submitProject;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public MockTestInterview getMockTestInterview() {
        return mockTestInterview;
    }

    public void setMockTestInterview(MockTestInterview mockTestInterview) {
        this.mockTestInterview = mockTestInterview;
    }

    public SubmitProject(Long id, byte[] submitProject, String userAnswer, LocalDateTime submittedAt, boolean isSubmitted, User user, Course course, Project project, User student, MockTestInterview mockTestInterview) {
        this.id = id;
        this.submitProject = submitProject;
        this.userAnswer = userAnswer;
        this.submittedAt = submittedAt;
        this.isSubmitted = isSubmitted;
        this.user = user;
        this.course = course;
        this.project = project;
        this.student = student;
        this.mockTestInterview = mockTestInterview;
    }

    @Override
    public String toString() {
        return "SubmitProject{" +
                "id=" + id +
                ", submitProject=" + Arrays.toString(submitProject) +
                ", userAnswer='" + userAnswer + '\'' +
                ", submittedAt=" + submittedAt +
                ", isSubmitted=" + isSubmitted +
                ", user=" + user +
                ", course=" + course +
                ", project=" + project +
                ", student=" + student +
                ", mockTestInterview=" + mockTestInterview +
                '}';
    }
}