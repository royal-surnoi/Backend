
        package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.ImagePost;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.service.ImagePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/imagePosts")
public class ImagePostController {

    @Autowired
    private ImagePostService imagePostService;

    @PostMapping("/create")
    public ImagePost createImagePost(@RequestParam long userId,
                                     @RequestParam MultipartFile photo,
                                     @RequestParam(required = false) String imageDescription,
                                     @RequestParam(required = false) String category,
                                     @RequestParam(required = false) String tag) throws IOException {
        byte[] photoBytes = photo.getBytes();
        return imagePostService.createImagePost(userId, photoBytes, imageDescription, tag,category);
    }
    @GetMapping("/image-posts/category-null")
    public List<ImagePost> getImagePostsWithNullCategory() {
        return imagePostService.getImagePostsWithNullCategory();
    }

    @GetMapping("/get/{id}")
    public ImagePost getImagePostById(@PathVariable long id) {
        return imagePostService.getImagePostById(id);
    }

    @GetMapping("/getAll")
    public List<ImagePost> getAllImagePosts() {
        return imagePostService.getAllImagePosts();
    }

    @GetMapping("/user/{userId}")
    public List<ImagePost> getAllImagePostsByUserId(@PathVariable long userId) {
        return imagePostService.getAllImagePostsByUserId(userId);
    }

    @PutMapping("/update/{id}")
    public ImagePost updateImagePost(@PathVariable long id, @RequestParam(required = false) MultipartFile photo, @RequestParam(required = false) String imageDescription,@RequestParam(required = false) String tag) throws IOException {
        byte[] photoBytes = photo != null ? photo.getBytes() : null;
        return imagePostService.updateImagePost(id, photoBytes,imageDescription,tag);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteImagePost(@PathVariable long id) {
        imagePostService.deleteImagePost(id);
    }

    @PostMapping("/{postId}/like")
    public ImagePost likeImagePost(@PathVariable long postId, @RequestParam long userId) {
        return imagePostService.likeImagePost(postId, userId);
    }

    @PostMapping("/{postId}/dislike")
    public ImagePost dislikeImagePost(@PathVariable long postId) {
        return imagePostService.dislikeImagePost(postId);
    }

    @PostMapping("/{postId}/share")
    public ImagePost shareImagePost(@PathVariable long postId) {
        return imagePostService.shareImagePost(postId);
    }

    @GetMapping("/{postId}/likeCount")
    public int getLikeCountByImagePostId(@PathVariable long postId) {
        return imagePostService.getLikeCountByImagePostId(postId);
    }

    @GetMapping("/{postId}/shareCount")
    public int getShareCountByImagePostId(@PathVariable long postId) {
        return imagePostService.getShareCountByImagePostId(postId);
    }

    @GetMapping("/{postId}/likedUsers")
    public List<User> getUsersWhoLikedPost(@PathVariable long postId) {
        return imagePostService.getUsersWhoLikedImagePost(postId);
    }
    @GetMapping("/{postId}/liked-by/{userId}")
    public ResponseEntity<Boolean> isImagePostLikedByUser(
            @PathVariable long postId, @PathVariable long userId) {
        boolean isLiked = imagePostService.isImagePostLikedByUser(postId, userId);
        return ResponseEntity.ok(isLiked);
    }
    @PutMapping("/{id}/category")
    public ResponseEntity<ImagePost> updateCategory(
            @PathVariable long id,
            @RequestBody Map<String, String> request) {

        String category = request.get("category");

        if (category == null || category.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        ImagePost updatedImagePost = imagePostService.updateCategory(id, category);
        if (updatedImagePost == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedImagePost);
    }
    @GetMapping("/category/{category}")
    public List<ImagePost> getImagePostsByCategory(@PathVariable String category) {

        return imagePostService.getImagePostsByCategory(category);
    }
    //    @GetMapping("/category/recent/{category}")
    //    public List<ImagePost> getImagePostsByCategoryAndRecent(@PathVariable String category) {
    //        return imagePostService.getImagePostsByCategoryAndRecent(category);
    //    }

    @GetMapping("/trending")
    public ResponseEntity<List<ImagePost>> getTrendingPosts(@RequestParam String category) {
        List<ImagePost> trendingPosts = imagePostService.getTrendingPosts(category);
        return ResponseEntity.ok(trendingPosts);
    }
    @GetMapping("category/filtered")
    public List<ImagePost> getFilteredImagePosts(
            @RequestParam String category,
            @RequestParam Long userId) {
        return imagePostService.getFilteredImagePosts(category, userId);
    }
}