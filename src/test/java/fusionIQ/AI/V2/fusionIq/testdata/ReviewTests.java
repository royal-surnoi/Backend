package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Review;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewTests {

    @Test
    void testDefaultConstructor() {
        Review review = new Review();

        // Verifying that the default constructor initializes fields correctly
        assertThat(review.getId()).isEqualTo(0); // long default value
        assertThat(review.getCourse()).isNull();
        assertThat(review.getUser()).isNull();
        assertThat(review.getRating()).isNull();
        assertThat(review.getReviewComment()).isNull();
        assertThat(review.getTimestamp()).isNotNull(); // Should be initialized to current time
        assertThat(review.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void testParameterizedConstructor() {
        Course course = new Course();
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        Review review = new Review(1L, course, user, 5L, "Great course!", now);

        // Verifying that the parameterized constructor initializes fields correctly
        assertThat(review.getId()).isEqualTo(1L);
        assertThat(review.getCourse()).isEqualTo(course);
        assertThat(review.getUser()).isEqualTo(user);
        assertThat(review.getRating()).isEqualTo(5L);
        assertThat(review.getReviewComment()).isEqualTo("Great course!");
        assertThat(review.getTimestamp()).isEqualTo(now);
    }

    @Test
    void testSettersAndGetters() {
        Review review = new Review();
        Course course = new Course();
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        review.setId(1L);
        review.setCourse(course);
        review.setUser(user);
        review.setRating(4L);
        review.setReviewComment("Good course.");
        review.setTimestamp(now);

        // Verifying the setters and getters
        assertThat(review.getId()).isEqualTo(1L);
        assertThat(review.getCourse()).isEqualTo(course);
        assertThat(review.getUser()).isEqualTo(user);
        assertThat(review.getRating()).isEqualTo(4L);
        assertThat(review.getReviewComment()).isEqualTo("Good course.");
        assertThat(review.getTimestamp()).isEqualTo(now);
    }

    @Test
    void testToString() {
        Course course = new Course();
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        Review review = new Review(1L, course, user, 5L, "Excellent course!", now);

        String expectedToString = "Review{" +
                "id=" + review.getId() +
                ", course=" + review.getCourse() +
                ", user=" + review.getUser() +
                ", rating=" + review.getRating() +
                ", reviewComment='" + review.getReviewComment() + '\'' +
                ", timestamp=" + review.getTimestamp() +
                '}';

        // Verifying the toString method
        assertThat(review.toString()).isEqualTo(expectedToString);
    }
}
