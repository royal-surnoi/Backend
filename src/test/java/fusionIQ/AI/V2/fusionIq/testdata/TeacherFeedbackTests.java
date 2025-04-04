package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

class TeacherFeedbackTests {

    @Test
    void testDefaultConstructor() {
        TeacherFeedback feedback = new TeacherFeedback();

        // Verifying that the default constructor sets the createdAt field to the current time
        assertThat(feedback.getCreatedAt()).isNotNull();
        assertThat(feedback.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());

        // Verifying that other fields are initialized to their default values (null or empty)
        assertThat(feedback.getFeedback()).isNull();
        assertThat(feedback.getGrade()).isNull();
        assertThat(feedback.getTeacher()).isNull();
        assertThat(feedback.getStudent()).isNull();
        assertThat(feedback.getCourse()).isNull();
        assertThat(feedback.getQuiz()).isNull();
        assertThat(feedback.getAssignment()).isNull();
        assertThat(feedback.getProject()).isNull();
        assertThat(feedback.getLessonModule()).isNull();
        assertThat(feedback.getLesson()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        User teacher = new User();
        User student = new User();
        Course course = new Course();
        Quiz quiz = new Quiz();
        Assignment assignment = new Assignment();
        Project project = new Project();
        LessonModule lessonModule = new LessonModule();
        Lesson lesson = new Lesson();
        LocalDateTime now = LocalDateTime.now();
        TeacherFeedback feedback = new TeacherFeedback(
                1L, teacher, student, course, quiz, assignment, project, lessonModule, lesson, now, "Great job!", "A+");

        assertThat(feedback.getId()).isEqualTo(1L);
        assertThat(feedback.getTeacher()).isEqualTo(teacher);
        assertThat(feedback.getStudent()).isEqualTo(student);
        assertThat(feedback.getCourse()).isEqualTo(course);
        assertThat(feedback.getQuiz()).isEqualTo(quiz);
        assertThat(feedback.getAssignment()).isEqualTo(assignment);
        assertThat(feedback.getProject()).isEqualTo(project);
        assertThat(feedback.getLessonModule()).isEqualTo(lessonModule);
        assertThat(feedback.getLesson()).isEqualTo(lesson);
        assertThat(feedback.getCreatedAt()).isEqualTo(now);
        assertThat(feedback.getFeedback()).isEqualTo("Great job!");
        assertThat(feedback.getGrade()).isEqualTo("A+");
    }

    @Test
    void testSettersAndGetters() {
        TeacherFeedback feedback = new TeacherFeedback();
        User teacher = new User();
        User student = new User();
        Course course = new Course();
        Quiz quiz = new Quiz();
        Assignment assignment = new Assignment();
        Project project = new Project();
        LessonModule lessonModule = new LessonModule();
        Lesson lesson = new Lesson();
        LocalDateTime now = LocalDateTime.now();

        feedback.setId(1L);
        feedback.setTeacher(teacher);
        feedback.setStudent(student);
        feedback.setCourse(course);
        feedback.setQuiz(quiz);
        feedback.setAssignment(assignment);
        feedback.setProject(project);
        feedback.setLessonModule(lessonModule);
        feedback.setLesson(lesson);
        feedback.setCreatedAt(now);
        feedback.setFeedback("Good work.");
        feedback.setGrade("B");

        assertThat(feedback.getId()).isEqualTo(1L);
        assertThat(feedback.getTeacher()).isEqualTo(teacher);
        assertThat(feedback.getStudent()).isEqualTo(student);
        assertThat(feedback.getCourse()).isEqualTo(course);
        assertThat(feedback.getQuiz()).isEqualTo(quiz);
        assertThat(feedback.getAssignment()).isEqualTo(assignment);
        assertThat(feedback.getProject()).isEqualTo(project);
        assertThat(feedback.getLessonModule()).isEqualTo(lessonModule);
        assertThat(feedback.getLesson()).isEqualTo(lesson);
        assertThat(feedback.getCreatedAt()).isEqualTo(now);
        assertThat(feedback.getFeedback()).isEqualTo("Good work.");
        assertThat(feedback.getGrade()).isEqualTo("B");
    }

    @Test
    void testToString() {
        User teacher = new User();
        User student = new User();
        Course course = new Course();
        Quiz quiz = new Quiz();
        Assignment assignment = new Assignment();
        Project project = new Project();
        LessonModule lessonModule = new LessonModule();
        Lesson lesson = new Lesson();
        LocalDateTime now = LocalDateTime.now();
        TeacherFeedback feedback = new TeacherFeedback(
                1L, teacher, student, course, quiz, assignment, project, lessonModule, lesson, now, "Excellent effort!", "A");

        String expectedToString = "TeacherFeedback{" +
                "id=" + feedback.getId() +
                ", teacher=" + feedback.getTeacher() +
                ", student=" + feedback.getStudent() +
                ", course=" + feedback.getCourse() +
                ", quiz=" + feedback.getQuiz() +
                ", assignment=" + feedback.getAssignment() +
                ", project=" + feedback.getProject() +
                ", lessonModule=" + feedback.getLessonModule() +
                ", lesson=" + feedback.getLesson() +
                ", createdAt=" + feedback.getCreatedAt() +
                ", feedback='" + feedback.getFeedback() + '\'' +
                ", grade='" + feedback.getGrade() + '\'' +
                '}';

        assertThat(feedback.toString()).isEqualTo(expectedToString);
    }
}
