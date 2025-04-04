package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.ArticlePostController;
import fusionIQ.AI.V2.fusionIq.data.ArticlePost;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.service.ArticlePostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArticlePostController.class)
public class ArticlePostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticlePostService articlePostService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateArticlePost() throws Exception {
        // Arrange
        ArticlePost mockArticlePost = new ArticlePost();
        mockArticlePost.setId(1L);

        when(articlePostService.createArticlePost(1L, "Sample Article", "Tag", "Category"))
                .thenReturn(mockArticlePost);

        // Act & Assert
        mockMvc.perform(post("/api/articleposts/create")
                        .param("userId", "1")
                        .param("article", "Sample Article")
                        .param("tag", "Tag")
                        .param("category", "Category")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetArticlePostById() throws Exception {
        ArticlePost mockArticlePost = new ArticlePost();
        mockArticlePost.setId(1L);

        when(articlePostService.getArticlePostById(1L)).thenReturn(mockArticlePost);

        mockMvc.perform(get("/api/articleposts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1}"));
    }

    @Test
    void testGetAllArticlePosts() throws Exception {
        ArticlePost mockArticlePost = new ArticlePost();
        mockArticlePost.setId(1L);

        List<ArticlePost> mockArticlePosts = Arrays.asList(mockArticlePost);

        when(articlePostService.getAllArticlePosts()).thenReturn(mockArticlePosts);

        mockMvc.perform(get("/api/articleposts/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1}]"));
    }

    @Test
    void testUpdateArticlePost() throws Exception {
        ArticlePost mockArticlePost = new ArticlePost();
        mockArticlePost.setId(1L);

        when(articlePostService.updateArticlePost(1L, "Updated Article", "Updated Tag")).thenReturn(mockArticlePost);

        mockMvc.perform(put("/api/articleposts/1")
                        .param("article", "Updated Article")
                        .param("tag", "Updated Tag")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1}"));
    }

    @Test
    void testDeleteArticlePost() throws Exception {
        mockMvc.perform(delete("/api/articleposts/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ArticlePost deleted successfully"));
    }

    @Test
    void testLikeArticlePost() throws Exception {
        ArticlePost mockArticlePost = new ArticlePost();
        mockArticlePost.setId(1L);

        when(articlePostService.likeArticlePost(1L, 1L)).thenReturn(mockArticlePost);

        mockMvc.perform(post("/api/articleposts/1/like")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1}"));
    }

    @Test
    void testDislikeArticlePost() throws Exception {
        ArticlePost mockArticlePost = new ArticlePost();
        mockArticlePost.setId(1L);

        when(articlePostService.dislikeArticlePost(1L, 1L)).thenReturn(mockArticlePost);

        mockMvc.perform(post("/api/articleposts/1/dislike")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1}"));
    }

    @Test
    void testShareArticlePost() throws Exception {
        ArticlePost mockArticlePost = new ArticlePost();
        mockArticlePost.setId(1L);

        when(articlePostService.shareArticlePost(1L)).thenReturn(mockArticlePost);

        mockMvc.perform(post("/api/articleposts/1/share")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1}"));
    }

    @Test
    void testGetLikeCountByArticlePostId() throws Exception {
        when(articlePostService.getLikeCountByArticlePostId(1L)).thenReturn(5);

        mockMvc.perform(get("/api/articleposts/1/likeCount")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testGetShareCountByArticlePostId() throws Exception {
        when(articlePostService.getShareCountByArticlePostId(1L)).thenReturn(3);

        mockMvc.perform(get("/api/articleposts/1/shareCount")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void testGetUsersWhoLikedPost() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);

        when(articlePostService.getUsersWhoLikedPost(1L)).thenReturn(Arrays.asList(mockUser));

        mockMvc.perform(get("/api/articleposts/1/likedUsers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1}]"));
    }

    @Test
    void testIsArticlePostLikedByUser() throws Exception {
        when(articlePostService.isArticlePostLikedByUser(1L, 1L)).thenReturn(true);

        mockMvc.perform(get("/api/articleposts/1/liked-by/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
