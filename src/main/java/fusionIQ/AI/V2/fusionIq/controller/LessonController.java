package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Activity;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @PostMapping("/add/{courseId}")
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson, @PathVariable Long courseId) {
        try {
            Lesson savedLesson = lessonService.saveLesson(lesson, courseId);
            return ResponseEntity.ok(savedLesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/getBy/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
        Optional<Lesson> lessonOpt = lessonService.findLessonById(id);
        return lessonOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/all")
    public ResponseEntity<List<Lesson>> getAllLessons() {
        List<Lesson> lessons = lessonService.findAllLessons();
        return ResponseEntity.ok(lessons);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Lesson>> getLessonsByCourse(@PathVariable Long courseId) {
        List<Lesson> lessons = lessonService.findLessonsByCourse(courseId);
        return ResponseEntity.ok(lessons);
    }
    @GetMapping("/activities/{lessonId}")
    public ResponseEntity<List<Activity>> getLessonActivities(@PathVariable Long lessonId) {
        List<Activity> activities = lessonService.getLessonActivities(lessonId);
        return ResponseEntity.ok(activities);
    }
    @PutMapping("/updateLesson/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable("id") long id, @RequestBody Lesson updatedLesson) {
        Optional<Lesson> existingLessonOptional = lessonService.getLessonById(id);
        if (existingLessonOptional.isPresent()) {
            Lesson existingLesson = existingLessonOptional.get();

            if (updatedLesson.getLessonTitle() != null) {
                existingLesson.setLessonTitle(updatedLesson.getLessonTitle());
            }
            if (updatedLesson.getLessonContent() != null) {
                existingLesson.setLessonContent(updatedLesson.getLessonContent());
            }
            if (updatedLesson.getLessonDuration() != null) {
                existingLesson.setLessonDuration(updatedLesson.getLessonDuration());
            }
            if (updatedLesson.getUpdatedAt() != null) {
                existingLesson.setUpdatedAt(updatedLesson.getUpdatedAt());
            }



            Lesson savedLesson= lessonService.savingLesson(existingLesson);
            return ResponseEntity.ok(savedLesson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updateLessonByCourse/{courseId}")
    public ResponseEntity<Lesson> patchLessonByCourseId(
            @PathVariable long courseId,
            @RequestBody Map<String, Object> updates) {

        Lesson updatedLesson = lessonService.patchLessonByCourseId(courseId, updates);

        if (updatedLesson != null) {
            return ResponseEntity.ok(updatedLesson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/updateLessonByCourse/{courseId}/{lessonId}")
    public ResponseEntity<Lesson> updateLessonByCourseId(@PathVariable Long courseId, @PathVariable Long lessonId, @RequestBody Lesson updatedLesson) {
        Optional<Lesson> updatedLessonOpt = lessonService.updateLessonByCourseId(courseId, lessonId, updatedLesson);
        return updatedLessonOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/addLesson/{lessonModuleId}")
    public ResponseEntity<Lesson> createLessonModule(@RequestBody Lesson lesson, @PathVariable Long lessonModuleId) {
        try {
            Lesson savedlesson = lessonService.savedLesson(lesson, lessonModuleId);
            return ResponseEntity.ok(savedlesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    @PutMapping("/lessonModule/{lessonModuleId}")
    public ResponseEntity<Lesson> updateLessonByLessonModuleId(
            @PathVariable Long lessonModuleId,
            @RequestBody Lesson lessonDetails) {
        Lesson updatedLesson = lessonService.updateLesson(lessonModuleId, lessonDetails);
        if (updatedLesson == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLesson);
    }

    @GetMapping("/module/{lessonModuleId}")
    public List<Lesson> getLessonsByLessonModuleId(@PathVariable Long lessonModuleId) {
        return lessonService.getLessonsByLessonModuleId(lessonModuleId);
    }

    @PostMapping("/module/{lessonModuleId}")
    public Lesson createLesson(@PathVariable Long lessonModuleId, @RequestBody Lesson lesson) {
        return lessonService.saveLesson(lessonModuleId, lesson);
    }
}
