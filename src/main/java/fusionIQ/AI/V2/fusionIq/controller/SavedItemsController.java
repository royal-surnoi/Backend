package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/savedItems")
public class SavedItemsController {

    @Autowired
    private SavedItemsRepo savedItemsRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ArticlePostRepo articlePostRepo;

    @Autowired
    private ShortVideoRepo shortVideoRepo;

    @Autowired
    private LongVideoRepo longVideoRepo;

    @Autowired
    private ImagePostRepo imagePostRepo;

    @PostMapping("/saveImagePost")
    public ResponseEntity<?> saveImagePost(@RequestParam Long userId, @RequestParam Long imagePostId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ImagePost imagePost = imagePostRepo.findById(imagePostId).orElseThrow(() -> new ResourceNotFoundException("ImagePost not found"));

        SavedItems existingSavedItem = savedItemsRepo.findByUserAndImagePost(user, imagePost);
        if (existingSavedItem != null) {
            return new ResponseEntity<>("Item already saved", HttpStatus.OK);
        }

        SavedItems savedItem = new SavedItems(user, imagePost);
        savedItemsRepo.save(savedItem);

        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @PostMapping("/saveArticlePost")
    public ResponseEntity<?> saveArticlePost(@RequestParam Long userId, @RequestParam Long articlePostId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ArticlePost articlePost = articlePostRepo.findById(articlePostId).orElseThrow(() -> new ResourceNotFoundException("ArticlePost not found"));

        SavedItems existingSavedItem = savedItemsRepo.findByUserAndArticlePost(user, articlePost);
        if (existingSavedItem != null) {
            return new ResponseEntity<>("Item already saved", HttpStatus.OK);
        }

        SavedItems savedItem = new SavedItems(user, articlePost);
        savedItemsRepo.save(savedItem);

        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @PostMapping("/saveShortVideo")
    public ResponseEntity<?> saveShortVideo(@RequestParam Long userId, @RequestParam Long shortVideoId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ShortVideo shortVideo = shortVideoRepo.findById(shortVideoId).orElseThrow(() -> new ResourceNotFoundException("ShortVideo not found"));

        SavedItems existingSavedItem = savedItemsRepo.findByUserAndShortVideo(user, shortVideo);
        if (existingSavedItem != null) {
            return new ResponseEntity<>("Item already saved", HttpStatus.OK);
        }

        SavedItems savedItem = new SavedItems(user, shortVideo);
        savedItemsRepo.save(savedItem);

        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @PostMapping("/saveLongVideo")
    public ResponseEntity<?> saveLongVideo(@RequestParam Long userId, @RequestParam Long longVideoId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        LongVideo longVideo = longVideoRepo.findById(longVideoId).orElseThrow(() -> new ResourceNotFoundException("LongVideo not found"));

        SavedItems existingSavedItem = savedItemsRepo.findByUserAndLongVideo(user, longVideo);
        if (existingSavedItem != null) {
            return new ResponseEntity<>("Item already saved", HttpStatus.OK);
        }

        SavedItems savedItem = new SavedItems(user, longVideo);
        savedItemsRepo.save(savedItem);

        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @GetMapping("/getSavedPosts")
    public ResponseEntity<List<ImagePost>> getSavedPosts(@RequestParam Long userId) {
        List<SavedItems> savedItems = savedItemsRepo.findByUserIdAndImagePostIsNotNull(userId);
        List<ImagePost> imagePost = savedItems.stream().map(SavedItems::getImagePost).collect(Collectors.toList());
        return new ResponseEntity<>(imagePost, HttpStatus.OK);
    }

    @GetMapping("/getSavedArticlePosts")
    public ResponseEntity<List<ArticlePost>> getSavedArticlePosts(@RequestParam Long userId) {
        List<SavedItems> savedItems = savedItemsRepo.findByUserIdAndArticlePostIsNotNull(userId);
        List<ArticlePost> articlePosts = savedItems.stream().map(SavedItems::getArticlePost).collect(Collectors.toList());
        return new ResponseEntity<>(articlePosts, HttpStatus.OK);
    }

    @GetMapping("/getSavedShortVideos")
    public ResponseEntity<List<ShortVideo>> getSavedShortVideos(@RequestParam Long userId) {
        List<SavedItems> savedItems = savedItemsRepo.findByUserIdAndShortVideoIsNotNull(userId);
        List<ShortVideo> shortVideos = savedItems.stream().map(SavedItems::getShortVideo).collect(Collectors.toList());
        return new ResponseEntity<>(shortVideos, HttpStatus.OK);
    }

    @GetMapping("/getSavedLongVideos")
    public ResponseEntity<List<LongVideo>> getSavedLongVideos(@RequestParam Long userId) {
        List<SavedItems> savedItems = savedItemsRepo.findByUserIdAndLongVideoIsNotNull(userId);
        List<LongVideo> longVideos = savedItems.stream().map(SavedItems::getLongVideo).collect(Collectors.toList());
        return new ResponseEntity<>(longVideos, HttpStatus.OK);
    }
    @GetMapping("/getAllSavedItems")
    public ResponseEntity<Map<String, List<?>>> getAllSavedItems(@RequestParam Long userId) {
        List<SavedItems> allSavedItems = savedItemsRepo.findByUserId(userId);

        List<ImagePost> imagePosts = new ArrayList<>();
        List<ArticlePost> articlePosts = new ArrayList<>();
        List<ShortVideo> shortVideos = new ArrayList<>();
        List<LongVideo> longVideos = new ArrayList<>();

        for (SavedItems item : allSavedItems) {
            if (item.getImagePost() != null) {
                imagePosts.add(item.getImagePost());
            } else if (item.getArticlePost() != null) {
                articlePosts.add(item.getArticlePost());
            } else if (item.getShortVideo() != null) {
                shortVideos.add(item.getShortVideo());
            } else if (item.getLongVideo() != null) {
                longVideos.add(item.getLongVideo());
            }
        }

        Map<String, List<?>> result = new HashMap<>();
        result.put("imagePosts", imagePosts);
        result.put("articlePosts", articlePosts);
        result.put("shortVideos", shortVideos);
        result.put("longVideos", longVideos);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/deleteImagePost")
    public ResponseEntity<?> deleteSavedImagePost(@RequestParam Long userId, @RequestParam Long imagePostId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ImagePost imagePost = imagePostRepo.findById(imagePostId).orElseThrow(() -> new ResourceNotFoundException("ImagePost not found"));

        SavedItems savedItem = savedItemsRepo.findByUserAndImagePost(user, imagePost);
        if (savedItem != null) {
            savedItemsRepo.delete(savedItem);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteArticlePost")
    public ResponseEntity<?> deleteSavedArticlePost(@RequestParam Long userId, @RequestParam Long articlePostId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ArticlePost articlePost = articlePostRepo.findById(articlePostId).orElseThrow(() -> new ResourceNotFoundException("ArticlePost not found"));

        SavedItems savedItem = savedItemsRepo.findByUserAndArticlePost(user, articlePost);
        if (savedItem != null) {
            savedItemsRepo.delete(savedItem);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteShortVideo")
    public ResponseEntity<?> deleteSavedShortVideo(@RequestParam Long userId, @RequestParam Long shortVideoId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ShortVideo shortVideo = shortVideoRepo.findById(shortVideoId).orElseThrow(() -> new ResourceNotFoundException("ShortVideo not found"));

        SavedItems savedItem = savedItemsRepo.findByUserAndShortVideo(user, shortVideo);
        if (savedItem != null) {
            savedItemsRepo.delete(savedItem);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteLongVideo")
    public ResponseEntity<?> deleteSavedLongVideo(@RequestParam Long userId, @RequestParam Long longVideoId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        LongVideo longVideo = longVideoRepo.findById(longVideoId).orElseThrow(() -> new ResourceNotFoundException("LongVideo not found"));

        SavedItems savedItem = savedItemsRepo.findByUserAndLongVideo(user, longVideo);
        if (savedItem != null) {
            savedItemsRepo.delete(savedItem);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteSavedItem")
    public ResponseEntity<?> deleteSavedItem(@RequestParam Long savedItemId) {
        SavedItems savedItem = savedItemsRepo.findById(savedItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Saved item not found"));
        savedItemsRepo.delete(savedItem);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getSavedItemId")
    public ResponseEntity<Long> getSavedItemId(@RequestParam Long userId, @RequestParam String postType, @RequestParam Long postId) {

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SavedItems savedItem = null;

        switch (postType) {

            case "image":

                ImagePost imagePost = imagePostRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("ImagePost not found"));

                savedItem = savedItemsRepo.findByUserAndImagePost(user, imagePost);

                break;

            case "article":

                ArticlePost articlePost = articlePostRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("ArticlePost not found"));

                savedItem = savedItemsRepo.findByUserAndArticlePost(user, articlePost);

                break;

            case "shortVideo":

                ShortVideo shortVideo = shortVideoRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("ShortVideo not found"));

                savedItem = savedItemsRepo.findByUserAndShortVideo(user, shortVideo);

                break;

            case "longVideo":

                LongVideo longVideo = longVideoRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("LongVideo not found"));

                savedItem = savedItemsRepo.findByUserAndLongVideo(user, longVideo);

                break;

            default:

                throw new IllegalArgumentException("Unknown post type: " + postType);

        }

        if (savedItem != null) {

            return ResponseEntity.ok(savedItem.getId());

        } else {

            return ResponseEntity.notFound().build();

        }

    }
}
