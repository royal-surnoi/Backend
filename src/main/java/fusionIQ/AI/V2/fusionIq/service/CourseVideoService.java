package fusionIQ.AI.V2.fusionIq.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.exception.VideoNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class CourseVideoService {

    @Autowired
    private final VideoRepo videoRepo;
    @Autowired
    private final CourseRepo courseRepo;
    @Autowired
    private final LessonRepo lessonRepo;
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private LessonModuleRepo lessonModuleRepo;
    @Autowired
    private VideoProgressRepo videoProgressRepo;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    @Autowired
    private  UserRepo userRepo;

    private final String bucketName = "fusion-chat-bk1";
    private final String folderName = "CourseVideos/";
    @Autowired
    public CourseVideoService(VideoRepo videoRepo, CourseRepo courseRepo, LessonRepo lessonRepo) {
        this.videoRepo = videoRepo;
        this.courseRepo = courseRepo;
        this.lessonRepo = lessonRepo;
    }

    public Video uploadVideo(MultipartFile file, Long courseId, Long lessonId) throws IOException {
        String key = folderName + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        File convertedFile = convertMultiPartToFile(file);

        amazonS3.putObject(new PutObjectRequest(bucketName, key, convertedFile));
        String s3Url = amazonS3.getUrl(bucketName, key).toString();
        String duration = getVideoDuration(convertedFile);
        if (!convertedFile.delete()) {
            System.err.println("Failed to delete file: " + convertedFile.getAbsolutePath());
        }


        Video video = new Video();
        video.setVideoTitle(file.getOriginalFilename());
        video.setS3Key(key);
        video.setS3Url(s3Url);
        video.setVideoDuration(duration);
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new UserNotFoundException("Course not found with id " + courseId));
        video.setCourse(course);

        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new UserNotFoundException("Lesson not found with id " + lessonId));
        video.setLesson(lesson);

        return videoRepo.save(video);
    }

    private String getVideoDuration(File videoFile) {
        try {
            MultimediaObject multimediaObject = new MultimediaObject(videoFile);
            MultimediaInfo info = multimediaObject.getInfo();
            long durationInMillis = info.getDuration();

            long hours = (durationInMillis / 3600000);
            long minutes = (durationInMillis % 3600000) / 60000;
            long seconds = (durationInMillis % 60000) / 1000;

            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } catch (InputFormatException e) {
            throw new RuntimeException("Unsupported video format: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error while reading video file: " + e.getMessage());
        }
    }




    public Video newUploadVideo(MultipartFile file, Long courseId, Long lessonId, Long lessonModuleId) throws IOException {
        String key = folderName + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        File convertedFile = convertMultiPartToFile(file);

        amazonS3.putObject(new PutObjectRequest(bucketName, key, convertedFile));
        String s3Url = amazonS3.getUrl(bucketName, key).toString();
        if (!convertedFile.delete()) {
            System.err.println("Warning: Temporary file " + convertedFile.getAbsolutePath() + " could not be deleted.");
        }

        Video video = new Video();
        video.setVideoTitle(file.getOriginalFilename());
        video.setS3Key(key);
        video.setS3Url(s3Url);

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new UserNotFoundException("Course not found with id " + courseId));
        video.setCourse(course);

        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new UserNotFoundException("Lesson not found with id " + lessonId));
        video.setLesson(lesson);

        LessonModule lessonModule = lessonModuleRepo.findById(lessonModuleId)
                .orElseThrow(() -> new UserNotFoundException("Lesson module not found with id " + lessonModuleId));
        video.setLessonModule(lessonModule);

        return videoRepo.save(video);
    }
    public Video uploadDemoVideo(MultipartFile file, Long courseId) throws IOException {
        String key = folderName + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        File convertedFile = convertMultiPartToFile(file);

        amazonS3.putObject(new PutObjectRequest(bucketName, key, convertedFile));
        String s3Url = amazonS3.getUrl(bucketName, key).toString();
        if (!convertedFile.delete()) {
            System.err.println("Warning: Temporary file " + convertedFile.getAbsolutePath() + " could not be deleted.");
        }


        Video video1 = new Video();
        video1.setVideoTitle(file.getOriginalFilename());
        video1.setS3Key(key);
        video1.setS3Url(s3Url);

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new UserNotFoundException("Course not found with id " + courseId));
        video1.setCourse(course);



        return videoRepo.save(video1);
    }

    public List<Video> getVideosByCourse(Long courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new UserNotFoundException("Course not found with id " + courseId));
        return videoRepo.findByCourse(course);
    }

    public List<Video> getVideosByLessonId(Long lessonId) {
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new UserNotFoundException("Lesson not found with id " + lessonId));
        return videoRepo.findByLesson(lesson);
    }

    public Video getVideoById(Long id) {
        Optional<Video> videoOptional = videoRepo.findById(id);
        return videoOptional.orElse(null);
    }

    public Video updateVideo(Long id, MultipartFile file, String videoDescription) throws IOException {
        Optional<Video> videoOptional = videoRepo.findById(id);
        if (videoOptional.isPresent()) {
            Video existingVideo = videoOptional.get();

            existingVideo.setVideoDescription(videoDescription);

            if (file != null && !file.isEmpty()) {
                String key = folderName + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
                File convertedFile = convertMultiPartToFile(file);
                amazonS3.putObject(new PutObjectRequest(bucketName, key, convertedFile));
                String s3Url = amazonS3.getUrl(bucketName, key).toString();
                if (!convertedFile.delete()) {
                    System.err.println("Warning: Temporary file " + convertedFile.getAbsolutePath() + " could not be deleted.");
                }


                existingVideo.setVideoTitle(file.getOriginalFilename());
                existingVideo.setS3Key(key);
                existingVideo.setS3Url(s3Url);
            }

            return videoRepo.save(existingVideo);
        } else {
            throw new UserNotFoundException("Video not found with id " + id);
        }

    }


    public void deleteVideo(Long id) {
        Video video = videoRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Video not found with id " + id));

        String s3Key = video.getS3Key();
        amazonS3.deleteObject(bucketName, s3Key);
        videoRepo.deleteById(id);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }

    public List<Video> getVideosByUserIdAndLevel(Long userId, Course.Level level) {
        return videoRepo.findByUserIdAndLevel(userId, level);
    }

    public List<Video> getVideosByCourseIdAndTitleAndLevel(Long courseId, String courseTitle, Course.Level level) {
        return videoRepo.findByCourseIdAndCourseCourseTitleAndCourseLevel(courseId, courseTitle, level);
    }

    public void saveProgress(Long userId, Long videoId, double progress) {
        VideoProgress videoProgress = videoProgressRepo.findByUserIdAndVideoId(userId, videoId);
        if (videoProgress == null) {
            Video video = videoRepo.findById(videoId)
                    .orElseThrow(() -> new UserNotFoundException("Video not found with id " + videoId));
            User user = new User(userId); // Assuming User entity exists
            videoProgress = new VideoProgress();
            videoProgress.setUser(user);
            videoProgress.setVideo(video);
        }
        videoProgress.setProgress(progress);
        videoProgressRepo.save(videoProgress);
    }

    public VideoProgress getProgress(Long userId, Long videoId) {
        VideoProgress videoProgress = videoProgressRepo.findByUserIdAndVideoId(userId, videoId);
        if (videoProgress != null) {
            return videoProgress;
        }
        return new VideoProgress();
    }

    public void updateProgress(Long userId, Long videoId, double progress) {
        VideoProgress videoProgress = videoProgressRepo.findByUserIdAndVideoId(userId, videoId);
        if (videoProgress == null) {
            Video video = videoRepo.findById(videoId)
                    .orElseThrow(() -> new UserNotFoundException("Video not found with id " + videoId));
            User user = new User(userId); // Assuming User entity exists
            videoProgress = new VideoProgress();
            videoProgress.setUser(user);
            videoProgress.setVideo(video);
        }
        videoProgress.setProgress(progress);
        videoProgressRepo.save(videoProgress);
    }

    public double calculateAverageProgress(Long userId, Long courseId) {
        // Fetch all videos for the course
        List<Video> courseVideos = videoRepo.findByCourseId(courseId);

        if (courseVideos == null || courseVideos.isEmpty()) {
            return 0.0;
        }

        double totalProgress = 0.0;
        int videoCount = courseVideos.size();

        for (Video video : courseVideos) {
            VideoProgress progress = videoProgressRepo.findByUserIdAndVideoId(userId, video.getId());
            if (progress != null) {
                totalProgress += progress.getProgress();
            }
        }

        return totalProgress / videoCount;
    }

    public double calculateLessonProgressByUser(Long userId, Long lessonId) {
        List<Video> lessonVideos = videoRepo.findByLessonId(lessonId);

        if (lessonVideos == null || lessonVideos.isEmpty()) {
            return 0.0;
        }

        double totalProgress = 0.0;
        int videoCount = lessonVideos.size();

        for (Video video : lessonVideos) {
            VideoProgress progress = videoProgressRepo.findByUserIdAndVideoId(userId, video.getId());
            if (progress != null) {
                totalProgress += progress.getProgress();
            }
        }

        return totalProgress / videoCount;
    }

    public double calculateLessonModuleProgress(Long userId, Long lessonModuleId) {
        List<Lesson> lessons = lessonRepo.findByLessonModuleId(lessonModuleId);

        if (lessons == null || lessons.isEmpty()) {
            return 0.0;
        }

        double totalProgress = 0.0;
        int totalLessons = lessons.size();

        for (Lesson lesson : lessons) {
            List<Video> lessonVideos = videoRepo.findByLessonId(lesson.getId());
            if (lessonVideos == null || lessonVideos.isEmpty()) {
                continue;
            }

            double lessonTotalProgress = 0.0;
            int videoCount = lessonVideos.size();

            for (Video video : lessonVideos) {
                VideoProgress progress = videoProgressRepo.findByUserIdAndVideoId(userId, video.getId());
                if (progress != null) {
                    lessonTotalProgress += progress.getProgress();
                }
            }

            totalProgress += (lessonTotalProgress / videoCount);
        }

        return totalProgress / totalLessons;
    }

    public double calculateCourseProgress(Long userId, Long courseId) {
        List<LessonModule> lessonModules = lessonModuleRepo.findByCourseId(courseId);

        if (lessonModules == null || lessonModules.isEmpty()) {
            System.out.println("No lesson modules found for courseId: " + courseId);
            return 0.0;
        }

        System.out.println("Found " + lessonModules.size() + " lesson modules for courseId: " + courseId);

        double totalProgress = 0.0;
        int totalModules = lessonModules.size();

        for (LessonModule lessonModule : lessonModules) {
            double moduleProgress = calculateLessonModuleProgress(userId, lessonModule.getId());

            if (moduleProgress > 0) {
                System.out.println("Progress for lessonModule " + lessonModule.getId() + ": " + moduleProgress);
                totalProgress += moduleProgress;
            }
        }

        double averageProgress = totalModules == 0 ? 0.0 : totalProgress / totalModules;
        System.out.println("Average progress for courseId " + courseId + ": " + averageProgress);
        return averageProgress;
    }



    public Map<String, Double> getEnrolledUserProgress(Long courseId) {

        // Fetch enrollments for the course

        List<Enrollment> enrollments = enrollmentService.findByCourseId(courseId);

        // Initialize the map to store user progress

        Map<String, Double> userProgressMap = new HashMap<>();

        // Check if there are any lessons for the course

        List<Lesson> lessons = lessonRepo.findByCourseId(courseId);

        // If no lessons are found, populate the map with user names and 0.0 progress

        if (lessons == null || lessons.isEmpty()) {

            System.out.println("No lessons found for courseId: " + courseId);

            for (Enrollment enrollment : enrollments) {

                String userName = enrollment.getUser().getName(); // Assuming getName() method returns user's name

                userProgressMap.put(userName, 0.0);

            }

            return userProgressMap;

        }

        // If lessons are found, calculate the progress for each user

        for (Enrollment enrollment : enrollments) {

            Long userId = enrollment.getUser().getId();

            double totalProgress = 0.0;

            int totalVideos = 0;

            for (Lesson lesson : lessons) {

                List<Video> videos = videoRepo.findByLessonId(lesson.getId());

                if (videos != null && !videos.isEmpty()) {

                    totalVideos += videos.size();

                    for (Video video : videos) {

                        VideoProgress videoProgress = videoProgressRepo.findByUserIdAndVideoId(userId, video.getId());

                        if (videoProgress != null && videoProgress.getProgress() > 0) {

                            totalProgress += videoProgress.getProgress();

                        }

                    }

                }

            }

            double progressPercentage = totalVideos == 0 ? 0.0 : totalProgress / totalVideos;

            String userName = enrollment.getUser().getName(); // Assuming getName() method returns user's name

            userProgressMap.put(userName, progressPercentage);

        }

        return userProgressMap;

    }




    private boolean isVideoWatchedByUser(Long userId, Long videoId) {
        VideoProgress videoProgress = videoProgressRepo.findByUserIdAndVideoId(userId, videoId);

        if (videoProgress == null) {
            System.out.println("No progress found for userId: " + userId + " and videoId: " + videoId);
            return false;
        }

        return videoProgress.getProgress() == 100.0;
    }

    public Map<String, Double> getEnrolledUsersProgress(Long courseId) {
        List<LessonModule> lessonModules = lessonModuleRepo.findByCourseId(courseId);

        if (lessonModules == null || lessonModules.isEmpty()) {
            System.out.println("No lesson modules found for courseId: " + courseId);
            return Collections.emptyMap();
        }

        Map<Long, Double> userProgressMap = new HashMap<>();

        List<Enrollment> enrollments = enrollmentRepo.findByCourseId(courseId);
        for (Enrollment enrollment : enrollments) {
            Long userId = enrollment.getUser().getId();
            double totalProgress = 0.0;
            int totalModules = lessonModules.size();

            for (LessonModule lessonModule : lessonModules) {
                double moduleProgress = calculateLessonModulesProgress(userId, lessonModule.getId());
                if (moduleProgress > 0) {
                    totalProgress += moduleProgress;
                }
            }

            double averageProgress = totalModules == 0 ? 0.0 : totalProgress / totalModules;
            userProgressMap.put(userId, averageProgress);
        }

        // Map user IDs to names
        Map<String, Double> userNameProgressMap = new HashMap<>();
        for (Map.Entry<Long, Double> entry : userProgressMap.entrySet()) {
            Long userId = entry.getKey();
            Double progress = entry.getValue();
            String userName = userRepo.findById(userId).map(User::getName).orElse("Unknown");
            userNameProgressMap.put(userName, progress);
        }

        return userNameProgressMap;
    }

    private double calculateLessonModulesProgress(Long userId, Long lessonModuleId) {
        List<Video> videos = videoRepo.findByLessonModuleId(lessonModuleId);

        if (videos == null || videos.isEmpty()) {
            return 0.0;
        }

        double totalProgress = 0.0;
        int totalVideos = videos.size();

        for (Video video : videos) {
            VideoProgress videoProgress = videoProgressRepo.findByUserIdAndVideoId(userId, video.getId());
            if (videoProgress != null && videoProgress.getProgress() > 0) {
                totalProgress += videoProgress.getProgress();
            }
        }

        return totalVideos == 0 ? 0.0 : totalProgress / totalVideos;
    }

    public double calculateCourseProgressByUser(Long userId, Long courseId) {
        List<Lesson> courseLessons = lessonRepo.findByCourseId(courseId);

        if (courseLessons == null || courseLessons.isEmpty()) {
            System.out.println("No lessons found for course ID: " + courseId);
            return 0.0;
        }

        double totalProgress = 0.0;
        int lessonCount = courseLessons.size();

        for (Lesson lesson : courseLessons) {
            List<Video> lessonVideos = videoRepo.findByLessonId(lesson.getId());

            if (lessonVideos == null || lessonVideos.isEmpty()) {
                System.out.println("No videos found for lesson ID: " + lesson.getId());
                continue;
            }

            double lessonTotalProgress = 0.0;
            int videoCount = lessonVideos.size();

            for (Video video : lessonVideos) {
                VideoProgress progress = videoProgressRepo.findByUserIdAndVideoId(userId, video.getId());
                if (progress != null) {
                    System.out.println("Video ID: " + video.getId() + ", Progress: " + progress.getProgress());
                    lessonTotalProgress += progress.getProgress();
                } else {
                    System.out.println("Video ID: " + video.getId() + " has no progress data for user ID: " + userId);
                }
            }

            double lessonProgressPercentage = (lessonTotalProgress / videoCount);
            System.out.println("Lesson ID: " + lesson.getId() + ", Progress Percentage: " + lessonProgressPercentage);
            totalProgress += lessonProgressPercentage;
        }

        double courseProgress = totalProgress / lessonCount;
        System.out.println("Total Course Progress: " + courseProgress);
        return courseProgress;
    }

    public void saveOrUpdateProgress(Long userId, Long videoId, double progress) {
        VideoProgress videoProgress = videoProgressRepo.findByUserIdAndVideoId(userId, videoId);
        if (videoProgress == null) {
            // No existing progress, create a new one
            Video video = videoRepo.findById(videoId)
                    .orElseThrow(() -> new RuntimeException("Video not found with id " + videoId));
            User user = new User(userId); // Assuming User entity exists
            videoProgress = new VideoProgress();
            videoProgress.setUser(user);
            videoProgress.setVideo(video);
        }
        // Update progress
        videoProgress.setProgress(progress);
        videoProgressRepo.save(videoProgress);
    }

    public String getTotalCourseDuration(Long courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new UserNotFoundException("Course not found with id " + courseId));

        // Get all videos related to the course
        List<Video> videos = videoRepo.findByCourse(course);

        // Sum the durations
        long totalSeconds = 0;
        for (Video video : videos) {
            String duration = video.getVideoDuration();

            // Skip if duration is null or empty
            if (duration != null && !duration.isEmpty()) {
                totalSeconds += convertDurationToSeconds(duration);
            }
        }

        return convertSecondsToDuration(totalSeconds);
    }

    // Convert "HH:mm:ss" string to seconds
    private long convertDurationToSeconds(String duration) {
        try {
            String[] parts = duration.split(":");
            long hours = Long.parseLong(parts[0]);
            long minutes = Long.parseLong(parts[1]);
            long seconds = Long.parseLong(parts[2]);
            return hours * 3600 + minutes * 60 + seconds;
        } catch (Exception e) {
            // Log or handle invalid duration formats gracefully
            throw new IllegalArgumentException("Invalid video duration format: " + duration);
        }
    }

    // Convert seconds back to "HH:mm:ss" format
    private String convertSecondsToDuration(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    public List<Map<String, String>> getVideoDetails(Long courseId, Long lessonId) {
        List<Object[]> results = videoRepo.findVideoDetailsByCourseIdAndLessonId(courseId, lessonId);
        List<Map<String, String>> videoDetails = new ArrayList<>();

        if (results != null) {
            for (Object[] result : results) {
                Map<String, String> videoDetail = new HashMap<>();
                videoDetail.put("videoTitle", (String) result[0]);
                videoDetail.put("videoDescription", (String) result[1]);
                videoDetail.put("transcript", (String) result[2]);
                videoDetails.add(videoDetail);
            }
        }
        return videoDetails;
    }

    public Video updateTranscriptById(Long id, String transcript) {
        Optional<Video> videoOptional = videoRepo.findById(id);

        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            video.setTranscript(transcript);
            return videoRepo.save(video);
        } else {
            throw new VideoNotFoundException("Video with ID " + id + " not found.");
        }
    }
}
