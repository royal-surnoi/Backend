package fusionIQ.AI.V2.fusionIq.testservice;



import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import fusionIQ.AI.V2.fusionIq.data.GroupMember;
import fusionIQ.AI.V2.fusionIq.data.GroupMessages;
import fusionIQ.AI.V2.fusionIq.data.Message;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.ChatService;
import fusionIQ.AI.V2.fusionIq.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ChatServiceTest {

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private MessageRepo messageRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private GroupMessageRepo groupMessageRepo;

    @Mock
    private BlockRepository blockRepo;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testSaveMessage_SenderBlocked() {
        User sender = new User();
        sender.setId(1L);
        User receiver = new User();
        receiver.setId(2L);
        String messageContent = "Hello!";
        MultipartFile file = mock(MultipartFile.class);

        when(blockRepo.existsByBlockerAndBlocked(receiver, sender)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            chatService.saveMessage(sender, receiver, messageContent, file);
        });

        verify(messageRepo, times(0)).save(any(Message.class));
    }



    @Test
    public void testIsUserOnline() {
        chatService.setUserOnline(1L, true);
        boolean isOnline = chatService.isUserOnline(1L);
        assertTrue(isOnline);
    }

    @Test
    public void testSetUserOnline() {
        chatService.setUserOnline(1L, true);
        assertTrue(chatService.isUserOnline(1L));

        chatService.setUserOnline(1L, false);
        assertFalse(chatService.isUserOnline(1L));
    }

    @Test
    public void testAddUserToGroup() {
        GroupMessages groupMessages = new GroupMessages();
        groupMessages.setMembers(new HashSet<>());

        User user = new User();
        user.setId(1L);

        when(groupMessageRepo.findById(anyLong())).thenReturn(Optional.of(groupMessages));
        when(groupMessageRepo.save(any(GroupMessages.class))).thenReturn(groupMessages);

        GroupMessages updatedGroup = chatService.addUserToGroup(1L, user);

        assertTrue(updatedGroup.getMembers().contains(user));
        verify(groupMessageRepo, times(1)).save(any(GroupMessages.class));
    }

    @Test
    public void testRemoveUserFromGroup() {
        User user = new User();
        user.setId(1L);

        GroupMessages group = new GroupMessages();
        group.setMembers(new HashSet<>(Collections.singleton(user)));

        when(groupMessageRepo.findById(anyLong())).thenReturn(Optional.of(group));
        when(groupMessageRepo.save(any(GroupMessages.class))).thenReturn(group);

        GroupMessages updatedGroup = chatService.removeUserFromGroup(1L, user);

        assertFalse(updatedGroup.getMembers().contains(user));
        verify(groupMessageRepo, times(1)).save(any(GroupMessages.class));
    }



    @Test
    public void testGetGroupMessages() {
        GroupMessages group = new GroupMessages();
        group.setId(1L);
        when(groupMessageRepo.findById(anyLong())).thenReturn(Optional.of(group));

        chatService.getGroupMessages(1L);

        verify(messageRepo, times(1)).findByGroupMessagesOrderByCreatedAtAsc(any(GroupMessages.class));
    }

    @Test
    public void testGetGroupMembersWithAdminStatus() {
        GroupMessages groupMessages = new GroupMessages();
        groupMessages.setMembers(new HashSet<>());
        groupMessages.setAdmin(new User());

        when(groupMessageRepo.findById(anyLong())).thenReturn(Optional.of(groupMessages));

        List<GroupMember> members = chatService.getGroupMembersWithAdminStatus(1L);

        assertNotNull(members);
        verify(groupMessageRepo, times(1)).findById(anyLong());
    }

    @Test
    public void testDeleteGroup() {
        doNothing().when(groupMessageRepo).deleteMessagesByGroupMessagesId(anyLong());
        doNothing().when(groupMessageRepo).deleteGroupMessagesById(anyLong());

        chatService.deletegroup(1L);

        verify(groupMessageRepo, times(1)).deleteMessagesByGroupMessagesId(anyLong());
        verify(groupMessageRepo, times(1)).deleteGroupMessagesById(anyLong());
    }

    @Test
    public void testDeleteMessageBySenderAndMessageId() {
        Message message = new Message();
        message.setId(1L);
        message.setSender(new User());

        when(messageRepo.findByIdAndSenderId(anyLong(), anyLong())).thenReturn(message);

        boolean result = chatService.deleteMessageBySenderAndMessageId(1L, 1L);

        assertTrue(result);
        verify(messageRepo, times(1)).delete(any(Message.class));
    }

    @Test
    public void testDeleteMessageBySenderAndMessageId_NotFound() {
        when(messageRepo.findByIdAndSenderId(anyLong(), anyLong())).thenReturn(null);

        boolean result = chatService.deleteMessageBySenderAndMessageId(1L, 1L);

        assertFalse(result);
        verify(messageRepo, times(0)).delete(any(Message.class));
    }



    @Test
    public void testGetUnreadMessages() {
        when(messageRepo.findByReceiverIdAndIsReadFalse(anyLong())).thenReturn(new ArrayList<>());

        List<Message> unreadMessages = chatService.getUnreadMessages(1L);

        assertNotNull(unreadMessages);
        verify(messageRepo, times(1)).findByReceiverIdAndIsReadFalse(anyLong());
    }

    @Test
    public void testMarkAsRead() {
        Message message = new Message();
        message.setId(1L);
        message.setRead(false);

        when(messageRepo.findById(anyLong())).thenReturn(Optional.of(message));

        chatService.markAsRead(1L);

        assertTrue(message.isRead());
        verify(messageRepo, times(1)).save(any(Message.class));
    }

    @Test
    public void testMarkAllAsRead() {
        List<Message> unreadMessages = Arrays.asList(new Message(), new Message());

        when(messageRepo.findByReceiverIdAndIsReadFalse(anyLong())).thenReturn(unreadMessages);

        chatService.markAllAsRead(1L);

        for (Message message : unreadMessages) {
            assertTrue(message.isRead());
        }
        verify(messageRepo, times(1)).saveAll(anyList());
    }
}

