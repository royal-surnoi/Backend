package fusionIQ.AI.V2.fusionIq.testservice;




import fusionIQ.AI.V2.fusionIq.data.Enrollment;
import fusionIQ.AI.V2.fusionIq.data.Mentor;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.EnrollmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.MentorRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private EnrollmentRepo enrollmentRepo;

    @Mock
    private MentorRepo mentorRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for getting user enrollments
//    @Test
//    public void testGetUserEnrollments() {
//        // Create a mock list of enrollments
//        List<Enrollment> mockEnrollments = Arrays.asList(new Enrollment(), new Enrollment());
//
//        // Mock the behavior of enrollmentRepo to return mockEnrollments when findByUserId is called
//        when(enrollmentRepo.findByUserId(1L)).thenReturn(mockEnrollments);
//
//        // Act: Call the service method that interacts with the mock
//        List<Enrollment> result = userService.getUserEnrollments(1L);
//
//        // Verify that the findByUserId method was called exactly once with the correct parameter
//        verify(enrollmentRepo, times(1)).findByUserId(1L);
//
//        // Assert that the result contains the expected number of enrollments
//        assertEquals(2, result.size(), "Expected 2 enrollments to be returned");
//    }
//




    // Test case for registering a mentor where the user is already a mentor
    @Test
    public void testRegisterMentor_UserAlreadyMentor() {
        // Mock to simulate that the user exists
        User mockUser = new User();
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

        // Mock to simulate that the user is already a mentor
        when(mentorRepo.existsByUserId(1L)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerMentor(1L, new Mentor());
        });

        // Assert the exception message
        assertEquals("User is already registered as a mentor", exception.getMessage());
    }


    // Test case for registering a mentor where the user is not found
    @Test
    public void testRegisterMentor_UserNotFound() {
        // Mock to simulate that the user is not found
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.registerMentor(1L, new Mentor());
        });

        // Assert the exception message
        assertEquals("User not found", exception.getMessage());
    }
}

