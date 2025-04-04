package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Posts;
import fusionIQ.AI.V2.fusionIq.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/createImage")
    public Posts createImagePost(@RequestParam long userId,
                                 @RequestParam MultipartFile photo) throws IOException {
        byte[] photoBytes = photo.getBytes();
        return postService.createImagePost(userId, photoBytes);
    }

    @PostMapping("/createArticle")
    public Posts createArticlePost(@RequestParam long userId,
                                   @RequestParam String article) {
        return postService.createArticlePost(userId, article);
    }
}
