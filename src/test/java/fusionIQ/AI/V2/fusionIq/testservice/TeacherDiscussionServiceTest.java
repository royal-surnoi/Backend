package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.TeacherDiscussion;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.TeacherDiscussionRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.TeacherDiscussionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TeacherDiscussionServiceTest {

    @InjectMocks
    private TeacherDiscussionService teacherDiscussionService;

    @Mock
    private TeacherDiscussionRepo teacherDiscussionRepo;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTeacherDiscussionById() {
        TeacherDiscussion discussion = new TeacherDiscussion();
        discussion.setId(1L);
        when(teacherDiscussionRepo.findById(1L)).thenReturn(Optional.of(discussion));

        Optional<TeacherDiscussion> result = teacherDiscussionService.getTeacherDiscussionById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(teacherDiscussionRepo, times(1)).findById(1L);
    }

    @Test
    void testCreateTeacherDiscussion() {
        User user = new User();
        user.setId(1L);

        TeacherDiscussion discussion = new TeacherDiscussion();
        discussion.setDiscussionContent("Test content");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(teacherDiscussionRepo.save(any(TeacherDiscussion.class))).thenReturn(discussion);

        TeacherDiscussion result = teacherDiscussionService.createTeacherDiscussion(1L, discussion);

        assertNotNull(result);
        assertEquals("Test content", result.getDiscussionContent());
        verify(userRepo, times(1)).findById(1L);
        verify(teacherDiscussionRepo, times(1)).save(any(TeacherDiscussion.class));
    }

    @Test
    void testUpdateTeacherDiscussion() {
        TeacherDiscussion existingDiscussion = new TeacherDiscussion();
        existingDiscussion.setId(1L);

        TeacherDiscussion updatedDiscussion = new TeacherDiscussion();
        updatedDiscussion.setDiscussionContent("Updated content");

        when(teacherDiscussionRepo.findById(1L)).thenReturn(Optional.of(existingDiscussion));
        when(teacherDiscussionRepo.save(any(TeacherDiscussion.class))).thenReturn(updatedDiscussion);

        TeacherDiscussion result = teacherDiscussionService.updateTeacherDiscussion(1L, updatedDiscussion);

        assertNotNull(result);
        assertEquals("Updated content", result.getDiscussionContent());
        verify(teacherDiscussionRepo, times(1)).findById(1L);
        verify(teacherDiscussionRepo, times(1)).save(any(TeacherDiscussion.class));
    }

    @Test
    void testDeleteTeacherDiscussion() {
        doNothing().when(teacherDiscussionRepo).deleteById(1L);

        teacherDiscussionService.deleteTeacherDiscussion(1L);

        verify(teacherDiscussionRepo, times(1)).deleteById(1L);
    }
}

