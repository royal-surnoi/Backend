package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.PostCommunity;
import fusionIQ.AI.V2.fusionIq.service.PostCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post-community")
public class PostCommunityController {

    @Autowired
    private PostCommunityService postCommunityService;

//    @GetMapping
//    public List<PostCommunity> getAllPostCommunities() {
//        return postCommunityService.getAllPostCommunities();
//    }

    @PostMapping("/askquestion/{userId}/{jobCommunityId}")
    public PostCommunity createPostCommunity(
            @PathVariable Long userId,
            @PathVariable Long jobCommunityId,
            @RequestBody PostCommunity postCommunity) {
        return postCommunityService.createPostCommunity(userId, jobCommunityId, postCommunity);
    }

//    @DeleteMapping("/{id}")
//    public void deletePostCommunity(@PathVariable Long id) {
//        postCommunityService.deletePostCommunity(id);
//    }
}
