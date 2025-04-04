package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.AnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AnswerServiceTest {

    @Mock
    private AnswerRepo answerRepo;

    @Mock
    private QuestionRepo questionRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private JobQuizRepository jobQuizRepository;

    @Mock
    private JobQuizProgressRepo jobQuizProgressRepo;

    @InjectMocks
    private AnswerService answerService;

    private JobQuiz mockJobQuiz;
    private User mockUser;
    private Question mockQuestion;
    private Recruiter mockRecruiter;
    private JobAdmin mockJobAdmin;
    private Job mockJob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock objects
        mockJobQuiz = new JobQuiz();
        mockJobQuiz.setId(1L);
        mockJobQuiz.setQuizName("Test Quiz");
        mockJobQuiz.setJobId(1L);

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("Test User");
        mockUser.setEmail("test@example.com");


        mockQuestion = new Question();
        mockQuestion.setId(1L);
        mockQuestion.setText("Test Question");
        mockQuestion.setCorrectAnswer("Correct Answer");

        mockRecruiter = new Recruiter();
        mockRecruiter.setId(1L);
        mockRecruiter.setRecruiterName("Test Recruiter");
        mockRecruiter.setRecruiterEmail("recruiter@example.com");

        mockJobAdmin = new JobAdmin();
        mockJobAdmin.setId(1L);
        mockJobAdmin.setJobAdminName("Test Admin");
        mockJobAdmin.setJobAdminEmail("admin@example.com");

        mockJob = new Job();
        mockJob.setId(1L);
        mockJob.setJobTitle("Test Job");

        mockJobQuiz.setRecruiter(mockRecruiter);
        mockJobQuiz.setJob(mockJob);
        mockJobQuiz.setQuestions(Collections.singletonList(mockQuestion));
    }

    @Test
    void testSaveAnswers_Success() {
        // Arrange
        when(jobQuizRepository.findById(1L)).thenReturn(Optional.of(mockJobQuiz));
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(questionRepo.findById(1L)).thenReturn(Optional.of(mockQuestion));

        List<Map<String, Object>> answers = new ArrayList<>();
        Map<String, Object> answer = new HashMap<>();
        answer.put("questionId", "1");
        answer.put("selectedAnswer", "Correct Answer");
        answers.add(answer);

        // Act
        Map<String, Object> result = answerService.saveAnswers(1L, 1L, answers);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.get("jobQuizId"));
        assertEquals(1L, result.get("userId"));
        verify(answerRepo, times(1)).save(any(Answer.class));
    }

    @Test
    void testCalculateCorrectAnswerPercentage() {
        // Arrange
        when(answerRepo.countTotalQuestionsByJobQuizId(1L)).thenReturn(10L);
        when(answerRepo.countCorrectAnswersByJobQuizId(1L)).thenReturn(7L);
        when(answerRepo.countAttemptedAnswersByJobQuizId(1L)).thenReturn(9L);

        // Act
        double percentage = answerService.calculateCorrectAnswerPercentage(1L);

        // Assert
        assertEquals(70.0, percentage, 0.01);
    }

    @Test
    void testSubmitAnswersAndCalculatePercentage_WithRecruiter() {
        // Arrange
        when(jobQuizRepository.findById(1L)).thenReturn(Optional.of(mockJobQuiz));
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

        List<Map<String, Object>> answers = new ArrayList<>();
        Map<String, Object> answer = new HashMap<>();
        answer.put("questionId", 1);
        answer.put("selectedAnswer", "Correct Answer");
        answers.add(answer);

        // Act
        Map<String, Object> result = answerService.submitAnswersAndCalculatePercentage(1L, 1L, answers);

        // Assert
        assertNotNull(result);
        assertEquals(mockRecruiter.getId(), result.get("recruiterId"));
        assertEquals(mockRecruiter.getRecruiterName(), result.get("recruiterName"));
        assertEquals(100.0, result.get("correctAnswerPercentage"));
        verify(jobQuizProgressRepo, times(1)).save(any(JobQuizProgress.class));
    }

    @Test
    void testSubmitAnswersAndCalculatePercentage_WithAdmin() {
        // Arrange
        mockJobQuiz.setRecruiter(null);
        mockJobQuiz.setJobAdmin(mockJobAdmin);
        when(jobQuizRepository.findById(1L)).thenReturn(Optional.of(mockJobQuiz));
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

        List<Map<String, Object>> answers = new ArrayList<>();
        Map<String, Object> answer = new HashMap<>();
        answer.put("questionId", 1);
        answer.put("selectedAnswer", "Correct Answer");
        answers.add(answer);

        // Act
        Map<String, Object> result = answerService.submitAnswersAndCalculatePercentage(1L, 1L, answers);

        // Assert
        assertNotNull(result);
        assertEquals(mockJobAdmin.getId(), result.get("adminId"));
        assertEquals(mockJobAdmin.getJobAdminName(), result.get("adminName"));
        assertEquals(100.0, result.get("correctAnswerPercentage"));
        verify(jobQuizProgressRepo, times(1)).save(any(JobQuizProgress.class));
    }

    @Test
    void testGetQuizProgressDetails() {
        // Arrange
        JobQuizProgress mockProgress = new JobQuizProgress();
        mockProgress.setJobQuiz(mockJobQuiz);
        mockProgress.setUserId(1L);
        mockProgress.setUserName("Test User");
        mockProgress.setScorePercentage(85.0);

        when(jobQuizProgressRepo.findByJobQuizIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(mockProgress));
        when(answerRepo.findByJobQuizIdAndUserId(1L, 1L))
                .thenReturn(Collections.emptyList());

        // Act
        Map<String, Object> result = answerService.getQuizProgressDetails(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.get("userId"));
        assertEquals("Test User", result.get("userName"));
        assertEquals(85.0, result.get("scorePercentage"));
    }

    @Test
    void testGetProgressByJobAndRecruiterId() {
        // Arrange
        JobQuizProgress mockProgress = new JobQuizProgress();
        mockProgress.setJobQuiz(mockJobQuiz);
        mockProgress.setUserId(1L);
        mockProgress.setCompletedAt(LocalDateTime.now());

        when(jobQuizProgressRepo.findByJobIdAndRecruiterId(1L, 1L))
                .thenReturn(Collections.singletonList(mockProgress));
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(answerRepo.findByJobQuizProgress(mockProgress))
                .thenReturn(Collections.emptyList());

        // Act
        List<Map<String, Object>> result = answerService.getProgressByJobAndRecruiterId(1L, 1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).get("userId"));
        assertEquals(mockUser.getUserImage(), result.get(0).get("userImage"));
    }

    @Test
    void testGetProgressByJobAndAdminId() {
        // Arrange
        JobQuizProgress mockProgress = new JobQuizProgress();
        mockProgress.setJobQuiz(mockJobQuiz);
        mockProgress.setUserId(1L);
        mockProgress.setCompletedAt(LocalDateTime.now());

        when(jobQuizProgressRepo.findByJobIdAndAdminId(1L, 1L))
                .thenReturn(Collections.singletonList(mockProgress));
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(answerRepo.findByJobQuizProgress(mockProgress))
                .thenReturn(Collections.emptyList());

        // Act
        List<Map<String, Object>> result = answerService.getProgressByJobAndAdminId(1L, 1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).get("userId"));
        assertEquals(mockUser.getUserImage(), result.get(0).get("userImage"));
    }

    @Test
    void testSaveAnswers_JobQuizNotFound() {
        // Arrange
        when(jobQuizRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                answerService.saveAnswers(1L, 1L, Collections.emptyList()));
    }

    @Test
    void testSaveAnswers_UserNotFound() {
        // Arrange
        when(jobQuizRepository.findById(1L)).thenReturn(Optional.of(mockJobQuiz));
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                answerService.saveAnswers(1L, 1L, Collections.emptyList()));
    }

    @Test
    void testCalculateCorrectAnswerPercentage_NoQuestions() {
        // Arrange
        when(answerRepo.countTotalQuestionsByJobQuizId(1L)).thenReturn(0L);

        // Act
        double percentage = answerService.calculateCorrectAnswerPercentage(1L);

        // Assert
        assertEquals(0.0, percentage);
    }

    @Test
    void testSubmitAnswersAndCalculatePercentage_NoRecruiterOrAdmin() {
        // Arrange
        mockJobQuiz.setRecruiter(null);
        mockJobQuiz.setJobAdmin(null);
        when(jobQuizRepository.findById(1L)).thenReturn(Optional.of(mockJobQuiz));
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

        List<Map<String, Object>> answers = new ArrayList<>();
        Map<String, Object> answer = new HashMap<>();
        answer.put("questionId", 1);
        answer.put("selectedAnswer", "Correct Answer");
        answers.add(answer);

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                answerService.submitAnswersAndCalculatePercentage(1L, 1L, answers));
    }

    @Test
    void testGetQuizProgressDetails_NotFound() {
        // Arrange
        when(jobQuizProgressRepo.findByJobQuizIdAndUserId(1L, 1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                answerService.getQuizProgressDetails(1L, 1L));
    }
}