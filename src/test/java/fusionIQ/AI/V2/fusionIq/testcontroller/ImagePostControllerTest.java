package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.ImagePostController;
import fusionIQ.AI.V2.fusionIq.data.ImagePost;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.service.ImagePostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImagePostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImagePostService imagePostService;

    @InjectMocks
    private ImagePostController imagePostController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(imagePostController).build();
    }

    @Test
    public void testCreateImagePost() throws Exception {
        MockMultipartFile photo = new MockMultipartFile("photo", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);

        when(imagePostService.createImagePost(anyLong(), any(byte[].class), anyString(), anyString(), anyString())).thenReturn(imagePost);

        mockMvc.perform(multipart("/api/imagePosts/create")
                        .file(photo)
                        .param("userId", "1")
                        .param("imageDescription", "Test description")
                        .param("category", "Test category")
                        .param("tag", "Test tag"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    public void testGetImagePostById() throws Exception {
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);

        when(imagePostService.getImagePostById(1L)).thenReturn(imagePost);

        mockMvc.perform(get("/api/imagePosts/get/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    public void testGetAllImagePosts() throws Exception {
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);

        when(imagePostService.getAllImagePosts()).thenReturn(List.of(imagePost));

        mockMvc.perform(get("/api/imagePosts/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testGetAllImagePostsByUserId() throws Exception {
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);

        when(imagePostService.getAllImagePostsByUserId(1L)).thenReturn(List.of(imagePost));

        mockMvc.perform(get("/api/imagePosts/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }


    @Test
    public void testDeleteImagePost() throws Exception {
        doNothing().when(imagePostService).deleteImagePost(1L);

        mockMvc.perform(delete("/api/imagePosts/delete/1"))
                .andExpect(status().isOk());

        verify(imagePostService, times(1)).deleteImagePost(1L);
    }

    @Test
    public void testLikeImagePost() throws Exception {
        ImagePost likedPost = new ImagePost();
        likedPost.setId(1L);

        when(imagePostService.likeImagePost(1L, 1L)).thenReturn(likedPost);

        mockMvc.perform(post("/api/imagePosts/1/like")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    public void testDislikeImagePost() throws Exception {
        ImagePost dislikedPost = new ImagePost();
        dislikedPost.setId(1L);

        when(imagePostService.dislikeImagePost(1L)).thenReturn(dislikedPost);

        mockMvc.perform(post("/api/imagePosts/1/dislike"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    public void testShareImagePost() throws Exception {
        ImagePost sharedPost = new ImagePost();
        sharedPost.setId(1L);

        when(imagePostService.shareImagePost(1L)).thenReturn(sharedPost);

        mockMvc.perform(post("/api/imagePosts/1/share"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    public void testGetLikeCountByImagePostId() throws Exception {
        when(imagePostService.getLikeCountByImagePostId(1L)).thenReturn(10);

        mockMvc.perform(get("/api/imagePosts/1/likeCount"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    public void testGetShareCountByImagePostId() throws Exception {
        when(imagePostService.getShareCountByImagePostId(1L)).thenReturn(5);

        mockMvc.perform(get("/api/imagePosts/1/shareCount"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    public void testGetUsersWhoLikedPost() throws Exception {
        User user = new User();
        user.setId(1L);

        when(imagePostService.getUsersWhoLikedImagePost(1L)).thenReturn(List.of(user));

        mockMvc.perform(get("/api/imagePosts/1/likedUsers"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testIsImagePostLikedByUser() throws Exception {
        when(imagePostService.isImagePostLikedByUser(1L, 1L)).thenReturn(true);

        mockMvc.perform(get("/api/imagePosts/1/liked-by/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
