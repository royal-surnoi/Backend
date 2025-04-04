package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Follow;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping("/{id}")
    public ResponseEntity<Follow> getFollowById(@PathVariable Long id) {
        return followService.getFollowById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFollow(@PathVariable Long id) {
        followService.deleteFollow(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/incrementCounts/{followerId}/{followingId}")
    public ResponseEntity<Void> incrementCounts(@PathVariable Long followerId, @PathVariable Long followingId) {
        followService.incrementFollowerAndFollowingCounts(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decrementCounts/{followerId}/{followingId}")
    public ResponseEntity<Void> decrementCounts(@PathVariable Long followerId, @PathVariable Long followingId) {
        followService.decrementFollowerAndFollowingCounts(followerId, followingId);
        return ResponseEntity.ok().build();
    }




    @GetMapping("/sumFollowerCounts/{followerId}")
    public ResponseEntity<Long> sumFollowerCounts(@PathVariable Long followerId) {
        Long sum = followService.sumFollowerCountByFollowerId(followerId);
        return ResponseEntity.ok(sum);
    }

    @GetMapping("/sumFollowingCounts/{followingId}")
    public ResponseEntity<Long> sumFollowingCounts(@PathVariable Long followingId) {
        Long sum = followService.sumFollowingCountByFollowingId(followingId);
        return ResponseEntity.ok(sum);
    }


    @GetMapping("/countByFollowingId/{followingId}")
    public ResponseEntity<Long> countFollowRequestsByFollowingId(@PathVariable Long followingId) {
        Long count = followService.countFollowRequestsByFollowingId(followingId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/followerIdsByFollowingId/{followingId}")
    public ResponseEntity<List<Long>> getFollowerIdsByFollowingId(@PathVariable Long followingId) {
        List<Long> followerIds = followService.getFollowerIdsByFollowingId(followingId);
        return ResponseEntity.ok(followerIds);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllFollows() {
        followService.deleteAllFollows();
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/followingIdByFollowerIdWithCountOne/{followerId}")
    public ResponseEntity<Long> getFollowingIdByFollowerIdWithCountOne(@PathVariable Long followerId) {
        Optional<Long> followingIdOpt = followService.getFollowingIdByFollowerIdWithCountOne(followerId);
        return followingIdOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/FollwersofFirstFollwer/{followerId}")
    public ResponseEntity<List<Follow>> follwersoffirstfollwer(@PathVariable Long followerId) {
        List<Follow> followers = followService.getFollowersWithCountEqualsToOne(followerId);
        return ResponseEntity.ok(followers);
    }
    @GetMapping("/followerIdByFollowingIdWithCountOne/{followingId}")
    public ResponseEntity<Long> getFollowerIdByFollowingIdWithCountOne(@PathVariable Long followingId) {
        Optional<Long> followerIdOpt = followService.getFollowerIdByFollowingIdWithCountOne(followingId);
        return followerIdOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/saveByIds")
    public ResponseEntity<Object> createFollowByIds(@RequestParam Long followerId, @RequestParam Long followingId) {
        Follow savedFollow = followService.createFollow(followerId, followingId);

        if (savedFollow != null) {
            return ResponseEntity.ok(savedFollow);
        } else {
            return ResponseEntity.badRequest().body("Follow request already sent");
        }
    }
    @GetMapping("/findfollowerIdsByFollowingId/{followingId}")
    public ResponseEntity<List<Long>> findFollowerIdsByFollowingId(@PathVariable Long followingId) {
        List<Long> followerIds = followService.getFollowerIdsByFollowingId(followingId);
        return ResponseEntity.ok(followerIds);
    }

    @GetMapping("/followersWithCountEqualsToOne/{followerId}")
    public ResponseEntity<List<Follow>> getFollowersWithCountEqualsToOne(@PathVariable Long followerId) {
        List<Follow> followers = followService.getFollowersWithCountEqualsToOne(followerId);
        return ResponseEntity.ok(followers);
    }

    @DeleteMapping("/deleteByIds")
    public ResponseEntity<Void> deleteByFollowerAndFollowing(@RequestParam Long followerId, @RequestParam Long followingId) {
        followService.deleteByFollowerAndFollowing(followerId, followingId);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/zero-followers-following/followings/{followingId}")
    public List<Long> getAllFollowerIdsWithZeroFollowersAndFollowingByFollowingId(@PathVariable Long followingId) {
        return followService.getAllFollowerIdsWithFollowerAndFollowingCountZeroByFollowingId(followingId);
    }

    @GetMapping("/zero-following-followers/followers/{followerId}")
    public List<Long> getAllFollowingIdsWithZeroFollowingAndFollowersByFollowerId(@PathVariable Long followerId) {
        return followService.getAllFollowingIdsWithFollowingAndFollowersCountZeroByFollowerId(followerId);
    }

    @GetMapping("/followingWithCountEqualsToOne/{followingId}")
    public ResponseEntity<List<Follow>> getFollowingWithCountEqualsToOne(@PathVariable Long followingId) {
        List<Follow> followers = followService.getFollowingWithCountEqualsToOne(followingId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/followers/teacher/{teacherId}")
    public List<User> getFollowersByTeacherId(@PathVariable Long teacherId) {
        return followService.getFollowersByTeacherId(teacherId);
    }

    @GetMapping("/details/{userId}")
    public Map<String, List<Follow>> getFollowDetails(@PathVariable Long userId) {
        return followService.getFollowDetails(userId);
    }

    @GetMapping("/findFollowingIdsByFollowerId/{followerId}")
    public ResponseEntity<List<Long>> findFollowingIdsByFollowerId(@PathVariable Long followerId) {
        List<Long> followingIds = followService.getFollowingIdsByFollowerId(followerId);
        return ResponseEntity.ok(followingIds);
    }

}
