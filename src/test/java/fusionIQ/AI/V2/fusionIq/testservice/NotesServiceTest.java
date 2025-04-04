package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.NotesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotesServiceTest {

    @Mock
    private NotesRepo notesRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private LessonRepo lessonRepo;

    @Mock
    private VideoRepo videoRepo;

    @InjectMocks
    private NotesService notesService;

    private User user;
    private Course course;
    private Lesson lesson;
    private Video video;
    private Notes notes;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        course = new Course();
        course.setId(1L);

        lesson = new Lesson();
        lesson.setId(1L);

        video = new Video();
        video.setId(1L);

        notes = new Notes();
        notes.setId(1L);
        notes.setUser(user);
        notes.setCourse(course);
        notes.setLesson(lesson);
        notes.setMyNotes("Sample notes");
        notes.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testSaveNote_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepo.findById(1L)).thenReturn(Optional.of(lesson));
        when(notesRepo.save(any(Notes.class))).thenReturn(notes);

        Notes savedNote = notesService.saveNote(1L, 1L, 1L, "Sample notes");

        assertNotNull(savedNote);
        assertEquals("Sample notes", savedNote.getMyNotes());
        verify(notesRepo, times(1)).save(any(Notes.class));
    }

    @Test
    void testSaveNote_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notesService.saveNote(1L, 1L, 1L, "Sample notes"));
        verify(notesRepo, times(0)).save(any(Notes.class));
    }

    @Test
    void testSaveNote_CourseNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notesService.saveNote(1L, 1L, 1L, "Sample notes"));
        verify(notesRepo, times(0)).save(any(Notes.class));
    }

    @Test
    void testSaveNote_LessonNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notesService.saveNote(1L, 1L, 1L, "Sample notes"));
        verify(notesRepo, times(0)).save(any(Notes.class));
    }

    @Test
    void testGetNotesByUserIdAndCourseId_Success() {
        when(notesRepo.findByUserIdAndCourseId(1L, 1L)).thenReturn(List.of(notes));

        List<Notes> result = notesService.getNotesByUserIdAndCourseId(1L, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sample notes", result.get(0).getMyNotes());
        verify(notesRepo, times(1)).findByUserIdAndCourseId(1L, 1L);
    }

    @Test
    void testSaveNotesByCourseAndUser_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        when(notesRepo.save(any(Notes.class))).thenReturn(notes);

        Notes savedNote = notesService.saveNotes(1L, 1L, "Sample notes");

        assertNotNull(savedNote);
        assertEquals("Sample notes", savedNote.getMyNotes());
        verify(notesRepo, times(1)).save(any(Notes.class));
    }

    @Test
    void testDeleteNoteById_Success() {
        when(notesRepo.existsById(1L)).thenReturn(true);
        doNothing().when(notesRepo).deleteById(1L);

        notesService.deleteNoteById(1L);

        verify(notesRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNoteById_NoteNotFound() {
        when(notesRepo.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> notesService.deleteNoteById(1L));
        verify(notesRepo, times(0)).deleteById(anyLong());
    }

    @Test
    void testGetAllNotes_Success() {
        when(notesRepo.findAll()).thenReturn(List.of(notes));

        List<Notes> result = notesService.getAllNotes();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(notesRepo, times(1)).findAll();
    }

    @Test
    void testGetNotesByUserId_Success() {
        when(notesRepo.findByUserId(1L)).thenReturn(List.of(notes));

        List<Notes> result = notesService.getNotesByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(notesRepo, times(1)).findByUserId(1L);
    }

    @Test
    void testGetNotesByLessonId_Success() {
        when(notesRepo.findByLessonId(1L)).thenReturn(List.of(notes));

        List<Notes> result = notesService.getNotesByLessonId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(notesRepo, times(1)).findByLessonId(1L);
    }

    @Test
    void testGetNotesByCourseId_Success() {
        when(notesRepo.findByCourseId(1L)).thenReturn(List.of(notes));

        List<Notes> result = notesService.getNotesByCourseId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(notesRepo, times(1)).findByCourseId(1L);
    }



    @Test
    void testCreateNoteByVideo_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> notesService.createNote(1L, 1L, "Sample notes"));
        verify(notesRepo, times(0)).save(any(Notes.class));
    }

    @Test
    void testCreateNoteByVideo_VideoNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(videoRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> notesService.createNote(1L, 1L, "Sample notes"));
        verify(notesRepo, times(0)).save(any(Notes.class));
    }
}

