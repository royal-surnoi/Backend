package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Project;
import fusionIQ.AI.V2.fusionIq.data.SubmitProject;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SubmitProjectRepo extends JpaRepository<SubmitProject,Long> {
    List<SubmitProject> findByUserId(Long userId);

    List<SubmitProject> findByCourseId(Long courseId);


    void deleteByCourseId(Long id);

    Optional<Object> findByProjectId(Long projectId);

    long countByProject_CourseAndUser(Course course, User user);

    @Query("SELECT COUNT(sp) FROM SubmitProject sp WHERE sp.course = :course")
    long countByCourse(@Param("course") Course course);

    @Query("SELECT COUNT(sp) FROM SubmitProject sp WHERE sp.course = :course AND sp.user = :user")
    long countByCourseAndUser(@Param("course") Course course, @Param("user") User user);

    @Query("SELECT DISTINCT sp.user.id FROM SubmitProject sp WHERE sp.project.id = :projectId")
    List<Long> findUserIdsByProjectId(@Param("projectId") Long projectId);


    int countByCourseIdAndUserId(Long courseId, Long userId);


    @Query("SELECT COUNT(sp) FROM SubmitProject sp WHERE sp.course.id = :courseId")
    long countByCourseId(Long courseId);

    @Query("SELECT MAX(sp.submittedAt) FROM SubmitProject sp WHERE sp.course.id = :courseId")
    Optional<LocalDateTime> findLatestSubmissionDateByCourseId(Long courseId);

    @Query("SELECT COUNT(DISTINCT s.user) FROM SubmitProject s WHERE s.course.id = :courseId AND FUNCTION('MONTH', s.submittedAt) = :month AND FUNCTION('YEAR', s.submittedAt) = :year")
    long countUniqueUsersByCourseIdAndMonth(@Param("courseId") Long courseId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(DISTINCT s.user) FROM SubmitProject s WHERE s.course.id = :courseId")
    long countUniqueUsersByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT FUNCTION('MONTH', s.submittedAt), COUNT(DISTINCT s.user) FROM SubmitProject s WHERE s.course.id = :courseId AND FUNCTION('YEAR', s.submittedAt) = :year GROUP BY FUNCTION('MONTH', s.submittedAt)")
    List<Object[]> countUniqueUsersByCourseIdAndMonth(@Param("courseId") Long courseId, @Param("year") int year);

    List<SubmitProject> findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<SubmitProject> findByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    boolean existsByUserIdAndProjectId(Long userId, Long projectId);


    boolean existsByProjectIdAndUserIdAndIsSubmitted(Long projectId, Long userId, boolean isSubmitted);

    @Query("SELECT sp FROM SubmitProject sp WHERE sp.project.teacher.id = :teacherId AND sp.project.mockTestInterview.id = :mockId")
    List<SubmitProject> findByTeacherIdAndMockTestInterviewId(@Param("teacherId") Long teacherId, @Param("mockId") Long mockId);

    @Query("SELECT sp.project.mockTestInterview.id FROM SubmitProject sp WHERE sp.project.teacher.id = :teacherId ORDER BY sp.submittedAt DESC")
    Long findLatestMockIdByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT new map(sp.submitProject as submitProject, sp.userAnswer as userAnswer, u.name as studentName, sp.submittedAt as submittedAt) " +
            "FROM SubmitProject sp " +
            "JOIN sp.project p " +
            "JOIN sp.student u " +  // Assuming 'student' is the field in SubmitProject that refers to User
            "WHERE p.teacher.id = :teacherId AND p.mockTestInterview.id = :mockId AND sp.project.id = p.id AND sp.mockTestInterview.id = :mockId")
    List<Map<String, Object>> findSubmissionsByTeacherIdAndMockId(@Param("teacherId") Long teacherId, @Param("mockId") Long mockId);


//    @Query("SELECT sp FROM SubmitProject sp " +
//            "JOIN sp.project p " +
//            "WHERE p.mockTestInterview.teacher.id = :teacherId")
//    List<SubmitProject> findAllByTeacherId(@Param("teacherId") Long teacherId);

    Optional<SubmitProject> findByProjectIdAndUserId(Long projectId, Long userId);

    boolean existsByUserAndCourseAndProject(User user, Course course, Project project);

    @Query("SELECT sp FROM SubmitProject sp " +
            "JOIN sp.project p " +
            "WHERE p.mockTestInterview.teacher.id = :teacherId")
    List<SubmitProject> findAllByTeacherId(@Param("teacherId") Long teacherId);

    List<SubmitProject> findByProjectTeacherIdAndIsSubmittedTrue(Long teacherId);

    List<SubmitProject> findByMockTestInterview_IdAndStudent_Id(Long mockId, Long studentId);

}

