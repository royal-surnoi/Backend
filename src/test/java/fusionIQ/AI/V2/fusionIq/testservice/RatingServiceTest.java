//package fusionIQ.AI.V2.fusionIq.testservice;
//
//
//
//
//import fusionIQ.AI.V2.fusionIq.data.Course;
//import fusionIQ.AI.V2.fusionIq.data.Rating;
//import fusionIQ.AI.V2.fusionIq.data.User;
//import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
//import fusionIQ.AI.V2.fusionIq.repository.RatingRepo;
//import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
//import fusionIQ.AI.V2.fusionIq.service.RatingService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//public class RatingServiceTest {
//
//    @Mock
//    private RatingRepo ratingRepo;
//
//    @Mock
//    private UserRepo userRepo;
//
//    @Mock
//    private CourseRepo courseRepo;
//
//    @InjectMocks
//    private RatingService ratingService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCreateRating() {
//        User user = new User();
//        user.setId(1L);
//
//        Course course = new Course();
//        course.setId(1L);
//
//        Rating rating = new Rating();
//        rating.setRatingValue(4);
//
//        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
//        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
//        when(ratingRepo.save(any(Rating.class))).thenReturn(rating);
//
//        Rating savedRating = ratingService.createRating(1L, 1L, rating);
//
//        assertEquals(4, savedRating.getRatingValue());
//        verify(ratingRepo, times(1)).save(rating);
//    }
//
//    @Test
//    public void testCreateRating_InvalidRatingValue() {
//        Rating rating = new Rating();
//        rating.setRatingValue(6);
//
//        assertThrows(IllegalArgumentException.class, () -> ratingService.createRating(1L, 1L, rating));
//    }
//
//    @Test
//    public void testCalculateAverageRating_NoRatings() {
//        when(ratingRepo.findByCourseId(1L)).thenReturn(Collections.emptyList());
//
//        double averageRating = ratingService.calculateAverageRating(1L);
//
//        assertEquals(0.0, averageRating);
//    }
//
//    @Test
//    public void testCalculateAverageRating_WithRatings() {
//        Rating rating = new Rating();
//        rating.setRatingValue(4);
//
//        when(ratingRepo.findByCourseId(1L)).thenReturn(Collections.singletonList(rating));
//
//        double averageRating = ratingService.calculateAverageRating(1L);
//
//        assertEquals(4.0, averageRating);
//    }
//}
