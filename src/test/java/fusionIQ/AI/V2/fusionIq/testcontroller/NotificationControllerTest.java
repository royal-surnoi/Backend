//
//package fusionIQ.AI.V2.fusionIq.testcontroller;
//
//import fusionIQ.AI.V2.fusionIq.controller.NotificationController;
//import fusionIQ.AI.V2.fusionIq.data.Notification;
//import fusionIQ.AI.V2.fusionIq.service.NotificationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(NotificationController.class)
////@ExtendWith(MockitoExtension.class)
//class NotificationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    //    @Mock
//    @MockBean
//    private NotificationService notificationService;
//
////    @InjectMocks
////    private NotificationController notificationController;
//
//    private Notification notification;
//
//    @BeforeEach
//    void setUp() {
////        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
//
//        notification = new Notification();
//        notification.setId(1L);
//        notification.setContent("Sample notification");
//    }
//
//    @Test
//    void testGetNotificationsByUser() throws Exception {
//        when(notificationService.getNotificationsByUser(anyLong())).thenReturn(List.of(notification));
//
//        mockMvc.perform(get("/api/notifications/user/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(notification.getId()))
//                .andExpect(jsonPath("$[0].content").value(notification.getContent()));
//    }
//
//    @Test
//    void testMarkNotificationAsRead() throws Exception {
//        doNothing().when(notificationService).markAsRead(anyLong());
//
//        mockMvc.perform(put("/api/notifications/read/1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testGetUnreadMessageCount() throws Exception {
//        when(notificationService.getUnreadMessageCount(anyLong())).thenReturn(5L);
//
//        mockMvc.perform(get("/api/notifications/unread-count/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("5"));
//    }
//
//    @Test
//    void testGetUnreadMessageCount_UserIdNull() throws Exception {
//        when(notificationService.getUnreadMessageCount(anyLong())).thenThrow(new IllegalArgumentException("User ID must not be null"));
//
//        mockMvc.perform(get("/api/notifications/unread-count/1"))
//                .andExpect(status().isBadRequest());
//    }
//}
