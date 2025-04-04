package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.SavedJobs;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobsRepository extends JpaRepository<SavedJobs, Long> {
    List<SavedJobs> findByUserId(Long userId);
    void deleteByJobId(Long jobId);


    Optional<SavedJobs> findByUserIdAndJobId(Long userId, Long jobId);


    void deleteByJobIdAndUserId(Long jobId, Long userId);

}