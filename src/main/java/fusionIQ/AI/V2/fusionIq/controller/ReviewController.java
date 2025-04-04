package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Review;
import fusionIQ.AI.V2.fusionIq.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;



import java.util.List;

import java.util.Optional;


@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/save/{userId}/{courseId}")
    public ResponseEntity<Review> createReview(@RequestBody Review review, @PathVariable Long userId, @PathVariable Long courseId) {
        try {
            Review savedReview = reviewService.saveReview(review, userId, courseId);
            return ResponseEntity.ok(savedReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("getBy/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Optional<Review> reviewOpt = reviewService.findReviewById(id);
        return reviewOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/allReviews")
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.findAllReviews();
        return ResponseEntity.ok(reviews);

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable Long userId) {
        List<Review> reviews = reviewService.findReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Review>> getReviewsByCourse(@PathVariable Long courseId) {
        List<Review> reviews = reviewService.findReviewsByCourse(courseId);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable("id") long id, @RequestBody Review updatedReview){
        Optional<Review> existingReviewOptional = reviewService.getReviewById(id);
        if (existingReviewOptional.isPresent()){
            Review existingReview = existingReviewOptional.get();

            if (updatedReview.getRating() != null){
                existingReview.setRating(updatedReview.getRating());
            }

            if (updatedReview.getReviewComment() != null){
                existingReview.setReviewComment(updatedReview.getReviewComment());
            }

            Review saveReview = reviewService.savingReview(existingReview);
            return ResponseEntity.ok(saveReview);
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }
}
