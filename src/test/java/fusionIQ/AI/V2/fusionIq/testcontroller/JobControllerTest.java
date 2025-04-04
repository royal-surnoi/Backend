package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.JobController;
import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import fusionIQ.AI.V2.fusionIq.data.Recruiter;
import fusionIQ.AI.V2.fusionIq.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RestController
@RequestMapping("/api/test/jobs")
class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

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
        testJob.setStatus("open");

        testAdmin = new JobAdmin();
        testAdmin.setId(1L);

        testRecruiter = new Recruiter();
        testRecruiter.setId(1L);
    }

    @Test
    void createJob_Success() {
        // Arrange
        when(jobService.createJob(anyLong(), anyLong(), any(Job.class))).thenReturn(testJob);

        // Act
        ResponseEntity<Job> response = jobController.createJob(1L, 1L, testJob);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Software Engineer", response.getBody().getJobTitle());
    }

    @Test
    void getJobsByRecruiterId_Success() {
        // Arrange
        List<Job> expectedJobs = Arrays.asList(testJob);
        when(jobService.getJobsByRecruiterId(1L)).thenReturn(expectedJobs);

        // Act
        List<Job> actualJobs = jobController.getJobsByRecruiterId(1L);

        // Assert
        assertEquals(expectedJobs.size(), actualJobs.size());
        assertEquals(expectedJobs.get(0).getJobTitle(), actualJobs.get(0).getJobTitle());
    }

    @Test
    void deleteJob_Success() {
        // Arrange
        doNothing().when(jobService).deleteJob(anyLong(), anyLong(), anyLong());

        // Act
        ResponseEntity<String> response = jobController.deleteJob(1L, 1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Job deleted successfully.", response.getBody());
    }

    @Test
    void updateJob_Success() {
        // Arrange
        when(jobService.updateJob(anyLong(), anyLong(), any(Job.class))).thenReturn(testJob);

        // Act
        ResponseEntity<Job> response = jobController.updateJob(1L, 1L, testJob);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getActiveJobs_Success() {
        // Arrange
        List<Job> expectedJobs = Arrays.asList(testJob);
        when(jobService.getActiveJobs()).thenReturn(expectedJobs);

        // Act
        List<Job> actualJobs = jobController.getActiveJobs();

        // Assert
        assertEquals(expectedJobs.size(), actualJobs.size());
        assertEquals("open", actualJobs.get(0).getStatus());
    }
}