//package fusionIQ.AI.V2.fusionIq.testdata;
//
//import fusionIQ.AI.V2.fusionIq.data.JwtResponse;
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class JwtResponseTest {
//
//    @Test
//    void testJwtResponseConstructorAndGetters() {
//        // Given
//        String token = "sampleToken";
//        long id = 1L;
//        String name = "John Doe";
//        String email = "john.doe@example.com";
//
//        // When
//        JwtResponse jwtResponse = new JwtResponse(token, id, name, email);
//
//        // Then
//        assertThat(jwtResponse.getToken()).isEqualTo(token);
//        assertThat(jwtResponse.getId()).isEqualTo(id);
//        assertThat(jwtResponse.getName()).isEqualTo(name);
//        assertThat(jwtResponse.getEmail()).isEqualTo(email);
//    }
//
//    @Test
//    void testSetters() {
//        // Given
//        JwtResponse jwtResponse = new JwtResponse("initialToken", 1L, "Initial Name", "initial@example.com");
//
//        // When
//        jwtResponse.setToken("newToken");
//        jwtResponse.setId(2L);
//        jwtResponse.setName("New Name");
//        jwtResponse.setEmail("new@example.com");
//
//        // Then
//        assertThat(jwtResponse.getToken()).isEqualTo("newToken");
//        assertThat(jwtResponse.getId()).isEqualTo(2L);
//        assertThat(jwtResponse.getName()).isEqualTo("New Name");
//        assertThat(jwtResponse.getEmail()).isEqualTo("new@example.com");
//    }
//}
//
