package fusionIQ.AI.V2.fusionIq.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        // Initialize a User object before each test
        user = new User();
        user.setId(1L);
        user.setName("Meghana");
        user.setEmail("maggipetla@gmail.com");
        user.setPassword("maggi@123");
        user.setPreferences("Preference A");
        user.setProfession("Tester");
        user.setUserLanguage("English");
        user.setOtp("123456");
        user.setOtpGeneratedTime(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setOnlineStatus(User.OnlineStatus.ONLINE);
        user.setUserDescription("This is Meghana.");
    }

    @Test
    void testUserCreation() {
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Meghana");
        assertThat(user.getEmail()).isEqualTo("maggipetla@gmail.com");
        assertThat(user.getPassword()).isEqualTo("maggi@123");
        assertThat(user.getPreferences()).isEqualTo("Preference A");
        assertThat(user.getProfession()).isEqualTo("Tester");
        assertThat(user.getUserLanguage()).isEqualTo("English");
        assertThat(user.getOtp()).isEqualTo("123456");
        assertThat(user.getOnlineStatus()).isEqualTo(User.OnlineStatus.ONLINE);
        assertThat(user.getUserDescription()).isEqualTo("This is Meghana.");
    }

    @Test
    void testSetUserProperties() {
        user.setName("Meghana");
        user.setEmail("maggipetla@gmail.com");
        user.setPassword("maggi@123");

        assertThat(user.getName()).isEqualTo("Meghana");
        assertThat(user.getEmail()).isEqualTo("maggipetla@gmail.com");
        assertThat(user.getPassword()).isEqualTo("maggi@123");
    }

    @Test
    void testUserEquality() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        User user3 = new User();
        user3.setId(2L);

        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
    }

    @Test
    void testUserHashCode() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        User user3 = new User();
        user3.setId(2L);

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }

    @Test
    void testUserToString() {
        String expectedString = "User{" +
                "id=1" +
                ", name='Meghana'" +
                ", email='maggipetla@gmail.com'" +
                ", password='maggi@123'" +
                ", preferences='Preference A'" +
                ", profession='Tester'" +
                ", userLanguage='English'" +
                ", otp='123456'" +
                ", otpGeneratedTime=" + user.getOtpGeneratedTime() +
                ", createdAt=" + user.getCreatedAt() +
                ", updatedAt=" + user.getUpdatedAt() +
                ", userImage=null" +
                ", onlineStatus=ONLINE" +
                ", lastSeen=null" +
                ", userDescription='This is Meghana.'" +
                '}';

        assertThat(user.toString()).isEqualTo(expectedString);
    }

    @Test
    void testUserDefaults() {
        User newUser = new User();
        assertThat(newUser.getCreatedAt()).isNotNull();
        assertThat(newUser.getUpdatedAt()).isNotNull();
        assertThat(newUser.getOnlineStatus()).isNull();
        assertThat(newUser.getLastSeen()).isNull();
    }

    @Test
    void testSetGetLastSeen() {
        LocalDateTime lastSeenTime = LocalDateTime.now();
        user.setLastSeen(lastSeenTime);
        assertThat(user.getLastSeen()).isEqualTo(lastSeenTime);
    }

    @Test
    void testSetGetUserImage() {
        byte[] imageData = new byte[]{1, 2, 3};
        user.setUserImage(imageData);
        assertThat(user.getUserImage()).isEqualTo(imageData);
    }

    @Test
    void testSetGetOtpGeneratedTime() {
        LocalDateTime otpGeneratedTime = LocalDateTime.now();
        user.setOtpGeneratedTime(otpGeneratedTime);
        assertThat(user.getOtpGeneratedTime()).isEqualTo(otpGeneratedTime);
    }
}
