package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.JobQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobQuizRepository extends JpaRepository<JobQuiz, Long> {
    List<JobQuiz> findByShortlistedCandidates_User_Id(Long userId);
    @Query("SELECT jq FROM JobQuiz jq WHERE jq.jobId = :jobId AND CONCAT(',', jq.userIds, ',') LIKE CONCAT('%,', :userId, ',%')")
    List<JobQuiz> findByJobIdAndUserId(@Param("jobId") Long jobId, @Param("userId") Long userId);

    List<JobQuiz> findByShortlistedCandidates_User_IdIn(List<Long> userIds);
}
