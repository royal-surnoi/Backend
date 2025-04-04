package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.GeneralDiscussion;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.GeneralDiscussionRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.GeneralDiscussionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class GeneralDiscussionServiceTest {

    @InjectMocks
    private GeneralDiscussionService generalDiscussionService;

    @Mock
    private GeneralDiscussionRepo generalDiscussionRepo;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGeneralDiscussions() {
        List<GeneralDiscussion> discussions = new ArrayList<>();
        discussions.add(new GeneralDiscussion());
        when(generalDiscussionRepo.findAll()).thenReturn(discussions);

        List<GeneralDiscussion> result = generalDiscussionService.getAllGeneralDiscussions();

        assertEquals(discussions, result);
        verify(generalDiscussionRepo, times(1)).findAll();
    }

    @Test
    void testGetGeneralDiscussionById_Found() {
        GeneralDiscussion discussion = new GeneralDiscussion();
        when(generalDiscussionRepo.findById(anyLong())).thenReturn(Optional.of(discussion));

        Optional<GeneralDiscussion> result = generalDiscussionService.getGeneralDiscussionById(1L);

        assertTrue(result.isPresent());
        assertEquals(discussion, result.get());
        verify(generalDiscussionRepo, times(1)).findById(1L);
    }

    @Test
    void testGetGeneralDiscussionById_NotFound() {
        when(generalDiscussionRepo.findById(anyLong())).thenReturn(Optional.empty());

        Optional<GeneralDiscussion> result = generalDiscussionService.getGeneralDiscussionById(1L);

        assertFalse(result.isPresent());
        verify(generalDiscussionRepo, times(1)).findById(1L);
    }

    @Test
    void testCreateGeneralDiscussion_Success() {
        User user = new User();
        user.setId(1L);

        GeneralDiscussion discussion = new GeneralDiscussion();
        discussion.setDiscussionContent("Content");
        discussion.setReplyContent("Reply");

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(generalDiscussionRepo.save(any(GeneralDiscussion.class))).thenReturn(discussion);

        GeneralDiscussion result = generalDiscussionService.createGeneralDiscussion(1L, discussion);

        assertEquals(discussion, result);
        verify(userRepo, times(1)).findById(1L);
        verify(generalDiscussionRepo, times(1)).save(discussion);
    }

    @Test
    void testCreateGeneralDiscussion_UserNotFound() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        GeneralDiscussion discussion = new GeneralDiscussion();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            generalDiscussionService.createGeneralDiscussion(1L, discussion);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepo, times(1)).findById(1L);
        verify(generalDiscussionRepo, times(0)).save(any(GeneralDiscussion.class));
    }

    @Test
    void testUpdateGeneralDiscussion_Success() {
        GeneralDiscussion existingDiscussion = new GeneralDiscussion();
        existingDiscussion.setDiscussionContent("Old Content");
        existingDiscussion.setReplyContent("Old Reply");

        GeneralDiscussion updatedDiscussion = new GeneralDiscussion();
        updatedDiscussion.setDiscussionContent("New Content");
        updatedDiscussion.setReplyContent("New Reply");

        when(generalDiscussionRepo.findById(anyLong())).thenReturn(Optional.of(existingDiscussion));
        when(generalDiscussionRepo.save(any(GeneralDiscussion.class))).thenReturn(existingDiscussion);

        GeneralDiscussion result = generalDiscussionService.updateGeneralDiscussion(1L, updatedDiscussion);

        assertEquals("New Content", result.getDiscussionContent());
        assertEquals("New Reply", result.getReplyContent());
        verify(generalDiscussionRepo, times(1)).findById(1L);
        verify(generalDiscussionRepo, times(1)).save(existingDiscussion);
    }

    @Test
    void testUpdateGeneralDiscussion_NotFound() {
        when(generalDiscussionRepo.findById(anyLong())).thenReturn(Optional.empty());

        GeneralDiscussion updatedDiscussion = new GeneralDiscussion();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            generalDiscussionService.updateGeneralDiscussion(1L, updatedDiscussion);
        });

        assertEquals("GeneralDiscussion not found", exception.getMessage());
        verify(generalDiscussionRepo, times(1)).findById(1L);
        verify(generalDiscussionRepo, times(0)).save(any(GeneralDiscussion.class));
    }

    @Test
    void testDeleteGeneralDiscussion_Success() {
        doNothing().when(generalDiscussionRepo).deleteById(anyLong());

        generalDiscussionService.deleteGeneralDiscussion(1L);

        verify(generalDiscussionRepo, times(1)).deleteById(1L);
    }
}
