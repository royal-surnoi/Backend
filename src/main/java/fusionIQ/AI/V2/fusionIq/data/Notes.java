package fusionIQ.AI.V2.fusionIq.data;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private Course course;

    @ManyToOne
    @JoinColumn
    private Video video;


    @ManyToOne
    @JoinColumn
    private Lesson lesson;
    private String myNotes;
    private LocalDateTime createdAt;

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public String getMyNotes() {
        return myNotes;
    }

    public void setMyNotes(String myNotes) {
        this.myNotes = myNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Notes(Long id, User user, Course course, Video video, Lesson lesson, String myNotes, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.video = video;
        this.lesson = lesson;
        this.myNotes = myNotes;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "id=" + id +
                ", user=" + user +
                ", course=" + course +
                ", video=" + video +
                ", lesson=" + lesson +
                ", myNotes='" + myNotes + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public Notes() {
        this.createdAt = LocalDateTime.now();
    }
}