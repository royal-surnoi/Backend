package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.AiSkillProgressController;
import fusionIQ.AI.V2.fusionIq.data.AiSkillProgress;
import fusionIQ.AI.V2.fusionIq.service.AiSkillProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest(AiSkillProgressController.class)
public class AiSkillProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AiSkillProgressService aiSkillProgressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testGetAISkillsByUserId() throws Exception {
        AiSkillProgress mockSkillProgress = new AiSkillProgress();
        mockSkillProgress.setId(1L);
        mockSkillProgress.setUserId(1L);

        List<AiSkillProgress> mockSkillProgressList = Arrays.asList(mockSkillProgress);

        when(aiSkillProgressService.getAISkillsByUserId(1L)).thenReturn(mockSkillProgressList);

        mockMvc.perform(get("/getSkill/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'userId':1}]"));
    }
}
