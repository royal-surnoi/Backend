package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserEnrollmentResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserEnrollmentResponseTests {

    @Test
    void testConstructorAndGetters() {
        User user = new User();
        List<String> courseTitles = Arrays.asList("Java Basics", "Spring Boot", "Microservices");

        UserEnrollmentResponse response = new UserEnrollmentResponse(user, courseTitles);

        // Verify that the constructor initializes the fields correctly
        assertThat(response.getUser()).isEqualTo(user);
        assertThat(response.getCourseTitles()).isEqualTo(courseTitles);
    }

    @Test
    void testSetters() {
        UserEnrollmentResponse response = new UserEnrollmentResponse(null, null);
        User user = new User();
        List<String> courseTitles = Arrays.asList("Java Basics", "Spring Boot", "Microservices");

        response.setUser(user);
        response.setCourseTitles(courseTitles);

        // Verify that the setters update the fields correctly
        assertThat(response.getUser()).isEqualTo(user);
        assertThat(response.getCourseTitles()).isEqualTo(courseTitles);
    }
}
