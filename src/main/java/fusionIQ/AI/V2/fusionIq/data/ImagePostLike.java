package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ImagePostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "image_post_id")
    private ImagePost imagePost;

    private LocalDateTime imageLikedAt;
    public ImagePostLike() {
    }

    public ImagePostLike(User user, ImagePost imagePost) {
        this.user = user;
        this.imagePost = imagePost;
        this.imageLikedAt=LocalDateTime.now();
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

    public ImagePost getImagePost() {
        return imagePost;
    }

    public void setImagePost(ImagePost imagePost) {
        this.imagePost = imagePost;
    }

    public LocalDateTime getImageLikedAt() {
        return imageLikedAt;
    }

    public void setImageLikedAt(LocalDateTime imageLikedAt) {
        this.imageLikedAt = imageLikedAt;
    }

    @Override
    public String toString() {
        return "ImagePostLike{" +
                "id=" + id +
                ", user=" + user +
                ", imagePost=" + imagePost +
                ", imageLikedAt=" + imageLikedAt +
                '}';
    }
}