package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotesService {

    @Autowired
   private NotesRepo notesRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private LessonRepo lessonRepo;


    @Autowired
    private VideoRepo videoRepo;

    public Notes saveNote(Long userId, Long courseId, Long lessonId, String myNotes) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new RuntimeException("Lesson not found"));

        Notes notes = new Notes();
        notes.setUser(user);
        notes.setCourse(course);
        notes.setLesson(lesson);
        notes.setMyNotes(myNotes);
        notes.setCreatedAt(LocalDateTime.now());

        return notesRepo.save(notes);
    }

    public List<Notes> getNotesByUserIdAndCourseId(Long userId, Long courseId) {
        return notesRepo.findByUserIdAndCourseId(userId, courseId);
    }

    public Notes saveNotes(Long userId, Long courseId, Long lessonId, String myNotes) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new RuntimeException("Lesson not found"));

        Notes notes = new Notes();
        notes.setUser(user);
        notes.setCourse(course);
        notes.setLesson(lesson);
        notes.setMyNotes(myNotes);
        notes.setCreatedAt(LocalDateTime.now());

        return notesRepo.save(notes);
    }

    public List<Notes> getNotesByUserIdCourseIdAndLessonId(Long userId, Long courseId, Long lessonId) {
        return notesRepo.findByUserIdAndCourseIdAndLessonId(userId, courseId, lessonId);
    }

    public Notes saveNotes(Long userId, Long courseId, String myNotes) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        Notes notes = new Notes();
        notes.setUser(user);
        notes.setCourse(course);
        notes.setMyNotes(myNotes);
        notes.setCreatedAt(LocalDateTime.now());

        return notesRepo.save(notes);
    }
    public void deleteNoteById(Long id) {
        if (notesRepo.existsById(id)) {
            notesRepo.deleteById(id);
        } else {
            throw new RuntimeException("Note not found");
        }
    }

    public List<Notes> getAllNotes() {
        return notesRepo.findAll();
    }
    public List<Notes> getNotesByUserId(Long userId) {
        return notesRepo.findByUserId(userId);
    }
    public List<Notes> getNotesByLessonId(Long lessonId) {
        return notesRepo.findByLessonId(lessonId);
    }


    public List<Notes> getNotesByCourseId(Long courseId) {
        return notesRepo.findByCourseId(courseId);
    }

    public Notes createNote(Long userId, Long videoId, String myNotes) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Video video = videoRepo.findById(videoId).orElseThrow(() -> new IllegalArgumentException("Video not found"));

        Notes notes = new Notes();
        notes.setUser(user);
        notes.setVideo(video);
        notes.setMyNotes(myNotes);
        notes.setCreatedAt(LocalDateTime.now());

        return notesRepo.save(notes);
    }
}
