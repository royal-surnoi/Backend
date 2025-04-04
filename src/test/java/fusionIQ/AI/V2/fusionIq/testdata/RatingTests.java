package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Rating;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class RatingTests {

    @Test
    void testDefaultConstructor() {
        Rating rating = new Rating();

        // Verifying that the default constructor initializes fields correctly
        assertThat(rating.getId()).isNull();
        assertThat(rating.getRatingValue()).isEqualTo(0); // Default int value
        assertThat(rating.getStars()).isEqualTo(0); // Default int value
        assertThat(rating.getCourse()).isNull();
        assertThat(rating.getUser()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        Course course = new Course();
        User user = new User();

        Rating rating = new Rating(1L, 5, 4, course, user);

        // Verifying that the parameterized constructor initializes fields correctly
        assertThat(rating.getId()).isEqualTo(1L);
        assertThat(rating.getRatingValue()).isEqualTo(5);
        assertThat(rating.getStars()).isEqualTo(4);
        assertThat(rating.getCourse()).isEqualTo(course);
        assertThat(rating.getUser()).isEqualTo(user);
    }

    @Test
    void testSettersAndGetters() {
        Rating rating = new Rating();
        Course course = new Course();
        User user = new User();

        rating.setId(1L);
        rating.setRatingValue(8);
        rating.setStars(5);
        rating.setCourse(course);
        rating.setUser(user);

        // Verifying the setters and getters
        assertThat(rating.getId()).isEqualTo(1L);
        assertThat(rating.getRatingValue()).isEqualTo(8);
        assertThat(rating.getStars()).isEqualTo(5);
        assertThat(rating.getCourse()).isEqualTo(course);
        assertThat(rating.getUser()).isEqualTo(user);
    }

    @Test
    void testToString() {
        Course course = new Course();
        User user = new User();

        Rating rating = new Rating(1L, 5, 4, course, user);

        String expectedToString = "Rating{" +
                "id=" + rating.getId() +
                ", ratingValue=" + rating.getRatingValue() +
                ", stars=" + rating.getStars() +
                ", course=" + course +
                ", user=" + user +
                '}';

        // Verifying the toString method
        assertThat(rating.toString()).isEqualTo(expectedToString);
    }
}
