
package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class UserArticleInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_post_id")
    private ArticlePost articlePost;

    private long articleInteraction;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserArticleInteraction(long id, User user, ArticlePost articlePost, long articleInteraction) {
        this.id = id;
        this.user = user;
        this.articlePost = articlePost;
        this.articleInteraction = articleInteraction;
    }

    public UserArticleInteraction() {
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

    public long getArticleInteraction() {
        return articleInteraction;
    }

    public void setArticleInteraction(long articleInteraction) {
        this.articleInteraction = articleInteraction;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserArticleInteraction{" +
                "id=" + id +
                ", user=" + user +
                ", articlePost=" + articlePost +
                ", articleInteraction=" + articleInteraction +
                ", createdAt=" + createdAt +
                '}';
    }
}