package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.WorkExperience;
import fusionIQ.AI.V2.fusionIq.service.WorkExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workExperience")
public class WorkExperienceController {

    @Autowired
    private WorkExperienceService workExperienceService;

//    @PostMapping("/create/{personalDetailsId}")
//    public ResponseEntity<WorkExperience> createWorkExperience(@PathVariable Long personalDetailsId, @RequestBody WorkExperience workExperience) {
//        WorkExperience createdWorkExperience = workExperienceService.createWorkExperience(personalDetailsId, workExperience);
//        return ResponseEntity.ok(createdWorkExperience);
//    }

    @PutMapping("/update/{id}")
    public ResponseEntity<WorkExperience> updateWorkExperience(@PathVariable Long id, @RequestBody WorkExperience updatedWorkExperience) {
        WorkExperience updated = workExperienceService.updateWorkExperience(id, updatedWorkExperience);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWorkExperience(@PathVariable Long id) {
        workExperienceService.deleteWorkExperience(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/personalDetails/{personalDetailsId}")
//    public ResponseEntity<List<WorkExperience>> getAllWorkExperienceByPersonalDetailsId(@PathVariable Long personalDetailsId) {
//        List<WorkExperience> workExperienceList = workExperienceService.getAllWorkExperienceByPersonalDetailsId(personalDetailsId);
//        return ResponseEntity.ok(workExperienceList);
//    }
//    @GetMapping("/personalDetails/{personalDetailsId}/workExperience/{workExperienceId}")
//    public ResponseEntity<WorkExperience> getWorkExperienceByPersonalDetailsIdAndWorkExperienceId(
//            @PathVariable Long personalDetailsId, @PathVariable Long workExperienceId) {
//        WorkExperience workExperience = workExperienceService.getWorkExperienceByPersonalDetailsIdAndWorkExperienceId(personalDetailsId, workExperienceId);
//        return ResponseEntity.ok(workExperience);
//    }


    @PostMapping("/user/{userId}")
    public ResponseEntity<WorkExperience> saveWorkExperience(@PathVariable Long userId, @RequestBody WorkExperience workExperience) {
        WorkExperience savedWorkExperience = workExperienceService.saveWorkExperience(userId, workExperience);
        return new ResponseEntity<>(savedWorkExperience, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public List<WorkExperience> getWorkExperienceByUserId(@PathVariable Long userId) {
        return workExperienceService.getWorkExperienceByUserId(userId);
    }
    @GetMapping("/workExperienceOnly/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getWorkExperienceOnlyByUserId(@PathVariable Long userId) {
        List<Map<String, Object>> workExperiences = workExperienceService.getWorkExperienceOnlyByUserId(userId);
        return ResponseEntity.ok(workExperiences);
    }



    @GetMapping("/user/{userId}/workExperience/{workExperienceId}")
    public ResponseEntity<WorkExperience> getWorkExperienceByUserIdAndWorkExperienceId(
            @PathVariable Long userId, @PathVariable Long workExperienceId) {
        WorkExperience workExperience = workExperienceService.getWorkExperienceByUserIdAndWorkExperienceId(userId, workExperienceId);
        return ResponseEntity.ok(workExperience);
    }

    @PutMapping("/update/{userId}/{workExperienceId}")
    public ResponseEntity<WorkExperience> updateWorkExperience(
            @PathVariable Long userId,
            @PathVariable Long workExperienceId,
            @RequestBody WorkExperience updatedWorkExperience) {

        WorkExperience updated = workExperienceService.updateWorkExperience(userId, workExperienceId, updatedWorkExperience);
        return ResponseEntity.ok(updated);
    }
}

