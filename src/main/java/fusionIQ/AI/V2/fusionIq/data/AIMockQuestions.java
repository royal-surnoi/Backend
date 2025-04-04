package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

@Entity
public class AIMockQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    @Column(name="question",length=20000)
    private String question;
    private String Q_s3Key;
    private String Q_s3Url;
    private String A_s3Key;
    private String A_s3Url;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "AIMockId")
    private AIMock aiMock;

    public AIMockQuestions() {
    }

    public AIMockQuestions(AIMock aiMock, User user, String answer, String a_s3Url, String a_s3Key, String q_s3Url, String q_s3Key, String question, long id) {
        this.aiMock = aiMock;
        this.user = user;
        this.answer = answer;
        A_s3Url = a_s3Url;
        A_s3Key = a_s3Key;
        Q_s3Url = q_s3Url;
        Q_s3Key = q_s3Key;
        this.question = question;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQ_s3Key() {
        return Q_s3Key;
    }

    public void setQ_s3Key(String q_s3Key) {
        Q_s3Key = q_s3Key;
    }

    public String getQ_s3Url() {
        return Q_s3Url;
    }

    public void setQ_s3Url(String q_s3Url) {
        Q_s3Url = q_s3Url;
    }

    public String getA_s3Key() {
        return A_s3Key;
    }

    public void setA_s3Key(String a_s3Key) {
        A_s3Key = a_s3Key;
    }

    public String getA_s3Url() {
        return A_s3Url;
    }

    public void setA_s3Url(String a_s3Url) {
        A_s3Url = a_s3Url;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AIMock getAiMock() {
        return aiMock;
    }

    public void setAiMock(AIMock aiMock) {
        this.aiMock = aiMock;
    }

    @Override
    public String toString() {
        return "AIMockQuestions{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", Q_s3Key='" + Q_s3Key + '\'' +
                ", Q_s3Url='" + Q_s3Url + '\'' +
                ", A_s3Key='" + A_s3Key + '\'' +
                ", A_s3Url='" + A_s3Url + '\'' +
                ", answer='" + answer + '\'' +
                ", user=" + user +
                ", aiMock=" + aiMock +
                '}';
    }
}
