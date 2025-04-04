package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.ApplyJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplyJobRepository extends JpaRepository<ApplyJob, Long> {
    List<ApplyJob> findByJobId(Long jobId);
    List<ApplyJob> findByUserId(Long userId);
    ApplyJob findByJobIdAndUserId(Long jobId, Long userId);

    List<ApplyJob> findByUserIdAndWithdraw(Long userId, String withdraw);
    @Query("SELECT COUNT(a) FROM ApplyJob a WHERE a.job.id = :jobId")
    Long countApplicationsByJobId(@Param("jobId") Long jobId);

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

}
