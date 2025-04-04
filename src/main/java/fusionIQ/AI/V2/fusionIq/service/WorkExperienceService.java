package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.PersonalDetails;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.WorkExperience;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.PersonalDetailsRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.repository.WorkExperienceRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkExperienceService {

    @Autowired
    private WorkExperienceRepo workExperienceRepository;

    @Autowired
    private PersonalDetailsRepo personalDetailsRepository;

    @Autowired
    private UserRepo userRepo;

    public WorkExperience updateWorkExperience(Long id, WorkExperience updatedWorkExperience) {
        Optional<WorkExperience> existingWorkExperience = workExperienceRepository.findById(id);
        if (existingWorkExperience.isPresent()) {
            WorkExperience workExperience = existingWorkExperience.get();
            copyNonNullProperties(updatedWorkExperience, workExperience);
            return workExperienceRepository.save(workExperience);
        } else {
            throw new ResourceNotFoundException("WorkExperience not found with id: " + id);
        }
    }

    public void deleteWorkExperience(Long id) {
        if (workExperienceRepository.existsById(id)) {
            workExperienceRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("WorkExperience not found with id: " + id);
        }
    }

    private void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    // Get the names of properties that are null
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public WorkExperience saveWorkExperience(Long userId, WorkExperience workExperience) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        workExperience.setUser(user); // Set the user in the work experience entity
        return workExperienceRepository.save(workExperience);
    }

    public List<WorkExperience> getWorkExperienceByUserId(Long userId) {
        return workExperienceRepository.findByUserId(userId);
    }
    public List<Map<String, Object>> getWorkExperienceOnlyByUserId(Long userId) {
        List<WorkExperience> workExperiences = workExperienceRepository.findByUserId(userId);

        return workExperiences.stream().map(workExperience -> {
            Map<String, Object> result = new HashMap<>();
            result.put("id", workExperience.getId());
            result.put("workCompanyName", workExperience.getWorkCompanyName());
            result.put("workStartDate", workExperience.getWorkStartDate() != null ? workExperience.getWorkStartDate().toString() : null);
            result.put("workEndDate", workExperience.getWorkEndDate() != null ? workExperience.getWorkEndDate().toString() : null);
            result.put("workDescription", workExperience.getWorkDescription());
            result.put("workRole", workExperience.getWorkRole());
            result.put("currentlyWorking", workExperience.getCurrentlyWorking());
            result.put("yearsOfExperience", workExperience.getYearsOfExperience());
            result.put("createdAt", workExperience.getCreatedAt() != null ? workExperience.getCreatedAt().toString() : null);
            return result;
        }).collect(Collectors.toList());
    }




    public WorkExperience getWorkExperienceByUserIdAndWorkExperienceId(Long userId, Long workExperienceId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        return workExperienceRepository.findById(workExperienceId)
                .filter(workExperience -> workExperience.getUser().getId() == userId)  // Use == for primitive long comparison
                .orElseThrow(() -> new RuntimeException("WorkExperience not found with id: " + workExperienceId + " for userId: " + userId));
    }
    public WorkExperience updateWorkExperience(Long userId, Long workExperienceId, WorkExperience updatedWorkExperience) {
        Optional<WorkExperience> existingWorkExperience = workExperienceRepository.findById(workExperienceId);

        if (existingWorkExperience.isPresent()) {
            WorkExperience workExperience = existingWorkExperience.get();

            // Check if workExperience is associated with the provided userId
            if (workExperience.getUser() != null && Long.valueOf(workExperience.getUser().getId()).equals(userId)) {
                copyNonNullProperties(updatedWorkExperience, workExperience);
                return workExperienceRepository.save(workExperience);
            } else {
                throw new IllegalArgumentException("User ID does not match the owner of the WorkExperience.");
            }
        } else {
            throw new ResourceNotFoundException("WorkExperience not found with id: " + workExperienceId);
        }
    }
}
