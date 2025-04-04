package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.MockTestInterview;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.service.EnrollmentService;
import fusionIQ.AI.V2.fusionIq.service.MockTestInterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class MockTestInterviewController {

    @Autowired
    private MockTestInterviewService service;

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/saveMock")
    public ResponseEntity<MockTestInterview> createMockTestInterview(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "relatedCourseId",required = false) Long relatedCourseId,
            @RequestParam("testType") MockTestInterview.TestType testType,
            @RequestParam("fee") BigDecimal fee,
            @RequestParam("freeAttempts") Integer freeAttempts,
            @RequestParam("image") MultipartFile image) {

        MockTestInterview mockTestInterview = new MockTestInterview();
        mockTestInterview.setTitle(title);
        mockTestInterview.setDescription(description);
        if (relatedCourseId != null) {
            mockTestInterview.setRelatedCourseId(new Course(relatedCourseId));
        } else {
            mockTestInterview.setRelatedCourseId(null);
        }
        mockTestInterview.setTestType(testType);
        mockTestInterview.setFee(fee);
        mockTestInterview.setFreeAttempts(freeAttempts);

        try {
            mockTestInterview.setImage(image.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        MockTestInterview created = service.createMockTestInterview(mockTestInterview);
        return ResponseEntity.ok(created);
    }



    @PostMapping("/saveNewMock")
    public ResponseEntity<MockTestInterview> createMockTestInterviewByCourse(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "relatedCourseId", required = false) Long relatedCourseId,
            @RequestParam("teacherId") Long teacherId,
            @RequestParam("testType") MockTestInterview.TestType testType,
            @RequestParam("fee") BigDecimal fee,
            @RequestParam("freeAttempts") Integer freeAttempts,
            @RequestParam("image") MultipartFile image) {

        MockTestInterview mockTestInterview = new MockTestInterview();
        mockTestInterview.setTitle(title);
        mockTestInterview.setDescription(description);
        if (relatedCourseId != null) {
            mockTestInterview.setRelatedCourseId(new Course(relatedCourseId));
        } else {
            mockTestInterview.setRelatedCourseId(null);
        }
        mockTestInterview.setTestType(testType);
        mockTestInterview.setFee(fee);
        mockTestInterview.setFreeAttempts(freeAttempts);

        try {
            mockTestInterview.setImage(image.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Pass teacherId to the service method for validation
        try {
            MockTestInterview created = service.createMockTestInterviewByCourse(mockTestInterview, teacherId);
            return ResponseEntity.ok(created);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @GetMapping("/getMock/{id}")
    public ResponseEntity<MockTestInterview> getMockTestInterviewById(@PathVariable Long id) {
        Optional<MockTestInterview> mockTestInterview = service.getMockTestInterviewById(id);
        return mockTestInterview.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getAllMockTests")
    public ResponseEntity<List<MockTestInterview>> getAllMockTests() {
        List<MockTestInterview> mockTests = service.getAllMockTestInterviews();
        return ResponseEntity.ok(mockTests);
    }



//    @GetMapping("/getAllMockTests")
//    public ResponseEntity<List<MockTestInterview>> getAllMockTestInterviews() {
//        List<MockTestInterview> mockTestInterviews = service.getAllMockTestInterviews();
//        return ResponseEntity.ok(mockTestInterviews);
//    }

//    @GetMapping("/getAllMockTests")
//    public ResponseEntity<List<MockTestInterview>> getAllMockTests() {
//        List<MockTestInterview> mockTests = service.getAllMockTestInterviews();
//        return ResponseEntity.ok(mockTests);
//    }


//    @GetMapping("/getAllMockTests")
//    public ResponseEntity<Page<MockTestInterview>> getAllMockTests(Pageable pageable) {
//        Page<MockTestInterview> mockTests = service.getAllMockTestInterviews(pageable);
//        return ResponseEntity.ok(mockTests);
//    }


    @GetMapping("/getMockStatus")
    public Map<String, Object> getStatus(@RequestParam Long userId, @RequestParam Long courseId, @RequestParam Long mockId) {
        Map<String, Object> response = new HashMap<>();

        // Enrollment status
        Map<String, Boolean> enrollmentStatus = enrollmentService.getEnrollmentStatus(userId, courseId);
        response.put("enrollmentStatus", enrollmentStatus);

        // Free trials check
        boolean freeTrialsExceeded = service.hasExceededFreeTrials(userId, mockId);
        response.put("freeTrialsExceeded", freeTrialsExceeded);

        // Fetch mock test interview details if needed
        // MockTestInterview mockTestInterview = mockTestInterviewService.getMockTestInterviewDetails(mockId);
        // response.put("mockTestInterviewDetails", mockTestInterview);

        return response;
    }


    @GetMapping("/getDetails/{mockId}")
    public ResponseEntity<?> getByMockId(@PathVariable Long mockId) {
        Object result = service.getByMockId(mockId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllSelectedFields")
    public ResponseEntity<List<Map<String, Object>>> getAllMockTestsWithSelectedFields() {
        List<Map<String, Object>> mockTests = service.getAllMockTestInterviewsWithSelectedFields();
        return ResponseEntity.ok(mockTests);
    }

    @GetMapping("/getAllAvailableSelectedFields")
    public ResponseEntity<List<Map<String, Object>>> getAllAvailableMockTestsWithSelectedFields() {
        List<Map<String, Object>> mockTests = service.getAllAvailableMockTestInterviewsWithAvailableSlots();
        return ResponseEntity.ok(mockTests);
    }
    @GetMapping("/getmockbyteacher/{teacherId}")
    public ResponseEntity<List<Map<String, Object>>> getMocksByTeacherId(@PathVariable Long teacherId) {
        List<Map<String, Object>> mocks = service.getMocksByTeacherId(teacherId);
        return ResponseEntity.ok(mocks);
    }
    @GetMapping("/getMockDetails/{teacherId}")
    public ResponseEntity<List<Map<String, Object>>> getMockDetails(@PathVariable Long teacherId) {
        List<Map<String, Object>> mockDetails = service.getMockDetails(teacherId);
        return ResponseEntity.ok(mockDetails);
    }
    @DeleteMapping("/DeleteMock/{mockId}/{teacherId}")
    public ResponseEntity<String> deleteMock(
            @PathVariable Long mockId,
            @PathVariable Long teacherId) {

        String response = service.deleteMock(mockId, teacherId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/EditMock/{mockId}/{teacherId}")
    public ResponseEntity<String> editMock(
            @PathVariable Long mockId,
            @PathVariable Long teacherId,
            @RequestBody MockTestInterview updatedMock) {

        String response = service.editMock(mockId, teacherId, updatedMock);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getMockDetailsByMockId/{mockId}")
    public ResponseEntity<List<Map<String, Object>>> getMockDetailsByMockId(@PathVariable Long mockId) {
        List<Map<String, Object>> mockDetails = service.getMockDetailsByMockId(mockId);
        return ResponseEntity.ok(mockDetails);
    }

}
