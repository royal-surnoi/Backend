package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.LoginRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    @Test
    void testDefaultConstructor() {
        LoginRequest loginRequest = new LoginRequest();
        assertThat(loginRequest.getEmail()).isNull();
        assertThat(loginRequest.getPassword()).isNull();
        assertThat(loginRequest.getOtp()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123", "123456");
        assertThat(loginRequest.getEmail()).isEqualTo("test@example.com");
        assertThat(loginRequest.getPassword()).isEqualTo("password123");
        assertThat(loginRequest.getOtp()).isEqualTo("123456");
    }

    @Test
    void testSetEmail() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        assertThat(loginRequest.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testSetPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password123");
        assertThat(loginRequest.getPassword()).isEqualTo("password123");
    }

    @Test
    void testSetOtp() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setOtp("123456");
        assertThat(loginRequest.getOtp()).isEqualTo("123456");
    }

    @Test
    void testEmailValidation() {
        // Assuming you have a method that checks for valid email format
        // You would typically want to create a validator method for email
        LoginRequest loginRequest = new LoginRequest("invalid-email", "password123", "123456");
        // Simulate the email validation
        boolean isValidEmail = loginRequest.getEmail().contains("@"); // Simple validation
        assertThat(isValidEmail).isFalse();
    }

    @Test
    void testEmptyPassword() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "", "123456");
        assertThat(loginRequest.getPassword()).isEmpty();
    }

    @Test
    void testValidLoginRequest() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123", "123456");
        assertThat(loginRequest.getEmail()).isEqualTo("test@example.com");
        assertThat(loginRequest.getPassword()).isEqualTo("password123");
        assertThat(loginRequest.getOtp()).isEqualTo("123456");
    }

    @Test
    void testNullFields() {
        LoginRequest loginRequest = new LoginRequest(null, null, null);
        assertThat(loginRequest.getEmail()).isNull();
        assertThat(loginRequest.getPassword()).isNull();
        assertThat(loginRequest.getOtp()).isNull();
    }
}
