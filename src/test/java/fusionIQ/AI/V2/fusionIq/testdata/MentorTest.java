package fusionIQ.AI.V2.fusionIq.testdata;


import fusionIQ.AI.V2.fusionIq.data.Mentor;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MentorTest {

    private Mentor mentor;
    private User user;

    @BeforeEach
    void setUp() {
        // Initialize a user and mentor for testing
        user = new User();
        user.setId(1L); // Example user ID

        mentor = new Mentor();
        mentor.setMentorId(1L);
        mentor.setUsername("mentorUsername");
        mentor.setPassword("mentorPassword");
        mentor.setUser(user);
    }

    @Test
    void testMentorInitialization() {
        assertThat(mentor.getMentorId()).isEqualTo(1L);
        assertThat(mentor.getUsername()).isEqualTo("mentorUsername");
        assertThat(mentor.getPassword()).isEqualTo("mentorPassword");
        assertThat(mentor.getUser()).isEqualTo(user);
    }

    @Test
    void testSetUsername() {
        mentor.setUsername("newUsername");
        assertThat(mentor.getUsername()).isEqualTo("newUsername");
    }

    @Test
    void testSetPassword() {
        mentor.setPassword("newPassword");
        assertThat(mentor.getPassword()).isEqualTo("newPassword");
    }

    @Test
    void testSetUser() {
        User newUser = new User();
        newUser.setId(2L); // New example user ID
        mentor.setUser(newUser);
        assertThat(mentor.getUser()).isEqualTo(newUser);
    }

    @Test
    void testToString() {
        String expectedString = "Mentor{" +
                "mentorId=1" +
                ", username='mentorUsername'" +
                ", password='mentorPassword'" +
                ", user=" + user +
                '}';
        assertThat(mentor.toString()).isEqualTo(expectedString);
    }
}

