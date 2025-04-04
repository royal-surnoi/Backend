package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.AIAssignment;
import fusionIQ.AI.V2.fusionIq.data.AIGrade;
import fusionIQ.AI.V2.fusionIq.data.AIQuiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AIGradeTests {

    private AIGrade aiGrade;
    private AIAssignment aiAssignment;
    private AIQuiz aiQuiz;

    @BeforeEach
    void setUp() {
        aiAssignment = new AIAssignment(); // Assuming AIAssignment class is defined elsewhere
        aiAssignment.setId(1L);

        aiQuiz = new AIQuiz(); // Assuming AIQuiz class is defined elsewhere
        aiQuiz.setId(1L);

        aiGrade = new AIGrade();
    }

    @Test
    void testDefaultConstructor() {
        assertThat(aiGrade.getAIGrade()).isNull();
        assertThat(aiGrade.getAIFeedback()).isNull();
        assertThat(aiGrade.getAiAssignment()).isNull();
        assertThat(aiGrade.getAiQuiz()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        AIGrade aiGrade = new AIGrade(1L, "A", "Excellent work", aiAssignment, aiQuiz);

        assertThat(aiGrade.getId()).isEqualTo(1L);
        assertThat(aiGrade.getAIGrade()).isEqualTo("A");
        assertThat(aiGrade.getAIFeedback()).isEqualTo("Excellent work");
        assertThat(aiGrade.getAiAssignment()).isEqualTo(aiAssignment);
        assertThat(aiGrade.getAiQuiz()).isEqualTo(aiQuiz);
    }

    @Test
    void testSettersAndGetters() {
        aiGrade.setId(2L);
        aiGrade.setAIGrade("B");
        aiGrade.setAIFeedback("Good job");
        aiGrade.setAiAssignment(aiAssignment);
        aiGrade.setAiQuiz(aiQuiz);

        assertThat(aiGrade.getId()).isEqualTo(2L);
        assertThat(aiGrade.getAIGrade()).isEqualTo("B");
        assertThat(aiGrade.getAIFeedback()).isEqualTo("Good job");
        assertThat(aiGrade.getAiAssignment()).isEqualTo(aiAssignment);
        assertThat(aiGrade.getAiQuiz()).isEqualTo(aiQuiz);
    }

    @Test
    void testToString() {
        aiGrade.setId(1L);
        aiGrade.setAIGrade("A");
        aiGrade.setAIFeedback("Excellent work");
        aiGrade.setAiAssignment(aiAssignment);
        aiGrade.setAiQuiz(aiQuiz);

        String expected = "AIGrade{id=1, AIGrade='A', AIFeedback='Excellent work', aiAssignment=" + aiAssignment + ", aiQuiz=" + aiQuiz + "}";
        assertThat(aiGrade.toString()).isEqualTo(expected);
    }
}
