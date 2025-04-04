package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.SubmitProjectController;
import fusionIQ.AI.V2.fusionIq.service.SubmitProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubmitProjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubmitProjectService submitProjectService;

    @InjectMocks
    private SubmitProjectController submitProjectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(submitProjectController).build();
    }

    @Test
    void testDeleteSubmitProject() throws Exception {
        // Mocking the service to do nothing (i.e., simulate a successful deletion)
        doNothing().when(submitProjectService).deleteSubmitProject(1L);

        mockMvc.perform(delete("/deleteSubmissionProject/1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());  // Adjusted to expect 200 OK status
    }
}
