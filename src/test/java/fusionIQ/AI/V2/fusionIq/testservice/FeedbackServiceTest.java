//package fusionIQ.AI.V2.fusionIq.testservice;
//
//
//
//import fusionIQ.AI.V2.fusionIq.data.Feedback;
//import fusionIQ.AI.V2.fusionIq.data.User;
//import fusionIQ.AI.V2.fusionIq.repository.FeedbackRepo;
//import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
//import fusionIQ.AI.V2.fusionIq.service.FeedbackService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//public class FeedbackServiceTest {
//
//    @InjectMocks
//    private FeedbackService feedbackService;
//
//    @Mock
//    private UserRepo userRepo;
//
//    @Mock
//    private FeedbackRepo feedbackRepo;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateFeedback_Success() {
//        User user = new User();
//        user.setId(1L);
//
//        Feedback feedback = new Feedback();
//        feedback.setFeedback(true);
//        feedback.setUser(user);
//
//        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
//        when(feedbackRepo.save(any(Feedback.class))).thenReturn(feedback);
//
//        Feedback result = feedbackService.createFeedback(1L, true);
//
//        assertEquals(feedback, result);
//        verify(userRepo, times(1)).findById(1L);
//        verify(feedbackRepo, times(1)).save(feedback);
//    }
//
//    @Test
//    void testCreateFeedback_UserNotFound() {
//        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () -> {
//            feedbackService.createFeedback(1L, true);
//        });
//
//        verify(userRepo, times(1)).findById(1L);
//        verify(feedbackRepo, times(0)).save(any(Feedback.class));
//    }
//
//    @Test
//    void testFindById_Success() {
//        Feedback feedback = new Feedback();
//        when(feedbackRepo.findById(anyLong())).thenReturn(Optional.of(feedback));
//
//        Feedback result = feedbackService.findById(1L);
//
//        assertEquals(feedback, result);
//        verify(feedbackRepo, times(1)).findById(1L);
//    }
//
//    @Test
//    void testFindById_NotFound() {
//        when(feedbackRepo.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () -> {
//            feedbackService.findById(1L);
//        });
//
//        verify(feedbackRepo, times(1)).findById(1L);
//    }
//
//    @Test
//    void testFindByUserId_Success() {
//        User user = new User();
//        user.setId(1L);
//
//        List<Feedback> feedbackList = new ArrayList<>();
//        feedbackList.add(new Feedback());
//
//        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
//        when(feedbackRepo.findByUserId(anyLong())).thenReturn(feedbackList);
//
//        List<Feedback> result = feedbackService.findByUserId(1L);
//
//        assertEquals(feedbackList, result);
//        verify(userRepo, times(1)).findById(1L);
//        verify(feedbackRepo, times(1)).findByUserId(1L);
//    }
//
//    @Test
//    void testFindByUserId_UserNotFound() {
//        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () -> {
//            feedbackService.findByUserId(1L);
//        });
//
//        verify(userRepo, times(1)).findById(1L);
//        verify(feedbackRepo, times(0)).findByUserId(anyLong());
//    }
//}
//
