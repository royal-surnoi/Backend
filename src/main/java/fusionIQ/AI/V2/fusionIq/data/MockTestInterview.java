package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Table(name = "MockTestsInterviews")
public class MockTestInterview {

    public enum TestType {
        PROJECT,
        QUIZ,
        ASSIGNMENT,
        INTERVIEW
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_course_id", nullable = true)
    private Course relatedCourseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_type", nullable = false)
    private TestType testType;

    @Column(nullable = false)
    private BigDecimal fee = BigDecimal.ZERO;

    @Column(name = "free_attempts", nullable = false)
    private Integer freeAttempts = 2;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Lob
    @Column(length = 100000)
    private byte[] Image;

    @OneToOne(mappedBy = "mockTestInterview", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Project project;

    @OneToOne(mappedBy = "mockTestInterview", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Assignment assignment;

    @OneToOne(mappedBy = "mockTestInterview")
    private TrainingRoom trainingRoom;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)  // Ensure nullable is set to false if teacher_id is mandatory
    private User teacher;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public MockTestInterview() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Course getRelatedCourseId() {
        return relatedCourseId;
    }

    public void setRelatedCourseId(Course relatedCourseId) {
        this.relatedCourseId = relatedCourseId;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getFreeAttempts() {
        return freeAttempts;
    }

    public void setFreeAttempts(Integer freeAttempts) {
        this.freeAttempts = freeAttempts;
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

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public TrainingRoom getTrainingRoom() {
        return trainingRoom;
    }

    public void setTrainingRoom(TrainingRoom trainingRoom) {
        this.trainingRoom = trainingRoom;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public MockTestInterview(Long id, String title, String description, Course relatedCourseId, TestType testType, BigDecimal fee, Integer freeAttempts, LocalDateTime createdAt, LocalDateTime updatedAt, byte[] image, Project project, Assignment assignment, TrainingRoom trainingRoom, User teacher) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.relatedCourseId = relatedCourseId;
        this.testType = testType;
        this.fee = fee;
        this.freeAttempts = freeAttempts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        Image = image;
        this.project = project;
        this.assignment = assignment;
        this.trainingRoom = trainingRoom;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "MockTestInterview{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", relatedCourseId=" + relatedCourseId +
                ", testType=" + testType +
                ", fee=" + fee +
                ", freeAttempts=" + freeAttempts +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", Image=" + Arrays.toString(Image) +
                ", project=" + project +
                ", assignment=" + assignment +
                ", trainingRoom=" + trainingRoom +
                ", teacher=" + teacher +
                '}';
    }
}
