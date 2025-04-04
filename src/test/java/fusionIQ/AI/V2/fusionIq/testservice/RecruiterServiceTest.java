//package fusionIQ.AI.V2.fusionIq.testservice;
//
//import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
//import fusionIQ.AI.V2.fusionIq.data.Recruiter;
//import fusionIQ.AI.V2.fusionIq.repository.RecruiterRepository;
//import fusionIQ.AI.V2.fusionIq.service.EmailSenderService;
//import fusionIQ.AI.V2.fusionIq.service.RecruiterService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//class RecruiterServiceTest {
//
//    @Mock
//    private RecruiterRepository recruiterRepository;
//
//    @Mock
//    private EmailSenderService emailSenderService;
//
//    private RecruiterService recruiterService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        recruiterService = new RecruiterService(recruiterRepository);
//        // Inject emailSenderService using ReflectionTestUtils
//        ReflectionTestUtils.setField(recruiterService, "emailSenderService", emailSenderService);
//    }
//
//    @Test
//    void testRegisterRecruiter_Success() {
//        // Create test data
//        Recruiter recruiter = new Recruiter();
//        recruiter.setRecruiterEmail("test@example.com");
//        recruiter.setRecruiterName("Test User");
//        recruiter.setRecruiterPassword("password123");
//
//        JobAdmin jobAdmin = new JobAdmin();
//        jobAdmin.setJobAdminCompanyName("Test Company");
//        jobAdmin.setJobAdminName("Admin User");
//
//        // Setup mock behavior
//        when(recruiterRepository.findByRecruiterEmail(recruiter.getRecruiterEmail()))
//                .thenReturn(Optional.empty());
//        when(recruiterRepository.save(any(Recruiter.class))).thenReturn(recruiter);
//
//        // Don't need to verify email content, just verify it was called
//        doNothing().when(emailSenderService).sendEmail(
//                anyString(),
//                anyString(),
//                anyString()
//        );
//
//        // Execute test
//        Recruiter result = recruiterService.registerRecruiter(recruiter, jobAdmin);
//
//        // Verify results
//        assertNotNull(result);
//        assertEquals(recruiter.getRecruiterEmail(), result.getRecruiterEmail());
//        verify(recruiterRepository).save(any(Recruiter.class));
//        verify(emailSenderService).sendEmail(
//                eq(recruiter.getRecruiterEmail()),
//                anyString(),
//                anyString()
//        );
//    }
//
////    @Test
////    void testRegisterRecruiter_EmailExists() {
////        Recruiter recruiter = new Recruiter();
////        recruiter.setRecruiterEmail("test@example.com");
////        JobAdmin jobAdmin = new JobAdmin();
////
////        when(recruiterRepository.findByRecruiterEmail(recruiter.getRecruiterEmail()))
////                .thenReturn(Optional.of(recruiter));
////
////        assertThrows(RuntimeException.class, () ->
////                recruiterService.registerRecruiter(recruiter, jobAdmin));
////
////        verify(emailSenderService, never()).sendEmail(
////                anyString(),
////                anyString(),
////                anyString()
////        );
////    }
//
//    @Test
//    void testLoginRecruiter_Success() {
//        String email = "test@example.com";
//        String password = "password";
//        Recruiter recruiter = new Recruiter();
//        recruiter.setRecruiterEmail(email);
//        recruiter.setRecruiterPassword(password);
//
//        when(recruiterRepository.findByRecruiterEmail(email))
//                .thenReturn(Optional.of(recruiter));
//
//        Recruiter result = recruiterService.loginRecruiter(email, password);
//
//        assertNotNull(result);
//        assertEquals(email, result.getRecruiterEmail());
//    }
//
//    @Test
//    void testLoginRecruiter_InvalidCredentials() {
//        String email = "test@example.com";
//        String password = "wrongpassword";
//
//        when(recruiterRepository.findByRecruiterEmail(email))
//                .thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () ->
//                recruiterService.loginRecruiter(email, password));
//    }
//
//    @Test
//    void testGetRecruitersByJobAdminAndRole() {
//        Long jobAdminId = 1L;
//        String role = "HR";
//        List<Recruiter> recruiters = new ArrayList<>();
//        Recruiter recruiter = new Recruiter();
//        recruiter.setRecruiterDeportment("IT");
//        recruiters.add(recruiter);
//
//        when(recruiterRepository.findByJobAdminIdAndRecruiterRole(jobAdminId, role))
//                .thenReturn(recruiters);
//
//        Map<String, List<Map<String, Object>>> result =
//                recruiterService.getRecruitersByJobAdminAndRole(jobAdminId, role);
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//    }
//
//    @Test
//    void testGetAdminByRecruiterId_Success() {
//        Long recruiterId = 1L;
//        Recruiter recruiter = new Recruiter();
//        JobAdmin jobAdmin = new JobAdmin();
//        recruiter.setJobAdmin(jobAdmin);
//
//        when(recruiterRepository.findById(recruiterId))
//                .thenReturn(Optional.of(recruiter));
//
//        JobAdmin result = recruiterService.getAdminByRecruiterId(recruiterId);
//
//        assertNotNull(result);
//        assertEquals(jobAdmin, result);
//    }
//
//    @Test
//    void testGetAdminByRecruiterId_NotFound() {
//        Long recruiterId = 1L;
//
//        when(recruiterRepository.findById(recruiterId))
//                .thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () ->
//                recruiterService.getAdminByRecruiterId(recruiterId));
//    }
//}