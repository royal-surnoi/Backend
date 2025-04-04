package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.AIMock;
import fusionIQ.AI.V2.fusionIq.service.AIMockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/aiMocks")

public class AIMockController {

    @Autowired
    private AIMockService aiMockService;

    // Create a new AI Mock record (excluding mockScore and feedBack)
    @PostMapping("/create")
    public ResponseEntity<AIMock> createMock(@RequestBody AIMock aiMock) {
        AIMock createdMock = aiMockService.createMock(aiMock);
        return ResponseEntity.ok(createdMock);
    }

    @GetMapping("/all")
    public List<AIMock> getAllMocks() {
        return aiMockService.getAllMocks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AIMock> getMockById(@PathVariable Long id) {
        Optional<AIMock> mock = aiMockService.getMockById(id);
        return mock.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update mockScore and feedBack for an existing AI Mock
    @PutMapping("/update/{aiMockId}")
    public ResponseEntity<AIMock> updateMockScoreAndFeedback(
            @PathVariable Long aiMockId,
            @RequestBody AIMock updateDetails) {

        AIMock updatedMock = aiMockService.updateMockScoreAndFeedback(aiMockId, updateDetails);
        return ResponseEntity.ok(updatedMock);
    }
}