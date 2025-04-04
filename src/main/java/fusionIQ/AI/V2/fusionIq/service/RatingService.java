package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Rating;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.RatingRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepo ratingRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private CourseService courseService;

    public List<Rating> getAllItems() {
        return ratingRepo.findAll();
    }

    public Rating rateItem(Long itemId, int rating) {
        Rating item = ratingRepo.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        item.setRatingValue(rating);
        return ratingRepo.save(item);
    }

    @Transactional
    public Rating createRating(Long userId, Long courseId, Rating rating) {
        if (rating.getStars() < 1 || rating.getStars() > 5) {
            throw new IllegalArgumentException("Rating stars must be between 1 and 5");
        }

        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        rating.setUser(user);
        rating.setCourse(course);

        // Save the rating
        Rating savedRating = ratingRepo.save(rating);

        // Update the overall course rating
        updateCourseRating(courseId);

        return savedRating;
    }

    private void updateCourseRating(Long courseId) {
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        // Retrieve all ratings for the course
        List<Rating> ratings = ratingRepo.findByCourseId(courseId);

        // Calculate the average rating
        double averageRating = ratings.stream()
                .mapToInt(Rating::getStars)
                .average()
                .orElse(0.0);

        // Update the course's overall rating
        course.setCourseRating((float) averageRating);
        courseRepo.save(course);
    }


    public double calculateAverageRating(Long courseId) {
        List<Rating> ratings = ratingRepo.findByCourseId(courseId);
        if (ratings.isEmpty()) {
            return 0.0;
        }

        double sum = ratings.stream().mapToDouble(Rating::getRatingValue).sum();
        double averageRating = sum / ratings.size();

        return Math.min(averageRating, 5.0);
    }

}

