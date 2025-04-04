package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class VideoLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "short_video_id")
    private ShortVideo shortVideo;

    @ManyToOne
    @JoinColumn(name = "long_video_id")
    private LongVideo longVideo;

    private LocalDateTime timestamp;

    public VideoLike() {
    }

    public VideoLike(User user, ShortVideo shortVideo) {
        this.user = user;
        this.shortVideo = shortVideo;
        this.timestamp = LocalDateTime.now();
    }

    public VideoLike(User user, LongVideo longVideo) {
        this.user = user;
        this.longVideo = longVideo;
        this.timestamp = LocalDateTime.now();
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

    public ShortVideo getShortVideo() {
        return shortVideo;
    }

    public void setShortVideo(ShortVideo shortVideo) {
        this.shortVideo = shortVideo;
    }

    public LongVideo getLongVideo() {
        return longVideo;
    }

    public void setLongVideo(LongVideo longVideo) {
        this.longVideo = longVideo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "VideoLike{" +
                "id=" + id +
                ", user=" + user +
                ", shortVideo=" + shortVideo +
                ", longVideo=" + longVideo +
                ", timestamp=" + timestamp +
                '}';
    }
}
