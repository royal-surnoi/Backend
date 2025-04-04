package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Course;

import fusionIQ.AI.V2.fusionIq.data.LessonModule;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonModuleRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LessonModuleService {

    @Autowired
    private LessonModuleRepo lessonModuleRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private LessonRepo lessonRepo;

    @Autowired
    private UserRepo userRepo;


    public LessonModule saveLessonModule(LessonModule lessonModule, Long courseId) {
        // Business logic: Link lesson with course
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isPresent()) {
            lessonModule.setCourse(courseOpt.get());
        } else {
            throw new IllegalArgumentException("Course not found");
        }
        return lessonModuleRepo.save(lessonModule);
    }

    public List<LessonModule> getAllLessonModules() {
        return lessonModuleRepo.findAll();
    }

    public List<LessonModule> getLessonModulesByCourseId(Long courseId) {
        List<LessonModule> lessonModules = lessonModuleRepo.findByCourseId(courseId);
        // Optionally filter out any modules with null courses
        lessonModules = lessonModules.stream()
                .filter(module -> module.getCourse() != null)
                .collect(Collectors.toList());
        return lessonModules;
    }

    public Optional<LessonModule> getLessonModuleById(Long id) {
        return lessonModuleRepo.findById(id);
    }

    @Transactional
    public void deleteLessonModule(Long lessonModuleId) {
        LessonModule lessonModule = lessonModuleRepo.findById(lessonModuleId)
                .orElseThrow(() -> new RuntimeException("LessonModule not found with id: " + lessonModuleId));

        lessonModuleRepo.delete(lessonModule);
    }

    @Transactional
    public LessonModule updateLessonModuleByIdAndCourseId(Long id, Long courseId, LessonModule updatedLessonModule) {
        Optional<LessonModule> existingLessonModuleOptional = lessonModuleRepo.findByIdAndCourseId(id, courseId);

        if (existingLessonModuleOptional.isEmpty()) {
            throw new RuntimeException("LessonModule not found for id: " + id + " and courseId: " + courseId);
        }

        LessonModule existingLessonModule = existingLessonModuleOptional.get();

        existingLessonModule.setModuleName(updatedLessonModule.getModuleName());
        existingLessonModule.setLesson(updatedLessonModule.getLesson());

        return lessonModuleRepo.save(existingLessonModule);
    }


    public LessonModule saveLessonModuleByUser(LessonModule lessonModule, Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            lessonModule.setUser(userOptional.get());
        } else {
            throw new IllegalArgumentException("User not found");
        }
        return lessonModuleRepo.save(lessonModule);
    }


}

