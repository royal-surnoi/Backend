package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.AIMockTest;
import fusionIQ.AI.V2.fusionIq.repository.AIMockTestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIMockTestService {

    private final AIMockTestRepo aiMockTestRepo;

    @Autowired
    public AIMockTestService(AIMockTestRepo aiMockTestRepository) {
        this.aiMockTestRepo = aiMockTestRepository;
    }

    // Save or update AIMockTest
    public AIMockTest saveAIMockTest(AIMockTest aiMockTest) {
        return aiMockTestRepo.save(aiMockTest);
    }

    // Get all AIMockTests
    public List<AIMockTest> getAllAIMockTests() {
        return aiMockTestRepo.findAll();
    }

    // Get AIMockTest by ID
    public AIMockTest getAIMockTestById(Long id) {
        return aiMockTestRepo.findById(id).orElse(null);
    }

    // Delete AIMockTest by ID
    public void deleteAIMockTest(Long id) {
        aiMockTestRepo.deleteById(id);
    }
}