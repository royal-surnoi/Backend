package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Optional<Recruiter> findByRecruiterEmail(String recruiterEmail);

    List<Recruiter> findByJobAdminId(Long jobAdminId);

    //    Optional<Recruiter> findByEmail(String recruiterEmail);
//    Optional<Recruiter> findByJobAdminEmail(String jobAdminEmail);
    List<Recruiter> findByJobAdminIdAndRecruiterRole(Long jobAdminId, String recruiterRole);

    @Query(value = "SELECT r.id, r.recruiter_name, r.recruiter_email, r.recruiter_role, r.recruiter_deportment, r.created_at " +
            "FROM recruiters r " +
            "WHERE r.job_admin_id = :jobAdminId AND r.recruiter_role = :role",
            nativeQuery = true)
    List<Object[]> findPanelMembersByJobAdminId(@Param("jobAdminId") Long jobAdminId, @Param("role") String role);

    // Method to fetch recruiter email by ID
    @Query("SELECT r.recruiterEmail FROM Recruiter r WHERE r.id = :id")
    Optional<String> findRecruiterEmailById(@Param("id") Long id);
}
