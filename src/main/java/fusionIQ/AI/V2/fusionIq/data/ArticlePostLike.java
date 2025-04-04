package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ArticlePostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_post_id")
    private ArticlePost articlePost;
    private LocalDateTime articleLikedAt;
    public ArticlePostLike() {}

    public ArticlePostLike(User user, ArticlePost articlePost) {
        this.user = user;
        this.articlePost = articlePost;
        this.articleLikedAt = LocalDateTime.now();
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

    public ArticlePost getArticlePost() {
        return articlePost;
    }

    public void setArticlePost(ArticlePost articlePost) {
        this.articlePost = articlePost;
    }

    public LocalDateTime getArticleLikedAt() {
        return articleLikedAt;
    }

    public void setArticleLikedAt(LocalDateTime articleLikedAt) {
        this.articleLikedAt = articleLikedAt;
    }

    @Override
    public String toString() {
        return "ArticlePostLike{" +
                "id=" + id +
                ", user=" + user +
                ", articlePost=" + articlePost +
                ", articleLikedAt=" + articleLikedAt +
                '}';
    }
}
