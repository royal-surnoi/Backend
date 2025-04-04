package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Rating;
import fusionIQ.AI.V2.fusionIq.repository.RatingRepo;
import fusionIQ.AI.V2.fusionIq.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class RatingController {

    @Autowired
    private RatingRepo ratingRepo;

    @Autowired
    private RatingService ratingService;

    @PostMapping("/user/{userId}/course/{courseId}")
    public Rating createRating(@PathVariable Long userId, @PathVariable Long courseId, @RequestBody Rating rating) {
        return ratingService.createRating(userId, courseId, rating);
    }
    @GetMapping("/average")
    public ResponseEntity<Double> getAverageRating() {
        List<Rating> ratings = ratingRepo.findAll();

        if (ratings.isEmpty()) {
            return ResponseEntity.ok().body(0.0);
        }
        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getRatingValue();
        }
        double averageRating = sum / ratings.size();

        // Cap the average rating at 5 if it exceeds that value
        averageRating = Math.min(averageRating, 5.0);

        return ResponseEntity.ok().body(averageRating);
    }

    @GetMapping("/course/{id}/rating")
    public ResponseEntity<Object> getRatingById(@PathVariable Long id) {
        Optional<Rating> optionalRating = ratingRepo.findById(id);

        if (optionalRating.isPresent()) {
            return ResponseEntity.ok().body(optionalRating.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rating not found");
        }
    }
    @GetMapping("/average/{courseId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long courseId) {
        double averageRating = ratingService.calculateAverageRating(courseId);
        return ResponseEntity.ok().body(averageRating);
    }
}
