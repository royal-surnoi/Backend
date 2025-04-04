package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkExperienceRepo extends JpaRepository<WorkExperience, Long> {
    List<WorkExperience> findByUserId(Long userId);
//    List<WorkExperience> findByPersonalDetailsId(Long personalDetailsId);
//
//    Optional<WorkExperience> findByIdAndPersonalDetailsId(Long id, Long personalDetailsId);

}