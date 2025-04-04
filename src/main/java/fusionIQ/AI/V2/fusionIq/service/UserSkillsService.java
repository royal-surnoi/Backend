package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.UserSkills;
import fusionIQ.AI.V2.fusionIq.repository.UserSkillsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSkillsService {

    private final UserSkillsRepo userSkillsRepo;

    @Autowired
    public UserSkillsService(UserSkillsRepo userSkillsRepo) {
        this.userSkillsRepo = userSkillsRepo;
    }

    // Method to add a new skill
    public UserSkills addSkill(UserSkills userSkills) {
        return userSkillsRepo.save(userSkills);
    }

    // Method to retrieve skills by user ID
    public List<UserSkills> getSkillsByUserId(Long userId) {
        return userSkillsRepo.findByUserId(userId);
    }

    // Method to update an existing skill
    public UserSkills updateSkill(Long skillId, UserSkills updatedUserSkills) {
        // Fetch the existing skill by its ID
        Optional<UserSkills> existingSkillOptional = userSkillsRepo.findById(skillId);

        // Check if the skill exists and update it
        return existingSkillOptional.map(existingSkill -> {
            existingSkill.setSkillName(updatedUserSkills.getSkillName());
            existingSkill.setLevel(updatedUserSkills.getLevel());
            // Update other fields as necessary
            return userSkillsRepo.save(existingSkill);  // Save and return updated skill
        }).orElse(null);  // Return null if skill not found
    }
}
