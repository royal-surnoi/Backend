package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;
    private Job job;
    private ApplyJob applyJob;
    private Education education;
    private UserDocunment document;
    private UserProjects project;
    private UserSkills skill;
    private Device device;
    private Institute institute;
    private InstituteTeacher teacher;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setProfession("Software Engineer");
        user.setUserLanguage("English");
        user.setUserDescription("Test user description");
        user.setOnlineStatus(User.OnlineStatus.ONLINE);
    }

    @Test
    void testBasicUserProperties() {
        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("Software Engineer", user.getProfession());
        assertEquals("English", user.getUserLanguage());
        assertEquals("Test user description", user.getUserDescription());
        assertEquals(User.OnlineStatus.ONLINE, user.getOnlineStatus());
    }

    @Test
    void testUserTimestamps() {
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertTrue(user.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(user.getUpdatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testApplyJobsRelationship() {
        // Setup
        job = new Job();
        job.setId(1L);
        job.setJobTitle("Software Developer");

        applyJob = new ApplyJob(user, job, "Applied", new byte[0]);

        ArrayList<ApplyJob> applyJobs = new ArrayList<>();
        applyJobs.add(applyJob);
        user.setApplyJobs(applyJobs);

        // Verify
        assertEquals(1, user.getApplyJobs().size());
        assertEquals("Applied", user.getApplyJobs().get(0).getStatus());
        assertEquals(job.getJobTitle(), user.getApplyJobs().get(0).getJob().getJobTitle());
    }

    @Test
    void testEducationRelationship() {
        education = new Education();
        education.setUser(user);
        user.setEducation(education);

        assertNotNull(user.getEducation());
        assertEquals(user, user.getEducation().getUser());
    }
    

    @Test
    void testUserEquality() {
        User user1 = new User(1L);
        User user2 = new User(1L);
        User user3 = new User(2L);

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    void testUserCollectionsInitialization() {
        User newUser = new User();
        assertNotNull(newUser.getDevices());
        assertNotNull(newUser.getDocuments());
        assertNotNull(newUser.getProjects());
        assertNotNull(newUser.getSkills());
        assertNotNull(newUser.getEducationDetails());
        assertNotNull(newUser.getRecruiterFeedbacks());
        assertNotNull(newUser.getJobCommunities());
        assertNotNull(newUser.getPostCommunities());
    }

    @Test
    void testUserImage() {
        byte[] imageData = "test image data".getBytes();
        user.setUserImage(imageData);
        assertArrayEquals(imageData, user.getUserImage());
    }

    @Test
    void testOtpFunctionality() {
        String otp = "123456";
        LocalDateTime otpTime = LocalDateTime.now();

        user.setOtp(otp);
        user.setOtpGeneratedTime(otpTime);

        assertEquals(otp, user.getOtp());
        assertEquals(otpTime, user.getOtpGeneratedTime());
    }

    @Test
    void testToString() {
        String userString = user.toString();
        assertTrue(userString.contains("id=" + user.getId()));
        assertTrue(userString.contains("name='" + user.getName() + "'"));
        assertTrue(userString.contains("email='" + user.getEmail() + "'"));
    }
}