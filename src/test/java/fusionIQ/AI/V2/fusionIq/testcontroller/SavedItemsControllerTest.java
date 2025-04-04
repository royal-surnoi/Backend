package fusionIQ.AI.V2.fusionIq.testcontroller;


import fusionIQ.AI.V2.fusionIq.controller.SavedItemsController;
import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SavedItemsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SavedItemsRepo savedItemsRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ImagePostRepo imagePostRepo;

    @InjectMocks
    private SavedItemsController savedItemsController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(savedItemsController).build();
    }

    @Test
    public void testSaveImagePost_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(imagePostRepo.findById(1L)).thenReturn(Optional.of(imagePost));
        when(savedItemsRepo.findByUserAndImagePost(user, imagePost)).thenReturn(null);

        mockMvc.perform(post("/savedItems/saveImagePost")
                        .param("userId", "1")
                        .param("imagePostId", "1"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testSaveImagePost_AlreadySaved() throws Exception {
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);
        SavedItems savedItem = new SavedItems(user, imagePost);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(imagePostRepo.findById(1L)).thenReturn(Optional.of(imagePost));
        when(savedItemsRepo.findByUserAndImagePost(user, imagePost)).thenReturn(savedItem);

        mockMvc.perform(post("/savedItems/saveImagePost")
                        .param("userId", "1")
                        .param("imagePostId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item already saved"));
    }

    @Test
    public void testGetSavedPosts_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);
        SavedItems savedItem = new SavedItems(user, imagePost);

        when(savedItemsRepo.findByUserIdAndImagePostIsNotNull(1L)).thenReturn(Collections.singletonList(savedItem));

        mockMvc.perform(get("/savedItems/getSavedPosts")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testDeleteSavedImagePost_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);
        SavedItems savedItem = new SavedItems(user, imagePost);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(imagePostRepo.findById(1L)).thenReturn(Optional.of(imagePost));
        when(savedItemsRepo.findByUserAndImagePost(user, imagePost)).thenReturn(savedItem);

        mockMvc.perform(delete("/savedItems/deleteImagePost")
                        .param("userId", "1")
                        .param("imagePostId", "1"))
                .andExpect(status().isOk());

        verify(savedItemsRepo, times(1)).delete(savedItem);
    }

    @Test
    public void testDeleteSavedImagePost_NotFound() throws Exception {
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(imagePostRepo.findById(1L)).thenReturn(Optional.of(imagePost));
        when(savedItemsRepo.findByUserAndImagePost(user, imagePost)).thenReturn(null);

        mockMvc.perform(delete("/savedItems/deleteImagePost")
                        .param("userId", "1")
                        .param("imagePostId", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetSavedItemId_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);
        SavedItems savedItem = new SavedItems(user, imagePost);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(imagePostRepo.findById(1L)).thenReturn(Optional.of(imagePost));
        when(savedItemsRepo.findByUserAndImagePost(user, imagePost)).thenReturn(savedItem);

        mockMvc.perform(get("/savedItems/getSavedItemId")
                        .param("userId", "1")
                        .param("postType", "image")
                        .param("postId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(savedItem.getId())));
    }

    @Test
    public void testGetSavedItemId_NotFound() throws Exception {
        User user = new User();
        user.setId(1L);
        ImagePost imagePost = new ImagePost();
        imagePost.setId(1L);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(imagePostRepo.findById(1L)).thenReturn(Optional.of(imagePost));
        when(savedItemsRepo.findByUserAndImagePost(user, imagePost)).thenReturn(null);

        mockMvc.perform(get("/savedItems/getSavedItemId")
                        .param("userId", "1")
                        .param("postType", "image")
                        .param("postId", "1"))
                .andExpect(status().isNotFound());
    }
}
