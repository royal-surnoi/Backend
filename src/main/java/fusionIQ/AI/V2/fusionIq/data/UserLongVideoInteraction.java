package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class UserLongVideoInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "long_video_id")
    private LongVideo longVideo;

    private long longVideoInteraction;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserLongVideoInteraction() {
    }

    public UserLongVideoInteraction(long id, User user, LongVideo longVideo, long longVideoInteraction) {
        this.id = id;
        this.user = user;
        this.longVideo = longVideo;
        this.longVideoInteraction = longVideoInteraction;
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

    public LongVideo getLongVideo() {
        return longVideo;
    }

    public void setLongVideo(LongVideo longVideo) {
        this.longVideo = longVideo;
    }

    public long getLongVideoInteraction() {
        return longVideoInteraction;
    }

    public void setLongVideoInteraction(long longVideoInteraction) {
        this.longVideoInteraction = longVideoInteraction;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserLongVideoInteraction{" +
                "id=" + id +
                ", user=" + user +
                ", longVideo=" + longVideo +
                ", longVideoInteraction=" + longVideoInteraction +
                ", createdAt=" + createdAt +
                '}';
    }
}