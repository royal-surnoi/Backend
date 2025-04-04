package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class LongVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String longVideoTitle;
    private String s3Key;
    private String s3Url;
    @Column(columnDefinition = "LONGTEXT")
    private String longVideoDescription;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;

    private String language;

    @Column(nullable = true)
    private String category;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "longVideo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<VideoComment> videoComments = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "long_video_like_timestamps",
            joinColumns = @JoinColumn(name = "long_video_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "liked_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Map<Long, LocalDateTime> likeTimestamps = new HashMap<>();
    @ElementCollection
    private Set<User> likedByUsers = new HashSet<>();

    private int longVideoLikes;
    private int longVideoShares;
    private int longVideoViews;
    private String tag;

    private String longVideoDuration;

    public LongVideo() {
        this.updatedDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public LongVideo(Long id, String longVideoTitle, String category, String s3Key, String s3Url, String longVideoDescription, LocalDateTime createdAt, LocalDateTime updatedDate, String language, User user, List<VideoComment> videoComments, Set<User> likedByUsers, int longVideoLikes, int longVideoShares, int longVideoViews, String tag, String longVideoDuration) {
        this.id = id;
        this.longVideoTitle = longVideoTitle;
        this.s3Key = s3Key;
        this.s3Url = s3Url;
        this.longVideoDescription = longVideoDescription;
        this.createdAt = createdAt;
        this.updatedDate = updatedDate;
        this.language = language;
        this.user = user;
        this.videoComments = videoComments;
        this.likedByUsers = likedByUsers;
        this.longVideoLikes = longVideoLikes;
        this.longVideoShares = longVideoShares;
        this.longVideoViews = longVideoViews;
        this.tag = tag;
        this.category = category;
        this.longVideoDuration = longVideoDuration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLongVideoTitle() {
        return longVideoTitle;
    }

    public void setLongVideoTitle(String longVideoTitle) {
        this.longVideoTitle = longVideoTitle;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLongVideoDescription() {
        return longVideoDescription;
    }

    public void setLongVideoDescription(String longVideoDescription) {
        this.longVideoDescription = longVideoDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<VideoComment> getVideoComments() {
        return videoComments;
    }

    public void setVideoComments(List<VideoComment> videoComments) {
        this.videoComments = videoComments;
    }

    public Set<User> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(Set<User> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }

    public int getLongVideoLikes() {
        return longVideoLikes;
    }

    public void setLongVideoLikes(int longVideoLikes) {
        this.longVideoLikes = longVideoLikes;
    }

    public int getLongVideoShares() {
        return longVideoShares;
    }

    public void setLongVideoShares(int longVideoShares) {
        this.longVideoShares = longVideoShares;
    }

    public int getLongVideoViews() {
        return longVideoViews;
    }

    public void setLongVideoViews(int longVideoViews) {
        this.longVideoViews = longVideoViews;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getLongVideoDuration() {
        return longVideoDuration;
    }

    public void setLongVideoDuration(String longVideoDuration) {
        this.longVideoDuration = longVideoDuration;
    }
    public Map<Long, LocalDateTime> getLikeTimestamps() {
        return likeTimestamps;
    }

    public void setLikeTimestamps(Map<Long, LocalDateTime> likeTimestamps) {
        this.likeTimestamps = likeTimestamps;
    }
    @Override
    public String toString() {
        return "LongVideo{" +
                "id=" + id +
                ", longVideoTitle='" + longVideoTitle + '\'' +
                ", s3Key='" + s3Key + '\'' +
                ", s3Url='" + s3Url + '\'' +
                ", longVideoDescription='" + longVideoDescription + '\'' +
                ", createdAt=" + createdAt +
                ", updatedDate=" + updatedDate +
                ", language='" + language + '\'' +
                ", user=" + user +
                ", videoComments=" + videoComments +
                ", likedByUsers=" + likedByUsers +
                ", longVideoLikes=" + longVideoLikes +
                ", longVideoShares=" + longVideoShares +
                ", longVideoViews=" + longVideoViews +
                ", tag='" + tag + '\'' +
                ", category='" + category + '\'' +
                ", longVideoDuration='" + longVideoDuration + '\'' +
                '}';
    }
}