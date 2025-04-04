package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.AIQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AIQuizRepo extends JpaRepository<AIQuiz , Long> {
    List<AIQuiz> findByUserId(Long userId);

    void deleteByCourseId(Long id);
    Optional<AIQuiz> findByIdAndUserId(long id, long userId);

    List<AIQuiz> findByUserIdAndLessonId(long userId, long lessonId);

}
