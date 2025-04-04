
package fusionIQ.AI.V2.fusionIq.testservice;


import fusionIQ.AI.V2.fusionIq.data.Message;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.MessageRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MessageServiceTest {

    @Mock
    private MessageRepo messageRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveMessageSuccess() {
        Long senderId = 1L;
        Long receiverId = 2L;
        Message message = new Message();
        User sender = new User();
        User receiver = new User();

        when(userRepo.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepo.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(messageRepo.save(any(Message.class))).thenReturn(message);

        Message savedMessage = messageService.saveMessage(message, senderId, receiverId);

        assertEquals(message, savedMessage);
    }

    @Test
    public void testSaveMessageSenderNotFound() {
        Long senderId = 1L;
        Long receiverId = 2L;
        Message message = new Message();

        when(userRepo.findById(senderId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            messageService.saveMessage(message, senderId, receiverId);
        });
    }

    @Test
    public void testFindMessageById() {
        Long messageId = 1L;
        Message message = new Message();

        when(messageRepo.findById(messageId)).thenReturn(Optional.of(message));

        Optional<Message> foundMessage = messageService.findMessageById(messageId);

        assertEquals(Optional.of(message), foundMessage);
    }

    @Test
    public void testFindAllMessages() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message());

        when(messageRepo.findAll()).thenReturn(messages);

        List<Message> foundMessages = messageService.findAllMessages();

        assertEquals(messages, foundMessages);
    }

    @Test
    public void testDeleteMessage() {
        Long messageId = 1L;

        doNothing().when(messageRepo).deleteById(messageId);

        messageService.deleteMessage(messageId);

        verify(messageRepo, times(1)).deleteById(messageId);
    }

    @Test
    public void testFindMessagesBySender() {
        Long senderId = 1L;
        List<Message> messages = new ArrayList<>();
        messages.add(new Message());

        when(messageRepo.findBySenderId(senderId)).thenReturn(messages);

        List<Message> foundMessages = messageService.findMessagesBySender(senderId);

        assertEquals(messages, foundMessages);
    }

    @Test
    public void testFindMessagesByReceiver() {
        Long receiverId = 2L;
        List<Message> messages = new ArrayList<>();
        messages.add(new Message());

        when(messageRepo.findByReceiverId(receiverId)).thenReturn(messages);

        List<Message> foundMessages = messageService.findMessagesByReceiver(receiverId);

        assertEquals(messages, foundMessages);
    }

    @Test
    public void testFindMessagesBetweenUsers() {
        Long senderId = 1L;
        Long receiverId = 2L;
        List<Message> messages = new ArrayList<>();
        messages.add(new Message());

        when(messageRepo.findBySenderIdAndReceiverId(senderId, receiverId)).thenReturn(messages);

        List<Message> foundMessages = messageService.findMessagesBetweenUsers(senderId, receiverId);

        assertEquals(messages, foundMessages);
    }
}
