//package fusionIQ.AI.V2.fusionIq.testdata;
//
//import fusionIQ.AI.V2.fusionIq.data.*;
//import org.junit.jupiter.api.Test;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//class SubmitAssignmentTests {
//
//    @Test
//    void testDefaultConstructor() {
//        SubmitAssignment submitAssignment = new SubmitAssignment();
//
//        // Verifying that the default constructor initializes fields correctly
//        assertThat(submitAssignment.getId()).isNull();
//        assertThat(submitAssignment.getSubmitAssignment(null)).isNull();
//        assertThat(submitAssignment.getSubmittedAt()).isNotNull(); // Should be initialized to current time
//        assertThat(submitAssignment.getUserAssignmentAnswer()).isNull();
//        assertThat(submitAssignment.isSubmitted()).isFalse(); // Default boolean value
//        assertThat(submitAssignment.getUser()).isNull();
//        assertThat(submitAssignment.getLesson()).isNull();
//        assertThat(submitAssignment.getAssignment()).isNull();
//        assertThat(submitAssignment.getLessonModule()).isNull();
//        assertThat(submitAssignment.getCourse()).isNull();
//        assertThat(submitAssignment.getStudent()).isNull();
//    }
//
//    @Test
//    void testParameterizedConstructor() {
//        byte[] assignmentContent = "Sample content".getBytes();
//        LocalDateTime submittedAt = LocalDateTime.now();
//        User user = new User();
//        Lesson lesson = new Lesson();
//        Assignment assignment = new Assignment();
//        LessonModule lessonModule = new LessonModule();
//        Course course = new Course();
//        User student = new User();
//
//        SubmitAssignment submitAssignment = new SubmitAssignment(1L, assignmentContent, submittedAt, "Answer", true, user, lesson, assignment, lessonModule, course, student);
//
//        // Verifying that the parameterized constructor initializes fields correctly
//        assertThat(submitAssignment.getId()).isEqualTo(1L);
//        assertThat(submitAssignment.getSubmitAssignment(null)).isEqualTo(assignmentContent);
//        assertThat(submitAssignment.getSubmittedAt()).isEqualTo(submittedAt);
//        assertThat(submitAssignment.getUserAssignmentAnswer()).isEqualTo("Answer");
//        assertThat(submitAssignment.isSubmitted()).isTrue();
//        assertThat(submitAssignment.getUser()).isEqualTo(user);
//        assertThat(submitAssignment.getLesson()).isEqualTo(lesson);
//        assertThat(submitAssignment.getAssignment()).isEqualTo(assignment);
//        assertThat(submitAssignment.getLessonModule()).isEqualTo(lessonModule);
//        assertThat(submitAssignment.getCourse()).isEqualTo(course);
//        assertThat(submitAssignment.getStudent()).isEqualTo(student);
//    }
//
//    @Test
//    void testSettersAndGetters() {
//        SubmitAssignment submitAssignment = new SubmitAssignment();
//        byte[] assignmentContent = "New content".getBytes();
//        LocalDateTime submittedAt = LocalDateTime.now();
//        User user = new User();
//        Lesson lesson = new Lesson();
//        Assignment assignment = new Assignment();
//        LessonModule lessonModule = new LessonModule();
//        Course course = new Course();
//        User student = new User();
//
//        submitAssignment.setId(1L);
//        submitAssignment.setSubmitAssignment(assignmentContent);
//        submitAssignment.setSubmittedAt(submittedAt);
//        submitAssignment.setUserAssignmentAnswer("Updated Answer");
//        submitAssignment.setSubmitted(true);
//        submitAssignment.setUser(user);
//        submitAssignment.setLesson(lesson);
//        submitAssignment.setAssignment(assignment);
//        submitAssignment.setLessonModule(lessonModule);
//        submitAssignment.setCourse(course);
//        submitAssignment.setStudent(student);
//
//        // Verifying the setters and getters
//        assertThat(submitAssignment.getId()).isEqualTo(1L);
//        assertThat(submitAssignment.getSubmitAssignment(null)).isEqualTo(assignmentContent);
//        assertThat(submitAssignment.getSubmittedAt()).isEqualTo(submittedAt);
//        assertThat(submitAssignment.getUserAssignmentAnswer()).isEqualTo("Updated Answer");
//        assertThat(submitAssignment.isSubmitted()).isTrue();
//        assertThat(submitAssignment.getUser()).isEqualTo(user);
//        assertThat(submitAssignment.getLesson()).isEqualTo(lesson);
//        assertThat(submitAssignment.getAssignment()).isEqualTo(assignment);
//        assertThat(submitAssignment.getLessonModule()).isEqualTo(lessonModule);
//        assertThat(submitAssignment.getCourse()).isEqualTo(course);
//        assertThat(submitAssignment.getStudent()).isEqualTo(student);
//    }
//
//    @Test
//    void testToString() {
//        byte[] assignmentContent = "ToString content".getBytes();
//        LocalDateTime submittedAt = LocalDateTime.now();
//        User user = new User();
//        Lesson lesson = new Lesson();
//        Assignment assignment = new Assignment();
//        LessonModule lessonModule = new LessonModule();
//        Course course = new Course();
//        User student = new User();
//
//        SubmitAssignment submitAssignment = new SubmitAssignment(1L, assignmentContent, submittedAt, "ToString Answer", true, user, lesson, assignment, lessonModule, course, student);
//
//        String expectedToString = "SubmitAssignment{" +
//                "id=" + submitAssignment.getId() +
//                ", submitAssignment=" + Arrays.toString(assignmentContent) +
//                ", submittedAt=" + submitAssignment.getSubmittedAt() +
//                ", userAssignmentAnswer='" + submitAssignment.getUserAssignmentAnswer() + '\'' +
//                ", isSubmitted=" + submitAssignment.isSubmitted() +
//                ", user=" + submitAssignment.getUser() +
//                ", lesson=" + submitAssignment.getLesson() +
//                ", assignment=" + submitAssignment.getAssignment() +
//                ", lessonModule=" + submitAssignment.getLessonModule() +
//                ", course=" + submitAssignment.getCourse() +
//                ", student=" + submitAssignment.getStudent() +
//                '}';
//
//        // Verifying the toString method
//        assertThat(submitAssignment.toString()).isEqualTo(expectedToString);
//    }
//}
