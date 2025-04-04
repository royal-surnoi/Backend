//package fusionIQ.AI.V2.fusionIq.testdata;
//
//import fusionIQ.AI.V2.fusionIq.data.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.time.LocalDateTime;
//
//class QuizTest {
//
//    private Quiz quiz;
//    private Course course;
//    private Lesson lesson;
//    private User user;
//    private User teacher;
//    private User student;
//    private LessonModule lessonModule;
//
//    @BeforeEach
//    void setUp() {
//        course = new Course();
//        lesson = new Lesson();
//        user = new User();
//        teacher = new User();
//        student = new User();
//        lessonModule = new LessonModule();
//
//        quiz = new Quiz(1L, "Sample Quiz", course, lesson, user, teacher, student, lessonModule,
//                LocalDateTime.of(2023, 8, 15, 10, 0),
//                LocalDateTime.of(2023, 8, 16, 10, 0),
//                LocalDateTime.of(2023, 8, 20, 10, 0),
//                LocalDateTime.of(2023, 8, 25, 10, 0));
//    }
//
//    @Test
//    void testConstructor() {
//        assertEquals(1L, quiz.getId());
//        assertEquals("Sample Quiz", quiz.getQuizName());
//        assertEquals(course, quiz.getCourse());
//        assertEquals(lesson, quiz.getLesson());
//        assertEquals(user, quiz.getUser());
//        assertEquals(teacher, quiz.getTeacher());
//        assertEquals(student, quiz.getStudent());
//        assertEquals(lessonModule, quiz.getLessonModule());
//        assertEquals(LocalDateTime.of(2023, 8, 15, 10, 0), quiz.getCreatedAt());
//        assertEquals(LocalDateTime.of(2023, 8, 16, 10, 0), quiz.getUpdatedAt());
//        assertEquals(LocalDateTime.of(2023, 8, 20, 10, 0), quiz.getStartDate());
//        assertEquals(LocalDateTime.of(2023, 8, 25, 10, 0), quiz.getEndDate());
//    }
//
//    @Test
//    void testSettersAndGetters() {
//        // Test setters and getters for quizName
//        quiz.setQuizName("Updated Quiz Name");
//        assertEquals("Updated Quiz Name", quiz.getQuizName());
//
//        // Test setters and getters for startDate and endDate
//        LocalDateTime newStartDate = LocalDateTime.of(2023, 9, 1, 12, 0);
//        quiz.setStartDate(newStartDate);
//        assertEquals(newStartDate, quiz.getStartDate());
//
//        LocalDateTime newEndDate = LocalDateTime.of(2023, 9, 2, 12, 0);
//        quiz.setEndDate(newEndDate);
//        assertEquals(newEndDate, quiz.getEndDate());
//    }
//
//    @Test
//    void testDefaultConstructor() {
//        Quiz defaultQuiz = new Quiz();
//        assertNotNull(defaultQuiz.getCreatedAt());
//        assertNotNull(defaultQuiz.getUpdatedAt());
//    }
//
//    @Test
//    void testToString() {
//        String expected = "Quiz{id=1, quizName='Sample Quiz', course=" + course +
//                ", lesson=" + lesson + ", user=" + user +
//                ", teacher=" + teacher + ", student=" + student +
//                ", lessonModule=" + lessonModule + ", createdAt=" +
//                "2023-08-15T10:00, updatedAt=2023-08-16T10:00, startDate=" +
//                "2023-08-20T10:00, endDate=2023-08-25T10:00}";
//        assertEquals(expected, quiz.toString());
//    }
//}
