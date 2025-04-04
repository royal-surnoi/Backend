package fusionIQ.AI.V2.fusionIq.data;

import java.util.List;

public class UpcomingItemsResponse {
    private List<Assignment> assessments;
    private List<Quiz> quizzes;
    private List<Project> projects;

    public UpcomingItemsResponse() {}

    public UpcomingItemsResponse(List<Assignment> assessments, List<Quiz> quizzes, List<Project> projects) {
        this.assessments = assessments;
        this.quizzes = quizzes;
        this.projects = projects;
    }

    public List<Assignment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assignment> assessments) {
        this.assessments = assessments;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
