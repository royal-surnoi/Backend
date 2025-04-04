package fusionIQ.AI.V2.fusionIq.testdata;


import fusionIQ.AI.V2.fusionIq.data.MentorResponse;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MentorResponseTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String token = "sampleToken123";
        long mentorId = 1L;
        String username = "mentorUser";

        // Act
        MentorResponse mentorResponse = new MentorResponse(token, mentorId, username);

        // Assert
        assertThat(mentorResponse.getToken()).isEqualTo(token);
        assertThat(mentorResponse.getMentorId()).isEqualTo(mentorId);
        assertThat(mentorResponse.getUsername()).isEqualTo(username);
    }

    @Test
    void testSetters() {
        // Arrange
        MentorResponse mentorResponse = new MentorResponse("initialToken", 1L, "initialUser");

        // Act
        mentorResponse.setToken("newToken");
        mentorResponse.setMentorId(2L);
        mentorResponse.setUsername("newUser");

        // Assert
        assertThat(mentorResponse.getToken()).isEqualTo("newToken");
        assertThat(mentorResponse.getMentorId()).isEqualTo(2L);
        assertThat(mentorResponse.getUsername()).isEqualTo("newUser");
    }

    @Test
    void testSetToken() {
        // Arrange
        MentorResponse mentorResponse = new MentorResponse("initialToken", 1L, "initialUser");

        // Act
        mentorResponse.setToken("newToken");

        // Assert
        assertThat(mentorResponse.getToken()).isEqualTo("newToken");
    }

    @Test
    void testSetMentorId() {
        // Arrange
        MentorResponse mentorResponse = new MentorResponse("initialToken", 1L, "initialUser");

        // Act
        mentorResponse.setMentorId(2L);

        // Assert
        assertThat(mentorResponse.getMentorId()).isEqualTo(2L);
    }

    @Test
    void testSetUsername() {
        // Arrange
        MentorResponse mentorResponse = new MentorResponse("initialToken", 1L, "initialUser");

        // Act
        mentorResponse.setUsername("newUser");

        // Assert
        assertThat(mentorResponse.getUsername()).isEqualTo("newUser");
    }

}

