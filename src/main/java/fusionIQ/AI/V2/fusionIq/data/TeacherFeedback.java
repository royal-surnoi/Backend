package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TeacherFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lessonModule_id")
    private LessonModule lessonModule;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "feedback", nullable = false, length = 1000)
    private String feedback;

    @Column(name = "grade", nullable = false)
    private String grade;

    public TeacherFeedback() {
        this.createdAt=LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public LessonModule getLessonModule() {
        return lessonModule;
    }

    public void setLessonModule(LessonModule lessonModule) {
        this.lessonModule = lessonModule;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "TeacherFeedback{" +
                "id=" + id +
                ", teacher=" + teacher +
                ", student=" + student +
                ", course=" + course +
                ", quiz=" + quiz +
                ", assignment=" + assignment +
                ", project=" + project +
                ", lessonModule=" + lessonModule +
                ", lesson=" + lesson +
                ", createdAt=" + createdAt +
                ", feedback='" + feedback + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }

    public TeacherFeedback(Long id, User teacher, User student, Course course, Quiz quiz, Assignment assignment, Project project, LessonModule lessonModule, Lesson lesson, LocalDateTime createdAt, String feedback, String grade) {
        this.id = id;
        this.teacher = teacher;
        this.student = student;
        this.course = course;
        this.quiz = quiz;
        this.assignment = assignment;
        this.project = project;
        this.lessonModule = lessonModule;
        this.lesson = lesson;
        this.createdAt = createdAt;
        this.feedback = feedback;
        this.grade = grade;
    }
}