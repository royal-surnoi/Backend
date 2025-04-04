package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobQuizService {
    private final JobQuizRepository jobQuizRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobRepository jobRepository;
    private final ShortlistedCandidatesRepository shortlistedCandidatesRepository;
    private final QuestionRepo questionRepo;
    private AnswerRepo answerRepo;
    private UserRepo userRepo;

    public JobQuizService(JobQuizRepository jobQuizRepository,
                          RecruiterRepository recruiterRepository, AnswerRepo answerRepo,
                          JobRepository jobRepository,
                          ShortlistedCandidatesRepository shortlistedCandidatesRepository, QuestionRepo questionRepo) {
        this.jobQuizRepository = jobQuizRepository;
        this.recruiterRepository = recruiterRepository;
        this.jobRepository = jobRepository;
        this.shortlistedCandidatesRepository = shortlistedCandidatesRepository;
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
    }

    public JobQuiz createJobQuizForUsers(Long recruiterId, Long jobId, List<Long> userIds,
                                         String quizName, LocalDate startDate, LocalDate endDate) {
        JobQuiz jobQuiz = new JobQuiz();
        jobQuiz.setQuizName(quizName);
        jobQuiz.setStartDate(startDate);
        jobQuiz.setEndDate(endDate);
        jobQuiz.setRecruiterId(recruiterId);
        jobQuiz.setJobId(jobId);

        // Do not set adminId for this API
        // jobQuiz.setAdminId(null); // This is now optional and null by default

        // Save JobQuiz
        return jobQuizRepository.save(jobQuiz);
    }





    public List<Question> addQuestionsToJobQuiz(Long jobQuizId, Long jobId, List<Question> questions) {
        // Fetch the job quiz
        JobQuiz jobQuiz = jobQuizRepository.findById(jobQuizId)
                .orElseThrow(() -> new ResourceNotFoundException("JobQuiz not found with ID: " + jobQuizId));

        // Validate and associate each question with the job quiz
        questions.forEach(question -> {
            if (question.getText() == null || question.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Question text cannot be null or empty.");
            }
            question.setJobQuiz(jobQuiz);
        });

        // Save all questions and return the saved list
        return questionRepo.saveAll(questions);
    }




    public List<JobQuiz> getJobQuizzesByUserAndJob(Long jobId, Long userId) {
        // Fetch quizzes directly by jobId and userId from the database
        return jobQuizRepository.findByJobIdAndUserId(jobId, userId);
    }




    public List<JobQuiz> getJobQuizByUserId(Long userId) {
        return jobQuizRepository.findByShortlistedCandidates_User_Id(userId);
    }

    public List<JobQuiz> getJobQuizByUserIds(List<Long> userIds) {
        return jobQuizRepository.findByShortlistedCandidates_User_IdIn(userIds);
    }

    public JobQuiz createJobQuizForUsers(Long recruiterId, Long jobId, String userIds,
                                         String quizName, LocalDate startDate, LocalDate endDate) {
        JobQuiz jobQuiz = new JobQuiz();
        jobQuiz.setQuizName(quizName);
        jobQuiz.setStartDate(startDate);
        jobQuiz.setEndDate(endDate);
        jobQuiz.setRecruiterId(recruiterId);
        jobQuiz.setJobId(jobId);
        jobQuiz.setUserIds(userIds);

        return jobQuizRepository.save(jobQuiz);
    }


    public JobQuiz createQuizWithQuestionsAndOptionalRecruiter(
            Long adminId,
            Long jobId,
            Long recruiterId,
            String quizName,
            LocalDate startDate,
            LocalDate endDate,
            String userIds,
            List<Map<String, Object>> questionsData
    ) {
        JobQuiz jobQuiz = new JobQuiz();
        jobQuiz.setAdminId(adminId);
        jobQuiz.setJobId(jobId);
        jobQuiz.setRecruiterId(recruiterId);
        jobQuiz.setQuizName(quizName);
        jobQuiz.setStartDate(startDate);
        jobQuiz.setEndDate(endDate);
        jobQuiz.setUserIds(userIds);

        jobQuiz = jobQuizRepository.save(jobQuiz);

        for (Map<String, Object> questionData : questionsData) {
            Question question = new Question();
            question.setText((String) questionData.get("text"));
            question.setCorrectAnswer((String) questionData.get("correctAnswer"));

            List<String> options = (List<String>) questionData.get("options");
            question.setOptionA(options.get(0));
            question.setOptionB(options.get(1));
            question.setOptionC(options.get(2));
            question.setOptionD(options.get(3));

            question.setJobQuiz(jobQuiz);
            questionRepo.save(question);
        }

        return jobQuiz;
    }

}
