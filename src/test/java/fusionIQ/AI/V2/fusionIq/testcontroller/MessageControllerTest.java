package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.MessageController;
import fusionIQ.AI.V2.fusionIq.data.Message;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMessageById() {
        Long messageId = 1L;
        Message message = new Message();
        message.setId(messageId);

        when(messageService.findMessageById(messageId)).thenReturn(Optional.of(message));

        ResponseEntity<Message> response = messageController.getMessageById(messageId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    public void testGetAllMessages() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message());

        when(messageService.findAllMessages()).thenReturn(messages);

        ResponseEntity<List<Message>> response = messageController.getAllMessages();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messages, response.getBody());
    }

    @Test
    public void testDeleteMessage() {
        Long messageId = 1L;

        doNothing().when(messageService).deleteMessage(messageId);

        ResponseEntity<Void> response = messageController.deleteMessage(messageId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(messageService, times(1)).deleteMessage(messageId);
    }

    @Test
    public void testGetMessagesBySender() {
        Long senderId = 1L;
        List<Message> messages = new ArrayList<>();
        messages.add(new Message());

        when(messageService.findMessagesBySender(senderId)).thenReturn(messages);

        ResponseEntity<List<Message>> response = messageController.getMessagesBySender(senderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messages, response.getBody());
    }




}
