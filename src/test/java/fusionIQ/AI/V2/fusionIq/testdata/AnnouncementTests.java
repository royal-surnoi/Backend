package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Announcement;
import fusionIQ.AI.V2.fusionIq.data.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnouncementTests {

    private Announcement announcement;
    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course(); // Assuming Course class is defined elsewhere
        course.setId(1L);

        announcement = new Announcement();
    }

    @Test
    void testDefaultConstructor() {
        assertThat(announcement.getId()).isEqualTo(0L); // Default value for long
        assertThat(announcement.getCourse()).isNull();
        assertThat(announcement.getUserAnnouncementTitle()).isNull();
        assertThat(announcement.getUserAnnouncementDescription()).isNull();
        assertThat(announcement.getCourseAnnouncementTitle()).isNull();
        assertThat(announcement.getCourseAnnouncementDescription()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        Announcement announcement = new Announcement(
                1L,
                course,
                "User Announcement Title",
                "User Announcement Description",
                "Course Announcement Title",
                "Course Announcement Description"
        );

        assertThat(announcement.getId()).isEqualTo(1L);
        assertThat(announcement.getCourse()).isEqualTo(course);
        assertThat(announcement.getUserAnnouncementTitle()).isEqualTo("User Announcement Title");
        assertThat(announcement.getUserAnnouncementDescription()).isEqualTo("User Announcement Description");
        assertThat(announcement.getCourseAnnouncementTitle()).isEqualTo("Course Announcement Title");
        assertThat(announcement.getCourseAnnouncementDescription()).isEqualTo("Course Announcement Description");
    }

    @Test
    void testSettersAndGetters() {
        announcement.setId(2L);
        announcement.setCourse(course);
        announcement.setUserAnnouncementTitle("Updated User Announcement Title");
        announcement.setUserAnnouncementDescription("Updated User Announcement Description");
        announcement.setCourseAnnouncementTitle("Updated Course Announcement Title");
        announcement.setCourseAnnouncementDescription("Updated Course Announcement Description");

        assertThat(announcement.getId()).isEqualTo(2L);
        assertThat(announcement.getCourse()).isEqualTo(course);
        assertThat(announcement.getUserAnnouncementTitle()).isEqualTo("Updated User Announcement Title");
        assertThat(announcement.getUserAnnouncementDescription()).isEqualTo("Updated User Announcement Description");
        assertThat(announcement.getCourseAnnouncementTitle()).isEqualTo("Updated Course Announcement Title");
        assertThat(announcement.getCourseAnnouncementDescription()).isEqualTo("Updated Course Announcement Description");
    }

    @Test
    void testToString() {
        announcement.setId(1L);
        announcement.setCourse(course);
        announcement.setUserAnnouncementTitle("User Announcement Title");
        announcement.setUserAnnouncementDescription("User Announcement Description");
        announcement.setCourseAnnouncementTitle("Course Announcement Title");
        announcement.setCourseAnnouncementDescription("Course Announcement Description");

        String expected = "Announcement{" +
                "id=" + 1L +
                ", course=" + course +
                ", userAnnouncementTitle='User Announcement Title'" +
                ", userAnnouncementDescription='User Announcement Description'" +
                ", courseAnnouncementTitle='Course Announcement Title'" +
                ", courseAnnouncementDescription='Course Announcement Description'" +
                '}';

        assertThat(announcement.toString()).isEqualTo(expected);
    }
}
