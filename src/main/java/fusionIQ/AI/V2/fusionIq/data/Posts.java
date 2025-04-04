package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Column(name = "photo", columnDefinition="LONGBLOB")
    private byte[] photo;

    @Column(length = 15000)
    private String article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime postDate;



    public Posts() {}

    public Posts(long id, byte[] photo, String article, User user, LocalDateTime postDate, List<SavedItems> savedItems) {
        this.id = id;
        this.photo = photo;
        this.article = article;
        this.user = user;
        this.postDate = postDate;
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



    @Override
    public String toString() {
        return "Posts{" +
                "id=" + id +
                ", photo=" + Arrays.toString(photo) +
                ", article='" + article + '\'' +
                ", user=" + user +
                ", postDate=" + postDate +
                '}';
    }
}
