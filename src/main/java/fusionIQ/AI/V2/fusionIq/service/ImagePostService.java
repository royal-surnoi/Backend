package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImagePostService {

    @Autowired
    private ImagePostRepo imagePostRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ImagePostLikeRepo imagePostLikeRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private SavedItemsRepo savedItemsRepo;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AIFeedRepo aiFeedRepo;

    @Autowired
    private UserImageInteractionRepo userImageInteractionRepo;

    public ImagePost createImagePost(long userId, byte[] photo, String imageDescription, String tag,String category) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("UserId not found"));
        ImagePost post = new ImagePost();
        post.setPhoto(photo);
        post.setUser(user);
        post.setCategory(category);
        post.setPostDate(LocalDateTime.now());
        if (imageDescription != null) post.setImageDescription(imageDescription);
        if (tag != null) post.setTag(tag);
        return imagePostRepo.save(post);
    }


    public ImagePost getImagePostById(long id) {
        return imagePostRepo.findById(id).orElseThrow(() -> new RuntimeException("ImagePostID not found"));
    }

    public List<ImagePost> getAllImagePosts() {
        return imagePostRepo.findAllOrderByPostDateDesc();
    }

    public List<ImagePost> getAllImagePostsByUserId(long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return imagePostRepo.findByUserOrderByPostDateDesc(user);
    }
    @Transactional
    public void deleteImagePost(long id) {
        ImagePost imagePost = getImagePostById(id);
        if (imagePost != null) {
            imagePostLikeRepo.deleteByImagePost(imagePost);
            commentRepo.deleteByImagePost(imagePost);
            savedItemsRepo.deleteByImagePost(imagePost);
            imagePostRepo.delete(imagePost);
            aiFeedRepo.deleteByImagePost(imagePost);
        } else {
            throw new RuntimeException("ImagePost not found");
        }
    }


    public ImagePost likeImagePost(long postId, long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ImagePost post = getImagePostById(postId);

        // Check if the user has already liked the post
        List<ImagePostLike> existingLikes = imagePostLikeRepo.findByImagePostAndUser(post, user);

        if (!existingLikes.isEmpty()) {
            // If the user has already liked the post, remove the like (unlike)
            imagePostLikeRepo.delete(existingLikes.get(0)); // Remove the first like found
            post.setImageLikeCount(post.getImageLikeCount() - 1);
        } else {
            // If the user hasn't liked the post, add a new like
            ImagePostLike like = new ImagePostLike(user, post);
            imagePostLikeRepo.save(like);
            post.setImageLikeCount(post.getImageLikeCount() + 1);

            // Send notification only on liking
            notificationService.createLikePostNotification(userId, postId, "image");
        }

        // Save the updated post
        return imagePostRepo.save(post);
    }


    public ImagePost dislikeImagePost(long id) {
        ImagePost post = getImagePostById(id);
        post.setImageDislikes(post.getImageDislikes() + 1);
        if (post.getImageLikeCount() > 0) {
            post.setImageLikeCount(post.getImageLikeCount() - 1);
        }
        return imagePostRepo.save(post);
    }

    public ImagePost shareImagePost(long id) {
        ImagePost post = getImagePostById(id);
        post.setImageShareCount(post.getImageShareCount() + 1);
        return imagePostRepo.save(post);
    }

    public int getLikeCountByImagePostId(long id) {
        ImagePost post = getImagePostById(id);
        return post.getImageLikeCount();
    }

    public int getShareCountByImagePostId(long id) {
        ImagePost post = getImagePostById(id);
        return post.getImageShareCount();
    }

    public List<User> getUsersWhoLikedImagePost(long postId) {
        ImagePost post = getImagePostById(postId);
        List<ImagePostLike> likes = imagePostLikeRepo.findByImagePost(post);
        return likes.stream().map(ImagePostLike::getUser).collect(Collectors.toList());
    }

    public ImagePost updateImagePost(long id, byte[] photo, String imageDescription, String tag) {
        ImagePost post = getImagePostById(id);
        if (photo != null && photo.length > 0) {
            post.setPhoto(photo);
        }
        if (imageDescription != null) post.setImageDescription(imageDescription);
        if (tag != null) post.setTag(tag);
        post.setUpdatedDate(LocalDateTime.now());
        return imagePostRepo.save(post);
    }
    public boolean isImagePostLikedByUser(long postId, long userId) {
        return imagePostLikeRepo.findByUserIdAndImagePostId(userId, postId).isPresent();
    }
    @Cacheable(value = "imageDetailsCache", key = "#userId + '-' + #imageId")
    public ImagePost getFullImageDetails(Long userId, Long imageId) {
        return imagePostRepo.findByUserIdAndId(userId, imageId);
    }

    public List<ImagePost> getImagePostsWithNullCategory() {
        return imagePostRepo.findByCategoryIsNull();
    }

    public List<Map<String, Object>> getAllImagePostsWithDetails() {
        List<Object[]> results = imagePostRepo.findAllImagePostsWithPersonalDetails();
        List<Map<String, Object>> imagePostsWithDetails = new ArrayList<>();

        for (Object[] result : results) {
            ImagePost imagePost = (ImagePost) result[0];
            PersonalDetails personalDetails = (PersonalDetails) result[1];
            User user = (User) result[2];

            Map<String, Object> imagePostMap = new HashMap<>();

            // Safely add ImagePost data
            Map<String, Object> imagePostData = new HashMap<>();
            if (imagePost != null) {
                imagePostData.put("id", imagePost.getId());
                imagePostData.put("photo", imagePost.getPhoto());
                imagePostData.put("postDate", imagePost.getPostDate());
                imagePostData.put("updatedDate", imagePost.getUpdatedDate());
                imagePostData.put("imageLikeCount", imagePost.getImageLikeCount());
                imagePostData.put("imageDislikes", imagePost.getImageDislikes());
                imagePostData.put("imageShareCount", imagePost.getImageShareCount());
                imagePostData.put("imageDescription", imagePost.getImageDescription());
                imagePostData.put("tag", imagePost.getTag());
            }


            imagePostMap.put("imagePost", imagePostData);

            // Add associated comments
            List<Map<String, Object>> commentsList = new ArrayList<>();
            if (imagePost != null && imagePost.getComments() != null) {
                for (Comment comment : imagePost.getComments()) {
                    Map<String, Object> commentData = new HashMap<>();
                    commentData.put("commentId", comment.getId());
                    commentData.put("commentText", comment.getText());
                    commentsList.add(commentData);
                }
            }

            imagePostData.put("comments", commentsList);

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
            imagePostMap.put("personalDetails", personalDetailsData);

            // Safely add User data
            Map<String, Object> userData = new HashMap<>();
            if (user != null) {
                userData.put("name", user.getName());
                userData.put("email", user.getEmail());
                userData.put("userImage", user.getUserImage());
                userData.put("userId", user.getId());
            }
            imagePostMap.put("user", userData);

            imagePostsWithDetails.add(imagePostMap);
        }

        return imagePostsWithDetails;
    }

    public List<Map<String, Object>> getTrendingImages() {
        LocalDateTime fromDate = LocalDateTime.now().minusHours(48);
        List<ImagePost> images = imagePostRepo.findTrendingImagesInLast48Hours(fromDate);

        // Limit results to top 10
        return images.stream()
                .limit(10)
                .map(image -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", image.getId());
                    map.put("imageDescription", image.getImageDescription());
                    map.put("score", image.getImageLikeCount() + image.getImageShareCount());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public ImagePost updateCategory(long id, String category) {
        return imagePostRepo.findById(id).map(imagePost -> {
            imagePost.setCategory(category);
            return imagePostRepo.save(imagePost);
        }).orElse(null);
    }

    public List<ImagePost> getImagePostsByCategory(String category) {
        return imagePostRepo.findByCategory(category);
    }

    public List<ImagePost> getTrendingPosts(String category) {
        LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);
        return imagePostRepo.findTrendingPostsByCategory(category, tenDaysAgo);
    }

    public List<ImagePost> getFilteredImagePosts(String category, Long userId) {
        // Validate input
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Step 1: Fetch ImagePosts by category excluding those posted in the last 7 days
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        List<ImagePost> imagePosts = imagePostRepo.findByCategoryAndPostDateBefore(category, oneWeekAgo);

        // Step 2: Fetch UserImageInteraction records for the user where imageInteraction == 1
        List<Long> interactedPostIds = userImageInteractionRepo
                .findByUserIdAndImageInteraction(userId, 1)
                .stream()
                .map(interaction -> interaction.getImagePost().getId())
                .collect(Collectors.toList());

        // Step 3: Exclude interacted posts from the filtered list
        return imagePosts.stream()
                .filter(post -> !interactedPostIds.contains(post.getId()))
                .collect(Collectors.toList());
    }
}
