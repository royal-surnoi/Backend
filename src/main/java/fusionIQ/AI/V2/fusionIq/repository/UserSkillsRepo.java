package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.UserSkills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillsRepo extends JpaRepository<UserSkills, Long> {

    // Method to find UserSkills by user ID
    List<UserSkills> findByUserId(Long userId);

    // No additional methods are necessary, as JpaRepository provides
    // basic CRUD operations, including an update by saving an entity.
}