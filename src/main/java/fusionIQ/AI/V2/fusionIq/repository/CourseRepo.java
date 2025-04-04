package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course,Long> {

        List<Course> findTop20ByOrderByCreatedAtDesc();
        List<Course> findTop20ByOrderByCreatedAtAsc();
        List<Course> findTop20ByOrderByUpdatedAtDesc();

    List<Course> findByCourseType(String courseType);
    List<Course> findByUserId(Long userId);

    List<Course> findByUser(User user);


    List<Course> findByUserId(long userId);

    @Query("SELECT c FROM Course c JOIN c.enrollments e WHERE e.user.id = :userId")
    List<Course> findCoursesByUserId(@Param("userId") long userId);



    List<Course> findByUserIdAndCourseType(Long userId, String courseType);

    @Query("SELECT 'Assignment' AS type, a.course.courseTitle, a.student FROM Assignment a WHERE a.teacher.id = :teacherId AND a.course.id = :courseId " +
            "UNION " +
            "SELECT 'Project' AS type, p.course.courseTitle, p.student FROM Project p WHERE p.teacher.id = :teacherId AND p.course.id = :courseId " +
            "UNION " +
            "SELECT 'Quiz' AS type, q.course.courseTitle, q.student FROM Quiz q WHERE q.teacher.id = :teacherId AND q.course.id = :courseId")
    List<Object[]> findByTeacherIdAndCourseId(Long teacherId, Long courseId);

    @Query("SELECT c FROM Course c JOIN c.lessons l JOIN c.enrollments e WHERE l.id = :lessonId AND e.user.id = :userId")
    Course findCourseByLessonId(@Param("lessonId") long lessonId);

    @Query("SELECT c FROM Course c WHERE CAST(c.coursePercentage AS float) > 90")
    List<Course> findCoursesWithPercentageAbove90();
    List<Course> findByUserIdAndCoursePercentage(Long userId, String coursePercentage);
}

