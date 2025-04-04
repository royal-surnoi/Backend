package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ArticlePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(length = 1500)
    private String article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime postDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;
    private int articleLikeCount;
    private int articleDislikes;
    private int articleShareCount;
    @Column(nullable = true)
    private String tag;

    @Column(nullable = true)
    private String category;

    @OneToMany(mappedBy = "articlePost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "articlePost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserArticleInteraction> userArticleInteraction;

    public ArticlePost(long l, String sampleArticle, User user, LocalDateTime now, LocalDateTime now1, int i, int i1, int i2, String technology, List<Comment> comments, List<UserArticleInteraction> interactions) {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArticlePost() {
        this.postDate = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public int getArticleLikeCount() {
        return articleLikeCount;
    }

    public void setArticleLikeCount(int articleLikeCount) {
        this.articleLikeCount = articleLikeCount;
    }

    public int getArticleDislikes() {
        return articleDislikes;
    }

    public void setArticleDislikes(int articleDislikes) {
        this.articleDislikes = articleDislikes;
    }

    public int getArticleShareCount() {
        return articleShareCount;
    }

    public void setArticleShareCount(int articleShareCount) {
        this.articleShareCount = articleShareCount;
    }

    public List<UserArticleInteraction> getUserArticleInteraction() {
        return userArticleInteraction;
    }

    public void setUserArticleInteraction(List<UserArticleInteraction> userArticleInteraction) {
        this.userArticleInteraction = userArticleInteraction;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArticlePost(long id, String article, User user, LocalDateTime postDate, LocalDateTime updatedDate, String category, int articleLikeCount, int articleDislikes, int articleShareCount, String tag, List<Comment> comments, List<UserArticleInteraction> userArticleInteraction) {
        this.id = id;
        this.article = article;
        this.user = user;
        this.postDate = postDate;
        this.updatedDate = updatedDate;
        this.articleLikeCount = articleLikeCount;
        this.articleDislikes = articleDislikes;
        this.articleShareCount = articleShareCount;
        this.tag = tag;
        this.category = category;
        this.comments = comments;
        this.userArticleInteraction = userArticleInteraction;
    }

    @Override
    public String toString() {
        return "ArticlePost{" +
                "id=" + id +
                ", article='" + article + '\'' +
                ", user=" + user +
                ", postDate=" + postDate +
                ", updatedDate=" + updatedDate +
                ", articleLikeCount=" + articleLikeCount +
                ", articleDislikes=" + articleDislikes +
                ", articleShareCount=" + articleShareCount +
                ", tag='" + tag + '\'' +
                ", userArticleInteraction=" + userArticleInteraction +
                ", category='" + category + '\'' +
                '}';
    }

}


