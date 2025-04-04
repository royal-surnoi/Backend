package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.CourseVideoTrailer;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

class CourseVideoTrailerTests {

    @Test
    void testDefaultConstructor() {
        CourseVideoTrailer courseVideoTrailer = new CourseVideoTrailer();

        // Verifying that the default constructor initializes fields correctly
        assertThat(courseVideoTrailer.getId()).isNull();
        assertThat(courseVideoTrailer.getVideoTrailerTitle()).isNull();
        assertThat(courseVideoTrailer.getS3Key()).isNull();
        assertThat(courseVideoTrailer.getS3Url()).isNull();
        assertThat(courseVideoTrailer.getVideoTrailerDescription()).isNull();
        assertThat(courseVideoTrailer.getCreatedAt()).isNotNull(); // Should be initialized to current time
        assertThat(courseVideoTrailer.getCourse()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        Course course = new Course();
        course.setId(1L);
        LocalDateTime createdAt = LocalDateTime.now();

        CourseVideoTrailer courseVideoTrailer = new CourseVideoTrailer(
                1L,
                "Introduction to Java",
                "s3-key-123",
                "https://s3.amazonaws.com/videos/trailer.mp4",
                "A brief introduction to the Java programming language.",
                createdAt,
                course
        );

        // Verifying that the parameterized constructor initializes fields correctly
        assertThat(courseVideoTrailer.getId()).isEqualTo(1L);
        assertThat(courseVideoTrailer.getVideoTrailerTitle()).isEqualTo("Introduction to Java");
        assertThat(courseVideoTrailer.getS3Key()).isEqualTo("s3-key-123");
        assertThat(courseVideoTrailer.getS3Url()).isEqualTo("https://s3.amazonaws.com/videos/trailer.mp4");
        assertThat(courseVideoTrailer.getVideoTrailerDescription()).isEqualTo("A brief introduction to the Java programming language.");
        assertThat(courseVideoTrailer.getCreatedAt()).isEqualTo(createdAt);
        assertThat(courseVideoTrailer.getCourse()).isEqualTo(course);
    }

    @Test
    void testSettersAndGetters() {
        Course course = new Course();
        course.setId(1L);
        LocalDateTime createdAt = LocalDateTime.now();

        CourseVideoTrailer courseVideoTrailer = new CourseVideoTrailer();
        courseVideoTrailer.setId(1L);
        courseVideoTrailer.setVideoTrailerTitle("Advanced Java Concepts");
        courseVideoTrailer.setS3Key("s3-key-456");
        courseVideoTrailer.setS3Url("https://s3.amazonaws.com/videos/advanced_trailer.mp4");
        courseVideoTrailer.setVideoTrailerDescription("An advanced overview of Java programming.");
        courseVideoTrailer.setCreatedAt(createdAt);
        courseVideoTrailer.setCourse(course);

        // Verifying the setters and getters
        assertThat(courseVideoTrailer.getId()).isEqualTo(1L);
        assertThat(courseVideoTrailer.getVideoTrailerTitle()).isEqualTo("Advanced Java Concepts");
        assertThat(courseVideoTrailer.getS3Key()).isEqualTo("s3-key-456");
        assertThat(courseVideoTrailer.getS3Url()).isEqualTo("https://s3.amazonaws.com/videos/advanced_trailer.mp4");
        assertThat(courseVideoTrailer.getVideoTrailerDescription()).isEqualTo("An advanced overview of Java programming.");
        assertThat(courseVideoTrailer.getCreatedAt()).isEqualTo(createdAt);
        assertThat(courseVideoTrailer.getCourse()).isEqualTo(course);
    }

    @Test
    void testToString() {
        Course course = new Course();
        course.setId(1L);
        LocalDateTime createdAt = LocalDateTime.now();

        CourseVideoTrailer courseVideoTrailer = new CourseVideoTrailer(
                1L,
                "Java Design Patterns",
                "s3-key-789",
                "https://s3.amazonaws.com/videos/design_patterns_trailer.mp4",
                "An introduction to design patterns in Java.",
                createdAt,
                course
        );

        String expectedToString = "CourseVideoTrailer{" +
                "id=" + courseVideoTrailer.getId() +
                ", videoTrailerTitle='" + courseVideoTrailer.getVideoTrailerTitle() + '\'' +
                ", s3Key='" + courseVideoTrailer.getS3Key() + '\'' +
                ", s3Url='" + courseVideoTrailer.getS3Url() + '\'' +
                ", videoTrailerDescription='" + courseVideoTrailer.getVideoTrailerDescription() + '\'' +
                ", createdAt=" + courseVideoTrailer.getCreatedAt() +
                ", course=" + courseVideoTrailer.getCourse() +
                '}';

        // Verifying the toString method
        assertThat(courseVideoTrailer.toString()).isEqualTo(expectedToString);
    }
}
