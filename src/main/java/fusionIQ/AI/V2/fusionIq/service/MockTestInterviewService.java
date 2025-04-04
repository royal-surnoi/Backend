package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MockTestInterviewService {

    @Autowired
    private MockTestInterviewRepository repository;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MentorRepo mentorRepo;

    @Autowired
    private AssignmentRepo assignmentRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private BookedMockTestInterviewRepository bookedMockTestInterviewRepo;



    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private TrainingRoomRepo trainingRoomRepo;

    @Autowired
    private CourseRepo courseRepository;

    public MockTestInterview createMockTestInterview(MockTestInterview mockTestInterview) {
        return repository.save(mockTestInterview);
    }


    public MockTestInterview createMockTestInterviewByCourse(MockTestInterview mockTestInterview, Long teacherId) {
        // Retrieve the teacher entity from the database
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new UserNotFoundException("Teacher with ID " + teacherId + " not found."));

        // Set the teacher for the mockTestInterview
        mockTestInterview.setTeacher(teacher);

        // Save the mock test interview with the teacher set
        return repository.save(mockTestInterview);
    }
    public Optional<MockTestInterview> getMockTestInterviewById(Long id) {
        Optional<MockTestInterview> mockTestInterviewOpt = repository.findById(id);

        if (mockTestInterviewOpt.isPresent()) {
            MockTestInterview mockTestInterview = mockTestInterviewOpt.get();

            // Fetch Project or Assignment based on TestType
            if (mockTestInterview.getTestType() == MockTestInterview.TestType.PROJECT) {
                // Initialize Project (this assumes the project relationship is eagerly fetched)
                mockTestInterview.getProject();
            } else if (mockTestInterview.getTestType() == MockTestInterview.TestType.ASSIGNMENT) {
                // Initialize Assignment (this assumes the assignment relationship is eagerly fetched)
                mockTestInterview.getAssignment();
            }

            return Optional.of(mockTestInterview);
        }

        return Optional.empty();
    }



    public List<MockTestInterview> getAllMockTestInterviews() {
        return repository.findAll();
    }



    public boolean hasExceededFreeTrials(Long studentId, Long mockId) {
        MockTestInterview mockTestInterview = repository.findById(mockId)
                .orElseThrow(() -> new RuntimeException("MockTestInterview not found"));

        Long bookedCount = repository.countByStudentIdAndMockTestInterviewId(studentId, mockId);
        return bookedCount >= mockTestInterview.getFreeAttempts();
    }

    public MockTestInterview getMockTestInterviewDetails(Long mockId) {
        return repository.findById(mockId)
                .orElseThrow(() -> new RuntimeException("MockTestInterview not found"));
    }

    public MockTestInterview findById(Long id) {
        Optional<MockTestInterview> mockTestInterview = repository.findById(id);
        return mockTestInterview.orElse(null);
    }


    public Map<String, Object> getByMockId(Long mockId) {
        Map<String, Object> result = new HashMap<>();

        // Fetch the slot associated with the given mockId
        List<Slot> slots = slotRepository.findByMockTestInterviewId(mockId);

        if (slots.isEmpty()) {
            throw new ResourceNotFoundException("No slots found for mock_id: " + mockId);
        }

        Slot selectedSlot = slots.get(0); // Choose the slot you need (e.g., the first one, or based on specific logic)
        MockTestInterview mockTestInterview = selectedSlot.getMockTestInterview();

        result.put("slotId", selectedSlot.getId());
        result.put("slotName", selectedSlot.getSlotName());
        result.put("slotTime", selectedSlot.getSlotTime());
        result.put("endTime", selectedSlot.getEndTime());

        // Fetch all projects associated with this mockTestInterview
        List<Project> projects = projectRepo.findAllByMockTestInterviewId(mockId);
        if (!projects.isEmpty()) {
            result.put("testType", "PROJECT");
            result.put("projects", projects.stream().map(project -> {
                Map<String, Object> projectDetails = new HashMap<>();
                projectDetails.put("id", project.getId());
                projectDetails.put("title", project.getProjectTitle());
                projectDetails.put("description", project.getProjectDescription());
                projectDetails.put("document", project.getProjectDocument());
                projectDetails.put("mock_id",project.getMockTestInterview().getId());
                return projectDetails;
            }).collect(Collectors.toList()));
        }

        // Fetch all assignments associated with this mockTestInterview
        List<Assignment> assignments = assignmentRepo.findAllByMockTestInterviewId(mockId);
        if (!assignments.isEmpty()) {
            result.put("testType", "ASSIGNMENT");
            result.put("assignments", assignments.stream().map(assignment -> {
                Map<String, Object> assignmentDetails = new HashMap<>();
                assignmentDetails.put("id", assignment.getId());
                assignmentDetails.put("title", assignment.getAssignmentTitle());
                assignmentDetails.put("description", assignment.getAssignmentDescription());
                assignmentDetails.put("document", assignment.getAssignmentDocument());
                assignmentDetails.put("mock_id",assignment.getMockTestInterview().getId());
                return assignmentDetails;
            }).collect(Collectors.toList()));
        }

        // Fetch interview details if the test type is "INTERVIEW"
        List<TrainingRoom> trainingRooms = trainingRoomRepo.findAllByMockTestInterviewId(mockId);
        if (!trainingRooms.isEmpty()) {
            result.put("testType", "INTERVIEW");
            result.put("interviews", trainingRooms.stream().map(trainingRoom -> {
                Map<String, Object> interviewDetails = new HashMap<>();
                interviewDetails.put("trainingRoomId", trainingRoom.getId());
                interviewDetails.put("conferenceUrl", trainingRoom.getConferenceUrl());
                interviewDetails.put("trainingRoomName", trainingRoom.getName());
                interviewDetails.put("mock_id",trainingRoom.getMockTestInterview().getId());
                return interviewDetails;
            }).collect(Collectors.toList()));
        }

        // If neither projects, assignments, nor interviews were found, throw an exception
        if (projects.isEmpty() && assignments.isEmpty() && trainingRooms.isEmpty()) {
            throw new ResourceNotFoundException("No assignment, project, or interview found for mock_id: " + mockId);
        }

        return result;
    }


    public List<Map<String, Object>> getAllMockTestInterviewsWithSelectedFields() {
        List<Object[]> results = repository.findSelectedFields();
        List<Map<String, Object>> mockTests = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> rowMap = new HashMap<>();
            rowMap.put("title", row[0]);
            rowMap.put("description", row[1]);
            rowMap.put("testType", row[2]);
            rowMap.put("fee", row[3]);
            rowMap.put("freeAttempts", row[4]);
            rowMap.put("image", row[5]);
            rowMap.put("courseId", row[6]); // Adding courseId to the map
            rowMap.put("mock_id",row[7]);
            mockTests.add(rowMap);

        }

        return mockTests;
    }

    public List<Map<String, Object>> getAllAvailableMockTestInterviewsWithAvailableSlots() {
        List<Object[]> results = repository.findSelectedFields();
        List<Map<String, Object>> mockTests = new ArrayList<>();

        for (Object[] row : results) {
            Long mockId = ((Number) row[7]).longValue(); // Proper conversion
            Course course = courseRepository.findById(((Number) row[3]).longValue()).orElse(null);
            boolean hasAvailableSlot = slotRepository.existsByMockTestInterviewIdAndBooked(mockId, false);

            if (hasAvailableSlot) {
                Map<String, Object> rowMap = new HashMap<>();
                rowMap.put("title", row[0]);
                rowMap.put("description", row[1]);
                rowMap.put("testType", row[2]);
                rowMap.put("fee", row[3]);
                rowMap.put("freeAttempts", row[4]);
                rowMap.put("image", row[5]);
                rowMap.put("courseId", row[6]);
                rowMap.put("mock_id", mockId);
                rowMap.put("Course_Title", course != null ? course.getCourseTitle() : "Unknown Course");
                mockTests.add(rowMap);
            }
        }
        return mockTests;
    }

    public List<Map<String, Object>> getMocksByTeacherId(Long teacherId) {
        List<Object[]> results = repository.findSpecificFieldsByTeacherId(teacherId);
        List<Map<String, Object>> mocks = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> mock = new HashMap<>();
            mock.put("mockId", row[0]);
            mock.put("title", row[1]);
            mock.put("description", row[2]);
            mock.put("relatedCourseId", row[3]);
            mock.put("teacherId", row[4]);
            mocks.add(mock);
        }
        return mocks;
    }
    public List<Map<String, Object>> getMockDetails(Long teacherId) {
        List<Object[]> results = repository.findMockDetailsByTeacherId(teacherId);
        List<Map<String, Object>> mockDetails = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> mock = new LinkedHashMap<>();
            mock.put("id", row[0]);                         // ID
            mock.put("createdAt", row[1]);                  // Created Date
            mock.put("title", row[2]);                      // Title
            mock.put("description", row[3]);                // 🔹 Added Description
            mock.put("testType", ((MockTestInterview.TestType) row[4]).name()); // Convert Enum to String
            mock.put("freeAttempts", row[5]);               // Free Attempts
            mock.put("mockId", row[6]);                     // Mock ID
            mockDetails.add(mock);
        }
        return mockDetails;
    }
    @Transactional
    public String deleteMock(Long mockId, Long teacherId) {
        Optional<MockTestInterview> mockOptional = repository.findById(mockId);

        if (!mockOptional.isPresent()) {
            return "Mock test not found.";
        }

        MockTestInterview mockTest = mockOptional.get();

        // Validate teacher ID
        if (!Long.valueOf(mockTest.getTeacher().getId()).equals(teacherId)) {
            return "Unauthorized: Teacher ID does not match.";
        }

        // 1️⃣ Delete related slots before deleting the mock test
        slotRepository.deleteByMockTestInterview_Id(mockId);

        // 2️⃣ Delete related TrainingRoom records
        trainingRoomRepo.deleteByMockTestInterview_Id(mockId);

        // 3️⃣ Now delete the mock test
        repository.delete(mockTest);

        return "Mock deleted successfully.";
    }
    // EDIT API: Updates mock interview based on mockId and teacherId
    public String editMock(Long mockId, Long teacherId, MockTestInterview updatedMock) {
        Optional<MockTestInterview> mockOptional = repository.findById(mockId);

        if (!mockOptional.isPresent()) {
            return "Mock test not found.";
        }

        MockTestInterview mockTest = mockOptional.get();

        // Validate teacher ID
        if (!Long.valueOf(mockTest.getTeacher().getId()).equals(teacherId)) {
            return "Unauthorized: Teacher ID does not match.";
        }

        // Update fields only if they are provided (to allow partial updates)
        if (updatedMock.getTitle() != null) {
            mockTest.setTitle(updatedMock.getTitle());
        }
        if (updatedMock.getDescription() != null) {
            mockTest.setDescription(updatedMock.getDescription());
        }
        if (updatedMock.getTestType() != null) {
            mockTest.setTestType(updatedMock.getTestType());
        }
        if (updatedMock.getFreeAttempts() != null) {
            mockTest.setFreeAttempts(updatedMock.getFreeAttempts());
        }

        repository.save(mockTest);
        return "Mock updated successfully.";
    }

    public List<Map<String, Object>> getMockDetailsByMockId(Long mockId) {
        List<Object[]> results = repository.findMockDetailsByMockId(mockId);
        List<Map<String, Object>> mockDetails = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> mock = new LinkedHashMap<>();
            mock.put("id", row[0]);                         // ID
            mock.put("createdAt", row[1]);                  // Created Date
            mock.put("title", row[2]);                      // Title
            mock.put("description", row[3]);                // Description
            mock.put("testType", ((MockTestInterview.TestType) row[4]).name()); // Convert Enum to String
            mock.put("freeAttempts", row[5]);               // Free Attempts
            mock.put("mockId", row[6]);                     // Mock ID
            mockDetails.add(mock);
        }
        return mockDetails;
    }

}