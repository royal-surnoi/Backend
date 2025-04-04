//package fusionIQ.AI.V2.fusionIq.testcontroller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import fusionIQ.AI.V2.fusionIq.controller.JobInterviewDetailsController;
//import fusionIQ.AI.V2.fusionIq.data.Job;
//import fusionIQ.AI.V2.fusionIq.data.JobInterviewDetails;
//import fusionIQ.AI.V2.fusionIq.repository.JobInterviewDetailsRepo;
//import fusionIQ.AI.V2.fusionIq.service.JobInterviewDetailsService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//
//@WebMvcTest(JobInterviewDetailsController.class)
//public class JobInterviewDetailsControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private JobInterviewDetailsService jobInterviewDetailsService;
//
//    @Mock
//    private JobInterviewDetailsRepo jobInterviewDetailsRepo;
//
//    @InjectMocks
//    private JobInterviewDetailsController jobInterviewDetailsController;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    public JobInterviewDetailsControllerTest() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testShortlistCandidate_WithRecruiterDetails() throws Exception {
//        JobInterviewDetails details = new JobInterviewDetails();
//        details.setUserId(1L);
//        details.setUserName("John Doe");
//        details.setUserEmail("john.doe@example.com");
//        details.setRecruiterId(2L);
//        details.setRecruiterName("Recruiter Name");
//        details.setRecruiterEmail("recruiter@example.com");
//        Job job = new Job();
//        job.setId(3L);
//        details.setJob(job);
//
//        when(jobInterviewDetailsService.saveShortlistDetails(
//                details.getUserId(), details.getUserName(), details.getUserEmail(),
//                details.getRecruiterId(), details.getRecruiterName(), details.getRecruiterEmail(),
//                null, details.getJob().getId(), null, null, null))
//                .thenReturn(details);
//
//        mockMvc.perform(post("/api/interviews/shortlist")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(details)))
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//    }
//
//    @Test
//    void testUpdateInterviewDetails() throws Exception {
//        JobInterviewDetails details = new JobInterviewDetails();
//        details.setId(1L);
//        details.setUserId(2L);
//        details.setUserName("Jane Doe");
//        details.setInterviewDescription("Updated Interview");
//        details.setInterviewerEmail("interviewer@example.com");
//        details.setInterviewerName("Interviewer");
//        Job job = new Job();
//        job.setId(4L);
//        details.setJob(job);
//
//        when(jobInterviewDetailsService.updateInterviewDetails(eq(1L), any(JobInterviewDetails.class)))
//                .thenReturn(details);
//
//        mockMvc.perform(put("/api/interviews/update/{id}", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(details)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("Jane Doe"));
//    }
//
//    @Test
//    void testGetInterviewDetailsByJobIdAndUserId() throws Exception {
//        JobInterviewDetails details = new JobInterviewDetails();
//        details.setUserId(2L);
//        details.setJob(new Job());
//        details.getJob().setId(3L);
//
//        when(jobInterviewDetailsRepo.findByJobIdAndUserId(3L, 2L)).thenReturn(List.of(details));
//
//        mockMvc.perform(get("/api/interviews/interviewDetails/{jobId}/user/{userId}", 3, 2))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(2L));
//    }
//
//    @Test
//    void testSaveScoreAndFeedback() throws Exception {
//        JobInterviewDetails details = new JobInterviewDetails();
//        details.setId(1L);
//        details.setInterviewerScore(85);
//        details.setInterviewerFeedback("Great candidate!");
//
//        when(jobInterviewDetailsRepo.findById(1L)).thenReturn(Optional.of(details));
//        when(jobInterviewDetailsRepo.save(any(JobInterviewDetails.class))).thenReturn(details);
//
//        mockMvc.perform(post("/api/interviews/save-score-feedback")
//                        .param("interviewId", "1")
//                        .param("interviewerScore", "85")
//                        .param("interviewerFeedback", "Great candidate!"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("Interview score and feedback saved successfully."));
//    }
//
//    @Test
//    void testGetScoreAndFeedback() throws Exception {
//        JobInterviewDetails details = new JobInterviewDetails();
//        details.setId(1L);
//        details.setInterviewerScore(90);
//        details.setInterviewerFeedback("Excellent candidate!");
//
//        when(jobInterviewDetailsRepo.findById(1L)).thenReturn(Optional.of(details));
//
//        mockMvc.perform(get("/api/interviews/score-feedback/{interviewId}", 1))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.interviewerScore").value(90));
//    }
//}
