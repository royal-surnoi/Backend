
        package fusionIQ.AI.V2.fusionIq.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.UnauthorizedException;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;


import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LongVideoService {

    @Autowired
    private LongVideoRepo longVideoRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private VideoCommentRepo videoCommentRepo;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SavedItemsRepo savedItemsRepo;
    @Autowired
    private NestedCommentRepo nestedCommentRepo;
    @Autowired
    private VideoLikeRepo videoLikeRepo;

    @Autowired
    private UserLongVideoInteractionRepo userLongVideoInteractionRepo;


    private final String bucketName = "fusion-chat-bk1";
    private final String folderName = "LongVideos/";

    public LongVideo uploadLongVideo(MultipartFile file, Long userId, String longVideoDescription, String tag,String category) throws IOException {
        String key = folderName + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        File convertedFile = convertMultiPartToFile(file);

        amazonS3.putObject(new PutObjectRequest(bucketName, key, convertedFile));
        String s3Url = amazonS3.getUrl(bucketName, key).toString();
        String duration = getVideoDuration(convertedFile);
        if (!convertedFile.delete()) {
            System.err.println("Failed to delete file: " + convertedFile.getAbsolutePath());
        }


        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        LongVideo longVideo = new LongVideo();
        longVideo.setLongVideoTitle(file.getOriginalFilename());
        longVideo.setS3Key(key);
        longVideo.setS3Url(s3Url);
        longVideo.setCategory(category);
        longVideo.setUser(user);
        longVideo.setLongVideoDuration(duration);
        if (longVideoDescription != null) longVideo.setLongVideoDescription(longVideoDescription);
        if (tag != null) longVideo.setTag(tag);

        return longVideoRepo.save(longVideo);
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

    public List<LongVideo> getAllLongVideos() {
        return longVideoRepo.findAll();
    }

    public Optional<LongVideo> getLongVideo(Long id) {
        return longVideoRepo.findById(id);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }


    @Transactional
    public void likeVideo(Long videoId, Long userId) {
        LongVideo longVideo = longVideoRepo.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found with id " + videoId));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        if (longVideo.getLikedByUsers().contains(user)) {
            longVideo.getLikedByUsers().remove(user);
            longVideo.getLikeTimestamps().remove(userId);
            longVideo.setLongVideoLikes(longVideo.getLongVideoLikes() - 1);
        } else {
            longVideo.getLikedByUsers().add(user);
            longVideo.getLikeTimestamps().put(userId, LocalDateTime.now());
            longVideo.setLongVideoLikes(longVideo.getLongVideoLikes() + 1);

            notificationService.createLikePostNotification(userId, videoId, "long_video"); // Trigger like notification
        }

        longVideoRepo.save(longVideo);
    }


    public void shareVideo(Long videoId) {
        LongVideo longVideo = longVideoRepo.findById(videoId)
                .orElseThrow(() -> new UserNotFoundException("Video not found with id " + videoId));
        longVideo.setLongVideoShares(longVideo.getLongVideoShares() + 1);
        longVideoRepo.save(longVideo);
    }

    public void incrementViewCount(Long videoId) {
        LongVideo longVideo = longVideoRepo.findById(videoId)
                .orElseThrow(() -> new UserNotFoundException("Video not found with id " + videoId));
        longVideo.setLongVideoViews(longVideo.getLongVideoViews() + 1);
        longVideoRepo.save(longVideo);
    }

    public VideoComment addComment(Long videoId, Long userId, String content) {
        LongVideo longVideo = longVideoRepo.findById(videoId)
                .orElseThrow(() -> new UserNotFoundException("Video not found with id " + videoId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        VideoComment videoComment = new VideoComment();
        videoComment.setVideoCommentContent(content);
        videoComment.setLongVideo(longVideo);
        videoComment.setUser(user);
        videoComment.setCreatedAt(LocalDateTime.now());

        VideoComment savedComment = videoCommentRepo.save(videoComment);
        notificationService.createCommentNotification(userId, videoId, content,"long_video");
        return savedComment;
    }

    public List<VideoComment> getComments(Long videoId) {
        LongVideo longVideo = longVideoRepo.findById(videoId)
                .orElseThrow(() -> new UserNotFoundException("Video not found with id " + videoId));
        return longVideo.getVideoComments();
    }


    public int getLikeCount(Long videoId) {
        LongVideo longVideo = longVideoRepo.findById(videoId).orElseThrow(() -> new UserNotFoundException("Video not found"));
        return longVideo.getLongVideoLikes();
    }

    public int getShareCount(Long videoId) {
        LongVideo longVideo = longVideoRepo.findById(videoId).orElseThrow(() -> new UserNotFoundException("Video not found"));
        return longVideo.getLongVideoShares();
    }


    public int getCommentCount(Long videoId) {
        return videoCommentRepo.countByShortVideoId(videoId);
    }

    public int getViewCount(Long videoId) {
        LongVideo longVideo = longVideoRepo.findById(videoId).orElseThrow(() -> new UserNotFoundException("Video not found"));
        return longVideo.getLongVideoViews();
    }


    public NestedComment addNestedComment(Long videoId, Long userId, String content, Long parentCommentId) {
        LongVideo longVideo = longVideoRepo.findById(videoId)
                .orElseThrow(() -> new UserNotFoundException("Video not found with id " + videoId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        VideoComment parentComment = videoCommentRepo.findById(parentCommentId)
                .orElseThrow(() -> new UserNotFoundException("Parent comment not found with id " + parentCommentId));

        NestedComment nestedComment = new NestedComment();
        nestedComment.setContent(content);
        nestedComment.setUser(user);
        nestedComment.setParentComment(parentComment);
        nestedComment.setCreatedAt(LocalDateTime.now());

        return nestedCommentRepo.save(nestedComment);
    }

    public List<NestedComment> getNestedComments(Long parentCommentId) {
        VideoComment parentComment = videoCommentRepo.findById(parentCommentId)
                .orElseThrow(() -> new UserNotFoundException("Parent comment not found with id " + parentCommentId));
        return nestedCommentRepo.findByParentComment(parentComment);
    }

    public List<LongVideo> getLongVideosByUserId(long userId) throws UserNotFoundException {
        List<LongVideo> longVideos = longVideoRepo.findByUserId(userId);
        if (longVideos == null) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return longVideos;
    }

    public LongVideo updateLongVideoDescription(Long id, String longVideoDescription, String tag) throws UserNotFoundException {
        Optional<LongVideo> optionalLongVideo = longVideoRepo.findById(id);
        if (!optionalLongVideo.isPresent()) {
            throw new UserNotFoundException("Long video not found");
        }
        LongVideo longVideo = optionalLongVideo.get();
        if (longVideoDescription != null) longVideo.setLongVideoDescription(longVideoDescription);
        if (tag != null) longVideo.setTag(tag);
        longVideo.setUpdatedDate(LocalDateTime.now());
        return longVideoRepo.save(longVideo);
    }

    @Transactional
    public void deleteLongVideo(long id) {
        LongVideo longVideo = getLongVideoById(id);
        if (longVideo != null) {
            videoCommentRepo.deleteByLongVideo(longVideo);
            savedItemsRepo.deleteByLongVideo(longVideo);
            longVideoRepo.delete(longVideo);
        } else {
            throw new RuntimeException("LongVideo not found");
        }
    }

    private LongVideo getLongVideoById(long id) {
        return longVideoRepo.findById(id).orElseThrow(() -> new RuntimeException("Short Video not found"));

    }

    public List<LongVideo> getLongVideosWithNullCategory() {
        return longVideoRepo.findByCategoryIsNull();
    }
    public VideoComment likeComment(Long videoId, Long commentId, Long userId) throws UserNotFoundException {
        LongVideo video = getLongVideo(videoId).orElseThrow(() -> new UserNotFoundException("Video not found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        VideoComment comment = videoCommentRepo.findById(commentId)
                .orElseThrow(() -> new UserNotFoundException("Comment not found"));

        if (video.getVideoComments().contains(comment)) {
            comment.incrementLikes();
            return videoCommentRepo.save(comment);
        } else {
            throw new UserNotFoundException("Comment does not belong to this video");
        }
    }


    public int getCommentLikes(Long videoId, Long commentId) throws UserNotFoundException {
        LongVideo video = getLongVideo(videoId).orElseThrow(() -> new UserNotFoundException("Video not found"));
        VideoComment comment = videoCommentRepo.findById(commentId)
                .orElseThrow(() -> new UserNotFoundException("Comment not found"));

        if (video.getVideoComments().contains(comment)) {
            return comment.getLikes();
        } else {
            throw new UserNotFoundException("Comment does not belong to this video");
        }
    }
    public boolean isVideoLikedByUser(Long videoId, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        Optional<LongVideo> video = longVideoRepo.findByIdAndLikedByUsersContaining(videoId, user);
        return video.isPresent();
    }


    public List<LongVideo> getLikedVideosByUser(Long userId) {
        return longVideoRepo.findByLikedByUsersContaining(userId);
    }
    @Transactional
    public NestedComment likeNestedComment(Long nestedCommentId, Long userId) {
        NestedComment nestedComment = nestedCommentRepo.findById(nestedCommentId)
                .orElseThrow(() -> new RuntimeException("Nested comment not found with id " + nestedCommentId));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        if (nestedComment.getLikedUserIds().contains(user.getId())) {
            nestedComment.getLikedUserIds().remove(user.getId());
            nestedComment.setLikes(nestedComment.getLikes() - 1);
        }
        else {
            nestedComment.getLikedUserIds().add(userId);
            nestedComment.setLikes(nestedComment.getLikes() + 1);

            // Trigger a notification or any other logic here if needed
        }

        return nestedCommentRepo.save(nestedComment);
    }

    public int getNestedCommentLikes(Long nestedCommentId) {
        NestedComment nestedComment = nestedCommentRepo.findById(nestedCommentId)
                .orElseThrow(() -> new RuntimeException("Nested comment not found with id " + nestedCommentId));
        return nestedComment.getLikes();
    }

    @Transactional
    public void deleteNestedComment(Long nestedCommentId) {
        NestedComment nestedComment = nestedCommentRepo.findById(nestedCommentId)
                .orElseThrow(() -> new RuntimeException("Nested comment not found with id " + nestedCommentId));
        nestedCommentRepo.delete(nestedComment);
    }
    @Transactional
    public void deleteComment(Long videoId, Long commentId, Long userId) throws UnauthorizedException {
        LongVideo longVideo = longVideoRepo.findById(videoId)
                .orElseThrow(() -> new UserNotFoundException("Video not found with id " + videoId));

        VideoComment comment = videoCommentRepo.findById(commentId)
                .orElseThrow(() -> new UserNotFoundException("Comment not found with id " + commentId));

        // Check if the user is either the video owner or the comment owner
        if (!(longVideo.getUser().getId() == (userId)) && !(comment.getUser().getId() == (userId))) {
            throw new UnauthorizedException("User is not authorized to delete this comment");
        }

        // Delete the comment and its nested comments
        deleteNestedComments(commentId);
        videoCommentRepo.delete(comment);
    }

    private void deleteNestedComments(Long parentCommentId) {
        List<VideoComment> nestedComments = videoCommentRepo.findByParentCommentId(parentCommentId);
        for (VideoComment nestedComment : nestedComments) {
            deleteNestedComments(nestedComment.getId());
            videoCommentRepo.delete(nestedComment);
        }
    }

    @Transactional
    public VideoComment editComment(Long videoId, Long commentId, Long userId, String newContent) throws UnauthorizedException {
        LongVideo longVideo = longVideoRepo.findById(videoId)
                .orElseThrow(() -> new UserNotFoundException("Video not found with id " + videoId));

        VideoComment comment = videoCommentRepo.findById(commentId)
                .orElseThrow(() -> new UserNotFoundException("Comment not found with id " + commentId));

        if (!(comment.getUser().getId() == (userId))) {
            throw new UnauthorizedException("You are not authorized to edit this comment.");
        }

        comment.setVideoCommentContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now()); // Update the timestamp if necessary

        return videoCommentRepo.save(comment);
    }

    @Transactional
    public NestedComment editNestedComment(Long nestedCommentId, Long userId, String newContent) throws UnauthorizedException {
        NestedComment nestedComment = nestedCommentRepo.findById(nestedCommentId)
                .orElseThrow(() -> new RuntimeException("Nested comment not found with id " + nestedCommentId));

        if (!(nestedComment.getUser().getId() == (userId))) {
            throw new UnauthorizedException("You are not authorized to edit this nested comment.");
        }

        nestedComment.setContent(newContent);
        nestedComment.setUpdatedAt(LocalDateTime.now()); // Update the timestamp if necessary

        return nestedCommentRepo.save(nestedComment);
    }

    @Cacheable(value = "videoDetailsCache", key = "#userId + '-' + #longVideoId")
    public LongVideo getFullVideoDetails(Long userId, Long videoId) {
        return longVideoRepo.findByUserIdAndId(userId, videoId);
    }


    public List<Map<String, Object>> getAllLongVideosWithDetails() {
        List<Object[]> results = longVideoRepo.findAllLongVideosWithPersonalDetails();
        List<Map<String, Object>> longVideosWithDetails = new ArrayList<>();

        for (Object[] result : results) {
            LongVideo longVideo = (LongVideo) result[0];
            PersonalDetails personalDetails = (PersonalDetails) result[1];
            User user = (User) result[2];

            Map<String, Object> longVideoMap = new HashMap<>();

            // Safely add LongVideo data
            Map<String, Object> longVideoData = new HashMap<>();
            if (longVideo != null) {
                longVideoData.put("id", longVideo.getId());
                longVideoData.put("title", longVideo.getLongVideoTitle());
                longVideoData.put("s3Url", longVideo.getS3Url());
                longVideoData.put("description", longVideo.getLongVideoDescription());
                longVideoData.put("likes", longVideo.getLongVideoLikes());
                longVideoData.put("shares", longVideo.getLongVideoShares());
                longVideoData.put("views", longVideo.getLongVideoViews());
                longVideoData.put("duration", longVideo.getLongVideoDuration());
                longVideoData.put("createdAt", longVideo.getCreatedAt());
                longVideoData.put("updatedDate", longVideo.getUpdatedDate());
                longVideoData.put("tag", longVideo.getTag());

                // Add associated comments (safely check if comments list is not null)
                List<Map<String, Object>> commentsList = new ArrayList<>();
                if (longVideo.getVideoComments() != null) {
                    for (VideoComment videoComment : longVideo.getVideoComments()) {
                        Map<String, Object> videoCommentData = new HashMap<>();
                        videoCommentData.put("commentId", videoComment.getId());
                        videoCommentData.put("commentText", videoComment.getVideoCommentContent());
                        commentsList.add(videoCommentData);
                    }
                }
                longVideoData.put("comments", commentsList);
            }
            longVideoMap.put("longVideo", longVideoData);

            // Safely add PersonalDetails data
            Map<String, Object> personalDetailsData = new HashMap<>();
            if (personalDetails != null) {
                personalDetailsData.put("personalDetailsId", personalDetails.getId());
                personalDetailsData.put("profession", personalDetails.getProfession());
                personalDetailsData.put("userLanguage", personalDetails.getUserLanguage());
                personalDetailsData.put("userDescription", personalDetails.getUserDescription());
                personalDetailsData.put("age", personalDetails.getAge());
                personalDetailsData.put("latitude", personalDetails.getLatitude());
                personalDetailsData.put("longitude", personalDetails.getLongitude());
                personalDetailsData.put("interests", personalDetails.getInterests());
            }
            longVideoMap.put("personalDetails", personalDetailsData);

            // Safely add User data
            Map<String, Object> userData = new HashMap<>();
            if (user != null) {
                userData.put("name", user.getName());
                userData.put("email", user.getEmail());
                userData.put("userImage", user.getUserImage());
                userData.put("userId", user.getId());
            }
            longVideoMap.put("user", userData);

            longVideosWithDetails.add(longVideoMap);
        }

        return longVideosWithDetails;
    }


    public List<Map<String, Object>> getTrendingLongVideos() {
        LocalDateTime fromDate = LocalDateTime.now().minusHours(48);
        List<LongVideo> longVideos = longVideoRepo.findTrendingLongVideosInLast48Hours(fromDate);

        // Limit results to top 10
        return longVideos.stream()
                .limit(10)
                .map(video -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", video.getId());
                    map.put("longVideoDescription", video.getLongVideoDescription());
                    map.put("score", video.getLongVideoLikes() + video.getLongVideoShares() + video.getLongVideoViews());
                    return map;
                })
                .collect(Collectors.toList());
    }
    public LongVideo updateCategory(Long id, String category) {
        LongVideo video = longVideoRepo.findById(id).orElse(null);
        if (video != null) {
            video.setCategory(category);
            return longVideoRepo.save(video);
        }
        return null;
    }
    public List<LongVideo> getVideosByCategory(String category) {
        return longVideoRepo.findByCategory(category);
    }

    public List<LongVideo> getTop10Posts(String category) {
        LocalDateTime last7Days = LocalDateTime.now().minusDays(7);
        return longVideoRepo.findTop10ByCategoryAndCreatedAtAfterOrderByEngagementDesc(category, last7Days);
    }

    public List<LongVideo> getFilteredLongVideos(String category, Long userId) {
        // Step 1: Validate input
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Step 2: Fetch all videos by category, excluding those created in the last 7 days
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        List<LongVideo> videos = longVideoRepo.findByCategoryAndCreatedAtBefore(category, oneWeekAgo);

        // Step 3: Fetch user interactions where interaction == 1
        List<Long> interactedVideoIds = userLongVideoInteractionRepo
                .findByUserIdAndLongVideoInteraction(userId, 1)
                .stream()
                .map(interaction -> interaction.getLongVideo().getId())
                .collect(Collectors.toList());

        // Step 4: Filter videos to exclude those interacted with by the user
        return videos.stream()
                .filter(video -> !interactedVideoIds.contains(video.getId()))
                .collect(Collectors.toList());
    }
}
