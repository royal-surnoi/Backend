package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.AIMockTest;
import fusionIQ.AI.V2.fusionIq.service.AIMockService;
import fusionIQ.AI.V2.fusionIq.service.AIMockTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aiMockTest")
public class AIMockTestController {

    @Autowired
    private AIMockTestService aiMockTestService;


    @PostMapping("/create")
    public AIMockTest createOrUpdateAIMockTest(@RequestBody AIMockTest aiMockTest) {
        return aiMockTestService.saveAIMockTest(aiMockTest);
    }


    @GetMapping("/all")
    public List<AIMockTest> getAllAIMockTests() {
        return aiMockTestService.getAllAIMockTests();
    }

    @GetMapping("/{id}")
    public AIMockTest getAIMockTestById(@PathVariable Long id) {
        return aiMockTestService.getAIMockTestById(id);
    }


    @DeleteMapping("/{id}")
    public String deleteAIMockTest(@PathVariable Long id) {
        aiMockTestService.deleteAIMockTest(id);
        return "AIMockTest with ID " + id + " deleted successfully.";
    }
}
