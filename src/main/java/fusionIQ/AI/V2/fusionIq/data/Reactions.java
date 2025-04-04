package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Reactions")
public class Reactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Reaction")
    private String reaction;

    @ManyToOne
    @JoinColumn(name = "MessageId", nullable = false)
    @JsonIgnore
    private Message message;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reactions(Long id, String reaction, Message message, User user) {
        this.id = id;
        this.reaction = reaction;
        this.message = message;
        this.user = user;
    }

    public Reactions() {
    }

    @Override
    public String toString() {
        return "Reactions{" +
                "id=" + id +
                ", reaction='" + reaction + '\'' +
                ", message=" + message +
                ", user=" + user +
                '}';
    }
}
