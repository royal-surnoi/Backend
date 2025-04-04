
package fusionIQ.AI.V2.fusionIq.testcontroller;




import fusionIQ.AI.V2.fusionIq.controller.NotesController;
import fusionIQ.AI.V2.fusionIq.data.Notes;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.Video;
import fusionIQ.AI.V2.fusionIq.service.NotesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NotesControllerTest {

    @Mock
    private NotesService notesService;

    @InjectMocks
    private NotesController notesController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notesController).build();
    }

    @Test
    void testCreateNote_Success() throws Exception {
        Notes notes = new Notes();
        notes.setMyNotes("This is a note");

        when(notesService.saveNote(anyLong(), anyLong(), anyLong(), anyString())).thenReturn(notes);

        mockMvc.perform(post("/saveNotes")
                        .param("userId", "1")
                        .param("courseId", "1")
                        .param("lessonId", "1")
                        .content("This is a note")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.myNotes").value("This is a note"));

        verify(notesService, times(1)).saveNote(anyLong(), anyLong(), anyLong(), anyString());
    }



    @Test
    void testDeleteNoteById_Success() throws Exception {
        doNothing().when(notesService).deleteNoteById(anyLong());

        mockMvc.perform(delete("/1"))
                .andExpect(status().isOk());

        verify(notesService, times(1)).deleteNoteById(anyLong());
    }

    @Test
    void testGetAllNotes_Success() throws Exception {
        Notes notes = new Notes();
        notes.setMyNotes("This is a note");

        when(notesService.getAllNotes()).thenReturn(List.of(notes));

        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].myNotes").value("This is a note"));

        verify(notesService, times(1)).getAllNotes();
    }

    @Test
    void testGetNotesByUserId() throws Exception {
        Notes notes = new Notes();
        notes.setMyNotes("This is a note");

        when(notesService.getNotesByUserId(anyLong())).thenReturn(List.of(notes));

        mockMvc.perform(get("/get/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].myNotes").value("This is a note"));

        verify(notesService, times(1)).getNotesByUserId(anyLong());
    }

    @Test
    void testGetNotesByLessonId() throws Exception {
        Notes notes = new Notes();
        notes.setMyNotes("This is a note");

        when(notesService.getNotesByLessonId(anyLong())).thenReturn(List.of(notes));

        mockMvc.perform(get("/get/lesson/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].myNotes").value("This is a note"));

        verify(notesService, times(1)).getNotesByLessonId(anyLong());
    }

    @Test
    void testGetNotesByCourseId() throws Exception {
        Notes notes = new Notes();
        notes.setMyNotes("This is a note");

        when(notesService.getNotesByCourseId(anyLong())).thenReturn(List.of(notes));

        mockMvc.perform(get("/get/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].myNotes").value("This is a note"));

        verify(notesService, times(1)).getNotesByCourseId(anyLong());
    }
}

