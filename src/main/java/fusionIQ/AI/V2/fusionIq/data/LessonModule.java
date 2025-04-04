package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class LessonModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String moduleName;

    @OneToMany(mappedBy = "lessonModule", cascade = CascadeType.ALL)
    private List<Lesson> lesson;

    @OneToMany(mappedBy = "lessonModule", cascade = CascadeType.ALL)
    private List<TeacherFeedback> teacherFeedbacks;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public LessonModule() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<Lesson> getLesson() {
        return lesson;
    }

    public void setLesson(List<Lesson> lesson) {
        this.lesson = lesson;
    }

    public List<TeacherFeedback> getTeacherFeedbacks() {
        return teacherFeedbacks;
    }

    public void setTeacherFeedbacks(List<TeacherFeedback> teacherFeedbacks) {
        this.teacherFeedbacks = teacherFeedbacks;
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

    public LessonModule(Long id, String moduleName, List<Lesson> lesson, List<TeacherFeedback> teacherFeedbacks, Course course, User user) {
        this.id = id;
        this.moduleName = moduleName;
        this.lesson = lesson;
        this.teacherFeedbacks = teacherFeedbacks;
        this.course = course;
        this.user = user;
    }

    @Override
    public String toString() {
        return "LessonModule{" +
                "id=" + id +
                ", moduleName='" + moduleName + '\'' +
                ", lesson=" + lesson +
                ", teacherFeedbacks=" + teacherFeedbacks +
                ", course=" + course +
                ", user=" + user +
                '}';
    }

}
