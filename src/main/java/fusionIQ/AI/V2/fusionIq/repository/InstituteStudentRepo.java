package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.InstituteStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InstituteStudentRepo extends JpaRepository<InstituteStudent, Long> {


    Optional<InstituteStudent> findByIdAndInstituteId(Long id, Long instituteId);

    boolean existsByEmail(String email);
    Optional<InstituteStudent> findByUserId(Long userId);
    List<InstituteStudent> findByStatus(InstituteStudent.Status status);

    @Query("SELECT s FROM InstituteStudent s WHERE s.institute.id = :instituteId AND s.verified_Otp = :verifiedOtp")
    List<InstituteStudent> findByInstituteIdAndVerifiedOtp(@Param("instituteId") Long instituteId, @Param("verifiedOtp") String verifiedOtp);

    Optional<InstituteStudent> findByUser_Id(Long userId);

    @Query("SELECT s FROM InstituteStudent s WHERE s.institute.id = :instituteId AND s.verified_Otp = :verifiedOtp AND s.status = :status")
    List<InstituteStudent> findVerifiedStudentsByInstituteIdAndStatus(@Param("instituteId") Long instituteId, @Param("verifiedOtp") String verifiedOtp, @Param("status") InstituteStudent.Status status);

    @Query("SELECT s FROM InstituteStudent s WHERE s.status = :status AND s.user.id = :userId")
    List<InstituteStudent> findByStatusAndUserId(@Param("status") InstituteStudent.Status status, @Param("userId") Long userId);

}