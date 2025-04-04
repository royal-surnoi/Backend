package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.ApplyJob;
import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.ShortlistedCandidates;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.ApplyJobRepository;
import fusionIQ.AI.V2.fusionIq.repository.ShortlistedCandidatesRepository;
import fusionIQ.AI.V2.fusionIq.service.EmailSenderService;
import fusionIQ.AI.V2.fusionIq.service.ShortlistedCandidatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ShortlistedCandidatesServiceTest {

    @Mock
    private ShortlistedCandidatesRepository shortlistedCandidatesRepository;

    @Mock
    private ApplyJobRepository applyJobRepository;

    @Mock
    private EmailSenderService emailSenderService;

    private ShortlistedCandidatesService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ShortlistedCandidatesService(shortlistedCandidatesRepository, applyJobRepository);
    }

    @Test
    void shortlistCandidate_Success() {
        // Arrange
        Long jobId = 1L;
        Long applicantId = 1L;
        String status = "Shortlisted";

        ApplyJob mockApplyJob = createMockApplyJob();
        when(applyJobRepository.findById(applicantId)).thenReturn(Optional.of(mockApplyJob));
        when(shortlistedCandidatesRepository.findByJobIdAndApplyJobId(jobId, applicantId))
                .thenReturn(Optional.empty());
        when(shortlistedCandidatesRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        ShortlistedCandidates result = service.shortlistCandidate(jobId, applicantId, status);

        // Assert
        assertNotNull(result);
        assertEquals(status, result.getStatus());
        verify(applyJobRepository).save(mockApplyJob);
    }

    @Test
    void shortlistMultipleCandidates_Success() {
        // Arrange
        Long jobId = 1L;
        List<Long> applicantIds = Arrays.asList(1L, 2L);
        String status = "Shortlisted";

        ApplyJob mockApplyJob = createMockApplyJob();
        when(applyJobRepository.findById(anyLong())).thenReturn(Optional.of(mockApplyJob));
        when(shortlistedCandidatesRepository.findByJobIdAndApplyJobId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());
        when(shortlistedCandidatesRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Map<String, Object> result = service.shortlistMultipleCandidates(jobId, applicantIds, status);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("success"));
        assertTrue(result.containsKey("failed"));
    }

    @Test
    void getShortlistedCandidatesWithDetailsByJobId_Success() {
        // Arrange
        Long jobId = 1L;
        ShortlistedCandidates mockCandidate = createMockShortlistedCandidate();
        when(shortlistedCandidatesRepository.findByJobId(jobId))
                .thenReturn(Arrays.asList(mockCandidate));

        // Act
        List<Map<String, Object>> result = service.getShortlistedCandidatesWithDetailsByJobId(jobId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    private ApplyJob createMockApplyJob() {
        ApplyJob applyJob = new ApplyJob();
        applyJob.setId(1L);

        Job job = new Job();
        job.setId(1L);
        applyJob.setJob(job);

        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        applyJob.setUser(user);

        return applyJob;
    }

    private ShortlistedCandidates createMockShortlistedCandidate() {
        ShortlistedCandidates candidate = new ShortlistedCandidates();
        candidate.setId(1L);
        candidate.setStatus("Shortlisted");
        candidate.setShortlistedAt(LocalDateTime.now());
        candidate.setApplyJob(createMockApplyJob());
        candidate.setJob(createMockApplyJob().getJob());
        candidate.setUser(createMockApplyJob().getUser());
        return candidate;
    }
}
