package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.JobAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobAdminRepository extends JpaRepository<JobAdmin, Long> {
    Optional<JobAdmin> findByJobAdminEmail(String jobAdminEmail);

}
