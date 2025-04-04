package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.NotificationService;
import fusionIQ.AI.V2.fusionIq.service.NotificationWebSocketHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepo notificationRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private NotificationWebSocketHandler notificationWebSocketHandler;

    private User user;
    private Notification notification;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("John Doe");

        notification = new Notification();
        notification.setId(1L);
        notification.setUser(user);
        notification.setContent("Test notification");
        notification.setRead(false);
    }

    @Test
    public void testCreateNotification() {
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(notificationRepo.save(any(Notification.class))).thenReturn(notification);

        Notification createdNotification = notificationService.createNotification(user.getId(), "Test notification");

        assertNotNull(createdNotification);
        assertEquals("Test notification", createdNotification.getContent());
        assertEquals(user, createdNotification.getUser());

        verify(notificationRepo, times(1)).save(any(Notification.class));
        verify(notificationWebSocketHandler, times(1)).sendNotification(anyString());
    }

    @Test
    public void testCreateNotification_UserNotFound() {
        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            notificationService.createNotification(user.getId(), "Test notification");
        });

        assertEquals("User not found", exception.getMessage());

        verify(notificationRepo, times(0)).save(any(Notification.class));
        verify(notificationWebSocketHandler, times(0)).sendNotification(anyString());
    }

    @Test
    public void testMarkAsRead() {
        when(notificationRepo.findById(notification.getId())).thenReturn(Optional.of(notification));

        notificationService.markAsRead(notification.getId());

        assertTrue(notification.isRead());
        verify(notificationRepo, times(1)).save(notification);
    }

    @Test
    public void testMarkAsRead_NotificationNotFound() {
        when(notificationRepo.findById(notification.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.markAsRead(notification.getId());
        });

        assertEquals("Notification not found", exception.getMessage());
        verify(notificationRepo, times(0)).save(any(Notification.class));
    }

    @Test
    public void testGetUnreadMessageCount() {
        when(notificationRepo.countUnreadMessagesByUserId(user.getId())).thenReturn(5L);

        long unreadCount = notificationService.getUnreadMessageCount(user.getId());

        assertEquals(5L, unreadCount);
        verify(notificationRepo, times(1)).countUnreadMessagesByUserId(user.getId());
    }

    @Test
    public void testGetUnreadMessageCount_UserIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationService.getUnreadMessageCount(null);
        });

        assertEquals("User ID must not be null", exception.getMessage());
        verify(notificationRepo, times(0)).countUnreadMessagesByUserId(anyLong());
    }
}

