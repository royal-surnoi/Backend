package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class QuizServiceTest {

    @Mock
    private QuizRepo quizRepo;

    @Mock
    private QuestionRepo questionRepo;

    @Mock
    private AnswerRepo answerRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private LessonRepo lessonRepo;

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private LessonModuleRepo lessonModuleRepo;

    @Mock
    private QuizProgressRepo quizProgressRepo;

    @InjectMocks
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllQuizzes_Success() {
        when(quizRepo.findAll()).thenReturn(Collections.singletonList(new Quiz()));

        List<Quiz> quizzes = quizService.getAllQuizzes();

        assertNotNull(quizzes);
        assertEquals(1, quizzes.size());
    }

    @Test
    void testGetQuizById_Found() {
        Quiz quiz = new Quiz();
        when(quizRepo.findById(anyLong())).thenReturn(Optional.of(quiz));

        Quiz foundQuiz = quizService.getQuizById(1L);

        assertNotNull(foundQuiz);
    }

    @Test
    void testGetQuizById_NotFound() {
        when(quizRepo.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            quizService.getQuizById(1L);
        });

        assertEquals("Quiz not found", exception.getMessage());
    }



    @Test
    void testCalculateCorrectAnswerPercentage() {
        Answer correctAnswer = new Answer();
        correctAnswer.setCorrect(true);
        Answer wrongAnswer = new Answer();
        wrongAnswer.setCorrect(false);

        when(answerRepo.findByQuizIdAndUserId(anyLong(), anyLong())).thenReturn(List.of(correctAnswer, wrongAnswer));

        double percentage = quizService.calculateCorrectAnswerPercentage(1L, 1L);

        assertEquals(50.0, percentage);
    }

    @Test
    void testAddQuestionsToQuiz_Success() {
        Quiz quiz = new Quiz();
        when(quizRepo.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(questionRepo.save(any(Question.class))).thenReturn(new Question());

        List<Question> addedQuestions = quizService.addQuestionsToQuiz(1L, Collections.singletonList(new Question()));

        assertNotNull(addedQuestions);
        assertEquals(1, addedQuestions.size());
    }



    @Test
    void testGetQuizzesByLessonId_Success() {
        when(quizRepo.findByLessonId(anyLong())).thenReturn(Collections.singletonList(new Quiz()));

        List<Quiz> quizzes = quizService.getQuizzesByLessonId(1L);

        assertNotNull(quizzes);
        assertEquals(1, quizzes.size());
    }

    @Test
    void testGetQuizzesByCourseId_Success() {
        when(quizRepo.findByCourseIdWithLesson(anyLong())).thenReturn(Collections.singletonList(new Quiz()));

        List<Quiz> quizzes = quizService.getQuizzesByCourseId(1L);

        assertNotNull(quizzes);
        assertEquals(1, quizzes.size());
    }



    @Test
    void testGetSubmittedQuizRatio_Success() {
        when(quizRepo.countByCourseId(anyLong())).thenReturn(10);
        when(answerRepo.countDistinctByUserIdAndCourseId(anyLong(), anyLong())).thenReturn(7);

        String ratio = quizService.getSubmittedQuizRatio(1L, 1L);

        assertEquals("7/10", ratio);
    }
}

