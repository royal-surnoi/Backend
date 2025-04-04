package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.service.ArticlePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articleposts")
public class ArticlePostController {

    @Autowired
    private ArticlePostService articlePostService;

    @PostMapping("/create")
    public ArticlePost createArticlePost(@RequestParam long userId,
                                         @RequestParam String article,
                                         @RequestParam(required = false) String category,
                                         @RequestParam(required = false) String tag) {
        return articlePostService.createArticlePost(userId, article, tag,category);
    }


    @GetMapping("/{id}")
    public ArticlePost getArticlePostById(@PathVariable long id) {
        return articlePostService.getArticlePostById(id);
    }

    @GetMapping("/getAll")
    public List<ArticlePost> getAllArticlePosts() {
        return articlePostService.getAllArticlePosts();
    }

    @GetMapping("/user/{userId}")
    public List<ArticlePost> getAllArticlePostsByUserId(@PathVariable long userId) {
        return articlePostService.getAllArticlePostsByUserId(userId);
    }

    @PutMapping("/{id}")
    public ArticlePost updateArticlePost(@PathVariable long id,
                                         @RequestParam(required = false) String article,@RequestParam(required = false) String tag) {
        return articlePostService.updateArticlePost(id, article,tag);
    }

    @GetMapping("/articles/category-null")
    public List<ArticlePost> getArticlesWithNullCategory() {
        return articlePostService.getArticlesWithNullCategory();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteArticlePost(@PathVariable long id) {
        try {
            articlePostService.deleteArticlePost(id);
            return ResponseEntity.ok("ArticlePost deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete ArticlePost: " + e.getMessage());
        }
    }


    @PostMapping("/{postId}/like")
    public ArticlePost likeArticlePost(@PathVariable long postId, @RequestParam long userId) {
        return articlePostService.likeArticlePost(postId, userId);
    }

    @PostMapping("/{postId}/dislike")
    public ArticlePost dislikeArticlePost(@PathVariable long postId, @RequestParam long userId) {
        return articlePostService.dislikeArticlePost(postId, userId);
    }

    @PostMapping("/{id}/share")
    public ArticlePost shareArticlePost(@PathVariable long id) {
        return articlePostService.shareArticlePost(id);
    }

    @GetMapping("/{id}/likeCount")
    public int getLikeCountByArticlePostId(@PathVariable long id) {
        return articlePostService.getLikeCountByArticlePostId(id);
    }

    @GetMapping("/{id}/shareCount")
    public int getShareCountByArticlePostId(@PathVariable long id) {
        return articlePostService.getShareCountByArticlePostId(id);
    }

    @GetMapping("/{postId}/likedUsers")
    public List<User> getUsersWhoLikedPost(@PathVariable long postId) {
        return articlePostService.getUsersWhoLikedPost(postId);
    }
    @GetMapping("/{postId}/liked-by/{userId}")
    public ResponseEntity<Boolean> isImagePostLikedByUser(
            @PathVariable long postId, @PathVariable long userId) {
        boolean isLiked = articlePostService.isArticlePostLikedByUser(postId, userId);
        return ResponseEntity.ok(isLiked);
    }
    @PutMapping("/{id}/category")
    public ResponseEntity<ArticlePost> updateCategory(
            @PathVariable long id,
            @RequestBody Map<String, String> request) {

        String category = request.get("category");

        if (category == null || category.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        ArticlePost updatedArticlePost = articlePostService.updateCategory(id, category);
        return ResponseEntity.ok(updatedArticlePost);
    }

    @GetMapping("/category/{category}")
    public List<ArticlePost> getArticlesByCategory(@PathVariable String category) {
        return articlePostService.getArticlesByCategory(category);
    }


    @GetMapping("/trending")
    public List<ArticlePost> getTrendingArticles(@RequestParam String category) {
        return articlePostService.getTrendingArticles(category);
    }

    @GetMapping("category/filtered")
    public List<ArticlePost> getFilteredArticlePosts(
            @RequestParam String category,
            @RequestParam Long userId) {
        return articlePostService.getFilteredArticlePosts(category, userId);
    }

}
