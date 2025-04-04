package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.AnnouncementController;
import fusionIQ.AI.V2.fusionIq.data.Announcement;
import fusionIQ.AI.V2.fusionIq.service.AnnouncementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnnouncementController.class)
public class AnnouncementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnnouncementService announcementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAnnouncementById() throws Exception {
        Announcement mockAnnouncement = new Announcement();
        mockAnnouncement.setId(1L);

        when(announcementService.getAnnouncementById(1L)).thenReturn(Optional.of(mockAnnouncement));

        mockMvc.perform(get("/api/announcements/ByAnnouncement/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1}"));
    }

    @Test
    void testGetAllAnnouncements() throws Exception {
        Announcement mockAnnouncement = new Announcement();
        mockAnnouncement.setId(1L);

        List<Announcement> mockAnnouncements = Arrays.asList(mockAnnouncement);

        when(announcementService.getAllAnnouncements()).thenReturn(mockAnnouncements);

        mockMvc.perform(get("/api/announcements/allAnnouncement")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1}]"));
    }
}

