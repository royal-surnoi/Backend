package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.AIQuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIQuizAnswerRepo extends JpaRepository<AIQuizAnswer,Long> {
    boolean existsByAiQuizId(Long aiQuiz);

    List<AIQuizAnswer> findByAiQuizIdAndUserId(Long quizId, Long userId);

    boolean existsByAiQuizIdAndUserId(Long aiQuizId, Long userId);
}
