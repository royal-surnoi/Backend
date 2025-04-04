package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Mock
    private User sender;

    @Mock
    private User receiver;

    @Mock
    private GroupMessages groupMessages;

    @Mock
    private CommunityMessages communityMessages;

    @Mock
    private Reactions reaction;

    private Message message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        message = new Message(); // Create a new Message instance for testing
    }

    @Test
    void testConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        List<Reactions> reactionsList = new ArrayList<>();
        reactionsList.add(reaction);

        Message messageWithValues = new Message(1L, sender, receiver, groupMessages, communityMessages,
                "Hello, this is a test message.", true, false,
                "https://example.com/file", true, false,
                "👍", "❤️", reactionsList, 5L, now);

        assertEquals(1L, messageWithValues.getId());
        assertEquals(sender, messageWithValues.getSender());
        assertEquals(receiver, messageWithValues.getReceiver());
        assertEquals(groupMessages, messageWithValues.getGroupMessages());
        assertEquals(communityMessages, messageWithValues.getCommunityMessages());
        assertEquals("Hello, this is a test message.", messageWithValues.getMessageContent());
        assertTrue(messageWithValues.isSent());
        assertFalse(messageWithValues.isRead());
        assertEquals("https://example.com/file", messageWithValues.getFileUrl());
        assertTrue(messageWithValues.isSenderDeleted());
        assertFalse(messageWithValues.isReceiverDeleted());
        assertEquals("👍", messageWithValues.getSender_reaction());
        assertEquals("❤️", messageWithValues.getReciver_reaction());
        assertEquals(reactionsList, messageWithValues.getReactions());
        assertEquals(5L, messageWithValues.getUnreadMessageCount());
        assertEquals(now, messageWithValues.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        List<Reactions> reactionsList = new ArrayList<>();
        reactionsList.add(reaction);

        message.setId(2L);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setGroupMessages(groupMessages);
        message.setCommunityMessages(communityMessages);
        message.setMessageContent("Updated message content.");
        message.setSent(false);
        message.setRead(true);
        message.setFileUrl("https://example.com/updatedFile");
        message.setSenderDeleted(false);
        message.setReceiverDeleted(true);
        message.setSender_reaction("👎");
        message.setReciver_reaction("😂");
        message.setReactions(reactionsList);
        message.setUnreadMessageCount(10L);
        message.setCreatedAt(now);

        assertEquals(2L, message.getId());
        assertEquals(sender, message.getSender());
        assertEquals(receiver, message.getReceiver());
        assertEquals(groupMessages, message.getGroupMessages());
        assertEquals(communityMessages, message.getCommunityMessages());
        assertEquals("Updated message content.", message.getMessageContent());
        assertFalse(message.isSent());
        assertTrue(message.isRead());
        assertEquals("https://example.com/updatedFile", message.getFileUrl());
        assertFalse(message.isSenderDeleted());
        assertTrue(message.isReceiverDeleted());
        assertEquals("👎", message.getSender_reaction());
        assertEquals("😂", message.getReciver_reaction());
        assertEquals(reactionsList, message.getReactions());
        assertEquals(10L, message.getUnreadMessageCount());
        assertEquals(now, message.getCreatedAt());
    }

    @Test
    void testDefaultConstructor() {
        Message defaultMessage = new Message();

        assertNotNull(defaultMessage.getCreatedAt());
        assertEquals(0L, defaultMessage.getId());
        assertNull(defaultMessage.getSender());
        assertNull(defaultMessage.getReceiver());
        assertNull(defaultMessage.getGroupMessages());
        assertNull(defaultMessage.getCommunityMessages());
        assertNull(defaultMessage.getMessageContent());
        assertFalse(defaultMessage.isSent());
        assertFalse(defaultMessage.isRead());
        assertNull(defaultMessage.getFileUrl());
        assertFalse(defaultMessage.isSenderDeleted());
        assertFalse(defaultMessage.isReceiverDeleted());
        assertNull(defaultMessage.getSender_reaction());
        assertNull(defaultMessage.getReciver_reaction());
        assertNull(defaultMessage.getReactions());
        assertEquals(0L, defaultMessage.getUnreadMessageCount());
    }

    @Test
    void testToString() {
        message.setId(3L);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setGroupMessages(groupMessages);
        message.setCommunityMessages(communityMessages);
        message.setMessageContent("ToString test message");
        message.setSent(true);
        message.setRead(false);
        message.setFileUrl("https://example.com/toStringFile");
        message.setSenderDeleted(false);
        message.setReceiverDeleted(false);
        message.setSender_reaction("🎉");
        message.setReciver_reaction("💖");
        List<Reactions> reactionsList = new ArrayList<>();
        reactionsList.add(reaction);
        message.setReactions(reactionsList);
        message.setUnreadMessageCount(7L);
        message.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));

        String expected = "Message{id=3, sender=" + sender + ", receiver=" + receiver +
                ", groupMessages=" + groupMessages + ", communityMessages=" + communityMessages +
                ", messageContent='ToString test message', sent=true, isRead=false, " +
                "fileUrl='https://example.com/toStringFile', senderDeleted=false, receiverDeleted=false, " +
                "sender_reaction='🎉', reciver_reaction='💖', reactions=" + reactionsList +
                ", unreadMessageCount=7, createdAt=2023-01-01T12:00}";
        assertEquals(expected, message.toString());
    }
}
