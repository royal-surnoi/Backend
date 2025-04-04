package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.JobQuiz;
import fusionIQ.AI.V2.fusionIq.data.Question;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.JobQuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.*;

class JobQuizServiceTest {
    @Mock
    private JobQuizRepository jobQuizRepository;
    @Mock
    private RecruiterRepository recruiterRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private ShortlistedCandidatesRepository shortlistedCandidatesRepository;
    @Mock
    private QuestionRepo questionRepo;
    @Mock
    private AnswerRepo answerRepo;

    private JobQuizService jobQuizService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobQuizService = new JobQuizService(jobQuizRepository, recruiterRepository, answerRepo,
                jobRepository, shortlistedCandidatesRepository, questionRepo);
    }

    @Test
    void testCreateJobQuizForUsers() {
        // Arrange
        Long recruiterId = 1L;
        Long jobId = 2L;
        String userIds = "1,2,3";
        String quizName = "Test Quiz";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);

        JobQuiz expectedQuiz = new JobQuiz();
        expectedQuiz.setId(1L);
        when(jobQuizRepository.save(any(JobQuiz.class))).thenReturn(expectedQuiz);

        // Act
        JobQuiz result = jobQuizService.createJobQuizForUsers(
                recruiterId, jobId, userIds, quizName, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(jobQuizRepository).save(any(JobQuiz.class));
    }

    @Test
    void testAddQuestionsToJobQuiz() {
        // Arrange
        Long jobQuizId = 1L;
        Long jobId = 2L;
        JobQuiz jobQuiz = new JobQuiz();
        jobQuiz.setId(jobQuizId);

        List<Question> questions = new ArrayList<>();
        Question question = new Question();
        question.setText("Test question");
        questions.add(question);

        when(jobQuizRepository.findById(jobQuizId)).thenReturn(Optional.of(jobQuiz));
        when(questionRepo.saveAll(any())).thenReturn(questions);

        // Act
        List<Question> result = jobQuizService.addQuestionsToJobQuiz(jobQuizId, jobId, questions);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test question", result.get(0).getText());
        verify(questionRepo).saveAll(any());
    }

    @Test
    void testGetJobQuizzesByUserAndJob() {
        // Arrange
        Long jobId = 1L;
        Long userId = 2L;
        List<JobQuiz> expectedQuizzes = new ArrayList<>();
        JobQuiz quiz = new JobQuiz();
        quiz.setId(1L);
        expectedQuizzes.add(quiz);

        when(jobQuizRepository.findByJobIdAndUserId(jobId, userId)).thenReturn(expectedQuizzes);

        // Act
        List<JobQuiz> result = jobQuizService.getJobQuizzesByUserAndJob(jobId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(jobQuizRepository).findByJobIdAndUserId(jobId, userId);
    }
}
