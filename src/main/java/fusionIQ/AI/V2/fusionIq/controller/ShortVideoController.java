package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.NestedComment;
import fusionIQ.AI.V2.fusionIq.data.VideoComment;
import fusionIQ.AI.V2.fusionIq.data.ShortVideo;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.exception.UnauthorizedException;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.service.ShortVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/short-video")
public class ShortVideoController {

    @Autowired
    private ShortVideoService shortVideoService;

    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadShortVideo(@PathVariable long userId, @RequestParam MultipartFile file, @RequestParam(required = false) String shortVideoDescription, @RequestParam(required = false) String tag,@RequestParam(required = false) String category) {
        try {
            ShortVideo shortVideo = shortVideoService.uploadShortVideo(file, userId, shortVideoDescription,tag,category);
            return new ResponseEntity<>("Short video uploaded successfully, URL: " + shortVideo.getS3Url(), HttpStatus.OK);
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Short video upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/short-videos")
    public ResponseEntity<List<ShortVideo>> getAllShortVideos() {
        List<ShortVideo> shortVideos = shortVideoService.getAllShortVideos();
        return ResponseEntity.ok(shortVideos);
    }
    // Endpoint to fetch ShortVideos with null category
    @GetMapping("/short-videos/category-null")
    public List<ShortVideo> getShortVideosWithNullCategory() {
        return shortVideoService.getShortVideosWithNullCategory();
    }
    @GetMapping("/short-videos/{id}")
    public ResponseEntity<ShortVideo> getShortVideoById(@PathVariable Long id) {
        Optional<ShortVideo> shortVideo = shortVideoService.getShortVideo(id);
        return shortVideo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/short-videos/getAll/{userId}")
    public ResponseEntity<List<ShortVideo>> getShortVideosByUserId(@PathVariable long userId) {
        try {
            List<ShortVideo> shortVideos = shortVideoService.getShortVideosByUserId(userId);
            return ResponseEntity.ok(shortVideos);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteShortVideo(@PathVariable long id) {
        try {
            shortVideoService.deleteShortVideo(id);
            return ResponseEntity.ok("ShortVideo deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete ShortVideo: " + e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateShortVideoDescription(@PathVariable Long id, @RequestParam(required = false) String shortVideoDescription, @RequestParam(required = false) String tag) {
        try {
            ShortVideo updatedShortVideo = shortVideoService.updateShortVideoDescription(id, shortVideoDescription,tag);
            return ResponseEntity.ok("Short video description updated successfully, URL: " + updatedShortVideo.getS3Url());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update short video description: " + e.getMessage());
        }
    }

    @PostMapping("/{videoId}/like")
    public ResponseEntity<Void> likeVideo(@PathVariable Long videoId, @RequestParam Long userId) {
        shortVideoService.likeVideo(videoId, userId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{id}/share")
    public ResponseEntity<Void> shareVideo(@PathVariable Long id) {
        try {
            shortVideoService.shareVideo(id);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long id) {
        try {
            shortVideoService.incrementViewCount(id);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/comment/{userId}")
    public ResponseEntity<VideoComment> addComment(@PathVariable Long id, @PathVariable Long userId, @RequestParam String content) {
        try {
            VideoComment videoComment = shortVideoService.addComment(id, userId, content);
            return ResponseEntity.ok(videoComment);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<VideoComment>> getComments(@PathVariable Long id) {
        try {
            List<VideoComment> videoComments = shortVideoService.getComments(id);
            return ResponseEntity.ok(videoComments);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/like-count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long id) {
        try {
            int likeCount = shortVideoService.getLikeCount(id);
            return ResponseEntity.ok(likeCount);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/share-count")
    public ResponseEntity<Integer> getShareCount(@PathVariable Long id) {
        try {
            int shareCount = shortVideoService.getShareCount(id);
            return ResponseEntity.ok(shareCount);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/comment-count")
    public ResponseEntity<Integer> getCommentCount(@PathVariable Long id) {
        try {
            int commentCount = shortVideoService.getCommentCount(id);
            return ResponseEntity.ok(commentCount);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/view-count")
    public ResponseEntity<Integer> getViewCount(@PathVariable Long id) {
        try {
            int viewCount = shortVideoService.getViewCount(id);
            return ResponseEntity.ok(viewCount);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/nested/{parentCommentId}/{userId}")
    public ResponseEntity<NestedComment> addNestedComment(
            @PathVariable Long parentCommentId,
            @PathVariable Long userId,
            @RequestParam String content) {
        try {
            NestedComment nestedComment = shortVideoService.addNestedComment(parentCommentId, userId, content);
            return ResponseEntity.ok(nestedComment);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/comment/{parentCommentId}/nested-comments")
    public ResponseEntity<List<NestedComment>> getNestedComments(@PathVariable Long parentCommentId) {
        try {
            List<NestedComment> nestedComments = shortVideoService.getNestedComments(parentCommentId);
            return ResponseEntity.ok(nestedComments);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{videoId}/comment/{commentId}/like/{userId}")
    public ResponseEntity<VideoComment> likeComment(
            @PathVariable Long videoId,
            @PathVariable Long commentId,
            @PathVariable Long userId) {
        try {
            VideoComment likedComment = shortVideoService.likeComment(videoId, commentId, userId);
            return ResponseEntity.ok(likedComment);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{videoId}/comment/{commentId}/likes")
    public ResponseEntity<Integer> getCommentLikes(
            @PathVariable Long videoId,
            @PathVariable Long commentId) {
        try {
            int likes = shortVideoService.getCommentLikes(videoId, commentId);
            return ResponseEntity.ok(likes);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/liked-videos/{userId}")
    public ResponseEntity<List<ShortVideo>> getLikedVideosByUser(@PathVariable Long userId) {
        try {
            List<ShortVideo> likedVideos = shortVideoService.getLikedVideosByUser(userId);
            return ResponseEntity.ok(likedVideos);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/is-liked/{videoId}/{userId}")
    public ResponseEntity<Boolean> isVideoLikedByUser(@PathVariable Long videoId, @PathVariable Long userId) {
        try {
            boolean isLiked = shortVideoService.isVideoLikedByUser(videoId, userId);
            return ResponseEntity.ok(isLiked);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/nested-comment/{nestedCommentId}/like/{userId}")
    public ResponseEntity<NestedComment> likeNestedComment(
            @PathVariable Long nestedCommentId,
            @PathVariable Long userId) {
        try {
            NestedComment likedNestedComment = shortVideoService.likeNestedComment(nestedCommentId, userId);
            return ResponseEntity.ok(likedNestedComment);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nested-comment/{nestedCommentId}/likes")
    public ResponseEntity<Integer> getNestedCommentLikes(@PathVariable Long nestedCommentId) {
        try {
            int likes = shortVideoService.getNestedCommentLikes(nestedCommentId);
            return ResponseEntity.ok(likes);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/nested-comment/{nestedCommentId}")
    public ResponseEntity<String> deleteNestedComment(@PathVariable Long nestedCommentId) {
        try {
            shortVideoService.deleteNestedComment(nestedCommentId);
            return ResponseEntity.ok("Nested comment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete nested comment: " + e.getMessage());
        }
    }

    @DeleteMapping("/{videoId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long videoId,
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        try {
            shortVideoService.deleteComment(videoId, commentId, userId);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to delete this comment");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete comment: " + e.getMessage());
        }
    }

    @PutMapping("comment/{videoId}/{commentId}/edit")
    public ResponseEntity<?> editComment(
            @PathVariable Long videoId,
            @PathVariable Long commentId,
            @RequestParam Long userId,
            @RequestParam String newContent) {
        try {
            if (newContent == null || newContent.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Content cannot be empty.");
            }

            VideoComment updatedComment = shortVideoService.editComment(videoId, commentId, userId, newContent);
            return ResponseEntity.ok(updatedComment);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to edit this comment.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video or Comment not found.");
        }
    }

    @PutMapping("nested/{nestedCommentId}/{userId}/edit")
    public ResponseEntity<NestedComment> editNestedComment(
            @PathVariable Long nestedCommentId,
            @PathVariable Long userId,
            @RequestParam(required = false) String newContent) {
        if (newContent == null || newContent.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Return 400 Bad Request if content is missing
        }

        try {
            NestedComment updatedNestedComment = shortVideoService.editNestedComment(nestedCommentId, userId, newContent);
            return ResponseEntity.ok(updatedNestedComment);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PutMapping("/{id}/category")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newCategory = request.get("category");
        if (newCategory == null || newCategory.isEmpty()) {
            return ResponseEntity.badRequest().body("Category cannot be null or empty");
        }

        boolean updated = shortVideoService.updateCategory(id, newCategory);
        if (updated) {
            return ResponseEntity.ok("Category updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ShortVideo not found");
        }
    }
    @GetMapping("/category/{category}")public ResponseEntity<List<ShortVideo>> getVideosByCategory(@PathVariable String category) {
        List<ShortVideo> videos = shortVideoService.getVideosByCategory(category);
        if (videos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/trending")
    public List<ShortVideo> getTrendingVideos(@RequestParam String category) {
        return shortVideoService.getTrendingVideos(category);
    }
    @GetMapping("/category/filtered")
    public List<ShortVideo> getFilteredShortVideos(
            @RequestParam String category,
            @RequestParam Long userId) {
        return shortVideoService.getFilteredShortVideos(category, userId);
    }
}
