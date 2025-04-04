package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.SubmitAssignmentController;
import fusionIQ.AI.V2.fusionIq.data.SubmitAssignment;
import fusionIQ.AI.V2.fusionIq.service.SubmitAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SubmitAssignmentControllerTest {

    @InjectMocks
    private SubmitAssignmentController submitAssignmentController;

    @Mock
    private SubmitAssignmentService submitAssignmentService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(submitAssignmentController).build();
    }

    @Test
    void testAssignmentSubmit() throws Exception {
        SubmitAssignment mockAssignment = new SubmitAssignment();
        mockAssignment.setId(1L);

        when(submitAssignmentService.submitAssignment(anyLong(), anyLong(), any(), anyString()))
                .thenReturn(mockAssignment);

        MockMultipartFile file = new MockMultipartFile("file", "assignment.pdf", "application/pdf", "some data".getBytes());

        mockMvc.perform(multipart("/saveSubmitAssignment/1/1")
                        .file(file)
                        .param("userAssignmentAnswer", "Answer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(submitAssignmentService, times(1))
                .submitAssignment(anyLong(), anyLong(), any(MultipartFile.class), anyString());
    }

    @Test
    void testGetAllSubmission() throws Exception {
        when(submitAssignmentService.findAllSubmissions()).thenReturn(Collections.singletonList(new SubmitAssignment()));

        mockMvc.perform(get("/allAssignmentSubmissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        verify(submitAssignmentService, times(1)).findAllSubmissions();
    }

    @Test
    void testGetSubmissionById() throws Exception {
        SubmitAssignment mockAssignment = new SubmitAssignment();
        mockAssignment.setId(1L);

        when(submitAssignmentService.findSubmissionById(1L)).thenReturn(Optional.of(mockAssignment));

        mockMvc.perform(get("/getSubmissionBy/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(submitAssignmentService, times(1)).findSubmissionById(1L);
    }

    @Test
    void testDeleteSubmitAssignment() throws Exception {
        // Mocking the service layer to do nothing when delete is called
        doNothing().when(submitAssignmentService).deleteSubmitAssignment(1L);

        // Perform the delete request and assert the expected status code
        mockMvc.perform(delete("/deleteSubmissionAssignment/1"))
                .andExpect(status().isOk());  // Update expectation to 200 OK as per the controller's behavior

        // Verify that the service method was called once
        Mockito.verify(submitAssignmentService, Mockito.times(1)).deleteSubmitAssignment(1L);
    }

    @Test
    void testGetSubmitAssignmentByLessonId() throws Exception {
        when(submitAssignmentService.getSubmitAssignmentByLessonId(anyLong())).thenReturn(Collections.singletonList(new SubmitAssignment()));

        mockMvc.perform(get("/getSubmitAssignmentByLesson/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        verify(submitAssignmentService, times(1)).getSubmitAssignmentByLessonId(anyLong());
    }

    @Test
    void testAssignmentSubmitByModule() throws Exception {
        SubmitAssignment mockAssignment = new SubmitAssignment();
        mockAssignment.setId(1L);

        when(submitAssignmentService.submitAssignmentByModule(anyLong(), anyLong(), anyLong(), any(MultipartFile.class), anyString()))
                .thenReturn(mockAssignment);

        MockMultipartFile file = new MockMultipartFile("file", "assignment.pdf", "application/pdf", "some data".getBytes());

        mockMvc.perform(multipart("/saveSubmitAssignmentByModule/1/1/1")
                        .file(file)
                        .param("userAssignmentAnswer", "Answer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(submitAssignmentService, times(1))
                .submitAssignmentByModule(anyLong(), anyLong(), anyLong(), any(MultipartFile.class), anyString());
    }
}

