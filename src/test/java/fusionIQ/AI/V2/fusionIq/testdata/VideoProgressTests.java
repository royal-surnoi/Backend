package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.data.LessonModule;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.Video;
import fusionIQ.AI.V2.fusionIq.data.VideoProgress;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class VideoProgressTests {

    @Test
    void testDefaultConstructor() {
        // Create a new instance using the default constructor
        VideoProgress videoProgress = new VideoProgress();

        // Assert default values
        assertThat(videoProgress.getId()).isZero(); // Default value for long is 0
        assertThat(videoProgress.getUser()).isNull();
        assertThat(videoProgress.getVideo()).isNull();
        assertThat(videoProgress.getCourse()).isNull();
        assertThat(videoProgress.getLesson()).isNull();
        assertThat(videoProgress.getLessonModule()).isNull();
        assertThat(videoProgress.getProgress()).isEqualTo(0.0);
        assertThat(videoProgress.getWatchedAt()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void testParameterizedConstructor() {
        // Create instances for dependencies
        User user = new User(); // Assume proper User initialization
        Video video = new Video(); // Assume proper Video initialization
        Course course = new Course(); // Assume proper Course initialization
        Lesson lesson = new Lesson(); // Assume proper Lesson initialization
        LessonModule lessonModule = new LessonModule(); // Assume proper LessonModule initialization

        // Define values for the VideoProgress object
        long id = 1L;
        double progress = 75.5;
        LocalDateTime watchedAt = LocalDateTime.now();

        // Create a new instance using the parameterized constructor
        VideoProgress videoProgress = new VideoProgress(id, user, video, course, lesson, lessonModule, progress, watchedAt);

        // Assert that the values are set correctly
        assertThat(videoProgress.getId()).isEqualTo(id);
        assertThat(videoProgress.getUser()).isEqualTo(user);
        assertThat(videoProgress.getVideo()).isEqualTo(video);
        assertThat(videoProgress.getCourse()).isEqualTo(course);
        assertThat(videoProgress.getLesson()).isEqualTo(lesson);
        assertThat(videoProgress.getLessonModule()).isEqualTo(lessonModule);
        assertThat(videoProgress.getProgress()).isEqualTo(progress);
        assertThat(videoProgress.getWatchedAt()).isEqualTo(watchedAt);
    }

    @Test
    void testGettersAndSetters() {
        // Create instances for dependencies
        User user = new User(); // Assume proper User initialization
        Video video = new Video(); // Assume proper Video initialization
        Course course = new Course(); // Assume proper Course initialization
        Lesson lesson = new Lesson(); // Assume proper Lesson initialization
        LessonModule lessonModule = new LessonModule(); // Assume proper LessonModule initialization

        // Create a new instance of VideoProgress
        VideoProgress videoProgress = new VideoProgress();

        // Set values
        long id = 1L;
        double progress = 50.0;
        LocalDateTime watchedAt = LocalDateTime.now().minusHours(1);

        videoProgress.setId(id);
        videoProgress.setUser(user);
        videoProgress.setVideo(video);
        videoProgress.setCourse(course);
        videoProgress.setLesson(lesson);
        videoProgress.setLessonModule(lessonModule);
        videoProgress.setProgress(progress);
        videoProgress.setWatchedAt(watchedAt);

        // Assert values
        assertThat(videoProgress.getId()).isEqualTo(id);
        assertThat(videoProgress.getUser()).isEqualTo(user);
        assertThat(videoProgress.getVideo()).isEqualTo(video);
        assertThat(videoProgress.getCourse()).isEqualTo(course);
        assertThat(videoProgress.getLesson()).isEqualTo(lesson);
        assertThat(videoProgress.getLessonModule()).isEqualTo(lessonModule);
        assertThat(videoProgress.getProgress()).isEqualTo(progress);
        assertThat(videoProgress.getWatchedAt()).isEqualTo(watchedAt);
    }

    @Test
    void testToString() {
        // Create instances for dependencies
        User user = new User(); // Assume proper User initialization
        Video video = new Video(); // Assume proper Video initialization
        Course course = new Course(); // Assume proper Course initialization
        Lesson lesson = new Lesson(); // Assume proper Lesson initialization
        LessonModule lessonModule = new LessonModule(); // Assume proper LessonModule initialization

        // Define values for the VideoProgress object
        long id = 1L;
        double progress = 50.0;
        LocalDateTime watchedAt = LocalDateTime.now();

        // Create a new instance
        VideoProgress videoProgress = new VideoProgress(id, user, video, course, lesson, lessonModule, progress, watchedAt);

        // Verify the toString() method output
        String expectedString = "VideoProgress{" +
                "id=" + id +
                ", user=" + user +
                ", video=" + video +
                ", course=" + course +
                ", lesson=" + lesson +
                ", lessonModule=" + lessonModule +
                ", watchedAt=" + watchedAt +
                '}';

        assertThat(videoProgress.toString()).isEqualTo(expectedString);
    }
}
