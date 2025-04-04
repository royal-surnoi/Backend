package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.MockTestInterview;
import fusionIQ.AI.V2.fusionIq.data.Slot;
import fusionIQ.AI.V2.fusionIq.repository.MockTestInterviewRepository;
import fusionIQ.AI.V2.fusionIq.repository.SlotRepository;
import fusionIQ.AI.V2.fusionIq.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/slots")
public class SlotController {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private MockTestInterviewRepository mockTestInterviewRepository;

    @Autowired
    private SlotService slotService;

    @PostMapping("/saveSlot")
    public Slot createSlot(@RequestBody Slot slot) {
        Optional<MockTestInterview> interview = mockTestInterviewRepository.findById(slot.getMockTestInterview().getId());
        if (!interview.isPresent()) {
            throw new IllegalArgumentException("Invalid MockTestInterview ID");
        }

        slot.setMockTestInterview(interview.get());
        return slotRepository.save(slot);
    }

    @PostMapping("/saveSlots/{mockId}")
    public ResponseEntity<Slot> createSlotByMockId(@PathVariable Long mockId, @RequestBody Slot slot) {
        Slot createdSlot = slotService.createSlotByMockId(mockId, slot);
        return ResponseEntity.ok(createdSlot);
    }

    @GetMapping("/{interviewId}/slots")
    public ResponseEntity<List<Slot>> getAllSlotsByInterviewId(@PathVariable Long interviewId) {
        List<Slot> slots = slotService.findAllByInterviewId(interviewId);
        if (slots.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/available/{mockId}")
    public ResponseEntity<List<Map<String, Object>>> getAvailableSlotDetailsByMockId(@PathVariable Long mockId) {
        List<Map<String, Object>> slotDetails = slotService.getAvailableSlotDetailsByMockId(mockId);
        return ResponseEntity.ok(slotDetails);
    }


}
