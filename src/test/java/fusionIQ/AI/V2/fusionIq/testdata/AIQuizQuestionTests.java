package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.AIQuiz;
import fusionIQ.AI.V2.fusionIq.data.AIQuizQuestion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AIQuizQuestionTests {

    @Test
    void testDefaultConstructor() {
        AIQuizQuestion aiQuizQuestion = new AIQuizQuestion();

        // Verifying that the default constructor initializes fields correctly
        assertThat(aiQuizQuestion.getId()).isNull();
        assertThat(aiQuizQuestion.getAIOption1()).isNull();
        assertThat(aiQuizQuestion.getAIOption2()).isNull();
        assertThat(aiQuizQuestion.getAIOption3()).isNull();
        assertThat(aiQuizQuestion.getAIOption4()).isNull();
        assertThat(aiQuizQuestion.getAIQuizCorrectAnswer()).isNull();
        assertThat(aiQuizQuestion.getAIQuizQuestion()).isNull();
        assertThat(aiQuizQuestion.getAIQuizAnswerDescription()).isNull();
        assertThat(aiQuizQuestion.getAiQuiz()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        AIQuiz aiQuiz = new AIQuiz();

        AIQuizQuestion aiQuizQuestion = new AIQuizQuestion(
                1L,
                "Option 1",
                "Option 2",
                "Option 3",
                "Option 4",
                "Correct Answer",
                "What is the capital of France?",
                "Paris is the capital of France.",
                aiQuiz
        );

        // Verifying that the parameterized constructor initializes fields correctly
        assertThat(aiQuizQuestion.getId()).isEqualTo(1L);
        assertThat(aiQuizQuestion.getAIOption1()).isEqualTo("Option 1");
        assertThat(aiQuizQuestion.getAIOption2()).isEqualTo("Option 2");
        assertThat(aiQuizQuestion.getAIOption3()).isEqualTo("Option 3");
        assertThat(aiQuizQuestion.getAIOption4()).isEqualTo("Option 4");
        assertThat(aiQuizQuestion.getAIQuizCorrectAnswer()).isEqualTo("Correct Answer");
        assertThat(aiQuizQuestion.getAIQuizQuestion()).isEqualTo("What is the capital of France?");
        assertThat(aiQuizQuestion.getAIQuizAnswerDescription()).isEqualTo("Paris is the capital of France.");
        assertThat(aiQuizQuestion.getAiQuiz()).isEqualTo(aiQuiz);
    }

    @Test
    void testSettersAndGetters() {
        AIQuiz aiQuiz = new AIQuiz();
        AIQuizQuestion aiQuizQuestion = new AIQuizQuestion();

        aiQuizQuestion.setId(1L);
        aiQuizQuestion.setAIOption1("Option 1");
        aiQuizQuestion.setAIOption2("Option 2");
        aiQuizQuestion.setAIOption3("Option 3");
        aiQuizQuestion.setAIOption4("Option 4");
        aiQuizQuestion.setAIQuizCorrectAnswer("Correct Answer");
        aiQuizQuestion.setAIQuizQuestion("What is the capital of France?");
        aiQuizQuestion.setAIQuizAnswerDescription("Paris is the capital of France.");
        aiQuizQuestion.setAiQuiz(aiQuiz);

        // Verifying the setters and getters
        assertThat(aiQuizQuestion.getId()).isEqualTo(1L);
        assertThat(aiQuizQuestion.getAIOption1()).isEqualTo("Option 1");
        assertThat(aiQuizQuestion.getAIOption2()).isEqualTo("Option 2");
        assertThat(aiQuizQuestion.getAIOption3()).isEqualTo("Option 3");
        assertThat(aiQuizQuestion.getAIOption4()).isEqualTo("Option 4");
        assertThat(aiQuizQuestion.getAIQuizCorrectAnswer()).isEqualTo("Correct Answer");
        assertThat(aiQuizQuestion.getAIQuizQuestion()).isEqualTo("What is the capital of France?");
        assertThat(aiQuizQuestion.getAIQuizAnswerDescription()).isEqualTo("Paris is the capital of France.");
        assertThat(aiQuizQuestion.getAiQuiz()).isEqualTo(aiQuiz);
    }

    @Test
    void testToString() {
        AIQuiz aiQuiz = new AIQuiz();
        AIQuizQuestion aiQuizQuestion = new AIQuizQuestion(
                1L,
                "Option 1",
                "Option 2",
                "Option 3",
                "Option 4",
                "Correct Answer",
                "What is the capital of France?",
                "Paris is the capital of France.",
                aiQuiz
        );

        String expectedToString = "AIQuizQuestion{" +
                "id=" + aiQuizQuestion.getId() +
                ", AIOption1='" + aiQuizQuestion.getAIOption1() + '\'' +
                ", AIOption2='" + aiQuizQuestion.getAIOption2() + '\'' +
                ", AIOption3='" + aiQuizQuestion.getAIOption3() + '\'' +
                ", AIOption4='" + aiQuizQuestion.getAIOption4() + '\'' +
                ", AIQuizCorrectAnswer='" + aiQuizQuestion.getAIQuizCorrectAnswer() + '\'' +
                ", AIQuizQuestion='" + aiQuizQuestion.getAIQuizQuestion() + '\'' +
                ", AIQuizAnswerDescription='" + aiQuizQuestion.getAIQuizAnswerDescription() + '\'' +
                ", aiQuiz=" + aiQuiz +
                '}';

        // Verifying the toString method
        assertThat(aiQuizQuestion.toString()).isEqualTo(expectedToString);
    }
}
