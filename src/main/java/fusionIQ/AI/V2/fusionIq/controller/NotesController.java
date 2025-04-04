package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Notes;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.Video;
import fusionIQ.AI.V2.fusionIq.repository.NotesRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.repository.VideoRepo;
import fusionIQ.AI.V2.fusionIq.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class NotesController {

    @Autowired
    NotesRepo notesRepo;

    @Autowired
    NotesService notesService;

  @Autowired
    UserRepo userRepo;

    @Autowired
    VideoRepo videoRepo;


    @PostMapping("/saveNotes")
    public Notes createNote(@RequestParam Long userId,
                            @RequestParam Long courseId,
                            @RequestParam Long lessonId,
                            @RequestBody String myNotes) {
        return notesService.saveNote(userId, courseId, lessonId, myNotes);
    }
    @PostMapping("/user/{userId}/video/{videoId}")
    public Notes createNoteByVideo(@PathVariable Long userId, @PathVariable Long videoId, @RequestBody String myNotes) {
        return notesService.createNote(userId, videoId, myNotes);
    }



    @GetMapping("/get/user")
    public List<Notes> getNotesByUserIdAndCourseId(@RequestParam Long userId,
                                                   @RequestParam Long courseId) {
        return notesService.getNotesByUserIdAndCourseId(userId, courseId);
    }

    @GetMapping("/get/course")
    public List<Notes> getNotesByUserIdCourseIdAndLessonId(@RequestParam Long userId,
                                                           @RequestParam Long courseId,
                                                           @RequestParam Long lessonId) {
        return notesService.getNotesByUserIdCourseIdAndLessonId(userId, courseId, lessonId);
    }


    @PostMapping("/saveByCourse")
    public Notes createNoteByCourseAndUser(@RequestParam Long userId,
                                           @RequestParam Long courseId,
                                           @RequestBody String myNotes) {
        return notesService.saveNotes(userId, courseId, myNotes);
    }

    @DeleteMapping("/{id}")
    public void deleteNoteById(@PathVariable Long id) {
        notesService.deleteNoteById(id);
    }


    @GetMapping("/all")
    public List<Notes> getAllNotes() {
        return notesService.getAllNotes();
    }

    @GetMapping("get/user/{userId}")
    public List<Notes> getNotesByUserId(@PathVariable Long userId) {
        return notesService.getNotesByUserId(userId);
    }
    @GetMapping("get/lesson/{lessonId}")
    public List<Notes> getNotesByLessonId(@PathVariable Long lessonId) {
        return notesService.getNotesByLessonId(lessonId);
    }

    @GetMapping("get/course/{courseId}")
    public List<Notes> getNotesByCourseId(@PathVariable Long courseId) {
        return notesService.getNotesByCourseId(courseId);
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