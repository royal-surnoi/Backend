package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.ActivityRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonModuleRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class LessonService {

    @Autowired
    private LessonRepo lessonRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private ActivityRepo activityRepo;
    @Autowired
    private LessonModuleRepo lessonModuleRepo;

    public Lesson saveLesson(Lesson lesson, Long courseId) {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            lesson.setCourse(courseOpt.get());
        } else {
            throw new IllegalArgumentException("Course not found");
        }
        return lessonRepo.save(lesson);
    }

    public Optional<Lesson> findLessonById(Long id) {
        return lessonRepo.findById(id);
    }

    public List<Lesson> findAllLessons() {
        return lessonRepo.findAll();
    }

    public void deleteLesson(Long id) {
        activityRepo.deleteByLessonId(id);
        lessonRepo.deleteById(id);
    }

    public List<Lesson> findLessonsByCourse(Long courseId) {
        return lessonRepo.findByCourseId(courseId);
    }

    public List<Activity> getLessonActivities(Long lessonId) {
        return activityRepo.findByLessonId(lessonId);
    }

    public Optional<Lesson> getLessonById(long id) {
        return lessonRepo.findById(id);
    }

    public Lesson savingLesson(Lesson existingLesson) {
        return lessonRepo.save(existingLesson);
    }

    public Lesson patchLessonByCourseId(long courseId, Map<String, Object> updates) {
        Optional<Course> optionalCourse = courseRepo.findById(courseId);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            Optional<Lesson> optionalLesson = course.getLessons().stream()
                    .findFirst();
            if (optionalLesson.isPresent()) {
                Lesson lesson = optionalLesson.get();
                updates.forEach((key, value) -> {
                    switch (key) {
                        case "lessonTitle":
                            lesson.setLessonTitle((String) value);
                            break;
                        case "lessonContent":
                            lesson.setLessonContent((String) value);
                            break;
                        case "lessonDescription":
                            lesson.setLessonDescription((String) value);
                            break;
                        case "lessonDuration":
                            lesson.setLessonDuration((Long) value);
                            break;
                        case "createdAt":
                            lesson.setCreatedAt((LocalDateTime) value);
                            break;
                        case "updatedAt":
                            lesson.setUpdatedAt((LocalDateTime) value);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid field: " + key);
                    }
                });
                return lessonRepo.save(lesson);
            }
        }
        return null;
    }
    public Optional<Lesson> updateLessonByCourseId(Long courseId, Long lessonId, Lesson updatedLesson) {
        Optional<Lesson> lessonOpt = lessonRepo.findById(lessonId);
        if (lessonOpt.isPresent() && lessonOpt.get().getCourse().getId() == courseId) {
            Lesson existingLesson = lessonOpt.get();
            if (updatedLesson.getLessonTitle() != null) {
                existingLesson.setLessonTitle(updatedLesson.getLessonTitle());
            }
            if (updatedLesson.getLessonContent() != null) {
                existingLesson.setLessonContent(updatedLesson.getLessonContent());
            }
            if (updatedLesson.getLessonDescription() != null) {
                existingLesson.setLessonDescription(updatedLesson.getLessonDescription());
            }
            if (updatedLesson.getLessonDuration() != null) {
                existingLesson.setLessonDuration(updatedLesson.getLessonDuration());
            }
            if (updatedLesson.getUpdatedAt() != null) {
                existingLesson.setUpdatedAt(updatedLesson.getUpdatedAt());
            }
            lessonRepo.save(existingLesson);
            return Optional.of(existingLesson);
        }
        return Optional.empty();
    }

    @Transactional
    public Lesson savedLesson(Lesson lesson, Long lessonModuleId) {
        LessonModule lessonModule = lessonModuleRepo.findById(lessonModuleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lesson module ID"));

        lesson.setLessonModule(lessonModule);

        return lessonRepo.save(lesson);
    }

    public Lesson updateLesson(Long lessonModuleId, Lesson lessonDetails) {
        List<Lesson> lessons = lessonRepo.findByLessonModuleId(lessonModuleId);
        if (lessons.isEmpty()) {
            return null;
        }
        Lesson lesson = lessons.get(0);

        lesson.setLessonTitle(lessonDetails.getLessonTitle());
        lesson.setLessonContent(lessonDetails.getLessonContent());
        lesson.setLessonDescription(lessonDetails.getLessonDescription());
        lesson.setLessonDuration(lessonDetails.getLessonDuration());
        lesson.setUpdatedAt(LocalDateTime.now());

        return lessonRepo.save(lesson);
    }

    public List<Lesson> getLessonsByLessonModuleId(Long lessonModuleId) {
        return lessonRepo.findByLessonModuleId(lessonModuleId);
    }

    public Lesson saveLesson(Long lessonModuleId, Lesson lesson) {
        LessonModule lessonModule = lessonModuleRepo.findById(lessonModuleId).orElseThrow(() -> new RuntimeException("LessonModule not found"));
        lesson.setLessonModule(lessonModule);
        return lessonRepo.save(lesson);
    }






}
