package fusionIQ.AI.V2.fusionIq.testdata;



import fusionIQ.AI.V2.fusionIq.data.Discussion;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

class DiscussionTest {

    @Test
    void testDiscussionConstructorAndGetters() {
        // Set up a user and other required fields
        User user = new User(); // Replace with actual User object initialization
        user.setId(1L); // Example ID
        String discussionContent = "This is a discussion.";
        String replyContent = "This is a reply.";
        LocalDateTime timestamp = LocalDateTime.now();

        // Create a Discussion object using the constructor
        Discussion discussion = new Discussion(1L, user, discussionContent, replyContent, timestamp);

        // Assert that the fields are set correctly
        assertThat(discussion.getId()).isEqualTo(1L);
        assertThat(discussion.getUser()).isEqualTo(user);
        assertThat(discussion.getDiscussionContent()).isEqualTo(discussionContent);
        assertThat(discussion.getReplyContent()).isEqualTo(replyContent);
        assertThat(discussion.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    void testDefaultTimestamp() {
        // Create a Discussion object using the default constructor
        Discussion discussion = new Discussion();

        // Assert that the timestamp is set to the current time
        assertThat(discussion.getTimestamp()).isNotNull();
    }

    @Test
    void testSettersAndGetters() {
        // Set up a user and other required fields
        User user = new User(); // Replace with actual User object initialization
        user.setId(2L); // Example ID

        // Create a Discussion object
        Discussion discussion = new Discussion();

        // Set fields using setters
        discussion.setId(2L);
        discussion.setUser(user);
        discussion.setDiscussionContent("Another discussion.");
        discussion.setReplyContent("Another reply.");

        // Assert that the fields are set correctly
        assertThat(discussion.getId()).isEqualTo(2L);
        assertThat(discussion.getUser()).isEqualTo(user);
        assertThat(discussion.getDiscussionContent()).isEqualTo("Another discussion.");
        assertThat(discussion.getReplyContent()).isEqualTo("Another reply.");
    }

    @Test
    void testToString() {
        // Set up a user
        User user = new User(); // Replace with actual User object initialization
        user.setId(3L); // Example ID

        // Create a Discussion object
        Discussion discussion = new Discussion(3L, user, "Test discussion", "Test reply", LocalDateTime.now());

        // Check the toString output
        String expectedString = "Discussion{" +
                "id=3, " +
                "user=" + user + ", " +
                "discussionContent='Test discussion', " +
                "replyContent='Test reply', " +
                "timestamp=" + discussion.getTimestamp() +
                '}';
        assertThat(discussion.toString()).isEqualTo(expectedString);
    }
}
