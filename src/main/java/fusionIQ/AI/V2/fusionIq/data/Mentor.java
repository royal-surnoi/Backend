package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mentorId;
    private String username;
    private String password;
    private String mentorOtp;
    private LocalDateTime mentorOtpGeneratedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Mentor() {
    }

    public Mentor(Long mentorId, String username, String password, String mentorOtp, LocalDateTime mentorOtpGeneratedTime, User user) {
        this.mentorId = mentorId;
        this.username = username;
        this.password = password;
        this.mentorOtp = mentorOtp;
        this.mentorOtpGeneratedTime = mentorOtpGeneratedTime;
        this.user = user;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMentorOtp() {
        return mentorOtp;
    }

    public void setMentorOtp(String mentorOtp) {
        this.mentorOtp = mentorOtp;
    }

    public LocalDateTime getMentorOtpGeneratedTime() {
        return mentorOtpGeneratedTime;
    }

    public void setMentorOtpGeneratedTime(LocalDateTime mentorOtpGeneratedTime) {
        this.mentorOtpGeneratedTime = mentorOtpGeneratedTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Mentor{" +
                "mentorId=" + mentorId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mentorOtp='" + mentorOtp + '\'' +
                ", mentorOtpGeneratedTime=" + mentorOtpGeneratedTime +
                ", user=" + user +
                '}';
    }
}
