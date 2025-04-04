//package fusionIQ.AI.V2.fusionIq.testcontroller;
//
//
//
//
//import fusionIQ.AI.V2.fusionIq.controller.ChatController;
//import fusionIQ.AI.V2.fusionIq.data.GroupMessages;
//import fusionIQ.AI.V2.fusionIq.data.Message;
//import fusionIQ.AI.V2.fusionIq.data.User;
//import fusionIQ.AI.V2.fusionIq.service.ChatService;
//import fusionIQ.AI.V2.fusionIq.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class ChatControllerTest {
//
//    @Mock
//    private ChatService chatService;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private MultipartFile file;
//
//    @InjectMocks
//    private ChatController chatController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testSaveMessage() throws IOException {
//        Long senderId = 1L;
//        Long receiverId = 2L;
//        String content = "Hello";
//
//        Message message = new Message();
//        message.setId(1L);
//
//        when(chatService.saveMessage(any(User.class), any(User.class), eq(content), eq(file)))
//                .thenReturn(message);
//
//        ResponseEntity<Message> response = chatController.saveMessage(senderId, receiverId, content, file);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(message, response.getBody());
//    }
//
//    @Test
//    void testSaveMessageWithIOException() throws IOException {
//        Long senderId = 1L;
//        Long receiverId = 2L;
//        String content = "Hello";
//
//        when(chatService.saveMessage(any(User.class), any(User.class), eq(content), eq(file)))
//                .thenThrow(IOException.class);
//
//        ResponseEntity<Message> response = chatController.saveMessage(senderId, receiverId, content, file);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//
//    @Test
//    void testGetMessages() throws IOException {
//        Long userId = 1L;
//        List<Message> messages = new ArrayList<>();
//        messages.add(new Message());
//
//        when(chatService.getMessages(userId)).thenReturn(messages);
//
//        ResponseEntity<List<Message>> response = chatController.getMessages(userId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(messages, response.getBody());
//    }
//
//    @Test
//    void testGetMessagesWithIOException() throws IOException {
//        Long userId = 1L;
//
//        when(chatService.getMessages(userId)).thenThrow(IOException.class);
//
//        ResponseEntity<List<Message>> response = chatController.getMessages(userId);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//
//    @Test
//    void testIsUserOnline() {
//        Long userId = 1L;
//        when(chatService.isUserOnline(userId)).thenReturn(true);
//
//        ResponseEntity<Boolean> response = chatController.isUserOnline(userId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(true, response.getBody());
//    }
//
//    @Test
//    void testIsUserOnlineWithException() {
//        Long userId = 1L;
//
//        when(chatService.isUserOnline(userId)).thenThrow(RuntimeException.class);
//
//        ResponseEntity<Boolean> response = chatController.isUserOnline(userId);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//
//    @Test
//    void testSetUserOnline() {
//        Long userId = 1L;
//        boolean online = true;
//
//        ResponseEntity<Void> response = chatController.setUserOnline(userId, online);
//
//        verify(chatService, times(1)).setUserOnline(userId, online);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void testSetUserOnlineWithException() {
//        Long userId = 1L;
//        boolean online = true;
//
//        doThrow(RuntimeException.class).when(chatService).setUserOnline(userId, online);
//
//        ResponseEntity<Void> response = chatController.setUserOnline(userId, online);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//
//
//
//
//    @Test
//    void testGetConversationWithException() {
//        Long senderId = 1L;
//        Long receiverId = 2L;
//
//        when(chatService.getConversation(senderId, receiverId)).thenThrow(RuntimeException.class);
//
//        ResponseEntity<List<Message>> response = chatController.getConversation(senderId, receiverId);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//
//    @Test
//    void testAddUserToGroup() {
//        Long groupMessagesId = 1L;
//        Long userId = 1L;
//
//        GroupMessages group = new GroupMessages();
//        when(chatService.addUserToGroup(groupMessagesId, new User(userId)))
//                .thenReturn(group);
//
//        ResponseEntity<GroupMessages> response = chatController.addUserToGroup(groupMessagesId, userId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(group, response.getBody());
//    }
//
//    @Test
//    void testRemoveUserFromGroup() {
//        Long groupMessagesId = 1L;
//        Long userId = 1L;
//
//        GroupMessages group = new GroupMessages();
//        when(chatService.removeUserFromGroup(groupMessagesId, new User(userId)))
//                .thenReturn(group);
//
//        ResponseEntity<GroupMessages> response = chatController.removeUserFromGroup(groupMessagesId, userId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(group, response.getBody());
//    }
//
//    @Test
//    void testSendGroupMessage() throws IOException {
//        Long groupId = 1L;
//        Long senderId = 1L;
//        String messageContent = "Hello";
//
//        Message message = new Message();
//        when(chatService.saveGroupMessage(groupId, senderId, messageContent, file))
//                .thenReturn(message);
//
//        ResponseEntity<Message> response = chatController.sendGroupMessage(groupId, senderId, messageContent, file);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(message, response.getBody());
//    }
//
//    @Test
//    void testSendGroupMessageWithIOException() throws IOException {
//        Long groupId = 1L;
//        Long senderId = 1L;
//        String messageContent = "Hello";
//
//        when(chatService.saveGroupMessage(groupId, senderId, messageContent, file))
//                .thenThrow(IOException.class);
//
//        ResponseEntity<Message> response = chatController.sendGroupMessage(groupId, senderId, messageContent, file);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//
//    @Test
//    void testGetGroupMessages() {
//        Long groupId = 1L;
//        List<Message> messages = new ArrayList<>();
//        messages.add(new Message());
//
//        when(chatService.getGroupMessages(groupId)).thenReturn(messages);
//
//        ResponseEntity<List<Message>> response = chatController.getGroupMessages(groupId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(messages, response.getBody());
//    }
//
//    @Test
//    void testGetGroup() {
//        Long groupMessagesId = 1L;
//
//        GroupMessages group = new GroupMessages();
//        when(chatService.getGroup(groupMessagesId)).thenReturn(group);
//
//        ResponseEntity<GroupMessages> response = chatController.getGroup(groupMessagesId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(group, response.getBody());
//    }
//
//    @Test
//    void testCreateGroup() {
//        String name = "Group 1";
//        Long adminId = 1L;
//        List<Long> memberIds = new ArrayList<>();
//
//        GroupMessages group = new GroupMessages();
//        when(chatService.createGroup(name, adminId, memberIds)).thenReturn(group);
//
//        ResponseEntity<GroupMessages> response = chatController.createGroup(name, adminId, memberIds);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals(group, response.getBody());
//    }
//
//    @Test
//    void testGetGroupsForUser() {
//        Long userId = 1L;
//        List<GroupMessages> groups = new ArrayList<>();
//
//        when(chatService.getGroupsForUser(userId)).thenReturn(groups);
//
//        ResponseEntity<List<GroupMessages>> response = chatController.getGroupsForUser(userId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(groups, response.getBody());
//    }
//
//    @Test
//    void testDeleteGroup() {
//        Long groupId = 1L;
//
//        doNothing().when(chatService).deletegroup(groupId);
//
//        chatController.deletegroup(groupId);
//
//        verify(chatService, times(1)).deletegroup(groupId);
//    }
//
//    @Test
//    void testDeleteMessage() {
//        Long senderId = 1L;
//        Long messageId = 1L;
//
//        when(chatService.deleteMessageBySenderAndMessageId(senderId, messageId)).thenReturn(true);
//
//        ResponseEntity<String> response = chatController.deleteMessage(senderId, messageId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Message deleted successfully", response.getBody());
//    }
//
//    @Test
//    void testDeleteMessageNotFound() {
//        Long senderId = 1L;
//        Long messageId = 1L;
//
//        when(chatService.deleteMessageBySenderAndMessageId(senderId, messageId)).thenReturn(false);
//
//        ResponseEntity<String> response = chatController.deleteMessage(senderId, messageId);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("Message not found or you do not have permission to delete this message", response.getBody());
//    }
//
//    @Test
//    void testDeleteChat() {
//        Long userId = 1L;
//        Long otherUserId = 2L;
//
//        when(chatService.deleteChatByUser(userId, otherUserId)).thenReturn(true);
//
//        ResponseEntity<String> response = chatController.deleteChat(userId, otherUserId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Chat deleted successfully", response.getBody());
//    }
//
//    @Test
//    void testDeleteChatNotFound() {
//        Long userId = 1L;
//        Long otherUserId = 2L;
//
//        when(chatService.deleteChatByUser(userId, otherUserId)).thenReturn(false);
//
//        ResponseEntity<String> response = chatController.deleteChat(userId, otherUserId);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("Chat not found or you do not have permission to delete this chat", response.getBody());
//    }
//
//    @Test
//    void testUpdateUserOnlineStatus() {
//        Long userId = 1L;
//        boolean inChatComponent = true;
//
//        User user = new User();
//        when(userService.getUserById(userId)).thenReturn(user);
//
//        ResponseEntity<Void> response = chatController.updateUserOnlineStatus(userId, inChatComponent);
//
//        verify(userService, times(1)).updateUserOnlineStatus(userId, User.OnlineStatus.ONLINE);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void testGetUserOnlineStatus() {
//        Long userId = 1L;
//
//        User user = new User();
//        user.setOnlineStatus(User.OnlineStatus.ONLINE);
//
//        when(userService.getUserById(userId)).thenReturn(user);
//
//        ResponseEntity<String> response = chatController.getUserOnlineStatus(userId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("online", response.getBody());
//    }
//
//    @Test
//    void testGetUserOnlineStatusNotFound() {
//        Long userId = 1L;
//
//        when(userService.getUserById(userId)).thenReturn(null);
//
//        ResponseEntity<String> response = chatController.getUserOnlineStatus(userId);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    void testGetUnreadMessageCount() {
//        Long userId = 1L;
//        long count = 10L;
//
//        when(chatService.getUnreadMessageCount(userId)).thenReturn(count);
//
//        ResponseEntity<Long> response = chatController.getUnreadMessageCount(userId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(count, response.getBody());
//    }
//
//    @Test
//    void testGetUnreadMessages() {
//        Long receiverId = 1L;
//        List<Message> messages = new ArrayList<>();
//
//        when(chatService.getUnreadMessages(receiverId)).thenReturn(messages);
//
//        List<Message> response = chatController.getUnreadMessages(receiverId);
//
//        assertEquals(messages, response);
//    }
//
//    @Test
//    void testMarkAsRead() {
//        Long messageId = 1L;
//
//        doNothing().when(chatService).markAsRead(messageId);
//
//        chatController.markAsRead(messageId);
//
//        verify(chatService, times(1)).markAsRead(messageId);
//    }
//
//    @Test
//    void testMarkAllAsRead() {
//        Long userId = 1L;
//
//        doNothing().when(chatService).markAllAsRead(userId);
//
//        chatController.markAllAsRead(userId);
//
//        verify(chatService, times(1)).markAllAsRead(userId);
//    }
//
//    @Test
//    void testGetContacts() {
//        Long userId = 1L;
//        List<Message> contacts = new ArrayList<>();
//
//        when(chatService.getContactsWithUnreadCounts(userId)).thenReturn(contacts);
//
//        ResponseEntity<List<Message>> response = chatController.getContacts(userId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(contacts, response.getBody());
//    }
//
//    @Test
//    void testGetUnreadSendersCount() {
//        Long userId = 1L;
//        long count = 5L;
//
//        when(chatService.getUnreadSendersCount(userId)).thenReturn(count);
//
//        ResponseEntity<Long> response = chatController.getUnreadSendersCount(userId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(count, response.getBody());
//    }
//}
