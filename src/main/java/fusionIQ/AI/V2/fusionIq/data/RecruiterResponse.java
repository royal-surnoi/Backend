package fusionIQ.AI.V2.fusionIq.data;

public class RecruiterResponse {

    private String token;
    private long id;
    private String recruiterName;
    private String recruiterEmail;

    public RecruiterResponse(String token, long id, String recruiterName, String recruiterEmail) {
        this.token = token;
        this.id = id;
        this.recruiterName = recruiterName;
        this.recruiterEmail = recruiterEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public String getRecruiterEmail() {
        return recruiterEmail;
    }

    public void setRecruiterEmail(String recruiterEmail) {
        this.recruiterEmail = recruiterEmail;
    }
}
