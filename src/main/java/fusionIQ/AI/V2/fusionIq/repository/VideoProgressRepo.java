package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.VideoProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoProgressRepo extends JpaRepository<VideoProgress,Long> {

    List<VideoProgress> findByUserId(long userId);
    List<VideoProgress> findByUserIdAndCourseId(long userId, long courseId);
    long countByUserIdAndCourseId(long userId, long courseId);


    long countByUserIdAndLessonId(long userId, long lessonId);

    List<VideoProgress> findByUserIdAndLessonId(long userId, long lessonId);



    @Modifying
    @Query("DELETE FROM VideoProgress vp WHERE vp.video.course.id = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);


    boolean existsByUserIdAndVideoId(long userId, long videoId); // This is already added

    @Query("SELECT COUNT(vp) FROM VideoProgress vp WHERE vp.user.id = :userId AND vp.video.lesson.lessonModule.id = :lessonModuleId")
    long countByUserIdAndLessonModuleId(@Param("userId") Long userId, @Param("lessonModuleId") Long lessonModuleId);

    VideoProgress findByUserIdAndVideoId(Long userId, Long videoId);
}
