package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SlotService {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private BookedMockTestInterviewRepository bookedMockTestInterviewRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MockTestInterviewRepository mockTestInterviewRepo;

    @Autowired
    private TrainingRoomRepo trainingRoomRepo;

    public List<Slot> findAllByInterviewId(Long interviewId) {
        return slotRepository.findByMockTestInterviewId(interviewId);
    }

    public List<Slot> getAllSlots() {
        return slotRepository.findAll();
    }


    public List<Map<String, Object>> getAvailableSlotDetailsByMockId(Long mockId) {
        List<Object[]> slotDetailsList = slotRepository.findAvailableSlotDetailsByMockId(mockId);

        // Convert Object[] into a structured map
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] slotDetails : slotDetailsList) {
            Map<String, Object> slotMap = new HashMap<>();
            slotMap.put("slotId", slotDetails[0]);
            slotMap.put("mockId", slotDetails[1]);
            slotMap.put("courseId", slotDetails[2]);
            slotMap.put("slotName", slotDetails[3]);
            slotMap.put("slotTime", slotDetails[4]);
            slotMap.put("endTime", slotDetails[5]);
            slotMap.put("testType", slotDetails[6]);
            slotMap.put("courseTitle", slotDetails[7]);
            result.add(slotMap);
        }

        return result;
    }
    @Transactional
    public Map<String, Object> bookSlot(BookingRequest request) {
        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid slot ID"));

        if (slot.getBooked()) {
            throw new IllegalArgumentException("Slot already booked");
        }

        slot.setBooked(true);
        slotRepository.save(slot);

        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));

        BookedMockTestInterview bookedMockTestInterview = new BookedMockTestInterview();
        bookedMockTestInterview.setSlot(slot);
        bookedMockTestInterview.setUser(user);
        bookedMockTestInterview.setBookingTime(LocalDateTime.now());

        bookedMockTestInterviewRepository.save(bookedMockTestInterview);

        // Return a custom response with minimal fields
        Map<String, Object> response = new HashMap<>();
        response.put("bookingId", bookedMockTestInterview.getId());
        response.put("slotId", slot.getId());
        response.put("userId", user.getId());
        response.put("bookingTime", bookedMockTestInterview.getBookingTime());

        return response;
    }

    public List<BookedMockTestInterview> getBookedSlots(Long mockTestInterviewId) {
        return bookedMockTestInterviewRepository.findBySlotMockTestInterviewId(mockTestInterviewId);
    }


    public List<BookedMockTestInterview> getBookedSlotsByUserId(Long userId) {
        return bookedMockTestInterviewRepository.findBookedSlotsByUserId(userId);
    }

    public List<BookedMockTestInterview> getBookedSlotByUserId(Long userId) {
        return bookedMockTestInterviewRepository.findByUserId(userId);

    }


    public List<Map<String, Object>> getBookedSlotsWithDetailsByUserId(Long userId) {
        List<BookedMockTestInterview> bookedSlots = bookedMockTestInterviewRepository.findBookedSlotsWithDetailsByUserId(userId);

        return bookedSlots.stream().map(bookedSlot -> {
            Map<String, Object> result = new HashMap<>();
            Slot slot = bookedSlot.getSlot();
            result.put("slotDate", slot.getSlotTime());
            result.put("endDate", slot.getEndTime());
            result.put("slot_id", slot.getId());

            MockTestInterview mockTestInterview = slot.getMockTestInterview();
            if (mockTestInterview != null) {
                result.put("mock_id", mockTestInterview.getId());

                // Check if project is available
                if (mockTestInterview.getProject() != null) {
                    result.put("type", "Project");
                    result.put("title", mockTestInterview.getProject().getProjectTitle());
                    result.put("projectId", mockTestInterview.getProject().getId());
                }
                // Check if assignment is available
                else if (mockTestInterview.getAssignment() != null) {
                    result.put("type", "Assignment");
                    result.put("title", mockTestInterview.getAssignment().getAssignmentTitle());
                    result.put("assignmentId", mockTestInterview.getAssignment().getId());
                }

                // Fetch TrainingRoom details if available
                if (mockTestInterview.getTrainingRoom() != null) {
                    result.put("type", "Interview");
                    result.put("trainingRoomName", mockTestInterview.getTrainingRoom().getName());
                    result.put("conferenceUrl", mockTestInterview.getTrainingRoom().getConferenceUrl());
                    result.put("trainingRoomId", mockTestInterview.getTrainingRoom().getId());
                }
            }

            return result;
        }).collect(Collectors.toList());
    }



    public Slot createSlotByMockId(Long mockId, Slot slot) {
        Optional<MockTestInterview> mockTestInterviewOpt = mockTestInterviewRepo.findById(mockId);

        if (mockTestInterviewOpt.isPresent()) {
            MockTestInterview mockTestInterview = mockTestInterviewOpt.get();
            slot.setMockTestInterview(mockTestInterview);  // Setting the mockTestInterview properly
            return slotRepository.save(slot);
        } else {
            throw new IllegalArgumentException("MockTestInterview with id " + mockId + " not found");
        }
    }

}
