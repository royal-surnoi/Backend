package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.CourseDocuments;
import fusionIQ.AI.V2.fusionIq.data.Enrollment;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.CourseDocumentsRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.EnrollmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseDocumentsService {

    @Autowired
    private CourseDocumentsRepo courseDocumentsRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private EnrollmentRepo enrollmentRepo;

    public Course getCourseById(Long courseId) {
        return courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    public CourseDocuments saveCourseDocument(CourseDocuments courseDocument) {
        return courseDocumentsRepo.save(courseDocument);
    }

    public List<CourseDocuments> getDocumentsByCourseId(Long courseId) {
        return courseDocumentsRepo.findByCourseId(courseId);
    }

    public void deleteDocumentById(Long id) {
        courseDocumentsRepo.deleteById(id);
    }


    public List<Map<String, Object>> getUserEnrolledCourseDocuments(Long userId) {
        // Fetch enrollments where the user is enrolled
        List<Enrollment> enrollments = enrollmentRepo.findByUserId(userId);

        if (enrollments.isEmpty()) {
            return Collections.emptyList();
        }

        // Extract enrolled course IDs
        List<Long> enrolledCourseIds = enrollments.stream()
                .map(enrollment -> enrollment.getCourse().getId())
                .collect(Collectors.toList());

        // Fetch course documents only for enrolled courses
        List<CourseDocuments> courseDocuments = courseDocumentsRepo.findByCourseIdIn(enrolledCourseIds);

        // Map the required data
        return courseDocuments.stream().map(doc -> {
            Map<String, Object> documentMap = new HashMap<>();
            documentMap.put("id", doc.getId());
            documentMap.put("courseDocument", doc.getCourseDocument());

            // Extract course details safely
            Course course = doc.getCourse();
            if (course != null) {
                documentMap.put("courseTitle", course.getCourseTitle());
                documentMap.put("courseImage", course.getCourseImage());
            } else {
                documentMap.put("courseTitle", null);
                documentMap.put("courseImage", null);
            }

            return documentMap;
        }).collect(Collectors.toList());
    }
}
