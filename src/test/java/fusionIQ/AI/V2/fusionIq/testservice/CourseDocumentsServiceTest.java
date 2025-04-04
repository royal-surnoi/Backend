package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.CourseDocuments;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.CourseDocumentsRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.service.CourseDocumentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseDocumentsServiceTest {

    @Mock
    private CourseDocumentsRepo courseDocumentsRepo;

    @Mock
    private CourseRepo courseRepo;

    @InjectMocks
    private CourseDocumentsService courseDocumentsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCourseById() {
        Course course = new Course();
        course.setId(1L);

        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(course));

        Course result = courseDocumentsService.getCourseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(courseRepo, times(1)).findById(1L);
    }

    @Test
    void testGetCourseById_CourseNotFound() {
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseDocumentsService.getCourseById(1L));
        verify(courseRepo, times(1)).findById(1L);
    }

    @Test
    void testSaveCourseDocument() {
        CourseDocuments courseDocument = new CourseDocuments();
        courseDocument.setId(1L);

        when(courseDocumentsRepo.save(any(CourseDocuments.class))).thenReturn(courseDocument);

        CourseDocuments result = courseDocumentsService.saveCourseDocument(courseDocument);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(courseDocumentsRepo, times(1)).save(courseDocument);
    }

    @Test
    void testGetDocumentsByCourseId() {
        CourseDocuments courseDocument1 = new CourseDocuments();
        courseDocument1.setId(1L);
        CourseDocuments courseDocument2 = new CourseDocuments();
        courseDocument2.setId(2L);

        when(courseDocumentsRepo.findByCourseId(anyLong())).thenReturn(List.of(courseDocument1, courseDocument2));

        List<CourseDocuments> result = courseDocumentsService.getDocumentsByCourseId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(courseDocumentsRepo, times(1)).findByCourseId(1L);
    }

    @Test
    void testDeleteDocumentById() {
        doNothing().when(courseDocumentsRepo).deleteById(anyLong());

        courseDocumentsService.deleteDocumentById(1L);

        verify(courseDocumentsRepo, times(1)).deleteById(1L);
    }
}

