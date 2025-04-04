package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.MockTestInterview;
import fusionIQ.AI.V2.fusionIq.data.TrainingRoom;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainingRoomRepo extends JpaRepository<TrainingRoom, Long> {
    List<TrainingRoom> findByTeacherAndScheduledTimeAfter(User teacher, LocalDateTime scheduledTime);

    @Query("SELECT tr FROM TrainingRoom tr WHERE tr.course.id = :courseId AND tr.teacher.id = :teacherId")
    List<TrainingRoom> findByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId);

    List<TrainingRoom> findByCourseId(Long courseId);

    TrainingRoom findByMockTestInterview(MockTestInterview mockTestInterview);

    List<TrainingRoom> findByMockTestInterviewId(Long mockId);

    List<TrainingRoom> findAllByMockTestInterviewId(Long mockId);

    List<TrainingRoom> findByCourse_Id(Long courseId);

    void deleteByMockTestInterview_Id(Long mockTestInterviewId);

}
