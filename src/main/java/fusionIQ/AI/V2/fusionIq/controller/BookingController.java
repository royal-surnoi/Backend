package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.BookedMockTestInterview;
import fusionIQ.AI.V2.fusionIq.data.BookingRequest;
import fusionIQ.AI.V2.fusionIq.data.MockTestInterview;
import fusionIQ.AI.V2.fusionIq.data.Slot;
import fusionIQ.AI.V2.fusionIq.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private SlotService slotService;

    @PostMapping("/book")
    public Map<String, Object> bookSlot(@RequestBody BookingRequest request) {
        return slotService.bookSlot(request);
    }

    @GetMapping("/booked/{mockTestInterviewId}")
    public List<BookedMockTestInterview> getBookedSlots(@PathVariable Long mockTestInterviewId) {
        return slotService.getBookedSlots(mockTestInterviewId);
    }

//    @GetMapping("/User/{userId}")
//    public List<BookedMockTestInterview> getBookedSlotsByStudentId(@PathVariable Long userId) {
//        return slotService.getBookedSlotsByUserId(userId);
//    }

//    @GetMapping("/User/{userId}")
//    public List<Map<String, Object>> getBookedSlotsByStudentId(@PathVariable Long userId) {
//        List<BookedMockTestInterview> bookedSlots = slotService.getBookedSlotsByUserId(userId);
//
//        return bookedSlots.stream().map(bookedSlot -> {
//            Map<String, Object> result = new HashMap<>();
//            result.put("id", bookedSlot.getId());
//            result.put("bookingTime", bookedSlot.getBookingTime());
//            result.put("score", bookedSlot.getScore());
//            result.put("attemptDate", bookedSlot.getAttemptDate());
//
//            Slot slot = bookedSlot.getSlot();
//            if (slot != null) {
//                result.put("slotName", slot.getSlotName());
//                result.put("slotTime", slot.getSlotTime());
//                result.put("endTime", slot.getEndTime());
//                result.put("mockId",slot.getMockTestInterview().getId());
//
//                MockTestInterview mockTest = slot.getMockTestInterview();
//                if (mockTest != null) {
//                    result.put("testType", mockTest.getTestType());
//                    result.put("mockTestTitle", mockTest.getTitle());
//                    result.put("mockTestDescription", mockTest.getDescription());
//
//                }
//            }
//
//            return result;
//        }).collect(Collectors.toList());
//    }

//    @GetMapping("/User/{userId}")
//    public List<Map<String, Object>> getBookedSlotsByStudentId(@PathVariable Long userId) {
//        List<BookedMockTestInterview> bookedSlots = slotService.getBookedSlotsByUserId(userId);
//
//        return bookedSlots.stream().map(bookedSlot -> {
//            Map<String, Object> result = new HashMap<>();
//            result.put("id", bookedSlot.getId());
//            result.put("bookingTime", bookedSlot.getBookingTime());
//            result.put("score", bookedSlot.getScore());
//            result.put("attemptDate", bookedSlot.getAttemptDate());
//
//            Slot slot = bookedSlot.getSlot();
//            if (slot != null) {
//                result.put("slotName", slot.getSlotName());
//                result.put("slotTime", slot.getSlotTime());
//                result.put("endTime", slot.getEndTime());
//                result.put("mockId", slot.getMockTestInterview().getId());
//
//                if (slot.getMockTestInterview() != null) {
//                    result.put("testType", slot.getMockTestInterview().getTestType());
//                    result.put("mockTestTitle", slot.getMockTestInterview().getTitle());
//                    result.put("mockTestDescription", slot.getMockTestInterview().getDescription());
//                }
//            }
//
//            return result;
//        }).collect(Collectors.toList());
//    }

    @GetMapping("/User/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getBookedSlotsWithDetailsByUserId(@PathVariable Long userId) {
        List<Map<String, Object>> bookedSlots = slotService.getBookedSlotsWithDetailsByUserId(userId);
        return ResponseEntity.ok(bookedSlots);
    }

    @GetMapping("/getBookedDetails/{userId}")
    public ResponseEntity<List<BookedMockTestInterview>> getBookedSlotsByUserId(@PathVariable Long userId) {
        List<BookedMockTestInterview> bookedSlots = slotService.getBookedSlotByUserId(userId);
        if (bookedSlots.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookedSlots);
    }
}
