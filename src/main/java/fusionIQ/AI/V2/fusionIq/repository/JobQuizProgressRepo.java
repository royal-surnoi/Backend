package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Answer;
import fusionIQ.AI.V2.fusionIq.data.JobQuizProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobQuizProgressRepo extends JpaRepository<JobQuizProgress, Long> {
        // Additional custom queries can be defined here if needed

        Optional<JobQuizProgress> findByJobQuizIdAndUserId(Long jobQuizId, Long userId);

        List<JobQuizProgress> findByRecruiterId(Long recruiterId);


    @Query("SELECT jp FROM JobQuizProgress jp WHERE jp.jobQuiz.jobId = :jobId AND jp.recruiterId = :recruiterId")
    List<JobQuizProgress> findByJobIdAndRecruiterId(@Param("jobId") Long jobId, @Param("recruiterId") Long recruiterId);

    @Query("SELECT jp FROM JobQuizProgress jp WHERE jp.jobQuiz.jobId = :jobId AND jp.adminId = :adminId")
    List<JobQuizProgress> findByJobIdAndAdminId(@Param("jobId") Long jobId, @Param("adminId") Long adminId);

    List<JobQuizProgress> findByUserIdAndJobQuizId(Long userId, Long jobQuizId);
}