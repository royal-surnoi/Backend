package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import fusionIQ.AI.V2.fusionIq.data.Recruiter;
import fusionIQ.AI.V2.fusionIq.repository.JobAdminRepository;
import fusionIQ.AI.V2.fusionIq.repository.JobRepository;
import fusionIQ.AI.V2.fusionIq.repository.RecruiterRepository;
import fusionIQ.AI.V2.fusionIq.repository.SavedJobsRepository;
import fusionIQ.AI.V2.fusionIq.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private JobAdminRepository adminRepository;

    @Mock
    private SavedJobsRepository savedJobsRepository;

    @InjectMocks
    private JobService jobService;

    private Job testJob;
    private JobAdmin testAdmin;
    private Recruiter testRecruiter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        testJob = new Job();
        testJob.setId(1L);
        testJob.setJobTitle("Software Engineer");
        testJob.setJobDescription("Java Developer position");
        testJob.setRequiredSkills("Java, Spring Boot");
        testJob.setLocation("New York");
        testJob.setMinSalary(new BigDecimal("70000"));
        testJob.setMaxSalary(new BigDecimal("100000"));
        testJob.setStatus("open");
        testJob.setVacancyCount(2);

        testAdmin = new JobAdmin();
        testAdmin.setId(1L);
        testAdmin.setJobAdminName("Test Admin");

        testRecruiter = new Recruiter();
        testRecruiter.setId(1L);
        testRecruiter.setRecruiterName("Test Recruiter");
    }

    @Test
    void createJob_Success() {
        // Arrange
        when(adminRepository.findById(1L)).thenReturn(Optional.of(testAdmin));
        when(recruiterRepository.findById(1L)).thenReturn(Optional.of(testRecruiter));
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);

        // Act
        Job createdJob = jobService.createJob(1L, 1L, testJob);

        // Assert
        assertNotNull(createdJob);
        assertEquals("Software Engineer", createdJob.getJobTitle());
        verify(jobRepository).save(any(Job.class));
    }

    @Test
    void getJobsByRecruiterId_Success() {
        // Arrange
        List<Job> expectedJobs = Arrays.asList(testJob);
        when(jobRepository.findByRecruiterId(1L)).thenReturn(expectedJobs);

        // Act
        List<Job> actualJobs = jobService.getJobsByRecruiterId(1L);

        // Assert
        assertEquals(expectedJobs.size(), actualJobs.size());
        assertEquals(expectedJobs.get(0).getJobTitle(), actualJobs.get(0).getJobTitle());
    }

    @Test
    void getActiveJobs_Success() {
        // Arrange
        List<Job> expectedJobs = Arrays.asList(testJob);
        when(jobRepository.findAll()).thenReturn(expectedJobs);

        // Act
        List<Job> actualJobs = jobService.getActiveJobs();

        // Assert
        assertEquals(expectedJobs.size(), actualJobs.size());
        assertTrue(actualJobs.get(0).getVacancyCount() > 0);
        assertEquals("open", actualJobs.get(0).getStatus());
    }

    @Test
    void updateJob_Success() {
        // Arrange
        when(adminRepository.findById(1L)).thenReturn(Optional.of(testAdmin));
        when(jobRepository.findById(1L)).thenReturn(Optional.of(testJob));
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);

        Job updatedJob = new Job();
        updatedJob.setJobTitle("Senior Software Engineer");

        // Act
        Job result = jobService.updateJob(1L, 1L, updatedJob);

        // Assert
        assertNotNull(result);
        verify(jobRepository).save(any(Job.class));
    }

    @Test
    void deleteJob_Success() {
        // Arrange
        when(adminRepository.findById(1L)).thenReturn(Optional.of(testAdmin));
        when(jobRepository.findById(1L)).thenReturn(Optional.of(testJob));

        // Act & Assert
        assertDoesNotThrow(() -> jobService.deleteJob(1L, 1L));
        verify(jobRepository).delete(any(Job.class));
    }

    @Test
    void closeJob_Success() {
        // Arrange
        testJob.setRecruiter(testRecruiter);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(testJob));
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);

        // Act
        boolean result = jobService.closeJobByRecruiterIdAndJobId(1L, 1L);

        // Assert
        assertTrue(result);
        assertEquals("closed", testJob.getStatus());
    }
}