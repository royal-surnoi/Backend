package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.VideoLike;
import fusionIQ.AI.V2.fusionIq.repository.VideoLikeRepo;
import fusionIQ.AI.V2.fusionIq.service.VideoLikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VideoLikeServiceTest {

    @InjectMocks
    private VideoLikeService videoLikeService;

    @Mock
    private VideoLikeRepo videoLikeRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllVideoLikes() {
        List<VideoLike> videoLikes = Arrays.asList(new VideoLike(), new VideoLike());
        when(videoLikeRepo.findAll()).thenReturn(videoLikes);

        List<VideoLike> result = videoLikeService.getAllVideoLikes();
        assertEquals(2, result.size());
        verify(videoLikeRepo, times(1)).findAll();
    }

    @Test
    public void testGetVideoLikeById_Found() {
        VideoLike videoLike = new VideoLike();
        when(videoLikeRepo.findById(anyLong())).thenReturn(Optional.of(videoLike));

        Optional<VideoLike> result = videoLikeService.getVideoLikeById(1L);
        assertTrue(result.isPresent());
        assertEquals(videoLike, result.get());
        verify(videoLikeRepo, times(1)).findById(anyLong());
    }

    @Test
    public void testGetVideoLikeById_NotFound() {
        when(videoLikeRepo.findById(anyLong())).thenReturn(Optional.empty());

        Optional<VideoLike> result = videoLikeService.getVideoLikeById(1L);
        assertFalse(result.isPresent());
        verify(videoLikeRepo, times(1)).findById(anyLong());
    }

    @Test
    public void testCreateVideoLike() {
        VideoLike videoLike = new VideoLike();
        when(videoLikeRepo.save(any(VideoLike.class))).thenReturn(videoLike);

        VideoLike result = videoLikeService.createVideoLike(videoLike);
        assertEquals(videoLike, result);
        verify(videoLikeRepo, times(1)).save(any(VideoLike.class));
    }

    @Test
    public void testUpdateVideoLike_Success() {
        VideoLike existingVideoLike = new VideoLike();
        VideoLike updatedVideoLike = new VideoLike();

        // Assuming User is a class and has a constructor that takes a username
        User updatedUser = new User();
        updatedUser.setName("Updated User");

        updatedVideoLike.setUser(updatedUser);  // Use a User object instead of a String

        when(videoLikeRepo.findById(anyLong())).thenReturn(Optional.of(existingVideoLike));
        when(videoLikeRepo.save(any(VideoLike.class))).thenReturn(updatedVideoLike);

        VideoLike result = videoLikeService.updateVideoLike(1L, updatedVideoLike);

        assertEquals("Updated User", result.getUser().getName());  // Adjust according to your User class structure
        verify(videoLikeRepo, times(1)).findById(anyLong());
        verify(videoLikeRepo, times(1)).save(any(VideoLike.class));
    }


    @Test
    public void testUpdateVideoLike_NotFound() {
        VideoLike updatedVideoLike = new VideoLike();
        when(videoLikeRepo.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            videoLikeService.updateVideoLike(1L, updatedVideoLike);
        });

        assertEquals("VideoLike not found with id 1", exception.getMessage());
        verify(videoLikeRepo, times(1)).findById(anyLong());
    }

    @Test
    public void testDeleteVideoLike() {
        doNothing().when(videoLikeRepo).deleteById(anyLong());

        videoLikeService.deleteVideoLike(1L);
        verify(videoLikeRepo, times(1)).deleteById(anyLong());
    }
}

