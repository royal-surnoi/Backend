package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Education;
import fusionIQ.AI.V2.fusionIq.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/education")
public class EducationController {

    @Autowired
    private EducationService educationService;


    @PutMapping("/update/{id}")
    public ResponseEntity<Education> updateEducation(@PathVariable Long id, @RequestBody Education updatedEducation) {
        Education updated = educationService.updateEducation(id, updatedEducation);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        educationService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/personalDetails/{personalDetailsId}")
//    public ResponseEntity<List<Education>> getAllEducationByPersonalDetailsId(@PathVariable Long personalDetailsId) {
//        List<Education> educationList = educationService.getAllEducationByPersonalDetailsId(personalDetailsId);
//        return ResponseEntity.ok(educationList);
//    }


    @GetMapping("/user/{userId}")
    public List<Education> getEducationByUserId(@PathVariable Long userId) {
        return educationService.getEducationByUserId(userId);
    }

    @PostMapping("/create/user/{userId}")
    public ResponseEntity<Education> saveEducation(@PathVariable Long userId, @RequestBody Education education) {
        Education savedEducation = educationService.saveEducation(userId, education);
        return new ResponseEntity<>(savedEducation, HttpStatus.CREATED);
    }
    @PutMapping("/update/{userId}/{educationId}")
    public ResponseEntity<Education> updateEducation(
            @PathVariable Long userId,
            @PathVariable Long educationId,
            @RequestBody Education updatedEducation) {
        Education updated = educationService.updateEducation(userId, educationId, updatedEducation);
        return ResponseEntity.ok(updated);
    }
}
