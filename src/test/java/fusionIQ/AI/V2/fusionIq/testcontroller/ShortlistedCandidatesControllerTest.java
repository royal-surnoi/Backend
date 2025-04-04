package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.ShortlistedCandidatesController;
import fusionIQ.AI.V2.fusionIq.data.ApplyJob;
import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.ShortlistedCandidates;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.ApplyJobRepository;
import fusionIQ.AI.V2.fusionIq.repository.ShortlistedCandidatesRepository;
import fusionIQ.AI.V2.fusionIq.service.ApplyJobService;
import fusionIQ.AI.V2.fusionIq.service.JobService;
import fusionIQ.AI.V2.fusionIq.service.ShortlistedCandidatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ShortlistedCandidatesControllerTest {

    @Mock
    private ShortlistedCandidatesService shortlistedCandidatesService;

    @Mock
    private ApplyJobService applyJobService;

    @Mock
    private JobService jobService;

    @Mock
    private ApplyJobRepository applyJobRepository;

    @Mock
    private ShortlistedCandidatesRepository shortlistedCandidatesRepository;

    private ShortlistedCandidatesController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ShortlistedCandidatesController(
                shortlistedCandidatesService,
                applyJobService,
                jobService
        );
    }

    @Test
    void shortlistCandidate_Success() {
        // Arrange
        Long jobId = 1L;
        Long applicantId = 1L;
        Map<String, String> request = new HashMap<>();
        request.put("status", "Shortlisted");

        ShortlistedCandidates mockCandidate = createMockShortlistedCandidate();
        when(shortlistedCandidatesService.shortlistCandidate(jobId, applicantId, "Shortlisted"))
                .thenReturn(mockCandidate);

        // Act
        ResponseEntity<Map<String, Object>> response = controller.shortlistCandidate(jobId, applicantId, request);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(mockCandidate.getId(), response.getBody().get("id"));
        assertEquals(mockCandidate.getStatus(), response.getBody().get("status"));
    }

    @Test
    void shortlistMultipleCandidates_Success() {
        // Arrange
        Long jobId = 1L;
        List<Long> applicantIds = Arrays.asList(1L, 2L);
        Map<String, Object> request = new HashMap<>();
        request.put("applicantIds", applicantIds);
        request.put("status", "Shortlisted");

        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("success", Arrays.asList(new HashMap<>()));
        mockResult.put("failed", new ArrayList<>());

        when(shortlistedCandidatesService.shortlistMultipleCandidates(eq(jobId), anyList(), anyString()))
                .thenReturn(mockResult);

        // Act
        ResponseEntity<Map<String, Object>> response = controller.shortlistMultipleCandidates(jobId, request);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
    }

    @Test
    void getShortlistedCandidatesByJobId_Success() {
        // Arrange
        Long jobId = 1L;
        List<Map<String, Object>> mockCandidates = Arrays.asList(new HashMap<>());
        when(shortlistedCandidatesService.getShortlistedCandidatesWithDetailsByJobId(jobId))
                .thenReturn(mockCandidates);

        // Act
        Map<String, Object> response = controller.getShortlistedCandidatesByJobId(jobId);

        // Assert
        assertNotNull(response);
        assertTrue(response.containsKey("shortlistedCandidates"));
    }

    private ShortlistedCandidates createMockShortlistedCandidate() {
        ShortlistedCandidates candidate = new ShortlistedCandidates();
        candidate.setId(1L);
        candidate.setStatus("Shortlisted");
        candidate.setShortlistedAt(LocalDateTime.now());

        Job job = new Job();
        job.setId(1L);
        candidate.setJob(job);

        ApplyJob applyJob = new ApplyJob();
        applyJob.setId(1L);
        candidate.setApplyJob(applyJob);

        User user = new User();
        user.setId(1L);
        candidate.setUser(user);

        candidate.setRecruiterId(1L);
        candidate.setAdminId(1L);

        return candidate;
    }
}

