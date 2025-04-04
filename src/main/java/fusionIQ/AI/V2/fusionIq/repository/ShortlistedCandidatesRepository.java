package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.ShortlistedCandidates;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository

public interface ShortlistedCandidatesRepository extends JpaRepository<ShortlistedCandidates, Long> {

    List<ShortlistedCandidates> findByJobId(Long jobId);

    Optional<ShortlistedCandidates> findByJobIdAndApplyJobId(Long jobId, Long applyJobId);

    List<ShortlistedCandidates> findAllById(Iterable<Long> ids);

    List<ShortlistedCandidates> findByRecruiterIdAndJobIdAndUserId(Long recruiterId, Long jobId, Long userId);

    List<ShortlistedCandidates> findByUserId(Long userId);

//    @Query("SELECT s FROM ShortlistedCandidates s WHERE s.user.id = :userId AND s.job.id = :jobId")

//    List<ShortlistedCandidates> findByUserIdAndJobId(@Param("userId") Long userId, @Param("jobId") Long jobId);

    Optional<ShortlistedCandidates> findByUserIdAndJobId(Long userId, Long jobId);

}

