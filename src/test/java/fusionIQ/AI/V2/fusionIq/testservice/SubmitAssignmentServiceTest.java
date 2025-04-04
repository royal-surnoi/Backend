
package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.SubmitAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class SubmitAssignmentServiceTest {

    @InjectMocks
    private SubmitAssignmentService submitAssignmentService;

    @Mock
    private SubmitAssignmentRepo submitAssignmentRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private LessonRepo lessonRepo;

    @Mock
    private LessonModuleRepo lessonModuleRepo;

    @Mock
    private AssignmentRepo assignmentRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitAssignment() throws Exception {
        User user = new User();
        user.setId(1L);

        Lesson lesson = new Lesson();
        lesson.setId(1L);

        SubmitAssignment submitAssignment = new SubmitAssignment();
        submitAssignment.setId(1L);

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(lessonRepo.findById(anyLong())).thenReturn(Optional.of(lesson));
        when(submitAssignmentRepo.save(any(SubmitAssignment.class))).thenReturn(submitAssignment);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        SubmitAssignment result = submitAssignmentService.submitAssignment(1L, 1L, file, "Answer");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(submitAssignmentRepo, times(1)).save(any(SubmitAssignment.class));
    }

    @Test
    void testFindAllSubmissions() {
        when(submitAssignmentRepo.findAll()).thenReturn(Collections.singletonList(new SubmitAssignment()));

        assertEquals(1, submitAssignmentService.findAllSubmissions().size());
        verify(submitAssignmentRepo, times(1)).findAll();
    }

    @Test
    void testFindSubmissionById() {
        SubmitAssignment submitAssignment = new SubmitAssignment();
        submitAssignment.setId(1L);

        when(submitAssignmentRepo.findById(1L)).thenReturn(Optional.of(submitAssignment));

        Optional<SubmitAssignment> result = submitAssignmentService.findSubmissionById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(submitAssignmentRepo, times(1)).findById(1L);
    }

    @Test
    void testDeleteSubmitAssignment() {
        SubmitAssignment submitAssignment = new SubmitAssignment();
        submitAssignment.setId(1L);

        when(submitAssignmentRepo.findById(1L)).thenReturn(Optional.of(submitAssignment));
        doNothing().when(submitAssignmentRepo).deleteById(1L);

        submitAssignmentService.deleteSubmitAssignment(1L);

        verify(submitAssignmentRepo, times(1)).deleteById(1L);
    }

    @Test
    void testSubmitAssignmentByModule() throws Exception {
        User user = new User();
        user.setId(1L);

        LessonModule lessonModule = new LessonModule();
        lessonModule.setId(1L);

        Assignment assignment = new Assignment();
        assignment.setId(1L);

        SubmitAssignment submitAssignment = new SubmitAssignment();
        submitAssignment.setId(1L);

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(lessonModuleRepo.findById(anyLong())).thenReturn(Optional.of(lessonModule));
        when(assignmentRepo.findById(anyLong())).thenReturn(Optional.of(assignment));
        when(submitAssignmentRepo.save(any(SubmitAssignment.class))).thenReturn(submitAssignment);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        SubmitAssignment result = submitAssignmentService.submitAssignmentByModule(1L, 1L, 1L, file, "Answer");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(submitAssignmentRepo, times(1)).save(any(SubmitAssignment.class));
    }
}
