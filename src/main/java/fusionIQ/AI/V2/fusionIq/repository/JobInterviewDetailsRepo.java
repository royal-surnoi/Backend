package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.JobInterviewDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import java.util.List;

import java.util.Optional;

@Repository

public interface JobInterviewDetailsRepo extends JpaRepository<JobInterviewDetails, Long> {

    List<JobInterviewDetails> findByUserId(Long userId);

    @Query(value = "SELECT * FROM job_interview_details WHERE job_id = :jobId AND recruiter_id = :recruiterId ORDER BY id DESC LIMIT 1", nativeQuery = true)

    JobInterviewDetails findByJobIdAndRecruiterId(@Param("jobId") Long jobId, @Param("recruiterId") Long recruiterId);

    List<JobInterviewDetails> findByJobIdAndUserId(Long jobId, Long userId);

    @Query(value = "SELECT * FROM job_interview_details WHERE job_id = :jobId AND user_id = :userId ORDER BY id DESC LIMIT 1", nativeQuery = true)

    JobInterviewDetails findByJobIdAndUserIdgetone(@Param("jobId") Long jobId, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM job_interview_details WHERE job_id = :jobId AND admin_id = :adminId ORDER BY id DESC LIMIT 1", nativeQuery = true)

    JobInterviewDetails findByJobIdAndAdminId(@Param("jobId") Long jobId, @Param("adminId") Long adminId);

    List<JobInterviewDetails> findByUserIdAndJobId(Long userId, Long jobId);

    @Query("SELECT j FROM JobInterviewDetails j WHERE j.recruiterId = :recruiterId AND j.interviewTimestamp >= :startTime ORDER BY j.interviewTimestamp ASC")

    List<JobInterviewDetails>   findInterviewsForTodayByRecruiterId(

            @Param("recruiterId") Long recruiterId,

            @Param("startTime") LocalDateTime startTime

    );

    @Query("SELECT j FROM JobInterviewDetails j WHERE j.adminId = :adminId AND j.interviewTimestamp >= :startTime ORDER BY j.interviewTimestamp ASC")

    List<JobInterviewDetails> findByAdminIdAndInterviewTimestampAfter(

            @Param("adminId") Long adminId,

            @Param("startTime") LocalDateTime startTime

    );

    @Query("SELECT j FROM JobInterviewDetails j WHERE j.userId = :userId AND j.interviewTimestamp >= :startTime ORDER BY j.interviewTimestamp ASC")

    List<JobInterviewDetails> findByUserIdAndInterviewTimestampAfter(

            @Param("userId") Long userId,

            @Param("startTime") LocalDateTime startTime

    );
    @Query("SELECT j FROM JobInterviewDetails j WHERE j.interviewerEmail = :email AND j.interviewTimestamp >= :startTime ORDER BY j.interviewTimestamp ASC")
    List<JobInterviewDetails> findByInterviewerEmailAndInterviewTimestampAfter(
            @Param("email") String email,
            @Param("startTime") LocalDateTime startTime
    );


}
