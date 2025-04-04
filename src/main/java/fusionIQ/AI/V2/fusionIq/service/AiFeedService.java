package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.AiFeed;
import fusionIQ.AI.V2.fusionIq.repository.AIFeedRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AiFeedService {

    @Autowired
    private AIFeedRepo aiFeedRepo;

    public Optional<AiFeed> getAiFeedById(Long feedId) {
        return aiFeedRepo.findById(feedId);
    }

    public Map<String, Object> buildAiFeedResponse(AiFeed aiFeed) {
        Map<String, Object> response = new HashMap<>();
        response.put("feedId", aiFeed.getId());
        response.put("feedType", aiFeed.getFeedType());
        response.put("feedInteraction", aiFeed.isFeedInteraction());
        response.put("createdAt", aiFeed.getCreatedAt());

        if (aiFeed.getArticlePost() != null) {
            response.put("articlePost", aiFeed.getArticlePost());
        } else if (aiFeed.getImagePost() != null) {
            response.put("imagePost", aiFeed.getImagePost());
        } else if (aiFeed.getShortVideo() != null) {
            response.put("shortVideo", aiFeed.getShortVideo());
        } else if (aiFeed.getLongVideo() != null) {
            response.put("longVideo", aiFeed.getLongVideo());
        }

        return response;
    }

    public List<Map<String, Object>> getAiFeedsByFeedTypeFormatted(String feedType) {
        List<AiFeed> aiFeeds = aiFeedRepo.findByFeedType(feedType);
        return aiFeeds.stream()
                .map(this::buildAiFeedResponses)
                .collect(Collectors.toList());
    }
    public List<AiFeed> getFeedsByUserIdAndFeedInteraction(Long userId, boolean feedInteraction) {
        return aiFeedRepo.findByUserIdAndFeedInteractionOrderByCreatedAtDesc(userId, feedInteraction);
    }

    // Custom response formatter to include only userId
    public Map<String, Object> buildAiFeedResponses(AiFeed aiFeed) {
        Map<String, Object> response = new HashMap<>();
        response.put("feedId", aiFeed.getId());
        response.put("feedType", aiFeed.getFeedType());
        response.put("feedInteraction", aiFeed.isFeedInteraction());
        response.put("createdAt", aiFeed.getCreatedAt());

        // Include only userId from User entity
        if (aiFeed.getUser() != null) {
            response.put("userId", aiFeed.getUser().getId());
        }

        // Include related post details based on feedType
        if (aiFeed.getArticlePost() != null) {
            response.put("articlePost", aiFeed.getArticlePost());
        } else if (aiFeed.getImagePost() != null) {
            response.put("imagePost", aiFeed.getImagePost());
        } else if (aiFeed.getShortVideo() != null) {
            response.put("shortVideo", aiFeed.getShortVideo());
        } else if (aiFeed.getLongVideo() != null) {
            response.put("longVideo", aiFeed.getLongVideo());
        }

        return response;
    }

    public List<AiFeed> getAllInteractionsByUserId(Long userId) {
        return aiFeedRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

}
