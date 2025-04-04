package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Search;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    @Mock
    private User user; // Mock the User object if needed

    private Search search;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Create a new instance of Search before each test
        search = new Search();
    }

    @Test
    void testConstructorAndGetters() {
        // Test the constructor
        LocalDateTime now = LocalDateTime.now();
        Search searchWithValues = new Search(1L, "searchContent", "courseContent", "jobContent", now, user);

        // Assertions
        assertNotNull(searchWithValues.getId());
        assertEquals("searchContent", searchWithValues.getSearchContent());
        assertEquals("courseContent", searchWithValues.getCourseContent());
        assertEquals("jobContent", searchWithValues.getJobContent());
        assertEquals(now, searchWithValues.getSearchedAt());
        assertEquals(user, searchWithValues.getUser());
    }

    @Test
    void testSettersAndGetters() {
        // Set values using setters
        search.setId(1L);
        search.setSearchContent("newSearchContent");
        search.setCourseContent("newCourseContent");
        search.setJobContent("newJobContent");
        search.setSearchedAt(LocalDateTime.now().minusDays(1));
        search.setUser(user);

        // Assertions
        assertEquals(1L, search.getId());
        assertEquals("newSearchContent", search.getSearchContent());
        assertEquals("newCourseContent", search.getCourseContent());
        assertEquals("newJobContent", search.getJobContent());
        assertNotNull(search.getSearchedAt());
        assertEquals(user, search.getUser());
    }

    @Test
    void testDefaultConstructor() {
        // Create Search object using the default constructor
        Search defaultSearch = new Search();

        // Check that the searchedAt field is set to the current time
        assertNotNull(defaultSearch.getSearchedAt());
    }

    @Test
    void testToString() {
        // Set values to the search object
        search.setId(2L);
        search.setSearchContent("sampleSearch");
        search.setCourseContent("sampleCourse");
        search.setJobContent("sampleJob");
        search.setSearchedAt(LocalDateTime.now());
        search.setUser(user);

        // Check the toString method
        String expectedString = "Search{id=2, searchContent='sampleSearch', courseContent='sampleCourse', jobContent='sampleJob', searchedAt=" + search.getSearchedAt() + ", user=" + user + "}";
        assertEquals(expectedString, search.toString());
    }
}
