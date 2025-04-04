package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.LongVideoController;
import fusionIQ.AI.V2.fusionIq.data.LongVideo;
import fusionIQ.AI.V2.fusionIq.data.NestedComment;
import fusionIQ.AI.V2.fusionIq.data.VideoComment;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.exception.UnauthorizedException;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.service.LongVideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LongVideoControllerTest {

    @Mock
    private LongVideoService longVideoService;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private LongVideoController longVideoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadLongVideo_Success() throws IOException, UserNotFoundException {
        LongVideo longVideo = new LongVideo();
        longVideo.setS3Url("http://example.com/video.mp4");
        when(longVideoService.uploadLongVideo(file, 1L, "description", "tag" , "category")).thenReturn(longVideo);

        ResponseEntity<String> response = longVideoController.uploadLongVideo(1L, file, "description", "tag", "category");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Long video uploaded successfully, URL: http://example.com/video.mp4", response.getBody());
        verify(longVideoService, times(1)).uploadLongVideo(file, 1L, "description", "tag", "category");
    }

    @Test
    void testUploadLongVideo_Failure() throws IOException, UserNotFoundException {
        when(longVideoService.uploadLongVideo(file, 1L, "description", "tag","category")).thenThrow(new IOException("Failed to upload"));

        ResponseEntity<String> response = longVideoController.uploadLongVideo(1L, file, "description", "tag","category");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Long video upload failed: Failed to upload", response.getBody());
        verify(longVideoService, times(1)).uploadLongVideo(file, 1L, "description", "tag","category");
    }

    @Test
    void testGetAllLongVideos() {
        List<LongVideo> longVideos = Arrays.asList(new LongVideo(), new LongVideo());
        when(longVideoService.getAllLongVideos()).thenReturn(longVideos);

        ResponseEntity<List<LongVideo>> response = longVideoController.getAllLongVideos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(longVideos, response.getBody());
        verify(longVideoService, times(1)).getAllLongVideos();
    }

    @Test
    void testGetLongVideoById_Success() {
        LongVideo longVideo = new LongVideo();
        when(longVideoService.getLongVideo(1L)).thenReturn(Optional.of(longVideo));

        ResponseEntity<LongVideo> response = longVideoController.getLongVideoById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(longVideo, response.getBody());
        verify(longVideoService, times(1)).getLongVideo(1L);
    }

    @Test
    void testGetLongVideoById_NotFound() {
        when(longVideoService.getLongVideo(1L)).thenReturn(Optional.empty());

        ResponseEntity<LongVideo> response = longVideoController.getLongVideoById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(longVideoService, times(1)).getLongVideo(1L);
    }

    @Test
    void testDeleteLongVideo_Success() {
        doNothing().when(longVideoService).deleteLongVideo(1L);

        ResponseEntity<String> response = longVideoController.deleteLongVideo(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("LongVideo deleted successfully", response.getBody());
        verify(longVideoService, times(1)).deleteLongVideo(1L);
    }

    @Test
    void testDeleteLongVideo_Failure() {
        doThrow(new RuntimeException("Failed to delete")).when(longVideoService).deleteLongVideo(1L);

        ResponseEntity<String> response = longVideoController.deleteLongVideo(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to delete LongVideo: Failed to delete", response.getBody());
        verify(longVideoService, times(1)).deleteLongVideo(1L);
    }

    @Test
    void testAddComment_Success() throws UserNotFoundException {
        VideoComment comment = new VideoComment();
        when(longVideoService.addComment(1L, 1L, "Nice video")).thenReturn(comment);

        ResponseEntity<VideoComment> response = longVideoController.addComment(1L, 1L, "Nice video");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comment, response.getBody());
        verify(longVideoService, times(1)).addComment(1L, 1L, "Nice video");
    }

    @Test
    void testAddComment_UserNotFound() throws UserNotFoundException {
        when(longVideoService.addComment(1L, 1L, "Nice video")).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<VideoComment> response = longVideoController.addComment(1L, 1L, "Nice video");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(longVideoService, times(1)).addComment(1L, 1L, "Nice video");
    }

    @Test
    void testLikeVideo_Success() throws UserNotFoundException {
        doNothing().when(longVideoService).likeVideo(1L, 1L);

        ResponseEntity<Void> response = longVideoController.likeVideo(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(longVideoService, times(1)).likeVideo(1L, 1L);
    }

    @Test
    void testLikeVideo_UserNotFound() throws UserNotFoundException {
        doThrow(new UserNotFoundException("User not found")).when(longVideoService).likeVideo(1L, 1L);

        ResponseEntity<Void> response = longVideoController.likeVideo(1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(longVideoService, times(1)).likeVideo(1L, 1L);
    }

    @Test
    void testUpdateLongVideoDescription_Success() throws UserNotFoundException {
        LongVideo longVideo = new LongVideo();
        longVideo.setS3Url("http://example.com/video.mp4");
        when(longVideoService.updateLongVideoDescription(1L, "Updated description", "Updated tag")).thenReturn(longVideo);

        ResponseEntity<String> response = longVideoController.updateLongVideoDescription(1L, "Updated description", "Updated tag");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Long video description updated successfully, URL: http://example.com/video.mp4", response.getBody());
        verify(longVideoService, times(1)).updateLongVideoDescription(1L, "Updated description", "Updated tag");
    }

    @Test
    void testUpdateLongVideoDescription_UserNotFound() throws UserNotFoundException {
        when(longVideoService.updateLongVideoDescription(1L, "Updated description", "Updated tag")).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<String> response = longVideoController.updateLongVideoDescription(1L, "Updated description", "Updated tag");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(longVideoService, times(1)).updateLongVideoDescription(1L, "Updated description", "Updated tag");
    }

    @Test
    void testGetComments_Success() throws UserNotFoundException {
        List<VideoComment> comments = Arrays.asList(new VideoComment(), new VideoComment());
        when(longVideoService.getComments(1L)).thenReturn(comments);

        ResponseEntity<List<VideoComment>> response = longVideoController.getComments(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comments, response.getBody());
        verify(longVideoService, times(1)).getComments(1L);
    }

    @Test
    void testGetComments_UserNotFound() throws UserNotFoundException {
        when(longVideoService.getComments(1L)).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<List<VideoComment>> response = longVideoController.getComments(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(longVideoService, times(1)).getComments(1L);
    }

    @Test
    void testAddNestedComment_Success() throws UserNotFoundException {
        NestedComment nestedComment = new NestedComment();
        when(longVideoService.addNestedComment(1L, 1L, "Nested comment", 1L)).thenReturn(nestedComment);

        ResponseEntity<NestedComment> response = longVideoController.addNestedComment(1L, 1L, "Nested comment", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(nestedComment, response.getBody());
        verify(longVideoService, times(1)).addNestedComment(1L, 1L, "Nested comment", 1L);
    }

    @Test
    void testAddNestedComment_UserNotFound() throws UserNotFoundException {
        when(longVideoService.addNestedComment(1L, 1L, "Nested comment", 1L)).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<NestedComment> response = longVideoController.addNestedComment(1L, 1L, "Nested comment", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(longVideoService, times(1)).addNestedComment(1L, 1L, "Nested comment", 1L);
    }

    @Test
    void testDeleteComment_Success() throws UserNotFoundException, UnauthorizedException {
        doNothing().when(longVideoService).deleteComment(1L, 1L, 1L);

        ResponseEntity<String> response = longVideoController.deleteComment(1L, 1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment deleted successfully", response.getBody());
        verify(longVideoService, times(1)).deleteComment(1L, 1L, 1L);
    }

    @Test
    void testDeleteComment_Unauthorized() throws UserNotFoundException, UnauthorizedException {
        doThrow(new UnauthorizedException("Unauthorized")).when(longVideoService).deleteComment(1L, 1L, 1L);

        ResponseEntity<String> response = longVideoController.deleteComment(1L, 1L, 1L);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Unauthorized to delete this comment", response.getBody());
        verify(longVideoService, times(1)).deleteComment(1L, 1L, 1L);
    }

    @Test
    void testDeleteComment_UserNotFound() throws UserNotFoundException, UnauthorizedException {
        doThrow(new UserNotFoundException("User not found")).when(longVideoService).deleteComment(1L, 1L, 1L);

        ResponseEntity<String> response = longVideoController.deleteComment(1L, 1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(longVideoService, times(1)).deleteComment(1L, 1L, 1L);
    }

    @Test
    void testEditComment_Success() throws UserNotFoundException, UnauthorizedException {
        VideoComment updatedComment = new VideoComment();
        when(longVideoService.editComment(1L, 1L, 1L, "Updated content")).thenReturn(updatedComment);

        ResponseEntity<?> response = longVideoController.editComment(1L, 1L, 1L, "Updated content");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedComment, response.getBody());
        verify(longVideoService, times(1)).editComment(1L, 1L, 1L, "Updated content");
    }

    @Test
    void testEditComment_Unauthorized() throws UserNotFoundException, UnauthorizedException {
        when(longVideoService.editComment(1L, 1L, 1L, "Updated content")).thenThrow(new UnauthorizedException("Unauthorized"));

        ResponseEntity<?> response = longVideoController.editComment(1L, 1L, 1L, "Updated content");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("You are not authorized to edit this comment.", response.getBody());
        verify(longVideoService, times(1)).editComment(1L, 1L, 1L, "Updated content");
    }

    @Test
    void testEditComment_UserNotFound() throws UserNotFoundException, UnauthorizedException {
        when(longVideoService.editComment(1L, 1L, 1L, "Updated content")).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<?> response = longVideoController.editComment(1L, 1L, 1L, "Updated content");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Video or Comment not found.", response.getBody());
        verify(longVideoService, times(1)).editComment(1L, 1L, 1L, "Updated content");
    }
}
