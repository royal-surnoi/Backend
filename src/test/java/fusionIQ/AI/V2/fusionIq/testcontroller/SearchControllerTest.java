package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.SearchController;
import fusionIQ.AI.V2.fusionIq.data.Search;
import fusionIQ.AI.V2.fusionIq.service.SearchService;
import fusionIQ.AI.V2.fusionIq.repository.SearchRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SearchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SearchRepo searchRepo;

    @Mock
    private SearchService searchService;

    @InjectMocks
    private SearchController searchController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
    }

    @Test
    public void testGetSearchesByUserId_NoContent() throws Exception {
        when(searchRepo.findByUserId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetSearchesByUserId_WithContent() throws Exception {
        Search search = new Search();
        search.setId(1L);
        when(searchRepo.findByUserId(1L)).thenReturn(List.of(search));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testDeleteSearch() throws Exception {
        mockMvc.perform(delete("/deleteSearch/1"))
                .andExpect(status().isNoContent());

        verify(searchService, times(1)).deleteSearch(1L);
    }

    @Test
    public void testStoreSearchContent() throws Exception {
        Search search = new Search();
        search.setId(1L);

        when(searchService.storeSearchContent(1L, "search content")).thenReturn(search);

        mockMvc.perform(post("/storeSearchContent")
                        .param("userId", "1")
                        .param("searchContent", "search content"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    public void testStoreCourseContent() throws Exception {
        Search search = new Search();
        search.setId(1L);

        when(searchService.storeCourseContent(1L, "course content")).thenReturn(search);

        mockMvc.perform(post("/storeCourseContent")
                        .param("userId", "1")
                        .param("courseContent", "course content"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    public void testGetSearchContentByUserId() throws Exception {
        Search search = new Search();
        search.setId(1L);

        when(searchService.getSearchContentByUserId(1L)).thenReturn(List.of(search));

        mockMvc.perform(get("/getSearchContent")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testGetCourseContentByUserId() throws Exception {
        Search search = new Search();
        search.setId(1L);

        when(searchService.getCourseContentByUserId(1L)).thenReturn(List.of(search));

        mockMvc.perform(get("/getCourseContent")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testGetSearchContent() throws Exception {
        Search search = new Search();
        search.setId(1L);

        when(searchService.getSearchContent()).thenReturn(List.of(search));

        mockMvc.perform(get("/all/getSearchContent"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }

    @Test
    public void testGetCourseContent() throws Exception {
        Search search = new Search();
        search.setId(1L);

        when(searchService.getCourseContent()).thenReturn(List.of(search));

        mockMvc.perform(get("/all/getCourseContent"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1}]"));
    }
}
