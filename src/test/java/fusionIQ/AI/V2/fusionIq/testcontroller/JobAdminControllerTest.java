//package fusionIQ.AI.V2.fusionIq.testcontroller;
//
//
//
//import fusionIQ.AI.V2.fusionIq.controller.JobAdminController;
//import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
//import fusionIQ.AI.V2.fusionIq.service.JobAdminService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//
//class JobAdminControllerTest {
//
//    @Mock
//    private JobAdminService jobAdminService;
//
//    @InjectMocks
//    private JobAdminController jobAdminController;
//
//    private JobAdmin testJobAdmin;
//    private MultipartFile testFile;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        testJobAdmin = new JobAdmin();
//        testJobAdmin.setId(1L);
//        testJobAdmin.setJobAdminName("Test Admin");
//        testJobAdmin.setJobAdminEmail("test@example.com");
//        testJobAdmin.setJobAdminPassword("password123");
//
//        testFile = new MockMultipartFile(
//                "test.jpg",
//                "test.jpg",
//                "image/jpeg",
//                "test data".getBytes()
//        );
//    }
//
//    @Test
//    void loginJobAdmin_Success() {
//        when(jobAdminService.loginJobAdmin("test@example.com", "password123"))
//                .thenReturn(testJobAdmin);
//
//        ResponseEntity<JobAdmin> response = jobAdminController.loginJobAdmin(
//                "test@example.com",
//                "password123"
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(testJobAdmin.getJobAdminEmail(), response.getBody().getJobAdminEmail());
//    }
//
//    @Test
//    void loginJobAdmin_Failure() {
//        when(jobAdminService.loginJobAdmin("test@example.com", "wrongpass"))
//                .thenThrow(new RuntimeException("Invalid credentials"));
//
//        ResponseEntity<JobAdmin> response = jobAdminController.loginJobAdmin(
//                "test@example.com",
//                "wrongpass"
//        );
//
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//        assertNull(response.getBody());
//    }
//
//    @Test
//    void registerJobAdmin_Success() {
//        when(jobAdminService.registerJobAdmin(
//                anyString(), anyString(), anyString(),
//                anyString(), anyString(), anyString()
//        )).thenReturn(testJobAdmin);
//
//        ResponseEntity<?> response = jobAdminController.registerJobAdmin(
//                "Test Admin",
//                "Test Company",
//                "test@example.com",
//                "password123",
//                "password123",
//                "ADMIN"
//        );
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody() instanceof JobAdmin);
//    }
//
//    @Test
//    void registerJobAdmin_Failure() {
//        when(jobAdminService.registerJobAdmin(
//                anyString(), anyString(), anyString(),
//                anyString(), anyString(), anyString()
//        )).thenThrow(new RuntimeException("Email already exists"));
//
//        ResponseEntity<?> response = jobAdminController.registerJobAdmin(
//                "Test Admin",
//                "Test Company",
//                "test@example.com",
//                "password123",
//                "password123",
//                "ADMIN"
//        );
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertTrue(response.getBody() instanceof Map);
//        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
//        assertEquals("Email already exists", errorResponse.get("error"));
//    }
//
//    @Test
//    void updateCompanyDetails_Success() throws IOException {
//        when(jobAdminService.updateCompanyDetails(
//                anyLong(), anyString(), anyString(), anyString(),
//                anyInt(), anyString(), anyString(), anyString(),
//                anyString(), any(MultipartFile.class), any(MultipartFile.class),
//                any(MultipartFile.class), any(MultipartFile.class), anyString(),
//                anyString(), anyString(), any(MultipartFile.class), any(MultipartFile.class),
//                any(MultipartFile.class), any(MultipartFile.class), any(MultipartFile.class)
//        )).thenReturn(testJobAdmin);
//
//        ResponseEntity<JobAdmin> response = jobAdminController.updateCompanyDetails(
//                1L, "Description", "IT", "www.test.com",
//                100, "Location", "License123", "GST123",
//                "CIN123", testFile, testFile, testFile, testFile,
//                "About", "1234567890", "Overview",
//                testFile, testFile, testFile, testFile, testFile
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//    }
//
//    @Test
//    void updateCompanyDetails_IOException() throws IOException {
//        when(jobAdminService.updateCompanyDetails(
//                anyLong(), anyString(), anyString(), anyString(),
//                anyInt(), anyString(), anyString(), anyString(),
//                anyString(), any(MultipartFile.class), any(MultipartFile.class),
//                any(MultipartFile.class), any(MultipartFile.class), anyString(),
//                anyString(), anyString(), any(MultipartFile.class), any(MultipartFile.class),
//                any(MultipartFile.class), any(MultipartFile.class), any(MultipartFile.class)
//        )).thenThrow(new IOException("File processing error"));
//
//        ResponseEntity<JobAdmin> response = jobAdminController.updateCompanyDetails(
//                1L, "Description", "IT", "www.test.com",
//                100, "Location", "License123", "GST123",
//                "CIN123", testFile, testFile, testFile, testFile,
//                "About", "1234567890", "Overview",
//                testFile, testFile, testFile, testFile, testFile
//        );
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertNull(response.getBody());
//    }
//
//    @Test
//    void getJobAdminById_Success() {
//        when(jobAdminService.getJobAdminWithSensitiveDataExcluded(1L))
//                .thenReturn(testJobAdmin);
//
//        ResponseEntity<JobAdmin> response = jobAdminController.getJobAdminById(1L);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(testJobAdmin.getId(), response.getBody().getId());
//    }
//
//    @Test
//    void getJobAdminById_NotFound() {
//        when(jobAdminService.getJobAdminWithSensitiveDataExcluded(999L))
//                .thenThrow(new RuntimeException("JobAdmin not found"));
//
//        ResponseEntity<JobAdmin> response = jobAdminController.getJobAdminById(999L);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertNull(response.getBody());
//    }
//
//    @Test
//    void updateCompanyLocation_Success() {
//        when(jobAdminService.updateCompanyLocation(1L, 40.7128, -74.0060))
//                .thenReturn(testJobAdmin);
//
//        ResponseEntity<JobAdmin> response = jobAdminController.updateCompanyLocation(
//                1L, 40.7128, -74.0060
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//    }
//
//    @Test
//    void updateCompanyLocation_NotFound() {
//        when(jobAdminService.updateCompanyLocation(999L, 40.7128, -74.0060))
//                .thenThrow(new RuntimeException("JobAdmin not found"));
//
//        ResponseEntity<JobAdmin> response = jobAdminController.updateCompanyLocation(
//                999L, 40.7128, -74.0060
//        );
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertNull(response.getBody());
//    }
//
//    @Test
//    void addNewAdmin_Success() {
//        JobAdmin newAdmin = new JobAdmin();
//        newAdmin.setJobAdminName("New Admin");
//
//        when(jobAdminService.addNewAdmin(1L, newAdmin)).thenReturn(newAdmin);
//
//        ResponseEntity<JobAdmin> response = jobAdminController.addNewAdmin(1L, newAdmin);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("New Admin", response.getBody().getJobAdminName());
//    }
//}
