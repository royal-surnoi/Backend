package fusionIQ.AI.V2.fusionIq.controller;


import fusionIQ.AI.V2.fusionIq.data.LessonModule;

import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonModuleRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.LessonModuleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LessonModuleController {

    @Autowired
    private LessonModuleService lessonModuleService;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
   private UserRepo userRepo;

    @Autowired
   private LessonModuleRepo lessonModuleRepo;


    @PostMapping("/addLessonModule/{courseId}")
    public ResponseEntity<LessonModule> createLesson(@RequestBody LessonModule lessonModule, @PathVariable Long courseId) {
        try {
            LessonModule savedLessonModule = lessonModuleService.saveLessonModule(lessonModule, courseId);
            return ResponseEntity.ok(savedLessonModule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getAllModules")
    public ResponseEntity<List<LessonModule>> getAllLessonModules() {
        List<LessonModule> lessonModules = lessonModuleService.getAllLessonModules();
        return ResponseEntity.ok(lessonModules);
    }

    @GetMapping("/getModuleByCourse/{courseId}")
    public ResponseEntity<List<LessonModule>> getLessonModulesByCourseId(@PathVariable Long courseId) {
        List<LessonModule> lessonModules = lessonModuleService.getLessonModulesByCourseId(courseId);
        return lessonModules.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(lessonModules);
    }

    @DeleteMapping("/deleteModule/{lessonModuleId}")
    public ResponseEntity<?> deleteLessonModule(@PathVariable Long lessonModuleId) {
        try {
            lessonModuleService.deleteLessonModule(lessonModuleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/updateByCourseIdAndId/{courseId}/{id}")
    public ResponseEntity<LessonModule> updateLessonModuleByIdAndCourseId(
            @PathVariable Long courseId,
            @PathVariable Long id,
            @RequestBody LessonModule updatedLessonModule) {
        try {
            LessonModule updatedModule = lessonModuleService.updateLessonModuleByIdAndCourseId(id, courseId, updatedLessonModule);
            return ResponseEntity.ok(updatedModule);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }

    @PostMapping("/User/addLessonModule/{userId}")
    public ResponseEntity<LessonModule> createLessonByUser(@RequestBody LessonModule lessonModule, @PathVariable Long userId) {
        try {
            LessonModule savedLessonModule = lessonModuleService.saveLessonModuleByUser(lessonModule, userId);
            return ResponseEntity.ok(savedLessonModule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

    }



}

