package fusionIQ.AI.V2.fusionIq.testservice;




import fusionIQ.AI.V2.fusionIq.data.Announcement;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.repository.AnnouncementRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.service.AnnouncementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class AnnouncementServiceTest {

    @InjectMocks
    private AnnouncementService announcementService;

    @Mock
    private AnnouncementRepo announcementRepo;

    @Mock
    private CourseRepo courseRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAnnouncementById() {
        Announcement mockAnnouncement = new Announcement();
        mockAnnouncement.setId(1L);

        when(announcementRepo.findById(1L)).thenReturn(Optional.of(mockAnnouncement));

        Optional<Announcement> retrievedAnnouncement = announcementService.getAnnouncementById(1L);

        assertEquals(Optional.of(mockAnnouncement), retrievedAnnouncement);
    }

    @Test
    void testGetAllAnnouncements() {
        Announcement mockAnnouncement = new Announcement();
        mockAnnouncement.setId(1L);

        List<Announcement> mockAnnouncements = Arrays.asList(mockAnnouncement);

        when(announcementRepo.findAll()).thenReturn(mockAnnouncements);

        List<Announcement> retrievedAnnouncements = announcementService.getAllAnnouncements();

        assertEquals(mockAnnouncements, retrievedAnnouncements);
    }
}
