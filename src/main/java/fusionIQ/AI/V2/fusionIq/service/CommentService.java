package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.exception.UnauthorizedException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ImagePostRepo imagePostRepo;
    @Autowired
    private ArticlePostRepo articlePostRepo;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NestedCommentRepo nestedCommentRepo;
    public Comment addCommentToImagePost(long userId, long imagePostId, String text) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ImagePost imagePost = imagePostRepo.findById(imagePostId).orElseThrow(() -> new RuntimeException("ImagePost not found"));
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setImagePost(imagePost);
        comment.setText(text);
        comment.setCommentDate(LocalDateTime.now());
        notificationService.createCommentNotification(userId, imagePostId, text,"image");

        return commentRepo.save(comment);
    }
    public Comment addCommentToArticlePost(long userId, long articlePostId, String text) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ArticlePost articlePost = articlePostRepo.findById(articlePostId).orElseThrow(() -> new RuntimeException("ArticlePost not found"));
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setArticlePost(articlePost);
        comment.setText(text);
        comment.setCommentDate(LocalDateTime.now());
        notificationService.createCommentNotification(userId, articlePostId, text,"article");

        return commentRepo.save(comment);
    }
    public List<Comment> getCommentsByImagePostId(long imagePostId) {
        return commentRepo.findByImagePostId(imagePostId);
    }
    public List<Comment> getCommentsByArticlePostId(long articlePostId) {
        return commentRepo.findByArticlePostId(articlePostId);
    }
    public void deleteComment(long commentId) {
        commentRepo.deleteById(commentId);
    }
    public List<Comment> getCommentsByUserIdAndImagePostId(long userId, long imagePostId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return commentRepo.findByUserIdAndImagePostId(userId, imagePostId);
    }
    public List<Comment> getCommentsByUserIdAndArticlePostId(long userId, long articlePostId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return commentRepo.findByUserIdAndArticlePostId(user.getId(), articlePostId);
    }

    public List<Comment> getCommentsByUserIdAndPostId(long userId, long postId) {
        List<Comment> comments = new ArrayList<>();
        List<Comment> imagePostComments = commentRepo.findByUserIdAndImagePostId(userId, postId);
        comments.addAll(imagePostComments);
        List<Comment> articlePostComments = commentRepo.findByUserIdAndArticlePostId(userId, postId);
        comments.addAll(articlePostComments);
        return comments;
    }
    public long getTotalCommentsByImagePostId(long imagePostId) {
        return commentRepo.countByImagePostId(imagePostId);
    }

    public long getTotalCommentsByArticlePostId(long articlePostId) {
        return commentRepo.countByArticlePostId(articlePostId);
    }
    public List<Comment> getNestedComments(long parentCommentId) {
        Comment parentComment = commentRepo.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        return commentRepo.findByParentComment(parentComment);
    }

    public Comment addNestedCommentToImagePost(long userId, long parentCommentId, String text, long imagePostId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ImagePost imagePost = imagePostRepo.findById(imagePostId).orElseThrow(() -> new RuntimeException("ImagePost not found"));

        Comment parentComment = commentRepo.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        Comment nestedComment = new Comment();
        nestedComment.setUser(user);
        nestedComment.setImagePost(imagePost);
        nestedComment.setText(text);
        nestedComment.setParentComment(parentComment);
        nestedComment.setCommentDate(LocalDateTime.now());
        notificationService.createCommentNotification(userId, imagePostId, text,"image");

        return commentRepo.save(nestedComment);
    }

    public Comment addNestedCommentToArticlePost(long userId, long parentCommentId, String text, long articlePostId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ArticlePost articlePost = articlePostRepo.findById(articlePostId).orElseThrow(() -> new RuntimeException("ArticlePost not found"));

        Comment parentComment = commentRepo.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        Comment nestedComment = new Comment();
        nestedComment.setUser(user);
        nestedComment.setArticlePost(articlePost);
        nestedComment.setText(text);
        nestedComment.setParentComment(parentComment);
        nestedComment.setCommentDate(LocalDateTime.now());
        notificationService.createCommentNotification(userId, articlePostId, text,"article");

        return commentRepo.save(nestedComment);
    }

    public List<Comment> getNestedCommentsForImagePost(long imagePostId, long parentCommentId) {
        return commentRepo.findNestedCommentsByImagePostIdAndParentCommentId(imagePostId, parentCommentId);
    }

    public List<Comment> getNestedCommentsForArticlePost(long articlePostId, long parentCommentId) {
        return commentRepo.findNestedCommentsByArticlePostIdAndParentCommentId(articlePostId, parentCommentId);
    }



    public NestedComment addNestedComment(Long userId, String content, Long baseCommentId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Comment baseComment = commentRepo.findById(baseCommentId).orElseThrow(() -> new RuntimeException("Parent comment not found"));

        NestedComment nestedComment = new NestedComment();
        nestedComment.setUser(user);
        nestedComment.setContent(content);
        nestedComment.setBaseComment(baseComment);
        nestedComment.setCreatedAt(LocalDateTime.now());

        return nestedCommentRepo.save(nestedComment);
    }

    // Get nested comments by parent comment
    public List<NestedComment> getNestedCommentsByParentComment(Long baseCommentId) {
        Comment baseComment = commentRepo.findById(baseCommentId).orElseThrow(() -> new RuntimeException("Parent comment not found"));
        return nestedCommentRepo.findByBaseComment(baseComment);
    }

    public NestedComment toggleLikeNestedComment(Long nestedCommentId, Long userId) {
        NestedComment nestedComment = nestedCommentRepo.findById(nestedCommentId)
                .orElseThrow(() -> new RuntimeException("Nested comment not found"));

        if (nestedComment.getLikedUserIds().contains(userId)) {
            // If the user has already liked this comment, remove the like
            nestedComment.getLikedUserIds().remove(userId);
            nestedComment.setLikes(nestedComment.getLikes() - 1);
        } else {
            // If the user has not liked this comment, add the like
            nestedComment.getLikedUserIds().add(userId);
            nestedComment.setLikes(nestedComment.getLikes() + 1);
        }

        return nestedCommentRepo.save(nestedComment);
    }
    public int getNestedCommentLikesCount(Long nestedCommentId) {
        NestedComment nestedComment = nestedCommentRepo.findById(nestedCommentId)
                .orElseThrow(() -> new RuntimeException("Nested comment not found with id " + nestedCommentId));
        return nestedComment.getLikes(); // or use getLikesCount() if you have such a method
    }

    public Comment toggleLikeComment(long commentId, long userId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + commentId));

        if (comment.getLikedUserIds().contains(userId)) {
            // If the user has already liked this comment, remove the like
            comment.getLikedUserIds().remove(userId);
            comment.setLikeCount(comment.getLikeCount() - 1);
        } else {
            // If the user has not liked this comment, add the like
            comment.getLikedUserIds().add(userId);
            comment.setLikeCount(comment.getLikeCount() + 1);
        }

        return commentRepo.save(comment);
    }

    public Map<String, Object> getCommentDetails(long commentId, long userId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + commentId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found with id " + userId));
        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", comment.getLikeCount());
        response.put("likedUserIds", comment.getLikedUserIds());

        return response;
    }

    // Delete comment for an article post
    @Transactional
    public void deleteArticleComment(Long articleId, Long commentId, Long userId) {
        ArticlePost articlePost = articlePostRepo.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // Check if the user is the comment owner or the article owner
        if (comment.getUser().getId() == (userId) || articlePost.getUser().getId() == (userId)) {
            nestedCommentRepo.deleteByBaseCommentId(commentId);

            commentRepo.delete(comment);
        } else {
            throw new UnauthorizedException("User is not authorized to delete this comment");
        }
    }

    public void deleteImageComment(Long imageId, Long commentId, Long userId) {
        ImagePost imagePost = imagePostRepo.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // Check if the user is the comment owner or the image owner
        if (comment.getUser().getId() == (userId) || imagePost.getUser().getId() == (userId)) {
            nestedCommentRepo.deleteByBaseCommentId(commentId);

            commentRepo.delete(comment);
        } else {
            throw new UnauthorizedException("User is not authorized to delete this comment");
        }
    }

