package fusionIQ.AI.V2.fusionIq.testdata;

import static org.junit.jupiter.api.Assertions.*;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AnswerTest {

    private Answer answer;
    private User mockUser;
    private Quiz mockQuiz;
    private Question mockQuestion;
    private Course mockCourse;
    private User mockStudent;
    private JobQuiz mockJobQuiz;
    private JobQuizProgress mockJobQuizProgress;

    @BeforeEach
    public void setUp() {
        // Mocking the related entities
        mockUser = Mockito.mock(User.class);
        mockQuiz = Mockito.mock(Quiz.class);
        mockQuestion = Mockito.mock(Question.class);
        mockCourse = Mockito.mock(Course.class);
        mockStudent = Mockito.mock(User.class);
        mockJobQuiz = Mockito.mock(JobQuiz.class);
        mockJobQuizProgress = Mockito.mock(JobQuizProgress.class);

        // Create a new Answer object with mocked dependencies
        answer = new Answer();
        answer.setId(1L);
        answer.setUser(mockUser);
        answer.setQuiz(mockQuiz);
        answer.setQuestion(mockQuestion);
        answer.setCourse(mockCourse);
        answer.setStudent(mockStudent);
        answer.setJobQuiz(mockJobQuiz);
        answer.setJobQuizProgress(mockJobQuizProgress);
        answer.setSelectedAnswer("A");
        answer.setCorrect(true);
    }

    @Test
    public void testGetId() {
        assertEquals(1L, answer.getId());
    }

    @Test
    public void testSetId() {
        answer.setId(2L);
        assertEquals(2L, answer.getId());
    }

    @Test
    public void testGetUser() {
        assertEquals(mockUser, answer.getUser());
    }

    @Test
    public void testSetUser() {
        User newUser = Mockito.mock(User.class);
        answer.setUser(newUser);
        assertEquals(newUser, answer.getUser());
    }

    @Test
    public void testGetQuiz() {
        assertEquals(mockQuiz, answer.getQuiz());
    }

    @Test
    public void testSetQuiz() {
        Quiz newQuiz = Mockito.mock(Quiz.class);
        answer.setQuiz(newQuiz);
        assertEquals(newQuiz, answer.getQuiz());
    }

    @Test
    public void testGetQuestion() {
        assertEquals(mockQuestion, answer.getQuestion());
    }

    @Test
    public void testSetQuestion() {
        Question newQuestion = Mockito.mock(Question.class);
        answer.setQuestion(newQuestion);
        assertEquals(newQuestion, answer.getQuestion());
    }

    @Test
    public void testGetCourse() {
        assertEquals(mockCourse, answer.getCourse());
    }

    @Test
    public void testSetCourse() {
        Course newCourse = Mockito.mock(Course.class);
        answer.setCourse(newCourse);
        assertEquals(newCourse, answer.getCourse());
    }

    @Test
    public void testGetStudent() {
        assertEquals(mockStudent, answer.getStudent());
    }

    @Test
    public void testSetStudent() {
        User newStudent = Mockito.mock(User.class);
        answer.setStudent(newStudent);
        assertEquals(newStudent, answer.getStudent());
    }

    @Test
    public void testGetSelectedAnswer() {
        assertEquals("A", answer.getSelectedAnswer());
    }

    @Test
    public void testSetSelectedAnswer() {
        answer.setSelectedAnswer("B");
        assertEquals("B", answer.getSelectedAnswer());
    }

    @Test
    public void testIsCorrect() {
        assertTrue(answer.isCorrect());
    }

    @Test
    public void testSetCorrect() {
        answer.setCorrect(false);
        assertFalse(answer.isCorrect());
    }

    @Test
    public void testGetJobQuiz() {
        assertEquals(mockJobQuiz, answer.getJobQuiz());
    }

    @Test
    public void testSetJobQuiz() {
        JobQuiz newJobQuiz = Mockito.mock(JobQuiz.class);
        answer.setJobQuiz(newJobQuiz);
        assertEquals(newJobQuiz, answer.getJobQuiz());
    }

    @Test
    public void testGetJobQuizProgress() {
        assertEquals(mockJobQuizProgress, answer.getJobQuizProgress());
    }

    @Test
    public void testSetJobQuizProgress() {
        JobQuizProgress newJobQuizProgress = Mockito.mock(JobQuizProgress.class);
        answer.setJobQuizProgress(newJobQuizProgress);
        assertEquals(newJobQuizProgress, answer.getJobQuizProgress());
    }

    @Test
    public void testToString() {
        String expectedString = "Answer{id=1, user=" + mockUser + ", quiz=" + mockQuiz + ", jobQuiz=" + mockJobQuiz +
                ", jobQuizProgress=" + mockJobQuizProgress + ", question=" + mockQuestion + ", course=" + mockCourse +
                ", student=" + mockStudent + ", selectedAnswer='A', isCorrect=true}";

        assertEquals(expectedString, answer.toString());
    }
}
