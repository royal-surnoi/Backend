package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.Follow;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.FollowRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.FollowService;
import fusionIQ.AI.V2.fusionIq.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class FollowServiceTest {

    @InjectMocks
    private FollowService followService;

    @Mock
    private FollowRepo followRepository;

    @Mock
    private UserRepo userRepo;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveFollow_Success() {
        Follow follow = new Follow();
        when(followRepository.save(any(Follow.class))).thenReturn(follow);

        Follow result = followService.saveFollow(follow);

        assertEquals(follow, result);
        verify(followRepository, times(1)).save(follow);
    }

    @Test
    void testGetFollowById_Found() {
        Follow follow = new Follow();
        when(followRepository.findById(anyLong())).thenReturn(Optional.of(follow));

        Optional<Follow> result = followService.getFollowById(1L);

        assertTrue(result.isPresent());
        assertEquals(follow, result.get());
        verify(followRepository, times(1)).findById(1L);
    }

    @Test
    void testGetFollowById_NotFound() {
        when(followRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Follow> result = followService.getFollowById(1L);

        assertFalse(result.isPresent());
        verify(followRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteFollow_Success() {
        doNothing().when(followRepository).deleteById(anyLong());

        followService.deleteFollow(1L);

        verify(followRepository, times(1)).deleteById(1L);
    }

    @Test
    void testIncrementFollowerAndFollowingCounts_Success() {
        Follow follow = new Follow();
        follow.setFollowerCount(1L);
        follow.setFollowingCount(1L);

        when(followRepository.findByFollowerIdAndFollowingId(anyLong(), anyLong())).thenReturn(Optional.of(follow));
        when(followRepository.save(any(Follow.class))).thenReturn(follow);

        followService.incrementFollowerAndFollowingCounts(1L, 2L);

        assertEquals(2L, follow.getFollowerCount());
        assertEquals(2L, follow.getFollowingCount());
        verify(notificationService, times(1)).createFollowRequestAcceptedNotification(1L, 2L);
    }

    @Test
    void testIncrementFollowerAndFollowingCounts_FollowRequestNotFound() {
        when(followRepository.findByFollowerIdAndFollowingId(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            followService.incrementFollowerAndFollowingCounts(1L, 2L);
        });

        verify(notificationService, times(0)).createFollowRequestAcceptedNotification(anyLong(), anyLong());
    }

    @Test
    void testDecrementFollowerAndFollowingCounts_Success() {
        doNothing().when(followRepository).decrementFollowerCount(anyLong(), anyLong());
        doNothing().when(followRepository).decrementFollowingCount(anyLong(), anyLong());

        followService.decrementFollowerAndFollowingCounts(1L, 2L);

        verify(followRepository, times(1)).decrementFollowerCount(1L, 2L);
        verify(followRepository, times(1)).decrementFollowingCount(1L, 2L);
    }

    @Test
    void testCreateFollow_Success() {
        User follower = new User();
        follower.setId(1L);
        User following = new User();
        following.setId(2L);

        Follow follow = new Follow(follower, following);
        follow.setFollowerCount(0L);
        follow.setFollowingCount(0L);
        follow.setRequestedTime(LocalDateTime.now());
        follow.setRequested(true);
        follow.setFollowed(false);

        when(followRepository.findByFollowerIdAndFollowingId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(userRepo.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepo.findById(2L)).thenReturn(Optional.of(following));
        when(followRepository.save(any(Follow.class))).thenReturn(follow);

        Follow result = followService.createFollow(1L, 2L);

        assertNotNull(result);
        assertEquals(follow, result);
        verify(notificationService, times(1)).createFollowRequestNotification(1L, 2L);
    }

    @Test
    void testCreateFollow_FollowRequestAlreadyExists() {
        Follow follow = new Follow();
        when(followRepository.findByFollowerIdAndFollowingId(anyLong(), anyLong())).thenReturn(Optional.of(follow));

        Follow result = followService.createFollow(1L, 2L);

        assertNull(result);
        verify(followRepository, times(0)).save(any(Follow.class));
        verify(notificationService, times(0)).createFollowRequestNotification(anyLong(), anyLong());
    }

    @Test
    void testGetFollowersWithCountEqualsToOne_Success() {
        List<Follow> follows = Arrays.asList(new Follow());
        when(followRepository.findByFollowerIdAndFollowerCountAndFollowingCount(anyLong(), eq(1L), eq(1L)))
                .thenReturn(follows);

        List<Follow> result = followService.getFollowersWithCountEqualsToOne(1L);

        assertEquals(follows, result);
        verify(followRepository, times(1))
                .findByFollowerIdAndFollowerCountAndFollowingCount(1L, 1L, 1L);
    }

    @Test
    void testGetFollowingWithCountEqualsToOne_Success() {
        List<Follow> follows = Arrays.asList(new Follow());
        when(followRepository.findByFollowingIdAndFollowingCountAndFollowerCount(anyLong(), eq(1L), eq(1L)))
                .thenReturn(follows);

        List<Follow> result = followService.getFollowingWithCountEqualsToOne(1L);

        assertEquals(follows, result);
        verify(followRepository, times(1))
                .findByFollowingIdAndFollowingCountAndFollowerCount(1L, 1L, 1L);
    }

    @Test
    void testGetAllFollowerIdsWithFollowerAndFollowingCountZeroByFollowingId_Success() {
        List<Long> followerIds = Arrays.asList(1L, 2L, 3L);
        when(followRepository.findFollowerIdsByFollowingIdAndFollowerCountAndFollowingCountZero(anyLong()))
                .thenReturn(followerIds);

        List<Long> result = followService.getAllFollowerIdsWithFollowerAndFollowingCountZeroByFollowingId(1L);

        assertEquals(followerIds, result);
        verify(followRepository, times(1))
                .findFollowerIdsByFollowingIdAndFollowerCountAndFollowingCountZero(1L);
    }

    @Test
    void testGetAllFollowingIdsWithFollowingAndFollowersCountZeroByFollowerId_Success() {
        List<Long> followingIds = Arrays.asList(1L, 2L, 3L);
        when(followRepository.findFollowingIdsByFollowerIdAndFollowingCountAndFollowerCountZero(anyLong()))
                .thenReturn(followingIds);

        List<Long> result = followService.getAllFollowingIdsWithFollowingAndFollowersCountZeroByFollowerId(1L);

        assertEquals(followingIds, result);
        verify(followRepository, times(1))
                .findFollowingIdsByFollowerIdAndFollowingCountAndFollowerCountZero(1L);
    }

    @Test
    void testGetFollowersByTeacherId_Success() {
        List<User> followers = Arrays.asList(new User(), new User());
        when(followRepository.findFollowersByTeacherId(anyLong())).thenReturn(followers);

        List<User> result = followService.getFollowersByTeacherId(1L);

        assertEquals(followers, result);
        verify(followRepository, times(1)).findFollowersByTeacherId(1L);
    }

    @Test
    void testGetFollowDetails_Success() {
        List<Follow> followers = Arrays.asList(new Follow());
        List<Follow> following = Arrays.asList(new Follow());

        when(followRepository.findFollowersByUserIdAndFollowerCount(anyLong())).thenReturn(followers);
        when(followRepository.findFollowingByUserIdAndFollowingCount(anyLong())).thenReturn(following);

        Map<String, List<Follow>> result = followService.getFollowDetails(1L);

        assertEquals(followers, result.get("followers"));
        assertEquals(following, result.get("following"));
        verify(followRepository, times(1)).findFollowersByUserIdAndFollowerCount(1L);
        verify(followRepository, times(1)).findFollowingByUserIdAndFollowingCount(1L);
    }
}
