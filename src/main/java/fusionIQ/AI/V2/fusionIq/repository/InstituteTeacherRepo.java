package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.InstituteTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InstituteTeacherRepo extends JpaRepository<InstituteTeacher, Long> {
    Optional<InstituteTeacher> findByTeacherId(String teacherId);

    Optional<InstituteTeacher> findByTeacherIdAndInstituteId(String teacherId, Long instituteId);

    Optional<InstituteTeacher> findByEmail(String email);
    @Query("SELECT t FROM InstituteTeacher t WHERE t.institute.id = :instituteId AND t.verified_Otp = :otpVerified AND t.status = :status")
    List<InstituteTeacher> findVerifiedTeachersByInstituteIdAndStatus(@Param("instituteId") Long instituteId, @Param("otpVerified") String otpVerified, @Param("status") InstituteTeacher.Status status);

    List<InstituteTeacher> findByStatus(InstituteTeacher.Status status);
    @Query("SELECT t FROM InstituteTeacher t WHERE t.status = :status AND t.institute.user.id = :userId")
    List<InstituteTeacher> findByStatusAndUserId(@Param("status") InstituteTeacher.Status status, @Param("userId") Long userId);
}
