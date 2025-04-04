package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.QuizProgress;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.QuizProgressRepo;
import fusionIQ.AI.V2.fusionIq.repository.QuizRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizProgressService {
    @Autowired
    private QuizProgressRepo quizProgressRepo;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private UserRepo userRepo;


    public List<QuizProgress> getUserProgress(long userId) {
        return quizProgressRepo.findByUserId(userId);
    }

    public List<QuizProgress> getUserLessonProgress(long userId, long lessonId) {
        return quizProgressRepo.findByUserIdAndQuizLessonId(userId, lessonId);
    }
    public List<QuizProgress> getUserLessonModuleProgress(long userId, long lessonModuleId) {
        return quizProgressRepo.findByUserIdAndQuizLessonModuleId(userId, lessonModuleId);
    }

    public double calculateProgressPercentage(long userId, long lessonModuleId) {
        long totalQuizzes = quizRepo.countByLessonModuleId(lessonModuleId);
        long completedQuizzes = quizProgressRepo.countByUserIdAndQuizLessonModuleId(userId, lessonModuleId);

        if (totalQuizzes == 0) {
            return 0;
        }

        return (double) completedQuizzes / totalQuizzes * 100;
    }

    public String calculateCompletedQuizRatio(long userId, long lessonModuleId) {
        long totalQuizzes = quizRepo.countByLessonModuleId(lessonModuleId);
        long completedQuizzes = quizProgressRepo.countByUserIdAndQuizLessonModuleId(userId, lessonModuleId);

        return completedQuizzes + "/" + totalQuizzes;
    }

    public double calculateProgressPercentageForLesson(long userId, long lessonId) {
        long totalQuizzes = quizRepo.countByLessonId(lessonId);
        long completedQuizzes = quizProgressRepo.countByUserIdAndQuizLessonId(userId, lessonId);

        if (totalQuizzes == 0) {
            return 0;
        }

        return (double) completedQuizzes / totalQuizzes * 100;
    }

    public String calculateCompletedQuizRatioForLesson(long userId, long lessonId) {
        long totalQuizzes = quizRepo.countByLessonId(lessonId);
        long completedQuizzes = quizProgressRepo.countByUserIdAndQuizLessonId(userId, lessonId);

        return completedQuizzes + "/" + totalQuizzes;
    }

    public String getQuizProgressByCourseAndUser(Long courseId, Long userId) {
        List<QuizProgress> progressList = quizProgressRepo.findByCourseIdAndUserId(courseId, userId);
        long totalQuizzes = progressList.size();
        long completedQuizzes = progressList.stream().filter(QuizProgress::isCompleted).count();

        return completedQuizzes + "/" + totalQuizzes;
    }

    public List<User> getUsersWhoSubmittedQuiz(Long quizId) {
        List<Long> userIds = quizProgressRepo.findUserIdsByQuizIdAndCompleted(quizId, true);
        return userRepo.findAllById(userIds);
    }

    public String calculateCompletedQuizRatioForCourse( Long courseId) {
        long totalQuizzes = quizRepo.countByCourseId(courseId);
        long completedQuizzes = quizProgressRepo.countByQuizCourseId(courseId);

        return completedQuizzes + "/" + totalQuizzes;
    }

    public boolean hasQuizProgress(Long quizId, Long userId) {
        return quizProgressRepo.existsByQuizIdAndUserId(quizId, userId);
    }

    public void trackQuizProgress(QuizProgress quizProgress) {
        quizProgressRepo.save(quizProgress);
    }
}
