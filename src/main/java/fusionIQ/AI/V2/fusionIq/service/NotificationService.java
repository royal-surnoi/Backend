package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ShortVideoRepo shortVideoRepo;
    @Autowired
    private LongVideoRepo longVideoRepo;
    @Autowired
    private AssignmentRepo assignmentRepo;
    @Autowired
    private ImagePostRepo imagePostRepo;
    @Autowired
    private ArticlePostRepo articlePostRepo;

    public List<Notification> getNotificationsByUser(User user) {
        return notificationRepo.findByUser(user);
    }


public Notification createNotification(Long userId, String content) {
    User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    Notification notification = new Notification();
    notification.setUser(user);
    notification.setContent(content);
    Notification savedNotification = notificationRepo.save(notification);

    try {
        notificationWebSocketHandler.sendNotification(content);
    } catch (Exception e) {
        e.printStackTrace();
    }
    sendNotificationAsync(String.valueOf(user.getId()), content);

    return savedNotification;
}


    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepo.findByUserId(userId);
    }

     @Transactional
    public void createFollowRequestNotification(Long followerId, Long followeeId) {
        User follower = userRepo.findById(followerId).orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
        User followee = userRepo.findById(followeeId).orElseThrow(() -> new ResourceNotFoundException("Followee not found"));
        String message = follower.getName() + " requested to follow you.";
        String url = "/profile/" + followerId;
         createNotificationWithActionUser(followee, follower, message, url, "followRequest", null, null);

         sendNotificationAsync(String.valueOf(followee.getId()), message);
   }

    @Transactional
    public void createFollowRequestAcceptedNotification(Long followerId, Long followeeId) {
        User follower = userRepo.findById(followerId).orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
        User followee = userRepo.findById(followeeId).orElseThrow(() -> new ResourceNotFoundException("Followee not found"));
        String message = followee.getName() + " accepted your follow request.";
        String url = "/profile/" + followeeId;
        createNotificationWithActionUser(follower, followee, message, url, "followRequestAccepted", null, null);

        sendNotificationAsync(String.valueOf(follower.getId()), message);

    }

    private void createNotificationWithActionUser(User user, User actionUser, String content, String url, String contentType, String contentUrl, String contentText) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setActionUser(actionUser);
        notification.setContent(content);
        notification.setUrl(url);
        notification.setContentType(contentType);
        notification.setContentUrl(contentUrl);
        notification.setContentText(contentText);
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);
        notificationRepo.save(notification);

        sendNotificationAsync(String.valueOf(user.getId()), content);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId).orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepo.save(notification);
    }

    private void createNotification(User user, String content, String url, String contentType, String contentUrl, String contentText) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setContent(content);
        notification.setUrl(url);
        notification.setContentType(contentType);
        notification.setContentUrl(contentUrl);
        notification.setContentText(contentText);
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);
        notificationRepo.save(notification);

        sendNotificationAsync(String.valueOf(user.getId()), content);

    }
    public void createLikePostNotification(Long userId, Long postId, String contentType) {
        User likingUser = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        String url = "";
        User postOwner = null;

        switch (contentType) {
            case "short_video":
                ShortVideo shortVideo = shortVideoRepo.findById(postId).orElseThrow(() -> new RuntimeException("Short Video not found"));
                url = "/short-video/short-videos/" + postId;
                postOwner = shortVideo.getUser();
                break;
            case "long_video":
                LongVideo longVideo = longVideoRepo.findById(postId).orElseThrow(() -> new RuntimeException("Long Video not found"));
                url = "/long-video/long-videos/" + postId;
                postOwner = longVideo.getUser();
                break;
            case "image":
                ImagePost imagePost = imagePostRepo.findById(postId).orElseThrow(() -> new RuntimeException("Image Post not found"));
                url = "/api/imagePosts/get/" + postId;
                postOwner = imagePost.getUser();
                break;
            case "article":
                ArticlePost articlePost = articlePostRepo.findById(postId).orElseThrow(() -> new RuntimeException("Article Post not found"));
                url = "/api/articleposts/" + postId;
                postOwner = articlePost.getUser();
                break;
        }

        if (postOwner == null) {
            throw new RuntimeException("Post owner not found");
        }

        createNotificationWithActionUser(postOwner, likingUser, likingUser.getName() + " liked your post", url, contentType, null, null);


    }

    public void createCommentNotification(Long userId, Long postId, String commentContent, String contentType) {
        User commentingUser = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        String url = "";
        User postOwner = null;

        switch (contentType) {
            case "short_video":
                ShortVideo shortVideo = shortVideoRepo.findById(postId).orElse(null);
                if (shortVideo != null) {
                    url = "/short-video/short-videos/" + postId;
                    postOwner = shortVideo.getUser();
                } else {
                    throw new RuntimeException("Short Video not found with id " + postId);
                }
                break;
            case "long_video":
                LongVideo longVideo = longVideoRepo.findById(postId).orElse(null);
                if (longVideo != null) {
                    url = "/long-video/long-videos/" + postId;
                    postOwner = longVideo.getUser();
                } else {
                    throw new RuntimeException("Long Video not found with id " + postId);
                }
                break;
            case "image":
                ImagePost imagePost = imagePostRepo.findById(postId).orElse(null);
                if (imagePost != null) {
                    url = "/api/imagePosts/get/" + postId;
                    postOwner = imagePost.getUser();
                } else {
                    throw new RuntimeException("Image Post not found with id " + postId);
                }
                break;
            case "article":
                ArticlePost articlePost = articlePostRepo.findById(postId).orElse(null);
                if (articlePost != null) {
                    url = "/api/articleposts/" + postId;
                    postOwner = articlePost.getUser();
                } else {
                    throw new RuntimeException("Article Post not found with id " + postId);
                }
                break;
            default:
                throw new RuntimeException("Unknown content type: " + contentType);
        }

        if (postOwner == null) {
            throw new RuntimeException("Post owner not found for post id " + postId + " and content type " + contentType);
        }

        createNotificationWithActionUser(postOwner, commentingUser, commentingUser.getName() + " commented on your post: " + commentContent, url, contentType, null, null);

    }

    @Async
    public void sendNotificationAsync(String userId, String message) {
        try {
            notificationWebSocketHandler.sendNotification(userId, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void notifyReceiver(User receiver, Message message) {
        // For now, just log the notification for demonstration purposes
        System.out.println("New message notification: Receiver ID - " + receiver.getId()
                + ", Message from - " + message.getSender().getId()
                + ", Content - " + message.getMessageContent());

    }

    public long getUnreadMessageCount(Long userId) {

        if (userId == null) {

            throw new IllegalArgumentException("User ID must not be null");

        }

        return notificationRepo.countUnreadMessagesByUserId(userId);

    }

    public List<Notification> processNotificationsWithActionUserImage(List<Notification> notifications) {
        for (Notification notification : notifications) {
            User actionUser = notification.getActionUser();
            if (actionUser != null && actionUser.getUserImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(actionUser.getUserImage());
                notification.setActionUserImageBase64(base64Image);
            }
        }
        return notifications;
    }
}

