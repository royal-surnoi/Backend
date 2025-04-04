package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepo extends JpaRepository<Education, Long> {
//    List<Education> findByPersonalDetailsId(Long personalDetailsId);

    List<Education> findByUserId(Long userId);

}
