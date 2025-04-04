package fusionIQ.AI.V2.fusionIq.data;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CourseMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn
    private User from;

    @ManyToOne
    @JoinColumn
    private User to;

    private String subject;
    private LocalDateTime sentAt;
    private String courseMessage;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getCourseMessage() {
        return courseMessage;
    }

    public void setCourseMessage(String courseMessage) {
        this.courseMessage = courseMessage;
    }

    public CourseMessage(Long id, User from, User to, String subject, LocalDateTime sentAt, String courseMessage) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.sentAt = sentAt;
        this.courseMessage = courseMessage;
    }

    @Override
    public String toString() {
        return "CourseMessage{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", subject='" + subject + '\'' +
                ", sentAt=" + sentAt +
                ", courseMessage='" + courseMessage + '\'' +
                '}';
    }

    public CourseMessage() {
        this.sentAt = LocalDateTime.now();
    }
}
