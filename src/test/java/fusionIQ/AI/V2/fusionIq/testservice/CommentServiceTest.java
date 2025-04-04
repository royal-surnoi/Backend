package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.exception.UnauthorizedException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.CommentService;
import fusionIQ.AI.V2.fusionIq.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepo commentRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ImagePostRepo imagePostRepo;

    @Mock
    private ArticlePostRepo articlePostRepo;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NestedCommentRepo nestedCommentRepo;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCommentToImagePost() {
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setImagePost(imagePost);
        comment.setText("Nice image!");
        comment.setCommentDate(LocalDateTime.now());

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(imagePostRepo.findById(anyLong())).thenReturn(Optional.of(imagePost));
        when(commentRepo.save(any(Comment.class))).thenReturn(comment);

        Comment savedComment = commentService.addCommentToImagePost(1L, 1L, "Nice image!");

        assertNotNull(savedComment);
        assertEquals("Nice image!", savedComment.getText());
        verify(notificationService, times(1)).createCommentNotification(anyLong(), anyLong(), anyString(), eq("image"));
        verify(commentRepo, times(1)).save(any(Comment.class));
    }

    @Test
    public void testAddCommentToArticlePost() {
        User user = new User();
        user.setId(1L);
        ArticlePost articlePost = new ArticlePost();
        articlePost.setId(1L);
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setArticlePost(articlePost);
        comment.setText("Great article!");
        comment.setCommentDate(LocalDateTime.now());

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(articlePostRepo.findById(anyLong())).thenReturn(Optional.of(articlePost));
        when(commentRepo.save(any(Comment.class))).thenReturn(comment);

        Comment savedComment = commentService.addCommentToArticlePost(1L, 1L, "Great article!");

        assertNotNull(savedComment);
        assertEquals("Great article!", savedComment.getText());
        verify(notificationService, times(1)).createCommentNotification(anyLong(), anyLong(), anyString(), eq("article"));
        verify(commentRepo, times(1)).save(any(Comment.class));
    }

    @Test
    public void testGetCommentsByImagePostId() {
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());

        when(commentRepo.findByImagePostId(anyLong())).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByImagePostId(1L);

        assertEquals(2, result.size());
        verify(commentRepo, times(1)).findByImagePostId(anyLong());
    }

    @Test
    public void testGetCommentsByArticlePostId() {
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());

        when(commentRepo.findByArticlePostId(anyLong())).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByArticlePostId(1L);

        assertEquals(2, result.size());
        verify(commentRepo, times(1)).findByArticlePostId(anyLong());
    }

    @Test
    public void testDeleteComment() {
        doNothing().when(commentRepo).deleteById(anyLong());

        commentService.deleteComment(1L);

        verify(commentRepo, times(1)).deleteById(anyLong());
    }

    @Test
    public void testGetCommentsByUserIdAndImagePostId() {
        User user = new User();
        user.setId(1L);
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(commentRepo.findByUserIdAndImagePostId(anyLong(), anyLong())).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByUserIdAndImagePostId(1L, 1L);

        assertEquals(2, result.size());
        verify(commentRepo, times(1)).findByUserIdAndImagePostId(anyLong(), anyLong());
    }

    @Test
    public void testGetCommentsByUserIdAndArticlePostId() {
        User user = new User();
        user.setId(1L);
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(commentRepo.findByUserIdAndArticlePostId(anyLong(), anyLong())).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByUserIdAndArticlePostId(1L, 1L);

        assertEquals(2, result.size());
        verify(commentRepo, times(1)).findByUserIdAndArticlePostId(anyLong(), anyLong());
    }

    @Test
    public void testGetTotalCommentsByImagePostId() {
        when(commentRepo.countByImagePostId(anyLong())).thenReturn(5L);

        long count = commentService.getTotalCommentsByImagePostId(1L);

        assertEquals(5L, count);
        verify(commentRepo, times(1)).countByImagePostId(anyLong());
    }

    @Test
    public void testGetTotalCommentsByArticlePostId() {
        when(commentRepo.countByArticlePostId(anyLong())).thenReturn(3L);

        long count = commentService.getTotalCommentsByArticlePostId(1L);

        assertEquals(3L, count);
        verify(commentRepo, times(1)).countByArticlePostId(anyLong());
    }

    @Test
    public void testGetNestedComments() {
        Comment parentComment = new Comment();
        parentComment.setId(1L);
        List<Comment> nestedComments = Arrays.asList(new Comment(), new Comment());

        when(commentRepo.findById(anyLong())).thenReturn(Optional.of(parentComment));
        when(commentRepo.findByParentComment(any(Comment.class))).thenReturn(nestedComments);

        List<Comment> result = commentService.getNestedComments(1L);

        assertEquals(2, result.size());
        verify(commentRepo, times(1)).findByParentComment(any(Comment.class));
    }

    @Test
    public void testAddNestedCommentToImagePost() {
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);
        Comment parentComment = new Comment();
        parentComment.setId(1L);

        Comment nestedComment = new Comment();
        nestedComment.setUser(user);
        nestedComment.setImagePost(imagePost);
        nestedComment.setText("Reply to image comment");
        nestedComment.setParentComment(parentComment);
        nestedComment.setCommentDate(LocalDateTime.now());

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(imagePostRepo.findById(anyLong())).thenReturn(Optional.of(imagePost));
        when(commentRepo.findById(anyLong())).thenReturn(Optional.of(parentComment));
        when(commentRepo.save(any(Comment.class))).thenReturn(nestedComment);

        Comment savedComment = commentService.addNestedCommentToImagePost(1L, 1L, "Reply to image comment", 1L);

        assertNotNull(savedComment);
        assertEquals("Reply to image comment", savedComment.getText());
        verify(notificationService, times(1)).createCommentNotification(anyLong(), anyLong(), anyString(), eq("image"));
        verify(commentRepo, times(1)).save(any(Comment.class));
    }

    @Test
    public void testAddNestedCommentToArticlePost() {
        User user = new User();
        user.setId(1L);
        ArticlePost articlePost = new ArticlePost();
        articlePost.setId(1L);
        Comment parentComment = new Comment();
        parentComment.setId(1L);

        Comment nestedComment = new Comment();
        nestedComment.setUser(user);
        nestedComment.setArticlePost(articlePost);
        nestedComment.setText("Reply to article comment");
        nestedComment.setParentComment(parentComment);
        nestedComment.setCommentDate(LocalDateTime.now());

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(articlePostRepo.findById(anyLong())).thenReturn(Optional.of(articlePost));
        when(commentRepo.findById(anyLong())).thenReturn(Optional.of(parentComment));
        when(commentRepo.save(any(Comment.class))).thenReturn(nestedComment);

        Comment savedComment = commentService.addNestedCommentToArticlePost(1L, 1L, "Reply to article comment", 1L);

        assertNotNull(savedComment);
        assertEquals("Reply to article comment", savedComment.getText());
        verify(notificationService, times(1)).createCommentNotification(anyLong(), anyLong(), anyString(), eq("article"));
        verify(commentRepo, times(1)).save(any(Comment.class));
    }

    @Test
    public void testDeleteArticleComment() {
        ArticlePost articlePost = new ArticlePost();
        articlePost.setId(1L);
        Comment comment = new Comment();
        comment.setUser(new User());
        comment.getUser().setId(1L);
        comment.setArticlePost(articlePost);

        when(articlePostRepo.findById(anyLong())).thenReturn(Optional.of(articlePost));
        when(commentRepo.findById(anyLong())).thenReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.deleteArticleComment(1L, 1L, 1L));

        verify(nestedCommentRepo, times(1)).deleteByBaseCommentId(anyLong());
        verify(commentRepo, times(1)).delete(any(Comment.class));
    }

    @Test
    public void testDeleteImageComment() {
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);
        Comment comment = new Comment();
        comment.setUser(new User());
        comment.getUser().setId(1L);
        comment.setImagePost(imagePost);

        when(imagePostRepo.findById(anyLong())).thenReturn(Optional.of(imagePost));
        when(commentRepo.findById(anyLong())).thenReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.deleteImageComment(1L, 1L, 1L));

        verify(nestedCommentRepo, times(1)).deleteByBaseCommentId(anyLong());
        verify(commentRepo, times(1)).delete(any(Comment.class));
    }

    @Test
    public void testDeleteNestedComment() {
        NestedComment nestedComment = new NestedComment();
        nestedComment.setUser(new User());
        nestedComment.getUser().setId(1L);

        when(nestedCommentRepo.findById(anyLong())).thenReturn(Optional.of(nestedComment));

        assertDoesNotThrow(() -> commentService.deleteNestedComment(1L, 1L));

        verify(nestedCommentRepo, times(1)).delete(any(NestedComment.class));
    }

    @Test
    public void testEditComment() {
        Comment comment = new Comment();
        comment.setUser(new User());
        comment.getUser().setId(1L);
        comment.setText("Original text");

        when(commentRepo.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentRepo.save(any(Comment.class))).thenReturn(comment);

        Comment updatedComment = commentService.editComment(1L, 1L, "Updated text");

        assertEquals("Updated text", updatedComment.getText());
        verify(commentRepo, times(1)).save(any(Comment.class));
    }

    @Test
    public void testEditNestedComment() {
        NestedComment nestedComment = new NestedComment();
        nestedComment.setUser(new User());
        nestedComment.getUser().setId(1L);
        nestedComment.setContent("Original content");

        when(nestedCommentRepo.findById(anyLong())).thenReturn(Optional.of(nestedComment));
        when(nestedCommentRepo.save(any(NestedComment.class))).thenReturn(nestedComment);

        NestedComment updatedNestedComment = commentService.editNestedComment(1L, 1L, "Updated content");

        assertEquals("Updated content", updatedNestedComment.getContent());
        verify(nestedCommentRepo, times(1)).save(any(NestedComment.class));
    }
}

