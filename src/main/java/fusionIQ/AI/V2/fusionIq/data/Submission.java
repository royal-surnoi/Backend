package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submissionDate;

    private double submissionGrade;
    private String submissionFeedback;

    public Submission() {
        this.submissionDate=LocalDateTime.now();


    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public double getSubmissionGrade() {
        return submissionGrade;
    }

    public void setSubmissionGrade(double submissionGrade) {
        this.submissionGrade = submissionGrade;
    }

    public String getSubmissionFeedback() {
        return submissionFeedback;
    }

    public void setSubmissionFeedback(String submissionFeedback) {
        this.submissionFeedback = submissionFeedback;
    }

    public Submission(long id, Activity activity, User user, Project project, Course course, Assignment assignment, LocalDateTime submissionDate, double submissionGrade, String submissionFeedback) {
        this.id = id;
        this.activity = activity;
        this.user = user;
        this.project = project;
        this.course = course;
        this.assignment = assignment;
        this.submissionDate = submissionDate;
        this.submissionGrade = submissionGrade;
        this.submissionFeedback = submissionFeedback;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", activity=" + activity +
                ", user=" + user +
                ", project=" + project +
                ", course=" + course +
                ", assignment=" + assignment +
                ", submissionDate=" + submissionDate +
                ", submissionGrade=" + submissionGrade +
                ", submissionFeedback='" + submissionFeedback + '\'' +
                '}';
    }
}