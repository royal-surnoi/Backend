package fusionIQ.AI.V2.fusionIq.data;

public class MentorResponse {
    private String token;
    private long mentorId;
    private String username;

    public MentorResponse(String token, long mentorId, String username) {
        this.token = token;
        this.mentorId = mentorId;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getMentorId() {
        return mentorId;
    }

    public void setMentorId(long mentorId) {
        this.mentorId = mentorId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
