package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
public class TeacherDiscussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String discussionContent;

    private String replyContent;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] attachment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public TeacherDiscussion() {
        this.timestamp = LocalDateTime.now();
    }

    public TeacherDiscussion(long id, User user, String discussionContent, String replyContent, byte[] attachment, LocalDateTime timestamp) {
        this.id = id;
        this.user = user;
        this.discussionContent = discussionContent;
        this.replyContent = replyContent;
        this.attachment = attachment;
        this.timestamp = timestamp;
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

    public String getDiscussionContent() {
        return discussionContent;
    }

    public void setDiscussionContent(String discussionContent) {
        this.discussionContent = discussionContent;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "TeacherDiscussion{" +
                "id=" + id +
                ", user=" + user +
                ", discussionContent='" + discussionContent + '\'' +
                ", replyContent='" + replyContent + '\'' +
                ", attachment=" + Arrays.toString(attachment) +
                ", timestamp=" + timestamp +
                '}';
    }
}

