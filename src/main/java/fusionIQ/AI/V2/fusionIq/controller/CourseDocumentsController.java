package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.CourseDocuments;
import fusionIQ.AI.V2.fusionIq.service.CourseDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CourseDocumentsController {

    @Autowired
    private CourseDocumentsService courseDocumentsService;

    @PostMapping("/{courseId}/documents")
    public List<CourseDocuments> uploadDocuments(
            @PathVariable Long courseId,
            @RequestParam("files") MultipartFile[] files) throws IOException {

        List<CourseDocuments> savedDocuments = new ArrayList<>();
        Course course = courseDocumentsService.getCourseById(courseId);

        for (MultipartFile file : files) {
            byte[] documentBytes = file.getBytes();
            CourseDocuments courseDocument = new CourseDocuments();
            courseDocument.setCourseDocument(documentBytes);
            courseDocument.setCourse(course);
            savedDocuments.add(courseDocumentsService.saveCourseDocument(courseDocument));
        }

        return savedDocuments;
    }

    @GetMapping("/{courseId}/documents")
    public List<CourseDocuments> getDocumentsByCourseId(@PathVariable Long courseId) {
        return courseDocumentsService.getDocumentsByCourseId(courseId);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        courseDocumentsService.deleteDocumentById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/enrolled-documents")
    public ResponseEntity<List<Map<String, Object>>> getUserEnrolledCourseDocuments(@PathVariable Long userId) {
        List<Map<String, Object>> documents = courseDocumentsService.getUserEnrolledCourseDocuments(userId);

        if (documents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(documents);
    }
}