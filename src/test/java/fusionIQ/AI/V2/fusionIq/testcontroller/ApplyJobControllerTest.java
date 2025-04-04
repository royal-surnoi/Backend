package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.ApplyJobController;
import fusionIQ.AI.V2.fusionIq.data.ApplyJob;
import fusionIQ.AI.V2.fusionIq.service.ApplyJobService;
import fusionIQ.AI.V2.fusionIq.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class ApplyJobControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ApplyJobService applyJobService;

    @Mock
    private JobService jobService;

    @InjectMocks
    private ApplyJobController applyJobController;

    private ApplyJob testApplication;
    private MockMultipartFile testResume;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        // Manually set the jobService after mocks are initialized
        ReflectionTestUtils.setField(applyJobController, "jobService", jobService);

        mockMvc = MockMvcBuilders.standaloneSetup(applyJobController).build();

        testApplication = new ApplyJob();
        testApplication.setId(1L);
        testApplication.setStatus("applied");

        testResume = new MockMultipartFile(
                "resume",
                "resume.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "test resume content".getBytes()
        );
    }


    @Test
    void testApplyJob() throws Exception {
        when(applyJobService.applyJob(anyLong(), anyLong(), anyString(), any(byte[].class)))
                .thenReturn(testApplication);

        ApplyJob result = applyJobController.applyJob(1L, 1L, "applied", testResume);

        assertNotNull(result);
        assertEquals(testApplication.getId(), result.getId());
        assertEquals(testApplication.getStatus(), result.getStatus());
        verify(applyJobService).applyJob(eq(1L), eq(1L), eq("applied"), any(byte[].class));
    }

    @Test
    void testGetApplicationsByUserId() {
        List<ApplyJob> applications = List.of(testApplication);
        when(applyJobService.getApplicationsByUserId(1L)).thenReturn(applications);

        List<ApplyJob> result = applyJobController.getApplicationsByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testApplication.getId(), result.getFirst().getId());
        verify(applyJobService).getApplicationsByUserId(1L);
    }

    @Test
    void testGetApplicationsByJobId() {
        Map<String, Object> applicationData = new HashMap<>();
        applicationData.put("applications", List.of(testApplication));
        when(applyJobService.getApplicationsByJobId(1L)).thenReturn(applicationData);

        Map<String, Object> result = applyJobController.getApplicationsByJobId(1L);

        assertNotNull(result);
        assertTrue(result.containsKey("applications"));
        verify(applyJobService).getApplicationsByJobId(1L);
    }

    @Test
    void testUpdateApplicationStatus() {
        when(applyJobService.updateApplicationStatus(1L, "accepted"))
                .thenReturn(testApplication);

        ApplyJob result = applyJobController.updateApplicationStatus(1L, "accepted");

        assertNotNull(result);
        assertEquals(testApplication.getId(), result.getId());
        verify(applyJobService).updateApplicationStatus(1L, "accepted");
    }

    @Test
    void testGetApplicationStatus_Found() {
        when(applyJobService.getApplicationByJobAndUser(1L, 1L))
                .thenReturn(testApplication);

        String result = applyJobController.getApplicationStatus(1L, 1L);

        assertEquals("applied", result);
        verify(applyJobService).getApplicationByJobAndUser(1L, 1L);
    }

    @Test
    void testGetApplicationStatus_NotFound() {
        when(applyJobService.getApplicationByJobAndUser(1L, 1L))
                .thenReturn(null);

        String result = applyJobController.getApplicationStatus(1L, 1L);

        assertEquals("No application found for the provided job and user.", result);
        verify(applyJobService).getApplicationByJobAndUser(1L, 1L);
    }

    @Test
    void testGetApplicationsWithEducationByJobId() {
        Map<String, Object> applicationData = new HashMap<>();
        applicationData.put("applications", List.of(testApplication));
        when(applyJobService.getApplicationsWithEducationByJobId(1L))
                .thenReturn(applicationData);

        Map<String, Object> result = applyJobController.getApplicationsWithEducationByJobId(1L);

        assertNotNull(result);
        assertTrue(result.containsKey("applications"));
        verify(applyJobService).getApplicationsWithEducationByJobId(1L);
    }

    @Test
    void testDeleteApplication_Success() {
        doNothing().when(applyJobService).deleteApplication(1L);

        String result = applyJobController.deleteApplication(1L);

        assertEquals("Application with ID 1 has been successfully deleted.", result);
        verify(applyJobService).deleteApplication(1L);
    }

    @Test
    void testDeleteApplication_Failure() {
        doThrow(new IllegalArgumentException("Application not found"))
                .when(applyJobService).deleteApplication(1L);

        String result = applyJobController.deleteApplication(1L);

        assertEquals("Application not found", result);
        verify(applyJobService).deleteApplication(1L);
    }

    @Test
    void testUpdateWithdrawStatus() {
        when(applyJobService.updateWithdrawStatus(1L))
                .thenReturn(testApplication);

        ApplyJob result = applyJobController.updateWithdrawStatus(1L);

        assertNotNull(result);
        assertEquals(testApplication.getId(), result.getId());
        verify(applyJobService).updateWithdrawStatus(1L);
    }

    @Test
    void testGetActiveApplicationsByUserId() {
        List<ApplyJob> applications = List.of(testApplication);
        when(applyJobService.getActiveApplicationsByUserId(1L))
                .thenReturn(applications);

        List<ApplyJob> result = applyJobController.getActiveApplicationsByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testApplication.getId(), result.getFirst().getId());
        verify(applyJobService).getActiveApplicationsByUserId(1L);
    }

//    @Test
//    void testReapplyForJob() {
//        // Mock job service check
//        when(jobService.checkIfJobIsOpen(1L)).thenReturn(true);
//        // Mock the applyJobService method - using when() instead of doNothing()
//        ApplyJob mockedApplyJob = new ApplyJob();
//        when(applyJobService.reapplyForJob(1L)).thenReturn(mockedApplyJob);
//
//        String result = applyJobController.reapplyForJob(1L);
//
//        assertEquals("Reapplication was successful.", result);
//        verify(applyJobService).reapplyForJob(1L);
//        verify(jobService).checkIfJobIsOpen(1L);
//    }

    @Test
    void testIsJobOpenAndApplied() {
        when(jobService.checkIfJobIsOpen(1L)).thenReturn(true);
        when(applyJobService.Isapply(1L, 1L)).thenReturn(false);

        ResponseEntity<Map<String, Boolean>> response = applyJobController.isJobOpenAndApplied(1L, 1L);
        Map<String, Boolean> result = response.getBody();

        assertNotNull(result);
        assertTrue(result.get("isOpen"));
        assertFalse(result.get("isApplied"));
        verify(jobService).checkIfJobIsOpen(1L);
        verify(applyJobService).Isapply(1L, 1L);
    }

    @BeforeEach
    void tearDown() throws Exception {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }
}