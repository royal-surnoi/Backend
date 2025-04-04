package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VideoTest {

    @Mock
    private Course course;

    @Mock
    private Lesson lesson;

    @Mock
    private LessonModule lessonModule;

    @Mock
    private Notes note;

    private Video video;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        video = new Video(); // Initialize a new instance of Video before each test
    }

    @Test
    void testConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        List<Notes> notesList = new ArrayList<>();
        notesList.add(note);

        Video videoWithValues = new Video(1L, "Video Title", "s3Key123", "https://s3url.com/video",
                75.5, "Video Description", "10:45", now, "English", course, lesson,
                "Sample transcript", lessonModule, notesList);

        assertEquals(1L, videoWithValues.getId());
        assertEquals("Video Title", videoWithValues.getVideoTitle());
        assertEquals("s3Key123", videoWithValues.getS3Key());
        assertEquals("https://s3url.com/video", videoWithValues.getS3Url());
        assertEquals(75.5, videoWithValues.getProgress());
        assertEquals("Video Description", videoWithValues.getVideoDescription());
        assertEquals("10:45", videoWithValues.getVideoDuration());
        assertEquals(now, videoWithValues.getCreatedAt());
        assertEquals("English", videoWithValues.getLanguage());
        assertEquals(course, videoWithValues.getCourse());
        assertEquals(lesson, videoWithValues.getLesson());
        assertEquals("Sample transcript", videoWithValues.getTranscript());
        assertEquals(lessonModule, videoWithValues.getLessonModule());
        assertEquals(notesList, videoWithValues.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        List<Notes> notesList = new ArrayList<>();
        notesList.add(note);

        video.setId(2L);
        video.setVideoTitle("Updated Video Title");
        video.setS3Key("updatedS3Key456");
        video.setS3Url("https://s3url.com/updatedVideo");
        video.setProgress(90.0);
        video.setVideoDescription("Updated Video Description");
        video.setVideoDuration("15:30");
        video.setCreatedAt(now);
        video.setLanguage("Spanish");
        video.setCourse(course);
        video.setLesson(lesson);
        video.setTranscript("Updated transcript");
        video.setLessonModule(lessonModule);
        video.setNotes(notesList);

        assertEquals(2L, video.getId());
        assertEquals("Updated Video Title", video.getVideoTitle());
        assertEquals("updatedS3Key456", video.getS3Key());
        assertEquals("https://s3url.com/updatedVideo", video.getS3Url());
        assertEquals(90.0, video.getProgress());
        assertEquals("Updated Video Description", video.getVideoDescription());
        assertEquals("15:30", video.getVideoDuration());
        assertEquals(now, video.getCreatedAt());
        assertEquals("Spanish", video.getLanguage());
        assertEquals(course, video.getCourse());
        assertEquals(lesson, video.getLesson());
        assertEquals("Updated transcript", video.getTranscript());
        assertEquals(lessonModule, video.getLessonModule());
        assertEquals(notesList, video.getNotes());
    }

    @Test
    void testDefaultConstructor() {
        Video defaultVideo = new Video();

        assertNotNull(defaultVideo.getCreatedAt());
        assertEquals(0L, defaultVideo.getId());
        assertNull(defaultVideo.getVideoTitle());
        assertNull(defaultVideo.getS3Key());
        assertNull(defaultVideo.getS3Url());
        assertEquals(0.0, defaultVideo.getProgress());
        assertNull(defaultVideo.getVideoDescription());
        assertNull(defaultVideo.getVideoDuration());
        assertNull(defaultVideo.getLanguage());
        assertNull(defaultVideo.getCourse());
        assertNull(defaultVideo.getLesson());
        assertNull(defaultVideo.getTranscript());
        assertNull(defaultVideo.getLessonModule());
        assertNull(defaultVideo.getNotes());
    }

    @Test
    void testToString() {
        video.setId(3L);
        video.setVideoTitle("ToString Test Video");
        video.setS3Key("toStringKey");
        video.setS3Url("https://s3url.com/toString");
        video.setProgress(50.0);
        video.setVideoDescription("Testing toString");
        video.setVideoDuration("5:00");
        video.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
        video.setLanguage("French");
        video.setCourse(course);
        video.setLesson(lesson);
        video.setTranscript("ToString transcript");
        video.setLessonModule(lessonModule);
        List<Notes> notesList = new ArrayList<>();
        notesList.add(note);
        video.setNotes(notesList);

        String expected = "Video{id=3, videoTitle='ToString Test Video', s3Key='toStringKey', s3Url='https://s3url.com/toString', progress=50.0, videoDescription='Testing toString', videoDuration='5:00', createdAt=2023-01-01T12:00, language='French', course=" + course + ", lesson=" + lesson + ", transcript='ToString transcript', lessonModule=" + lessonModule + ", notes=" + notesList + "}";
        assertEquals(expected, video.toString());
    }
}
