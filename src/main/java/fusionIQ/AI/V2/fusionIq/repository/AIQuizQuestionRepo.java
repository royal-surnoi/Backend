package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.AIQuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIQuizQuestionRepo extends JpaRepository<AIQuizQuestion,Long> {

    List<AIQuizQuestion> findByAiQuizId(Long aiQuizId);
}
