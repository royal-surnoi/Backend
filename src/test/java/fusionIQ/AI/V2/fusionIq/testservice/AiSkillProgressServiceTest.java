package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.AiSkillProgress;
import fusionIQ.AI.V2.fusionIq.repository.AiSkillProgressRepo;
import fusionIQ.AI.V2.fusionIq.service.AiSkillProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AiSkillProgressServiceTest {

    @InjectMocks
    private AiSkillProgressService aiSkillProgressService;

    @Mock
    private AiSkillProgressRepo aiSkillProgressRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAISkill() {
        AiSkillProgress mockSkillProgress = new AiSkillProgress();
        mockSkillProgress.setId(1L);
        mockSkillProgress.setUserId(1L);

        when(aiSkillProgressRepo.save(mockSkillProgress)).thenReturn(mockSkillProgress);

        AiSkillProgress savedSkillProgress = aiSkillProgressService.saveAISkill(mockSkillProgress);

        assertEquals(mockSkillProgress, savedSkillProgress);
    }

    @Test
    void testGetAISkillsByUserId() {
        AiSkillProgress mockSkillProgress = new AiSkillProgress();
        mockSkillProgress.setId(1L);
        mockSkillProgress.setUserId(1L);

        List<AiSkillProgress> mockSkillProgressList = Arrays.asList(mockSkillProgress);

        when(aiSkillProgressRepo.findByUserId(1L)).thenReturn(mockSkillProgressList);

        List<AiSkillProgress> retrievedSkills = aiSkillProgressService.getAISkillsByUserId(1L);

        assertEquals(mockSkillProgressList, retrievedSkills);
    }
}
