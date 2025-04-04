package fusionIQ.AI.V2.fusionIq.testcontroller;
import fusionIQ.AI.V2.fusionIq.controller.AssignmentController;
import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AssignmentControllerTest {

    @InjectMocks
    private AssignmentController assignmentController;

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private UserService userService;

    @Mock
    private SubmitAssignmentService submitAssignmentService;

    @Mock
    private AssignmentRepo assignmentRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private LessonRepo lessonRepo;

    @Mock
    private SubmitAssignmentRepo submitAssignmentRepo;

    private Assignment testAssignment;
    private User testUser;
    private Lesson testLesson;
    private Course testCourse;
    private MultipartFile testFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        testAssignment = new Assignment();
        testAssignment.setId(1L);
        testAssignment.setAssignmentTitle("Test Assignment");
        testAssignment.setAssignmentTopicName("Test Topic");

        testUser = new User();
        testUser.setId(1L);
        // Remove the username setter if it doesn't exist in your User class
        // Add any other required user fields based on your actual User class

        testLesson = new Lesson();
        testLesson.setId(1L);
        testLesson.setLessonTitle("Test Lesson");

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setCourseTitle("Test Course");

        testFile = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "test content".getBytes()
        );
    }
    @Test
    void getAssignmentsByCourseId_Success() {
        // Arrange
        List<Assignment> assignments = Arrays.asList(testAssignment);
        when(assignmentService.getAssignmentsByCourseId(1L)).thenReturn(assignments);

        // Act
        ResponseEntity<List<Assignment>> response = assignmentController.getAssignmentsByCourseId(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assignments, response.getBody());
    }

    @Test
    void getAssignmentsByCourseId_NotFound() {
        // Arrange
        when(assignmentService.getAssignmentsByCourseId(1L)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Assignment>> response = assignmentController.getAssignmentsByCourseId(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addAssignment_Success() throws IOException {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        when(assignmentService.addAssign(anyLong(), anyLong(), anyString(), anyString(),
                any(MultipartFile.class), any(LocalDateTime.class), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(testAssignment);

        // Act
        ResponseEntity<Assignment> response = assignmentController.addAssignment(
                1L, 1L, "Test Title", "Test Topic", now, now.plusDays(7),
                now.plusDays(8), testFile
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testAssignment, response.getBody());
        verify(assignmentService).addAssign(
                eq(1L), eq(1L), eq("Test Title"), eq("Test Topic"),
                eq(testFile), eq(now), eq(now.plusDays(7)), eq(now.plusDays(8))
        );
    }

    @Test
    void createAssignment_Success() {
        // Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(lessonRepo.findById(1L)).thenReturn(Optional.of(testLesson));
        when(assignmentRepo.save(any(Assignment.class))).thenReturn(testAssignment);

        // Act
        ResponseEntity<Assignment> response = assignmentController.createAssignment(
                1L, 1L, testFile, "Test Title", "Test Topic"
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getProgressPercentageByCourseAndUser_Success() {
        // Arrange
        Map<String, Double> progressMap = new HashMap<>();
        progressMap.put("progress", 75.0);
        when(assignmentService.calculateProgressPercentage(1L, 1L)).thenReturn(progressMap);

        // Act
        ResponseEntity<Map<String, Double>> response =
                assignmentController.getProgressPercentageByCourseAndUser(1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(progressMap, response.getBody());
    }

    @Test
    void submitAssignment_Success() {
        // Arrange
        SubmitAssignment submitAssignment = new SubmitAssignment();
        when(assignmentService.hasUserSubmittedAssignment(1L, 1L)).thenReturn(false);
        when(assignmentService.submitAssignment(eq(1L), eq(1L), eq(1L), any(MultipartFile.class),
                anyString())).thenReturn(submitAssignment);

        // Act
        ResponseEntity<SubmitAssignment> response = assignmentController.submitAssignment(
                1L, 1L, 1L, testFile, "Test Answer"
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(submitAssignment, response.getBody());
    }

    @Test
    void submitAssignment_AlreadySubmitted() {
        // Arrange
        when(assignmentService.hasUserSubmittedAssignment(1L, 1L)).thenReturn(true);

        // Act
        ResponseEntity<SubmitAssignment> response = assignmentController.submitAssignment(
                1L, 1L, 1L, testFile, "Test Answer"
        );

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getUpcomingAssignments_Success() {
        // Arrange
        List<Assignment> upcomingAssignments = Arrays.asList(testAssignment);
        when(assignmentService.getUpcomingAssignmentsByUserId(1L)).thenReturn(upcomingAssignments);

        // Act
        List<Assignment> response = assignmentController.getUpcomingAssignments(1L);

        // Assert
        assertEquals(upcomingAssignments, response);
        verify(assignmentService).getUpcomingAssignmentsByUserId(1L);
    }

    @Test
    void getAssignmentSubmissions_Success() {
        // Arrange
        SubmitAssignment submitAssignment = new SubmitAssignment();
        submitAssignment.setAssignment(testAssignment);
        submitAssignment.setSubmittedAt(LocalDateTime.now());

        List<SubmitAssignment> submissions = Arrays.asList(submitAssignment);
        when(submitAssignmentService.getAssignmentsByUserId(1L)).thenReturn(submissions);

        // Act
        List<Map<String, Object>> response = assignmentController.getAssignmentTitlesAndSubmissionDates(1L);

        // Assert
        assertFalse(response.isEmpty());
        assertEquals(testAssignment.getAssignmentTitle(), response.get(0).get("assignmentTitle"));
        assertNotNull(response.get(0).get("submittedAt"));
    }

    @Test
    void deleteAssignment_Success() {
        // Arrange
        doNothing().when(assignmentService).deleteAssign(1L);

        // Act
        ResponseEntity<Void> response = assignmentController.deleteAssignment(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(assignmentService).deleteAssign(1L);
    }

    @Test
    void getProgressDetails_Success() {
        // Arrange
        Map<String, Object> progressDetails = new HashMap<>();
        progressDetails.put("completion", 75.0);
        when(assignmentService.getProgressDetails(1L, 1L)).thenReturn(progressDetails);

        // Act
        ResponseEntity<Map<String, Object>> response = assignmentController.getProgressDetails(1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(progressDetails, response.getBody());
    }
}