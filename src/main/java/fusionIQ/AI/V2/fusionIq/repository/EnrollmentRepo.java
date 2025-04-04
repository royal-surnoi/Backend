package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Enrollment;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepo extends JpaRepository<Enrollment,Long> {
    void deleteByUserId(long userId);
    List<Enrollment> findByUserId(long userId);
    void deleteByUserIdAndCourseId(long userId, long courseId);
    @EntityGraph(attributePaths = {"user", "course"})
    List<Enrollment> findByCourseId(long courseId);
    Enrollment findByUserIdAndCourseId(long userId, long courseId);

    void deleteByCourseId(Long courseId);

    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);
    List<Enrollment> findProgressByCourseIdAndUserId(long courseId,long userId);

    List<Enrollment> findByCourse(Course course);

    List<Enrollment> findByUser(User user);

    List<Enrollment> findByUserId(Long userId);



    @Query("SELECT e.course.id FROM Enrollment e WHERE e.user.id = :userId")
    List<Long> findCourseIdsByUserId(@Param("userId") Long userId);


    @Query("SELECT e.user.id FROM Enrollment e WHERE e.course.id = :courseId")
    List<Long> findUserIdsByCourseId(@Param("courseId") Long courseId);


    boolean existsByUserId(Long userId);






    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId")
    long countByCourseId(Long courseId);

//    long countByCourseId(Long courseId);
@Query("SELECT FUNCTION('MONTH', e.enrollmentDate), COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND FUNCTION('YEAR', e.enrollmentDate) = :year GROUP BY FUNCTION('MONTH', e.enrollmentDate)")
List<Object[]> countEnrollmentsByCourseIdAndMonth(@Param("courseId") Long courseId, @Param("year") int year);

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    // Custom query to check if the user is enrolled in a course
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Enrollment e WHERE e.user.id = :userId AND e.course.id = :courseId")
    boolean isUserEnrolledInCourse(Long userId, Long courseId);


}


