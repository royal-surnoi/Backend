package fusionIQ.AI.V2.fusionIq.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String videoTitle;
    private String s3Key;
    private String s3Url;
    private double progress;
    private String videoDescription;
    private String videoDuration;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private String language;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;


    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String transcript;

    @ManyToOne
    @JoinColumn(name = "lesson_module_id")
    private LessonModule lessonModule;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Notes> notes;


    public Video() {
        this.createdAt = LocalDateTime.now();
    }

    public Video(Long id) {
        this.id = id;
    }

    public Video(long id, String videoTitle, String s3Key, String s3Url, double progress, String videoDescription, String videoDuration, LocalDateTime createdAt, String language, Course course, Lesson lesson, String transcript, LessonModule lessonModule, List<Notes> notes) {
        this.id = id;
        this.videoTitle = videoTitle;
        this.s3Key = s3Key;
        this.s3Url = s3Url;
        this.progress = progress;
        this.videoDescription = videoDescription;
        this.videoDuration = videoDuration;
        this.createdAt = createdAt;
        this.language = language;
        this.course = course;
        this.lesson = lesson;
        this.transcript = transcript;
        this.lessonModule = lessonModule;
        this.notes = notes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public LessonModule getLessonModule() {
        return lessonModule;
    }

    public void setLessonModule(LessonModule lessonModule) {
        this.lessonModule = lessonModule;
    }

    public List<Notes> getNotes() {
        return notes;
    }

    public void setNotes(List<Notes> notes) {
        this.notes = notes;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", videoTitle='" + videoTitle + '\'' +
                ", s3Key='" + s3Key + '\'' +
                ", s3Url='" + s3Url + '\'' +
                ", progress=" + progress +
                ", videoDescription='" + videoDescription + '\'' +
                ", videoDuration='" + videoDuration + '\'' +
                ", createdAt=" + createdAt +
                ", language='" + language + '\'' +
                ", course=" + course +
                ", lesson=" + lesson +
                ", transcript='" + transcript + '\'' +
                ", lessonModule=" + lessonModule +
                ", notes=" + notes +
                '}';
    }
}