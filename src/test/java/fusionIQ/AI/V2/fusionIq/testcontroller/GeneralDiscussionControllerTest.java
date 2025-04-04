package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.GeneralDiscussionController;
import fusionIQ.AI.V2.fusionIq.data.GeneralDiscussion;
import fusionIQ.AI.V2.fusionIq.service.GeneralDiscussionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GeneralDiscussionControllerTest {

    @Mock
    private GeneralDiscussionService generalDiscussionService;

    @InjectMocks
    private GeneralDiscussionController generalDiscussionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGeneralDiscussions() {
        GeneralDiscussion discussion1 = new GeneralDiscussion();
        GeneralDiscussion discussion2 = new GeneralDiscussion();
        List<GeneralDiscussion> discussions = Arrays.asList(discussion1, discussion2);

        when(generalDiscussionService.getAllGeneralDiscussions()).thenReturn(discussions);

        List<GeneralDiscussion> result = generalDiscussionController.getAllGeneralDiscussions();

        assertEquals(2, result.size());
        verify(generalDiscussionService, times(1)).getAllGeneralDiscussions();
    }

    @Test
    void testGetGeneralDiscussionById_Success() {
        Long discussionId = 1L;
        GeneralDiscussion discussion = new GeneralDiscussion();

        when(generalDiscussionService.getGeneralDiscussionById(discussionId)).thenReturn(Optional.of(discussion));

        ResponseEntity<GeneralDiscussion> response = generalDiscussionController.getGeneralDiscussionById(discussionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(discussion, response.getBody());
        verify(generalDiscussionService, times(1)).getGeneralDiscussionById(discussionId);
    }

    @Test
    void testGetGeneralDiscussionById_NotFound() {
        Long discussionId = 1L;

        when(generalDiscussionService.getGeneralDiscussionById(discussionId)).thenReturn(Optional.empty());

        ResponseEntity<GeneralDiscussion> response = generalDiscussionController.getGeneralDiscussionById(discussionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(generalDiscussionService, times(1)).getGeneralDiscussionById(discussionId);
    }


    @Test
    void testUpdateGeneralDiscussion_Success() {
        Long discussionId = 1L;
        GeneralDiscussion updatedDiscussion = new GeneralDiscussion();
        updatedDiscussion.setDiscussionContent("Updated Content");
        updatedDiscussion.setReplyContent("Updated Reply");

        when(generalDiscussionService.updateGeneralDiscussion(discussionId, updatedDiscussion)).thenReturn(updatedDiscussion);

        ResponseEntity<GeneralDiscussion> response = generalDiscussionController.updateGeneralDiscussion(discussionId, updatedDiscussion);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDiscussion, response.getBody());
        verify(generalDiscussionService, times(1)).updateGeneralDiscussion(discussionId, updatedDiscussion);
    }

    @Test
    void testDeleteGeneralDiscussion_Success() {
        Long discussionId = 1L;

        doNothing().when(generalDiscussionService).deleteGeneralDiscussion(discussionId);

        ResponseEntity<Void> response = generalDiscussionController.deleteGeneralDiscussion(discussionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(generalDiscussionService, times(1)).deleteGeneralDiscussion(discussionId);
    }
}
