
        package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.LongVideo;
import fusionIQ.AI.V2.fusionIq.data.NestedComment;
import fusionIQ.AI.V2.fusionIq.data.VideoComment;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.exception.UnauthorizedException;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.service.LongVideoService;
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
@RequestMapping("/long-video")
public class LongVideoController {

    @Autowired
    private LongVideoService longVideoService;

    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadLongVideo(@PathVariable long userId, @RequestParam MultipartFile file ,@RequestParam(required = false) String longVideoDescription,@RequestParam(required = false) String tag,@RequestParam(required = false) String category) {
        try {
            LongVideo longVideo = longVideoService.uploadLongVideo(file, userId, longVideoDescription, tag,category);
            return new ResponseEntity<>("Long video uploaded successfully, URL: " + longVideo.getS3Url(), HttpStatus.OK);
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Long video upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/long-videos")
    public ResponseEntity<List<LongVideo>> getAllLongVideos() {
        List<LongVideo> longVideos = longVideoService.getAllLongVideos();
        return ResponseEntity.ok(longVideos);
    }

    @GetMapping("/long-videos/{id}")
    public ResponseEntity<LongVideo> getLongVideoById(@PathVariable Long id) {
        Optional<LongVideo> longVideo = longVideoService.getLongVideo(id);
        return longVideo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/longVideos/{userId}")
    public ResponseEntity<List<LongVideo>> getLongVideosByUserId(@PathVariable long userId) {
        try {
            List<LongVideo> longVideos =longVideoService.getLongVideosByUserId(userId);
            return ResponseEntity.ok(longVideos);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateLongVideoDescription(@PathVariable Long id, @RequestParam(required = false) String longVideoDescription, @RequestParam(required = false) String tag) {
        try {
            LongVideo updatedLongVideo = longVideoService.updateLongVideoDescription(id, longVideoDescription, tag);
            return ResponseEntity.ok("Long video description updated successfully, URL: " + updatedLongVideo.getS3Url());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update long video description: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLongVideo(@PathVariable long id) {
        try {
            longVideoService.deleteLongVideo(id);
            return ResponseEntity.ok("LongVideo deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete LongVideo: " + e.getMessage());
        }
    }



    @PostMapping("/{videoId}/like")
    public ResponseEntity<Void> likeVideo(@PathVariable Long videoId,@RequestParam Long userId) {
        try {
            longVideoService.likeVideo(videoId,userId);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<Void> shareVideo(@PathVariable Long id) {
        try {
            longVideoService.shareVideo(id);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long id) {
        try {
            longVideoService.incrementViewCount(id);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/comment/{userId}")
    public ResponseEntity<VideoComment> addComment(@PathVariable Long id, @PathVariable Long userId, @RequestParam String content) {
        try {
            VideoComment videoComment = longVideoService.addComment(id, userId, content);
            return ResponseEntity.ok(videoComment);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<VideoComment>> getComments(@PathVariable Long id) {
        try {
            List<VideoComment> videoComments = longVideoService.getComments(id);
            return ResponseEntity.ok(videoComments);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/like-count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long id) {
        try {
            int likeCount = longVideoService.getLikeCount(id);
            return ResponseEntity.ok(likeCount);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/share-count")
    public ResponseEntity<Integer> getShareCount(@PathVariable Long id) {
        try {
            int shareCount = longVideoService.getShareCount(id);
            return ResponseEntity.ok(shareCount);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/comment-count")
    public ResponseEntity<Integer> getCommentCount(@PathVariable Long id) {
        try {
            int commentCount = longVideoService.getCommentCount(id);
            return ResponseEntity.ok(commentCount);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/view-count")
    public ResponseEntity<Integer> getViewCount(@PathVariable Long id) {
        try {
            int viewCount = longVideoService.getViewCount(id);
            return ResponseEntity.ok(viewCount);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{id}/comment/{userId}/nested/{parentCommentId}")
    public ResponseEntity<NestedComment> addNestedComment(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestParam String content,
            @PathVariable Long parentCommentId) {
        try {
            NestedComment comment = longVideoService.addNestedComment(id, userId, content, parentCommentId);
            return ResponseEntity.ok(comment);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/comment/{parentCommentId}/nested")
    public ResponseEntity<List<NestedComment>> getNestedComments(@PathVariable Long parentCommentId) {
        List<NestedComment> nestedComments = longVideoService.getNestedComments(parentCommentId);
        return ResponseEntity.ok(nestedComments);
    }

    @PostMapping("/{videoId}/comment/{commentId}/like/{userId}")
    public ResponseEntity<VideoComment> likeComment(
            @PathVariable Long videoId,
            @PathVariable Long commentId,
            @PathVariable Long userId) {
        try {
            VideoComment likedComment = longVideoService.likeComment(videoId, commentId, userId);
            return ResponseEntity.ok(likedComment);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/longvideos/category-null")
    public List<LongVideo> getLongVideosWithNullCategory() {
        return longVideoService.getLongVideosWithNullCategory();
    }
    @GetMapping("/{videoId}/comment/{commentId}/likes")
    public ResponseEntity<Integer> getCommentLikes(
            @PathVariable Long videoId,
            @PathVariable Long commentId) {
        try {
            int likes = longVideoService.getCommentLikes(videoId, commentId);
            return ResponseEntity.ok(likes);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/liked-videos/{userId}")
    public ResponseEntity<List<LongVideo>> getLikedVideosByUser(@PathVariable Long userId) {
        try {
            List<LongVideo> likedVideos = longVideoService.getLikedVideosByUser(userId);
            return ResponseEntity.ok(likedVideos);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/is-liked/{videoId}/{userId}")
    public ResponseEntity<Boolean> isVideoLikedByUser(@PathVariable Long videoId, @PathVariable Long userId) {
        try {
            boolean isLiked = longVideoService.isVideoLikedByUser(videoId, userId);
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
            NestedComment likedNestedComment = longVideoService.likeNestedComment(nestedCommentId, userId);
            return ResponseEntity.ok(likedNestedComment);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nested-comment/{nestedCommentId}/likes")
    public ResponseEntity<Integer> getNestedCommentLikes(@PathVariable Long nestedCommentId) {
        try {
            int likes = longVideoService.getNestedCommentLikes(nestedCommentId);
            return ResponseEntity.ok(likes);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/nested-comment/{nestedCommentId}")
    public ResponseEntity<String> deleteNestedComment(@PathVariable Long nestedCommentId) {
        try {
            longVideoService.deleteNestedComment(nestedCommentId);
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
            longVideoService.deleteComment(videoId, commentId, userId);
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

            VideoComment updatedComment = longVideoService.editComment(videoId, commentId, userId, newContent);
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
            NestedComment updatedNestedComment = longVideoService.editNestedComment(nestedCommentId, userId, newContent);
            return ResponseEntity.ok(updatedNestedComment);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PutMapping("/{id}/category")
    public ResponseEntity<LongVideo> updateCategory(@PathVariable Long id, @RequestBody Map<String, String> categoryRequest) {
        String category = categoryRequest.get("category");
        LongVideo updatedVideo = longVideoService.updateCategory(id, category);
        if (updatedVideo != null) {
            return ResponseEntity.ok(updatedVideo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/category/{category}")
    public ResponseEntity<List<LongVideo>> getVideosByCategory(@PathVariable String category) {
        List<LongVideo> videos = longVideoService.getVideosByCategory(category);
        if (videos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videos);
    }

//    @GetMapping("/trending")
//    public List<LongVideo> getTrendingVideos(@RequestParam String category) {
//        return longVideoService.getTrendingVideos(category);

    @GetMapping("/trending")
    public ResponseEntity<List<LongVideo>> getTopPosts(@RequestParam String category) {
        List<LongVideo> topPosts = longVideoService.getTop10Posts(category);

        if (topPosts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(topPosts);
    }
    // Endpoint to fetch filtered long videos
    @GetMapping("/category/filtered")
    public List<LongVideo> getFilteredLongVideos(
            @RequestParam String category,
            @RequestParam Long userId) {
        return longVideoService.getFilteredLongVideos(category, userId);
    }
}
