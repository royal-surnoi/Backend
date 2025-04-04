package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.Follow;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class FollowTest {

    private User follower;
    private User following;

    @BeforeEach
    void setUp() {
        follower = new User();
        follower.setId(1L);

        following = new User();
        following.setId(2L);
    }

    @Test
    void testFollowConstructor() {
        Follow follow = new Follow(follower, following);

        assertThat(follow.getFollower()).isEqualTo(follower);
        assertThat(follow.getFollowing()).isEqualTo(following);
        assertThat(follow.getFollowerCount()).isEqualTo(0L);
        assertThat(follow.getFollowingCount()).isEqualTo(0L);
        assertThat(follow.getRequestedTime()).isNotNull();
    }

    @Test
    void testFollowDefaultConstructor() {
        Follow follow = new Follow();

        assertThat(follow.getFollowerCount()).isEqualTo(0L);
        assertThat(follow.getFollowingCount()).isEqualTo(0L);
        assertThat(follow.getRequestedTime()).isNotNull();
    }

    @Test
    void testSetAndGetFollower() {
        Follow follow = new Follow();
        follow.setFollower(follower);

        assertThat(follow.getFollower()).isEqualTo(follower);
    }

    @Test
    void testSetAndGetFollowing() {
        Follow follow = new Follow();
        follow.setFollowing(following);

        assertThat(follow.getFollowing()).isEqualTo(following);
    }

    @Test
    void testSetAndGetFollowerCount() {
        Follow follow = new Follow();
        follow.setFollowerCount(10L);

        assertThat(follow.getFollowerCount()).isEqualTo(10L);
    }

    @Test
    void testSetAndGetFollowingCount() {
        Follow follow = new Follow();
        follow.setFollowingCount(5L);

        assertThat(follow.getFollowingCount()).isEqualTo(5L);
    }

    @Test
    void testSetAndGetRequestedTime() {
        LocalDateTime now = LocalDateTime.now();
        Follow follow = new Follow();
        follow.setRequestedTime(now);

        assertThat(follow.getRequestedTime()).isEqualTo(now);
    }
}