//nested
    @Transactional
    public void deleteNestedComments(Long commentId) {
        // Find all nested comments associated with the base comment
        List<NestedComment> nestedComments = nestedCommentRepo.findByBaseCommentId(commentId);

        // Delete all nested comments
        for (NestedComment nestedComment : nestedComments) {
            nestedCommentRepo.delete(nestedComment);
        }
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // Check if the user is the comment owner
        if (comment.getUser().getId() == (userId)) {
            deleteNestedComments(commentId);
            commentRepo.delete(comment);
        } else {
            throw new UnauthorizedException("User is not authorized to delete this comment");
        }
    }

    @Transactional
    public void deleteNestedComment(Long nestedCommentId, Long userId) {
        NestedComment nestedComment = nestedCommentRepo.findById(nestedCommentId)
                .orElseThrow(() -> new ResourceNotFoundException("Nested comment not found"));

        // Check if the user is the owner of the nested comment
        if (nestedComment.getUser().getId()== (userId)) {
            nestedCommentRepo.delete(nestedComment);
        } else {
            throw new UnauthorizedException("User is not authorized to delete this nested comment");
        }
    }


    @Transactional
    public Comment editComment(long commentId, long userId, String newText) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // Check if the user is the post owner
        if (comment.getUser().getId() != userId) {
            throw new UnauthorizedException("User is not authorized to edit this comment");
        }

        comment.setText(newText);
        return commentRepo.save(comment);
    }

    @Transactional
    public NestedComment editNestedComment(long nestedCommentId, long userId, String newContent) {
        NestedComment nestedComment = nestedCommentRepo.findById(nestedCommentId)
                .orElseThrow(() -> new ResourceNotFoundException("Nested comment not found"));

        // Check if the user is the post owner
        if (nestedComment.getUser().getId() != userId) {
            throw new UnauthorizedException("User is not authorized to edit this nested comment");
        }

        nestedComment.setContent(newContent);
        return nestedCommentRepo.save(nestedComment);
    }


}
 