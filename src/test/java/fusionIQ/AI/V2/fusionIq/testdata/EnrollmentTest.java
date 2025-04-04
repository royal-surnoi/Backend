package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Enrollment;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EnrollmentTest {
    private User user;
    private Course course;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        // Initialize User and Course instances for testing
        user = new User();
        user.setId(1L); // Set user ID for testing
        user.setName("Test User"); // Assuming there's a setName method
        course = new Course();
        course.setId(1L); // Set course ID for testing
        course.setCourseTitle("Test Course");
        course.setCourseDescription("A course for testing purposes");
        course.setCourseLanguage("English");
        course.setLevel(Course.Level.Beginner); // Set other necessary fields
        // Initialize an Enrollment instance for testing with id=0 (or any value) and current date
        enrollment = new Enrollment(0, user, course, LocalDateTime.now(), 0L);
    }

    @Test
    void testEnrollmentCreation() {
        assertThat(enrollment).isNotNull();
        assertThat(enrollment.getUser()).isEqualTo(user);
        assertThat(enrollment.getCourse()).isEqualTo(course);
        assertThat(enrollment.getProgress()).isEqualTo(0L);
        assertThat(enrollment.getEnrollmentDate()).isBefore(LocalDateTime.now().plusSeconds(1)); // Check if the date is set correctly
    }

    @Test
    void testSetUser() {
        User newUser = new User();
        newUser.setId(2L);
        enrollment.setUser(newUser);
        assertThat(enrollment.getUser()).isEqualTo(newUser);
    }

    @Test
    void testSetCourse() {
        Course newCourse = new Course();
        newCourse.setId(2L);
        enrollment.setCourse(newCourse);
        assertThat(enrollment.getCourse()).isEqualTo(newCourse);
    }

    @Test
    void testSetProgress() {
        enrollment.setProgress(50L);
        assertThat(enrollment.getProgress()).isEqualTo(50L);
    }

    @Test
    void testSetEnrollmentDate() {
        LocalDateTime newDate = LocalDateTime.now().plusDays(1);
        enrollment.setEnrollmentDate(newDate);
        assertThat(enrollment.getEnrollmentDate()).isEqualTo(newDate);
    }

    @Test
    void testToString() {
        String expectedString = "Enrollment{id=0, user=" + user + ", course=" + course +
                ", enrollmentDate=" + enrollment.getEnrollmentDate() + ", progress=0}";
        assertThat(enrollment.toString()).isEqualToIgnoringWhitespace(expectedString);
    }
}
