package fusionIQ.AI.V2.fusionIq.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
public class   Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String assignmentTitle;
    private String assignmentTopicName;
    private String assignmentDescription;
    private Long maxScore;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
    private LocalDateTime reviewMeetDate;

    private String assignmentAnswer;
    @Lob
    @Column(length = 100000)
    private byte[] assignmentDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id") //
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "mock_id")
    private MockTestInterview mockTestInterview;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private LessonModule lessonModule;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Submission> submissions;

    private String studentIds;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;


    public Assignment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.reviewMeetDate = LocalDateTime.now();
        this.startDate = LocalDateTime.now();
        this.endDate = LocalDateTime.now();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getAssignmentTopicName() {
        return assignmentTopicName;
    }

    public void setAssignmentTopicName(String assignmentTopicName) {
        this.assignmentTopicName = assignmentTopicName;
    }

    public String getAssignmentDescription() {
        return assignmentDescription;
    }

    public void setAssignmentDescription(String assignmentDescription) {
        this.assignmentDescription = assignmentDescription;
    }

    public Long getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Long maxScore) {
        this.maxScore = maxScore;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getReviewMeetDate() {
        return reviewMeetDate;
    }

    public void setReviewMeetDate(LocalDateTime reviewMeetDate) {
        this.reviewMeetDate = reviewMeetDate;
    }

    public String getAssignmentAnswer() {
        return assignmentAnswer;
    }

    public void setAssignmentAnswer(String assignmentAnswer) {
        this.assignmentAnswer = assignmentAnswer;
    }

    public byte[] getAssignmentDocument() {
        return assignmentDocument;
    }

    public void setAssignmentDocument(byte[] assignmentDocument) {
        this.assignmentDocument = assignmentDocument;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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

    public MockTestInterview getMockTestInterview() {
        return mockTestInterview;
    }

    public void setMockTestInterview(MockTestInterview mockTestInterview) {
        this.mockTestInterview = mockTestInterview;
    }

    public LessonModule getLessonModule() {
        return lessonModule;
    }

    public void setLessonModule(LessonModule lessonModule) {
        this.lessonModule = lessonModule;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
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

    public Assignment(long id, String assignmentTitle, String assignmentTopicName, String assignmentDescription, Long maxScore, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime reviewMeetDate, String assignmentAnswer, byte[] assignmentDocument, Lesson lesson, Course course, User user, User teacher, User student, MockTestInterview mockTestInterview, LessonModule lessonModule, List<Submission> submissions, String studentIds, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.assignmentTitle = assignmentTitle;
        this.assignmentTopicName = assignmentTopicName;
        this.assignmentDescription = assignmentDescription;
        this.maxScore = maxScore;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reviewMeetDate = reviewMeetDate;
        this.assignmentAnswer = assignmentAnswer;
        this.assignmentDocument = assignmentDocument;
        this.lesson = lesson;
        this.course = course;
        this.user = user;
        this.teacher = teacher;
        this.student = student;
        this.mockTestInterview = mockTestInterview;
        this.lessonModule = lessonModule;
        this.submissions = submissions;
        this.studentIds = studentIds;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", assignmentTitle='" + assignmentTitle + '\'' +
                ", assignmentTopicName='" + assignmentTopicName + '\'' +
                ", assignmentDescription='" + assignmentDescription + '\'' +
                ", maxScore=" + maxScore +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reviewMeetDate=" + reviewMeetDate +
                ", assignmentAnswer='" + assignmentAnswer + '\'' +
                ", assignmentDocument=" + Arrays.toString(assignmentDocument) +
                ", lesson=" + lesson +
                ", course=" + course +
                ", user=" + user +
                ", teacher=" + teacher +
                ", student=" + student +
                ", mockTestInterview=" + mockTestInterview +
                ", lessonModule=" + lessonModule +
                ", submissions=" + submissions +
                ", studentIds='" + studentIds + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}