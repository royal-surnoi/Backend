package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Review;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.ReviewRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;



import java.util.List;

import java.util.Optional;



@Service
public class ReviewService {

    @Autowired
    private ReviewRepo reviewRepository;
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private CourseRepo courseRepository;

    public Review saveReview(Review review, Long userId, Long courseId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (userOpt.isPresent() && courseOpt.isPresent()) {
            review.setUser(userOpt.get());
            review.setCourse(courseOpt.get());
        } else {
            throw new IllegalArgumentException("User or Course not found");
        }
        return reviewRepository.save(review);
    }



    public Optional<Review> findReviewById(Long id) {
        return reviewRepository.findById(id);
    }


    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }


    public List<Review> findReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }


    public List<Review> findReviewsByCourse(Long courseId) {
        return reviewRepository.findByCourseId(courseId);
    }

    public Optional<Review>getReviewById(Long id){
        return reviewRepository.findById(id);
    }

    public Review savingReview(Review existingReview){
        return reviewRepository.save(existingReview);
    }

}
