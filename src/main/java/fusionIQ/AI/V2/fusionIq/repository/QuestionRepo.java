package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepo extends JpaRepository<Question,Long> {

    List<Question> findByQuizId(Long quizId);


    boolean existsByQuizId(Long id);

    List<Question> findByJobQuiz_Id(Long jobQuizId);

    // Check if questions exist for a JobQuiz ID
    boolean existsByJobQuiz_Id(Long jobQuizId);

    List<Question> findByJobQuizId(Long jobQuizId);
}
