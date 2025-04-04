package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.data.LessonModule;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LessonModuleTest {

    @Test
    void testCreateLessonModule() {
        LessonModule lessonModule = new LessonModule();
        lessonModule.setModuleName("Introduction to Programming");
        assertThat(lessonModule.getModuleName()).isEqualTo("Introduction to Programming");
    }

    @Test
    void testSetAndGetModuleName() {
        LessonModule lessonModule = new LessonModule();
        lessonModule.setModuleName("Data Structures");
        assertThat(lessonModule.getModuleName()).isEqualTo("Data Structures");
    }

    @Test
    void testAddLessons() {
        LessonModule lessonModule = new LessonModule();
        List<Lesson> lessons = new ArrayList<>();
        Lesson lesson = new Lesson(); // Assuming Lesson class has a default constructor
        lesson.setLessonTitle("Lesson 1");
        lesson.setLessonModule(lessonModule); // Ensure bidirectional relationship is maintained
        lessons.add(lesson);

        lessonModule.setLesson(lessons);
        assertThat(lessonModule.getLesson()).contains(lesson);
    }

    @Test
    void testSetAndGetCourse() {
        LessonModule lessonModule = new LessonModule();
        Course course = new Course(); // Assuming Course class has a default constructor
        course.setCourseTitle("Java Programming");

        lessonModule.setCourse(course);
        assertThat(lessonModule.getCourse()).isEqualTo(course);
    }

    @Test
    void testSetAndGetUser() {
        LessonModule lessonModule = new LessonModule();
        User user = new User(); // Assuming User class has a default constructor
        user.setId(1L);

        lessonModule.setUser(user);
        assertThat(lessonModule.getUser()).isEqualTo(user);
    }

    @Test
    void testToString() {
        LessonModule lessonModule = new LessonModule();
        lessonModule.setModuleName("Advanced Java");
        List<Lesson> lessons = new ArrayList<>(); // Set to empty list instead of null for accurate string representation
        lessonModule.setLesson(lessons);

        // Updated expected string to include the `teacherFeedbacks` field
        String expectedString = "LessonModule{id=null, moduleName='Advanced Java', lesson=[], teacherFeedbacks=null, course=null, user=null}";
        assertThat(lessonModule.toString()).isEqualTo(expectedString);
    }
}
