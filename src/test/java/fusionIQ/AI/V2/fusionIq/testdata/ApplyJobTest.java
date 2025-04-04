package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.ApplyJob;
import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.ShortlistedCandidates;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplyJobTest {


    @Test
    void testApplyJobConstructor() {
        // Arrange
        User user = new User();
        Job job = new Job();
        byte[] resume = new byte[]{1, 2, 3};

        // Act
        ApplyJob applyJob = new ApplyJob(user, job, "Applied", resume);

        // Assert
        assertNotNull(applyJob);
        assertEquals(user, applyJob.getUser());
        assertEquals(job, applyJob.getJob());
        assertEquals("Applied", applyJob.getStatus());
        assertArrayEquals(resume, applyJob.getResume());
        assertNotNull(applyJob.getLastUpdated()); // Ensure the timestamp is initialized
    }
    @Test
    void testStatusUpdate() {
        // Arrange
        User user = new User();
        Job job = new Job();
        ApplyJob applyJob = new ApplyJob(user, job, "Applied", new byte[]{});

        // Act
        applyJob.setStatus("Shortlisted");

        // Assert
        assertEquals("Shortlisted", applyJob.getStatus());
        assertNotNull(applyJob.getLastUpdated()); // Ensure lastUpdated field is updated
    }
    @Test
    void testResumeFieldHandling() {
        // Arrange
        User user = new User();
        Job job = new Job();
        byte[] resume = new byte[]{1, 2, 3};
        ApplyJob applyJob = new ApplyJob(user, job, "Applied", resume);

        // Act
        byte[] newResume = new byte[]{4, 5, 6};
        applyJob.setResume(newResume);

        // Assert
        assertArrayEquals(newResume, applyJob.getResume());
    }
    @Test
    void testWithdrawFieldDefaultValue() {
        // Arrange
        User user = new User();
        Job job = new Job();
        ApplyJob applyJob = new ApplyJob(user, job, "Applied", new byte[]{});

        // Act
        String withdrawStatus = applyJob.getWithdraw();

        // Assert
        assertEquals("no", withdrawStatus); // Default value should be "no"
    }
    @Test
    void testShortlistedCandidatesRelationship() {
        // Arrange
        User user = new User();
        Job job = new Job();
        ApplyJob applyJob = new ApplyJob(user, job, "Applied", new byte[]{});

        ShortlistedCandidates shortlistedCandidate = new ShortlistedCandidates();
        shortlistedCandidate.setApplyJob(applyJob);

        List<ShortlistedCandidates> shortlistedCandidates = new ArrayList<>();
        shortlistedCandidates.add(shortlistedCandidate);
        applyJob.setShortlistedCandidates(shortlistedCandidates);

        // Act
        List<ShortlistedCandidates> result = applyJob.getShortlistedCandidates();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(shortlistedCandidate, result.get(0));
    }
    @Test
    void testManyToOneRelationships() {
        // Arrange
        User user = new User();
        Job job = new Job();
        ApplyJob applyJob = new ApplyJob(user, job, "Applied", new byte[]{});

        // Act & Assert
        assertEquals(user, applyJob.getUser());
        assertEquals(job, applyJob.getJob());
    }
    @Test
    void testToStringMethod() {
        // Arrange
        User user = new User();
        Job job = new Job();
        ApplyJob applyJob = new ApplyJob(user, job, "Applied", new byte[]{});

        // Act
        String result = applyJob.toString();

        // Assert
        assertTrue(result.contains("ApplyJob"));
        assertTrue(result.contains("id=" + applyJob.getId()));
        assertTrue(result.contains("user=" + user.toString()));
        assertTrue(result.contains("job=" + job.toString()));
    }
    @Test
    void testApplyJobWithNullFields() {
        // Arrange
        ApplyJob applyJob = new ApplyJob();

        // Act & Assert
        assertNull(applyJob.getUser());
        assertNull(applyJob.getJob());
        assertNull(applyJob.getStatus());
        assertNull(applyJob.getResume());
        assertNull(applyJob.getWithdraw());
    }

}
