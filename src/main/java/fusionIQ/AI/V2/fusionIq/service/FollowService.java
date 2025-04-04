package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Follow;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.FollowRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FollowService {
    @Autowired
    private FollowRepo followRepository;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private NotificationService notificationService;
    public Follow saveFollow(Follow follow) {
        return followRepository.save(follow);
    }

    public Optional<Follow> getFollowById(Long id) {
        return followRepository.findById(id);
    }

    public void deleteFollow(Long id) {
        followRepository.deleteById(id);
    }

    @Transactional
    public void incrementFollowerAndFollowingCounts(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new IllegalArgumentException("Follow request not found"));

        follow.setFollowerCount(follow.getFollowerCount() + 1);
        follow.setFollowingCount(follow.getFollowingCount() + 1);

        follow.setFollowed(true);
        follow.setRequested(false);

        followRepository.save(follow);

        notificationService.createFollowRequestAcceptedNotification(followerId, followingId);
    }

    @Transactional
    public void decrementFollowerAndFollowingCounts(Long followerId, Long followingId) {
        followRepository.decrementFollowerCount(followerId, followingId);
        followRepository.decrementFollowingCount(followerId, followingId);
    }

    public Long sumFollowerCountByFollowerId(Long followerId) {
        return followRepository.sumFollowerCountByFollowerId(followerId);
    }

    public Long sumFollowingCountByFollowingId(Long followingId) {
        return followRepository.sumFollowingCountByFollowingId(followingId);
    }



    public Long countFollowRequestsByFollowingId(Long followingId) {
        return followRepository.countByFollowing_Id(followingId);
    }

    public List<Long> getFollowerIdsByFollowingId(Long followingId) {
        return followRepository.findFollowerIdsByFollowingId(followingId);
    }

    public void deleteAllFollows() {
    }
    public Optional<Long> getFollowingIdByFollowerIdWithCountOne(Long followerId) {
        return followRepository.findFollowingIdByFollowerIdWithCountOne(followerId);
    }

    public Optional<Long> getFollowerIdByFollowingIdWithCountOne(Long followingId) {
        return followRepository.findFollowerIdByFollowingIdWithCountOne(followingId);
    }

    @Transactional
    public Follow createFollow(Long followerId, Long followingId) {
        // Check if follow request already exists
        Optional<Follow> existingFollow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (existingFollow.isPresent()) {
            return null; // Follow request already exists
        }

        User follower = userRepo.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower user not found"));
        User following = userRepo.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("Following user not found"));

        Follow follow = new Follow(follower, following);
        follow.setFollowerCount(0L);
        follow.setFollowingCount(0L);
        follow.setRequestedTime(LocalDateTime.now());
        follow.setRequested(true);
        follow.setFollowed(false);

        Follow savedFollow = followRepository.save(follow);

        notificationService.createFollowRequestNotification(followerId, followingId);

        return savedFollow;
    }

    @Transactional
    public List<Follow> getFollowersWithCountEqualsToOne(Long followerId) {
        return followRepository.findByFollowerIdAndFollowerCountAndFollowingCount(followerId, 1L, 1L);
    }

    public void deleteByFollowerAndFollowing(Long followerId, Long followingId) {
        Optional<Follow> follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        follow.ifPresent(followRepository::delete);
    }



    public List<Long> getAllFollowerIdsWithFollowerAndFollowingCountZeroByFollowingId(Long followingId) {
        return followRepository.findFollowerIdsByFollowingIdAndFollowerCountAndFollowingCountZero(followingId);
    }
    @Transactional
    public List<Follow> getFollowingWithCountEqualsToOne(Long followingId) {
        return followRepository.findByFollowingIdAndFollowingCountAndFollowerCount(followingId, 1L, 1L);
    }
    @Transactional
    public List<Follow> follwersofFirstFollwer(Long followerId) {
        List<Follow> Follwers = followRepository.findByFollowerIdAndFollowerCountAndFollowingCount(followerId, 1L, 1L);
        return followRepository.findByFollowerIdAndFollowerCountAndFollowingCount(Follwers.getFirst().getId(), 1L, 1L);
    }
    public List<Long> getAllFollowingIdsWithFollowingAndFollowersCountZeroByFollowerId(Long followerId) {
        return followRepository.findFollowingIdsByFollowerIdAndFollowingCountAndFollowerCountZero(followerId);
    }
    public List<User> getFollowersByTeacherId(Long teacherId) {
        return followRepository.findFollowersByTeacherId(teacherId);
    }
    public Map<String, List<Follow>> getFollowDetails(Long userId) {
        List<Follow> followers = followRepository.findFollowersByUserIdAndFollowerCount(userId);
        List<Follow> following = followRepository.findFollowingByUserIdAndFollowingCount(userId);
        Map<String, List<Follow>> followDetails = new HashMap<>();
        followDetails.put("followers", followers);
        followDetails.put("following", following);
        return followDetails;
    }

    public List<Long> getFollowingIdsByFollowerId(Long followerId) {
        return followRepository.findFollowingIdsByFollowerId(followerId);
    }

    public List<Long> getFollowingIdsByUserId(Long userId) {
        return followRepository.findFollowingIdsByFollowerId(userId);
    }

}