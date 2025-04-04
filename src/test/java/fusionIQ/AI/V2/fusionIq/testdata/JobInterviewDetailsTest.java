package fusionIQ.AI.V2.fusionIq.testdata;


import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.JobInterviewDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JobInterviewDetailsTest {

    private JobInterviewDetails jobInterviewDetails;
    private LocalDateTime testDateTime;
    private Job testJob;

    @BeforeEach
    void setUp() {
        jobInterviewDetails = new JobInterviewDetails();
        testDateTime = LocalDateTime.now();
        testJob = new Job();
        testJob.setId(1L);
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(jobInterviewDetails);
    }

    @Test
    void testParameterizedConstructor() {
        JobInterviewDetails details = new JobInterviewDetails(
                1L, // id
                2L, // userId
                "John Doe", // userName
                "john@example.com", // userEmail
                3L, // recruiterId
                "Jane Recruiter", // recruiterName
                "jane@recruiter.com", // recruiterEmail
                4L, // adminId
                "Admin User", // adminName
                "admin@example.com", // adminEmail
                testDateTime, // interviewTimestamp
                "http://meet.example.com/123", // interviewLink
                "http://score.example.com/123", // interviewScoreLink
                "Technical Interview Round 1", // interviewDescription
                "interviewer@example.com", // interviewerEmail
                "Tech Interviewer", // interviewerName
                85, // interviewerScore
                "Good technical knowledge", // interviewerFeedback
                "level-1", // currentLevel
                "level-2", // upcomingLevel
                "Positive feedback from initial screening", // currentFeedback
                testJob // job
        );

        assertNotNull(details);
        assertEquals(1L, details.getId());
        assertEquals(2L, details.getUserId());
        assertEquals("John Doe", details.getUserName());
        assertEquals("john@example.com", details.getUserEmail());
        assertEquals(3L, details.getRecruiterId());
        assertEquals("Jane Recruiter", details.getRecruiterName());
        assertEquals("jane@recruiter.com", details.getRecruiterEmail());
        assertEquals(4L, details.getAdminId());
        assertEquals("Admin User", details.getAdminName());
        assertEquals("admin@example.com", details.getAdminEmail());
        assertEquals(testDateTime, details.getInterviewTimestamp());
        assertEquals("http://meet.example.com/123", details.getInterviewLink());
        assertEquals("http://score.example.com/123", details.getInterviewScoreLink());
        assertEquals("Technical Interview Round 1", details.getInterviewDescription());
        assertEquals("interviewer@example.com", details.getInterviewerEmail());
        assertEquals("Tech Interviewer", details.getInterviewerName());
        assertEquals(85, details.getInterviewerScore());
        assertEquals("Good technical knowledge", details.getInterviewerFeedback());
        assertEquals("level-1", details.getCurrentLevel());
        assertEquals("level-2", details.getUpcomingLevel());
        assertEquals("Positive feedback from initial screening", details.getCurrentFeedback());
        assertEquals(testJob, details.getJob());
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        jobInterviewDetails.setId(id);
        assertEquals(id, jobInterviewDetails.getId());
    }

    @Test
    void testSetAndGetUserId() {
        Long userId = 2L;
        jobInterviewDetails.setUserId(userId);
        assertEquals(userId, jobInterviewDetails.getUserId());
    }

    @Test
    void testSetAndGetUserName() {
        String userName = "John Doe";
        jobInterviewDetails.setUserName(userName);
        assertEquals(userName, jobInterviewDetails.getUserName());
    }

    @Test
    void testSetAndGetUserEmail() {
        String userEmail = "john@example.com";
        jobInterviewDetails.setUserEmail(userEmail);
        assertEquals(userEmail, jobInterviewDetails.getUserEmail());
    }

    @Test
    void testSetAndGetRecruiterId() {
        Long recruiterId = 3L;
        jobInterviewDetails.setRecruiterId(recruiterId);
        assertEquals(recruiterId, jobInterviewDetails.getRecruiterId());
    }

    @Test
    void testSetAndGetRecruiterName() {
        String recruiterName = "Jane Recruiter";
        jobInterviewDetails.setRecruiterName(recruiterName);
        assertEquals(recruiterName, jobInterviewDetails.getRecruiterName());
    }

    @Test
    void testSetAndGetRecruiterEmail() {
        String recruiterEmail = "jane@recruiter.com";
        jobInterviewDetails.setRecruiterEmail(recruiterEmail);
        assertEquals(recruiterEmail, jobInterviewDetails.getRecruiterEmail());
    }

    @Test
    void testSetAndGetAdminId() {
        Long adminId = 4L;
        jobInterviewDetails.setAdminId(adminId);
        assertEquals(adminId, jobInterviewDetails.getAdminId());
    }

    @Test
    void testSetAndGetAdminName() {
        String adminName = "Admin User";
        jobInterviewDetails.setAdminName(adminName);
        assertEquals(adminName, jobInterviewDetails.getAdminName());
    }

    @Test
    void testSetAndGetAdminEmail() {
        String adminEmail = "admin@example.com";
        jobInterviewDetails.setAdminEmail(adminEmail);
        assertEquals(adminEmail, jobInterviewDetails.getAdminEmail());
    }

    @Test
    void testSetAndGetInterviewTimestamp() {
        jobInterviewDetails.setInterviewTimestamp(testDateTime);
        assertEquals(testDateTime, jobInterviewDetails.getInterviewTimestamp());
    }

    @Test
    void testSetAndGetInterviewLink() {
        String interviewLink = "http://meet.example.com/123";
        jobInterviewDetails.setInterviewLink(interviewLink);
        assertEquals(interviewLink, jobInterviewDetails.getInterviewLink());
    }

    @Test
    void testSetAndGetInterviewScoreLink() {
        String interviewScoreLink = "http://score.example.com/123";
        jobInterviewDetails.setInterviewScoreLink(interviewScoreLink);
        assertEquals(interviewScoreLink, jobInterviewDetails.getInterviewScoreLink());
    }

    @Test
    void testSetAndGetInterviewDescription() {
        String interviewDescription = "Technical Interview Round 1";
        jobInterviewDetails.setInterviewDescription(interviewDescription);
        assertEquals(interviewDescription, jobInterviewDetails.getInterviewDescription());
    }

    @Test
    void testSetAndGetInterviewerEmail() {
        String interviewerEmail = "interviewer@example.com";
        jobInterviewDetails.setInterviewerEmail(interviewerEmail);
        assertEquals(interviewerEmail, jobInterviewDetails.getInterviewerEmail());
    }

    @Test
    void testSetAndGetInterviewerName() {
        String interviewerName = "Tech Interviewer";
        jobInterviewDetails.setInterviewerName(interviewerName);
        assertEquals(interviewerName, jobInterviewDetails.getInterviewerName());
    }

    @Test
    void testSetAndGetInterviewerScore() {
        Integer interviewerScore = 85;
        jobInterviewDetails.setInterviewerScore(interviewerScore);
        assertEquals(interviewerScore, jobInterviewDetails.getInterviewerScore());
    }

    @Test
    void testSetAndGetInterviewerFeedback() {
        String interviewerFeedback = "Good technical knowledge";
        jobInterviewDetails.setInterviewerFeedback(interviewerFeedback);
        assertEquals(interviewerFeedback, jobInterviewDetails.getInterviewerFeedback());
    }

    @Test
    void testSetAndGetCurrentLevel() {
        String currentLevel = "level-1";
        jobInterviewDetails.setCurrentLevel(currentLevel);
        assertEquals(currentLevel, jobInterviewDetails.getCurrentLevel());
    }

    @Test
    void testSetAndGetUpcomingLevel() {
        String upcomingLevel = "level-2";
        jobInterviewDetails.setUpcomingLevel(upcomingLevel);
        assertEquals(upcomingLevel, jobInterviewDetails.getUpcomingLevel());
    }

    @Test
    void testSetAndGetCurrentFeedback() {
        String currentFeedback = "Positive feedback from initial screening";
        jobInterviewDetails.setCurrentFeedback(currentFeedback);
        assertEquals(currentFeedback, jobInterviewDetails.getCurrentFeedback());
    }

    @Test
    void testSetAndGetJob() {
        jobInterviewDetails.setJob(testJob);
        assertEquals(testJob, jobInterviewDetails.getJob());
    }

    @Test
    void testToString() {
        jobInterviewDetails.setId(1L);
        jobInterviewDetails.setUserName("John Doe");
        jobInterviewDetails.setUserEmail("john@example.com");

        String toString = jobInterviewDetails.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("userName='John Doe'"));
        assertTrue(toString.contains("userEmail='john@example.com'"));
    }

    @Test
    void testNullValues() {
        JobInterviewDetails details = new JobInterviewDetails();

        assertNull(details.getId());
        assertNull(details.getUserId());
        assertNull(details.getUserName());
        assertNull(details.getUserEmail());
        assertNull(details.getRecruiterId());
        assertNull(details.getRecruiterName());
        assertNull(details.getRecruiterEmail());
        assertNull(details.getAdminId());
        assertNull(details.getAdminName());
        assertNull(details.getAdminEmail());
        assertNull(details.getInterviewTimestamp());
        assertNull(details.getInterviewLink());
        assertNull(details.getInterviewScoreLink());
        assertNull(details.getInterviewDescription());
        assertNull(details.getInterviewerEmail());
        assertNull(details.getInterviewerName());
        assertNull(details.getInterviewerScore());
        assertNull(details.getInterviewerFeedback());
        assertNull(details.getCurrentLevel());
        assertNull(details.getUpcomingLevel());
        assertNull(details.getCurrentFeedback());
        assertNull(details.getJob());
    }
}