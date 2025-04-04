//package fusionIQ.AI.V2.fusionIq.testdata;
//
//
//import fusionIQ.AI.V2.fusionIq.data.Notification;
//import fusionIQ.AI.V2.fusionIq.data.User;
//import org.junit.jupiter.api.Test;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.time.LocalDateTime;
//
//class NotificationTests {
//
//    @Test
//    void testNotificationCreationWithNoArgs() {
//        Notification notification = new Notification();
//
//        assertThat(notification).isNotNull();
//        assertThat(notification.getTimestamp()).isNotNull();
//        assertThat(notification.isRead()).isFalse(); // Default value
//    }
//
//    @Test
//    void testNotificationCreationWithArgs() {
//        User user = new User(); // Replace with actual User initialization
//        user.setId(1L);
//
//        LocalDateTime now = LocalDateTime.now();
//        Notification notification = new Notification(1L, user, "Test content", "http://test.com", now, false, "post", "http://contenturl.com", "Some content text");
//
//        assertThat(notification).isNotNull();
//        assertThat(notification.getId()).isEqualTo(1L);
//        assertThat(notification.getUser()).isEqualTo(user);
//        assertThat(notification.getContent()).isEqualTo("Test content");
//        assertThat(notification.getUrl()).isEqualTo("http://test.com");
//        assertThat(notification.getTimestamp()).isEqualTo(now);
//        assertThat(notification.isRead()).isFalse();
//        assertThat(notification.getContentType()).isEqualTo("post");
//        assertThat(notification.getContentUrl()).isEqualTo("http://contenturl.com");
//        assertThat(notification.getContentText()).isEqualTo("Some content text");
//    }
//
//    @Test
//    void testSettersAndGetters() {
//        User user = new User(); // Replace with actual User initialization
//        user.setId(1L);
//
//        Notification notification = new Notification();
//        notification.setId(2L);
//        notification.setUser(user);
//        notification.setContent("New content");
//        notification.setUrl("http://newurl.com");
//        notification.setRead(true);
//        notification.setContentType("video");
//        notification.setContentUrl("http://newcontenturl.com");
//        notification.setContentText("New content text");
//
//        assertThat(notification.getId()).isEqualTo(2L);
//        assertThat(notification.getUser()).isEqualTo(user);
//        assertThat(notification.getContent()).isEqualTo("New content");
//        assertThat(notification.getUrl()).isEqualTo("http://newurl.com");
//        assertThat(notification.isRead()).isTrue();
//        assertThat(notification.getContentType()).isEqualTo("video");
//        assertThat(notification.getContentUrl()).isEqualTo("http://newcontenturl.com");
//        assertThat(notification.getContentText()).isEqualTo("New content text");
//    }
//
//    @Test
//    void testToStringMethod() {
//        User user = new User(); // Replace with actual User initialization
//        user.setId(1L);
//
//        Notification notification = new Notification(1L, user, "Test content", "http://test.com", LocalDateTime.now(), false, "post", "http://contenturl.com", "Some content text");
//
//        String expectedToString = "Notification{id=1, user=" + user + ", content='Test content', url='http://test.com', timestamp=" + notification.getTimestamp() + ", isRead=false, contentType='post', contentUrl='http://contenturl.com', contentText='Some content text'}";
//
//        assertThat(notification.toString()).isEqualToIgnoringWhitespace(expectedToString);
//    }
//}
