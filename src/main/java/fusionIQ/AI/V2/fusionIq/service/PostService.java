package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Posts;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.PostRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    public Posts createImagePost(long userId, byte[] photo) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Posts post = new Posts();
        post.setPhoto(photo);
        post.setUser(user);
        post.setPostDate(LocalDateTime.now());
        return postRepo.save(post);
    }

    public Posts createArticlePost(long userId, String article) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Posts post = new Posts();
        post.setArticle(article);
        post.setUser(user);
        post.setPostDate(LocalDateTime.now());
        return postRepo.save(post);
    }
}
