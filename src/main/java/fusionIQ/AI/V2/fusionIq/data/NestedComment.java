package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class NestedComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private VideoComment parentComment;

    @ManyToOne
    @JoinColumn(name = "base_comment_id")
    private Comment baseComment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private int likes;
    @ElementCollection
    private Set<Long> likedUserIds = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NestedComment(){

    }

    public NestedComment(Comment baseComment, LocalDateTime updatedAt) {
        this.baseComment = baseComment;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VideoComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(VideoComment parentComment) {
        this.parentComment = parentComment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Comment getBaseComment() {
        return baseComment;
    }

    public void setBaseComment(Comment baseComment) {
        this.baseComment = baseComment;
    }

    public Set<Long> getLikedUserIds() {
        return likedUserIds;
    }

    public void setLikedUserIds(Set<Long> likedUserIds) {
        this.likedUserIds = likedUserIds;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "NestedComment{" +
                "id=" + id +
                ", parentComment=" + parentComment +
                ", baseComment=" + baseComment +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", likes=" + likes +
                ", createdAt=" + createdAt +
                '}';
    }
}
