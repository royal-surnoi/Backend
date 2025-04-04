package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Lesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;


    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL)
    private List<Quiz> quizzes;


    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL)
    private List<Video> video;

    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL)
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL)
    private List<SubmitAssignment> submitAssignments;


    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL)
    private List<TeacherFeedback> teacherFeedbacks;

    @ManyToOne
    @JoinColumn(name = "lesson_module_id")
    private LessonModule lessonModule;


    private String lessonTitle;
    private String lessonContent;

    @Column(columnDefinition = "LONGTEXT")
    private String lessonDescription;

    private Long lessonDuration;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public Lesson() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public List<Video> getVideo() {
        return video;
    }

    public void setVideo(List<Video> video) {
        this.video = video;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<SubmitAssignment> getSubmitAssignments() {
        return submitAssignments;
    }

    public void setSubmitAssignments(List<SubmitAssignment> submitAssignments) {
        this.submitAssignments = submitAssignments;
    }

    public List<TeacherFeedback> getTeacherFeedbacks() {
        return teacherFeedbacks;
    }

    public void setTeacherFeedbacks(List<TeacherFeedback> teacherFeedbacks) {
        this.teacherFeedbacks = teacherFeedbacks;
    }

    public LessonModule getLessonModule() {
        return lessonModule;
    }

    public void setLessonModule(LessonModule lessonModule) {
        this.lessonModule = lessonModule;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public String getLessonContent() {
        return lessonContent;
    }

    public void setLessonContent(String lessonContent) {
        this.lessonContent = lessonContent;
    }

    public String getLessonDescription() {
        return lessonDescription;
    }

    public void setLessonDescription(String lessonDescription) {
        this.lessonDescription = lessonDescription;
    }

    public Long getLessonDuration() {
        return lessonDuration;
    }

    public void setLessonDuration(Long lessonDuration) {
        this.lessonDuration = lessonDuration;
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

    public Lesson(long id, User user, Course course, List<Quiz> quizzes, List<Video> video, List<Assignment> assignments, List<SubmitAssignment> submitAssignments, List<TeacherFeedback> teacherFeedbacks, LessonModule lessonModule, String lessonTitle, String lessonContent, String lessonDescription, Long lessonDuration, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.quizzes = quizzes;
        this.video = video;
        this.assignments = assignments;
        this.submitAssignments = submitAssignments;
        this.teacherFeedbacks = teacherFeedbacks;
        this.lessonModule = lessonModule;
        this.lessonTitle = lessonTitle;
        this.lessonContent = lessonContent;
        this.lessonDescription = lessonDescription;
        this.lessonDuration = lessonDuration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", user=" + user +
                ", course=" + course +
                ", quizzes=" + quizzes +
                ", video=" + video +
                ", assignments=" + assignments +
                ", submitAssignments=" + submitAssignments +
                ", teacherFeedbacks=" + teacherFeedbacks +
                ", lessonModule=" + lessonModule +
                ", lessonTitle='" + lessonTitle + '\'' +
                ", lessonContent='" + lessonContent + '\'' +
                ", lessonDescription='" + lessonDescription + '\'' +
                ", lessonDuration=" + lessonDuration +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}


