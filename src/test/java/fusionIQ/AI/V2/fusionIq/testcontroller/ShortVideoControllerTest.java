package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.ShortVideoController;
import fusionIQ.AI.V2.fusionIq.data.ShortVideo;
import fusionIQ.AI.V2.fusionIq.data.VideoComment;
import fusionIQ.AI.V2.fusionIq.exception.UnauthorizedException;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.service.ShortVideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ShortVideoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShortVideoService shortVideoService;

    @InjectMocks
    private ShortVideoController shortVideoController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shortVideoController).build();
    }

    @Test
    public void testUploadShortVideo() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        ShortVideo shortVideo = new ShortVideo();
        shortVideo.setS3Url("http://s3.url/video.mp4");

        when(shortVideoService.uploadShortVideo(any(MultipartFile.class), anyLong(), anyString(), anyString(),anyString())).thenReturn(shortVideo);

        mockMvc.perform(multipart("/short-video/upload/1")
                        .file("file", file.getBytes())
                        .param("shortVideoDescription", "Sample Description")
                        .param("category", "Sample Category")
                        .param("tag", "Sample Tag"))
                .andExpect(status().isOk())
                .andExpect(content().string("Short video uploaded successfully, URL: http://s3.url/video.mp4"));
    }

    @Test
    public void testGetAllShortVideos() throws Exception {
        ShortVideo shortVideo = new ShortVideo();
        shortVideo.setId(1L);

        when(shortVideoService.getAllShortVideos()).thenReturn(List.of(shortVideo));

        mockMvc.perform(get("/short-video/short-videos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testGetShortVideoById() throws Exception {
        ShortVideo shortVideo = new ShortVideo();
        shortVideo.setId(1L);

        when(shortVideoService.getShortVideo(1L)).thenReturn(Optional.of(shortVideo));

        mockMvc.perform(get("/short-video/short-videos/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    public void testGetShortVideoById_NotFound() throws Exception {
        when(shortVideoService.getShortVideo(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/short-video/short-videos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteShortVideo() throws Exception {
        doNothing().when(shortVideoService).deleteShortVideo(1L);

        mockMvc.perform(delete("/short-video/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("ShortVideo deleted successfully"));
    }

    @Test
    public void testDeleteShortVideo_Failure() throws Exception {
        doThrow(new RuntimeException("Failed to delete")).when(shortVideoService).deleteShortVideo(1L);

        mockMvc.perform(delete("/short-video/delete/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to delete ShortVideo: Failed to delete"));
    }

    @Test
    public void testLikeVideo() throws Exception {
        doNothing().when(shortVideoService).likeVideo(1L, 1L);

        mockMvc.perform(post("/short-video/1/like")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddComment() throws Exception {
        VideoComment comment = new VideoComment();
        comment.setVideoCommentContent("Sample Comment");

        when(shortVideoService.addComment(1L, 1L, "Sample Comment")).thenReturn(comment);

        mockMvc.perform(post("/short-video/1/comment/1")
                        .param("content", "Sample Comment"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"videoCommentContent\":\"Sample Comment\"}"));
    }

    @Test
    public void testEditComment_Unauthorized() throws Exception {
        doThrow(new UnauthorizedException("You are not authorized to edit this comment."))
                .when(shortVideoService).editComment(anyLong(), anyLong(), anyLong(), anyString());

        mockMvc.perform(put("/short-video/comment/1/1/edit")
                        .param("userId", "1")
                        .param("newContent", "Updated Comment"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("You are not authorized to edit this comment."));
    }
}
