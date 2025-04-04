package fusionIQ.AI.V2.fusionIq.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PersonalDetailsService {

    @Autowired
    private PersonalDetailsRepo personalDetailsRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ArticlePostRepo articlePostRepo;

    @Autowired
    private ImagePostRepo imagePostRepo;

    @Autowired
    private ShortVideoRepo shortVideoRepo;

    @Autowired
    private LongVideoRepo longVideoRepo;

    @Autowired
    private AIFeedRepo aiFeedRepo;
@Autowired
private  EducationRepo educationRepo;
@Autowired
private WorkExperienceRepo workExperienceRepo;
    @Autowired
    private UserProjectsRepo userProjectsRepo;

    @Autowired
    private UserSkillsRepo userSkillsRepo;

    @Autowired
    public PersonalDetailsService(PersonalDetailsRepo personalDetailsRepo, UserRepo userRepo) {
        this.personalDetailsRepo = personalDetailsRepo;
        this.userRepo = userRepo;
    }

    public PersonalDetails savePersonalDetails(Long userId, PersonalDetails personalDetails) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            personalDetails.setUser(user);
            checkAndSetProfileComplete(personalDetails);
            return personalDetailsRepo.save(personalDetails);
        } else {
            throw new RuntimeException("User not found");
        }
    }
    public PersonalDetails savePersonalDetails(
            Long userId,
            String firstName,
            String lastName,
            String userDescription,
            String userLanguage,
            String profession,
            Date dateOfBirth,
            String phoneNumber,
            String permanentAddress,
            String permanentCity,
            String permanentState,
            String permanentCountry,
            String permanentZipcode,
            String interests,
            Double longitude,
            Double latitude,
            String skills,
            String gender,
            Integer age,
            boolean isProfileComplete,
            LocalDateTime createdAt,
            PersonalDetails.Theme theme,
            String selfIntroductionVideo,
            String s3Key,
            String s3Url,
            byte[] resumeBytes,
            byte[] bannerImageBytes) {

        // Find the user by userId
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Create a new PersonalDetails object and set all fields
            PersonalDetails personalDetails = new PersonalDetails();
            personalDetails.setUser(user);
            personalDetails.setFirstName(firstName);
            personalDetails.setLastName(lastName);
            personalDetails.setUserDescription(userDescription);
            personalDetails.setUserLanguage(userLanguage);
            personalDetails.setProfession(profession);
            personalDetails.setDateOfBirth(dateOfBirth);
            personalDetails.setPhoneNumber(phoneNumber);
            personalDetails.setPermanentAddress(permanentAddress);
            personalDetails.setPermanentCity(permanentCity);
            personalDetails.setPermanentState(permanentState);
            personalDetails.setPermanentCountry(permanentCountry);
            personalDetails.setPermanentZipcode(permanentZipcode);
            personalDetails.setInterests(interests);
            personalDetails.setLongitude(longitude);
            personalDetails.setLatitude(latitude);
            personalDetails.setSkills(skills);
            personalDetails.setGender(gender);
            personalDetails.setAge(age);
            personalDetails.setProfileComplete(isProfileComplete);
            personalDetails.setCreatedAt(createdAt);
            personalDetails.setTheme(theme);
            personalDetails.setResume(resumeBytes);
            personalDetails.setBannerImage(bannerImageBytes);
            checkAndSetProfileComplete(personalDetails); // Ensure this method is implemented
            return personalDetailsRepo.save(personalDetails);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public PersonalDetails getPersonalDetailsByUserId(Long userId) {
        Optional<PersonalDetails> optional = personalDetailsRepo.findByUserId(userId);
        return optional.orElse(null);
    }

    public PersonalDetails getPersonalDetailsById(Long id) {
        Optional<PersonalDetails> optional = personalDetailsRepo.findById(id);
        return optional.orElse(null);
    }

    public List<PersonalDetails> getAllPersonalDetails() {
        return personalDetailsRepo.findAll();
    }

    public void deletePersonalDetails(Long id) {
        personalDetailsRepo.deleteById(id);
    }

    public PersonalDetails updatePersonalDetails(Long id, PersonalDetails updatedDetails) {
        Optional<PersonalDetails> optional = personalDetailsRepo.findById(id);
        if (optional.isPresent()) {
            PersonalDetails existingDetails = optional.get();
            copyNonNullProperties(updatedDetails, existingDetails);
            checkAndSetProfileComplete(existingDetails);
            return personalDetailsRepo.save(existingDetails);
        } else {
            throw new RuntimeException("Personal details not found");
        }
    }

    private void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null || (srcValue instanceof Integer && ((Integer) srcValue == 0))) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames.toArray(new String[0]);
    }



    public void updateLocation(Long userId, Double latitude, Double longitude) {
        Optional<PersonalDetails> optional = personalDetailsRepo.findByUserId(userId);
        if (optional.isPresent()) {
            PersonalDetails personalDetails = optional.get();
            personalDetails.setLatitude(latitude);
            personalDetails.setLongitude(longitude);
            personalDetailsRepo.save(personalDetails);
        } else {
            throw new RuntimeException("Personal details not found for userId: " + userId);
        }
    }
    public void checkAndSetProfileComplete(PersonalDetails personalDetails) {
        boolean isComplete = personalDetails.getAge() != null && personalDetails.getAge() > 0 &&
                personalDetails.getInterests() != null && !personalDetails.getInterests().isEmpty() &&
                personalDetails.getLatitude() != null &&
                personalDetails.getLongitude() != null &&
                personalDetails.getPermanentAddress() != null && !personalDetails.getPermanentAddress().isEmpty() &&
                personalDetails.getPermanentCity() != null && !personalDetails.getPermanentCity().isEmpty() &&
                personalDetails.getPermanentCountry() != null && !personalDetails.getPermanentCountry().isEmpty() &&
                personalDetails.getPermanentState() != null && !personalDetails.getPermanentState().isEmpty() &&
                personalDetails.getPermanentZipcode() != null && !personalDetails.getPermanentZipcode().isEmpty() &&
                personalDetails.getSkills() != null && !personalDetails.getSkills().isEmpty() &&
                personalDetails.getUserLanguage() != null && !personalDetails.getUserLanguage().isEmpty() &&
                personalDetails.getUserDescription() != null && !personalDetails.getUserDescription().isEmpty() &&
                personalDetails.getProfession() != null && !personalDetails.getProfession().isEmpty();

        personalDetails.setProfileComplete(isComplete);
        personalDetailsRepo.save(personalDetails);
    }

    public PersonalDetails findByUserId(Long userId) {
        PersonalDetails details = personalDetailsRepo.findByUserId(userId).orElse(null);
        if (details != null) {
            checkAndSetProfileComplete(details);
        }
        return details;
    }

    public Map<String, Object> getPersonalDetailsWithUserFields(Long userId) {
        List<Object[]> result = personalDetailsRepo.findPersonalDetailsAndUserFieldsByUserId(userId);

        if (result.isEmpty()) {
            throw new RuntimeException("No personal details found for user with id: " + userId);
        }

        Object[] fields = result.get(0);

        Map<String, Object> detailsMap = new HashMap<>();
        detailsMap.put("profession", fields[0]);
        detailsMap.put("userLanguage", fields[1]);
        detailsMap.put("userDescription", fields[2]);
        detailsMap.put("age", fields[3]);
        detailsMap.put("latitude", fields[4]);
        detailsMap.put("longitude", fields[5]);
        detailsMap.put("interests", fields[6]);
        detailsMap.put("name", fields[7]);
        detailsMap.put("email", fields[8]);
        detailsMap.put("userImage", fields[9]);
        detailsMap.put("userId", fields[10]);

        return detailsMap;
    }

    public List<Map<String, Object>> getSelectedPersonalDetails() {
        List<Object[]> results = personalDetailsRepo.findSelectedPersonalDetails();
        List<Map<String, Object>> mappedResults = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("userId", row[0]);
            userDetails.put("age", row[1]);
            userDetails.put("longitude", row[2]);
            userDetails.put("latitude", row[3]);
            userDetails.put("interests", row[4]);

            mappedResults.add(userDetails);
        }

        return mappedResults;

    }

    public void createAiFeed(Long userId, String feedType, boolean feedInteraction, LocalDateTime createdAt,
                             Long articleId, Long imageId, Long shortVideoId, Long longVideoId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        AiFeed aiFeed = new AiFeed();
        aiFeed.setUser(user);
        aiFeed.setFeedType(feedType);
        aiFeed.setFeedInteraction(feedInteraction);
        aiFeed.setCreatedAt(createdAt);

        if (articleId != null) {
            ArticlePost articlePost = articlePostRepo.findById(articleId).orElse(null);
            aiFeed.setArticlePost(articlePost);
        }

        if (imageId != null) {
            ImagePost imagePost = imagePostRepo.findById(imageId).orElse(null);
            aiFeed.setImagePost(imagePost);
        }

        if (shortVideoId != null) {
            ShortVideo shortVideo = shortVideoRepo.findById(shortVideoId).orElse(null);
            aiFeed.setShortVideo(shortVideo);
        }

        if (longVideoId != null) {
            LongVideo longVideo = longVideoRepo.findById(longVideoId).orElse(null);
            aiFeed.setLongVideo(longVideo);
        }

        aiFeedRepo.save(aiFeed);
    }

    public PersonalDetails uploadResume(Long userId, MultipartFile file) throws IOException {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found with id " + userId);
        }

        User user = userOptional.get();
        Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepo.findByUserId(userId);
        PersonalDetails personalDetails = personalDetailsOptional.orElseThrow(() -> new UserNotFoundException("Personal details not found for user with id " + userId));

        // Convert MultipartFile to byte array
        byte[] resumeData = file.getBytes();
        personalDetails.setResume(resumeData);

        return personalDetailsRepo.save(personalDetails);
    }

    public PersonalDetails updateTheme(Long userId, PersonalDetails.Theme theme) {
        Optional<PersonalDetails> detailsOptional = personalDetailsRepo.findByUserId(userId);
        if (detailsOptional.isPresent()) {
            PersonalDetails personalDetails = detailsOptional.get();
            personalDetails.setTheme(theme);
            return personalDetailsRepo.save(personalDetails);
        } else {
            throw new RuntimeException("Personal details not found for user ID: " + userId);
        }
    }

    // Method to update banner image
    public PersonalDetails updateBannerImage(Long userId, MultipartFile bannerImage) throws IOException {
        Optional<PersonalDetails> detailsOptional = personalDetailsRepo.findByUserId(userId);
        if (detailsOptional.isPresent()) {
            PersonalDetails personalDetails = detailsOptional.get();
            personalDetails.setBannerImage(bannerImage.getBytes());
            return personalDetailsRepo.save(personalDetails);
        } else {
            throw new RuntimeException("Personal details not found for user ID: " + userId);
        }
    }


    public byte[] getResumeByUserId(Long userId) {
        Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepo.findByUserId(userId);
        PersonalDetails personalDetails = personalDetailsOptional.orElseThrow(() -> new UserNotFoundException("Personal details not found for user with id " + userId));

        if (personalDetails.getResume() == null) {
            throw new UserNotFoundException("No resume found for user with id " + userId);
        }

        return personalDetails.getResume();
    }

    @Transactional
    public boolean updateSomePersonalDetails(Long id, PersonalDetails updatedDetails) {
        Optional<PersonalDetails> optionalDetails = personalDetailsRepo.findById(id);

        if (optionalDetails.isPresent()) {
            PersonalDetails existingDetails = optionalDetails.get();

            // Update only non-null fields
            if (updatedDetails.getFirstName() != null) {
                existingDetails.setFirstName(updatedDetails.getFirstName());
            }
            if (updatedDetails.getLastName() != null) {
                existingDetails.setLastName(updatedDetails.getLastName());
            }
            if (updatedDetails.getUserDescription() != null) {
                existingDetails.setUserDescription(updatedDetails.getUserDescription());
            }
            if (updatedDetails.getUserLanguage() != null) {
                existingDetails.setUserLanguage(updatedDetails.getUserLanguage());
            }
            if (updatedDetails.getProfession() != null) {
                existingDetails.setProfession(updatedDetails.getProfession());
            }
            if (updatedDetails.getDateOfBirth() != null) {
                existingDetails.setDateOfBirth(updatedDetails.getDateOfBirth());
            }
            if (updatedDetails.getPhoneNumber() != null) {
                existingDetails.setPhoneNumber(updatedDetails.getPhoneNumber());
            }
            if (updatedDetails.getPermanentAddress() != null) {
                existingDetails.setPermanentAddress(updatedDetails.getPermanentAddress());
            }
            if (updatedDetails.getPermanentCity() != null) {
                existingDetails.setPermanentCity(updatedDetails.getPermanentCity());
            }
            if (updatedDetails.getPermanentState() != null) {
                existingDetails.setPermanentState(updatedDetails.getPermanentState());
            }
            if (updatedDetails.getPermanentCountry() != null) {
                existingDetails.setPermanentCountry(updatedDetails.getPermanentCountry());
            }
            if (updatedDetails.getPermanentZipcode() != null) {
                existingDetails.setPermanentZipcode(updatedDetails.getPermanentZipcode());
            }
            if (updatedDetails.getInterests() != null) {
                existingDetails.setInterests(updatedDetails.getInterests());
            }
            if (updatedDetails.getSkills() != null) {
                existingDetails.setSkills(updatedDetails.getSkills());
            }
            if (updatedDetails.getGender() != null) {
                existingDetails.setGender(updatedDetails.getGender());
            }
            if (updatedDetails.getAge() != null) {
                existingDetails.setAge(updatedDetails.getAge());
            }

            // Save updated details
            personalDetailsRepo.save(existingDetails);
            return true;
        }

        // Return false if no record found
        return false;
    }
    public PersonalDetailsService(PersonalDetailsRepo personalDetailsRepo, EducationRepo educationRepo,
                                  WorkExperienceRepo workExperienceRepo, UserProjectsRepo userProjectsRepo,
                                  UserSkillsRepo userSkillsRepo) {
        this.personalDetailsRepo = personalDetailsRepo;
        this.educationRepo = educationRepo;
        this.workExperienceRepo = workExperienceRepo;
        this.userProjectsRepo = userProjectsRepo;
        this.userSkillsRepo = userSkillsRepo;
    }

    public Map<String, Object> getUserProfile(Long userId) {
        Map<String, Object> userProfile = new HashMap<>();

        // Fetch data from repositories
        Optional<PersonalDetails> personalDetails = personalDetailsRepo.findByUserId(userId);
        List<Education> education = educationRepo.findByUserId(userId);
        List<WorkExperience> workExperiences = workExperienceRepo.findByUserId(userId);
        List<UserProjects> userProjects = userProjectsRepo.findByUserId(userId);
        List<UserSkills> userSkills = userSkillsRepo.findByUserId(userId);

        // Remove unwanted fields before adding data to response
        personalDetails.ifPresent(details -> {
            details.setResume(null); // Remove resume
            details.setBannerImage(null); // Remove bannerImage
            if (details.getUser() != null) {
                details.getUser().setUserImage(null); // Remove userImage
            }
        });

        // Remove `user` details from WorkExperience
        workExperiences.forEach(work -> work.setUser(null));

        // Remove `user` details from UserProjects
        userProjects.forEach(project -> project.setUser(null));

        // Remove `user` details from UserSkills
        userSkills.forEach(skill -> skill.setUser(null));

        userProjects.forEach(project -> project.setProjectImage(null)); // Remove projectImage
        userProjects.forEach(project -> project.setProjectVideo(null));

        workExperiences.forEach(work -> {
            if (work.getUser() != null) {
                work.getUser().setUserImage(null); // Remove userImage from workExperience
            }
        });

        userSkills.forEach(skill -> {
            if (skill.getUser() != null) {
                skill.getUser().setUserImage(null); // Remove userImage from userSkills
            }
        });

        // Add cleaned data to response
        userProfile.put("personalDetails", personalDetails.orElse(null));
        userProfile.put("education", education);
        userProfile.put("workExperiences", workExperiences);
        userProfile.put("userProjects", userProjects);
        userProfile.put("userSkills", userSkills);

        return userProfile;
    }

}
