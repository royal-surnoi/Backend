package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.UserLoginRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserLoginRequestTests {

    @Test
    void testGettersAndSetters() {
        UserLoginRequest loginRequest = new UserLoginRequest();

        // Set values
        String email = "test@example.com";
        String password = "password123";
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Assert values
        assertThat(loginRequest.getEmail()).isEqualTo(email);
        assertThat(loginRequest.getPassword()).isEqualTo(password);
    }

    @Test
    void testDefaultConstructor() {
        UserLoginRequest loginRequest = new UserLoginRequest();

        // Assert default values
        assertThat(loginRequest.getEmail()).isNull();
        assertThat(loginRequest.getPassword()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        // Create an instance with parameterized values
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail("example@example.com");
        loginRequest.setPassword("securePassword");

        // Assert that the values are set correctly
        assertThat(loginRequest.getEmail()).isEqualTo("example@example.com");
        assertThat(loginRequest.getPassword()).isEqualTo("securePassword");
    }
}
