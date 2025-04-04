package fusionIQ.AI.V2.fusionIq.testcontroller;




import fusionIQ.AI.V2.fusionIq.controller.CommentController;
import fusionIQ.AI.V2.fusionIq.data.Comment;
import fusionIQ.AI.V2.fusionIq.data.NestedComment;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.exception.UnauthorizedException;
import fusionIQ.AI.V2.fusionIq.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCommentToImagePost() {
        long userId = 1L;
        long imagePostId = 1L;
        String text = "This is a comment";

        Comment comment = new Comment();
        when(commentService.addCommentToImagePost(userId, imagePostId, text)).thenReturn(comment);

        Comment response = commentController.addCommentToImagePost(userId, imagePostId, text);

        assertEquals(comment, response);
    }

    @Test
    void testAddCommentToArticlePost() {
        long userId = 1L;
        long articlePostId = 1L;
        String text = "This is a comment";

        Comment comment = new Comment();
        when(commentService.addCommentToArticlePost(userId, articlePostId, text)).thenReturn(comment);

        Comment response = commentController.addCommentToArticlePost(userId, articlePostId, text);

        assertEquals(comment, response);
    }

    @Test
    void testGetCommentsByImagePostId() {
        long imagePostId = 1L;

        List<Comment> comments = new ArrayList<>();
        when(commentService.getCommentsByImagePostId(imagePostId)).thenReturn(comments);

        List<Comment> response = commentController.getCommentsByImagePostId(imagePostId);

        assertEquals(comments, response);
    }

    @Test
    void testGetCommentsByArticlePostId() {
        long articlePostId = 1L;

        List<Comment> comments = new ArrayList<>();
        when(commentService.getCommentsByArticlePostId(articlePostId)).thenReturn(comments);

        List<Comment> response = commentController.getCommentsByArticlePostId(articlePostId);

        assertEquals(comments, response);
    }

    @Test
    void testDeleteComment() {
        long commentId = 1L;

        doNothing().when(commentService).deleteComment(commentId);

        commentController.deleteComment(commentId);

        verify(commentService, times(1)).deleteComment(commentId);
    }

    @Test
    void testGetNestedComments() {
        long parentCommentId = 1L;

        List<Comment> comments = new ArrayList<>();
        when(commentService.getNestedComments(parentCommentId)).thenReturn(comments);

        List<Comment> response = commentController.getNestedComments(parentCommentId);

        assertEquals(comments, response);
    }

    @Test
    void testAddNestedCommentToImagePost() {
        long userId = 1L;
        long parentCommentId = 1L;
        String text = "This is a nested comment";
        long imagePostId = 1L;

        Comment nestedComment = new Comment();
        when(commentService.addNestedCommentToImagePost(userId, parentCommentId, text, imagePostId))
                .thenReturn(nestedComment);

        Comment response = commentController.addNestedComment(userId, parentCommentId, text, "image", imagePostId);

        assertEquals(nestedComment, response);
    }

    @Test
    void testAddNestedCommentToArticlePost() {
        long userId = 1L;
        long parentCommentId = 1L;
        String text = "This is a nested comment";
        long articlePostId = 1L;

        Comment nestedComment = new Comment();
        when(commentService.addNestedCommentToArticlePost(userId, parentCommentId, text, articlePostId))
                .thenReturn(nestedComment);

        Comment response = commentController.addNestedComment(userId, parentCommentId, text, "article", articlePostId);

        assertEquals(nestedComment, response);
    }

    @Test
    void testAddNestedCommentInvalidPostType() {
        long userId = 1L;
        long parentCommentId = 1L;
        String text = "This is a nested comment";
        long postId = 1L;

        assertThrows(IllegalArgumentException.class, () -> {
            commentController.addNestedComment(userId, parentCommentId, text, "invalid", postId);
        });
    }

    @Test
    void testDeleteNestedComment() {
        long nestedCommentId = 1L;
        long userId = 1L;

        doNothing().when(commentService).deleteNestedComment(nestedCommentId, userId);

        ResponseEntity<?> response = commentController.deleteNestedComment(nestedCommentId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentService, times(1)).deleteNestedComment(nestedCommentId, userId);
    }

    @Test
    void testDeleteNestedCommentUnauthorized() {
        long nestedCommentId = 1L;
        long userId = 1L;

        doThrow(new UnauthorizedException("User is not authorized"))
                .when(commentService).deleteNestedComment(nestedCommentId, userId);

        ResponseEntity<?> response = commentController.deleteNestedComment(nestedCommentId, userId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User is not authorized", response.getBody());
    }

    @Test
    void testEditComment() {
        long commentId = 1L;
        long userId = 1L;
        String newText = "Updated comment text";

        Comment updatedComment = new Comment();
        when(commentService.editComment(commentId, userId, newText)).thenReturn(updatedComment);

        ResponseEntity<Comment> response = commentController.editComment(commentId, userId, newText);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedComment, response.getBody());
    }

    @Test
    void testEditNestedComment() {
        long nestedCommentId = 1L;
        long userId = 1L;
        String newContent = "Updated nested comment text";

        NestedComment updatedNestedComment = new NestedComment();
        when(commentService.editNestedComment(nestedCommentId, userId, newContent)).thenReturn(updatedNestedComment);

        ResponseEntity<NestedComment> response = commentController.editNestedComment(nestedCommentId, userId, newContent);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedNestedComment, response.getBody());
    }

    @Test
    void testGetTotalCommentsByImagePostId() {
        long imagePostId = 1L;
        long count = 10L;

        when(commentService.getTotalCommentsByImagePostId(imagePostId)).thenReturn(count);

        long response = commentController.getTotalCommentsByImagePostId(imagePostId);

        assertEquals(count, response);
    }

    @Test
    void testGetTotalCommentsByArticlePostId() {
        long articlePostId = 1L;
        long count = 15L;

        when(commentService.getTotalCommentsByArticlePostId(articlePostId)).thenReturn(count);

        long response = commentController.getTotalCommentsByArticlePostId(articlePostId);

        assertEquals(count, response);
    }

    @Test
    void testToggleLikeComment() {
        long commentId = 1L;
        long userId = 1L;

        Comment comment = new Comment();
        when(commentService.toggleLikeComment(commentId, userId)).thenReturn(comment);

        Comment response = commentController.likeComment(commentId, userId);

        assertEquals(comment, response);
    }

    @Test
    void testToggleLikeNestedComment() {
        long nestedCommentId = 1L;
        long userId = 1L;

        NestedComment nestedComment = new NestedComment();
        when(commentService.toggleLikeNestedComment(nestedCommentId, userId)).thenReturn(nestedComment);

        NestedComment response = commentController.toggleLikeNestedComment(nestedCommentId, userId);

        assertEquals(nestedComment, response);
    }

    @Test
    void testGetNestedCommentLikes() {
        long nestedCommentId = 1L;
        int likes = 5;

        when(commentService.getNestedCommentLikesCount(nestedCommentId)).thenReturn(likes);

        ResponseEntity<Integer> response = commentController.getNestedCommentLikes(nestedCommentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(likes, response.getBody());
    }
}
