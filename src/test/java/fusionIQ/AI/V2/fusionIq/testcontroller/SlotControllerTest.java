package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.SlotController;
import fusionIQ.AI.V2.fusionIq.data.MockTestInterview;
import fusionIQ.AI.V2.fusionIq.data.Slot;
import fusionIQ.AI.V2.fusionIq.service.SlotService;
import fusionIQ.AI.V2.fusionIq.repository.MockTestInterviewRepository;
import fusionIQ.AI.V2.fusionIq.repository.SlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SlotControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SlotRepository slotRepository;

    @Mock
    private MockTestInterviewRepository mockTestInterviewRepository;

    @Mock
    private SlotService slotService;

    @InjectMocks
    private SlotController slotController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(slotController).build();
    }

    @Test
    public void testCreateSlot_Success() throws Exception {
        MockTestInterview interview = new MockTestInterview();
        interview.setId(1L);

        Slot slot = new Slot();
        slot.setMockTestInterview(interview);

        when(mockTestInterviewRepository.findById(1L)).thenReturn(Optional.of(interview));
        when(slotRepository.save(any(Slot.class))).thenReturn(slot);

        mockMvc.perform(post("/api/slots/saveSlot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mockTestInterview\": {\"id\": 1}}"))
                .andExpect(status().isOk());
    }



    @Test
    public void testGetAllSlotsByInterviewId_Success() throws Exception {
        Slot slot = new Slot();
        slot.setId(1L);

        when(slotService.findAllByInterviewId(1L)).thenReturn(List.of(slot));

        mockMvc.perform(get("/api/slots/1/slots"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testGetAllSlotsByInterviewId_NoContent() throws Exception {
        when(slotService.findAllByInterviewId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/slots/1/slots"))
                .andExpect(status().isNoContent());
    }

//    @Test
//    public void testGetAvailableSlots() throws Exception {
//        Slot slot = new Slot();
//        slot.setId(1L);
//
//        when(slotService.getAvailableSlots(1L)).thenReturn(List.of(slot));
//
//        mockMvc.perform(get("/api/slots/available/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[{\"id\":1}]"));
//    }
}
