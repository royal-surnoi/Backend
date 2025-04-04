package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticlePostService {

    @Autowired
    private ArticlePostRepo articlePostRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ArticlePostLikeRepo articlePostLikeRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserArticleInteractionRepo userArticleInteractionRepo;

    @Autowired
    private SavedItemsRepo savedItemsRepo;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AIFeedRepo aiFeedRepo;

    public ArticlePost createArticlePost(long userId, String article, String tag,String category) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ArticlePost post = new ArticlePost();
        post.setArticle(article);
        post.setCategory(category);
        if (tag != null) post.setTag(tag);
        post.setUser(user);
        post.setPostDate(LocalDateTime.now());
        return articlePostRepo.save(post);
    }


    public ArticlePost getArticlePostById(long id) {
        return articlePostRepo.findById(id).orElseThrow(() -> new RuntimeException("ArticlePost not found"));
    }

    public List<ArticlePost> getAllArticlePosts() {
        return articlePostRepo.findAllOrderByPostDateDesc();
    }

    public List<ArticlePost> getAllArticlePostsByUserId(long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return articlePostRepo.findByUserOrderByPostDateDesc(user);
    }

    public ArticlePost updateArticlePost(long id, String article, String tag) {
        ArticlePost post = getArticlePostById(id);
        post.setArticle(article);
        if (tag != null) post.setTag(tag);
        post.setUpdatedDate(LocalDateTime.now());
        return articlePostRepo.save(post);
    }

    @Transactional
    public void deleteArticlePost(long id) {
        ArticlePost articlePost = getArticlePostById(id);
        if (articlePost != null) {
            articlePostLikeRepo.deleteByArticlePost(articlePost);
            commentRepo.deleteByArticlePost(articlePost);
            userArticleInteractionRepo.deleteByArticlePost(articlePost);
            savedItemsRepo.deleteByArticlePost(articlePost);
            articlePostRepo.delete(articlePost);
            aiFeedRepo.deleteByArticlePost(articlePost);
        } else {
            throw new RuntimeException("ArticlePost not found");
        }
    }


    public ArticlePost likeArticlePost(long postId, long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ArticlePost post = getArticlePostById(postId);

        // Check if the user has already liked the post
        List<ArticlePostLike> existingLikes = articlePostLikeRepo.findByArticlePostAndUser(post, user);

        if (!existingLikes.isEmpty()) {
            // If the user has already liked the post, remove the like (unlike)
            articlePostLikeRepo.delete(existingLikes.get(0)); // Remove the first like found
            post.setArticleLikeCount(post.getArticleLikeCount() - 1);
        } else {
            // If the user hasn't liked the post, add a new like
            ArticlePostLike like = new ArticlePostLike(user, post);
            articlePostLikeRepo.save(like);
            post.setArticleLikeCount(post.getArticleLikeCount() + 1);

            // Send notification only on liking
            notificationService.createLikePostNotification(userId, postId, "article");
        }

        // Save the updated post
        articlePostRepo.save(post);

        return post;
    }


    public ArticlePost dislikeArticlePost(long postId, long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ArticlePost post = getArticlePostById(postId);
        List<ArticlePostLike> likes = articlePostLikeRepo.findByArticlePostAndUser(post, user);
        if (!likes.isEmpty()) {
            articlePostLikeRepo.deleteAll(likes);
            post.setArticleLikeCount(post.getArticleLikeCount() - 1);
            articlePostRepo.save(post);
        }
        return post;
    }

    public ArticlePost shareArticlePost(long id) {
        ArticlePost post = getArticlePostById(id);
        post.setArticleShareCount(post.getArticleShareCount() + 1);
        return articlePostRepo.save(post);
    }

    public int getLikeCountByArticlePostId(long id) {
        ArticlePost post = getArticlePostById(id);
        return post.getArticleLikeCount();
    }

    public int getShareCountByArticlePostId(long id) {
        ArticlePost post = getArticlePostById(id);
        return post.getArticleShareCount();
    }

    public List<User> getUsersWhoLikedPost(long postId) {
        ArticlePost post = getArticlePostById(postId);
        List<ArticlePostLike> likes = articlePostLikeRepo.findByArticlePost(post);
        return likes.stream().map(ArticlePostLike::getUser).toList();
    }

    public boolean isArticlePostLikedByUser(long postId, long userId) {
        return articlePostLikeRepo.findByUserIdAndArticlePostId(userId, postId).isPresent();
    }

    @Cacheable(value = "articleDetailsCache", key = "#userId + '-' + #articleId")
    public ArticlePost getFullArticleDetails(Long userId, Long articleId) {
        return articlePostRepo.findByUserIdAndId(userId, articleId);
    }

    public List<Map<String, Object>> getAllArticlePostsWithDetails() {
        List<Object[]> results = articlePostRepo.findAllArticlePostsWithPersonalDetails();
        List<Map<String, Object>> articlePostsWithDetails = new ArrayList<>();

        for (Object[] result : results) {
            ArticlePost articlePost = (ArticlePost) result[0];
            PersonalDetails personalDetails = (PersonalDetails) result[1];
            User user = (User) result[2];

            Map<String, Object> articlePostMap = new HashMap<>();

            // Add ArticlePost data
            Map<String, Object> articlePostData = new HashMap<>();
            if (articlePost != null) {
                articlePostData.put("id", articlePost.getId());
                articlePostData.put("article", articlePost.getArticle());
                articlePostData.put("postDate", articlePost.getPostDate());
                articlePostData.put("updatedDate", articlePost.getUpdatedDate());
                articlePostData.put("articleLikeCount", articlePost.getArticleLikeCount());
                articlePostData.put("articleDislikes", articlePost.getArticleDislikes());
                articlePostData.put("articleShareCount", articlePost.getArticleShareCount());
                articlePostData.put("tag", articlePost.getTag());

                // Add associated comments
                List<Map<String, Object>> commentsList = new ArrayList<>();
                for (Comment comment : articlePost.getComments()) {
                    Map<String, Object> commentData = new HashMap<>();
                    commentData.put("commentId", comment.getId());
                    commentData.put("commentText", comment.getText());
                    commentsList.add(commentData);
                }
                articlePostData.put("comments", commentsList);

                // Add UserArticleInteraction details
//                List<Map<String, Object>> interactionList = new ArrayList<>();
//                for (UserArticleInteraction interaction : articlePost.getUserArticleInteraction()) {
//                    Map<String, Object> interactionData = new HashMap<>();
//                    interactionData.put("interactionType", interaction.getType());
//                    interactionList.add(interactionData);
//                }
//                articlePostData.put("userInteractions", interactionList);
//            }
                articlePostMap.put("articlePost", articlePostData);

                // Add PersonalDetails data
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
                articlePostMap.put("personalDetails", personalDetailsData);

                // Add User data
                Map<String, Object> userData = new HashMap<>();
                if (user != null) {
                    userData.put("name", user.getName());
                    userData.put("email", user.getEmail());
                    userData.put("userImage", user.getUserImage());
                    userData.put("userId", user.getId());
                }
                articlePostMap.put("user", userData);

                articlePostsWithDetails.add(articlePostMap);
            }

        }
        return articlePostsWithDetails;
    }

    public List<Map<String, Object>> getTrendingArticles() {
        LocalDateTime fromDate = LocalDateTime.now().minusHours(48);
        List<ArticlePost> articles = articlePostRepo.findTrendingArticlesInLast48Hours(fromDate);

        // Limit results to top 10
        return articles.stream()
                .limit(10)
                .map(article -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", article.getId());
                    map.put("article", article.getArticle());
                    map.put("score", article.getArticleLikeCount() + article.getArticleShareCount());
                    return map;
                })
                .collect(Collectors.toList());
    }
    public List<ArticlePost> getArticlesWithNullCategory() {
        return articlePostRepo.findByCategoryIsNull();
    }
    public ArticlePost updateCategory(long id, String category) {
        ArticlePost articlePost = articlePostRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ArticlePost with ID " + id + " not found"));

        articlePost.setCategory(category);
        return articlePostRepo.save(articlePost);
    }
    public List<ArticlePost> getArticlesByCategory(String category) {
        return articlePostRepo.findByCategory(category);
    }


    public ArticlePostService(ArticlePostRepo articlePostRepo) {
        this.articlePostRepo = articlePostRepo;
    }

    public List<ArticlePost> getTrendingArticles(String category) {
        LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);
        return articlePostRepo.findTrendingArticlesByCategory(category, tenDaysAgo);
    }

    public List<ArticlePost> getFilteredArticlePosts(String category, Long userId) {
        // Validate input
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Step 1: Fetch ArticlePosts by category excluding those posted in the last 7 days
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        List<ArticlePost> articlePosts = articlePostRepo.findByCategoryAndPostDateBefore(category, oneWeekAgo);

        // Step 2: Fetch UserArticleInteraction records for the user where articleInteraction == 1
        List<Long> interactedPostIds = userArticleInteractionRepo
                .findByUserIdAndArticleInteraction(userId, 1)
                .stream()
                .map(interaction -> interaction.getArticlePost().getId())
                .collect(Collectors.toList());

        // Step 3: Exclude interacted articles from the filtered list
        return articlePosts.stream()
                .filter(post -> !interactedPostIds.contains(post.getId()))
                .collect(Collectors.toList());
    }
}
