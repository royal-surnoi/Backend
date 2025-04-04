//package fusionIQ.AI.V2.fusionIq.testservice;
//
//
//
//import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
//import fusionIQ.AI.V2.fusionIq.repository.JobAdminRepository;
//import fusionIQ.AI.V2.fusionIq.service.JobAdminService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class JobAdminServiceTest {
//
//    @Mock
//    private JobAdminRepository jobAdminRepository;
//
//    @InjectMocks
//    private JobAdminService jobAdminService;
//
//    public JobAdminServiceTest() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testLoginJobAdmin_Success() {
//        String email = "admin@example.com";
//        String password = "password123";
//        JobAdmin mockAdmin = new JobAdmin();
//        mockAdmin.setJobAdminEmail(email);
//        mockAdmin.setJobAdminPassword(password);
//
//        when(jobAdminRepository.findByJobAdminEmail(email)).thenReturn(Optional.of(mockAdmin));
//
//        JobAdmin result = jobAdminService.loginJobAdmin(email, password);
//
//        assertNotNull(result);
//        assertEquals(email, result.getJobAdminEmail());
//    }
//
//    @Test
//    void testLoginJobAdmin_Failure() {
//        String email = "admin@example.com";
//        String password = "wrongpassword";
//
//        when(jobAdminRepository.findByJobAdminEmail(email)).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () -> jobAdminService.loginJobAdmin(email, password));
//    }
//
//    @Test
//    void testRegisterJobAdmin_Success() {
//        String email = "newadmin@example.com";
//        String password = "password123";
//        JobAdmin newAdmin = new JobAdmin();
//        newAdmin.setJobAdminEmail(email);
//        newAdmin.setJobAdminPassword(password);
//
//        when(jobAdminRepository.findByJobAdminEmail(email)).thenReturn(Optional.empty());
//        when(jobAdminRepository.save(any(JobAdmin.class))).thenReturn(newAdmin);
//
//        JobAdmin result = jobAdminService.registerJobAdmin("Admin Name", "Company", email, password, "ROLE_ADMIN", password);
//
//        assertNotNull(result);
//        assertEquals(email, result.getJobAdminEmail());
//    }
//
//    @Test
//    void testUpdateCompanyLocation_Success() {
//        Long jobAdminId = 1L;
//        double latitude = 40.7128;
//        double longitude = -74.0060;
//
//        JobAdmin mockAdmin = new JobAdmin();
//        mockAdmin.setId(jobAdminId);
//
//        when(jobAdminRepository.findById(jobAdminId)).thenReturn(Optional.of(mockAdmin));
//        when(jobAdminRepository.save(mockAdmin)).thenReturn(mockAdmin);
//
//        JobAdmin result = jobAdminService.updateCompanyLocation(jobAdminId, latitude, longitude);
//
//        assertNotNull(result);
//        assertEquals(latitude, result.getCompanyLatitude());
//        assertEquals(longitude, result.getCompanyLongitude());
//    }
//}
//
