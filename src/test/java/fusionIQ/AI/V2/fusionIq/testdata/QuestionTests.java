//package fusionIQ.AI.V2.fusionIq.testdata;
//
//import fusionIQ.AI.V2.fusionIq.data.Question;
//import fusionIQ.AI.V2.fusionIq.data.Quiz;
//import fusionIQ.AI.V2.fusionIq.repository.QuestionRepo;
//import fusionIQ.AI.V2.fusionIq.repository.QuizRepo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class QuestionTests {
//
//    @Autowired
//    private QuestionRepo questionRepo;
//
//    @Autowired
//    private QuizRepo quizRepo;
//
//    private Quiz testQuiz;
//
//    @BeforeEach
//    public void setUp() {
//        Optional<Quiz> optionalQuiz = quizRepo.findById(50L); // Example quiz ID from your table
//        if (optionalQuiz.isPresent()) {
//            testQuiz = optionalQuiz.get();
//        } else {
//            testQuiz = new Quiz();
//            testQuiz.setQuizName("Sample Quiz");
//            testQuiz.setStartDate(LocalDateTime.now());
//            testQuiz.setEndDate(LocalDateTime.now().plusDays(1));
//            testQuiz = quizRepo.save(testQuiz);
//        }
//    }
//
//    @Test
//    public void testSaveQuestion() {
//        Question question = new Question();
//        question.setText("What is Java?");
//        question.setOptionA("A programming language");
//        question.setOptionB("A coffee brand");
//        question.setOptionC("A type of dance");
//        question.setOptionD("None of the above");
//        question.setCorrectAnswer("A programming language");
//
//        if (testQuiz.getCourse() != null || testQuiz.getLesson() != null || testQuiz.getLessonModule() != null) {
//            question.setQuiz(testQuiz);
//            Question savedQuestion = questionRepo.save(question);
//            assertThat(savedQuestion).isNotNull();
//            assertThat(savedQuestion.getId()).isNotNull();
//        } else {
//            // Handling for NULL course, lesson, or lesson module
//            System.out.println("Skipping testSaveQuestion due to missing course, lesson, or lesson module in quiz.");
//        }
//    }
//
//    @Test
//    public void testUpdateQuestion() {
//        Optional<Question> optionalQuestion = questionRepo.findById(1L); // Example question ID
//        if (optionalQuestion.isPresent() && testQuiz != null) {
//            Question question = optionalQuestion.get();
//            question.setText("Updated Question Text");
//
//            if (testQuiz.getCourse() != null || testQuiz.getLesson() != null || testQuiz.getLessonModule() != null) {
//                question.setQuiz(testQuiz);
//                Question updatedQuestion = questionRepo.save(question);
//                assertThat(updatedQuestion.getText()).isEqualTo("Updated Question Text");
//            } else {
//                // Handling for NULL course, lesson, or lesson module
//                System.out.println("Skipping testUpdateQuestion due to missing course, lesson, or lesson module in quiz.");
//            }
//        } else {
//            System.out.println("Skipping testUpdateQuestion as the question or quiz was not found.");
//        }
//    }
//
//    @Test
//    public void testDeleteQuestionById() {
//        Question question = new Question();
//        question.setText("Temporary question");
//        question.setOptionA("Option A");
//        question.setOptionB("Option B");
//        question.setOptionC("Option C");
//        question.setOptionD("Option D");
//        question.setCorrectAnswer("Option A");
//
//        if (testQuiz.getCourse() != null || testQuiz.getLesson() != null || testQuiz.getLessonModule() != null) {
//            question.setQuiz(testQuiz);
//            Question savedQuestion = questionRepo.save(question);
//            Long questionId = savedQuestion.getId();
//            assertThat(questionRepo.findById(questionId)).isPresent();
//
//            questionRepo.deleteById(questionId);
//            assertThat(questionRepo.findById(questionId)).isNotPresent();
//        } else {
//            // Handling for NULL course, lesson, or lesson module
//            System.out.println("Skipping testDeleteQuestionById due to missing course, lesson, or lesson module in quiz.");
//        }
//    }
//}
