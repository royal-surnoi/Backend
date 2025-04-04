//package fusionIQ.AI.V2.fusionIq.testcontroller;
//
//
//
//import fusionIQ.AI.V2.fusionIq.controller.UserController;
//import fusionIQ.AI.V2.fusionIq.data.*;
//import fusionIQ.AI.V2.fusionIq.service.EmailSenderService;
//import fusionIQ.AI.V2.fusionIq.service.TokenService;
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
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//public class UserControllerTest {
//
//    @InjectMocks
//    private UserController userController;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private TokenService tokenService;
//
//    @Mock
//    private EmailSenderService emailSenderService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCreateUser_Success() {
//        User user = new User();
//        when(userService.saveUser(any(User.class))).thenReturn(user);
//
//        ResponseEntity<User> response = userController.createUser(user);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//    }
//
//    @Test
//    public void testCreateUser_BadRequest() {
//        doThrow(new IllegalArgumentException("Email already in use")).when(userService).saveUser(any(User.class));
//
//        ResponseEntity<User> response = userController.createUser(new User());
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals(null, response.getBody());
//    }
//
//    @Test
//    public void testGetUserById_Found() {
//        User user = new User();
//        user.setId(1L);
//        when(userService.findUserById(1L)).thenReturn(Optional.of(user));
//
//        ResponseEntity<User> response = userController.getUserById(1L);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//    }
//
//    @Test
//    public void testGetUserById_NotFound() {
//        when(userService.findUserById(1L)).thenReturn(Optional.empty());
//
//        ResponseEntity<User> response = userController.getUserById(1L);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    public void testGetAllUsers() {
//        List<User> users = Arrays.asList(new User(), new User());
//        when(userService.findAllUsers()).thenReturn(users);
//
//        ResponseEntity<List<User>> response = userController.getAllUsers();
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(2, response.getBody().size());
//    }
//
//    @Test
//    public void testDeleteUser() {
//        doNothing().when(userService).deleteUser(anyLong());
//
//        ResponseEntity<Void> response = userController.deleteUser(1L);
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(userService, times(1)).deleteUser(1L);
//    }
//
//    @Test
//    public void testGetUserByEmail_Found() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
//
//        ResponseEntity<User> response = userController.getUserByEmail("test@example.com");
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//    }
//
//    @Test
//    public void testGetUserByEmail_NotFound() {
//        when(userService.findByEmail("test@example.com")).thenReturn(Optional.empty());
//
//        ResponseEntity<User> response = userController.getUserByEmail("test@example.com");
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    public void testFollowUser() {
//        doNothing().when(userService).followUser(anyLong(), anyLong());
//
//        ResponseEntity<Void> response = userController.followUser(1L, 2L);
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(userService, times(1)).followUser(1L, 2L);
//    }
//
//    @Test
//    public void testUnfollowUser() {
//        doNothing().when(userService).unfollowUser(anyLong(), anyLong());
//
//        ResponseEntity<Void> response = userController.unfollowUser(1L, 2L);
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(userService, times(1)).unfollowUser(1L, 2L);
//    }
//
//    @Test
//    public void testEnrollUserInCourse() {
//        doNothing().when(userService).enrollUserInCourse(anyLong(), anyLong());
//
//        ResponseEntity<Void> response = userController.enrollUserInCourse(1L, 1L);
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(userService, times(1)).enrollUserInCourse(1L, 1L);
//    }
//
//    @Test
//    public void testUnenrollUserFromCourse() {
//        doNothing().when(userService).unenrollUserFromCourse(anyLong(), anyLong());
//
//        ResponseEntity<Void> response = userController.unenrollUserFromCourse(1L, 1L);
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(userService, times(1)).unenrollUserFromCourse(1L, 1L);
//    }
//
//    @Test
//    public void testRegisterUser_Success() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        when(userService.existsByEmail(anyString())).thenReturn(false);
//        when(userService.registerUser(any(User.class))).thenReturn(user);
//
//        ResponseEntity<?> response = userController.register(user);
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals(user, response.getBody());
//        verify(emailSenderService, times(1)).sendEmail(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    public void testRegisterUser_EmailAlreadyTaken() {
//        User user = new User();
//        user.setEmail("test@example.com");
//
//        // Mocking the service to throw an IllegalArgumentException when the email is already taken
//        doThrow(new IllegalArgumentException("Email is already taken")).when(userService).registerUser(any(User.class));
//
//        // Now call the controller's register method
//        ResponseEntity<?> response = userController.register(user);
//
//        // Assert that the response status is 409 CONFLICT
//        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
//
//        // Assert that the response body contains the expected message
//        assertEquals("Email is already taken", response.getBody());
//    }
//
//
//
//
//
//    @Test
//    public void testLogin_Success() {
//        User user = new User();
//        user.setId(1L);
//        user.setEmail("test@example.com");
//        when(userService.login(anyString(), anyString())).thenReturn(user);
//        when(tokenService.generateToken(anyString())).thenReturn("jwt-token");
//
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("test@example.com");
//        loginRequest.setPassword("password");
//
//        ResponseEntity<?> response = userController.login(loginRequest);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        JwtResponse jwtResponse = (JwtResponse) response.getBody();
//        assertEquals("jwt-token", jwtResponse.getToken());
//    }
//
//    @Test
//    public void testLogin_InvalidCredentials() {
//        when(userService.login(anyString(), anyString())).thenReturn(null);
//
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("test@example.com");
//        loginRequest.setPassword("password");
//
//        ResponseEntity<?> response = userController.login(loginRequest);
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//        assertEquals("Invalid credentials", response.getBody());
//    }
//
//    @Test
//    public void testUpdateUser() {
//        User user = new User();
//        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(user);
//
//        ResponseEntity<User> response = userController.updateUser(1L, user);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//    }
//
//    @Test
//    public void testUploadUserImage_Success() throws IOException {
//        MultipartFile image = mock(MultipartFile.class);
//        doNothing().when(userService).saveUserImage(anyLong(), any(MultipartFile.class));
//
//        ResponseEntity<String> response = userController.uploadUserImage(1L, image);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Image uploaded successfully", response.getBody());
//    }
//
//    @Test
//    public void testUploadUserImage_Failure() throws IOException {
//        MultipartFile image = mock(MultipartFile.class);
//        doThrow(new IOException("Failed to upload")).when(userService).saveUserImage(anyLong(), any(MultipartFile.class));
//
//        ResponseEntity<String> response = userController.uploadUserImage(1L, image);
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Failed to upload image: Failed to upload", response.getBody());
//    }
//
//    @Test
//    public void testGetUserStatus() {
//        User user = new User();
//        user.setOnlineStatus(User.OnlineStatus.ONLINE);
//        when(userService.getUser(anyLong())).thenReturn(user);
//
//        ResponseEntity<Map<String, Object>> response = userController.getUserStatus(1L);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(User.OnlineStatus.ONLINE, response.getBody().get("onlineStatus"));
//    }
//}
//
