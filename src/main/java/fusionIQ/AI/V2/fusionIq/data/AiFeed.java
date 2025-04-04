package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AiFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String feedType;

    private boolean feedInteraction;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticlePost articlePost;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private ImagePost imagePost;

    @ManyToOne
    @JoinColumn(name = "shortVideo_id")
    private ShortVideo shortVideo;

    @ManyToOne
    @JoinColumn(name = "longVideo_id")
    private LongVideo longVideo;

    public AiFeed() {
        this.createdAt=LocalDateTime.now();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public boolean isFeedInteraction() {
        return feedInteraction;
    }

    public void setFeedInteraction(boolean feedInteraction) {
        this.feedInteraction = feedInteraction;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArticlePost getArticlePost() {
        return articlePost;
    }

    public void setArticlePost(ArticlePost articlePost) {
        this.articlePost = articlePost;
    }

    public ImagePost getImagePost() {
        return imagePost;
    }

    public void setImagePost(ImagePost imagePost) {
        this.imagePost = imagePost;
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

    public AiFeed(Long id, String feedType, boolean feedInteraction, LocalDateTime createdAt, User user, ArticlePost articlePost, ImagePost imagePost, ShortVideo shortVideo, LongVideo longVideo) {
        this.id = id;
        this.feedType = feedType;
        this.feedInteraction = feedInteraction;
        this.createdAt = createdAt;
        this.user = user;
        this.articlePost = articlePost;
        this.imagePost = imagePost;
        this.shortVideo = shortVideo;
        this.longVideo = longVideo;
    }

    @Override
    public String toString() {
        return "AiFeed{" +
                "id=" + id +
                ", feedType='" + feedType + '\'' +
                ", feedInteraction=" + feedInteraction +
                ", createdAt=" + createdAt +
                ", user=" + user +
                ", articlePost=" + articlePost +
                ", imagePost=" + imagePost +
                ", shortVideo=" + shortVideo +
                ", longVideo=" + longVideo +
                '}';
    }
}
