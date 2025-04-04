package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecruiterTest {

    private Recruiter recruiter;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        recruiter = new Recruiter();
        testDateTime = LocalDateTime.now();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(recruiter);
        assertNull(recruiter.getId());
        assertNull(recruiter.getRecruiterName());
        assertNull(recruiter.getRecruiterEmail());
        assertNull(recruiter.getRecruiterPassword());
        assertNull(recruiter.getRecruiterRole());
        assertNull(recruiter.getRecruiterDeportment());
        assertNull(recruiter.getCreatedAt());
        assertNull(recruiter.getUpdatedAt());
    }

    @Test
    void testIdConstructor() {
        Recruiter recruiterWithId = new Recruiter(1L);
        assertEquals(1L, recruiterWithId.getId());
    }

    @Test
    void testParameterizedConstructor() {
        JobAdmin jobAdmin = new JobAdmin();
        List<JobQuiz> jobQuizzes = new ArrayList<>();
        List<Job> recruiterJobs = new ArrayList<>();
        List<RecruiterFeedback> recruiterFeedbacks = new ArrayList<>();

        Recruiter recruiterWithParams = new Recruiter(
                1L,
                "Test Recruiter",
                "test@example.com",
                "password123",
                "HR",
                "Engineering",
                testDateTime,
                testDateTime,
                jobAdmin,
                jobQuizzes,
                recruiterJobs,
                recruiterFeedbacks
        );

        assertEquals(1L, recruiterWithParams.getId());
        assertEquals("Test Recruiter", recruiterWithParams.getRecruiterName());
        assertEquals("test@example.com", recruiterWithParams.getRecruiterEmail());
        assertEquals("password123", recruiterWithParams.getRecruiterPassword());
        assertEquals("HR", recruiterWithParams.getRecruiterRole());
        assertEquals("Engineering", recruiterWithParams.getRecruiterDeportment());
        assertEquals(testDateTime, recruiterWithParams.getCreatedAt());
        assertEquals(testDateTime, recruiterWithParams.getUpdatedAt());
        assertEquals(jobAdmin, recruiterWithParams.getJobAdmin());
        assertEquals(jobQuizzes, recruiterWithParams.getJobQuizzes());
        assertEquals(recruiterJobs, recruiterWithParams.getRecruiterJobs());
        assertEquals(recruiterFeedbacks, recruiterWithParams.getRecruiterFeedbacks());
    }

    @Test
    void testSetAndGetBasicFields() {
        recruiter.setId(1L);
        recruiter.setRecruiterName("John Doe");
        recruiter.setRecruiterEmail("john.doe@example.com");
        recruiter.setRecruiterPassword("securePass123");
        recruiter.setRecruiterRole("Senior Recruiter");
        recruiter.setRecruiterDeportment("HR Department");

        assertEquals(1L, recruiter.getId());
        assertEquals("John Doe", recruiter.getRecruiterName());
        assertEquals("john.doe@example.com", recruiter.getRecruiterEmail());
        assertEquals("securePass123", recruiter.getRecruiterPassword());
        assertEquals("Senior Recruiter", recruiter.getRecruiterRole());
        assertEquals("HR Department", recruiter.getRecruiterDeportment());
    }

    @Test
    void testSetAndGetDateTime() {
        recruiter.setCreatedAt(testDateTime);
        recruiter.setUpdatedAt(testDateTime);

        assertEquals(testDateTime, recruiter.getCreatedAt());
        assertEquals(testDateTime, recruiter.getUpdatedAt());
    }

    @Test
    void testSetAndGetJobAdmin() {
        JobAdmin jobAdmin = new JobAdmin();
        jobAdmin.setId(1L);
        jobAdmin.setJobAdminName("Admin Test");

        recruiter.setJobAdmin(jobAdmin);
        assertEquals(jobAdmin, recruiter.getJobAdmin());
        assertEquals("Admin Test", recruiter.getJobAdmin().getJobAdminName());
    }

    @Test
    void testSetAndGetJobQuizzes() {
        List<JobQuiz> jobQuizzes = new ArrayList<>();
        JobQuiz quiz1 = new JobQuiz();
        JobQuiz quiz2 = new JobQuiz();
        jobQuizzes.add(quiz1);
        jobQuizzes.add(quiz2);

        recruiter.setJobQuizzes(jobQuizzes);
    }
}