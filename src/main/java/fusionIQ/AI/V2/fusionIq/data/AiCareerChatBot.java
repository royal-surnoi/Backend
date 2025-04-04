package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AiCareerChatBot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userQuestion;
    private String botResponse;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserQuestion() {
        return userQuestion;
    }

    public void setUserQuestion(String userQuestion) {
        this.userQuestion = userQuestion;
    }

    public String getBotResponse() {
        return botResponse;
    }

    public void setBotResponse(String botResponse) {
        this.botResponse = botResponse;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AiCareerChatBot(Long id, String userQuestion, String botResponse, LocalDateTime createdAt, User user) {
        this.id = id;
        this.userQuestion = userQuestion;
        this.botResponse = botResponse;
        this.createdAt = createdAt;
        this.user = user;
    }

    public AiCareerChatBot() {
        this.createdAt=LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "CareerChatBot{" +
                "id=" + id +
                ", userQuestion='" + userQuestion + '\'' +
                ", botResponse='" + botResponse + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + user +
                '}';
    }
}
