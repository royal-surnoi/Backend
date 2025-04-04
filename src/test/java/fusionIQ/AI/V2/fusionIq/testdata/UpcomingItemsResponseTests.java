package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Assignment;
import fusionIQ.AI.V2.fusionIq.data.Project;
import fusionIQ.AI.V2.fusionIq.data.Quiz;
import fusionIQ.AI.V2.fusionIq.data.UpcomingItemsResponse;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UpcomingItemsResponseTests {

    @Test
    void testDefaultConstructor() {
        UpcomingItemsResponse response = new UpcomingItemsResponse();

        assertThat(response.getAssessments()).isNull();
        assertThat(response.getQuizzes()).isNull();
        assertThat(response.getProjects()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        Assignment assignment = new Assignment(); // Replace with actual initialization
        Quiz quiz = new Quiz(); // Replace with actual initialization
        Project project = new Project(); // Replace with actual initialization

        List<Assignment> assignments = List.of(assignment);
        List<Quiz> quizzes = List.of(quiz);
        List<Project> projects = List.of(project);

        UpcomingItemsResponse response = new UpcomingItemsResponse(assignments, quizzes, projects);

        assertThat(response.getAssessments()).isEqualTo(assignments);
        assertThat(response.getQuizzes()).isEqualTo(quizzes);
        assertThat(response.getProjects()).isEqualTo(projects);
    }

    @Test
    void testSettersAndGetters() {
        Assignment assignment = new Assignment(); // Replace with actual initialization
        Quiz quiz = new Quiz(); // Replace with actual initialization
        Project project = new Project(); // Replace with actual initialization

        List<Assignment> assignments = List.of(assignment);
        List<Quiz> quizzes = List.of(quiz);
        List<Project> projects = List.of(project);

        UpcomingItemsResponse response = new UpcomingItemsResponse();
        response.setAssessments(assignments);
        response.setQuizzes(quizzes);
        response.setProjects(projects);

        assertThat(response.getAssessments()).isEqualTo(assignments);
        assertThat(response.getQuizzes()).isEqualTo(quizzes);
        assertThat(response.getProjects()).isEqualTo(projects);
    }
}
