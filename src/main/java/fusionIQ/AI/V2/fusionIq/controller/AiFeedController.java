package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.AiFeed;
import fusionIQ.AI.V2.fusionIq.data.Search;
import fusionIQ.AI.V2.fusionIq.repository.AIFeedRepo;
import fusionIQ.AI.V2.fusionIq.repository.PersonalDetailsRepo;
import fusionIQ.AI.V2.fusionIq.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ai")
public class AiFeedController {

    @Autowired
    private PersonalDetailsService personalDetailsService;

    @Autowired
    private PersonalDetailsRepo personalDetailsRepo;

    @Autowired
    private ArticlePostService articlePostService;

    @Autowired
    private ImagePostService imagePostService;

    @Autowired
    private LongVideoService longVideoService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private AIFeedRepo aiFeedRepo;

    @Autowired
    private FollowService followService;
    @Autowired
    private AiFeedService aiFeedService;

    @Autowired
    private ShortVideoService shortVideoService;

    @GetMapping("/getUserPersonalDetails/{userId}")
    public ResponseEntity<Map<String, Object>> getUserDetails(@PathVariable Long userId) {
        Map<String, Object> details = personalDetailsService.getPersonalDetailsWithUserFields(userId);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }




    @GetMapping("/getAllArticlePosts")
    public ResponseEntity<List<Map<String, Object>>> getAllArticlePostsWithDetails() {
        List<Map<String, Object>> articlePosts = articlePostService.getAllArticlePostsWithDetails();
        return new ResponseEntity<>(articlePosts, HttpStatus.OK);
    }

    @GetMapping("/getAllImagePosts")
    public ResponseEntity<List<Map<String, Object>>> getAllImagePostsWithDetails() {
        List<Map<String, Object>> imagePosts = imagePostService.getAllImagePostsWithDetails();
        return new ResponseEntity<>(imagePosts, HttpStatus.OK);
    }

    @GetMapping("/getAllLongVideoPosts")
    public ResponseEntity<List<Map<String, Object>>> getAllLongVideosWithDetails() {
        List<Map<String, Object>> longVideos = longVideoService.getAllLongVideosWithDetails();
        return new ResponseEntity<>(longVideos, HttpStatus.OK);
    }

    @GetMapping("/getAllShortVideoPosts")
    public ResponseEntity<List<Map<String, Object>>> getAllShortVideosWithDetails() {
        List<Map<String, Object>> shortVideos = shortVideoService.getAllShortVideosWithDetails();
        return new ResponseEntity<>(shortVideos, HttpStatus.OK);
    }


