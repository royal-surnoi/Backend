package fusionIQ.AI.V2.fusionIq.testservice;


import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Review;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.ReviewRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock
    private ReviewRepo reviewRepository;

    @Mock
    private UserRepo userRepository;

    @Mock
    private CourseRepo courseRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveReview_Success() {
        Review review = new Review();
        review.setReviewComment("Great course");
        review.setRating(5L);

        User user = new User();
        user.setId(1L);

        Course course = new Course();
        course.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(reviewRepository.save(review)).thenReturn(review);

        Review savedReview = reviewService.saveReview(review, 1L, 1L);

        assertEquals(review, savedReview);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    public void testSaveReview_UserOrCourseNotFound() {
        Review review = new Review();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> reviewService.saveReview(review, 1L, 1L));
    }

    @Test
    public void testFindReviewById() {
        Review review = new Review();
        review.setId(1L);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        Optional<Review> foundReview = reviewService.findReviewById(1L);

        assertEquals(Optional.of(review), foundReview);
    }

    @Test
    public void testFindAllReviews() {
        Review review = new Review();

        when(reviewRepository.findAll()).thenReturn(Collections.singletonList(review));

        assertEquals(1, reviewService.findAllReviews().size());
    }

    @Test
    public void testDeleteReview() {
        reviewService.deleteReview(1L);
        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindReviewsByUser() {
        Review review = new Review();

        when(reviewRepository.findByUserId(1L)).thenReturn(Collections.singletonList(review));

        assertEquals(1, reviewService.findReviewsByUser(1L).size());
    }

    @Test
    public void testFindReviewsByCourse() {
        Review review = new Review();

        when(reviewRepository.findByCourseId(1L)).thenReturn(Collections.singletonList(review));

        assertEquals(1, reviewService.findReviewsByCourse(1L).size());
    }

    @Test
    public void testUpdateReview() {
        Review review = new Review();
        review.setId(1L);
        review.setReviewComment("Updated comment");

        when(reviewRepository.save(review)).thenReturn(review);

        Review updatedReview = reviewService.savingReview(review);

        assertEquals("Updated comment", updatedReview.getReviewComment());
        verify(reviewRepository, times(1)).save(review);
    }
}
