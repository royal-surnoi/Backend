package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class CourseTest {

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
    }

    @Test
    void testDefaultConstructor() {
        assertThat(course.getCreatedAt()).isNotNull();
        assertThat(course.getUpdatedAt()).isNotNull();
        assertThat(course.getPromoCodeExpiration()).isNotNull();
    }

    @Test
    void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        User user = new User();
        user.setId(1L);

        course.setId(1L);
        course.setCourseTitle("Test Course");
        course.setCourseDescription("Test Description");
        course.setCourseLanguage("English");
        course.setLevel(Course.Level.Intermediate);
        course.setCourseDuration("8 hours");
        course.setCreatedAt(now);
        course.setUpdatedAt(now);
        course.setLessons(new ArrayList<>());
        course.setSubmissions(new ArrayList<>());
        course.setUser(user);
        course.setEnrollments(new ArrayList<>());
        course.setVideos(new ArrayList<>());
        course.setReviews(new ArrayList<>());
        course.setProjects(new ArrayList<>());
        course.setAssignments(new ArrayList<>());
        course.setCourseTools(new ArrayList<>());
        course.setSubmitProjects(new ArrayList<>());
        course.setAnnouncements(new ArrayList<>());
        course.setQuizzes(new ArrayList<>());
        course.setAnswers(new ArrayList<>());
        course.setCourseFee(200L);
        course.setDiscountFee(100L);
        course.setDiscountPercentage(50L);
        course.setCurrency("EUR");
        course.setPromoCodeExpiration(now);
        course.setLevel_1("Level 1");
        course.setLevel_2("Level 2");
        course.setLevel_3("Level 3");
        course.setLevel_4("Level 4");
        course.setLevel_5("Level 5");
        course.setLevel_6("Level 6");
        course.setLevel_7("Level 7");
        course.setLevel_8("Level 8");
        course.setPromoCode("PROMO2024");
        course.setCourseType("Offline");
        course.setCoursePercentage("25%");
        course.setCourseTerm("Fall");

        assertThat(course.getId()).isEqualTo(1L);
        assertThat(course.getCourseTitle()).isEqualTo("Test Course");
        assertThat(course.getCourseDescription()).isEqualTo("Test Description");
        assertThat(course.getCourseLanguage()).isEqualTo("English");
        assertThat(course.getLevel()).isEqualTo(Course.Level.Intermediate);
        assertThat(course.getCourseDuration()).isEqualTo("8 hours");
        assertThat(course.getCreatedAt()).isEqualTo(now);
        assertThat(course.getUpdatedAt()).isEqualTo(now);
        assertThat(course.getCourseFee()).isEqualTo(200L);
        assertThat(course.getDiscountFee()).isEqualTo(100L);
        assertThat(course.getDiscountPercentage()).isEqualTo(50L);
        assertThat(course.getCurrency()).isEqualTo("EUR");
        assertThat(course.getPromoCodeExpiration()).isEqualTo(now);
        assertThat(course.getPromoCode()).isEqualTo("PROMO2024");
        assertThat(course.getCourseType()).isEqualTo("Offline");
        assertThat(course.getCoursePercentage()).isEqualTo("25%");
        assertThat(course.getCourseTerm()).isEqualTo("Fall");
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        course.setId(1L);
        course.setCourseTitle("Test Course");
        course.setCourseDescription("Test Description");
        course.setCourseLanguage("English");
        course.setLevel(Course.Level.Advanced);
        course.setCourseDuration("6 hours");
        course.setCreatedAt(now);
        course.setUpdatedAt(now);
        course.setPromoCodeExpiration(now);

        String expectedToString = "Course{" +
                "id=" + 1L +
                ", courseTitle='Test Course'" +
                ", courseDescription='Test Description'" +
                ", courseLanguage='English'" +
                ", level=Advanced" +
                ", courseDuration='6 hours'" +
                ", createdAt=" + now +
                ", updatedAt=" + now +
                ", lessons=null" +
                ", submissions=null" +
                ", user=null" +
                ", enrollments=null" +
                ", videos=null" +
                ", reviews=null" +
                ", projects=null" +
                ", assignments=null" +
                ", courseTools=null" +
                ", submitProjects=null" +
                ", announcements=null" +
                ", quizzes=null" +
                ", answers=null" +
                ", courseFee=null" +
                ", discountFee=null" +
                ", discountPercentage=null" +
                ", currency='null'" +
                ", promoCodeExpiration=" + now +
                ", level_1='null'" +
                ", level_2='null'" +
                ", level_3='null'" +
                ", level_4='null'" +
                ", level_5='null'" +
                ", level_6='null'" +
                ", level_7='null'" +
                ", level_8='null'" +
                ", promoCode='null'" +
                ", courseType='null'" +
                ", coursePercentage='null'" +
                ", courseTerm='null'" +
                ", courseRating=0.0" +  // Include the courseRating field
                ", courseImage=null" +
                ", courseDocument=null" +
                '}';

        assertThat(course.toString()).isEqualTo(expectedToString);
    }


    @Test
    void testEqualityAndHashCode() {
        Course course1 = new Course(1L);
        Course course2 = new Course(1L);

        // Use AssertJ's recursive comparison to compare fields
        assertThat(course1)
                .usingRecursiveComparison()
                .isEqualTo(course2);

        // Changing a field should make them different
        course2.setId(2L);

        assertThat(course1)
                .usingRecursiveComparison()
                .isNotEqualTo(course2);
    }
}
