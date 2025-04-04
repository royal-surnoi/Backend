package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.CourseMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseMessageRepo extends JpaRepository<CourseMessage , Long> {


    @Query("SELECT cm FROM CourseMessage cm WHERE cm.to.id IN (SELECT e.user.id FROM Enrollment e WHERE e.course.id = :courseId) OR cm.from.id IN (SELECT e.user.id FROM Enrollment e WHERE e.course.id = :courseId)")
    List<CourseMessage> findByCourseId(@Param("courseId") Long courseId);

}

