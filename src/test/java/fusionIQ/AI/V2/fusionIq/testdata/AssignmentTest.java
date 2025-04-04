package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentTest {

    private Assignment assignment;
    private Lesson lesson;
    private Course course;
    private User teacher;
    private User student;
    private LessonModule lessonModule;
    private List<Submission> submissions;

    @BeforeEach
    void setUp() {
        lesson = new Lesson();
        course = new Course();
        teacher = new User();
        student = new User();
        lessonModule = new LessonModule();
        submissions = new ArrayList<>();

        // Create a sample Assignment object
        assignment = new Assignment();
        assignment.setAssignmentTitle("Math Assignment");
        assignment.setAssignmentTopicName("Algebra");
        assignment.setAssignmentDescription("Solve algebraic equations");
        assignment.setMaxScore(100L);
        assignment.setStartDate(LocalDateTime.of(2023, 8, 10, 10, 0));
        assignment.setEndDate(LocalDateTime.of(2023, 8, 15, 18, 0));
        assignment.setReviewMeetDate(LocalDateTime.of(2023, 8, 17, 12, 0));
        assignment.setAssignmentAnswer("x = 10");
        assignment.setStudentIds("1,2,3");
        assignment.setLesson(lesson);
        assignment.setCourse(course);
        assignment.setTeacher(teacher);
        assignment.setStudent(student);
        assignment.setLessonModule(lessonModule);
        assignment.setSubmissions(submissions);
    }

    @Test
    void testAssignmentProperties() {
        assertEquals("Math Assignment", assignment.getAssignmentTitle());
        assertEquals("Algebra", assignment.getAssignmentTopicName());
        assertEquals("Solve algebraic equations", assignment.getAssignmentDescription());
        assertEquals(100L, assignment.getMaxScore());
        assertEquals(LocalDateTime.of(2023, 8, 10, 10, 0), assignment.getStartDate());
        assertEquals(LocalDateTime.of(2023, 8, 15, 18, 0), assignment.getEndDate());
        assertEquals(LocalDateTime.of(2023, 8, 17, 12, 0), assignment.getReviewMeetDate());
        assertEquals("x = 10", assignment.getAssignmentAnswer());
        assertEquals("1,2,3", assignment.getStudentIds());
    }

    @Test
    void testAssignmentRelationships() {
        assertNotNull(assignment.getLesson());
        assertNotNull(assignment.getCourse());
        assertNotNull(assignment.getTeacher());
        assertNotNull(assignment.getStudent());
        assertNotNull(assignment.getLessonModule());
    }

    @Test
    void testAssignmentSubmissions() {
        assertEquals(0, assignment.getSubmissions().size());

        Submission submission1 = new Submission();
        Submission submission2 = new Submission();
        assignment.getSubmissions().add(submission1);
        assignment.getSubmissions().add(submission2);

        assertEquals(2, assignment.getSubmissions().size());
    }

    @Test
    void testAssignmentCreationTime() {
        assertNotNull(assignment.getCreatedAt());
        assertNotNull(assignment.getUpdatedAt());
    }
}
