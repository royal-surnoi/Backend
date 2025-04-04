package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.VideoLikeController;
import fusionIQ.AI.V2.fusionIq.data.VideoLike;
import fusionIQ.AI.V2.fusionIq.service.VideoLikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class VideoLikeControllerTest {

    @InjectMocks
    private VideoLikeController videoLikeController;

    @Mock
    private VideoLikeService videoLikeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllVideoLikes() {
        List<VideoLike> videoLikes = Arrays.asList(new VideoLike(), new VideoLike());
        when(videoLikeService.getAllVideoLikes()).thenReturn(videoLikes);

        List<VideoLike> result = videoLikeController.getAllVideoLikes();
        assertEquals(2, result.size());
        verify(videoLikeService, times(1)).getAllVideoLikes();
    }

    @Test
    public void testGetVideoLikeById_Found() {
        VideoLike videoLike = new VideoLike();
        when(videoLikeService.getVideoLikeById(anyLong())).thenReturn(Optional.of(videoLike));

        ResponseEntity<VideoLike> response = videoLikeController.getVideoLikeById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(videoLike, response.getBody());
        verify(videoLikeService, times(1)).getVideoLikeById(anyLong());
    }

    @Test
    public void testGetVideoLikeById_NotFound() {
        when(videoLikeService.getVideoLikeById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<VideoLike> response = videoLikeController.getVideoLikeById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(videoLikeService, times(1)).getVideoLikeById(anyLong());
    }

    @Test
    public void testCreateVideoLike() {
        VideoLike videoLike = new VideoLike();
        when(videoLikeService.createVideoLike(any(VideoLike.class))).thenReturn(videoLike);

        VideoLike result = videoLikeController.createVideoLike(videoLike);
        assertEquals(videoLike, result);
        verify(videoLikeService, times(1)).createVideoLike(any(VideoLike.class));
    }

    @Test
    public void testUpdateVideoLike_Success() {
        VideoLike videoLike = new VideoLike();
        when(videoLikeService.updateVideoLike(anyLong(), any(VideoLike.class))).thenReturn(videoLike);

        ResponseEntity<VideoLike> response = videoLikeController.updateVideoLike(1L, videoLike);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(videoLike, response.getBody());
        verify(videoLikeService, times(1)).updateVideoLike(anyLong(), any(VideoLike.class));
    }

    @Test
    public void testDeleteVideoLike() {
        doNothing().when(videoLikeService).deleteVideoLike(anyLong());

        ResponseEntity<Void> response = videoLikeController.deleteVideoLike(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(videoLikeService, times(1)).deleteVideoLike(anyLong());
    }
}
