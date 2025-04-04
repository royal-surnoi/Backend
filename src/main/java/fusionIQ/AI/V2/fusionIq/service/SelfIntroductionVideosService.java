package fusionIQ.AI.V2.fusionIq.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import fusionIQ.AI.V2.fusionIq.data.PersonalDetails;
import fusionIQ.AI.V2.fusionIq.data.SelfIntroductionVideos;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.PersonalDetailsRepo;
import fusionIQ.AI.V2.fusionIq.repository.SelfIntroductionVideosRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class SelfIntroductionVideosService {

    @Autowired
    private AmazonS3 amazonS3;

    private final String bucketName = "fusion-chat-bk1";
    private final String folderName = "SelfIntroductionVideos/";

    @Autowired
    private SelfIntroductionVideosRepo selfIntroductionVideosRepo;

    public SelfIntroductionVideosService(SelfIntroductionVideosRepo selfIntroductionVideosRepo) {
        this.selfIntroductionVideosRepo = selfIntroductionVideosRepo;
    }

    @Autowired
    private PersonalDetailsRepo personalDetailsRepo;

    @Autowired
    private UserRepo userRepo;

    public SelfIntroductionVideos uploadAllVideos(Long userId, MultipartFile personalVideo, MultipartFile educationVideo,
                                                  MultipartFile workExperienceVideo, MultipartFile achievementsVideo) throws IOException {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SelfIntroductionVideos videos = new SelfIntroductionVideos();

        // Upload each file and save both the S3 key and URI
        if (personalVideo != null) {
            videos.setSiPersonalS3Key(uploadToS3(personalVideo));
            videos.setSiPersonalS3Uri(getS3Uri(videos.getSiPersonalS3Key()));
        }
        if (educationVideo != null) {
            videos.setSiEducationS3Key(uploadToS3(educationVideo));
            videos.setSiEducationS3Uri(getS3Uri(videos.getSiEducationS3Key()));
        }
        if (workExperienceVideo != null) {
            videos.setSiWorkExperienceS3Key(uploadToS3(workExperienceVideo));
            videos.setSiWorkExperienceS3Uri(getS3Uri(videos.getSiWorkExperienceS3Key()));
        }
        if (achievementsVideo != null) {
            videos.setSiAchievementsS3Key(uploadToS3(achievementsVideo));
            videos.setSiAchievementsS3Uri(getS3Uri(videos.getSiAchievementsS3Key()));
        }

        videos.setUser(user);
        user.setSelfIntroductionVideos(videos); // Assuming User entity has this relationship

        return selfIntroductionVideosRepo.save(videos);
    }

    private String uploadToS3(MultipartFile file) throws IOException {
        String fileName = folderName + UUID.randomUUID() + "-" + file.getOriginalFilename();
        File convertedFile = convertMultiPartToFile(file);
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, convertedFile));
        if (!convertedFile.delete()) {
            System.err.println("Warning: Temporary file " + convertedFile.getAbsolutePath() + " could not be deleted.");
        }

        return fileName;
    }

    private String getS3Uri(String s3Key) {
        return amazonS3.getUrl(bucketName, s3Key).toString();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }


    public void createOrUpdateTimestampsByUserId(Long userId, Map<String, BigInteger> timestamps) {
        // Fetch the video entity by userId
        SelfIntroductionVideos video = selfIntroductionVideosRepo.findByUserId(userId);

        if (video == null) {
            throw new RuntimeException("Video for userId " + userId + " not found");
        }

        // Update timestamps based on the input map
        video.setSelfIntroTimestamp(timestamps.getOrDefault("selfIntroTimestamp", video.getSelfIntroTimestamp()));
        video.setEducationTimestamp(timestamps.getOrDefault("educationTimestamp", video.getEducationTimestamp()));
        video.setWorkExpTimestamp(timestamps.getOrDefault("workExpTimestamp", video.getWorkExpTimestamp()));
        video.setAchievementsTimestamp(timestamps.getOrDefault("achievementsTimestamp", video.getAchievementsTimestamp()));

        // Save the updated entity
        selfIntroductionVideosRepo.save(video);
    }

    public Map<String, Object> getSelfIntroductionVideosByUserId(Long userId) {
        Optional<SelfIntroductionVideos> optionalVideo = selfIntroductionVideosRepo.findBySelfIntroductionVideoUserId(userId);

        if (optionalVideo.isPresent()) {
            SelfIntroductionVideos video = optionalVideo.get();
            Map<String, Object> response = new HashMap<>();
            response.put("siCombinedS3Key", video.getSiCombinedS3Key());
            response.put("siCombinedS3Uri", video.getSiCombinedS3Uri());
            response.put("selfIntroTimestamp", video.getSelfIntroTimestamp());
            response.put("educationTimestamp", video.getEducationTimestamp());
            response.put("workExpTimestamp", video.getWorkExpTimestamp());
            response.put("achievementsTimestamp", video.getAchievementsTimestamp());
            return response;
        } else {
            throw new RuntimeException("No data found for the given userId: " + userId);
        }
    }
    public SelfIntroductionVideos uploadCombinedVideo(Long userId, MultipartFile combinedVideo) throws IOException {
        SelfIntroductionVideos videos = selfIntroductionVideosRepo.findByUserId(userId);
//                .orElseThrow(() -> new RuntimeException("SelfIntroductionVideos not found for id: " + userId));

        // Upload the combined video to S3 and update the S3 key and URI
        if (combinedVideo != null) {
            String combinedS3Key = uploadToS3(combinedVideo);
            String combinedS3Uri = getS3Uri(combinedS3Key);
            videos.setSiCombinedS3Key(combinedS3Key);
            videos.setSiCombinedS3Uri(combinedS3Uri);
        }

        return selfIntroductionVideosRepo.save(videos);
    }

    public SelfIntroductionVideos updateVideos(Long userId, MultipartFile personalVideo, MultipartFile educationVideo,
                                               MultipartFile workExperienceVideo, MultipartFile achievementsVideo) throws IOException {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SelfIntroductionVideos videos = selfIntroductionVideosRepo.findByUserId2(user.getId()).orElse(new SelfIntroductionVideos());

        // Personal Video
        if (personalVideo != null) {
            videos.setSiPersonalS3Key(uploadToS3(personalVideo));
            videos.setSiPersonalS3Uri(getS3Uri(videos.getSiPersonalS3Key()));
        } else if (videos.getSiPersonalS3Key() == null) {
            throw new RuntimeException("Personal video is required if no existing link is present.");
        }

        // Education Video
        if (educationVideo != null) {
            videos.setSiEducationS3Key(uploadToS3(educationVideo));
            videos.setSiEducationS3Uri(getS3Uri(videos.getSiEducationS3Key()));
        } else if (videos.getSiEducationS3Key() == null) {
            throw new RuntimeException("Education video is required if no existing link is present.");
        }

        // Work Experience Video
        if (workExperienceVideo != null) {
            videos.setSiWorkExperienceS3Key(uploadToS3(workExperienceVideo));
            videos.setSiWorkExperienceS3Uri(getS3Uri(videos.getSiWorkExperienceS3Key()));
        } else if (videos.getSiWorkExperienceS3Key() == null) {
            throw new RuntimeException("Work experience video is required if no existing link is present.");
        }

        // Achievements Video
        if (achievementsVideo != null) {
            videos.setSiAchievementsS3Key(uploadToS3(achievementsVideo));
            videos.setSiAchievementsS3Uri(getS3Uri(videos.getSiAchievementsS3Key()));
        } else if (videos.getSiAchievementsS3Key() == null) {
            throw new RuntimeException("Achievements video is required if no existing link is present.");
        }

        // Set user and save the updated entity
        videos.setUser(user);
        user.setSelfIntroductionVideos(videos); // Assuming bidirectional relationship

        SelfIntroductionVideos updatedVideos = selfIntroductionVideosRepo.save(videos);


        return updatedVideos;

    }
}
