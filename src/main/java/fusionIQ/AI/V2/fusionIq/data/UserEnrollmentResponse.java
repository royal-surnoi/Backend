package fusionIQ.AI.V2.fusionIq.data;

import java.util.List;

public class UserEnrollmentResponse {
    private User user;
    private List<String> courseTitles;

    public UserEnrollmentResponse(User user, List<String> courseTitles) {
        this.user = user;
        this.courseTitles = courseTitles;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getCourseTitles() {
        return courseTitles;
    }

    public void setCourseTitles(List<String> courseTitles) {
        this.courseTitles = courseTitles;
    }
}

