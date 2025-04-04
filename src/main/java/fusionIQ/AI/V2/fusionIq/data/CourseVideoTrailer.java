package fusionIQ.AI.V2.fusionIq.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CourseVideoTrailer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String videoTrailerTitle;
    private String s3Key;
    private String s3Url;
    private String videoTrailerDescription;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoTrailerTitle() {
        return videoTrailerTitle;
    }

    public void setVideoTrailerTitle(String videoTrailerTitle) {
        this.videoTrailerTitle = videoTrailerTitle;
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

    public String getVideoTrailerDescription() {
        return videoTrailerDescription;
    }

    public void setVideoTrailerDescription(String videoTrailerDescription) {
        this.videoTrailerDescription = videoTrailerDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseVideoTrailer(Long id, String videoTrailerTitle, String s3Key, String s3Url, String videoTrailerDescription, LocalDateTime createdAt, Course course) {
        this.id = id;
        this.videoTrailerTitle = videoTrailerTitle;
        this.s3Key = s3Key;
        this.s3Url = s3Url;
        this.videoTrailerDescription = videoTrailerDescription;
        this.createdAt = createdAt;
        this.course = course;
    }

    @Override
    public String toString() {
        return "CourseVideoTrailer{" +
                "id=" + id +
                ", videoTrailerTitle='" + videoTrailerTitle + '\'' +
                ", s3Key='" + s3Key + '\'' +
                ", s3Url='" + s3Url + '\'' +
                ", videoTrailerDescription='" + videoTrailerDescription + '\'' +
                ", createdAt=" + createdAt +
                ", course=" + course +
                '}';
    }

    public CourseVideoTrailer() {
        this.createdAt = LocalDateTime.now();
    }
}

