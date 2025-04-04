package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.RecruiterController;
import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import fusionIQ.AI.V2.fusionIq.data.Recruiter;
import fusionIQ.AI.V2.fusionIq.data.RecruiterResponse;
import fusionIQ.AI.V2.fusionIq.service.JobAdminService;
import fusionIQ.AI.V2.fusionIq.service.RecruiterService;
import fusionIQ.AI.V2.fusionIq.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecruiterController.class)
public class RecruiterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecruiterService recruiterService;

    @MockBean
    private JobAdminService jobAdminService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private RecruiterController recruiterController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testRegisterRecruiter() throws Exception {
//        JobAdmin jobAdmin = new JobAdmin();
//        jobAdmin.setId(1L);
//
//        Recruiter recruiter = new Recruiter();
//        recruiter.setId(1L);
//        recruiter.setRecruiterName("Test Recruiter");
//        recruiter.setRecruiterEmail("test@example.com");
//
//        when(jobAdminService.findById(anyLong())).thenReturn(jobAdmin);
//        when(recruiterService.registerRecruiter(any(Recruiter.class), any(JobAdmin.class))).thenReturn(recruiter);
//
//        mockMvc.perform(post("/api/recruiters/register/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"recruiterName\":\"Test Recruiter\",\"recruiterEmail\":\"test@example.com\"}"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{\"id\":1,\"recruiterName\":\"Test Recruiter\",\"recruiterEmail\":\"test@example.com\"}"));
//    }

    @Test
    public void testLoginRecruiter() throws Exception {
        Recruiter recruiter = new Recruiter();
        recruiter.setId(1L);
        recruiter.setRecruiterName("Test Recruiter");
        recruiter.setRecruiterEmail("test@example.com");
        recruiter.setRecruiterPassword("password");

        when(recruiterService.findRecruiterByEmail(anyString())).thenReturn(Optional.of(recruiter));
        when(tokenService.generateToken(anyString())).thenReturn("jwtToken");

        mockMvc.perform(post("/api/recruiters/login")
                        .param("recruiterEmail", "test@example.com")
                        .param("recruiterPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"jwtToken\",\"id\":1,\"recruiterName\":\"Test Recruiter\",\"recruiterEmail\":\"test@example.com\"}"));
    }

    @Test
    public void testGetAdminByRecruiterId() throws Exception {
        JobAdmin jobAdmin = new JobAdmin();
        jobAdmin.setId(1L);

        when(recruiterService.getAdminByRecruiterId(anyLong())).thenReturn(jobAdmin);

        mockMvc.perform(get("/api/recruiters/admins/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    public void testGetAdminAndRecruiterById() throws Exception {
        Recruiter recruiter = new Recruiter();
        recruiter.setId(1L);
        recruiter.setRecruiterName("Test Recruiter");
        recruiter.setRecruiterEmail("test@example.com");

        JobAdmin jobAdmin = new JobAdmin();
        jobAdmin.setId(1L);

        recruiter.setJobAdmin(jobAdmin);

        when(recruiterService.getRecruiterById(anyLong())).thenReturn(recruiter);

        Map<String, Object> response = new HashMap<>();
        response.put("recruiter", recruiter);
        response.put("jobAdmin", jobAdmin);

        mockMvc.perform(get("/api/recruiters/adminsRecruiters/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"recruiter\":{\"id\":1,\"recruiterName\":\"Test Recruiter\",\"recruiterEmail\":\"test@example.com\"},\"jobAdmin\":{\"id\":1}}"));
    }

    @Test
    public void testUpdateRecruiterDetails() throws Exception {
        JobAdmin jobAdmin = new JobAdmin();
        jobAdmin.setId(1L);

        Recruiter recruiter = new Recruiter();
        recruiter.setId(1L);
        recruiter.setRecruiterName("Updated Recruiter");
        recruiter.setRecruiterEmail("updated@example.com");

        when(jobAdminService.findById(anyLong())).thenReturn(jobAdmin);
        when(recruiterService.updateRecruiterDetails(anyLong(), any(Recruiter.class), any(JobAdmin.class))).thenReturn(recruiter);

        mockMvc.perform(put("/api/recruiters/updateRecruiterDetails/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recruiterName\":\"Updated Recruiter\",\"recruiterEmail\":\"updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"recruiterName\":\"Updated Recruiter\",\"recruiterEmail\":\"updated@example.com\"}"));
    }

    @Test
    public void testGetJobAdminIdByRecruiterId() throws Exception {
        when(recruiterService.getJobAdminIdByRecruiterId(anyLong())).thenReturn(1L);

        mockMvc.perform(get("/api/recruiters/1/jobAdminId"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void testGetRecruitersByJobAdminAndRole() throws Exception {
        Map<String, List<Map<String, Object>>> recruitersGrouped = new HashMap<>();
        recruitersGrouped.put("role", List.of(new HashMap<>()));

        when(recruiterService.getRecruitersByJobAdminAndRole(anyLong(), anyString())).thenReturn(recruitersGrouped);

        mockMvc.perform(get("/api/recruiters/by-job-admin/1")
                        .param("recruiterRole", "role"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"role\":[{}]}"));
    }
}
