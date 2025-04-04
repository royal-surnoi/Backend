package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class ShortVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shortVideoTitle;
    private String s3Key;
    private String s3Url;
    @Column(length = 1000)
    private String shortVideoDescription;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;

    private String language;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "shortVideo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<VideoComment> videoComments;

    @ElementCollection
    @CollectionTable(name = "short_video_like_timestamps",
            joinColumns = @JoinColumn(name = "short_video_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "liked_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Map<Long, LocalDateTime> likeTimestamps = new HashMap<>();
    @ElementCollection
    private Set<User> likedByUsers = new HashSet<>();

    private int shortVideoLikes;
    private int shortVideoShares;
    private int shortVideoViews;
    private String tag;
    @Column(name = "category")
    private String category;

    private String shortVideoDuration;

    public ShortVideo() {
        this.updatedDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public ShortVideo(Long id, String shortVideoTitle, String category, String s3Key, String s3Url, String shortVideoDescription, LocalDateTime createdAt, LocalDateTime updatedDate, String language, User user, List<VideoComment> videoComments, Set<User> likedByUsers, int shortVideoLikes, int shortVideoShares, int shortVideoViews, String tag,String shortVideoDuration) {
        this.id = id;
        this.shortVideoTitle = shortVideoTitle;
        this.s3Key = s3Key;
        this.s3Url = s3Url;
        this.shortVideoDescription = shortVideoDescription;
        this.createdAt = createdAt;
        this.updatedDate = updatedDate;
        this.language = language;
        this.user = user;
        this.videoComments = videoComments;
        this.likedByUsers = likedByUsers;
        this.shortVideoLikes = shortVideoLikes;
        this.shortVideoShares = shortVideoShares;
        this.shortVideoViews = shortVideoViews;
        this.tag = tag;
        this.category = category;
        this.shortVideoDuration = shortVideoDuration;
    }

    public ShortVideo(long l, String sampleTitle, String s, String url, String sampleDescription, LocalDateTime now, LocalDateTime now1, String english, User user, List<VideoComment> videoComments, Set<User> likedByUsers, int i, int i1, int i2, String sampleTag, String time) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getShortVideoTitle() {
        return shortVideoTitle;
    }

    public void setShortVideoTitle(String shortVideoTitle) {
        this.shortVideoTitle = shortVideoTitle;
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

    public String getShortVideoDescription() {
        return shortVideoDescription;
    }

    public void setShortVideoDescription(String shortVideoDescription) {
        this.shortVideoDescription = shortVideoDescription;
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

    public int getShortVideoLikes() {
        return shortVideoLikes;
    }

    public void setShortVideoLikes(int shortVideoLikes) {
        this.shortVideoLikes = shortVideoLikes;
    }

    public int getShortVideoShares() {
        return shortVideoShares;
    }

    public void setShortVideoShares(int shortVideoShares) {
        this.shortVideoShares = shortVideoShares;
    }

    public int getShortVideoViews() {
        return shortVideoViews;
    }

    public void setShortVideoViews(int shortVideoViews) {
        this.shortVideoViews = shortVideoViews;
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

    public String getShortVideoDuration() {
        return shortVideoDuration;
    }

    public void setShortVideoDuration(String shortVideoDuration) {
        this.shortVideoDuration = shortVideoDuration;
    }

    public Map<Long, LocalDateTime> getLikeTimestamps() {
        return likeTimestamps;
    }

    public void setLikeTimestamps(Map<Long, LocalDateTime> likeTimestamps) {
        this.likeTimestamps = likeTimestamps;
    }

    @Override
    public String toString() {
        return "ShortVideo{" +
                "id=" + id +
                ", shortVideoTitle='" + shortVideoTitle + '\'' +
                ", s3Key='" + s3Key + '\'' +
                ", s3Url='" + s3Url + '\'' +
                ", shortVideoDescription='" + shortVideoDescription + '\'' +
                ", createdAt=" + createdAt +
                ", updatedDate=" + updatedDate +
                ", language='" + language + '\'' +
                ", user=" + user +
                ", videoComments=" + videoComments +
                ", likedByUsers=" + likedByUsers +
                ", shortVideoLikes=" + shortVideoLikes +
                ", shortVideoShares=" + shortVideoShares +
                ", shortVideoViews=" + shortVideoViews +
                ", tag='" + tag + '\'' +
                ", category='" + category + '\'' +
                ", shortVideoDuration='" + shortVideoDuration + '\'' +
                '}';
    }
}
