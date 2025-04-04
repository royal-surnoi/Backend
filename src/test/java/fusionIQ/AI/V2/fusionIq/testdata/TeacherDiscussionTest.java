package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.TeacherDiscussion;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherDiscussionTest {

    @Test
    void testDefaultConstructor() {
        // Test default constructor
        TeacherDiscussion teacherDiscussion = new TeacherDiscussion();
        assertThat(teacherDiscussion).isNotNull();
        assertThat(teacherDiscussion.getId()).isEqualTo(0);
        assertThat(teacherDiscussion.getUser()).isNull();
        assertThat(teacherDiscussion.getDiscussionContent()).isNull();
        assertThat(teacherDiscussion.getReplyContent()).isNull();
        assertThat(teacherDiscussion.getAttachment()).isNull();
        assertThat(teacherDiscussion.getTimestamp()).isNotNull();
    }

    @Test
    void testParameterizedConstructor() {
        // Test parameterized constructor with sample data
        byte[] attachmentData = {1, 2, 3, 4, 5};
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(1L);

        TeacherDiscussion teacherDiscussion = new TeacherDiscussion(
                1L, user, "Discussion content", "Reply content", attachmentData, now);

        assertThat(teacherDiscussion).isNotNull();
        assertThat(teacherDiscussion.getId()).isEqualTo(1L);
        assertThat(teacherDiscussion.getUser()).isEqualTo(user);
        assertThat(teacherDiscussion.getDiscussionContent()).isEqualTo("Discussion content");
        assertThat(teacherDiscussion.getReplyContent()).isEqualTo("Reply content");
        assertThat(teacherDiscussion.getAttachment()).isEqualTo(attachmentData);
        assertThat(teacherDiscussion.getTimestamp()).isEqualTo(now);
    }

    @Test
    void testGettersAndSetters() {
        // Test getters and setters
        TeacherDiscussion teacherDiscussion = new TeacherDiscussion();
        byte[] attachmentData = {6, 7, 8, 9, 10};
        LocalDateTime now = LocalDateTime.now().minusDays(1);

        User user = new User();
        user.setId(2L);

        teacherDiscussion.setId(2L);
        teacherDiscussion.setUser(user);
        teacherDiscussion.setDiscussionContent("Updated discussion content");
        teacherDiscussion.setReplyContent("Updated reply content");
        teacherDiscussion.setAttachment(attachmentData);
        teacherDiscussion.setTimestamp(now);

        assertThat(teacherDiscussion.getId()).isEqualTo(2L);
        assertThat(teacherDiscussion.getUser()).isEqualTo(user);
        assertThat(teacherDiscussion.getDiscussionContent()).isEqualTo("Updated discussion content");
        assertThat(teacherDiscussion.getReplyContent()).isEqualTo("Updated reply content");
        assertThat(teacherDiscussion.getAttachment()).isEqualTo(attachmentData);
        assertThat(teacherDiscussion.getTimestamp()).isEqualTo(now);
    }

    @Test
    void testToString() {
        // Test toString method
        byte[] attachmentData = {1, 2, 3, 4, 5};
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(1L);

        TeacherDiscussion teacherDiscussion = new TeacherDiscussion(
                1L, user, "Discussion content", "Reply content", attachmentData, now);

        String expectedToString = "TeacherDiscussion{" +
                "id=" + teacherDiscussion.getId() +
                ", user=" + teacherDiscussion.getUser() +
                ", discussionContent='" + teacherDiscussion.getDiscussionContent() + '\'' +
                ", replyContent='" + teacherDiscussion.getReplyContent() + '\'' +
                ", attachment=" + Arrays.toString(teacherDiscussion.getAttachment()) +
                ", timestamp=" + teacherDiscussion.getTimestamp() +
                '}';

        assertThat(teacherDiscussion.toString()).isEqualTo(expectedToString);
    }
}
