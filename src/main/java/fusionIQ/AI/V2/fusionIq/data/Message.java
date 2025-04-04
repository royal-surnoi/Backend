package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_messages_id")
    private GroupMessages groupMessages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Community_messages_id")
    private CommunityMessages communityMessages ;

    private String messageContent;
    private boolean sent;
    @Column(name = "is_read")
    private boolean isRead = false;
    private String fileUrl; // Add this field to store the file URL

    private boolean senderDeleted = false;
    private boolean receiverDeleted = false;

    @Column(name = "sender_reaction", nullable = true)
    private String sender_reaction;

    @Column(name = "reciver_reaction", nullable = true)
    private String reciver_reaction;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Reactions> reactions;



    @Transient
    private long unreadMessageCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;


    public long getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(long unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public Message() {
        this.createdAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public GroupMessages getGroupMessages() {
        return groupMessages;
    }

    public void setGroupMessages(GroupMessages groupMessages) {
        this.groupMessages = groupMessages;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public boolean isSenderDeleted() {
        return senderDeleted;
    }

    public void setSenderDeleted(boolean senderDeleted) {
        this.senderDeleted = senderDeleted;
    }

    public boolean isReceiverDeleted() {
        return receiverDeleted;
    }

    public void setReceiverDeleted(boolean receiverDeleted) {
        this.receiverDeleted = receiverDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSender_reaction() {
        return sender_reaction;
    }

    public void setSender_reaction(String sender_reaction) {
        this.sender_reaction = sender_reaction;
    }

    public String getReciver_reaction() {
        return reciver_reaction;
    }

    public void setReciver_reaction(String reciver_reaction) {
        this.reciver_reaction = reciver_reaction;
    }

    public List<Reactions> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reactions> reactions) {
        this.reactions = reactions;
    }


    public CommunityMessages getCommunityMessages() {
        return communityMessages;
    }

    public void setCommunityMessages(CommunityMessages communityMessages) {
        this.communityMessages = communityMessages;
    }

    public Message(long id, User sender, User receiver, GroupMessages groupMessages, CommunityMessages communityMessages, String messageContent, boolean sent, boolean isRead, String fileUrl, boolean senderDeleted, boolean receiverDeleted, String sender_reaction, String reciver_reaction, List<Reactions> reactions, long unreadMessageCount, LocalDateTime createdAt) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.groupMessages = groupMessages;
        this.communityMessages = communityMessages;
        this.messageContent = messageContent;
        this.sent = sent;
        this.isRead = isRead;
        this.fileUrl = fileUrl;
        this.senderDeleted = senderDeleted;
        this.receiverDeleted = receiverDeleted;
        this.sender_reaction = sender_reaction;
        this.reciver_reaction = reciver_reaction;
        this.reactions = reactions;
        this.unreadMessageCount = unreadMessageCount;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", groupMessages=" + groupMessages +
                ", communityMessages=" + communityMessages +
                ", messageContent='" + messageContent + '\'' +
                ", sent=" + sent +
                ", isRead=" + isRead +
                ", fileUrl='" + fileUrl + '\'' +
                ", senderDeleted=" + senderDeleted +
                ", receiverDeleted=" + receiverDeleted +
                ", sender_reaction='" + sender_reaction + '\'' +
                ", reciver_reaction='" + reciver_reaction + '\'' +
                ", reactions=" + reactions +
                ", unreadMessageCount=" + unreadMessageCount +
                ", createdAt=" + createdAt +
                '}';
    }
}