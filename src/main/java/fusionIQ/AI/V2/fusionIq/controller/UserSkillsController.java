package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserSkills;
import fusionIQ.AI.V2.fusionIq.service.UserSkillsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class UserSkillsController {

    private final UserSkillsService userSkillsService;

    public UserSkillsController(UserSkillsService userSkillsService) {
        this.userSkillsService = userSkillsService;
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<UserSkills> addSkill(
            @PathVariable Long userId,
            @RequestBody UserSkills userSkills) {

        // Check if the user object is null, if so, initialize it
        if (userSkills.getUser() == null) {
            userSkills.setUser(new User());  // Create a new User object
        }

        // Set the user ID in the User object
        userSkills.getUser().setId(userId);

        UserSkills addedSkill = userSkillsService.addSkill(userSkills);
        return ResponseEntity.ok(addedSkill);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSkills>> getSkillsByUserId(@PathVariable Long userId) {
        List<UserSkills> skills = userSkillsService.getSkillsByUserId(userId);
        return ResponseEntity.ok(skills);
    }
    @PutMapping("/update/{skillId}")
    public ResponseEntity<UserSkills> updateSkill(
            @PathVariable Long skillId,
            @RequestBody UserSkills updatedUserSkills) {

        // Call the service to update the skill using the skillId
        UserSkills updatedSkill = userSkillsService.updateSkill(skillId, updatedUserSkills);

        // Return a response entity with the updated skill or a 404 if not found
        return updatedSkill != null
                ? ResponseEntity.ok(updatedSkill)
                : ResponseEntity.notFound().build();
    }
}