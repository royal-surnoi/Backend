package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Feedback;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class FeedbackTest {

    @Test
    void testDefaultConstructor() {
        // Test default constructor
        Feedback feedback = new Feedback();

        // Assert that fields are initialized to default values
        assertThat(feedback.getId()).isNull();
        assertThat(feedback.isFeedback()).isFalse(); // Default boolean value is false
        assertThat(feedback.getUser()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        // Create a sample user
        User user = new User();
        user.setId(1L); // Set user ID or any other required fields

        // Create feedback using the parameterized constructor
        Feedback feedback = new Feedback(1L, true, user);

        // Assert that fields are set correctly
        assertThat(feedback.getId()).isEqualTo(1L);
        assertThat(feedback.isFeedback()).isTrue();
        assertThat(feedback.getUser()).isEqualTo(user);
    }

    @Test
    void testSettersAndGetters() {
        // Create feedback
        Feedback feedback = new Feedback();

        // Create a sample user
        User user = new User();
        user.setId(1L); // Set user ID

        // Set values using setters
        feedback.setId(2L);
        feedback.setFeedback(true);
        feedback.setUser(user);

        // Assert that the getters return the correct values
        assertThat(feedback.getId()).isEqualTo(2L);
        assertThat(feedback.isFeedback()).isTrue();
        assertThat(feedback.getUser()).isEqualTo(user);
    }

    @Test
    void testToString() {
        // Create a sample user
        User user = new User();
        user.setId(1L); // Set user ID

        // Create feedback
        Feedback feedback = new Feedback(1L, true, user);

        // Assert that toString method returns the expected format
        String expectedString = "Feedback{id=1, feedback=true, user=" + user + "}";
        assertThat(feedback.toString()).isEqualTo(expectedString);
    }
}
