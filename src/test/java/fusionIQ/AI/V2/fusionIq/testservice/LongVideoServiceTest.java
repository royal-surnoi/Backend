
package fusionIQ.AI.V2.fusionIq.testservice;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.UnauthorizedException;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.LongVideoService;
import fusionIQ.AI.V2.fusionIq.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LongVideoServiceTest {

    @Mock
    private LongVideoRepo longVideoRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private VideoCommentRepo videoCommentRepo;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NestedCommentRepo nestedCommentRepo;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private LongVideoService longVideoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testLikeVideo_Success() throws UserNotFoundException {
        LongVideo video = new LongVideo();
        video.setLongVideoLikes(0);
        User user = new User();
        user.setId(1L);
        when(longVideoRepo.findById(1L)).thenReturn(Optional.of(video));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        longVideoService.likeVideo(1L, 1L);

        assertEquals(1, video.getLongVideoLikes());
        assertTrue(video.getLikedByUsers().contains(user));
        verify(notificationService, times(1)).createLikePostNotification(1L, 1L, "long_video");
    }





    @Test
    void testAddComment_UserNotFound() {
        when(longVideoRepo.findById(1L)).thenReturn(Optional.of(new LongVideo()));
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            longVideoService.addComment(1L, 1L, "Great video!");
        });
    }

    @Test
    void testDeleteComment_Success() throws UnauthorizedException, UserNotFoundException {
        LongVideo video = new LongVideo();
        video.setUser(new User());
        video.getUser().setId(1L);
        VideoComment comment = new VideoComment();
        comment.setUser(new User());
        comment.getUser().setId(1L);

        when(longVideoRepo.findById(1L)).thenReturn(Optional.of(video));
        when(videoCommentRepo.findById(1L)).thenReturn(Optional.of(comment));

        longVideoService.deleteComment(1L, 1L, 1L);

        verify(videoCommentRepo, times(1)).delete(comment);
    }

    @Test
    void testDeleteComment_Unauthorized() throws UserNotFoundException {
        LongVideo video = new LongVideo();
        video.setUser(new User());
        video.getUser().setId(1L);
        VideoComment comment = new VideoComment();
        comment.setUser(new User());
        comment.getUser().setId(2L);

        when(longVideoRepo.findById(1L)).thenReturn(Optional.of(video));
        when(videoCommentRepo.findById(1L)).thenReturn(Optional.of(comment));

        assertThrows(UnauthorizedException.class, () -> {
            longVideoService.deleteComment(1L, 1L, 3L);
        });
    }


    @Test
    void testEditComment_Unauthorized() {
        VideoComment comment = new VideoComment();
        comment.setUser(new User());
        comment.getUser().setId(2L);
        when(longVideoRepo.findById(1L)).thenReturn(Optional.of(new LongVideo()));
        when(videoCommentRepo.findById(1L)).thenReturn(Optional.of(comment));

        assertThrows(UnauthorizedException.class, () -> {
            longVideoService.editComment(1L, 1L, 1L, "Updated content");
        });
    }




}
