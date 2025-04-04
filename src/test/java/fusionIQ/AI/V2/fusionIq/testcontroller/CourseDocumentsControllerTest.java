//package fusionIQ.AI.V2.fusionIq.testcontroller;
//
//import fusionIQ.AI.V2.fusionIq.controller.CourseDocumentsController;
//import fusionIQ.AI.V2.fusionIq.data.Course;
//import fusionIQ.AI.V2.fusionIq.data.CourseDocuments;
//import fusionIQ.AI.V2.fusionIq.service.CourseDocumentsService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class CourseDocumentsControllerTest {
//
//    @Mock
//    private CourseDocumentsService courseDocumentsService;
//
//    @InjectMocks
//    private CourseDocumentsController courseDocumentsController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testUploadDocument() throws IOException {
//        Long courseId = 1L;
//        MultipartFile file = mock(MultipartFile.class);
//        byte[] documentBytes = new byte[]{1, 2, 3};
//
//        when(file.getBytes()).thenReturn(documentBytes);
//
//        Course course = new Course();
//        CourseDocuments courseDocument = new CourseDocuments();
//        courseDocument.setCourseDocument(documentBytes);
//        courseDocument.setCourse(course);
//
//        when(courseDocumentsService.getCourseById(courseId)).thenReturn(course);
//        when(courseDocumentsService.saveCourseDocument(any(CourseDocuments.class))).thenReturn(courseDocument);
//
//        CourseDocuments result = courseDocumentsController.uploadDocument(courseId, file);
//
//        assertEquals(documentBytes, result.getCourseDocument());
//        assertEquals(course, result.getCourse());
//        verify(courseDocumentsService, times(1)).getCourseById(courseId);
//        verify(courseDocumentsService, times(1)).saveCourseDocument(any(CourseDocuments.class));
//    }
//
//    @Test
//    void testGetDocumentsByCourseId() {
//        Long courseId = 1L;
//        CourseDocuments doc1 = new CourseDocuments();
//        CourseDocuments doc2 = new CourseDocuments();
//        List<CourseDocuments> documents = Arrays.asList(doc1, doc2);
//
//        when(courseDocumentsService.getDocumentsByCourseId(courseId)).thenReturn(documents);
//
//        List<CourseDocuments> result = courseDocumentsController.getDocumentsByCourseId(courseId);
//
//        assertEquals(2, result.size());
//        verify(courseDocumentsService, times(1)).getDocumentsByCourseId(courseId);
//    }
//
//    @Test
//    void testDeleteDocument() {
//        Long documentId = 1L;
//
//        doNothing().when(courseDocumentsService).deleteDocumentById(documentId);
//
//        ResponseEntity<Void> response = courseDocumentsController.deleteDocument(documentId);
//
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(courseDocumentsService, times(1)).deleteDocumentById(documentId);
//    }
//}
