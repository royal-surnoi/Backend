package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.FollowController;
import fusionIQ.AI.V2.fusionIq.data.Follow;
import fusionIQ.AI.V2.fusionIq.service.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FollowControllerTest {

    @Mock
    private FollowService followService;

    @InjectMocks
    private FollowController followController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFollowById_Success() {
        Long followId = 1L;
        Follow follow = new Follow();

        when(followService.getFollowById(followId)).thenReturn(Optional.of(follow));

        ResponseEntity<Follow> response = followController.getFollowById(followId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(follow, response.getBody());
        verify(followService, times(1)).getFollowById(followId);
    }

    @Test
    void testGetFollowById_NotFound() {
        Long followId = 1L;

        when(followService.getFollowById(followId)).thenReturn(Optional.empty());

        ResponseEntity<Follow> response = followController.getFollowById(followId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(followService, times(1)).getFollowById(followId);
    }

    @Test
    void testDeleteFollow_Success() {
        Long followId = 1L;

        doNothing().when(followService).deleteFollow(followId);

        ResponseEntity<Void> response = followController.deleteFollow(followId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(followService, times(1)).deleteFollow(followId);
    }

    @Test
    void testIncrementCounts_Success() {
        Long followerId = 1L;
        Long followingId = 2L;

        doNothing().when(followService).incrementFollowerAndFollowingCounts(followerId, followingId);

        ResponseEntity<Void> response = followController.incrementCounts(followerId, followingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(followService, times(1)).incrementFollowerAndFollowingCounts(followerId, followingId);
    }

    @Test
    void testDecrementCounts_Success() {
        Long followerId = 1L;
        Long followingId = 2L;

        doNothing().when(followService).decrementFollowerAndFollowingCounts(followerId, followingId);

        ResponseEntity<Void> response = followController.decrementCounts(followerId, followingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(followService, times(1)).decrementFollowerAndFollowingCounts(followerId, followingId);
    }

    @Test
    void testSumFollowerCounts_Success() {
        Long followerId = 1L;
        Long expectedSum = 10L;

        when(followService.sumFollowerCountByFollowerId(followerId)).thenReturn(expectedSum);

        ResponseEntity<Long> response = followController.sumFollowerCounts(followerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSum, response.getBody());
        verify(followService, times(1)).sumFollowerCountByFollowerId(followerId);
    }

    @Test
    void testSumFollowingCounts_Success() {
        Long followingId = 2L;
        Long expectedSum = 15L;

        when(followService.sumFollowingCountByFollowingId(followingId)).thenReturn(expectedSum);

        ResponseEntity<Long> response = followController.sumFollowingCounts(followingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSum, response.getBody());
        verify(followService, times(1)).sumFollowingCountByFollowingId(followingId);
    }

    @Test
    void testCountFollowRequestsByFollowingId_Success() {
        Long followingId = 2L;
        Long expectedCount = 5L;

        when(followService.countFollowRequestsByFollowingId(followingId)).thenReturn(expectedCount);

        ResponseEntity<Long> response = followController.countFollowRequestsByFollowingId(followingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCount, response.getBody());
        verify(followService, times(1)).countFollowRequestsByFollowingId(followingId);
    }

    @Test
    void testGetFollowerIdsByFollowingId_Success() {
        Long followingId = 2L;
        List<Long> expectedFollowerIds = Arrays.asList(1L, 3L, 5L);

        when(followService.getFollowerIdsByFollowingId(followingId)).thenReturn(expectedFollowerIds);

        ResponseEntity<List<Long>> response = followController.getFollowerIdsByFollowingId(followingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFollowerIds, response.getBody());
        verify(followService, times(1)).getFollowerIdsByFollowingId(followingId);
    }

    @Test
    void testDeleteAllFollows_Success() {
        doNothing().when(followService).deleteAllFollows();

        ResponseEntity<Void> response = followController.deleteAllFollows();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(followService, times(1)).deleteAllFollows();
    }

    @Test
    void testGetFollowingIdByFollowerIdWithCountOne_Success() {
        Long followerId = 1L;
        Long expectedFollowingId = 2L;

        when(followService.getFollowingIdByFollowerIdWithCountOne(followerId)).thenReturn(Optional.of(expectedFollowingId));

        ResponseEntity<Long> response = followController.getFollowingIdByFollowerIdWithCountOne(followerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFollowingId, response.getBody());
        verify(followService, times(1)).getFollowingIdByFollowerIdWithCountOne(followerId);
    }

    @Test
    void testGetFollowingIdByFollowerIdWithCountOne_NotFound() {
        Long followerId = 1L;

        when(followService.getFollowingIdByFollowerIdWithCountOne(followerId)).thenReturn(Optional.empty());

        ResponseEntity<Long> response = followController.getFollowingIdByFollowerIdWithCountOne(followerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(followService, times(1)).getFollowingIdByFollowerIdWithCountOne(followerId);
    }

    @Test
    void testGetFollowerIdByFollowingIdWithCountOne_Success() {
        Long followingId = 2L;
        Long expectedFollowerId = 1L;

        when(followService.getFollowerIdByFollowingIdWithCountOne(followingId)).thenReturn(Optional.of(expectedFollowerId));

        ResponseEntity<Long> response = followController.getFollowerIdByFollowingIdWithCountOne(followingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFollowerId, response.getBody());
        verify(followService, times(1)).getFollowerIdByFollowingIdWithCountOne(followingId);
    }

    @Test
    void testGetFollowerIdByFollowingIdWithCountOne_NotFound() {
        Long followingId = 2L;

        when(followService.getFollowerIdByFollowingIdWithCountOne(followingId)).thenReturn(Optional.empty());

        ResponseEntity<Long> response = followController.getFollowerIdByFollowingIdWithCountOne(followingId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(followService, times(1)).getFollowerIdByFollowingIdWithCountOne(followingId);
    }

    @Test
    void testCreateFollowByIds_Success() {
        Long followerId = 1L;
        Long followingId = 2L;
        Follow follow = new Follow();

        when(followService.createFollow(followerId, followingId)).thenReturn(follow);

        ResponseEntity<Object> response = followController.createFollowByIds(followerId, followingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(follow, response.getBody());
        verify(followService, times(1)).createFollow(followerId, followingId);
    }

    @Test
    void testCreateFollowByIds_AlreadyExists() {
        Long followerId = 1L;
        Long followingId = 2L;

        when(followService.createFollow(followerId, followingId)).thenReturn(null);

        ResponseEntity<Object> response = followController.createFollowByIds(followerId, followingId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Follow request already sent", response.getBody());
        verify(followService, times(1)).createFollow(followerId, followingId);
    }

    @Test
    void testDeleteByFollowerAndFollowing_Success() {
        Long followerId = 1L;
        Long followingId = 2L;

        doNothing().when(followService).deleteByFollowerAndFollowing(followerId, followingId);

        ResponseEntity<Void> response = followController.deleteByFollowerAndFollowing(followerId, followingId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(followService, times(1)).deleteByFollowerAndFollowing(followerId, followingId);
    }

    @Test
    void testGetFollowersWithCountEqualsToOne_Success() {
        Long followerId = 1L;
        Follow follow1 = new Follow();
        Follow follow2 = new Follow();
        List<Follow> expectedFollowers = Arrays.asList(follow1, follow2);

        when(followService.getFollowersWithCountEqualsToOne(followerId)).thenReturn(expectedFollowers);

        ResponseEntity<List<Follow>> response = followController.getFollowersWithCountEqualsToOne(followerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFollowers, response.getBody());
        verify(followService, times(1)).getFollowersWithCountEqualsToOne(followerId);
    }
}
