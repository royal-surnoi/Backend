package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class VideoProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne
    @JoinColumn
    private Course course;

    @ManyToOne
    @JoinColumn
    private Lesson lesson;

    @ManyToOne
    @JoinColumn
    private LessonModule lessonModule;
    private double progress;
    private LocalDateTime watchedAt;

    public VideoProgress() {
        this.watchedAt = LocalDateTime.now();
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

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public LessonModule getLessonModule() {
        return lessonModule;
    }

    public void setLessonModule(LessonModule lessonModule) {
        this.lessonModule = lessonModule;
    }

    public LocalDateTime getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(LocalDateTime watchedAt) {
        this.watchedAt = watchedAt;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public VideoProgress(long id, User user, Video video, Course course, Lesson lesson, LessonModule lessonModule, double progress, LocalDateTime watchedAt) {
        this.id = id;
        this.user = user;
        this.video = video;
        this.course = course;
        this.lesson = lesson;
        this.lessonModule = lessonModule;
        this.progress = progress;
        this.watchedAt = watchedAt;
    }

    @Override
    public String toString() {
        return "VideoProgress{" +
                "id=" + id +
                ", user=" + user +
                ", video=" + video +
                ", course=" + course +
                ", lesson=" + lesson +
                ", lessonModule=" + lessonModule +
                ", watchedAt=" + watchedAt +
                '}';
    }
}