package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
public class SubmitAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] submitAssignment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submittedAt;

    private String userAssignmentAnswer;

    private boolean isSubmitted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn
    private Lesson lesson;

    @ManyToOne
    @JoinColumn
    private Assignment assignment;

    @ManyToOne
    @JoinColumn
    private LessonModule lessonModule;

    @ManyToOne
    @JoinColumn
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "mock_id")
    private MockTestInterview mockTestInterview;

    public SubmitAssignment() {
        this.submittedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getSubmitAssignment() {
        return submitAssignment;
    }

    public void setSubmitAssignment(byte[] submitAssignment) {
        this.submitAssignment = submitAssignment;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getUserAssignmentAnswer() {
        return userAssignmentAnswer;
    }

    public void setUserAssignmentAnswer(String userAssignmentAnswer) {
        this.userAssignmentAnswer = userAssignmentAnswer;
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

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public LessonModule getLessonModule() {
        return lessonModule;
    }

    public void setLessonModule(LessonModule lessonModule) {
        this.lessonModule = lessonModule;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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

    public SubmitAssignment(Long id, byte[] submitAssignment, LocalDateTime submittedAt, String userAssignmentAnswer, boolean isSubmitted, User user, Lesson lesson, Assignment assignment, LessonModule lessonModule, Course course, User student, MockTestInterview mockTestInterview) {
        this.id = id;
        this.submitAssignment = submitAssignment;
        this.submittedAt = submittedAt;
        this.userAssignmentAnswer = userAssignmentAnswer;
        this.isSubmitted = isSubmitted;
        this.user = user;
        this.lesson = lesson;
        this.assignment = assignment;
        this.lessonModule = lessonModule;
        this.course = course;
        this.student = student;
        this.mockTestInterview = mockTestInterview;
    }

    @Override
    public String toString() {
        return "SubmitAssignment{" +
                "id=" + id +
                ", submitAssignment=" + Arrays.toString(submitAssignment) +
                ", submittedAt=" + submittedAt +
                ", userAssignmentAnswer='" + userAssignmentAnswer + '\'' +
                ", isSubmitted=" + isSubmitted +
                ", user=" + user +
                ", lesson=" + lesson +
                ", assignment=" + assignment +
                ", lessonModule=" + lessonModule +
                ", course=" + course +
                ", student=" + student +
                ", mockTestInterview=" + mockTestInterview +
                '}';
    }
}
