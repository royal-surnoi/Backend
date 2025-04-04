package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.VideoLike;
import fusionIQ.AI.V2.fusionIq.repository.VideoLikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoLikeService {

    @Autowired
    private VideoLikeRepo videoLikeRepo;

    public List<VideoLike> getAllVideoLikes() {
        return videoLikeRepo.findAll();
    }

    public Optional<VideoLike> getVideoLikeById(long id) {
        return videoLikeRepo.findById(id);
    }

    public VideoLike createVideoLike(VideoLike videoLike) {
        return videoLikeRepo.save(videoLike);
    }

    public VideoLike updateVideoLike(long id, VideoLike videoLikeDetails) {
        Optional<VideoLike> optionalVideoLike = videoLikeRepo.findById(id);

        if (optionalVideoLike.isPresent()) {
            VideoLike videoLike = optionalVideoLike.get();
            videoLike.setUser(videoLikeDetails.getUser());
            videoLike.setShortVideo(videoLikeDetails.getShortVideo());
            videoLike.setLongVideo(videoLikeDetails.getLongVideo());
            return videoLikeRepo.save(videoLike);
        } else {
            throw new RuntimeException("VideoLike not found with id " + id);
        }
    }

    public void deleteVideoLike(long id) {
        videoLikeRepo.deleteById(id);
    }
}
