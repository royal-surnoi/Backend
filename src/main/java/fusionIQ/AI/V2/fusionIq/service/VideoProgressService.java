package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.VideoProgress;
import fusionIQ.AI.V2.fusionIq.repository.EnrollmentRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.repository.VideoProgressRepo;
import fusionIQ.AI.V2.fusionIq.repository.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoProgressService {

    @Autowired
    private VideoProgressRepo videoProgressRepo;

   @Autowired
   private VideoRepo videoRepo;

   @Autowired
   private EnrollmentRepo enrollmentRepo;

   @Autowired
    UserRepo userRepo;

    public void trackVideoProgress(VideoProgress videoProgress) {
        if (!videoProgressRepo.existsByUserIdAndVideoId(videoProgress.getUser().getId(), videoProgress.getVideo().getId())) {
            videoProgressRepo.save(videoProgress);
        }
    }

    public List<VideoProgress> getUserCourseProgress(long userId, long courseId) {
        return videoProgressRepo.findByUserIdAndCourseId(userId, courseId);
    }

    public double calculateProgressPercentage(long userId, long courseId) {
        long totalVideos = videoRepo.countByCourseId(courseId);
        long watchedVideos = videoProgressRepo.countByUserIdAndCourseId(userId, courseId);

        if (totalVideos == 0) {
            return 0;
        }

        return (double) watchedVideos / totalVideos * 100;
    }

    public double calculateAllCourseProgressPercentage(long userId, long courseId) {
        long totalVideos = videoRepo.countByCourseId(courseId);
        long watchedVideos = videoProgressRepo.countByUserIdAndCourseId(userId, courseId);

        System.out.println("Total videos: " + totalVideos + ", Watched videos: " + watchedVideos);

        if (totalVideos == 0) {
            return 0;
        }

        return (double) watchedVideos / totalVideos * 100;
    }
    public List<VideoProgress> getUserLessonProgress(long userId, long lessonId) {
        return videoProgressRepo.findByUserIdAndLessonId(userId, lessonId);
    }

    public double calculateProgressPercentageByLesson(long userId, long lessonId) {
        long totalVideos = videoRepo.countByLessonId(lessonId);
        long watchedVideos = videoProgressRepo.countByUserIdAndLessonId(userId, lessonId);

        if (totalVideos == 0) {
            return 0;
        }

        return (double) watchedVideos / totalVideos * 100;
    }


public Map<Long, Map<String, Object>> calculateProgressPercentageForAllUsersByCourse(Long courseId) {
    List<Long> enrolledUserIds = enrollmentRepo.findUserIdsByCourseId(courseId);
    long totalVideos = videoRepo.countByCourseId(courseId);

    Map<Long, Map<String, Object>> userProgressMap = new HashMap<>();

    for (Long userId : enrolledUserIds) {
        User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            long watchedVideos = videoProgressRepo.countByUserIdAndCourseId(userId, courseId);
            double progressPercentage = totalVideos == 0 ? 0 : (double) watchedVideos / totalVideos * 100;

            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("name", user.getName());
            userDetails.put("progress", progressPercentage);

            userProgressMap.put(userId, userDetails);
        }
    }

    return userProgressMap;
}


    public Map<String, Object> calculateProgressPercentageForAllCoursesByUser(Long userId) {
        List<Long> enrolledCourseIds = enrollmentRepo.findCourseIdsByUserId(userId);
        User user = userRepo.findById(userId).orElse(null);

        Map<String, Object> userProgressMap = new HashMap<>();

        if (user != null) {
            userProgressMap.put("userName", user.getName());

            Map<Long, Map<String, Object>> courseProgressMap = new HashMap<>();

            for (Long courseId : enrolledCourseIds) {
                long totalVideos = videoRepo.countByCourseId(courseId);
                long watchedVideos = videoProgressRepo.countByUserIdAndCourseId(userId, courseId);
                double progressPercentage = totalVideos == 0 ? 0 : (double) watchedVideos / totalVideos * 100;

                Map<String, Object> courseDetails = new HashMap<>();
                courseDetails.put("courseId", courseId);
                courseDetails.put("progress", progressPercentage);

                courseProgressMap.put(courseId, courseDetails);
            }

            userProgressMap.put("courses", courseProgressMap);
        }

        return userProgressMap;
    }

    public double calculateProgressPercentageByLessonModule(long userId, long lessonModuleId) {
        long totalVideos = videoRepo.countByLessonModuleId(lessonModuleId);
        long watchedVideos = videoProgressRepo.countByUserIdAndLessonModuleId(userId, lessonModuleId);

        if (totalVideos == 0) {
            return 0;
        }

        return (double) watchedVideos / totalVideos * 100;
    }
}
