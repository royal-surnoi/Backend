package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ShortlistedCandidatesTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        ApplyJob applyJob = new ApplyJob();
        Job job = new Job();
        String status = "Shortlisted";
        Long recruiterId = 1L;
        Long adminId = 2L;

        // Act
        ShortlistedCandidates candidate = new ShortlistedCandidates(applyJob, job, status, recruiterId, adminId);

        // Assert
        assertEquals(applyJob, candidate.getApplyJob());
        assertEquals(job, candidate.getJob());
        assertEquals(status, candidate.getStatus());
        assertEquals(recruiterId, candidate.getRecruiterId());
        assertEquals(adminId, candidate.getAdminId());
        assertNotNull(candidate.getShortlistedAt());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        ShortlistedCandidates candidate = new ShortlistedCandidates();
        Long id = 1L;
        ApplyJob applyJob = new ApplyJob();
        Job job = new Job();
        LocalDateTime shortlistedAt = LocalDateTime.now();
        String status = "Shortlisted";
        User user = new User();
        JobQuiz jobQuiz = new JobQuiz();
        Long recruiterId = 1L;
        Long adminId = 2L;

        // Act
        candidate.setId(id);
        candidate.setApplyJob(applyJob);
        candidate.setJob(job);
        candidate.setShortlistedAt(shortlistedAt);
        candidate.setStatus(status);
        candidate.setUser(user);
        candidate.setJobQuiz(jobQuiz);
        candidate.setRecruiterId(recruiterId);
        candidate.setAdminId(adminId);

        // Assert
        assertEquals(id, candidate.getId());
        assertEquals(applyJob, candidate.getApplyJob());
        assertEquals(job, candidate.getJob());
        assertEquals(shortlistedAt, candidate.getShortlistedAt());
        assertEquals(status, candidate.getStatus());
        assertEquals(user, candidate.getUser());
        assertEquals(jobQuiz, candidate.getJobQuiz());
        assertEquals(recruiterId, candidate.getRecruiterId());
        assertEquals(adminId, candidate.getAdminId());
    }

    @Test
    void testToString() {
        // Arrange
        ShortlistedCandidates candidate = new ShortlistedCandidates();
        candidate.setId(1L);
        candidate.setStatus("Shortlisted");
        candidate.setShortlistedAt(LocalDateTime.now());
        candidate.setRecruiterId(1L);
        candidate.setAdminId(2L);

        // Act
        String toString = candidate.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("status='Shortlisted'"));
        assertTrue(toString.contains("recruiterId=1"));
        assertTrue(toString.contains("adminId=2"));
    }

    @Test
    void testDefaultConstructor() {
        // Act
        ShortlistedCandidates candidate = new ShortlistedCandidates();

        // Assert
        assertNotNull(candidate);
        assertNull(candidate.getId());
        assertNull(candidate.getStatus());
        assertNull(candidate.getShortlistedAt());
        assertNull(candidate.getRecruiterId());
        assertNull(candidate.getAdminId());
    }
}
