package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.AIAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AIAssignmentRepo extends JpaRepository<AIAssignment,Long> {
    @Query("SELECT a FROM AIAssignment a WHERE a.user.id = :userId AND a.course.id = :courseId")
    List<AIAssignment> findByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId);

    void deleteByCourseId(Long id);




    @Query("SELECT a FROM AIAssignment a WHERE a.user.id = :userId AND a.lesson.id = :lessonId")
    List<AIAssignment> findByUserIdAndLessonId(@Param("userId") Long userId, @Param("lessonId") Long lessonId);

}
