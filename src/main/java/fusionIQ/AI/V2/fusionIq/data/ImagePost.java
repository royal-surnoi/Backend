
package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class ImagePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "imagePost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserImageInteraction> UserImageInteraction;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime postDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;

    private int imageLikeCount;
    private int imageDislikes;
    private int imageShareCount;
    @Column(nullable = true)
    private String imageDescription;

    @Column(nullable = true)
    private String tag;

    @Column(nullable = true)
    private String category;

    @OneToMany(mappedBy = "imagePost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    public ImagePost(long id, byte[] photo, String category, User user, List<fusionIQ.AI.V2.fusionIq.data.UserImageInteraction> userImageInteraction, LocalDateTime postDate, LocalDateTime updatedDate, int imageLikeCount, int imageDislikes, int imageShareCount, String imageDescription, String tag, List<Comment> comments) {
        this.id = id;
        this.photo = photo;
        this.user = user;
        UserImageInteraction = userImageInteraction;
        this.postDate = postDate;
        this.updatedDate = updatedDate;
        this.imageLikeCount = imageLikeCount;
        this.imageDislikes = imageDislikes;
        this.imageShareCount = imageShareCount;
        this.imageDescription = imageDescription;
        this.tag = tag;
        this.category = category;
        this.comments = comments;
    }

    public ImagePost() {

    }

    public <T> ImagePost(long l, byte[] photo, User user, List<T> ts, LocalDateTime now, LocalDateTime now1, int i, int i1, int i2, String testDescription, String testTag, List<T> ts1) {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
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

    public int getImageLikeCount() {
        return imageLikeCount;
    }

    public void setImageLikeCount(int imageLikeCount) {
        this.imageLikeCount = imageLikeCount;
    }

    public int getImageDislikes() {
        return imageDislikes;
    }

    public void setImageDislikes(int imageDislikes) {
        this.imageDislikes = imageDislikes;
    }

    public int getImageShareCount() {
        return imageShareCount;
    }

    public void setImageShareCount(int imageShareCount) {
        this.imageShareCount = imageShareCount;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public List<fusionIQ.AI.V2.fusionIq.data.UserImageInteraction> getUserImageInteraction() { return UserImageInteraction;}

    public void setUserImageInteraction(List<fusionIQ.AI.V2.fusionIq.data.UserImageInteraction> userImageInteraction) { UserImageInteraction = userImageInteraction;}

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "ImagePost{" +
                "id=" + id +
                ", photo=" + Arrays.toString(photo) +
                ", user=" + user +
                ", UserImageInteraction=" + UserImageInteraction +
                ", postDate=" + postDate +
                ", updatedDate=" + updatedDate +
                ", imageLikeCount=" + imageLikeCount +
                ", imageDislikes=" + imageDislikes +
                ", imageShareCount=" + imageShareCount +
                ", imageDescription='" + imageDescription + '\'' +
                ", tag='" + tag + '\'' +
                ", category='" + category + '\'' +
                '}';
    }


}