    @GetMapping("/last-10-searches/{userId}")
    public List<Map<String, String>> getLast10SearchesByUserId(@PathVariable Long userId) {
        List<String> searchContents = searchService.getLast10SearchContentsByUserId(userId);
        return searchContents.stream()
                .map(content -> {
                    Map<String, String> searchMap = new HashMap<>();
                    searchMap.put("searchContent", content);
                    return searchMap;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/last-10-feedInteractions/{userId}")
    public List<AiFeed> getLast10FeedsWithInteraction(@PathVariable Long userId) {
        return aiFeedRepo.findTop10ByFeedInteractionTrueOrderByCreatedAtDesc(userId);
    }

    @GetMapping("/followingIds/{userId}")
    public List<Long> getFollowingIdsByUserId(@PathVariable Long userId) {
        return followService.getFollowingIdsByUserId(userId);
    }

    @GetMapping("/getSelectedDetails")
    public ResponseEntity<List<Map<String, Object>>> getSelectedPersonalDetails() {
        List<Map<String, Object>> selectedDetails = personalDetailsService.getSelectedPersonalDetails();
        return new ResponseEntity<>(selectedDetails, HttpStatus.OK);
    }

    @PostMapping("/insertRecommendation/{userId}")
    public ResponseEntity<String> createAiFeed(
            @PathVariable Long userId,
            @RequestParam String feedType,
            @RequestParam boolean feedInteraction,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt,
            @RequestParam(required = false) Long articleId,
            @RequestParam(required = false) Long imageId,
            @RequestParam(required = false) Long shortVideoId,
            @RequestParam(required = false) Long longVideoId) {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();  // Set current time if not provided
        }

        try {
            personalDetailsService.createAiFeed(userId, feedType, feedInteraction, createdAt, articleId, imageId, shortVideoId, longVideoId);
            return ResponseEntity.ok("AiFeed created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create AiFeed.");
        }
    }

    @GetMapping("/getByFeedId/{feedId}")
    public ResponseEntity<?> getAiFeedById(@PathVariable Long feedId) {
        Optional<AiFeed> aiFeedOpt = aiFeedService.getAiFeedById(feedId);
        if (aiFeedOpt.isPresent()) {
            AiFeed aiFeed = aiFeedOpt.get();
            Map<String, Object> response = aiFeedService.buildAiFeedResponse(aiFeed);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feed not found with id: " + feedId);
        }
    }

    @GetMapping("/type/{feedType}")
    public ResponseEntity<?> getAiFeedsByFeedType(@PathVariable String feedType) {
        List<Map<String, Object>> response = aiFeedService.getAiFeedsByFeedTypeFormatted(feedType);
        if (!response.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No feeds found with type: " + feedType);
        }
    }

    @GetMapping("/feeds/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getFeedsByUserIdAndFeedInteraction(
            @PathVariable Long userId,
            @RequestParam boolean feedInteraction) {
        List<AiFeed> feeds = aiFeedService.getFeedsByUserIdAndFeedInteraction(userId, feedInteraction);

        // Transform the results into a list of maps for the response
        List<Map<String, Object>> response = feeds.stream()
                .map(feed -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("feedId", feed.getId());
                    map.put("feedType", feed.getFeedType());
                    map.put("feedInteraction", feed.isFeedInteraction());
                    map.put("createdAt", feed.getCreatedAt());

                    // Include additional details based on the feed type
                    if (feed.getArticlePost() != null) {
                        map.put("articlePost", feed.getArticlePost());
                    } else if (feed.getImagePost() != null) {
                        map.put("imagePost", feed.getImagePost());
                    } else if (feed.getShortVideo() != null) {
                        map.put("shortVideo", feed.getShortVideo());
                    } else if (feed.getLongVideo() != null) {
                        map.put("longVideo", feed.getLongVideo());
                    }

                    return map;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //update
    @GetMapping("/trending/articles")
    public ResponseEntity<List<Map<String, Object>>> getTrendingArticles() {
        List<Map<String, Object>> trendingArticles = articlePostService.getTrendingArticles();
        return new ResponseEntity<>(trendingArticles, HttpStatus.OK);
    }

    // Endpoint: GET /ai/trending/images
    @GetMapping("/trending/images")
    public ResponseEntity<List<Map<String, Object>>> getTrendingImages() {
        List<Map<String, Object>> trendingImages = imagePostService.getTrendingImages();
        return new ResponseEntity<>(trendingImages, HttpStatus.OK);
    }

    // Endpoint: GET /ai/trending/long-videos
    @GetMapping("/trending/long-videos")
    public ResponseEntity<List<Map<String, Object>>> getTrendingLongVideos() {
        List<Map<String, Object>> trendingLongVideos = longVideoService.getTrendingLongVideos();
        return new ResponseEntity<>(trendingLongVideos, HttpStatus.OK);
    }

    @GetMapping("/trending/short-videos")
    public ResponseEntity<List<Map<String, Object>>> getTrendingShortVideos() {
        List<Map<String, Object>> trendingShortVideos = shortVideoService.getTrendingShortVideos();
        return new ResponseEntity<>(trendingShortVideos, HttpStatus.OK);
    }


    // Endpoint: GET /ai-feed/{userId}/interactions
    @GetMapping("/ai-feed/{userId}/interactions")
    public ResponseEntity<List<AiFeed>> getAllInteractionsByUserId(@PathVariable Long userId) {
        List<AiFeed> interactions = aiFeedService.getAllInteractionsByUserId(userId);
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }


}