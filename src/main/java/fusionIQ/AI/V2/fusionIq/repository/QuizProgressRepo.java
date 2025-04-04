package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.QuizProgress;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizProgressRepo extends JpaRepository<QuizProgress, Long> {
    List<QuizProgress> findByUserId(Long userId);
    List<QuizProgress> findByUserIdAndQuizLessonModuleId(Long userId, Long lessonModuleId);
    long countByUserIdAndQuizLessonModuleId(Long userId, Long lessonId);

    List<QuizProgress> findByUserIdAndQuizLessonId(long userId, long lessonId);

    long countByUserIdAndQuizLessonId(long userId, long lessonModuleId);

    @Query("SELECT qp FROM QuizProgress qp WHERE qp.course.id = :courseId AND qp.user.id = :userId")
    List<QuizProgress> findByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);

    @Query("SELECT DISTINCT qp.user.id FROM QuizProgress qp WHERE qp.quiz.id = :quizId AND qp.completed = :completed")
    List<Long> findUserIdsByQuizIdAndCompleted(@Param("quizId") Long quizId, @Param("completed") boolean completed);

    long countByUserAndCourseAndCompleted(User user, Course course, boolean completed);
    List<QuizProgress> findByUserAndCourse(User user, Course course);

    List<QuizProgress> findByUser_IdAndCourse_IdAndCompleted(Long studentId, Long courseId, boolean completed);

    long countByQuizCourseId(Long courseId);

    boolean existsByQuizIdAndUserId(Long quizId, Long userId);

    List<QuizProgress> findByUserIdAndQuiz_Lesson_Id(Long userId, Long lessonId);

    Optional<QuizProgress> findByQuizIdAndUserId(Long quizId, Long userId);


}