package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.TeacherDiscussionController;
import fusionIQ.AI.V2.fusionIq.data.TeacherDiscussion;
import fusionIQ.AI.V2.fusionIq.service.TeacherDiscussionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TeacherDiscussionControllerTest {

    @InjectMocks
    private TeacherDiscussionController teacherDiscussionController;

    @Mock
    private TeacherDiscussionService teacherDiscussionService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherDiscussionController).build();
    }

    @Test
    void testGetAllTeacherDiscussions() throws Exception {
        when(teacherDiscussionService.getAllTeacherDiscussions()).thenReturn(Collections.singletonList(new TeacherDiscussion()));

        mockMvc.perform(get("/teacherDiscussion/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        verify(teacherDiscussionService, times(1)).getAllTeacherDiscussions();
    }

    @Test
    void testGetTeacherDiscussionById() throws Exception {
        TeacherDiscussion discussion = new TeacherDiscussion();
        discussion.setId(1L);
        when(teacherDiscussionService.getTeacherDiscussionById(1L)).thenReturn(Optional.of(discussion));

        mockMvc.perform(get("/teacherDiscussion/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(teacherDiscussionService, times(1)).getTeacherDiscussionById(1L);
    }

    @Test
    void testGetTeacherDiscussionById_NotFound() throws Exception {
        when(teacherDiscussionService.getTeacherDiscussionById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/teacherDiscussion/get/1"))
                .andExpect(status().isNotFound());

        verify(teacherDiscussionService, times(1)).getTeacherDiscussionById(1L);
    }

    @Test
    void testCreateTeacherDiscussion() throws Exception {
        TeacherDiscussion discussion = new TeacherDiscussion();
        discussion.setId(1L);

        MockMultipartFile attachment = new MockMultipartFile("attachment", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test Content".getBytes());

        when(teacherDiscussionService.createTeacherDiscussion(anyLong(), any(TeacherDiscussion.class)))
                .thenReturn(discussion);

        mockMvc.perform(multipart("/teacherDiscussion/1")
                        .file(attachment)
                        .param("discussionContent", "Test discussion")
                        .param("replyContent", "Test reply"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(teacherDiscussionService, times(1)).createTeacherDiscussion(anyLong(), any(TeacherDiscussion.class));
    }

    @Test
    void testUpdateTeacherDiscussion() throws Exception {
        TeacherDiscussion updatedDiscussion = new TeacherDiscussion();
        updatedDiscussion.setId(1L);
        updatedDiscussion.setDiscussionContent("Updated discussion");

        when(teacherDiscussionService.updateTeacherDiscussion(anyLong(), any(TeacherDiscussion.class)))
                .thenReturn(updatedDiscussion);

        mockMvc.perform(put("/teacherDiscussion/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"discussionContent\": \"Updated discussion\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discussionContent").value("Updated discussion"));

        verify(teacherDiscussionService, times(1)).updateTeacherDiscussion(anyLong(), any(TeacherDiscussion.class));
    }

    @Test
    void testDeleteTeacherDiscussion() throws Exception {
        doNothing().when(teacherDiscussionService).deleteTeacherDiscussion(1L);

        mockMvc.perform(delete("/teacherDiscussion/delete/1"))
                .andExpect(status().isNoContent());

        verify(teacherDiscussionService, times(1)).deleteTeacherDiscussion(1L);
    }
}
