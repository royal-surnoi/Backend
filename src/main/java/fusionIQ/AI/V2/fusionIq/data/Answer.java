package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "job_quiz_id")
    private JobQuiz jobQuiz;

@ManyToOne
private JobQuizProgress jobQuizProgress;

    private String selectedAnswer;

    private boolean isCorrect;

    public Answer() {

    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public JobQuiz getJobQuiz() {
        return jobQuiz;
    }
    public void setJobQuiz(JobQuiz jobQuiz) {
        this.jobQuiz = jobQuiz;
    }

    public JobQuizProgress getJobQuizProgress() {
        return jobQuizProgress;
    }

    public void setJobQuizProgress(JobQuizProgress jobQuizProgress) {
        this.jobQuizProgress = jobQuizProgress;
    }

    public Answer(Long id, User user, Quiz quiz,JobQuizProgress jobQuizProgress, JobQuiz jobQuiz, Question question, Course course, User student, String selectedAnswer, boolean isCorrect) {
        this.id = id;
        this.user = user;
        this.quiz = quiz;
        this.jobQuiz = jobQuiz;
        this.question = question;
        this.course = course;
        this.student = student;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
        this.jobQuizProgress = jobQuizProgress;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", user=" + user +
                ", quiz=" + quiz +
                ", jobQuiz=" + jobQuiz +
                ", jobQuizProgress=" + jobQuizProgress +
                ", question=" + question +
                ", course=" + course +
                ", student=" + student +
                ", selectedAnswer='" + selectedAnswer + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}