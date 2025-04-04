package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class SubmissionTest {

    @Test
    void testDefaultConstructor() {
        // Test default constructor
        Submission submission = new Submission();
        assertThat(submission).isNotNull();
        assertThat(submission.getId()).isZero();
        assertThat(submission.getActivity()).isNull();
        assertThat(submission.getUser()).isNull();
        assertThat(submission.getProject()).isNull();
        assertThat(submission.getCourse()).isNull();
        assertThat(submission.getAssignment()).isNull();
        assertThat(submission.getSubmissionDate()).isNotNull();
        assertThat(submission.getSubmissionGrade()).isZero();
        assertThat(submission.getSubmissionFeedback()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        // Test parameterized constructor with sample data
        Activity activity = new Activity();
        activity.setId(1L);

        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setId(1L);

        Course course = new Course();
        course.setId(1L);

        Assignment assignment = new Assignment();
        assignment.setId(1L);

        LocalDateTime now = LocalDateTime.now();
        Submission submission = new Submission(
                1L, activity, user, project, course, assignment, now, 85.5, "Great work!");

        assertThat(submission).isNotNull();
        assertThat(submission.getId()).isEqualTo(1L);
        assertThat(submission.getActivity()).isEqualTo(activity);
        assertThat(submission.getUser()).isEqualTo(user);
        assertThat(submission.getProject()).isEqualTo(project);
        assertThat(submission.getCourse()).isEqualTo(course);
        assertThat(submission.getAssignment()).isEqualTo(assignment);
        assertThat(submission.getSubmissionDate()).isEqualTo(now);
        assertThat(submission.getSubmissionGrade()).isEqualTo(85.5);
        assertThat(submission.getSubmissionFeedback()).isEqualTo("Great work!");
    }

    @Test
    void testGettersAndSetters() {
        // Test getters and setters
        Activity activity = new Activity();
        activity.setId(1L);

        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setId(1L);

        Course course = new Course();
        course.setId(1L);

        Assignment assignment = new Assignment();
        assignment.setId(1L);

        Submission submission = new Submission();
        submission.setId(1L);
        submission.setActivity(activity);
        submission.setUser(user);
        submission.setProject(project);
        submission.setCourse(course);
        submission.setAssignment(assignment);
        submission.setSubmissionDate(LocalDateTime.now());
        submission.setSubmissionGrade(90.0);
        submission.setSubmissionFeedback("Excellent work!");

        assertThat(submission.getId()).isEqualTo(1L);
        assertThat(submission.getActivity()).isEqualTo(activity);
        assertThat(submission.getUser()).isEqualTo(user);
        assertThat(submission.getProject()).isEqualTo(project);
        assertThat(submission.getCourse()).isEqualTo(course);
        assertThat(submission.getAssignment()).isEqualTo(assignment);
        assertThat(submission.getSubmissionDate()).isNotNull();
        assertThat(submission.getSubmissionGrade()).isEqualTo(90.0);
        assertThat(submission.getSubmissionFeedback()).isEqualTo("Excellent work!");
    }

    @Test
    void testToString() {
        // Test toString method
        Activity activity = new Activity();
        activity.setId(1L);

        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setId(1L);

        Course course = new Course();
        course.setId(1L);

        Assignment assignment = new Assignment();
        assignment.setId(1L);

        LocalDateTime now = LocalDateTime.now();
        Submission submission = new Submission(
                1L, activity, user, project, course, assignment, now, 85.5, "Great work!");

        String expectedToString = "Submission{" +
                "id=" + submission.getId() +
                ", activity=" + submission.getActivity() +
                ", user=" + submission.getUser() +
                ", project=" + submission.getProject() +
                ", course=" + submission.getCourse() +
                ", assignment=" + submission.getAssignment() +
                ", submissionDate=" + submission.getSubmissionDate() +
                ", submissionGrade=" + submission.getSubmissionGrade() +
                ", submissionFeedback='" + submission.getSubmissionFeedback() + '\'' +
                '}';

        assertThat(submission.toString()).isEqualTo(expectedToString);
    }
}
