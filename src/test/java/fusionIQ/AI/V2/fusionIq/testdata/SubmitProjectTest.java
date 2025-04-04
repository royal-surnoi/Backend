//package fusionIQ.AI.V2.fusionIq.testdata;
//
//import fusionIQ.AI.V2.fusionIq.data.Course;
//import fusionIQ.AI.V2.fusionIq.data.Project;
//import fusionIQ.AI.V2.fusionIq.data.SubmitProject;
//import fusionIQ.AI.V2.fusionIq.data.User;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class SubmitProjectTest {
//
//    @Test
//    void testDefaultConstructor() {
//        // Test default constructor
//        SubmitProject submitProject = new SubmitProject();
//        assertThat(submitProject).isNotNull();
//        assertThat(submitProject.getId()).isNull();
//        assertThat(submitProject.getSubmitProject()).isNull();
//        assertThat(submitProject.getUserAnswer()).isNull();
//        assertThat(submitProject.getSubmittedAt()).isNotNull();
//        assertThat(submitProject.isSubmitted()).isFalse();
//        assertThat(submitProject.getUser()).isNull();
//        assertThat(submitProject.getCourse()).isNull();
//        assertThat(submitProject.getProject()).isNull();
//        assertThat(submitProject.getStudent()).isNull();
//    }
//
//    @Test
//    void testParameterizedConstructor() {
//        // Test parameterized constructor with sample data
//        byte[] projectData = {1, 2, 3, 4, 5};
//        LocalDateTime now = LocalDateTime.now();
//
//        User user = new User();
//        user.setId(1L);
//
//        Course course = new Course();
//        course.setId(1L);
//
//        Project project = new Project();
//        project.setId(1L);
//
//        User student = new User();
//        student.setId(1L);
//
//        SubmitProject submitProject = new SubmitProject(
//                1L, projectData, "Sample Answer", now, true, user, course, project, student);
//
//        assertThat(submitProject).isNotNull();
//        assertThat(submitProject.getId()).isEqualTo(1L);
//        assertThat(submitProject.getSubmitProject()).isEqualTo(projectData);
//        assertThat(submitProject.getUserAnswer()).isEqualTo("Sample Answer");
//        assertThat(submitProject.getSubmittedAt()).isEqualTo(now);
//        assertThat(submitProject.isSubmitted()).isTrue();
//        assertThat(submitProject.getUser()).isEqualTo(user);
//        assertThat(submitProject.getCourse()).isEqualTo(course);
//        assertThat(submitProject.getProject()).isEqualTo(project);
//        assertThat(submitProject.getStudent()).isEqualTo(student);
//    }
//
//    @Test
//    void testGettersAndSetters() {
//        // Test getters and setters
//        SubmitProject submitProject = new SubmitProject();
//        byte[] projectData = {6, 7, 8, 9, 10};
//        LocalDateTime now = LocalDateTime.now().minusDays(1);
//
//        User user = new User();
//        user.setId(2L);
//
//        Course course = new Course();
//        course.setId(2L);
//
//        Project project = new Project();
//        project.setId(2L);
//
//        User student = new User();
//        student.setId(2L);
//
//        submitProject.setId(2L);
//        submitProject.setSubmitProject(projectData);
//        submitProject.setUserAnswer("Updated Answer");
//        submitProject.setSubmittedAt(now);
//        submitProject.setSubmitted(true);
//        submitProject.setUser(user);
//        submitProject.setCourse(course);
//        submitProject.setProject(project);
//        submitProject.setStudent(student);
//
//        assertThat(submitProject.getId()).isEqualTo(2L);
//        assertThat(submitProject.getSubmitProject()).isEqualTo(projectData);
//        assertThat(submitProject.getUserAnswer()).isEqualTo("Updated Answer");
//        assertThat(submitProject.getSubmittedAt()).isEqualTo(now);
//        assertThat(submitProject.isSubmitted()).isTrue();
//        assertThat(submitProject.getUser()).isEqualTo(user);
//        assertThat(submitProject.getCourse()).isEqualTo(course);
//        assertThat(submitProject.getProject()).isEqualTo(project);
//        assertThat(submitProject.getStudent()).isEqualTo(student);
//    }
//
//    @Test
//    void testToString() {
//        // Test toString method
//        byte[] projectData = {1, 2, 3, 4, 5};
//        LocalDateTime now = LocalDateTime.now();
//
//        User user = new User();
//        user.setId(1L);
//
//        Course course = new Course();
//        course.setId(1L);
//
//        Project project = new Project();
//        project.setId(1L);
//
//        User student = new User();
//        student.setId(1L);
//
//        SubmitProject submitProject = new SubmitProject(
//                1L, projectData, "Sample Answer", now, true, user, course, project, student);
//
//        String expectedToString = "SubmitProject{" +
//                "id=" + submitProject.getId() +
//                ", submitProject=" + Arrays.toString(submitProject.getSubmitProject()) +
//                ", userAnswer='" + submitProject.getUserAnswer() + '\'' +
//                ", submittedAt=" + submitProject.getSubmittedAt() +
//                ", isSubmitted=" + submitProject.isSubmitted() +
//                ", user=" + submitProject.getUser() +
//                ", course=" + submitProject.getCourse() +
//                ", project=" + submitProject.getProject() +
//                ", student=" + submitProject.getStudent() +
//                '}';
//
//        assertThat(submitProject.toString()).isEqualTo(expectedToString);
//    }
//}
