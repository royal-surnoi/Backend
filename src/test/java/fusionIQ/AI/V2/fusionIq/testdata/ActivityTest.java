package fusionIQ.AI.V2.fusionIq.testdata;



import fusionIQ.AI.V2.fusionIq.data.Activity;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class ActivityTest {

    @Test
    void testDefaultConstructor() {
        Activity activity = new Activity();
        assertThat(activity.getCreatedAt()).isNotNull();
        assertThat(activity.getUpdatedAt()).isNotNull();
        assertThat(activity.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(activity.getUpdatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void testParameterizedConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Lesson lesson = new Lesson(); // Assuming Lesson class exists and has a default constructor
        Activity activity = new Activity(1L, lesson, "Test Title", Activity.ActivityType.Quiz, "Test Content", now, now);

        assertThat(activity.getId()).isEqualTo(1L);
        assertThat(activity.getLesson()).isEqualTo(lesson);
        assertThat(activity.getActivityTitle()).isEqualTo("Test Title");
        assertThat(activity.getActivityType()).isEqualTo(Activity.ActivityType.Quiz);
        assertThat(activity.getActivityContent()).isEqualTo("Test Content");
        assertThat(activity.getCreatedAt()).isEqualTo(now);
        assertThat(activity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testSettersAndGetters() {
        Activity activity = new Activity();
        Lesson lesson = new Lesson(); // Assuming Lesson class exists and has a default constructor
        LocalDateTime now = LocalDateTime.now();

        activity.setId(1L);
        activity.setLesson(lesson);
        activity.setActivityTitle("New Title");
        activity.setActivityType(Activity.ActivityType.Assignment);
        activity.setActivityContent("New Content");
        activity.setCreatedAt(now);
        activity.setUpdatedAt(now);

        assertThat(activity.getId()).isEqualTo(1L);
        assertThat(activity.getLesson()).isEqualTo(lesson);
        assertThat(activity.getActivityTitle()).isEqualTo("New Title");
        assertThat(activity.getActivityType()).isEqualTo(Activity.ActivityType.Assignment);
        assertThat(activity.getActivityContent()).isEqualTo("New Content");
        assertThat(activity.getCreatedAt()).isEqualTo(now);
        assertThat(activity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testToString() {
        Lesson lesson = new Lesson(); // Assuming Lesson class exists and has a default constructor
        LocalDateTime now = LocalDateTime.now();
        Activity activity = new Activity(1L, lesson, "Test Title", Activity.ActivityType.Project, "Test Content", now, now);

        String expectedString = "Activity{id=1, lesson=" + lesson + ", activityTitle='Test Title', activityType=Project, activityContent='Test Content', createdAt=" + now + ", updatedAt=" + now + "}";
        assertThat(activity.toString()).isEqualTo(expectedString);
    }
}

