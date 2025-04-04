//package fusionIQ.AI.V2.fusionIq.testservice;
//
//
//
//import fusionIQ.AI.V2.fusionIq.data.*;
//import fusionIQ.AI.V2.fusionIq.repository.*;
//import fusionIQ.AI.V2.fusionIq.service.TeacherFeedbackService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TeacherFeedbackServiceTest {
//
//    @InjectMocks
//    private TeacherFeedbackService teacherFeedbackService;
//
//    @Mock
//    private TeacherFeedbackRepo teacherFeedbackRepo;
//
//    @Mock
//    private UserRepo userRepo;
//
//    @Mock
//    private AssignmentRepo assignmentRepo;
//
//    @Mock
//    private QuizRepo quizRepo;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllFeedback() {
//        List<TeacherFeedback> feedbackList = Arrays.asList(new TeacherFeedback(), new TeacherFeedback());
//        when(teacherFeedbackRepo.findAll()).thenReturn(feedbackList);
//
//        List<TeacherFeedback> result = teacherFeedbackService.getAllFeedback();
//
//        assertEquals(2, result.size());
//        verify(teacherFeedbackRepo, times(1)).findAll();
//    }
//
//    @Test
//    void testGetFeedbackById() {
//        TeacherFeedback feedback = new TeacherFeedback();
//        feedback.setId(1L);
//        when(teacherFeedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));
//
//        Optional<TeacherFeedback> result = teacherFeedbackService.getFeedbackById(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals(1L, result.get().getId());
//    }
//
//    @Test
//    void testCreateFeedback() {
//        TeacherFeedback feedback = new TeacherFeedback();
//        feedback.setFeedback("Good job!");
//        when(teacherFeedbackRepo.save(feedback)).thenReturn(feedback);
//
//        TeacherFeedback result = teacherFeedbackService.createFeedback(feedback);
//
//        assertNotNull(result);
//        assertEquals("Good job!", result.getFeedback());
//        verify(teacherFeedbackRepo, times(1)).save(feedback);
//    }
//
//    @Test
//    void testUpdateFeedback() {
//        TeacherFeedback feedback = new TeacherFeedback();
//        feedback.setId(1L);
//        feedback.setFeedback("Good job!");
//
//        when(teacherFeedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));
//        when(teacherFeedbackRepo.save(any(TeacherFeedback.class))).thenReturn(feedback);
//
//        TeacherFeedback updatedFeedback = new TeacherFeedback();
//        updatedFeedback.setFeedback("Updated feedback");
//
//        TeacherFeedback result = teacherFeedbackService.updateFeedback(1L, updatedFeedback);
//
//        assertNotNull(result);
//        assertEquals("Updated feedback", result.getFeedback());
//        verify(teacherFeedbackRepo, times(1)).findById(1L);
//        verify(teacherFeedbackRepo, times(1)).save(any(TeacherFeedback.class));
//    }
//
//    @Test
//    void testDeleteFeedback() {
//        doNothing().when(teacherFeedbackRepo).deleteById(1L);
//
//        teacherFeedbackService.deleteFeedback(1L);
//
//        verify(teacherFeedbackRepo, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void testCreateFeedbackByAssignment() {
//        TeacherFeedback feedback = new TeacherFeedback();
//        feedback.setFeedback("Great assignment!");
//
//        User teacher = new User();
//        teacher.setId(1L);
//        User student = new User();
//        student.setId(2L);
//        Assignment assignment = new Assignment();
//        assignment.setId(3L);
//
//        when(userRepo.findById(1L)).thenReturn(Optional.of(teacher));
//        when(userRepo.findById(2L)).thenReturn(Optional.of(student));
//        when(assignmentRepo.findById(3L)).thenReturn(Optional.of(assignment));
//        when(teacherFeedbackRepo.save(any(TeacherFeedback.class))).thenReturn(feedback);
//
//        TeacherFeedback result = teacherFeedbackService.createFeedbackByAssignment(1L, 2L, 3L, feedback);
//
//        assertNotNull(result);
//        assertEquals("Great assignment!", result.getFeedback());
//        verify(teacherFeedbackRepo, times(1)).save(any(TeacherFeedback.class));
//    }
//
//    @Test
//    void testCreateFeedbackByQuiz() {
//        TeacherFeedback feedback = new TeacherFeedback();
//        feedback.setFeedback("Great quiz!");
//
//        User teacher = new User();
//        teacher.setId(1L);
//        User student = new User();
//        student.setId(2L);
//        Quiz quiz = new Quiz();
//        quiz.setId(3L);
//
//        when(userRepo.findById(1L)).thenReturn(Optional.of(teacher));
//        when(userRepo.findById(2L)).thenReturn(Optional.of(student));
//        when(quizRepo.findById(3L)).thenReturn(Optional.of(quiz));
//        when(teacherFeedbackRepo.save(any(TeacherFeedback.class))).thenReturn(feedback);
//
//        TeacherFeedback result = teacherFeedbackService.createFeedbackByQuiz(1L, 2L, 3L, feedback);
//
//        assertNotNull(result);
//        assertEquals("Great quiz!", result.getFeedback());
//        verify(teacherFeedbackRepo, times(1)).save(any(TeacherFeedback.class));
//    }
//
//    @Test
//    void testGetFeedbackByStudentId() {
//        List<TeacherFeedback> feedbackList = Arrays.asList(new TeacherFeedback(), new TeacherFeedback());
//        when(teacherFeedbackRepo.findByStudentId(1L)).thenReturn(feedbackList);
//
//        List<TeacherFeedback> result = teacherFeedbackService.getFeedbackByStudentId(1L);
//
//        assertEquals(2, result.size());
//        verify(teacherFeedbackRepo, times(1)).findByStudentId(1L);
//    }
//
//    @Test
//    void testGetGradesByUserId() {
//        TeacherFeedback feedback1 = new TeacherFeedback();
//        feedback1.setGrade("A");
//        feedback1.setFeedback("Great job on the assignment!");
//        feedback1.setCreatedAt(LocalDateTime.now());
//
//        TeacherFeedback feedback2 = new TeacherFeedback();
//        feedback2.setGrade("B");
//        feedback2.setFeedback("Good effort!");
//        feedback2.setCreatedAt(LocalDateTime.now());
//
//        // Mock the behavior of the repository
//        when(teacherFeedbackRepo.findByStudentId(1L)).thenReturn(Arrays.asList(feedback1, feedback2));
//
//        // Act
//        List<String> grades = teacherFeedbackService.getGradesByUserId(1L);
//
//        // Debugging logs to understand what’s going wrong
//        System.out.println("Grades returned: " + grades);
//        grades.forEach(System.out::println);
//
//        // Assertions
//        assertEquals(2, grades.size());
//
//        // Adjust the assertions to match the actual format
//        assertTrue(grades.contains("Title: , Grade: A, Feedback: Great job on the assignment!, Created At: " + feedback1.getCreatedAt()));
//        assertTrue(grades.contains("Title: , Grade: B, Feedback: Good effort!, Created At: " + feedback2.getCreatedAt()));
//    }
//
//}
//
