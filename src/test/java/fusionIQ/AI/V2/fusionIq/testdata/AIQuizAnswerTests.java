package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.AIQuiz;
import fusionIQ.AI.V2.fusionIq.data.AIQuizAnswer;
import fusionIQ.AI.V2.fusionIq.data.AIQuizQuestion;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AIQuizAnswerTests {

    @Test
    void testDefaultConstructor() {
        AIQuizAnswer aiQuizAnswer = new AIQuizAnswer();

        // Verifying that the default constructor initializes fields correctly
        assertThat(aiQuizAnswer.getId()).isEqualTo(0L);
        assertThat(aiQuizAnswer.getAIQuizUserAnswer()).isNull();
        assertThat(aiQuizAnswer.getUser()).isNull();
        assertThat(aiQuizAnswer.getAiQuiz()).isNull();
        assertThat(aiQuizAnswer.getAiQuizQuestion()).isNull();
        assertThat(aiQuizAnswer.isCorrectAnswer()).isFalse(); // Assuming the default value is false
    }

    @Test
    void testParameterizedConstructor() {
        User user = new User();
        AIQuiz aiQuiz = new AIQuiz();
        AIQuizQuestion aiQuizQuestion = new AIQuizQuestion();

        AIQuizAnswer aiQuizAnswer = new AIQuizAnswer(1L, "User's Answer", true, user, aiQuiz, aiQuizQuestion);

        // Verifying that the parameterized constructor initializes fields correctly
        assertThat(aiQuizAnswer.getId()).isEqualTo(1L);
        assertThat(aiQuizAnswer.getAIQuizUserAnswer()).isEqualTo("User's Answer");
        assertThat(aiQuizAnswer.isCorrectAnswer()).isTrue();
        assertThat(aiQuizAnswer.getUser()).isEqualTo(user);
        assertThat(aiQuizAnswer.getAiQuiz()).isEqualTo(aiQuiz);
        assertThat(aiQuizAnswer.getAiQuizQuestion()).isEqualTo(aiQuizQuestion);
    }

    @Test
    void testSettersAndGetters() {
        AIQuizAnswer aiQuizAnswer = new AIQuizAnswer();
        User user = new User();
        AIQuiz aiQuiz = new AIQuiz();
        AIQuizQuestion aiQuizQuestion = new AIQuizQuestion();

        aiQuizAnswer.setId(1L);
        aiQuizAnswer.setAIQuizUserAnswer("User's Answer");
        aiQuizAnswer.setCorrectAnswer(true);
        aiQuizAnswer.setUser(user);
        aiQuizAnswer.setAiQuiz(aiQuiz);
        aiQuizAnswer.setAiQuizQuestion(aiQuizQuestion);

        // Verifying the setters and getters
        assertThat(aiQuizAnswer.getId()).isEqualTo(1L);
        assertThat(aiQuizAnswer.getAIQuizUserAnswer()).isEqualTo("User's Answer");
        assertThat(aiQuizAnswer.isCorrectAnswer()).isTrue();
        assertThat(aiQuizAnswer.getUser()).isEqualTo(user);
        assertThat(aiQuizAnswer.getAiQuiz()).isEqualTo(aiQuiz);
        assertThat(aiQuizAnswer.getAiQuizQuestion()).isEqualTo(aiQuizQuestion);
    }

    @Test
    void testToString() {
        User user = new User();
        AIQuiz aiQuiz = new AIQuiz();
        AIQuizQuestion aiQuizQuestion = new AIQuizQuestion();

        AIQuizAnswer aiQuizAnswer = new AIQuizAnswer(1L, "User's Answer", true, user, aiQuiz, aiQuizQuestion);

        String expectedToString = "AIQuizAnswer{" +
                "id=" + aiQuizAnswer.getId() +
                ", AIQuizUserAnswer='" + aiQuizAnswer.getAIQuizUserAnswer() + '\'' +
                ", isCorrectAnswer=" + aiQuizAnswer.isCorrectAnswer() +
                ", user=" + aiQuizAnswer.getUser() +
                ", aiQuiz=" + aiQuizAnswer.getAiQuiz() +
                ", aiQuizQuestion=" + aiQuizAnswer.getAiQuizQuestion() +
                '}';

        // Verifying the toString method
        assertThat(aiQuizAnswer.toString()).isEqualTo(expectedToString);
    }
}
