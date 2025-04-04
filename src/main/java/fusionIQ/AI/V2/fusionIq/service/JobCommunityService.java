package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.JobCommunity;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.JobCommunityRepository;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobCommunityService {

    @Autowired
    private JobCommunityRepository jobCommunityRepository;

    @Autowired
    private UserRepo userRepository;

    // Method to create a new community
    public JobCommunity createJobCommunity(Long userId, JobCommunity jobCommunity, MultipartFile communityImage) {
        // Fetch the user by userId
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Set the user in the JobCommunity object
        jobCommunity.setUser(user);

        // If a community image is provided, set it in the JobCommunity
        if (communityImage != null && !communityImage.isEmpty()) {
            try {
                jobCommunity.setCommunityImage(communityImage.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process the community image", e);
            }
        }

        // Save the JobCommunity to the database
        return jobCommunityRepository.save(jobCommunity);
    }


    // Method to add a user to an existing community
    public JobCommunity addUserToCommunity(Long communityId, Long userId) {
        // Fetch the community and user from the database
        JobCommunity jobCommunity = jobCommunityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the user is already a member to avoid duplicates
        if (!jobCommunity.getMembers().contains(user)) {
            jobCommunity.getMembers().add(user); // Add the user to the members list
            jobCommunity.setCommunityCount(jobCommunity.getCommunityCount() + 1); // Update the count
            jobCommunity.setUpdatedAt(LocalDateTime.now()); // Update the timestamp
        }

        // Save the updated community back to the database
        return jobCommunityRepository.save(jobCommunity);
    }


    public List<Map<String, Object>> getAllJobCommunities() {
        List<JobCommunity> communities = jobCommunityRepository.findAll();
        return communities.stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }


    private Map<String, Object> convertToMap(JobCommunity jobCommunity) {
        Map<String, Object> communityMap = new HashMap<>();

        communityMap.put("jobCommunityId", jobCommunity.getId());
        communityMap.put("communityDescription", jobCommunity.getCommunityDescription());
        communityMap.put("communityName", jobCommunity.getCommunityName());
        communityMap.put("createdAt", jobCommunity.getCreatedAt());
        communityMap.put("updatedAt", jobCommunity.getUpdatedAt());
        communityMap.put("userId", jobCommunity.getUser().getId());
        communityMap.put("communityCount", jobCommunity.getCommunityCount());

        // Ensure the communityImage is correctly added to the response map
        communityMap.put("communityImage", jobCommunity.getCommunityImage()); // This assumes communityImage is stored as a string (e.g., URL or base64 string)

        return communityMap;
    }

}
