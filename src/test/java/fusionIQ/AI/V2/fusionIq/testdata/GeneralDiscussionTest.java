package fusionIQ.AI.V2.fusionIq.testdata;


import fusionIQ.AI.V2.fusionIq.data.GeneralDiscussion;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class GeneralDiscussionTest {

    @Test
    void testDefaultConstructor() {
        GeneralDiscussion discussion = new GeneralDiscussion();
        assertThat(discussion.getTimestamp()).isNotNull(); // Ensure timestamp is set
    }

    @Test
    void testParameterizedConstructor() {
        User user = new User();
        user.setId(1L); // Mock user ID or set other necessary fields

        LocalDateTime now = LocalDateTime.now();
        byte[] attachment = new byte[]{1, 2, 3};

        GeneralDiscussion discussion = new GeneralDiscussion(1L, user, "Sample discussion", "Sample reply", attachment, now);

        assertThat(discussion.getId()).isEqualTo(1L);
        assertThat(discussion.getUser()).isEqualTo(user);
        assertThat(discussion.getDiscussionContent()).isEqualTo("Sample discussion");
        assertThat(discussion.getReplyContent()).isEqualTo("Sample reply");
        assertThat(discussion.getAttachment()).isEqualTo(attachment);
        assertThat(discussion.getTimestamp()).isEqualTo(now);
    }

    @Test
    void testSettersAndGetters() {
        GeneralDiscussion discussion = new GeneralDiscussion();
        User user = new User();
        user.setId(1L); // Mock user ID or set other necessary fields
        byte[] attachment = new byte[]{1, 2, 3};

        discussion.setId(2L);
        discussion.setUser(user);
        discussion.setDiscussionContent("Another discussion");
        discussion.setReplyContent("Another reply");
        discussion.setAttachment(attachment);

        assertThat(discussion.getId()).isEqualTo(2L);
        assertThat(discussion.getUser()).isEqualTo(user);
        assertThat(discussion.getDiscussionContent()).isEqualTo("Another discussion");
        assertThat(discussion.getReplyContent()).isEqualTo("Another reply");
        assertThat(discussion.getAttachment()).isEqualTo(attachment);
    }

    @Test
    void testToString() {
        User user = new User();
        user.setId(1L); // Mock user ID or set other necessary fields
        GeneralDiscussion discussion = new GeneralDiscussion(1L, user, "Sample discussion", "Sample reply", new byte[]{1, 2}, LocalDateTime.now());

        String toStringResult = discussion.toString();
        assertThat(toStringResult).contains("id=1");
        assertThat(toStringResult).contains("user=" + user);
        assertThat(toStringResult).contains("discussionContent='Sample discussion'");
        assertThat(toStringResult).contains("replyContent='Sample reply'");
    }

    @Test
    void testAttachment() {
        GeneralDiscussion discussion = new GeneralDiscussion();
        byte[] attachment = new byte[]{4, 5, 6};

        discussion.setAttachment(attachment);
        assertThat(discussion.getAttachment()).isEqualTo(attachment);
    }
}

