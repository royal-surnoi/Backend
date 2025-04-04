package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class SavedItems implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "image_post_id", nullable = true)
    private ImagePost imagePost;

    @ManyToOne
    @JoinColumn(name = "article_post_id", nullable = true)
    private ArticlePost articlePost;

    @ManyToOne
    @JoinColumn(name = "short_video_id", nullable = true)
    private ShortVideo shortVideo;

    @ManyToOne
    @JoinColumn(name = "long_video_id", nullable = true)
    private LongVideo longVideo;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime savedAt;

    public SavedItems() {
        this.savedAt = LocalDateTime.now();
    }

    public SavedItems(User user,ImagePost imagePost) {
        this.user = user;
        this.imagePost = imagePost;
        this.articlePost = null;
        this.shortVideo = null;
        this.longVideo = null;
        this.savedAt = LocalDateTime.now();
    }

    public SavedItems(User user, ArticlePost articlePost) {
        this.user = user;
        this.articlePost = articlePost;
        this.imagePost = null;
        this.shortVideo = null;
        this.longVideo = null;
        this.savedAt = LocalDateTime.now();
    }

    public SavedItems(User user, ShortVideo shortVideo) {
        this.user = user;
        this.imagePost = null;
        this.articlePost = null;
        this.longVideo = null;
        this.shortVideo = shortVideo;
        this.savedAt = LocalDateTime.now();
    }

    public SavedItems(User user, LongVideo longVideo) {
        this.user = user;
        this.imagePost = null;
        this.articlePost = null;
        this.shortVideo = null;
        this.longVideo = longVideo;
        this.savedAt = LocalDateTime.now();
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

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }

    public ArticlePost getArticlePost() {
        return articlePost;
    }

    public void setArticlePost(ArticlePost articlePost) {
        this.articlePost = articlePost;
    }

    public LongVideo getLongVideo() {
        return longVideo;
    }

    public void setLongVideo(LongVideo longVideo) {
        this.longVideo = longVideo;
    }

    public ShortVideo getShortVideo() {
        return shortVideo;
    }

    public void setShortVideo(ShortVideo shortVideo) {
        this.shortVideo = shortVideo;
    }

    @Override
    public String toString() {
        return "SavedItems{" +
                "id=" + id +
                ", user=" + user +
                ", imagePost=" + imagePost +
                ", articlePost=" + articlePost +
                ", shortVideo=" + shortVideo +
                ", longVideo=" + longVideo +
                ", savedAt=" + savedAt +
                '}';
    }
}
