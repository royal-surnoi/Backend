package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime commentDate;
    @ManyToOne
    @JoinColumn(name = "image_post_id")
    private ImagePost imagePost;
    @ManyToOne
    @JoinColumn(name = "article_post_id")
    private ArticlePost articlePost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    @OneToMany(mappedBy = "baseComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NestedComment> nestedComments = new ArrayList<>();
    @ElementCollection
    private List<Long> likedUserIds = new ArrayList<>();

    private int likeCount;

    public Comment() {}
    public Comment(long id, String text, User user, LocalDateTime commentDate, ImagePost imagePost, ArticlePost articlePost) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.commentDate = commentDate;
        this.imagePost = imagePost;
        this.articlePost = articlePost;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public LocalDateTime getCommentDate() {
        return commentDate;
    }
    public void setCommentDate(LocalDateTime commentDate) {
        this.commentDate = commentDate;
    }
    public ImagePost getImagePost() {
        return imagePost;
    }
    public void setImagePost(ImagePost imagePost) {
        this.imagePost = imagePost;
    }
    public ArticlePost getArticlePost() {
        return articlePost;
    }
    public void setArticlePost(ArticlePost articlePost) {
        this.articlePost = articlePost;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public List<Long> getLikedUserIds() {
        return likedUserIds;
    }

    public void setLikedUserIds(List<Long> likedUserIds) {
        this.likedUserIds = likedUserIds;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public List<NestedComment> getNestedComments() {
        return nestedComments;
    }

    public void setNestedComments(List<NestedComment> nestedComments) {
        this.nestedComments = nestedComments;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", user=" + user +
                ", commentDate=" + commentDate +
                ", imagePost=" + imagePost +
                ", articlePost=" + articlePost +
                ", parentComment=" + parentComment +
                '}';
    }
}
 