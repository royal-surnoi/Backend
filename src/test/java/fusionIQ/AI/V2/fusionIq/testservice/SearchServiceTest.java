package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.Search;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.SearchRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SearchServiceTest {

    @Mock
    private SearchRepo searchRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddSearch_UserFound() {
        User user = new User();
        user.setId(1L);
        Search search = new Search();

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(searchRepo.save(search)).thenReturn(search);

        Search savedSearch = searchService.addSearch(1L, search);

        assertEquals(search, savedSearch);
        verify(searchRepo, times(1)).save(search);
    }

    @Test
    public void testAddSearch_UserNotFound() {
        Search search = new Search();

        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> searchService.addSearch(1L, search));
    }

    @Test
    public void testDeleteSearch() {
        searchService.deleteSearch(1L);
        verify(searchRepo, times(1)).deleteById(1L);
    }

    @Test
    public void testStoreSearchContent() {
        User user = new User();
        user.setId(1L);
        Search search = new Search();
        search.setSearchContent("search content");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(searchRepo.save(any(Search.class))).thenReturn(search);

        Search savedSearch = searchService.storeSearchContent(1L, "search content");

        assertEquals("search content", savedSearch.getSearchContent());
        verify(searchRepo, times(1)).save(any(Search.class));
    }

    @Test
    public void testStoreCourseContent() {
        User user = new User();
        user.setId(1L);
        Search search = new Search();
        search.setCourseContent("course content");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(searchRepo.save(any(Search.class))).thenReturn(search);

        Search savedSearch = searchService.storeCourseContent(1L, "course content");

        assertEquals("course content", savedSearch.getCourseContent());
        verify(searchRepo, times(1)).save(any(Search.class));
    }

    @Test
    public void testGetSearchContentByUserId() {
        Search search = new Search();
        search.setId(1L);

        when(searchRepo.findByUserIdAndSearchContentIsNotNull(1L)).thenReturn(Collections.singletonList(search));

        assertEquals(1, searchService.getSearchContentByUserId(1L).size());
    }

    @Test
    public void testGetCourseContentByUserId() {
        Search search = new Search();
        search.setId(1L);

        when(searchRepo.findByUserIdAndCourseContentIsNotNull(1L)).thenReturn(Collections.singletonList(search));

        assertEquals(1, searchService.getCourseContentByUserId(1L).size());
    }

    @Test
    public void testGetSearchContent() {
        Search search = new Search();
        search.setId(1L);

        when(searchRepo.findBySearchContentIsNotNull()).thenReturn(Collections.singletonList(search));

        assertEquals(1, searchService.getSearchContent().size());
    }

    @Test
    public void testGetCourseContent() {
        Search search = new Search();
        search.setId(1L);

        when(searchRepo.findByCourseContentIsNotNull()).thenReturn(Collections.singletonList(search));

        assertEquals(1, searchService.getCourseContent().size());
    }
}

