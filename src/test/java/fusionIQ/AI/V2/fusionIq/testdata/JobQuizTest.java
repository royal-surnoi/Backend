package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.JobQuiz;
import fusionIQ.AI.V2.fusionIq.data.Question;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class JobQuizTest {
    @Test
    void testJobQuizConstructorAndGetters() {
        // Arrange
        Long id = 1L;
        String quizName = "Java Test";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);
        Long recruiterId = 2L;
        Long jobId = 3L;
        String userIds = "1,2,3";
        String shortlistedCandidateIds = "4,5,6";
        Long adminId = 4L;

        // Act
        JobQuiz jobQuiz = new JobQuiz(id, quizName, startDate, endDate, recruiterId,
                jobId, userIds, shortlistedCandidateIds, adminId, null, null, null, null, null, null);

        // Assert
        assertEquals(id, jobQuiz.getId());
        assertEquals(quizName, jobQuiz.getQuizName());
        assertEquals(startDate, jobQuiz.getStartDate());
        assertEquals(endDate, jobQuiz.getEndDate());
        assertEquals(recruiterId, jobQuiz.getRecruiterId());
        assertEquals(jobId, jobQuiz.getJobId());
        assertEquals(userIds, jobQuiz.getUserIds());
        assertEquals(shortlistedCandidateIds, jobQuiz.getShortlistedCandidateIds());
        assertEquals(adminId, jobQuiz.getAdminId());
    }

    @Test
    void testSetters() {
        // Arrange
        JobQuiz jobQuiz = new JobQuiz();
        Long id = 1L;
        String quizName = "Java Test";

        // Act
        jobQuiz.setId(id);
        jobQuiz.setQuizName(quizName);

        // Assert
        assertEquals(id, jobQuiz.getId());
        assertEquals(quizName, jobQuiz.getQuizName());
    }

    @Test
    void testRelationships() {
        // Arrange
        JobQuiz jobQuiz = new JobQuiz();
        List<Question> questions = new ArrayList<>();
        Question question = new Question();
        questions.add(question);

        // Act
        jobQuiz.setQuestions(questions);

        // Assert
        assertEquals(1, jobQuiz.getQuestions().size());
        assertEquals(question, jobQuiz.getQuestions().get(0));
    }
}