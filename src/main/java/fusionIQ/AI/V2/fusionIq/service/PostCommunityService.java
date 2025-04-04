package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.JobCommunity;
import fusionIQ.AI.V2.fusionIq.data.PostCommunity;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.JobCommunityRepository;
import fusionIQ.AI.V2.fusionIq.repository.PostCommunityRepository;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostCommunityService {

    @Autowired
    private PostCommunityRepository postCommunityRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private JobCommunityRepository jobCommunityRepository;

    public PostCommunity createPostCommunity(Long userId, Long jobCommunityId, PostCommunity postCommunity) {
        // Fetch the user and job community from their respective repositories
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        JobCommunity jobCommunity = jobCommunityRepository.findById(jobCommunityId)
                .orElseThrow(() -> new RuntimeException("Job Community not found"));

        // Set the associations in PostCommunity
        postCommunity.setUser(user);
        postCommunity.setJobCommunity(jobCommunity);

        // Save and return the new PostCommunity
        return postCommunityRepository.save(postCommunity);
    }

    public List<PostCommunity> getAllPostCommunities() {
        return postCommunityRepository.findAll();
    }

    public void deletePostCommunity(Long id) {
        postCommunityRepository.deleteById(id);
    }
}

