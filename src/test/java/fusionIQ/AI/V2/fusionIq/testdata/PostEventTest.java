package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.PostEvent;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class PostEventTest {

    @Test
    void testPostEventCreation() {
        Long userId = 1L;
        Long postId = 10L;

        // Create a new PostEvent object
        PostEvent postEvent = new PostEvent(userId, postId);

        // Assert that the object is created correctly
        assertThat(postEvent).isNotNull();
        assertThat(postEvent.getUserId()).isEqualTo(userId);
        assertThat(postEvent.getPostId()).isEqualTo(postId);
    }

    @Test
    void testPostEventWithNullValues() {
        // Create a new PostEvent with null values
        PostEvent postEvent = new PostEvent(null, null);

        // Assert that the object is created correctly even with null values
        assertThat(postEvent).isNotNull();
        assertThat(postEvent.getUserId()).isNull();
        assertThat(postEvent.getPostId()).isNull();
    }

    @Test
    void testPostEventWithOnlyUserId() {
        Long userId = 1L;

        // Create a new PostEvent with only userId set
        PostEvent postEvent = new PostEvent(userId, null);

        // Assert that the userId is set and postId is null
        assertThat(postEvent).isNotNull();
        assertThat(postEvent.getUserId()).isEqualTo(userId);
        assertThat(postEvent.getPostId()).isNull();
    }

    @Test
    void testPostEventWithOnlyPostId() {
        Long postId = 10L;

        // Create a new PostEvent with only postId set
        PostEvent postEvent = new PostEvent(null, postId);

        // Assert that the postId is set and userId is null
        assertThat(postEvent).isNotNull();
        assertThat(postEvent.getUserId()).isNull();
        assertThat(postEvent.getPostId()).isEqualTo(postId);
    }
}

