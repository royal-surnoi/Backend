package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.VideoRepo;
import fusionIQ.AI.V2.fusionIq.service.CourseVideoService;
import fusionIQ.AI.V2.fusionIq.service.EnrollmentService;
import fusionIQ.AI.V2.fusionIq.service.VideoProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/video")
public class CourseVideoController {
    @Autowired
    private CourseVideoService courseVideoService;
    @Autowired
    private VideoRepo videoRepo;
    @Autowired
    private VideoProgressService videoProgressService;

    @Autowired
    private EnrollmentService enrollmentService;




    @PostMapping("/upload/{courseId}/{lessonId}")
    public ResponseEntity<String> uploadVideo(@PathVariable Long courseId, @PathVariable Long lessonId, @RequestParam("file") MultipartFile file) {
        try {
            Video video = courseVideoService.uploadVideo(file, courseId, lessonId);
            return new ResponseEntity<>("File uploaded successfully, URL: " + video.getS3Url(), HttpStatus.OK);
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/upload/{courseId}")
    public ResponseEntity<Map<String, String>> uploadDemoVideo(@PathVariable Long courseId, @RequestParam("file") MultipartFile file) {
        try {
            Video video = courseVideoService.uploadDemoVideo(file, courseId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "File uploaded successfully");
            response.put("url", video.getS3Url());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("message", "File upload failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/courses/videos/{courseId}")
    public ResponseEntity<List<Video>> getVideosByCourse(@PathVariable Long courseId) {
        try {
            List<Video> videos = courseVideoService.getVideosByCourse(courseId);
            return ResponseEntity.ok(videos);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/videos")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoRepo.findAll();
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/get/videos/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable Long id) {
        Optional<Video> video = videoRepo.getVideoById(id);
        return video.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/videos/{id}")
    public ResponseEntity<Video> updateVideo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) {
        try {
            Video updatedVideo = courseVideoService.updateVideo(id, file, description);
            return ResponseEntity.ok(updatedVideo);
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable Long id) {
        try {
            courseVideoService.deleteVideo(id);
            return new ResponseEntity<>("Video deleted successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Video not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Video deletion failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/upload/{courseId}/{lessonId}/{lessonModuleId}")
    public ResponseEntity<String> newUploadVideo(@PathVariable Long courseId, @PathVariable Long lessonId, @PathVariable Long lessonModuleId, @RequestParam("file") MultipartFile file) {
        try {
            Video video = courseVideoService.newUploadVideo(file, courseId, lessonId, lessonModuleId);
            return new ResponseEntity<>("File uploaded successfully, URL: " + video.getS3Url(), HttpStatus.OK);
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/progress/{videoId}")
    public ResponseEntity<String> markVideoAsWatched(@PathVariable Long videoId, @RequestParam Long userId) {
        try {
            Optional<Video> videoOptional = videoRepo.findById(videoId);
            if (!videoOptional.isPresent()) {
                return new ResponseEntity<>("Video not found", HttpStatus.NOT_FOUND);
            }

            Video video = videoOptional.get();
            VideoProgress videoProgress = new VideoProgress();
            videoProgress.setUser(new User(userId)); // Ensure User has a constructor with id
            videoProgress.setVideo(video);
            videoProgress.setCourse(video.getCourse());
            videoProgress.setLesson(video.getLesson()); // Add lesson to VideoProgress

            videoProgressService.trackVideoProgress(videoProgress);

            return new ResponseEntity<>("Video marked as watched", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to mark video as watched: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progress/user/{userId}/course/{courseId}")
    public ResponseEntity<Double> getUserCourseProgress(@PathVariable Long userId, @PathVariable Long courseId) {
        try {
            double progress = videoProgressService.calculateProgressPercentage(userId, courseId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/progress/percentage/user/{userId}/course/{courseId}")
    public ResponseEntity<Double> getUserCourseProgressPercentage(@PathVariable Long userId, @PathVariable Long courseId) {
        try {
            double progressPercentage = videoProgressService.calculateProgressPercentage(userId, courseId);
            return ResponseEntity.ok(progressPercentage);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progress/user/{userId}/lesson/{lessonId}")
    public ResponseEntity<List<VideoProgress>> getUserLessonProgress(@PathVariable Long userId, @PathVariable Long lessonId) {
        try {
            List<VideoProgress> progress = videoProgressService.getUserLessonProgress(userId, lessonId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progress/percentage/user/{userId}/lesson/{lessonId}")
    public ResponseEntity<Double> getUserLessonProgressPercentage(@PathVariable Long userId, @PathVariable Long lessonId) {
        try {
            double progressPercentage = videoProgressService.calculateProgressPercentageByLesson(userId, lessonId);
            return ResponseEntity.ok(progressPercentage);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}/level/{level}")
    public ResponseEntity<List<Video>> getVideosByUserIdAndLevel(@PathVariable Long userId, @PathVariable Course.Level level) {
        List<Video> videos = courseVideoService.getVideosByUserIdAndLevel(userId, level);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/videos/lesson/{lessonId}")
    public ResponseEntity<List<Video>> getVideosByLessonId(@PathVariable Long lessonId) {
        try {
            List<Video> videos = courseVideoService.getVideosByLessonId(lessonId);
            return ResponseEntity.ok(videos);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findVideoByCourse/{courseId}")
    public ResponseEntity<List<Video>> getVideosByCourseIdAndTitleAndLevel(
            @PathVariable Long courseId,
            @RequestParam String courseTitle,
            @RequestParam Course.Level level) {

        List<Video> videos = courseVideoService.getVideosByCourseIdAndTitleAndLevel(courseId, courseTitle, level);

        if (!videos.isEmpty()) {
            return ResponseEntity.ok(videos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/allCourseProgress/percentage/user/{userId}")
    public ResponseEntity<Map<Long, Double>> getUserAllCourseProgress(@PathVariable Long userId) {
        try {
            List<Enrollment> enrollments = enrollmentService.findByUserId(userId);
            Map<Long, Double> progressMap = new HashMap<>();

            if (enrollments == null || enrollments.isEmpty()) {
                System.out.println("No enrollments found for userId: " + userId);
                return ResponseEntity.ok(progressMap);
            }

            for (Enrollment enrollment : enrollments) {
                Long courseId = enrollment.getCourse().getId();
                if (courseId == null) {
                    System.err.println("Course ID is null for enrollment: " + enrollment);
                    continue;
                }

                double progressPercentage = videoProgressService.calculateAllCourseProgressPercentage(userId, courseId);
                progressMap.put(courseId, progressPercentage);
            }

            return ResponseEntity.ok(progressMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/progress/percentage/course/{courseId}")
    public ResponseEntity<Map<Long, Map<String, Object>>> getAllUsersCourseProgressPercentage(@PathVariable Long courseId) {
        try {
            Map<Long, Map<String, Object>> userProgressMap = videoProgressService.calculateProgressPercentageForAllUsersByCourse(courseId);
            return ResponseEntity.ok(userProgressMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @GetMapping("/progress/percentage/user/{userId}")
    public ResponseEntity<Map<String, Object>> getAllCoursesUserProgressPercentage(@PathVariable Long userId) {
        try {
            Map<String, Object> userProgressMap = videoProgressService.calculateProgressPercentageForAllCoursesByUser(userId);
            return ResponseEntity.ok(userProgressMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/progress/percentage/user/{userId}/lessonModule/{lessonModuleId}")
    public ResponseEntity<Double> getUserLessonModuleProgressPercentage(
            @PathVariable Long userId, @PathVariable Long lessonModuleId) {
        try {
            double progressPercentage = videoProgressService.calculateProgressPercentageByLessonModule(userId, lessonModuleId);
            return ResponseEntity.ok(progressPercentage);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/saveProgress")
    public ResponseEntity<String> saveProgress(
            @RequestParam Long userId,
            @RequestParam Long videoId,
            @RequestParam double progress) {
        try {
            courseVideoService.saveProgress(userId, videoId, progress);
            return new ResponseEntity<>("Progress saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving progress: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint to get video progress
    @GetMapping("/getProgress")
    public ResponseEntity<VideoProgress> getProgress(
            @RequestParam Long userId,
            @RequestParam Long videoId) {
        try {
            VideoProgress progress = courseVideoService.getProgress(userId, videoId);
            return new ResponseEntity<>(progress, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/updateProgress")
    public ResponseEntity<String> updateProgress(
            @RequestParam Long userId,
            @RequestParam Long videoId,
            @RequestParam double progress) {
        try {
            courseVideoService.updateProgress(userId, videoId, progress);
            return new ResponseEntity<>("Progress updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating progress: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/averageProgress/user/{userId}/course/{courseId}")
    public ResponseEntity<Double> getAverageProgress(
            @PathVariable Long userId,
            @PathVariable Long courseId) {
        try {
            double averageProgress = courseVideoService.calculateAverageProgress(userId, courseId);
            return ResponseEntity.ok(averageProgress);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/progressOfLesson/user/{userId}/lesson/{lessonId}")
    public ResponseEntity<Double> getUsersLessonProgress(@PathVariable Long userId, @PathVariable Long lessonId) {
        try {
            double progressPercentage = courseVideoService.calculateLessonProgressByUser(userId, lessonId);
            return ResponseEntity.ok(progressPercentage);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/progressOfModule/user/{userId}/lessonModule/{lessonModuleId}")
    public ResponseEntity<Double> getUsersLessonModuleProgress(@PathVariable Long userId, @PathVariable Long lessonModuleId) {
        try {
            double progressPercentage = courseVideoService.calculateLessonModuleProgress(userId, lessonModuleId);
            return ResponseEntity.ok(progressPercentage);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progressOfCourse/user/{userId}/course/{courseId}")
    public ResponseEntity<Double> getUsersCourseProgress(@PathVariable Long userId, @PathVariable Long courseId) {
        try {
            double progressPercentage = courseVideoService.calculateCourseProgress(userId, courseId);
            return ResponseEntity.ok(progressPercentage);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progressByCourseByLesson/{courseId}")
    public ResponseEntity<Map<String, Double>> getEnrolledUsersProgress(@PathVariable Long courseId) {
        try {
            Map<String, Double> userProgressMap = courseVideoService.getEnrolledUserProgress(courseId);
            return ResponseEntity.ok(userProgressMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/progressByCourseLessonModule/{courseId}")
    public ResponseEntity<Map<String, Double>> getEnrolledUserProgress(@PathVariable Long courseId) {
        try {
            Map<String, Double> userProgressMap = courseVideoService.getEnrolledUsersProgress(courseId);
            return ResponseEntity.ok(userProgressMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progressOfCourseOfUser/user/{userId}/course/{courseId}")
    public ResponseEntity<Double> getCandidateCourseProgress(@PathVariable Long userId, @PathVariable Long courseId) {
        try {
            double courseProgress = courseVideoService.calculateCourseProgressByUser(userId, courseId);
            return ResponseEntity.ok(courseProgress);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveOrUpdateProgress")
    public ResponseEntity<String> saveOrUpdateProgress(
            @RequestParam Long userId,
            @RequestParam Long videoId,
            @RequestParam double progress) {
        try {
            courseVideoService.saveOrUpdateProgress(userId, videoId, progress);
            return new ResponseEntity<>("Progress saved or updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving or updating progress: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{courseId}/totalVideoDuration")
    public ResponseEntity<String> getTotalCourseDuration(@PathVariable Long courseId) {
        try {
            String totalDuration = courseVideoService.getTotalCourseDuration(courseId);
            return new ResponseEntity<>(totalDuration, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/details")
    public ResponseEntity<List<Map<String, String>>> getVideoDetails(
            @RequestParam("courseId") Long courseId,
            @RequestParam("lessonId") Long lessonId) {
        List<Map<String, String>> videoDetails = courseVideoService.getVideoDetails(courseId, lessonId);

        if (videoDetails.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videoDetails);
    }

    @PostMapping("/{id}/transcript")
    public ResponseEntity<Video> updateTranscript(@PathVariable Long id, @RequestBody String transcript) {
        Video updatedVideo = courseVideoService.updateTranscriptById(id, transcript);
        return ResponseEntity.ok(updatedVideo);
    }
}
