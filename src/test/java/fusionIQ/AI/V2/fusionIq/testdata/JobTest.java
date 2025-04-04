package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import fusionIQ.AI.V2.fusionIq.data.Recruiter;
import fusionIQ.AI.V2.fusionIq.data.SavedJobs;
import fusionIQ.AI.V2.fusionIq.data.RecruiterFeedback;
import fusionIQ.AI.V2.fusionIq.data.ApplyJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JobTest {
    private Job job;
    private JobAdmin jobAdmin;
    private Recruiter recruiter;

    @BeforeEach
    void setUp() {
        // Initialize a job admin
        jobAdmin = new JobAdmin();
        jobAdmin.setId(1L);
        jobAdmin.setJobAdminName("Test Admin");
        jobAdmin.setJobAdminEmail("admin@test.com");

        // Initialize a recruiter
        recruiter = new Recruiter();
        recruiter.setId(1L);
        recruiter.setRecruiterName("Test Recruiter");
        recruiter.setRecruiterEmail("recruiter@test.com");

        // Initialize the job with all fields
        job = new Job();
        job.setId(1L);
        job.setJobTitle("Software Engineer");
        job.setJobDescription("Java Developer position");
        job.setBasicJobQualification("Bachelor's degree in Computer Science");
        job.setPrimaryRoles("Development and maintenance of Java applications");
        job.setMainResponsibilities("Code development, testing, and deployment");
        job.setRequiredSkills("Java, Spring Boot, MySQL");
        job.setLocation("New York");
        job.setMinSalary(new BigDecimal("70000"));
        job.setMaxSalary(new BigDecimal("100000"));
        job.setJobType("Full-time");
        job.setStatus("open");
        job.setVacancyCount(2);
        job.setAppliedCount(0);
        job.setNumberOfLevels(3);
        job.setRequiredEducation("Bachelor's");
        job.setRequiredEducationStream("Computer Science");
        job.setRequiredPercentage(70.0);
        job.setRequiredPassoutYear(2023);
        job.setRequiredWorkExperience(2);
        job.setJobAdmin(jobAdmin);
        job.setRecruiter(recruiter);
    }

    @Test
    @DisplayName("Test Job Creation")
    void testJobCreation() {
        assertNotNull(job);
        assertEquals(1L, job.getId());
        assertEquals("Software Engineer", job.getJobTitle());
        assertEquals("open", job.getStatus());
        assertNotNull(job.getCreatedAt());
    }

    @Test
    @DisplayName("Test Job Basic Properties")
    void testJobBasicProperties() {
        assertEquals("Java Developer position", job.getJobDescription());
        assertEquals("Bachelor's degree in Computer Science", job.getBasicJobQualification());
        assertEquals("Development and maintenance of Java applications", job.getPrimaryRoles());
        assertEquals("Code development, testing, and deployment", job.getMainResponsibilities());
        assertEquals("Java, Spring Boot, MySQL", job.getRequiredSkills());
        assertEquals("New York", job.getLocation());
        assertEquals(new BigDecimal("70000"), job.getMinSalary());
        assertEquals(new BigDecimal("100000"), job.getMaxSalary());
        assertEquals("Full-time", job.getJobType());
    }

    @Test
    @DisplayName("Test Job Counts and Levels")
    void testJobCountsAndLevels() {
        assertEquals(2, job.getVacancyCount());
        assertEquals(0, job.getAppliedCount());
        assertEquals(3, job.getNumberOfLevels());
    }

    @Test
    @DisplayName("Test Job Requirements")
    void testJobRequirements() {
        assertEquals("Bachelor's", job.getRequiredEducation());
        assertEquals("Computer Science", job.getRequiredEducationStream());
        assertEquals(70.0, job.getRequiredPercentage());
        assertEquals(2023, job.getRequiredPassoutYear());
        assertEquals(2, job.getRequiredWorkExperience());
    }

    @Test
    @DisplayName("Test Job Relations")
    void testJobRelations() {
        assertNotNull(job.getJobAdmin());
        assertEquals(1L, job.getJobAdmin().getId());
        assertEquals("Test Admin", job.getJobAdmin().getJobAdminName());

        assertNotNull(job.getRecruiter());
        assertEquals(1L, job.getRecruiter().getId());
        assertEquals("Test Recruiter", job.getRecruiter().getRecruiterName());
    }

    @Test
    @DisplayName("Test Job Lists")
    void testJobLists() {
        // Initialize lists
        List<SavedJobs> savedJobs = new ArrayList<>();
        List<RecruiterFeedback> recruiterFeedbacks = new ArrayList<>();
        List<ApplyJob> applyJobs = new ArrayList<>();

        // Set lists
        job.setSavedJobs(savedJobs);
        job.setRecruiterFeedbacks(recruiterFeedbacks);
        job.setApplyJobs(applyJobs);

        // Test lists
        assertNotNull(job.getSavedJobs());
        assertNotNull(job.getRecruiterFeedbacks());
        assertNotNull(job.getApplyJobs());
        assertTrue(job.getSavedJobs().isEmpty());
        assertTrue(job.getRecruiterFeedbacks().isEmpty());
        assertTrue(job.getApplyJobs().isEmpty());
    }

    @Test
    @DisplayName("Test Job Status Update")
    void testJobStatusUpdate() {
        job.setStatus("closed");
        assertEquals("closed", job.getStatus());
    }

    @Test
    @DisplayName("Test Job Application Count Update")
    void testJobApplicationCountUpdate() {
        job.setAppliedCount(1);
        assertEquals(1, job.getAppliedCount());
    }

    @Test
    @DisplayName("Test Job ToString")
    void testJobToString() {
        String jobString = job.toString();
        assertTrue(jobString.contains("Software Engineer"));
        assertTrue(jobString.contains("New York"));
        assertTrue(jobString.contains("Java Developer position"));
    }

    @Test
    @DisplayName("Test Job Full Constructor")
    void testJobFullConstructor() {
        Job newJob = new Job(
                1L, "Software Engineer", "Description", "Qualification",
                "Roles", "Responsibilities", "Skills", "Location",
                new BigDecimal("70000"), new BigDecimal("100000"),
                "Full-time", "open", 2, 0, 3,
                LocalDateTime.now(), "Bachelor's", "Computer Science",
                70.0, 2023, 2, recruiter, jobAdmin,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
        );

        assertNotNull(newJob);
        assertEquals("Software Engineer", newJob.getJobTitle());
        assertEquals("Description", newJob.getJobDescription());
        assertEquals(jobAdmin, newJob.getJobAdmin());
        assertEquals(recruiter, newJob.getRecruiter());
    }

    @Test
    @DisplayName("Test Job Default Constructor")
    void testJobDefaultConstructor() {
        Job newJob = new Job();
        assertNotNull(newJob);
        assertNotNull(newJob.getCreatedAt());
        assertEquals("open", newJob.getStatus());
    }

    @Test
    @DisplayName("Test Job ID Constructor")
    void testJobIdConstructor() {
        Job newJob = new Job(1L);
        assertNotNull(newJob);
        assertEquals(1L, newJob.getId());
    }

    @Test
    @DisplayName("Test Salary Range Validation")
    void testSalaryRangeValidation() {
        job.setMinSalary(new BigDecimal("60000"));
        job.setMaxSalary(new BigDecimal("90000"));
        assertTrue(job.getMaxSalary().compareTo(job.getMinSalary()) > 0);
    }

    @Test
    @DisplayName("Test Required Skills Update")
    void testRequiredSkillsUpdate() {
        String newSkills = "Java, Spring Boot, MySQL, Docker";
        job.setRequiredSkills(newSkills);
        assertEquals(newSkills, job.getRequiredSkills());
    }
}
