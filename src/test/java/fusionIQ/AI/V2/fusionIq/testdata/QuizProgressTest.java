package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Quiz;
import fusionIQ.AI.V2.fusionIq.data.QuizProgress;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

class QuizProgressTest {

    @Test
    void testDefaultConstructor() {
        QuizProgress quizProgress = new QuizProgress();
        assertThat(quizProgress.getCompletedAt()).isNotNull(); // Should be initialized to now
    }

    @Test
    void testParameterizedConstructor() {
        User user = new User();
        Quiz quiz = new Quiz();
        Course course = new Course();
        LocalDateTime completedAt = LocalDateTime.now();
        boolean completed = true;

        QuizProgress quizProgress = new QuizProgress(1L, user, quiz, course, completedAt, completed);

        assertThat(quizProgress.getId()).isEqualTo(1L);
        assertThat(quizProgress.getUser()).isEqualTo(user);
        assertThat(quizProgress.getQuiz()).isEqualTo(quiz);
        assertThat(quizProgress.getCourse()).isEqualTo(course);
        assertThat(quizProgress.getCompletedAt()).isEqualTo(completedAt);
        assertThat(quizProgress.isCompleted()).isTrue();
    }

    @Test
    void testSettersAndGetters() {
        QuizProgress quizProgress = new QuizProgress();

        User user = new User();
        Quiz quiz = new Quiz();
        Course course = new Course();
        LocalDateTime completedAt = LocalDateTime.now();
        boolean completed = false;

        quizProgress.setUser(user);
        quizProgress.setQuiz(quiz);
        quizProgress.setCourse(course);
        quizProgress.setCompletedAt(completedAt);
        quizProgress.setCompleted(completed);

        assertThat(quizProgress.getUser()).isEqualTo(user);
        assertThat(quizProgress.getQuiz()).isEqualTo(quiz);
        assertThat(quizProgress.getCourse()).isEqualTo(course);
        assertThat(quizProgress.getCompletedAt()).isEqualTo(completedAt);
        assertThat(quizProgress.isCompleted()).isFalse();
    }

    @Test
    void testToString() {
        User user = new User();
        Quiz quiz = new Quiz();
        Course course = new Course();
        LocalDateTime completedAt = LocalDateTime.now();
        boolean completed = true;

        QuizProgress quizProgress = new QuizProgress(1L, user, quiz, course, completedAt, completed);

        String expectedString = "QuizProgress{" +
                "id=1" +
                ", user=" + user +
                ", quiz=" + quiz +
                ", course=" + course +
                ", completedAt=" + completedAt +
                ", completed=true" +
                '}';
        assertThat(quizProgress.toString()).isEqualTo(expectedString);
    }
}
