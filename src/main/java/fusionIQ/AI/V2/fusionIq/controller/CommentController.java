package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Comment;
import fusionIQ.AI.V2.fusionIq.data.NestedComment;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.exception.UnauthorizedException;
import fusionIQ.AI.V2.fusionIq.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/imagepost/{imagePostId}/add")
    public Comment addCommentToImagePost(@RequestParam long userId, @PathVariable long imagePostId, @RequestParam String text) {
        return commentService.addCommentToImagePost(userId, imagePostId, text);
    }
    @PostMapping("/articlepost/{articlePostId}/add")
    public Comment addCommentToArticlePost(@RequestParam long userId, @PathVariable long articlePostId, @RequestParam String text) {
        return commentService.addCommentToArticlePost(userId, articlePostId, text);
    }
    @GetMapping("/imagepost/{imagePostId}")
    public List<Comment> getCommentsByImagePostId(@PathVariable long imagePostId) {
        return commentService.getCommentsByImagePostId(imagePostId);
    }
    @GetMapping("/articlepost/{articlePostId}")
    public List<Comment> getCommentsByArticlePostId(@PathVariable long articlePostId) {
        return commentService.getCommentsByArticlePostId(articlePostId);
    }
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable long commentId) {
        commentService.deleteComment(commentId);
    }
    @GetMapping("/user/{userId}/image/{imagePostId}")
    public List<Comment> getCommentsByUserIdAndImagePostId(@PathVariable long userId, @PathVariable long imagePostId) {
        return commentService.getCommentsByUserIdAndImagePostId(userId, imagePostId);
    }
    @GetMapping("/user/{userId}/article/{articlePostId}")
    public List<Comment> getCommentsByUserIdAndArticlePostId(@PathVariable long userId, @PathVariable long articlePostId) {
        return commentService.getCommentsByUserIdAndArticlePostId(userId, articlePostId);
    }
    @GetMapping("/user/{userId}/post/{postId}")
    public List<Comment> getCommentsByUserIdAndPostId(@PathVariable long userId, @PathVariable long postId) {
        return commentService.getCommentsByUserIdAndPostId(userId, postId);
    }
    // Add these endpoints to get the total count of comments
    @GetMapping("/count/imagepost/{imagePostId}")
    public long getTotalCommentsByImagePostId(@PathVariable long imagePostId) {
        return commentService.getTotalCommentsByImagePostId(imagePostId);
    }

    @GetMapping("/count/articlepost/{articlePostId}")
    public long getTotalCommentsByArticlePostId(@PathVariable long articlePostId) {
        return commentService.getTotalCommentsByArticlePostId(articlePostId);
    }
    @GetMapping("/nested/{parentCommentId}")
    public List<Comment> getNestedComments(@PathVariable long parentCommentId) {
        return commentService.getNestedComments(parentCommentId);
    }

    @PostMapping("/nested/{parentCommentId}/add")
    public Comment addNestedComment(
            @RequestParam long userId,
            @PathVariable long parentCommentId,
            @RequestParam String text,
            @RequestParam String postType,
            @RequestParam long postId) {

        if ("image".equalsIgnoreCase(postType)) {
            return commentService.addNestedCommentToImagePost(userId, parentCommentId, text, postId);
        } else if ("article".equalsIgnoreCase(postType)) {
            return commentService.addNestedCommentToArticlePost(userId, parentCommentId, text, postId);
        } else {
            throw new IllegalArgumentException("Invalid postType. Must be 'image' or 'article'.");
        }
    }

    @GetMapping("/imagepost/{imagePostId}/nested/{parentCommentId}")
    public ResponseEntity<List<Comment>> getNestedCommentsForImagePost(
            @PathVariable long imagePostId, @PathVariable long parentCommentId) {

        List<Comment> nestedComments = commentService.getNestedCommentsForImagePost(imagePostId, parentCommentId);
        return ResponseEntity.ok().body(nestedComments);
    }

    @GetMapping("/articlepost/{articlePostId}/nested/{parentCommentId}")
    public ResponseEntity<List<Comment>> getNestedCommentsForArticlePost(
            @PathVariable long articlePostId, @PathVariable long parentCommentId) {

        List<Comment> nestedComments = commentService.getNestedCommentsForArticlePost(articlePostId, parentCommentId);

        return ResponseEntity.ok().body(nestedComments);
    }


    @PostMapping("/add/{userId}/{baseCommentId}/nested")
    public NestedComment addNestedComment(
            @PathVariable Long userId,
            @RequestParam String content,
            @PathVariable Long baseCommentId) {

        return commentService.addNestedComment(userId, content, baseCommentId);
    }

    @GetMapping("/parent-comment/{baseCommentId}/nested")
    public List<NestedComment> getNestedCommentsByParentComment(@PathVariable Long baseCommentId) {
        return commentService.getNestedCommentsByParentComment(baseCommentId);
    }
    @PostMapping("/{nestedCommentId}/{userId}/toggle-like")
    public NestedComment toggleLikeNestedComment(@PathVariable Long nestedCommentId, @PathVariable Long userId) {
        return commentService.toggleLikeNestedComment(nestedCommentId, userId);
    }

    @GetMapping("/{nestedCommentId}/likes")
    public ResponseEntity<Integer> getNestedCommentLikes(@PathVariable Long nestedCommentId) {
        int likesCount = commentService.getNestedCommentLikesCount(nestedCommentId);
        return ResponseEntity.ok(likesCount);
    }
    @PostMapping("/comment/{commentId}/{userId}/like")
    public Comment likeComment(@PathVariable long commentId, @PathVariable long userId) {
        return commentService.toggleLikeComment(commentId, userId);
    }

    @GetMapping("/comment/{commentId}/likes")
    public Map<String, Object> getCommentDetails(@PathVariable long commentId, @RequestParam long userId) {
        return commentService.getCommentDetails(commentId, userId);
    }

    // Delete a comment for an article post
    @DeleteMapping("/article/{articleId}/{commentId}")
    public ResponseEntity<?> deleteArticleComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        commentService.deleteArticleComment(articleId, commentId, userId);
        return ResponseEntity.ok().build();
    }

    // Delete a comment for an image post
    @DeleteMapping("/image/{imageId}/{commentId}")
    public ResponseEntity<?> deleteImageComment(
            @PathVariable Long imageId,
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        commentService.deleteImageComment(imageId, commentId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("nested/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @RequestParam Long userId) {
        try {
            commentService.deleteComment(commentId, userId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException | UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    @DeleteMapping("/{nestedCommentId}/nested")
    public ResponseEntity<?> deleteNestedComment(@PathVariable Long nestedCommentId,
                                                 @RequestParam Long userId) {
        try {
            commentService.deleteNestedComment(nestedCommentId, userId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException | UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // Edit a comment by the post owner
    @PutMapping("/comment/{commentId}/edit")
    public ResponseEntity<Comment> editComment(
            @PathVariable long commentId,
            @RequestParam long userId,
            @RequestParam String newText) {
        Comment updatedComment = commentService.editComment(commentId, userId, newText);
        return ResponseEntity.ok(updatedComment);
    }

    // Edit a nested comment by the post owner
    @PutMapping("/nested/{nestedCommentId}/edit")
    public ResponseEntity<NestedComment> editNestedComment(
            @PathVariable long nestedCommentId,
            @RequestParam long userId,
            @RequestParam String newContent) {
        NestedComment updatedNestedComment = commentService.editNestedComment(nestedCommentId, userId, newContent);
        return ResponseEntity.ok(updatedNestedComment);
    }
}
 