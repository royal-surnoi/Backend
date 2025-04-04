package fusionIQ.AI.V2.fusionIq.testservice;
import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.ArticlePostLike;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.ArticlePostService;
import fusionIQ.AI.V2.fusionIq.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ArticlePostServiceTest {

    @Mock
    private ArticlePostRepo articlePostRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ArticlePostLikeRepo articlePostLikeRepo;

    @Mock
    private CommentRepo commentRepo;

    @Mock
    private UserArticleInteractionRepo userArticleInteractionRepo;

    @Mock
    private SavedItemsRepo savedItemsRepo;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AIFeedRepo aiFeedRepo;

    private ArticlePostService articlePostService;

    private User testUser;
    private ArticlePost testArticlePost;
    private ArticlePostLike testArticlePostLike;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Initialize service with mocked dependencies
        articlePostService = new ArticlePostService(articlePostRepo);
        // Manually inject mocked dependencies
        TestUtils.setField(articlePostService, "userRepo", userRepo);
        TestUtils.setField(articlePostService, "articlePostLikeRepo", articlePostLikeRepo);
        TestUtils.setField(articlePostService, "commentRepo", commentRepo);
        TestUtils.setField(articlePostService, "userArticleInteractionRepo", userArticleInteractionRepo);
        TestUtils.setField(articlePostService, "savedItemsRepo", savedItemsRepo);
        TestUtils.setField(articlePostService, "notificationService", notificationService);
        TestUtils.setField(articlePostService, "aiFeedRepo", aiFeedRepo);

        // Set up test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");

        testArticlePost = new ArticlePost();
        testArticlePost.setId(1L);
        testArticlePost.setArticle("Test Article");
        testArticlePost.setUser(testUser);
        testArticlePost.setPostDate(LocalDateTime.now());
        testArticlePost.setCategory("Technology");

        testArticlePostLike = new ArticlePostLike(testUser, testArticlePost);
    }

    // Utility class for setting private fields
    private static class TestUtils {
        public static void setField(Object target, String fieldName, Object value) {
            try {
                java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    void createArticlePost_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(articlePostRepo.save(any(ArticlePost.class))).thenReturn(testArticlePost);

        ArticlePost result = articlePostService.createArticlePost(1L, "Test Article", "Tech", "Technology");

        assertNotNull(result);
        assertEquals("Test Article", result.getArticle());
        assertEquals("Technology", result.getCategory());
        verify(articlePostRepo).save(any(ArticlePost.class));
    }

    @Test
    void getArticlePostById_Success() {
        when(articlePostRepo.findById(1L)).thenReturn(Optional.of(testArticlePost));

        ArticlePost result = articlePostService.getArticlePostById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Article", result.getArticle());
    }

    @Test
    void getArticlePostById_NotFound() {
        when(articlePostRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> articlePostService.getArticlePostById(999L));
    }

    @Test
    void likeArticlePost_NewLike() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(articlePostRepo.findById(1L)).thenReturn(Optional.of(testArticlePost));
        when(articlePostLikeRepo.findByArticlePostAndUser(any(), any())).thenReturn(List.of());
        when(articlePostRepo.save(any(ArticlePost.class))).thenReturn(testArticlePost);

        ArticlePost result = articlePostService.likeArticlePost(1L, 1L);

        assertNotNull(result);
        verify(articlePostLikeRepo).save(any(ArticlePostLike.class));
        verify(notificationService).createLikePostNotification(1L, 1L, "article");
    }

    @Test
    void likeArticlePost_UnlikeExisting() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(articlePostRepo.findById(1L)).thenReturn(Optional.of(testArticlePost));
        when(articlePostLikeRepo.findByArticlePostAndUser(any(), any())).thenReturn(List.of(testArticlePostLike));
        when(articlePostRepo.save(any(ArticlePost.class))).thenReturn(testArticlePost);

        ArticlePost result = articlePostService.likeArticlePost(1L, 1L);

        assertNotNull(result);
        verify(articlePostLikeRepo).delete(any(ArticlePostLike.class));
    }

    @Test
    void deleteArticlePost_Success() {
        when(articlePostRepo.findById(1L)).thenReturn(Optional.of(testArticlePost));

        articlePostService.deleteArticlePost(1L);

        verify(articlePostLikeRepo).deleteByArticlePost(testArticlePost);
        verify(commentRepo).deleteByArticlePost(testArticlePost);
        verify(userArticleInteractionRepo).deleteByArticlePost(testArticlePost);
        verify(savedItemsRepo).deleteByArticlePost(testArticlePost);
        verify(aiFeedRepo).deleteByArticlePost(testArticlePost);
        verify(articlePostRepo).delete(testArticlePost);
    }

    @Test
    void getTrendingArticles_Success() {
        LocalDateTime fromDate = LocalDateTime.now().minusHours(48);
        List<ArticlePost> trendingArticles = Collections.singletonList(testArticlePost);
        when(articlePostRepo.findTrendingArticlesInLast48Hours(any(LocalDateTime.class))).thenReturn(trendingArticles);

        List<Map<String, Object>> result = articlePostService.getTrendingArticles();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        Map<String, Object> firstArticle = result.getFirst();
        assertEquals(1L, firstArticle.get("id"));
        assertEquals("Test Article", firstArticle.get("article"));
    }

    @Test
    void getFilteredArticlePosts_Success() {
        LocalDateTime.now().minusDays(7);
        List<ArticlePost> articles = Collections.singletonList(testArticlePost);
        when(articlePostRepo.findByCategoryAndPostDateBefore(eq("Technology"), any(LocalDateTime.class)))
                .thenReturn(articles);
        when(userArticleInteractionRepo.findByUserIdAndArticleInteraction(1L, 1))
                .thenReturn(List.of());

        List<ArticlePost> result = articlePostService.getFilteredArticlePosts("Technology", 1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Technology", result.getFirst().getCategory());
    }

    @Test
    void getFilteredArticlePosts_InvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> articlePostService.getFilteredArticlePosts("", 1L));
        assertThrows(IllegalArgumentException.class,
                () -> articlePostService.getFilteredArticlePosts("Technology", null));
    }

    @Test
    void updateCategory_Success() {
        when(articlePostRepo.findById(1L)).thenReturn(Optional.of(testArticlePost));
        when(articlePostRepo.save(any(ArticlePost.class))).thenReturn(testArticlePost);

        ArticlePost result = articlePostService.updateCategory(1L, "NewCategory");

        assertNotNull(result);
        assertEquals("NewCategory", result.getCategory());
        verify(articlePostRepo).save(testArticlePost);
    }

    @Test
    void updateCategory_NotFound() {
        when(articlePostRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> articlePostService.updateCategory(999L, "NewCategory"));
    }
}