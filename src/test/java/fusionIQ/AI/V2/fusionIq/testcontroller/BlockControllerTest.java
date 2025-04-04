package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.BlockController;
import fusionIQ.AI.V2.fusionIq.data.Block;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.BlockRepository;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
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

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlockController.class)
public class BlockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlockRepository blockRepo;

    @MockBean
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBlockUser_Success() throws Exception {
        User blocker = new User();
        blocker.setId(1L);

        User blocked = new User();
        blocked.setId(2L);

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(blocker));
        when(userRepo.findById(2L)).thenReturn(java.util.Optional.of(blocked));
        when(blockRepo.existsByBlockerAndBlocked(blocker, blocked)).thenReturn(false);

        mockMvc.perform(post("/block/block")
                        .param("blockerId", "1")
                        .param("blockedId", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User blocked successfully"));
    }

    @Test
    void testBlockUser_AlreadyBlocked() throws Exception {
        User blocker = new User();
        blocker.setId(1L);

        User blocked = new User();
        blocked.setId(2L);

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(blocker));
        when(userRepo.findById(2L)).thenReturn(java.util.Optional.of(blocked));
        when(blockRepo.existsByBlockerAndBlocked(blocker, blocked)).thenReturn(true);

        mockMvc.perform(post("/block/block")
                        .param("blockerId", "1")
                        .param("blockedId", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already blocked"));
    }



    @Test
    void testUnblockUser_Success() throws Exception {
        User blocker = new User();
        blocker.setId(1L);

        User blocked = new User();
        blocked.setId(2L);

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(blocker));
        when(userRepo.findById(2L)).thenReturn(java.util.Optional.of(blocked));
        when(blockRepo.existsByBlockerAndBlocked(blocker, blocked)).thenReturn(true);

        doNothing().when(blockRepo).deleteByBlockerAndBlocked(blocker, blocked);

        mockMvc.perform(delete("/block/unblock")
                        .param("blockerId", "1")
                        .param("blockedId", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User unblocked successfully"));
    }

    @Test
    void testUnblockUser_BlockRelationshipNotFound() throws Exception {
        User blocker = new User();
        blocker.setId(1L);

        User blocked = new User();
        blocked.setId(2L);

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(blocker));
        when(userRepo.findById(2L)).thenReturn(java.util.Optional.of(blocked));
        when(blockRepo.existsByBlockerAndBlocked(blocker, blocked)).thenReturn(false);

        mockMvc.perform(delete("/block/unblock")
                        .param("blockerId", "1")
                        .param("blockedId", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Block relationship not found"));
    }



}

