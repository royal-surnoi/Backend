package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class VideoComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String videoCommentContent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "short_video_id")
    @JsonBackReference
    private ShortVideo shortVideo;

    @ManyToOne
    @JoinColumn(name = "long_video_id")
    @JsonBackReference
    private LongVideo longVideo;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonBackReference
    private VideoComment parentComment;


    private int likes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoComment> replies = new HashSet<>();

    public VideoComment(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public VideoComment(){

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoCommentContent() {
        return videoCommentContent;
    }

    public void setVideoCommentContent(String videoCommentContent) {
        this.videoCommentContent = videoCommentContent;
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

    public VideoComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(VideoComment parentComment) {
        this.parentComment = parentComment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public Set<VideoComment> getReplies() {
        return replies;
    }

    public void setReplies(Set<VideoComment> replies) {
        this.replies = replies;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "VideoComment{" +
                "id=" + id +
                ", videoCommentContent='" + videoCommentContent + '\'' +
                ", user=" + user +
                ", shortVideo=" + shortVideo +
                ", longVideo=" + longVideo +
                ", parentComment=" + parentComment +
                ", createdAt=" + createdAt +
                '}';
    }


}


