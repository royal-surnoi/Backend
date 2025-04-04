package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.AIQuiz;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AIQuizTests {

    private AIQuiz aiQuiz;
    private Lesson lesson;
    private Course course;
    private User user;

    @BeforeEach
    void setUp() {
        lesson = new Lesson(); // Assuming Lesson class is defined elsewhere
        lesson.setId(1L);

        course = new Course(); // Assuming Course class is defined elsewhere
        course.setId(1L);

        user = new User(); // Assuming User class is defined elsewhere
        user.setId(1L);

        aiQuiz = new AIQuiz();
    }

    @Test
    void testDefaultConstructor() {
        assertThat(aiQuiz.getAIQuizName()).isNull();
        assertThat(aiQuiz.getLesson()).isNull();
        assertThat(aiQuiz.getCourse()).isNull();
        assertThat(aiQuiz.getUser()).isNull();
        assertThat(aiQuiz.getCreatedAt()).isNotNull();
    }

    @Test
    void testParameterizedConstructor() {
        LocalDateTime now = LocalDateTime.now();
        AIQuiz aiQuiz = new AIQuiz(
                1L,
                "Quiz Name",
                lesson,
                course,
                user,
                now
        );

        assertThat(aiQuiz.getId()).isEqualTo(1L);
        assertThat(aiQuiz.getAIQuizName()).isEqualTo("Quiz Name");
        assertThat(aiQuiz.getLesson()).isEqualTo(lesson);
        assertThat(aiQuiz.getCourse()).isEqualTo(course);
        assertThat(aiQuiz.getUser()).isEqualTo(user);
        assertThat(aiQuiz.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        aiQuiz.setId(2L);
        aiQuiz.setAIQuizName("Updated Quiz Name");
        aiQuiz.setLesson(lesson);
        aiQuiz.setCourse(course);
        aiQuiz.setUser(user);
        aiQuiz.setCreatedAt(now);

        assertThat(aiQuiz.getId()).isEqualTo(2L);
        assertThat(aiQuiz.getAIQuizName()).isEqualTo("Updated Quiz Name");
        assertThat(aiQuiz.getLesson()).isEqualTo(lesson);
        assertThat(aiQuiz.getCourse()).isEqualTo(course);
        assertThat(aiQuiz.getUser()).isEqualTo(user);
        assertThat(aiQuiz.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        aiQuiz.setId(1L);
        aiQuiz.setAIQuizName("Quiz Name");
        aiQuiz.setLesson(lesson);
        aiQuiz.setCourse(course);
        aiQuiz.setUser(user);
        aiQuiz.setCreatedAt(now);

        String expected = "AIQuiz{" +
                "id=" + 1L +
                ", AIQuizName='Quiz Name'" +
                ", lesson=" + lesson +
                ", course=" + course +
                ", user=" + user +
                ", createdAt=" + now +
                '}';

        assertThat(aiQuiz.toString()).isEqualTo(expected);
    }
}
