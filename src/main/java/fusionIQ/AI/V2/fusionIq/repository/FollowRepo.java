package fusionIQ.AI.V2.fusionIq.repository;



import fusionIQ.AI.V2.fusionIq.data.Follow;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepo extends JpaRepository<Follow, Long> {

    void deleteAll();


    @Query("SELECT SUM(f.followerCount) FROM Follow f WHERE f.follower.id = :followerId")
    Long sumFollowerCountByFollowerId(@Param("followerId") Long followerId);

    @Query("SELECT SUM(f.followingCount) FROM Follow f WHERE f.following.id = :followingId")
    Long sumFollowingCountByFollowingId(@Param("followingId") Long followingId);

    @Modifying
    @Query("UPDATE Follow f SET f.followerCount = 1 WHERE f.follower.id = :followerId AND f.following.id = :followingId")
    void incrementFollowerCount(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Modifying
    @Query("UPDATE Follow f SET f.followingCount = 1 WHERE f.follower.id = :followerId AND f.following.id = :followingId")
    void incrementFollowingCount(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Modifying
    @Query("UPDATE Follow f SET f.followerCount = 0 WHERE f.follower.id = :followerId AND f.following.id = :followingId")
    void decrementFollowerCount(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Modifying
    @Query("UPDATE Follow f SET f.followingCount = 0 WHERE f.follower.id = :followerId AND f.following.id = :followingId")
    void decrementFollowingCount(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    void deleteByFollowerId(Long id);

    void deleteByFollowingId(Long id);

    List<Follow> findByFollowerId(Long userId);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Long countByFollowing_Id(Long followingId);

    @Query("SELECT f.follower.id FROM Follow f WHERE f.following.id = :followingId")
    List<Long> findFollowerIdsByFollowingId(Long followingId);


    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId AND f.followingCount = 1 AND f.followerCount = 1")
    Optional<Long> findFollowingIdByFollowerIdWithCountOne(Long followerId);

    @Query("SELECT f.follower.id FROM Follow f WHERE f.following.id = :followingId AND f.followingCount = 1 AND f.followerCount = 1")
    Optional<Long> findFollowerIdByFollowingIdWithCountOne(Long followingId);
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    List<Follow> findByFollowerIdAndFollowerCountAndFollowingCount(Long followerId, Long followerCount, Long followingCount);

    @Query("SELECT f.follower.id FROM Follow f WHERE f.following.id = :followingId AND f.followerCount = 0 AND f.followingCount = 0")
    List<Long> findFollowerIdsByFollowingIdAndFollowerCountAndFollowingCountZero(@Param("followingId") Long followingId);

    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId AND f.followingCount = 0 AND f.followerCount = 0")
    List<Long> findFollowingIdsByFollowerIdAndFollowingCountAndFollowerCountZero(@Param("followerId") Long followerId);

    Follow findByFollowingId(Long followingId);
    List<Follow> findByFollowingIdAndFollowingCountAndFollowerCount(Long followingId, long l, long l1);

    @Query("SELECT f.follower FROM Follow f WHERE f.following.id = :teacherId")
    List<User> findFollowersByTeacherId(@Param("teacherId") Long teacherId);
    @Query("SELECT f FROM Follow f WHERE f.follower.id = :userId AND f.followerCount = 1")
    List<Follow> findFollowersByUserIdAndFollowerCount(@Param("userId") Long userId);

    @Query("SELECT f FROM Follow f WHERE f.following.id = :userId AND f.followingCount = 1")
    List<Follow> findFollowingByUserIdAndFollowingCount(@Param("userId") Long userId);



    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId")
    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);

}
