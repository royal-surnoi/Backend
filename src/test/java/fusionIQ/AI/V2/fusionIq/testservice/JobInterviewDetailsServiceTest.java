//package fusionIQ.AI.V2.fusionIq.testservice;
//
//import fusionIQ.AI.V2.fusionIq.data.Job;
//import fusionIQ.AI.V2.fusionIq.data.JobInterviewDetails;
//import fusionIQ.AI.V2.fusionIq.data.ShortlistedCandidates;
//import fusionIQ.AI.V2.fusionIq.repository.JobInterviewDetailsRepo;
//import fusionIQ.AI.V2.fusionIq.repository.JobRepository;
//import fusionIQ.AI.V2.fusionIq.repository.ShortlistedCandidatesRepository;
//import fusionIQ.AI.V2.fusionIq.service.EmailSenderService;
//import fusionIQ.AI.V2.fusionIq.service.JobInterviewDetailsService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import jakarta.persistence.EntityNotFoundException;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class JobInterviewDetailsServiceTest {
//
//    @Mock
//    private JobInterviewDetailsRepo jobInterviewDetailsRepo;
//
//    @Mock
//    private JobRepository jobRepository;
//
//    @Mock
//    private ShortlistedCandidatesRepository shortlistedCandidatesRepository;
//
//    @Mock
//    private EmailSenderService emailSenderService;
//
//    @InjectMocks
//    private JobInterviewDetailsService jobInterviewDetailsService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testSaveShortlistDetails() {
//        // Mock data
//        Long jobId = 1L;
//        Long userId = 100L;
//        String userName = "John Doe";
//        String userEmail = "john.doe@example.com";
//        String feedback = "Excellent candidate";
//
//        Job job = new Job();
//        job.setId(jobId);
//        job.setNumberOfLevels(3);
//
//        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
//        when(jobInterviewDetailsRepo.save(any(JobInterviewDetails.class))).thenAnswer(i -> i.getArgument(0));
//
//        // Test
//        JobInterviewDetails result = jobInterviewDetailsService.saveShortlistDetails(
//                userId, userName, userEmail,
//                null, null, null,
//                feedback, jobId,
//                null, null, null
//        );
//
//        // Verify
//        assertNotNull(result);
//        assertEquals(userId, result.getUserId());
//        assertEquals(userName, result.getUserName());
//        assertEquals(userEmail, result.getUserEmail());
//        assertEquals(feedback, result.getCurrentFeedback());
//        assertEquals("level-1", result.getCurrentLevel());
//        assertEquals("level-2", result.getUpcomingLevel());
//
//        verify(jobRepository, times(1)).findById(jobId);
//        verify(jobInterviewDetailsRepo, times(1)).save(any(JobInterviewDetails.class));
//    }
//
//    @Test
//    void testUpdateShortlistedStatus() {
//        // Mock data
//        Long userId = 100L;
//        Long jobId = 1L;
//
//        Job job = new Job();
//        job.setId(jobId);
//        job.setNumberOfLevels(2);
//
//        ShortlistedCandidates candidate = new ShortlistedCandidates();
//        candidate.setUser(userId);
//        candidate.setStatus("level-1");
//
//        JobInterviewDetails interviewDetails = new JobInterviewDetails();
//        interviewDetails.setUserId(userId);
//        interviewDetails.setCurrentLevel("level-1");
//
//        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
//        when(shortlistedCandidatesRepository.findByUserId(userId)).thenReturn(List.of(candidate));
//        when(jobInterviewDetailsRepo.findByUserId(userId)).thenReturn(List.of(interviewDetails));
//
//        // Test
//        jobInterviewDetailsService.updateShortlistedStatus(userId, "level-1", jobId);
//
//        // Verify
//        assertEquals("Completed", candidate.getStatus());
//        assertEquals("Completed", interviewDetails.getUpcomingLevel());
//
//        verify(shortlistedCandidatesRepository, times(1)).findByUserId(userId);
//        verify(jobInterviewDetailsRepo, times(1)).findByUserId(userId);
//        verify(shortlistedCandidatesRepository, times(1)).saveAll(anyList());
//        verify(jobInterviewDetailsRepo, times(1)).saveAll(anyList());
//    }
//
//    @Test
//    void testUpdateInterviewDetails() {
//        // Mock data
//        Long interviewId = 1L;
//        Long jobId = 2L;
//
//        Job job = new Job();
//        job.setId(jobId);
//
//        JobInterviewDetails existingDetails = new JobInterviewDetails();
//        existingDetails.setId(interviewId);
//        existingDetails.setJob(job);
//        existingDetails.setUserId(100L);
//        existingDetails.setRecruiterId(200L);
//
//        JobInterviewDetails updatedDetails = new JobInterviewDetails();
//        updatedDetails.setInterviewDescription("Updated Description");
//        updatedDetails.setInterviewerEmail("interviewer@example.com");
//        updatedDetails.setInterviewerName("Jane Smith");
//
//        when(jobInterviewDetailsRepo.findById(interviewId)).thenReturn(Optional.of(existingDetails));
//        when(jobInterviewDetailsRepo.save(any(JobInterviewDetails.class))).thenAnswer(i -> i.getArgument(0));
//
//        // Test
//        JobInterviewDetails result = jobInterviewDetailsService.updateInterviewDetails(interviewId, updatedDetails);
//
//        // Verify
//        assertNotNull(result);
//        assertEquals("Updated Description", result.getInterviewDescription());
//        assertEquals("interviewer@example.com", result.getInterviewerEmail());
//        assertEquals("Jane Smith", result.getInterviewerName());
//
//        verify(jobInterviewDetailsRepo, times(1)).findById(interviewId);
//        verify(jobInterviewDetailsRepo, times(1)).save(any(JobInterviewDetails.class));
//    }
//
//    @Test
//    void testSaveScoreAndFeedback() {
//        // Mock data
//        Long interviewId = 1L;
//        Integer score = 85;
//        String feedback = "Great performance";
//
//        JobInterviewDetails interviewDetails = new JobInterviewDetails();
//        interviewDetails.setId(interviewId);
//
//        when(jobInterviewDetailsRepo.findById(interviewId)).thenReturn(Optional.of(interviewDetails));
//        when(jobInterviewDetailsRepo.save(any(JobInterviewDetails.class))).thenAnswer(i -> i.getArgument(0));
//
//        // Test
//        JobInterviewDetails result = jobInterviewDetailsService.saveScoreAndFeedback(interviewId, score, feedback);
//
//        // Verify
//        assertNotNull(result);
//        assertEquals(score, result.getInterviewerScore());
//        assertEquals(feedback, result.getInterviewerFeedback());
//
//        verify(jobInterviewDetailsRepo, times(1)).findById(interviewId);
//        verify(jobInterviewDetailsRepo, times(1)).save(any(JobInterviewDetails.class));
//    }
//}
