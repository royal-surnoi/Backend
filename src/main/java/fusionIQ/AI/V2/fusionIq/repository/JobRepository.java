package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Job;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByRecruiterId(Long recruiterId);

    List<Job> findByJobAdmin_Id(Long adminId); // Correct method for fetching jobs by adminId

    List<Job> findByRequiredSkillsContainingAndLocationContaining(String requiredSkills, String location);

    List<Job> findByRequiredSkillsContaining(String requiredSkills);

    List<Job> findByLocationContaining(String location);

    @EntityGraph(attributePaths = {"jobAdmin", "recruiter"})
    Optional<Job> findDetailedById(Long id);

    @Query("SELECT CASE WHEN j.status = 'open' THEN true ELSE false END FROM Job j WHERE j.id = :jobId")boolean isJobOpen(Long jobId);

    List<Job> findByRecruiterIdAndStatus(Long recruiterId, String status);

    List<Job> findByJobAdminIdAndStatus(Long jobAdminId, String status);
    List<Job> findByJobAdminIdAndRecruiterIdAndStatus(Long jobAdminId, Long recruiterId, String status);
}