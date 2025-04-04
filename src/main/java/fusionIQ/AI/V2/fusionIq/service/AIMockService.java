package fusionIQ.AI.V2.fusionIq.service;
import fusionIQ.AI.V2.fusionIq.data.AIMock;
import fusionIQ.AI.V2.fusionIq.repository.AIMockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AIMockService {

    @Autowired
    private AIMockRepo aiMockRepo;

    public List<AIMock> getAllMocks() {
        return aiMockRepo.findAll();
    }

    public Optional<AIMock> getMockById(Long id) {
        return aiMockRepo.findById(id);
    }

    // Create a new AI Mock record (excluding mockScore and feedBack)
    public AIMock createMock(AIMock aiMock) {
        // Ensure mockScore and feedBack are not set when creating a new record
        aiMock.setMockScore(0); // Default value
        aiMock.setFeedBack(null); // Null to ensure it's not set

        return aiMockRepo.save(aiMock);
    }

    //  Update mockScore and feedBack
    public AIMock updateMockScoreAndFeedback(Long aiMockId, AIMock updateDetails) {
        AIMock aiMock = aiMockRepo.findById(aiMockId)
                .orElseThrow(() -> new RuntimeException("AI Mock not found with id: " + aiMockId));

        // Update only score and feedback fields
        aiMock.setMockScore(updateDetails.getMockScore());
        aiMock.setFeedBack(updateDetails.getFeedBack());

        return aiMockRepo.save(aiMock);
    }
}