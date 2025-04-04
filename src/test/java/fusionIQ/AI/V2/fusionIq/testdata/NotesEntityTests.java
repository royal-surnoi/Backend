package fusionIQ.AI.V2.fusionIq.testdata;


import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.data.Notes;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.Video;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NotesEntityTests {

    @Test
    void testNotesDefaultConstructor() {
        Notes note = new Notes();

        // Check that createdAt is set to the current time or not null
        assertThat(note.getCreatedAt()).isNotNull();
        assertThat(note.getCreatedAt()).isBefore(LocalDateTime.now().plusSeconds(1)); // Allow for slight timing discrepancies
    }

    @Test
    void testNotesParameterizedConstructor() {
        User user = new User();
        user.setId(1L); // Set user ID or other required fields

        Course course = new Course();
        course.setId(1L); // Set course ID or other required fields

        Video video = new Video();
        video.setId(1L); // Set video ID or other required fields

        Lesson lesson = new Lesson();
        lesson.setId(1L); // Set lesson ID or other required fields

        LocalDateTime now = LocalDateTime.now();
        Notes note = new Notes(1L, user, course, video, lesson, "Test Note", now);

        // Assert the attributes are set correctly
        assertThat(note.getId()).isEqualTo(1L);
        assertThat(note.getUser()).isEqualTo(user);
        assertThat(note.getCourse()).isEqualTo(course);
        assertThat(note.getVideo()).isEqualTo(video);
        assertThat(note.getLesson()).isEqualTo(lesson);
        assertThat(note.getMyNotes()).isEqualTo("Test Note");
        assertThat(note.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testSettersAndGetters() {
        Notes note = new Notes();

        User user = new User();
        user.setId(1L);
        note.setUser(user);
        assertThat(note.getUser()).isEqualTo(user);

        Course course = new Course();
        course.setId(1L);
        note.setCourse(course);
        assertThat(note.getCourse()).isEqualTo(course);

        Video video = new Video();
        video.setId(1L);
        note.setVideo(video);
        assertThat(note.getVideo()).isEqualTo(video);

        Lesson lesson = new Lesson();
        lesson.setId(1L);
        note.setLesson(lesson);
        assertThat(note.getLesson()).isEqualTo(lesson);

        note.setMyNotes("New note");
        assertThat(note.getMyNotes()).isEqualTo("New note");

        LocalDateTime createdAt = LocalDateTime.now();
        note.setCreatedAt(createdAt);
        assertThat(note.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void testNotesToString() {
        User user = new User();
        user.setId(1L);

        Course course = new Course();
        course.setId(1L);

        Video video = new Video();
        video.setId(1L);

        Lesson lesson = new Lesson();
        lesson.setId(1L);

        Notes note = new Notes();
        note.setUser(user);
        note.setCourse(course);
        note.setVideo(video);
        note.setLesson(lesson);
        note.setMyNotes("Test string");

        // Verify that toString() works correctly
        String result = note.toString();
        assertThat(result).contains("myNotes='Test string'");
        assertThat(result).contains("user=" + user);
        assertThat(result).contains("course=" + course);
        assertThat(result).contains("video=" + video);
        assertThat(result).contains("lesson=" + lesson);
    }

    @Test
    void testNotesWithNullFields() {
        Notes note = new Notes();
        note.setMyNotes(null); // Set note to null

        assertThat(note.getMyNotes()).isNull();
        assertThat(note.getUser()).isNull();
        assertThat(note.getCourse()).isNull();
        assertThat(note.getVideo()).isNull();
        assertThat(note.getLesson()).isNull();
        assertThat(note.getCreatedAt()).isNotNull(); // createdAt should still be set
    }
}

