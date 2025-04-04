package fusionIQ.AI.V2.fusionIq.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "action_user_id")
    private User actionUser;

    private String content;
    private String url;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    @Column(name = "is_read", nullable = false)
    private boolean isRead;
    private String contentType;
    private String contentUrl;
    private String contentText;


    @Transient
    private String actionUserImageBase64;

    public Notification() {
        this.timestamp = LocalDateTime.now();
    }


    public Notification(long id, User user, User actionUser, String content, String url, LocalDateTime timestamp, boolean isRead, String contentType, String contentUrl, String contentText, String actionUserImageBase64) {
        this.id = id;
        this.user = user;
        this.actionUser = actionUser;
        this.content = content;
        this.url = url;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.contentType = contentType;
        this.contentUrl = contentUrl;
        this.contentText = contentText;
        this.actionUserImageBase64 = actionUserImageBase64;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getActionUser() {
        return actionUser;
    }

    public void setActionUser(User actionUser) {
        this.actionUser = actionUser;
    }

    public String getActionUserImageBase64() {
        return actionUserImageBase64;
    }

    public void setActionUserImageBase64(String actionUserImageBase64) {
        this.actionUserImageBase64 = actionUserImageBase64;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", user=" + user +
                ", actionUser=" + actionUser +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                ", contentType='" + contentType + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", contentText='" + contentText + '\'' +
                ", actionUserImageBase64='" + actionUserImageBase64 + '\'' +
                '}';
    }
}

