package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.AIAssignment;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AIAssignmentTests {

    private AIAssignment aiAssignment;
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

        aiAssignment = new AIAssignment();
    }

    @Test
    void testDefaultConstructor() {
        assertThat(aiAssignment.getCreatedAt()).isNotNull();
        assertThat(aiAssignment.getAIAssignmentQuestion()).isNull();
        assertThat(aiAssignment.getAIAssignmentAnswer()).isNull();
        assertThat(aiAssignment.getAIAssignmentUserAnswer()).isNull();
        assertThat(aiAssignment.getAIAssignmentDescription()).isNull();
        assertThat(aiAssignment.getLesson()).isNull();
        assertThat(aiAssignment.getCourse()).isNull();
        assertThat(aiAssignment.getUser()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        LocalDateTime now = LocalDateTime.now();
        AIAssignment aiAssignment = new AIAssignment(1L, "Sample Question", "Sample Answer", "User Answer", "Description", lesson, course, user, now);

        assertThat(aiAssignment.getId()).isEqualTo(1L);
        assertThat(aiAssignment.getAIAssignmentQuestion()).isEqualTo("Sample Question");
        assertThat(aiAssignment.getAIAssignmentAnswer()).isEqualTo("Sample Answer");
        assertThat(aiAssignment.getAIAssignmentUserAnswer()).isEqualTo("User Answer");
        assertThat(aiAssignment.getAIAssignmentDescription()).isEqualTo("Description");
        assertThat(aiAssignment.getLesson()).isEqualTo(lesson);
        assertThat(aiAssignment.getCourse()).isEqualTo(course);
        assertThat(aiAssignment.getUser()).isEqualTo(user);
        assertThat(aiAssignment.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        aiAssignment.setId(2L);
        aiAssignment.setAIAssignmentQuestion("New Question");
        aiAssignment.setAIAssignmentAnswer("New Answer");
        aiAssignment.setAIAssignmentUserAnswer("New User Answer");
        aiAssignment.setAIAssignmentDescription("New Description");
        aiAssignment.setLesson(lesson);
        aiAssignment.setCourse(course);
        aiAssignment.setUser(user);
        aiAssignment.setCreatedAt(now);

        assertThat(aiAssignment.getId()).isEqualTo(2L);
        assertThat(aiAssignment.getAIAssignmentQuestion()).isEqualTo("New Question");
        assertThat(aiAssignment.getAIAssignmentAnswer()).isEqualTo("New Answer");
        assertThat(aiAssignment.getAIAssignmentUserAnswer()).isEqualTo("New User Answer");
        assertThat(aiAssignment.getAIAssignmentDescription()).isEqualTo("New Description");
        assertThat(aiAssignment.getLesson()).isEqualTo(lesson);
        assertThat(aiAssignment.getCourse()).isEqualTo(course);
        assertThat(aiAssignment.getUser()).isEqualTo(user);
        assertThat(aiAssignment.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testToString() {
        aiAssignment.setId(1L);
        aiAssignment.setAIAssignmentQuestion("Sample Question");
        aiAssignment.setAIAssignmentAnswer("Sample Answer");
        aiAssignment.setAIAssignmentUserAnswer("User Answer");
        aiAssignment.setAIAssignmentDescription("Description");
        aiAssignment.setLesson(lesson);
        aiAssignment.setCourse(course);
        aiAssignment.setUser(user);

        String expected = "AIAssignment{id=1, AIAssignmentQuestion='Sample Question', AIAssignmentAnswer='Sample Answer', AIAssignmentUserAnswer='User Answer', AIAssignmentDescription='Description', lessonId=1, courseId=1, userId=1, createdAt=" + aiAssignment.getCreatedAt() + "}";
        assertThat(aiAssignment.toString()).isEqualTo(expected);
    }
}
