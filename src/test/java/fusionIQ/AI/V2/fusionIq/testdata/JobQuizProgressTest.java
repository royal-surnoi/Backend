package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Answer;
import fusionIQ.AI.V2.fusionIq.data.JobQuiz;
import fusionIQ.AI.V2.fusionIq.data.JobQuizProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JobQuizProgressTest {

    private JobQuizProgress jobQuizProgress;
    private LocalDateTime completedAt;
    private JobQuiz jobQuiz;
    private List<Answer> answers;

    @BeforeEach
    void setUp() {
        completedAt = LocalDateTime.now();
        jobQuiz = new JobQuiz();
        answers = new ArrayList<>();

        jobQuizProgress = new JobQuizProgress(
                1L,                    // id
                100L,                  // userId
                "John Doe",            // userName
                "john@example.com",    // userEmail
                200L,                  // recruiterId
                "Jane Recruiter",      // recruiterName
                "jane@example.com",    // recruiterEmail
                300L,                  // adminId
                "Admin User",          // adminName
                "admin@example.com",   // adminEmail
                95.5,                  // scorePercentage
                completedAt,           // completedAt
                jobQuiz,              // jobQuiz
                answers               // answers
        );
    }

    @Test
    void testDefaultConstructor() {
        JobQuizProgress emptyProgress = new JobQuizProgress();
        assertNotNull(emptyProgress);
    }

    @Test
    void testParameterizedConstructor() {
        assertNotNull(jobQuizProgress);
        assertEquals(1L, jobQuizProgress.getId());
        assertEquals(100L, jobQuizProgress.getUserId());
        assertEquals("John Doe", jobQuizProgress.getUserName());
        assertEquals("john@example.com", jobQuizProgress.getUserEmail());
        assertEquals(200L, jobQuizProgress.getRecruiterId());
        assertEquals("Jane Recruiter", jobQuizProgress.getRecruiterName());
        assertEquals("jane@example.com", jobQuizProgress.getRecruiterEmail());
        assertEquals(300L, jobQuizProgress.getAdminId());
        assertEquals("Admin User", jobQuizProgress.getAdminName());
        assertEquals("admin@example.com", jobQuizProgress.getAdminEmail());
        assertEquals(95.5, jobQuizProgress.getScorePercentage());
        assertEquals(completedAt, jobQuizProgress.getCompletedAt());
        assertEquals(jobQuiz, jobQuizProgress.getJobQuiz());
        assertEquals(answers, jobQuizProgress.getAnswers());
    }

    @Test
    void testSetAndGetId() {
        jobQuizProgress.setId(2L);
        assertEquals(2L, jobQuizProgress.getId());
    }

    @Test
    void testSetAndGetUserId() {
        jobQuizProgress.setUserId(101L);
        assertEquals(101L, jobQuizProgress.getUserId());
    }

    @Test
    void testSetAndGetUserName() {
        jobQuizProgress.setUserName("Jane Doe");
        assertEquals("Jane Doe", jobQuizProgress.getUserName());
    }

    @Test
    void testSetAndGetUserEmail() {
        jobQuizProgress.setUserEmail("jane.doe@example.com");
        assertEquals("jane.doe@example.com", jobQuizProgress.getUserEmail());
    }

    @Test
    void testSetAndGetRecruiterId() {
        jobQuizProgress.setRecruiterId(201L);
        assertEquals(201L, jobQuizProgress.getRecruiterId());
    }

    @Test
    void testSetAndGetRecruiterName() {
        jobQuizProgress.setRecruiterName("New Recruiter");
        assertEquals("New Recruiter", jobQuizProgress.getRecruiterName());
    }

    @Test
    void testSetAndGetRecruiterEmail() {
        jobQuizProgress.setRecruiterEmail("new.recruiter@example.com");
        assertEquals("new.recruiter@example.com", jobQuizProgress.getRecruiterEmail());
    }

    @Test
    void testSetAndGetAdminId() {
        jobQuizProgress.setAdminId(301L);
        assertEquals(301L, jobQuizProgress.getAdminId());
    }

    @Test
    void testSetAndGetAdminName() {
        jobQuizProgress.setAdminName("New Admin");
        assertEquals("New Admin", jobQuizProgress.getAdminName());
    }

    @Test
    void testSetAndGetAdminEmail() {
        jobQuizProgress.setAdminEmail("new.admin@example.com");
        assertEquals("new.admin@example.com", jobQuizProgress.getAdminEmail());
    }

    @Test
    void testSetAndGetScorePercentage() {
        jobQuizProgress.setScorePercentage(98.8);
        assertEquals(98.8, jobQuizProgress.getScorePercentage());
    }

    @Test
    void testSetAndGetCompletedAt() {
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1);
        jobQuizProgress.setCompletedAt(newDateTime);
        assertEquals(newDateTime, jobQuizProgress.getCompletedAt());
    }

    @Test
    void testSetAndGetJobQuiz() {
        JobQuiz newJobQuiz = new JobQuiz();
        jobQuizProgress.setJobQuiz(newJobQuiz);
        assertEquals(newJobQuiz, jobQuizProgress.getJobQuiz());
    }

    @Test
    void testSetAndGetAnswers() {
        List<Answer> newAnswers = new ArrayList<>();
        Answer answer = new Answer();
        newAnswers.add(answer);
        jobQuizProgress.setAnswers(newAnswers);
        assertEquals(newAnswers, jobQuizProgress.getAnswers());
    }

    @Test
    void testToString() {
        String toString = jobQuizProgress.toString();
        assertTrue(toString.contains("id=" + jobQuizProgress.getId()));
        assertTrue(toString.contains("userId=" + jobQuizProgress.getUserId()));
        assertTrue(toString.contains("userName='" + jobQuizProgress.getUserName() + "'"));
        assertTrue(toString.contains("userEmail='" + jobQuizProgress.getUserEmail() + "'"));
        assertTrue(toString.contains("recruiterId=" + jobQuizProgress.getRecruiterId()));
        assertTrue(toString.contains("recruiterName='" + jobQuizProgress.getRecruiterName() + "'"));
        assertTrue(toString.contains("recruiterEmail='" + jobQuizProgress.getRecruiterEmail() + "'"));
        assertTrue(toString.contains("adminId=" + jobQuizProgress.getAdminId()));
        assertTrue(toString.contains("adminName='" + jobQuizProgress.getAdminName() + "'"));
        assertTrue(toString.contains("adminEmail='" + jobQuizProgress.getAdminEmail() + "'"));
        assertTrue(toString.contains("scorePercentage=" + jobQuizProgress.getScorePercentage()));
        assertTrue(toString.contains("completedAt=" + jobQuizProgress.getCompletedAt()));
    }
}
