package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recruiter_feedback")
public class RecruiterFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    @Column(nullable = false)
    private FeedbackLevel feedbackLevel;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private int feedbackScore;

    @Column(name = "recommended_course")
    private String recommendedCourse;

    @Column(name = "recruiter_feedback", columnDefinition = "TEXT")
    private String recruiterFeedback;


    @Column(name = "course_duration")
    private String courseDuration;

    @Column(name = "skill_level")
    private String skillLevel;

    @Column(name = "course_language")
    private String courseLanguage;

    @Column(name = "rating")
    private Double rating;

    // Enum for feedback levels
    public enum FeedbackLevel {
        SHORTLISTED,
        ACCEPTED,
        REJECTED,
        LEVEL1,
        LEVEL2,
        LEVEL3
    }


    public RecruiterFeedback() {
    }


    public RecruiterFeedback(User user, Recruiter recruiter, Job job, FeedbackLevel feedbackLevel, int feedbackScore, String recommendedCourse, String recruiterFeedback, String courseDuration, String skillLevel, String courseLanguage, Double rating) {
        this.user = user;
        this.recruiter = recruiter;
        this.job = job;
        this.feedbackLevel = feedbackLevel;
        this.timestamp = LocalDateTime.now();
        this.feedbackScore = feedbackScore;
        this.recommendedCourse = recommendedCourse;
        this.recruiterFeedback = recruiterFeedback;
        this.courseDuration = courseDuration;
        this.skillLevel = skillLevel;
        this.courseLanguage = courseLanguage;
        this.rating = rating;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public FeedbackLevel getFeedbackLevel() {
        return feedbackLevel;
    }

    public void setFeedbackLevel(FeedbackLevel feedbackLevel) {
        this.feedbackLevel = feedbackLevel;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getFeedbackScore() {
        return feedbackScore;
    }

    public void setFeedbackScore(int feedbackScore) {
        this.feedbackScore = feedbackScore;
    }

    public String getRecommendedCourse() {
        return recommendedCourse;
    }

    public void setRecommendedCourse(String recommendedCourse) {
        this.recommendedCourse = recommendedCourse;
    }

    public String getRecruiterFeedback() {
        return recruiterFeedback;
    }

    public void setRecruiterFeedback(String recruiterFeedback) {
        this.recruiterFeedback = recruiterFeedback;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getCourseLanguage() {
        return courseLanguage;
    }

    public void setCourseLanguage(String courseLanguage) {
        this.courseLanguage = courseLanguage;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "RecruiterFeedback{" +
                "id=" + id +
                ", user=" + user +
                ", recruiter=" + recruiter +
                ", job=" + job +
                ", feedbackLevel=" + feedbackLevel +
                ", timestamp=" + timestamp +
                ", feedbackScore=" + feedbackScore +
                ", recommendedCourse='" + recommendedCourse + '\'' +
                ", recruiterFeedback='" + recruiterFeedback + '\'' +
                ", courseDuration='" + courseDuration + '\'' +
                ", skillLevel='" + skillLevel + '\'' +
                ", courseLanguage='" + courseLanguage + '\'' +
                ", rating=" + rating +
                '}';
    }
}
